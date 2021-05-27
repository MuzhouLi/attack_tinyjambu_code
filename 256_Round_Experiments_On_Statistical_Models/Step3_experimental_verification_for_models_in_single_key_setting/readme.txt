1. Get empirical values in the single-key setting under KP sampling
(1) Run code "Get_empirical_error_probabilities_given_N_SingleKey_KP.cpp"
Usage:
g++ Get_empirical_error_probabilities_given_N_SingleKey_KP.cpp -o SK_KP -std=c++11
(2) Proceed run.sh to run SK_KP under different chioces of N
Usage:
bash run.sh 
(3) Get empirical values from all results files
For example, if N=2**20, you can use 'cat result_Data_200* | grep "suc"' to get the empirical value under 2**20 data.

2. Get theoretival values by running these two python files
Usage:
python3 -u Compute_theoretical_error_probabilities_given_N_SingleKey_KP.py | grep "ave"
python3 -u Compute_theoretical_error_probabilities_given_N_SingleKey_DKP.py | grep "ave"