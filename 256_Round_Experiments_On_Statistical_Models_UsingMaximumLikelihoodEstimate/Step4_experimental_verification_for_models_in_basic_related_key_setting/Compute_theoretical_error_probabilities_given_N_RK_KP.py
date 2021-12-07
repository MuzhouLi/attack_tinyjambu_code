import sys
import numpy as np
import math
import scipy.stats as stats
from scipy.special import comb
import itertools

ave_list=[0 for i in range(200,250,5)]

for N_index in range(200,250,5):
	N=2**(N_index*0.1)

	B=1 #for KP sampling
	
	'''
	n=128
	B=(2**n-N)/(1.0*(2**n-1))#for DKP sampling
	#'''
	
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

	tmpAT=[[0,0.0] for k in range(len(CorKind))]
	tmp=0.0
	tmpV=0.0
	tmpCoe=0.0
	
	max=N*1.0
	min=-N*1.0
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
						tmpCoe=CorKind[i]-CorKind[t]
						tmpV=B*(math.log(NumInEachKind[t]/(1.0*NumInEachKind[i]),math.e))-N/4.0*((CorKind[t])**2-(CorKind[i])**2)
						tmp=tmpV/tmpCoe
						
						if tmpCoe<0:
							tmpAT[t][0]=-1
						else:
							tmpAT[t][0]=1
						tmpAT[t][1]=tmp
						
				max=N*1.0
				min=-N*1.0
				for t in range(len(CorKind)):
					if t!=i:
						if tmpAT[t][0]!=0:
							if tmpAT[t][1]>max or tmpAT[t][1]<min:
								tmpAT[t][0]=0
								tmpAT[t][1]=0
				
				
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
				
				# use cdf of norm
				if maxInt==N:
					alpha[k]+=(1-stats.norm.cdf(minInt,loc=N/2.0*CorKind[k],scale=((N/2.0)*B)**0.5))
				elif minInt==-N:
					alpha[k]+=stats.norm.cdf(maxInt,loc=N/2.0*CorKind[k],scale=((N/2.0)*B)**0.5)
				else:
					alpha[k]+=(stats.norm.cdf(maxInt,loc=N/2.0*CorKind[k],scale=((N/2.0)*B)**0.5)-stats.norm.cdf(minInt,loc=N/2.0*CorKind[k],scale=((N/2.0)*B)**0.5))
				
		print(CorKind[k]/baseCor,1-alpha[k])

	ave=0.0
	for k in range(len(CorKind)):
		ave+=(alpha[k])*(NumInEachKind[k]/total)
	ave=1-ave
	print("N_index=",N_index,", diff_alpha=",diff_alpha,", ave_Pr=",ave)
	ave_list[int((N_index-200)/5)]=ave

if B==1:
	print("Under KP, diff_alpha=",diff_alpha,", ave",ave_list)
else:
	print("Under DKP, diff_alpha=",diff_alpha,", ave",ave_list)
