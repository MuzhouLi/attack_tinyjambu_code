Experimental verification on the Rock-Nyberg's model under KP sampling:

Take the data complexity $N=2^{20}$ as an example.

1.  Non-related-key setting
(1) compute theoretical error probabilities:
	python3 -u Compute_Theoretical_NRK_KP.py | grep "ave"
(2) obtain experimental error probabilities:
	a) g++ Get_Empirical_NRK_KP.cpp -o NRK_KP -std=c++11
	b) bash run_NRK_KP.sh
	c) cat result_NRK_KP_Data_200_* | grep "suc"

2. Basic related-key setting
(1) compute theoretical error probabilities: 
	python3 -u Compute_Theoretical_RK_KP.py | grep "ave"
(2) obtain experimental error probabilities: 
	a) g++ Get_Empirical_RK_KP.cpp -o RK_KP -std=c++11
	b) bash run_RK_KP.sh
	c) cat result_RK_KP_Data_200_* | grep "suc"

3. Multiple related-key setting
(1) compute theoretical error probabilities: 
	python3 -u Compute_Theoretical_MRK_KP.py | grep "ave"
(2) obtain experimental error probabilities: 
	a) g++ Get_Empirical_MRK_KP.cpp -o MRK_KP -std=c++11
	b) bash run_MRK_KP.sh
	c) cat result_MRK_KP_Data_200_* | grep "suc"