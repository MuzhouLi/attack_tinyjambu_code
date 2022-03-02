Experimental verification on the MLE-based model:

Take the data complexity $N=2^{20}$ as an example.

1.  Non-related-key setting under KP sampling
(1) compute theoretical error probabilities:
	java -cp .:commons-math3-3.6.1.jar ComputeCDFValues_NRK_KP_Test 20.0
(2) obtain experimental error probabilities:
	a) g++ Get_Empirical_NRK_KP.cpp -o NRK_KP -std=c++11
	b) bash run_NRK_KP.sh
	c) cat result_NRK_KP_Data_200_* | grep "suc"

2. Non-related-key setting under DKP sampling
(1) compute theoretical error probabilities: 
	java -cp .:commons-math3-3.6.1.jar ComputeCDFValues_NRK_NKP_Test 20.0
(2) obtain experimental error probabilities: 
	a) g++ Get_Empirical_NRK_DKP.cpp -o NRK_DKP -std=c++11
	b) bash run_NRK_DKP.sh
	c) cat result_NRK_DKP_Data_200_* | grep "suc"

3. Basic related-key setting under KP sampling (As we explained in the paper, experimental verification under DKP sampling is omitted.)
(1) compute theoretical error probabilities: 
	java -cp .:commons-math3-3.6.1.jar ComputeCDFValues_RK_Test 20.0 b (b=0 for KP, b=1 for DKP)
(2) obtain experimental error probabilities: 
	a) g++ Get_Empirical_RK_KP.cpp -o RK_KP -std=c++11
	b) bash run_RK_KP.sh
	c) cat result_RK_KP_Data_200_* | grep "suc"

4. Multiple related-key setting under KP sampling (As we explained in the paper, experimental verification under DKP sampling is omitted.)
(1) compute theoretical error probabilities: 
	java -cp .:commons-math3-3.6.1.jar ComputeCDFValues_MRK_Test 20.0 b (b=0 for KP, b=1 for DKP)
(2) obtain experimental error probabilities: 
	a) g++ Get_Empirical_MRK_KP.cpp -o MRK_KP -std=c++11
	b) bash run_MRK_KP.sh
	c) cat result_MRK_KP_Data_200_* | grep "suc"