#include<iostream>
#include<cstring>
#include<cmath>
#include<random>
#include<time.h>

using namespace std;

#define FrameBitsIV  0x10  
#define FrameBitsAD  0x30  
#define FrameBitsPC  0x50  //Framebits for plaintext/ciphertext      
#define FrameBitsFinalization 0x70 

#define NROUND1 128*3 
#define NROUND2 128*8

int main(int argc, char * argv[])
{
	if(argc!=4)
	{
		cout<<"./a.out testPart IterativeNum TestNumEachTime"<<endl;
		return 0;
	}
	int testPart=atoi(argv[1]);
	double IterativeNum=atoi(argv[2])/10.0;
	int TotalTestNum=atoi(argv[3]);
	cout<<"testPart="<<testPart<<", IterativeNum="<<IterativeNum<<", TotalTestNum="<<TotalTestNum<<endl;
	
	//K_\alpha, here \alpha=5
	double KeyClass[3]={-2,0,2};
	int KeyClassNum[3]={2,4,2};
	int KeyInKeyClass[3][4]={ 
		{0,7,-1,-1},
		{1,3,4,6},
		{2,5,-1,-1}
	};//-1 represents non-value
	
	double Pi[4];
	for(int i=0;i<4;i++)
	{
		Pi[i]=KeyClassNum[i]/8.0;
	}
	
	unsigned int state[4];
	unsigned int key[4];
	unsigned int key2[4];
	unsigned int nonce[3];
	unsigned int AD[1];
	unsigned int M[2];
	
	default_random_engine random((int)(testPart*IterativeNum));
	uniform_int_distribution<int> dis(0,1);
	
	int successNum=0;
	
	double maxV=0.0;
	int determined_index=0;
	int theory_index=0;
	int maskbit=0;
	
	double tmpArr[3];
	double counter_K1=0.0;
	double counter_K2=0.0;
	double testCor=0.0;
	int invlovedKeyInfo[3];
	unsigned int t1, t2, t3, t4;
	for(int test_num=0;test_num<TotalTestNum;test_num++)
	{
		for(int pos=0;pos<4;pos++)
		{
			key[pos]=0;
			for(int bit=0;bit<32;bit++)
			{
				key[pos]^=(dis(random)<<bit);
			}
			key2[pos]=key[pos];
		}
		
		//add key difference \alpha=5
		key2[0]=key[0]^0x40^0x200000;//(key[0]>>6)&0x1, (key[0]>>21)&0x1
		
		invlovedKeyInfo[0]=((key[0]>>6)&0x1);
		invlovedKeyInfo[1]=((key[0]>>7)&0x1)^((key[0]>>27)&0x1)^((key[0]>>30)&0x1)^((key[1]>>5)&0x1)^((key[1]>>12)&0x1)^((key[2]>>17)&0x1)^((key[3]>>15)&0x1)^((key[3]>>22)&0x1);
		invlovedKeyInfo[2]=((key[0]>>21)&0x1);
		
		counter_K1=0;
		for(unsigned long long each_num=0;each_num<(unsigned long long)pow(2,IterativeNum);each_num++)
		{
			for(int pos=0;pos<4;pos++)
			{
				state[pos]=0;
				for(int bit=0;bit<32;bit++)
				{
					state[pos]^=(dis(random)<<bit);
				}
			}
			
			maskbit=((state[0]>>7)&0x1)^((state[0]>>30)&0x1)^((state[1]>>5)&0x1)^((state[1]>>12)&0x1)^((state[1]>>22)&0x1)^((state[2]>>0)&0x1)^((state[2]>>13)&0x1)^((state[2]>>17)&0x1)^((state[2]>>20)&0x1)^((state[2]>>27)&0x1)^((state[3]>>2)&0x1)^((state[3]>>22)&0x1)^((state[3]>>25)&0x1);
			
			for (unsigned int index_i = 0; index_i < (256 >> 5); index_i = index_i+4)
			{
				t1 = (state[1] >> 15) | (state[2] << 17);  // 47 = 1*32+15 
				t2 = (state[2] >> 6)  | (state[3] << 26);  // 47 + 23 = 70 = 2*32 + 6 
				t3 = (state[2] >> 21) | (state[3] << 11);  // 47 + 23 + 15 = 85 = 2*32 + 21      
				t4 = (state[2] >> 27) | (state[3] << 5);   // 47 + 23 + 15 + 6 = 91 = 2*32 + 27 
				state[0] ^= t1 ^ (~(t2 & t3)) ^ t4 ^ ((unsigned int*)key)[0]; 
				
				t1 = (state[2] >> 15) | (state[3] << 17);   
				t2 = (state[3] >> 6)  | (state[0] << 26);   
				t3 = (state[3] >> 21) | (state[0] << 11);        
				t4 = (state[3] >> 27) | (state[0] << 5);    
				state[1] ^= t1 ^ (~(t2 & t3)) ^ t4 ^ ((unsigned int*)key)[1];

				t1 = (state[3] >> 15) | (state[0] << 17);
				t2 = (state[0] >> 6)  | (state[1] << 26);
				t3 = (state[0] >> 21) | (state[1] << 11);
				t4 = (state[0] >> 27) | (state[1] << 5);
				state[2] ^= t1 ^ (~(t2 & t3)) ^ t4 ^ ((unsigned int*)key)[2];  

				t1 = (state[0] >> 15) | (state[1] << 17);
				t2 = (state[1] >> 6)  | (state[2] << 26);
				t3 = (state[1] >> 21) | (state[2] << 11);
				t4 = (state[1] >> 27) | (state[2] << 5);
				state[3] ^= t1 ^ (~(t2 & t3)) ^ t4 ^ ((unsigned int*)key)[3];
			}
			
			maskbit=maskbit^((state[2]>>0)&0x1);
			
			counter_K1+=((maskbit==0)?1:0);
		}
				
		counter_K2=0;
		for(unsigned long long each_num=0;each_num<(unsigned long long)pow(2,IterativeNum);each_num++)
		{
			for(int pos=0;pos<4;pos++)
			{
				state[pos]=0;
				for(int bit=0;bit<32;bit++)
				{
					state[pos]^=(dis(random)<<bit);
				}
			}
			
			maskbit=((state[0]>>7)&0x1)^((state[0]>>30)&0x1)^((state[1]>>5)&0x1)^((state[1]>>12)&0x1)^((state[1]>>22)&0x1)^((state[2]>>0)&0x1)^((state[2]>>13)&0x1)^((state[2]>>17)&0x1)^((state[2]>>20)&0x1)^((state[2]>>27)&0x1)^((state[3]>>2)&0x1)^((state[3]>>22)&0x1)^((state[3]>>25)&0x1);
			
			for (unsigned int index_i = 0; index_i < (256 >> 5); index_i = index_i+4)
			{
				t1 = (state[1] >> 15) | (state[2] << 17);  // 47 = 1*32+15 
				t2 = (state[2] >> 6)  | (state[3] << 26);  // 47 + 23 = 70 = 2*32 + 6 
				t3 = (state[2] >> 21) | (state[3] << 11);  // 47 + 23 + 15 = 85 = 2*32 + 21      
				t4 = (state[2] >> 27) | (state[3] << 5);   // 47 + 23 + 15 + 6 = 91 = 2*32 + 27 
				state[0] ^= t1 ^ (~(t2 & t3)) ^ t4 ^ ((unsigned int*)key2)[0]; 
				
				t1 = (state[2] >> 15) | (state[3] << 17);   
				t2 = (state[3] >> 6)  | (state[0] << 26);   
				t3 = (state[3] >> 21) | (state[0] << 11);        
				t4 = (state[3] >> 27) | (state[0] << 5);    
				state[1] ^= t1 ^ (~(t2 & t3)) ^ t4 ^ ((unsigned int*)key2)[1];

				t1 = (state[3] >> 15) | (state[0] << 17);
				t2 = (state[0] >> 6)  | (state[1] << 26);
				t3 = (state[0] >> 21) | (state[1] << 11);
				t4 = (state[0] >> 27) | (state[1] << 5);
				state[2] ^= t1 ^ (~(t2 & t3)) ^ t4 ^ ((unsigned int*)key2)[2];  

				t1 = (state[0] >> 15) | (state[1] << 17);
				t2 = (state[1] >> 6)  | (state[2] << 26);
				t3 = (state[1] >> 21) | (state[2] << 11);
				t4 = (state[1] >> 27) | (state[2] << 5);
				state[3] ^= t1 ^ (~(t2 & t3)) ^ t4 ^ ((unsigned int*)key2)[3];
			}
			
			maskbit=maskbit^((state[2]>>0)&0x1);
			
			counter_K2+=((maskbit==0)?1:0);
		}
		
		testCor=(counter_K1-counter_K2);
		
		//make desicion according to counter
		determined_index=0;
		for(int i=0;i<3;i++)
		{
			tmpArr[i]=log(Pi[i])-pow(testCor-pow(2,IterativeNum-1-10)*KeyClass[i],2)/(pow(2,IterativeNum));
		}
		maxV=tmpArr[0];
		determined_index=0;
		for(int i=1;i<3;i++)
		{
			if(tmpArr[i]>maxV)
			{
				maxV=tmpArr[i];
				determined_index=i;
			}
		}
		
		//determine whether it outputs right key class
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<4;j++)
			{
				if(4*invlovedKeyInfo[0]+2*invlovedKeyInfo[1]+invlovedKeyInfo[2]==KeyInKeyClass[i][j])
				{
					theory_index=i;
				}
			}
		}
		if(theory_index==determined_index)
		{
			successNum++;
			cout<<"1"<<flush;
		}
		else
		{
			cout<<"0"<<flush;
		}
	}
	cout<<endl;
	cout<<"successNum is "<<successNum<<" among "<<TotalTestNum<<" tests."<<endl;
	
	return 0;
}