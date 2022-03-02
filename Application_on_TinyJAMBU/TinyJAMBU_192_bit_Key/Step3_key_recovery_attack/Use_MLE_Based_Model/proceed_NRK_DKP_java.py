import sys, os, getopt, concurrent.futures

os.environ['OPENBLAS_NUM_THREADS'] = '1' 
os.environ['OMP_NUM_THREADS'] = '1'

def proceed(cmd):
	result=os.popen(cmd).read()
	print(result)
	
if __name__ == "__main__":
	
	with concurrent.futures.ProcessPoolExecutor() as executor:
		Chosen_N=[96.6,96.8,97.0,97.2,97.4,97.6,97.8,98.0,98.2,98.4,98.6,98.8,99.0,99.2,99.4]
		cmd=[]
		for N in range(len(Chosen_N)):
			for ClassNum in range(727):#727
				cmd.append("java -cp .:commons-math3-3.6.1.jar MultiThreads_ComputeCDFValues_NRK_DKP "+str(Chosen_N[N])+" "+str(ClassNum)+" > R_NRK_DKP_N"+str(Chosen_N[N])+"_"+str(ClassNum)+".txt")
		executor.map(proceed,cmd)