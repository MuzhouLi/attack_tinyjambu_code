'''
This code is used to drive key classes and list corresponding keys in the key class.

Usage: python3 -u Step2_drive_key_classes.py 

Notice that list "involved_KeyBits" and "TrailNum_Parts" should be modified according to "ForArrayIn_Step2.txt" and "LinearTrails_RepeatedSolutionsRemoved.txt", respectively.

A file named "ForArrayIn_Step3.txt" is created, which will be used in Step 3.
'''

from numpy import *

# length of key bits
klen=128
# Values in this array can be obtained from file "ForArrayIn_Step2.txt".
involved_KeyBits=[
["+",0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0],
["-",0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0],
["-",0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0],
["+",0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0]
]

matrix=array(zeros([len(involved_KeyBits),len(involved_KeyBits[0])-1]))

for i in range(len(involved_KeyBits)):
	for j in range(len(involved_KeyBits[0])-1):
		matrix[i][j]=involved_KeyBits[i][j+1]

var=["" for i in range(matrix.shape[1])]
for i in range(matrix.shape[1]):
	var[i]="k_"+str(i)

tmpKEY=[0 for i in range(matrix.shape[1])]
for j in range(matrix.shape[1]):
	for i in range(matrix.shape[0]):
		if matrix[i][j]==1:
			tmpKEY[j]=1
			break
BITS=0
for i in range(len(tmpKEY)):
	BITS+=tmpKEY[i]
print("Number of all involved master key bits is",BITS)

# Do primary transformation on matrix, and obtain FinalMatrix, in order to get rank of matrix.
highestPos=[[] for i in range(matrix.shape[0]+1)]
boolV=True
for i in range(matrix.shape[0]+1):
	for pos in range(matrix.shape[1]):
		boolV=True
		for j in range(i):
			if matrix[j][pos]==1:
				boolV=False
				break
		if i==matrix.shape[0]:
			if boolV:
				highestPos[i].append(pos)
		else:
			if boolV and matrix[i][pos]==1:
				highestPos[i].append(pos)
FinalMatrix=array(zeros([matrix.shape[0],matrix.shape[1]]))
for i in range(matrix.shape[0]):
	for j in range(matrix.shape[1]):
		FinalMatrix[i][j]=matrix[i][j]
goon=False
for i in range(matrix.shape[0]):
	if len(highestPos[i])>1:
		goon=True
		break
while goon:
	for i in range(matrix.shape[0]):
		if len(highestPos[i])>1:
			for j in range(1,len(highestPos[i])):
				#matrix and var
				for row in range(matrix.shape[0]):
					FinalMatrix[row][highestPos[i][j]]=(FinalMatrix[row][highestPos[i][j]]+FinalMatrix[row][highestPos[i][0]])%2
				var[highestPos[i][0]]+=("^"+var[highestPos[i][j]])
	highestPos=[[] for i in range(matrix.shape[0]+1)]
	for i in range(FinalMatrix.shape[0]+1):
		for pos in range(FinalMatrix.shape[1]):
			boolV=True
			for j in range(i):
				if FinalMatrix[j][pos]==1:
					boolV=False
					break
			if i==FinalMatrix.shape[0]:
				if boolV:
					highestPos[i].append(pos)
			else:
				if boolV and FinalMatrix[i][pos]==1:
					highestPos[i].append(pos)
	goon=False
	for i in range(matrix.shape[0]):
		if len(highestPos[i])>1:
			goon=True
			break

# Rank of FinalMatrix is the number of involved equivalent key bits.
rank=linalg.matrix_rank(FinalMatrix)
print("number of involved equivalent key bits is",rank,end=' : ')
for pos in range(FinalMatrix.shape[1]):
	boolV=True
	for i in range(FinalMatrix.shape[0]):
		if FinalMatrix[i][pos]==1:
			boolV=False
			break
	if not boolV:
		print("ek_"+str(pos),end=',')
print()

print("relation between equivalent key bits and original key bits:")
for i in range(klen):
	print("ek_"+str(i)+"="+var[i])

