Run "Step1_search_all_trails_in_linear_hull.py" in a linux system to get all trails comprising the hull utilized in Section 3.3 as well as key bits involved in these trails. All these information are stored in the file named "LinearTrails_RepeatedSolutionsRemoved.txt". Besides, another file "ForArrayIn_Step2.txt" contains the array which will be used in Step 2.

Before proceeding this code, please install Gurobi (https://www.gurobi.com/) firstly.

Usage: 
python2 -u Step1_search_all_trails_in_linear_hull.py -r rounds -k 192 -i 32bit_inmask_in_hex -o 32bit_outmask_in_hex > LinearTrails.txt

In our paper:
rounds=384
32bit_inmask_in_hex=0x8024C000
32bit_outmask_in_hex=0x00220808