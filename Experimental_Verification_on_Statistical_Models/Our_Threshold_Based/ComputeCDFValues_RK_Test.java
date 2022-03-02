import org.apache.commons.math3.distribution.NormalDistribution;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class ComputeCDFValues_RK_Test{
	
	public static void main(String[] args){
		
		double Chosen_NIndex=Double.parseDouble(args[0]);
		BigDecimal N=new BigDecimal(0);
		int attack_type=Integer.parseInt(args[1]);
		int Chosen_Diff=Integer.parseInt(args[2]);
		
		if((Chosen_NIndex<20.0)||(Chosen_NIndex>24.5))
		{
			System.out.println("ERROR!!! Chosen_NIndex here is restricted to be 20.0 to 24.5 with the 0.5-step.");
		}
		//Precomputed values for 2^20.0 to 2^24.5 using Casio since BigDecimal didn't support pow() with double type.
		String Arr[]=new String[]{
			"1048576",
			"1482910.40037893051389227955567690837243427225639525870277931013294337029933588235",
			"2097152",
			"2965820.800757861027784559111353816744868544512790517405558620265886740598671764701",
			"4194304",
			"5931641.601515722055569118222707633489737089025581034811117240531773481197343529401",
			"8388608",
			"11863283.2030314441111382364454152669794741780511620696222344810635469623946870588",
			"16777216",
			"23726566.40606288822227647289083053395894835610232413924446896212709392478937411761"
		};
		BigDecimal ArrDecimal[]=new BigDecimal[10];
		for(int i=0;i<10;i++)
		{
			ArrDecimal[i]=new BigDecimal(Arr[i]);
		}
		N=ArrDecimal[(int)((Chosen_NIndex-20)/0.5)];
		
		//Following three variables influences accuracy !!!
		int globalDivideScale=50;
		MathContext mc=MathContext.DECIMAL128;
		RoundingMode rmd=RoundingMode.HALF_UP;
		
		int basic=10;
		double Correlation_NRK[]=new double[]{
			-2.5,-0.5,0.5,2.5
		};
		
		ArrayList<ArrayList<Integer>> List_KeyInKeyClass_NRK=new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tmpKey=new ArrayList<Integer>();
		
		//Read KeyInKeyClass_NRK from file "KeyInKeyClass_NRK_Test.txt"
		File array_file=new File("KeyInKeyClass_NRK_Test.txt");
		BufferedReader brd=null;
		String tmpLine;
		String tmpLineArr[];
		try{
			brd=new BufferedReader(new FileReader(array_file));
			while((tmpLine=brd.readLine())!=null)
			{
				tmpLineArr=tmpLine.split(",");
				tmpKey=new ArrayList<Integer>();
				for(int index=0;index<tmpLineArr.length;index++)
				{
					tmpKey.add(Integer.valueOf(tmpLineArr[index]));
				}
				List_KeyInKeyClass_NRK.add(tmpKey);
			}
			brd.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		int maxLength_Col=List_KeyInKeyClass_NRK.size();
		int maxLength_Row=0;
		for(int index=0;index<maxLength_Col;index++)
		{
			tmpKey=List_KeyInKeyClass_NRK.get(index);
			if(tmpKey.size()>maxLength_Row)
			{
				maxLength_Row=tmpKey.size();
			}
		}
		
		int KeyInKeyClass_NRK[][]=new int[maxLength_Col][maxLength_Row];
		for(int index_i=0;index_i<maxLength_Col;index_i++)
		{
			tmpKey=List_KeyInKeyClass_NRK.get(index_i);
			for(int index_j=0;index_j<tmpKey.size();index_j++)
			{
				KeyInKeyClass_NRK[index_i][index_j]=tmpKey.get(index_j);
			}
			if(tmpKey.size()<maxLength_Row)
			{
				for(int other=tmpKey.size();other<maxLength_Row;other++)
				{
					KeyInKeyClass_NRK[index_i][other]=-1;
				}
			}
		}
		
		ArrayList<Double> Correlation_RK=new ArrayList<Double>();
		ArrayList<ArrayList<Integer>> KeyInKeyClass_RK=new ArrayList<ArrayList<Integer>>();
		
		int another_index_i=0;
		boolean found=false;
		double tmpCorrelation_Diff=0.0;
		boolean containsThisCor=false;
		for(int index_i=0;index_i<maxLength_Col;index_i++)
		{
			for(int index_j=0;index_j<maxLength_Row;index_j++)
			{
				if(KeyInKeyClass_NRK[index_i][index_j]!=-1)
				{
					//System.out.print(KeyInKeyClass_NRK[index_i][index_j]);
					found=false;
					for(int find_i=0;find_i<maxLength_Col;find_i++)
					{
						for(int find_j=0;find_j<maxLength_Row;find_j++)
						{
							if(KeyInKeyClass_NRK[find_i][find_j]!=-1)
							{
								if(KeyInKeyClass_NRK[find_i][find_j]==(KeyInKeyClass_NRK[index_i][index_j]^Chosen_Diff))
								{
									another_index_i=find_i;
									//System.out.print(" "+index_i+" "+another_index_i);
									found=true;
									break;
								}
							}
							else
							{
								break;
							}
						}
						if(found)
						{
							break;
						}
					}
					
					tmpCorrelation_Diff=Correlation_NRK[index_i]-Correlation_NRK[another_index_i];
					//System.out.println(" "+tmpCorrelation_Diff);
					containsThisCor=false;
					for(int ClassNum=0;ClassNum<Correlation_RK.size();ClassNum++)
					{
						if(tmpCorrelation_Diff==Correlation_RK.get(ClassNum))
						{
							KeyInKeyClass_RK.get(ClassNum).add(KeyInKeyClass_NRK[index_i][index_j]);
							containsThisCor=true;
							break;
						}
					}
					if(containsThisCor)
					{
						;
					}
					else
					{
						tmpKey=new ArrayList<Integer>();
						tmpKey.add(KeyInKeyClass_NRK[index_i][index_j]);
						KeyInKeyClass_RK.add(tmpKey);
						Correlation_RK.add(tmpCorrelation_Diff);
					}
				}
				else
				{
					break;
				}
			}
		}
		//sort KeyInKeyClass_RK and Correlation_RK
		Map<Double,ArrayList<Integer>> sorted=new TreeMap<Double,ArrayList<Integer>>();
		ArrayList<Integer> tmpKeySort=new ArrayList<Integer>();
		int sort_counter=0;
		Iterator sort_itor=null;
		double sort_key;
		
		for(int index=0;index<Correlation_RK.size();index++)
		{
			tmpKeySort=new ArrayList<Integer>();
			tmpKey=KeyInKeyClass_RK.get(index);
			for(int index2=0;index2<tmpKey.size();index2++)
			{
				tmpKeySort.add(tmpKey.get(index2));
			}
			sorted.put(Correlation_RK.get(index),tmpKeySort);
		}
		
		Correlation_RK.clear();
		KeyInKeyClass_RK.clear();
		sort_counter=0;
		sort_itor=sorted.keySet().iterator();
		while(sort_itor.hasNext())
		{
			sort_key=(double) sort_itor.next();
			Correlation_RK.add(sort_key);
			tmpKey=(ArrayList<Integer>) sorted.get(sort_key);
			KeyInKeyClass_RK.add(tmpKey);
		}
		/*
		for(int index=0;index<Correlation_RK.size();index++)
		{
			System.out.print(Correlation_RK.get(index));
			System.out.print(" : ");
			tmpKey=KeyInKeyClass_RK.get(index);
			for(int index2=0;index2<tmpKey.size();index2++)
			{
				System.out.print(tmpKey.get(index2));
				System.out.print(" ");
			}
			System.out.println();
		}
		*/
		
		int TotalClassNum=Correlation_RK.size();
		int totalKey=0;
		int ClassSize[]=new int[TotalClassNum];
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			tmpKey=KeyInKeyClass_RK.get(ClassNum);
			ClassSize[ClassNum]=tmpKey.size();
			totalKey+=tmpKey.size();
		}
		
		Double tmpLinearCor[]=new Double[TotalClassNum];
		tmpLinearCor=Correlation_RK.toArray(tmpLinearCor);
		BigDecimal LinearCor[]=new BigDecimal[TotalClassNum];
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			LinearCor[ClassNum]=new BigDecimal(Double.toString(tmpLinearCor[ClassNum]));
		}
		
		BigDecimal TWO=new BigDecimal(2);
		BigDecimal FOUR=new BigDecimal(4);
		BigDecimal lnTWO=new BigDecimal(Math.log(2));
		
		BigDecimal Pr_Each[]=new BigDecimal[TotalClassNum];
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{	
			Pr_Each[ClassNum]=(new BigDecimal(ClassSize[ClassNum])).divide(new BigDecimal(totalKey),globalDivideScale,rmd);
		}
			
		BigDecimal B=new BigDecimal(0);
		if(attack_type==0)
		{
			//KP
			B=BigDecimal.ONE;
		}
		else
		{
			//DKP
			B=((TWO.pow(128)).subtract(N)).divide((TWO.pow(128)).subtract(BigDecimal.ONE),globalDivideScale,rmd);
		}
		
		double Pe_Each[]=new double[TotalClassNum];
		double TotalPe=0.0;
		
		double ObtainedKeyInfo=0.0;
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			ObtainedKeyInfo+=(-1*((Pr_Each[ClassNum]).doubleValue())*Math.log((Pr_Each[ClassNum]).doubleValue())/Math.log(2));
		}
		
		BigDecimal tmpCoe=new BigDecimal(0);
		tmpCoe=(N.divide(((TWO.pow((2*basic+1)))).multiply(B),globalDivideScale,rmd)).sqrt(mc);
		double up;
		double down;
		
		NormalDistribution NormD=new NormalDistribution();
		
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			Pe_Each[ClassNum]=0;
			
			for(int i=0;i<TotalClassNum;i++)
			{
				if(i!=ClassNum)
				{
					if(i==0)
					{
						up=(tmpCoe.multiply(((LinearCor[i].add(LinearCor[i+1])).divide(TWO)).subtract(LinearCor[ClassNum]))).doubleValue();
						Pe_Each[ClassNum]+=NormD.cumulativeProbability(up);
					}
					else if(i==TotalClassNum-1)
					{
						down=(tmpCoe.multiply(((LinearCor[i-1].add(LinearCor[i])).divide(TWO)).subtract(LinearCor[ClassNum]))).doubleValue();
						Pe_Each[ClassNum]+=(1-NormD.cumulativeProbability(down));
					}
					else
					{
						up=(tmpCoe.multiply(((LinearCor[i].add(LinearCor[i+1])).divide(TWO)).subtract(LinearCor[ClassNum]))).doubleValue();
						down=(tmpCoe.multiply(((LinearCor[i-1].add(LinearCor[i])).divide(TWO)).subtract(LinearCor[ClassNum]))).doubleValue();
						Pe_Each[ClassNum]+=(NormD.cumulativeProbability(up)-NormD.cumulativeProbability(down));
					}
				}
			}
			
			TotalPe+=(((Pr_Each[ClassNum]).doubleValue())*Pe_Each[ClassNum]);
			
			System.out.println("ClassNum "+(ClassNum)+" sum "+(Pe_Each[ClassNum])+" "+((Pr_Each[ClassNum]).doubleValue())+" "+((((Pr_Each[ClassNum]).doubleValue())*Pe_Each[ClassNum])));
			//System.out.println();
			
		}
		
		System.out.print("Chosen_NIndex= "+Chosen_NIndex+" Using 2**("+(Chosen_NIndex)+") data ");
		if(attack_type==0)
		{
			System.out.print("(KP)");
		}
		else
		{
			System.out.print("(DKP)");
		}
		System.out.println(" in total and key difference is "+Chosen_Diff+" : we can obtain "+ObtainedKeyInfo+" bits key information with Success Probability "+(1-TotalPe)+". Achieved Key Information is "+(ObtainedKeyInfo*(1-TotalPe)));
	}
	

}



























