import sys
import numpy as np
import math
import scipy.stats as stats
from scipy.special import comb
import itertools

ave_list=[0 for i in range(200,250,5)]

for N_index in range(200,250,5):
	N=2**(N_index*0.1)
		
	B=1#for KP sampling
	
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
	
	tmpAT=[[0,0.0] for k in range(len(CorKind))]
	tmp=0.0
	tmpV=0.0
	tmpCoe=0.0
	max=N
	min=0
	maxInt=0
	minInt=0
	for k in range(len(CorKind)):
		alpha[k]=0
		for i in range(len(CorKind)):
			if i!=k:
				for t in range(len(CorKind)):
					tmpAT[t][0]=0
					tmpAT[t][1]=0.0
				for t in range(len(CorKind)):
					if t!=i:
						tmpCoe=math.log((Pr[i]/(1.0*(1-Pr[i])))/(1.0*(Pr[t]/(1.0*(1-Pr[t])))),2)
						tmpV=math.log(NumInEachKind[t]/(NumInEachKind[i]*1.0),2)+N*math.log((1-Pr[t])/(1.0*(1-Pr[i])),2)
						tmp=tmpV/tmpCoe
						
						if tmpCoe<0:
							tmpAT[t][0]=-1
						else:
							tmpAT[t][0]=1
						tmpAT[t][1]=tmp
						
				max=N
				min=0
				for t in range(len(CorKind)):
					if t!=i:
						if tmpAT[t][0]==-1:
							if tmpAT[t][1]<max:
								max=tmpAT[t][1]
						else:
							if tmpAT[t][0]==1:
								if tmpAT[t][1]>min:
									min=tmpAT[t][1]
				print("tmpAT : ",tmpAT)
				print("max= ",max," min= ",min)
				maxInt=math.floor(max)
				minInt=math.ceil(min)
				print("maxInt= ",maxInt," minInt= ",minInt)
				
				alpha[k]+=((stats.binom.cdf(maxInt,N,Pr[k])-stats.binom.cdf(minInt,N,Pr[k])))
				
				print("alpha[j] for now is ",alpha[k])
				print()
					
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
print("ave",ave_list)
