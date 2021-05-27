import sys
import numpy as np
import math
import scipy.stats as stats
from scipy.special import comb
import itertools

ave_list=[0 for i in range(200,250,5)]

for N_index in range(200,250,5):
	N=2**(N_index*0.1)
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

	listDiff=[1,2,4]

	TotalPr=1.0

	for diff_alpha in listDiff:
		print(diff_alpha)
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

		print(CorKind)
		print(NumInEachKind)
		print("KeyInKeyClass=",KeyInKeyClass)
		
		d_alpha=1
		for i in range(len(CorKind)-1):
			if abs(CorKind[i+1]*baseCor-CorKind[i]*baseCor)<abs(d_alpha):
				d_alpha=CorKind[i+1]*baseCor-CorKind[i]*baseCor
		print(d_alpha)	
	
		ave=1-2**(math.log(len(CorKind)-1)/math.log(2)-N*(d_alpha**2)/(16*math.log(2)/math.log(math.e)*1.0))
		
		print("diff_alpha=",diff_alpha,", ave_Pr=",ave)
		TotalPr*=ave
	print("N_index=",N_index*0.1,", Total_ave_Pr=",TotalPr)
	ave_list[int((N_index-200)/5)]=TotalPr
print("TotalPr",ave_list)
