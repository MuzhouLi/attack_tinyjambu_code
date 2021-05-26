'''
This code is used to search all linear trails given specific linear hull for reduced-round TinyJambu utilizing the simple model, which only counts number of ANDs and then we compute its correlation according to Song et al.'s work.
'''

#!/usr/bin/python2
from __future__ import print_function
import sys, getopt, math

from gurobipy import *
from gurobifun import *

N = 128 # the words size.

def takeFirst(elem):
	return elem[0][1]

# Modelling tinyJambu constraints

def stateUptLin(mod, stin, stout, tmpapprox, r):
	""" Add the simple model onstraints of a single round of TinyJambu.
	Parameters:
	mod     - the Model
	stin    - the variables of the input state
	stout   - the variables of the output state
	r       - the current round number
	"""
	objectiveFun = []

	objectiveFun += [stin[0]]
	mod.addConstr(stout[127] == stin[0])
	j = r%15
	for i in xrange(2):
		mod.addConstr(tmpapprox[i] <= stin[0]) #Fix tmpapprox to 0 when not needed.
	for i in xrange(N-1):
		if(i == 84 or i == 69):
			addGenConstrXor(mod, [stout[i], stin[i+1], tmpapprox[ 0 if i == 69 else 1 ]], False)
		elif(i == 90 or i == 46):
			addGenConstrXor(mod, [stin[0], stin[i+1], stout[i]], False)
		else:
			mod.addConstr(stout[i] == stin[i+1])
	return objectiveFun

