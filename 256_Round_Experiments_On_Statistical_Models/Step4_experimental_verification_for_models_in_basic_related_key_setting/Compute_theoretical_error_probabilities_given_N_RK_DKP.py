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
	Ori_CorKind=[-2.5,-0.5,0.5,2.5]
	Ori_NumInEachKind=[1,3,3,1]
	Ori_KeyClassValue=[[7],[2, 3, 6],[0, 1, 4],[5]]

	def getKeyClass(keyvalue):
		for i in range(len(Ori_KeyClassValue)):
			for j in range(len(Ori_KeyClassValue[i])):
				if Ori_KeyClassValue[i][j]==keyvalue:
					return i

	def takeFirst(elem):
		return elem[0]
	
	diff_alpha=5
	
	tmpCor=0
	exist=False
	CorKind=[]
	NumInEachKind=[]
	KeyInKeyClass=[]
	for i in range(len(Ori_KeyClassValue)):
		for j in range(len(Ori_KeyClassValue[i])):
			tmpCor=Ori_CorKind[i]-Ori_CorKind[getKeyClass(Ori_KeyClassValue[i][j]^diff_alpha)]
			exist=False
			for k in range(len(CorKind)):
				if tmpCor==CorKind[k]:
					NumInEachKind[k]+=1
					KeyInKeyClass[k].append(Ori_KeyClassValue[i][j])
					exist=True
					break
			if not exist:
				CorKind.append(tmpCor)
				NumInEachKind.append(1)
				KeyInKeyClass.append([Ori_KeyClassValue[i][j]])

	Sorted=[[0,0,[]] for i in range(len(CorKind))]
	for i in range(len(Sorted)):
		Sorted[i][0]=CorKind[i]
		Sorted[i][1]=NumInEachKind[i]
		Sorted[i][2]=KeyInKeyClass[i]
	Sorted.sort(key=takeFirst)

	for i in range(len(Sorted)):
		CorKind[i]=Sorted[i][0]
		NumInEachKind[i]=Sorted[i][1]
		KeyInKeyClass[i]=Sorted[i][2]

	print(NumInEachKind)
	print("CorKind=",CorKind)
	print("KeyInKeyClass=",KeyInKeyClass)
	
	
	for k in range(len(CorKind)):
		CorKind[k]=CorKind[k]*baseCor
	print(CorKind)
		
	total=0.0
	for i in range(len(CorKind)):
		total+=(NumInEachKind[i]*1.0)
	print("TOTAL=",total)

	alpha=[0 for k in range(len(CorKind))]
	KeyInfo=[0 for k in range(len(CorKind))]

	for k in range(len(CorKind)):
		alpha[k]=0
		if k==0 or k==len(CorKind)-1:
			if k==0:
				for i in range(len(CorKind)):
					if i!=k:
						alpha[k]+=stats.norm.cdf((N**0.5)*((CorKind[k]+CorKind[k+1])/2.0-CorKind[i])/((2*B)**0.5))
			else:
				for i in range(len(CorKind)):
					if i!=k:
						alpha[k]+=(1-stats.norm.cdf((N**0.5)*((CorKind[k]+CorKind[k-1])/2.0-CorKind[i])/((2*B)**0.5)))
		else:
			for i in range(len(CorKind)):
				if i!=k:
					alpha[k]+=(stats.norm.cdf((N**0.5)*((CorKind[k]+CorKind[k+1])/2.0-CorKind[i])/((2*B)**0.5))-stats.norm.cdf((N**0.5)*((CorKind[k]+CorKind[k-1])/2.0-CorKind[i])/((2*B)**0.5)))
		print(CorKind[k]/baseCor,1-alpha[k])

	print("N=2^",math.log(N)/math.log(2))

	ave=0.0
	for k in range(len(CorKind)):
		ave+=(alpha[k])*(NumInEachKind[k]/total)
	ave=1-ave
	print("N_index=",N_index,", diff_alpha=",diff_alpha,", ave_Pr=",ave)
	ave_list[int((N_index-200)/5)]=ave
print("diff_alpha=",diff_alpha,", ave",ave_list)