# Remove rows containing all zeros, since they are useless.
FinalMatrix_withoutzeros=array(zeros([FinalMatrix.shape[0],rank]))
FinalVar=["" for i in range(rank)]
counter=0
for pos in range(FinalMatrix.shape[1]):
	boolV=True
	for i in range(FinalMatrix.shape[0]):
		if FinalMatrix[i][pos]==1:
			boolV=False
			break
	if not boolV:
		for i in range(FinalMatrix.shape[0]):
			FinalMatrix_withoutzeros[i][counter]=FinalMatrix[i][pos]
		FinalVar[counter]=var[pos]
		counter=counter+1

TotalTrail=FinalMatrix_withoutzeros.shape[0]

# Values in this array can be obtained from file "LinearTrails_RepeatedSolutionsRemoved.txt"
# This list records number of trails with different correlations, starting from max ones.
TrailNum_Parts=[0,1,TotalTrail]

CorEachTrail=[0 for i in range(len(involved_KeyBits))]
for part in range(len(TrailNum_Parts)-1):
	for trail_num in range(TrailNum_Parts[part],TrailNum_Parts[part+1]):
		if involved_KeyBits[trail_num][0]=="+":
			CorEachTrail[trail_num]=2*part
		else:
			CorEachTrail[trail_num]=2*part+1

#begin driving key classes
finalCorKind=[]# recording correlations of key classes
finalCorNum=[]# recording number of keys involved in this key class
finalKeyInKeyClass=[]# recording keys belonging to this key class
FinalCor=[[0.0 for j in range(2*(len(TrailNum_Parts)-1)+1)] for i in range(2**rank)]
eq=0
find=False
for key in range(2**rank):
	for j in range(2*(len(TrailNum_Parts)-1)+1):
		FinalCor[key][j]=0.0
	for part in range(len(TrailNum_Parts)-1):
		for trail_num in range(TrailNum_Parts[part],TrailNum_Parts[part+1]):
			eq=0
			for pos in range(rank):
				eq=eq+(FinalMatrix_withoutzeros[trail_num][pos]*((key>>(rank-1-pos))&0x1))
			if (eq%2)==0:
				FinalCor[key][CorEachTrail[trail_num]]+=1
			else:
				FinalCor[key][(CorEachTrail[trail_num]-2*part+1)%2+2*part]+=1
	FinalCor[key][2*(len(TrailNum_Parts)-1)]=0.0
	for pos in range(len(TrailNum_Parts)-1):
		FinalCor[key][2*(len(TrailNum_Parts)-1)]+=(FinalCor[key][2*pos]-FinalCor[key][2*pos+1])*(2**(-pos))
	find=False
	for kind in range(len(finalCorKind)):
		if FinalCor[key][2*(len(TrailNum_Parts)-1)]==finalCorKind[kind]:
			find=True
			finalCorNum[kind]+=1	
			finalKeyInKeyClass[kind].append(key)
		if find:
			break
	if not find:
		finalCorKind.append(FinalCor[key][2*(len(TrailNum_Parts)-1)])
		finalCorNum.append(1)
		finalKeyInKeyClass.append([key])

# output correlation and involved keys of each key class in "ForArrayIn_Step3.txt" which can be used in Step 3 to mount key recovery attacks.
print("Outputting correlation and involved keys of each key class in \"ForArrayIn_Step3.txt\".")
file_name = open("ForArrayIn_Step3.txt", 'w+')

def takeFirst(elem):
	return elem[0]

sorted=[[0,[]] for i in range(len(finalCorKind))]
for i in range(len(finalCorKind)):
	sorted[i][0]=finalCorKind[i]
	sorted[i][1]=finalKeyInKeyClass[i]
sorted.sort(key=takeFirst)
for i in range(len(finalCorKind)):
	finalCorKind[i]=sorted[i][0]
	finalKeyInKeyClass[i]=sorted[i][1]

print("[",end='',file=file_name)
for i in range(len(finalCorKind)):
	print(finalCorKind[i],end='',file=file_name)
	if i<len(finalCorKind)-1:
		print(",",end='',file=file_name)
print("],",end='',file=file_name)
print("[",end='',file=file_name)
for i in range(len(finalCorKind)):
	print(finalKeyInKeyClass[i],end='',file=file_name)
	if i<len(finalCorKind)-1:
		print(",",end='',file=file_name)
print("]",file=file_name)
print("Done.")