if __name__ == "__main__":
	try:
		opts, args=getopt.getopt(sys.argv[1:],"r:k:i:o:")
		if len(opts)!=4:
			print('usage: python2 -u Step1_search_all_trails_in_linear_hull.py -r rounds -k 128 -i 32bit_inmask_in_hex -o 32bit_outmask_in_hex > LinearTrails.txt')
			sys.exit(2)
	except getopt.GetoptError:
		sys.exit(2)
	for opt, arg in opts:
		if opt=='-r':
			nbrounds=int(arg)# number of rounds
		elif opt=='-k':
			klen=int(arg)# length of master key
		elif opt=='-i':
			inmask=int(arg,16)# 32-bit input mask
		elif opt=='-o':
			outmask=int(arg,16)# 32-bit output mask

	# building MILP model
	mod = Model("test")
	# Create state variables for every rounds.
	states = [get_new_variables_block(mod, N) for r in xrange(nbrounds+1)]
	approx = [[get_new_var(mod) for _ in xrange(2)] for r in xrange(nbrounds)]
	# Create extra variables
	extra_varaible=[]
	tmplist=[]
	list_ts=[0 for i in range(15)]
	for s in range(15):
		t_s=int(math.floor((nbrounds-1)/15.0))
		if s>(nbrounds-1)%15:
			t_s-=1
		list_ts[s]=t_s
		tmplist=[get_new_var(mod) for _ in xrange(3*t_s)]
		extra_varaible.append(tmplist)
	# building MILP model		
	mod.Params.PoolSearchMode = 2 #def: 0, sol first: 1, best sols: 2
	mod.Params.Threads = 16
	mod.Params.PoolSolutions = 1
	# Initialize the variable lists for the sum to be optimized.
	objFuns = []

	# Add the simple model constraints and objective function.
	for r in xrange(nbrounds):
		objFuns += stateUptLin(mod, states[r], states[r+1], approx[r], r)
	
	# Add constraints on extra_varaibles
	for s in range(15):
		t_s=list_ts[s]
		for j in range(t_s+1):
			and_variables(mod, states[s+15*j][0], approx[s+15*j][0], extra_varaible[s][j])
			and_variables(mod, states[s+15*j][0], approx[s+15*j][1], extra_varaible[s][j+t_s])
		for j in range(1,t_s+1):
			xor_var(mod, extra_varaible[s][j-1+2*t_s], extra_varaible[s][j-1+t_s], extra_varaible[s][j])
	
	# Add constraints on the relevant linear trail.
	not_all_zeros_constraint(mod, states[0])

	# Add constraints on the input/output mask of linear trails
	dstart=inmask<<64
	dend=outmask<<64
	
	#dstart=0x02400004081220010040102040000080
	#dend=  0x00000000000000010000000000000000

	for i in xrange(N):
		mod.addConstr(states[0][i] == (dstart >> i)&1)
		mod.addConstr(states[-1][i] == (dend >> i)&1)

	# Set objecive functions as sum of active AND gates.
	mod.setObjective(quicksum(objFuns))

	PossibleChainedAnds=[[] for i in range(15)] # List all possible chained Ands
	for i in range(70,85):
		for j in range(i,nbrounds+128-43,15):
			PossibleChainedAnds[i-70].append(j)
		print(PossibleChainedAnds[i-70])
	
	MaskOnAnds=[[] for i in range(15)] # Store masks on ANDs and linear parts (Refreshed under each trail.)
	for i in range(70,85):
		MaskOnAnds[i-70]=[0 for j in range(2*len(PossibleChainedAnds[i-70])-1)]
			
	trail=[] # Store all trails
	log_cor=[] # Store corresponding real signed correlation of the trail
	KeyMask=[] # Store corresponding round key masks
	tmpTrail=[]
	sol_counter=0
	goon=True
	constr=1
	while(goon):
		# Optimize.
		mod.addConstr(constr>=1)
		mod.update()
		mod.optimize()
		nSol = mod.SolCount
		if nSol>0:
			# Vrfiy whether this is a valid solution
			valid=True
			for r in range(0,len(states)-1):
				for j in range(128):
					if j in [0,47,70,85,91]:
						if j in [91,47]:
							if variables_block_to_int(states[r+1][j-1:j])^variables_block_to_int(states[r+1][127:128])!=variables_block_to_int(states[r][j:j+1]):
								valid=False
								break
						if j==0:
							if variables_block_to_int(states[r][j:j+1])!=variables_block_to_int(states[r+1][127:128]):
								valid=False
								break
					else:
						if variables_block_to_int(states[r+1][j-1:j])!=variables_block_to_int(states[r][j:j+1]):
							valid=False
							break
				if (variables_block_to_int(states[r+1][127:128])^1)&(1^(((variables_block_to_int(states[r+1][84:85])^variables_block_to_int(states[r][85:86]))^1)&((variables_block_to_int(states[r+1][69:70])^variables_block_to_int(states[r][70:71]))^1)))!=0:
					valid=False
				if not valid:
					break
			if valid:
				# Get chained Ands
				for i in range(70,85):
					MaskOnAnds[i-70]=[0 for j in range(2*len(PossibleChainedAnds[i-70])-1)]
				for r in range(0,len(states)-1):
					if variables_block_to_int(states[r+1][127:128])==1:
						MaskOnAnds[r%15][int(r/15)]=1
					if variables_block_to_int(states[r+1][69:70])^variables_block_to_int(states[r][70:71])==1:
						if int(r/15)==0:
							MaskOnAnds[r%15][len(PossibleChainedAnds[r%15])-1]=1
					if variables_block_to_int(states[r+1][84:85])^variables_block_to_int(states[r][85:86])==1:
						if int(r/15)!=0:
							MaskOnAnds[r%15][len(PossibleChainedAnds[r%15])-1+int(r/15)+1]=1
				# Compute real signed correlation of this trail and record it in list trail.
				tmpLogCor=0
				tmpSign=0 # 0 represents "+", while 1 represents "-".
				tmpSign_EachChained=0
				chained_list=[] # Store corresponding linear parts of chained Ands.
				chained_num=0
				zeroCor=False
				for i in range(70,85):
					start_j=0
					while start_j!=len(PossibleChainedAnds[i-70])-1:
						chained_num=0
						cond=0
						cond+=MaskOnAnds[i-70][len(PossibleChainedAnds[i-70])-1]
						chained_list=[]
						for j in range(start_j,len(PossibleChainedAnds[i-70])-1):
							if MaskOnAnds[i-70][j]==1:
								chained_num+=1
								if (chained_num-1)%2==0:
									cond+=MaskOnAnds[i-70][len(PossibleChainedAnds[i-70])-1+(chained_num-1)+2]
								if j==start_j:
									chained_list.append(MaskOnAnds[i-70][len(PossibleChainedAnds[i-70])-1])
								chained_list.append(MaskOnAnds[i-70][len(PossibleChainedAnds[i-70])-1+(chained_num-1)+1])
							else:
								start_j=j+1
								break
						if chained_num%2==1:
							tmpLogCor+=(chained_num+1)/2.0
							for index in range(int((chained_num+1)/2.0)):
								tmpSign_EachChained=0
								for index2 in range(index+1):
									tmpSign_EachChained^=chained_list[2*index2]
								tmpSign_EachChained*=chained_list[2*index+1]
								tmpSign^=tmpSign_EachChained
						if chained_num>0 and chained_num%2==0:
							if cond%2==1:
								zeroCor=True
								break
							else:
								tmpLogCor+=chained_num/2.0
								for index in range(int(chained_num/2.0)):
									tmpSign_EachChained=0
									for index2 in range(index+1):
										tmpSign_EachChained^=chained_list[2*index2]
									tmpSign_EachChained*=chained_list[2*index+1]
									tmpSign^=tmpSign_EachChained
					if zeroCor:
						break
				tmpKeyMask=[0 for i in range(nbrounds)] # Store masks on round key bits.
				if not zeroCor:# Record this trail if cor!=0
					for r in range(0,len(states)-1):
						tmpSign^=variables_block_to_int(states[r+1][127:128])
						tmpKeyMask[r]=variables_block_to_int(states[r+1][127:128])
					tmpTrail=[[0 for col in range(128)] for row in range(len(states))]
					for row in range(len(states)):
						for col in range(128):
							tmpTrail[row][col]=variables_block_to_int(states[row][col:col+1])
					trail.append(tmpTrail)
					log_cor.append([tmpSign,tmpLogCor])
					KeyMask.append(tmpKeyMask)
					
					print("Trail_"+str(sol_counter)+": ",end='')
					print(tmpSign,tmpLogCor,tmpKeyMask)
					
				sol_counter+=1
			
			goon=True
			# Add constraints to remove former solutions in the model
			constr=0
			for s in range(15):
				t_s=list_ts[s]
				for j in range(t_s+1):
					if variables_block_to_int(states[s+15*j][0:1])==0:
						constr+=states[s+15*j][0]
					else:
						constr+=(1-states[s+15*j][0])
				if int(extra_varaible[s][0].xn)==0:
					constr+=extra_varaible[s][0]
				else:
					constr+=(1-extra_varaible[s][0])
				if int(extra_varaible[s][t_s+t_s].xn)==0:
					constr+=extra_varaible[s][t_s+t_s]
				else:
					constr+=(1-extra_varaible[s][t_s+t_s])
				for j in range(1,t_s+1):
					if int(extra_varaible[s][j-1+2*t_s].xn)==0:
						constr+=extra_varaible[s][j-1+2*t_s]
					else:
						constr+=(1-extra_varaible[s][j-1+2*t_s])
		else:
			goon=False
	
	# remove trails with same input/output masks on each round
	final_trail=[]
	final_logCor=[]
	final_KeyMask=[]
	final_trail.append(trail[0])
	final_logCor.append(log_cor[0])
	final_KeyMask.append(KeyMask[0])
	exist=False
	for i in range(len(trail)):
		exist=False
		for j in range(len(final_trail)):
			if cmp(final_trail[j],trail[i])==0:
				exist=True
				break
		if not exist:
			final_trail.append(trail[i])
			final_logCor.append(log_cor[i])
			final_KeyMask.append(KeyMask[i])

	print("NumOfTrails=",len(final_trail))
	# Sort these trails according to |Correlation|
	sorted=[[[],[[[]]],[]] for i in range(len(final_trail))]
	for i in range(len(final_trail)):
		sorted[i][0]=final_logCor[i]
		sorted[i][1]=final_trail[i]
		sorted[i][2]=final_KeyMask[i]
	sorted.sort(key=takeFirst)
	for i in range(len(final_trail)):
		final_logCor[i]=sorted[i][0]
		final_trail[i]=sorted[i][1]
		final_KeyMask[i]=sorted[i][2]
	for i in range(len(final_trail)):
		print(final_logCor,final_KeyMask)
	
	# Output information of trails
	file_name=open("LinearTrails_RepeatedSolutionsRemoved.txt", 'w+')
	k=[0 for i in range(klen)]
	for i in range(len(final_trail)):
		if final_logCor[i][0]==1:
			print("sol_index="+str(i)+", cor=-2**(-"+str(final_logCor[i][1])+") involved bits=",end='',file=file_name)
		else:
			print("sol_index="+str(i)+", cor=+2**(-"+str(final_logCor[i][1])+") involved bits=",end='',file=file_name)
		k=[0 for j in range(klen)]
		for j in range(nbrounds):
			if final_KeyMask[i][j]==1:
				k[j%klen]^=1
		for k_i in range(klen):
			if k[k_i]==1:
				print("k_"+str(k_i)+",",end='',file=file_name)
		print("\n",file=file_name)
	file_name.close()
	
	# Output the array for next step
	file_name = open("ForArrayIn_Step2.txt", 'w+')
	for i in range(len(final_trail)):
		print("[",end='',file=file_name)
		if final_logCor[i][0]==1:
			print("\"-\"",end='',file=file_name)
		else:
			print("\"+\"",end='',file=file_name)
		k=[0 for j in range(klen)]
		for j in range(nbrounds):
			if final_KeyMask[i][j]==1:
				k[j%klen]^=1
		for k_i in range(klen):
			print(","+str(k[k_i]),end='',file=file_name)
		if i==len(final_trail)-1:
			print("]",file=file_name)
		else:
			print("],",file=file_name)
	file_name.close()
	
print("Done.")
				
	
	
