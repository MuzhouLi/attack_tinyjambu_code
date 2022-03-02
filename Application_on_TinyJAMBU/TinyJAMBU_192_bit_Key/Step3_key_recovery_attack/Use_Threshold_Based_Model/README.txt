Key recovery attacks on TinyJAMBU with the threshold-based model.

Before proceeding these codes, run "python3 -u genKeyInKeyClass_NRK.py > KeyInKeyClass_NRK.txt".

JAVA_VERSION >= 11

Denote the data complexity used as 2^{logN}, and b represents whether we are under KP sampling (b=0) or DKP sampling (b=1).

Running time of each code is described in Table 9 of our paper.

1. Non-related-key setting
	java -cp .:commons-math3-3.6.1.jar ComputeCDFValues_NRK logN b

2. Basic related-key setting
(1) KP:  nohup python3 proceed_RK_KP_java.py &
(2) DKP: nohup python3 proceed_RK_DKP_java.py &

3. Multiple related-key setting
	java -cp .:commons-math3-3.6.1.jar ComputeCDFValues_MRK logN b