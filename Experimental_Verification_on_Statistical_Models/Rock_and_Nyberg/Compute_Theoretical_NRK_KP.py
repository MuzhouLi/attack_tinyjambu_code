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
	CorKind=[-2.5,-0.5,0.5,2.5] # can be obtained from file "ForArrayIn_Step3.txt"
	NumInEachKind=[1,3,3,1] # can be obtained from file "ForArrayIn_Step3.txt"
	d=1*baseCor

	ave=1-2**(math.log(len(CorKind)-1)/math.log(2)-N*(d**2)/(8*math.log(2)/math.log(math.e)*1.0))
	print("N_index=",N_index,", ave_Pr=",ave)
	ave_list[int((N_index-200)/5)]=ave
print("ave",ave_list)