import sys, os, getopt, concurrent.futures

os.environ['OPENBLAS_NUM_THREADS'] = '1' 
os.environ['OMP_NUM_THREADS'] = '1'

def proceed(cmd):
	result=os.popen(cmd).read()
	print(result)
	
if __name__ == "__main__":
	
	with concurrent.futures.ProcessPoolExecutor() as executor:
		Chosen_N=[97.2,97.3,97,4,97.5,97.6,97.7,97.8,97.9,98.0,98.1,98.2,98.3,98.4,98.5,98.6,98.7,98.8,98.9]
		cmd=[]
		for N in range(len(Chosen_N)):
			cmd.append("java -cp .:commons-math3-3.6.1.jar MultiThreads_ComputeCDFValues_MRK "+str(Chosen_N[N])+" 1 > R_MRK_DKP_N"+str(Chosen_N[N])+".txt")
		executor.map(proceed,cmd)