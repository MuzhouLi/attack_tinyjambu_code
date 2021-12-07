import sys
import numpy as np
import math
import scipy.stats as stats
from scipy.special import comb
import itertools

ave_list=[0 for i in range(200,250,5)]

for N_index in range(200,250,5):
	N=2**(N_index*0.1)
	
	'''
	B=1#for KP sampling
	#'''
	
	#'''
	n=128
	B=(2**n-N)/(1.0*(2**n-1))#for DKP sampling
	#'''
	
	total=0

	baseCor=2**(-10)
	CorKind=[-2.5,-0.5,0.5,2.5] # can be obtained from file "ForArrayIn_Step3.txt"
	NumInEachKind=[1,3,3,1] # can be obtained from file "ForArrayIn_Step3.txt"

	for k in range(len(CorKind)):
		CorKind[k]=CorKind[k]*baseCor
	
	Pr=[0.0 for i in range(len(CorKind))]
	for i in range(len(CorKind)):
		Pr[i]=(CorKind[i]+1)/2.0
	
	total=0.0
	for i in range(len(CorKind)):
		total+=(NumInEachKind[i]*1.0)
	print("TOTAL=",total)

	alpha=[0 for k in range(len(CorKind))]
	KeyInfo=[0 for k in range(len(CorKind))]
	
	tmpRange=[[[0.0,N*1.0]] for ClassNum in range(len(CorKind))]
	RangeFinal=[]
	coe_A=0.0
	coe_B=0.0
	coe_C=0.0
	Delta=0.0
	maxRange=N**1.0
	minRange=0.0
	maxRangeInt=0
	minRangeInt=0
	solution_1=0.0
	solution_2=0.0
	locV=0.0
	scaleV=0.0
	no_solution=False
	
	for k in range(len(CorKind)):
		alpha[k]=0
		for i in range(len(CorKind)):
			if i!=k:
				for t in range(len(CorKind)):
					tmpRange[t]=[[0.0,N*1.0]]	
				no_solution=False
				RangeFinal.clear()
				
				for t in range(len(CorKind)):
					if t!=i:
						coe_A=Pr[i]*(1-Pr[i])-Pr[t]*(1-Pr[t])
						coe_B=-2*N*(Pr[i]*(1-Pr[i])*Pr[t]-Pr[t]*(1-Pr[t])*Pr[i])
						coe_C=(N**2)*(Pr[i]*(1-Pr[i])*Pr[t]*Pr[t]-Pr[t]*(1-Pr[t])*Pr[i]*Pr[i])-(math.log(NumInEachKind[t]/(1.0*NumInEachKind[i]),math.e)-0.5*math.log(Pr[t]/(1.0*Pr[i]),math.e)-0.5*math.log((1-Pr[t])/(1.0*(1-Pr[i])),math.e))*2*N*Pr[i]*(1-Pr[i])*Pr[t]*(1-Pr[t])*B
						
						if coe_A==0:
							if coe_B>0:
								if -coe_C/coe_B>N:
									no_solution=True
								else:
									if -coe_C/coe_B>=0:
										tmpRange[t][0][0]=-coe_C/coe_B
							else:
								if -coe_C/coe_B<0:
									no_solution=True
								else:
									if -coe_C/coe_B<=N:
										tmpRange[t][0][1]=-coe_C/coe_B
						else:
							Delta=coe_B**2-4*coe_A*coe_C
							if Delta>0:
								solution_1=(-coe_B+(Delta)**0.5)/(2.0*coe_A)
								solution_2=(-coe_B-(Delta)**0.5)/(2.0*coe_A)
								if coe_A>0:
									if solution_1>N and solution_2<0:
										no_solution=True
									if solution_2<0 and solution_1>=0 and solution_1<N:
										tmpRange[t][0][0]=solution_1
									if solution_2>0 and solution_2<=N and solution_1>N:
										tmpRange[t][0][1]=solution_2
									if solution_2>=0 and solution_2<N and solution_1>0 and solution_1<=N:
										tmpRange[t][0][1]=solution_2
										tmpRange[t]+=[[solution_1,N*1.0]]
								else:
									if solution_2<0 or solution_1>N:
										no_solution=True
									if solution_1>=0 and solution_1<=N and solution_2>=0 and solution_2<=N:
										tmpRange[t][0][0]=solution_1
										tmpRange[t][0][1]=solution_2
									if solution_1<0 and solution_2>=0 and solution_2<=N:
										tmpRange[t][0][0]=0
										tmpRange[t][0][1]=solution_2
									if solution_1>=0 and solution_1<=N and solution_2>N:
										tmpRange[t][0][0]=solution_1
										tmpRange[t][0][1]=N
							else:
								no_solution=True
						if no_solution:
							break
				
				for t in range(len(tmpRange)):
					if len(tmpRange[t])>1:
						print(tmpRange[t])
				if no_solution:
					alpha[k]+=0.0
				else:
					maxRange=N*1.0
					minRange=0.0
					for comb in itertools.product(tmpRange[0],tmpRange[1],tmpRange[2],tmpRange[3]):
						for t in range(4):
							if comb[t][0]>minRange:
								minRange=comb[t][0]
							if comb[t][1]<maxRange:
								maxRange=comb[t][1]
						maxRangeInt=math.floor(maxRange)
						minRangeInt=math.ceil(minRange)
						if maxRangeInt>minRangeInt:
							RangeFinal+=[[minRangeInt,maxRangeInt]]
					if len(RangeFinal)>0:
						print(RangeFinal)
						locV=1.0*N*Pr[k]
						scaleV=(1.0*N*Pr[k]*(1-Pr[k])*B)**0.5
						for elem in range(len(RangeFinal)):
							alpha[k]+=(stats.norm.cdf((RangeFinal[elem][1]-locV)/scaleV)-stats.norm.cdf((RangeFinal[elem][0]-locV)/scaleV))
						print(alpha[k])
					
		KeyInfo[k]=total-NumInEachKind[k]
		if NumInEachKind[k]==1:
			KeyInfo[k]+=1
		print("k=",k," i=", i," : final alpha[k] is ",alpha[k]," ; Other Info : ",KeyInfo[k],math.log(KeyInfo[k])/math.log(2))
		print()
		print()

	print("N=2^",math.log(N)/math.log(2))
		
	ave=0.0
	for k in range(len(CorKind)):
		ave+=(alpha[k])*(NumInEachKind[k]/(total*1.0))
	ave=1-ave
	print("N_index=",N_index,", ave_Pr=",ave)
	ave_list[int((N_index-200)/5)]=ave

print("Under DKP: ave is ",ave_list)
