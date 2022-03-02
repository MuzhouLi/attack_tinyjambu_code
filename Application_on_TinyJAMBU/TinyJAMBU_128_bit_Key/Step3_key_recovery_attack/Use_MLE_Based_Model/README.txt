Key recovery attacks on TinyJAMBU with the MLE-based model.

Before proceeding these codes, run "python3 -u genKeyInKeyClass_NRK.py > KeyInKeyClass_NRK.txt".

JAVA_VERSION >= 11

Evaluation time under a single N in each codes is described in Table 9 of our paper.

1. Non-related-key setting (KP sampling)
	nohup python3 proceed_NRK_KP_java.py &

2. Non-related-key setting (DKP sampling)
	nohup python3 proceed_NRK_DKP_java.py &

3. Basic related-key setting (KP sampling)
	nohup python3 proceed_RK_KP_java.py &

4. Basic related-key setting (DKP sampling)
	nohup python3 proceed_RK_DKP_java.py &

5. Multiple related-key setting (KP sampling)
	nohup python3 proceed_MRK_KP_java.py &

6. Multiple related-key setting (DKP sampling)
	nohup python3 proceed_MRK_DKP_java.py &