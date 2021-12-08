import sys
import numpy as np
import math
import scipy.stats as stats
from scipy.special import comb
import itertools

ave_list=[0 for i in range(200,250,5)]

for N_index in range(200,250,5):
	N=2**(N_index*0.1)

	B=(2**128-N)/(1.0*(2**128-1))#for DKP sampling	
	
	total=0

	baseCor=2**(-10)
	CorKind=[-2.5,-0.5,0.5,2.5] # can be obtained from file "ForArrayIn_Step3.txt"
	NumInEachKind=[1,3,3,1] # can be obtained from file "ForArrayIn_Step3.txt"

	for k in range(len(CorKind)):
		CorKind[k]=CorKind[k]*baseCor
		
	total=0.0
	for i in range(len(CorKind)):
		total+=(NumInEachKind[i]*1.0)
	print("TOTAL=",total)

	alpha=[0 for k in range(len(CorKind))]
	KeyInfo=[0 for k in range(len(CorKind))]

	for j in range(len(CorKind)):
		alpha[j]=0
		for i in range(len(CorKind)):
			if i!=j:
				if i==0:
					alpha[j]+=stats.norm.cdf((N**0.5)*((CorKind[i]+CorKind[i+1])/2.0-CorKind[j])/(((B*(1-(CorKind[j])**2))**0.5)*1.0))
				elif i==len(CorKind)-1:
					alpha[j]+=(1-stats.norm.cdf((N**0.5)*((CorKind[i]+CorKind[i-1])/2.0-CorKind[j])/(((B*(1-(CorKind[j])**2))**0.5)*1.0)))
				else:
					alpha[j]+=(stats.norm.cdf((N**0.5)*((CorKind[i]+CorKind[i+1])/2.0-CorKind[j])/(((B*(1-(CorKind[j])**2))**0.5)*1.0))-stats.norm.cdf((N**0.5)*((CorKind[i]+CorKind[i-1])/2.0-CorKind[j])/(((B*(1-(CorKind[j])**2))**0.5)*1.0)))
		KeyInfo[j]=total-NumInEachKind[j]
		if NumInEachKind[j]==1:
			KeyInfo[j]+=1
		print(CorKind[j]/baseCor,1-alpha[j],KeyInfo[j],math.log(KeyInfo[j])/math.log(2))

	print("N=2^",math.log(N)/math.log(2))

	ave=0.0
	for k in range(len(CorKind)):
		ave+=(alpha[k])*(NumInEachKind[k]/total)
	ave=1-ave
	print("N_index=",N_index,", ave_Pr=",ave)
	ave_list[int((N_index-200)/5)]=ave
print("ave",ave_list)
