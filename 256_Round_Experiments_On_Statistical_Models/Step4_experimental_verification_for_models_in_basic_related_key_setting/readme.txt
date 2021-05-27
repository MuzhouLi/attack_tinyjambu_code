1. Get empirical values in the basic related-key setting under KP sampling
(1) Run code "Get_empirical_error_probabilities_given_N_alpha_5_RK_KP.cpp"
Usage:
g++ Get_empirical_error_probabilities_given_N_alpha_5_RK_KP.cpp -o RK_KP -std=c++11
(2) Proceed run.sh to run RK_KP under different chioces of N
Usage:
bash run.sh 
(3) Get empirical values from all results files
For example, if N=2**20, you can use 'cat result_Data_200* | grep "suc"' to get the empirical value under 2**20 data.

2. Get theoretival values by running these two python files
Usage:
python3 -u Compute_theoretical_error_probabilities_given_N_RK_KP.py | grep "ave"
python3 -u Compute_theoretical_error_probabilities_given_N_RK_DKP.py | grep "ave"