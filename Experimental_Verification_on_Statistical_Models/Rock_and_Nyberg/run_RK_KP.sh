for((data=200;data<=220;data=$data+5))
do
        for((j=0;j<4;j++))
        do
                nohup ./RK_KP $j $data 500 > result_RK_KP_Data_$data\_Part_$j.txt &
        done
done

for((data=225;data<=245;data=$data+5))
do
        for((j=0;j<8;j++))
        do
                nohup ./RK_KP $j $data 250 > result_RK_KP_Data_$data\_Part_$j.txt &
        done
done 