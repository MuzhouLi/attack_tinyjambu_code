import sys, os, getopt, concurrent.futures

os.environ['OPENBLAS_NUM_THREADS'] = '1' 
os.environ['OMP_NUM_THREADS'] = '1'

def proceed(cmd):
	result=os.popen(cmd).read()
	print(result)
	
if __name__ == "__main__":
	
	with concurrent.futures.ProcessPoolExecutor() as executor:
		Chosen_N=[93.6,94.1,94.6,95.1,95.6,96.1,96.6,97.1,97.6,98.1,98.6]
		Chosen_KeyDiff=[i for i in range(2**15)]
		cmd=[]
		for N in range(len(Chosen_N)):
			for key_index in range(len(Chosen_KeyDiff)):
				cmd.append("java -cp .:commons-math3-3.6.1.jar MultiThreads_ComputeCDFValues_RK "+str(Chosen_N[N])+" 0 "+str(Chosen_KeyDiff[key_index])+" > R_RK_KP_N"+str(Chosen_N[N])+"_"+str(Chosen_KeyDiff[key_index])+".txt")
		executor.map(proceed,cmd)