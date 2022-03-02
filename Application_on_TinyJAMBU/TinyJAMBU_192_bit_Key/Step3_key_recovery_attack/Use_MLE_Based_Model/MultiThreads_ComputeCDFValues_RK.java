import org.apache.commons.math3.distribution.NormalDistribution;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class MultiThreads_ComputeCDFValues_RK{
	
	public static void main(String[] args){
		
		double Chosen_NIndex=Double.parseDouble(args[0]);
		BigDecimal N=new BigDecimal(0);
		int attack_type=Integer.parseInt(args[1]);
		int Chosen_Diff=Integer.parseInt(args[2]);
		
		if((Chosen_NIndex<93.6)||(Chosen_NIndex>98.6))
		{
			System.out.println("ERROR!!! Chosen_NIndex here is restricted to be 93.6 to 98.6 with 0.5-step.");
		}
		//Precomputed values for 2^93.6 to 2^98.6 using Casio since BigDecimal didn't support pow() with double type.
		String Arr[]=new String[]{
			"15010929807131071251091088961.8349975895364754025706455846882397246759333126517982392194122152706312733",
			"21228660517075308863831616244.1730416360877909891417397091129321194848025264292392429125273493018251687",
			"30021859614262142502182177923.6699951790729508051412911693764794493518666253035964784388244305412625465",
			"42457321034150617727663232488.3460832721755819782834794182258642389696050528584784858250546986036503374",
			"60043719228524285004364355847.3399903581459016102825823387529588987037332506071929568776488610825250931",
			"84914642068301235455326464976.6921665443511639565669588364517284779392101057169569716501093972073006748",
			"120087438457048570008728711694.679980716291803220565164677505917797407466501214385913755297722165050186",
			"169829284136602470910652929953.38433308870232791313391767290345695587842021143391394330021879441460135",
			"240174876914097140017457423389.359961432583606441130329355011835594814933002428771827510595444330100372",
			"339658568273204941821305859906.768666177404655826267835345806913911756840422867827886600437588829202699",
			"480349753828194280034914846778.719922865167212882260658710023671189629866004857543655021190888660200744"
		};
		BigDecimal ArrDecimal[]=new BigDecimal[11];
		for(int i=0;i<11;i++)
		{
			ArrDecimal[i]=new BigDecimal(Arr[i]);
		}
		N=ArrDecimal[(int)((Chosen_NIndex-93.6)*2)];
		
		//Following three variables influences accuracy !!!
		int globalDivideScale=40;
		MathContext mc=MathContext.DECIMAL64;
		RoundingMode rmd=RoundingMode.HALF_UP;
		
		int basic=42;
		double Correlation_NRK[]=new double[]{-77.875,-68.5,-65.375,-59.125,-56.0,-47.40625,-46.75,-46.625,-44.78125,-41.90625,-41.6875,-41.125,-39.25,-38.78125,-38.5625,-38.03125,-35.65625,-35.5,-35.4375,-35.40625,-34.90625,-33.625,-32.28125,-29.96875,-29.4375,-28.65625,-28.46875,-28.0,-27.84375,-27.625,-27.59375,-27.09375,-26.84375,-26.3125,-26.03125,-25.53125,-25.25,-25.21875,-24.9375,-24.71875,-24.5,-24.46875,-23.34375,-23.1875,-23.0625,-22.90625,-22.84375,-22.40625,-22.15625,-22.125,-21.59375,-21.46875,-21.375,-21.34375,-21.21875,-21.1875,-20.96875,-20.8125,-20.59375,-19.8125,-19.34375,-19.25,-19.0,-18.21875,-17.90625,-17.71875,-17.6875,-17.46875,-17.21875,-16.9375,-16.78125,-16.53125,-16.5,-16.28125,-16.15625,-16.125,-15.875,-15.8125,-15.6875,-15.65625,-15.59375,-15.34375,-15.125,-14.90625,-14.65625,-14.625,-14.59375,-14.5625,-14.34375,-14.3125,-13.9375,-13.8125,-13.78125,-13.71875,-13.5625,-13.53125,-13.46875,-13.25,-13.21875,-13.1875,-13.15625,-13.03125,-13.0,-12.78125,-12.75,-12.5625,-12.28125,-11.96875,-11.9375,-11.90625,-11.8125,-11.625,-11.46875,-11.4375,-11.375,-11.3125,-11.28125,-11.21875,-11.1875,-10.90625,-10.6875,-10.65625,-10.4375,-10.40625,-10.1875,-9.90625,-9.875,-9.75,-9.71875,-9.65625,-9.625,-9.59375,-9.5625,-9.5,-9.46875,-9.4375,-9.40625,-9.125,-9.09375,-9.0,-8.875,-8.84375,-8.8125,-8.78125,-8.5625,-8.53125,-8.5,-8.46875,-8.34375,-8.3125,-8.25,-8.21875,-8.09375,-8.0625,-7.96875,-7.9375,-7.875,-7.84375,-7.71875,-7.6875,-7.625,-7.59375,-7.5625,-7.53125,-7.3125,-7.28125,-7.25,-7.21875,-7.1875,-7.15625,-7.09375,-7.0625,-7.0,-6.96875,-6.9375,-6.90625,-6.84375,-6.8125,-6.78125,-6.75,-6.6875,-6.65625,-6.53125,-6.5,-6.46875,-6.4375,-6.375,-6.34375,-6.1875,-6.0,-5.96875,-5.90625,-5.875,-5.84375,-5.8125,-5.78125,-5.75,-5.71875,-5.6875,-5.65625,-5.53125,-5.5,-5.4375,-5.40625,-5.375,-5.34375,-5.28125,-5.25,-5.21875,-5.1875,-5.15625,-5.125,-5.09375,-5.0625,-5.03125,-4.96875,-4.9375,-4.90625,-4.875,-4.8125,-4.78125,-4.75,-4.71875,-4.6875,-4.65625,-4.59375,-4.5625,-4.53125,-4.5,-4.46875,-4.4375,-4.40625,-4.375,-4.34375,-4.3125,-4.28125,-4.25,-4.1875,-4.15625,-4.125,-4.09375,-4.03125,-4.0,-3.96875,-3.9375,-3.875,-3.84375,-3.78125,-3.75,-3.71875,-3.6875,-3.65625,-3.625,-3.5625,-3.53125,-3.5,-3.46875,-3.4375,-3.40625,-3.375,-3.34375,-3.3125,-3.28125,-3.25,-3.21875,-3.1875,-3.15625,-3.09375,-3.0625,-3.03125,-3.0,-2.9375,-2.90625,-2.875,-2.84375,-2.8125,-2.78125,-2.75,-2.71875,-2.6875,-2.65625,-2.625,-2.59375,-2.5625,-2.53125,-2.46875,-2.4375,-2.40625,-2.375,-2.34375,-2.3125,-2.28125,-2.25,-2.21875,-2.1875,-2.15625,-2.125,-2.09375,-2.0625,-2.03125,-2.0,-1.96875,-1.9375,-1.90625,-1.875,-1.84375,-1.8125,-1.78125,-1.75,-1.71875,-1.6875,-1.65625,-1.625,-1.59375,-1.53125,-1.5,-1.46875,-1.4375,-1.40625,-1.375,-1.34375,-1.3125,-1.28125,-1.25,-1.21875,-1.1875,-1.15625,-1.125,-1.09375,-1.0625,-1.03125,-1.0,-0.96875,-0.9375,-0.90625,-0.875,-0.84375,-0.8125,-0.78125,-0.75,-0.71875,-0.6875,-0.65625,-0.625,-0.59375,-0.5625,-0.53125,-0.5,-0.46875,-0.4375,-0.40625,-0.375,-0.34375,-0.3125,-0.28125,-0.25,-0.21875,-0.1875,-0.15625,-0.125,-0.09375,-0.0625,-0.03125,0.0,0.03125,0.0625,0.09375,0.125,0.15625,0.1875,0.21875,0.25,0.28125,0.3125,0.34375,0.375,0.40625,0.4375,0.46875,0.5,0.53125,0.5625,0.59375,0.625,0.65625,0.6875,0.71875,0.75,0.78125,0.8125,0.84375,0.875,0.90625,0.9375,0.96875,1.0,1.03125,1.0625,1.09375,1.125,1.15625,1.1875,1.21875,1.25,1.28125,1.3125,1.34375,1.375,1.40625,1.4375,1.46875,1.5,1.53125,1.59375,1.625,1.65625,1.6875,1.71875,1.75,1.78125,1.8125,1.84375,1.875,1.90625,1.9375,1.96875,2.0,2.03125,2.0625,2.09375,2.125,2.15625,2.1875,2.21875,2.25,2.28125,2.3125,2.34375,2.375,2.40625,2.4375,2.46875,2.53125,2.5625,2.59375,2.625,2.65625,2.6875,2.71875,2.75,2.78125,2.8125,2.84375,2.875,2.90625,2.9375,3.0,3.03125,3.0625,3.09375,3.15625,3.1875,3.21875,3.25,3.28125,3.3125,3.34375,3.375,3.40625,3.4375,3.46875,3.5,3.53125,3.5625,3.625,3.65625,3.6875,3.71875,3.75,3.78125,3.84375,3.875,3.9375,3.96875,4.0,4.03125,4.09375,4.125,4.15625,4.1875,4.25,4.28125,4.3125,4.34375,4.375,4.40625,4.4375,4.46875,4.5,4.53125,4.5625,4.59375,4.65625,4.6875,4.71875,4.75,4.78125,4.8125,4.875,4.90625,4.9375,4.96875,5.03125,5.0625,5.09375,5.125,5.15625,5.1875,5.21875,5.25,5.28125,5.34375,5.375,5.40625,5.4375,5.5,5.53125,5.65625,5.6875,5.71875,5.75,5.78125,5.8125,5.84375,5.875,5.90625,5.96875,6.0,6.1875,6.34375,6.375,6.4375,6.46875,6.5,6.53125,6.65625,6.6875,6.75,6.78125,6.8125,6.84375,6.90625,6.9375,6.96875,7.0,7.0625,7.09375,7.15625,7.1875,7.21875,7.25,7.28125,7.3125,7.53125,7.5625,7.59375,7.625,7.6875,7.71875,7.84375,7.875,7.9375,7.96875,8.0625,8.09375,8.21875,8.25,8.3125,8.34375,8.46875,8.5,8.53125,8.5625,8.78125,8.8125,8.84375,8.875,9.0,9.09375,9.125,9.40625,9.4375,9.46875,9.5,9.5625,9.59375,9.625,9.65625,9.71875,9.75,9.875,9.90625,10.1875,10.40625,10.4375,10.65625,10.6875,10.90625,11.1875,11.21875,11.28125,11.3125,11.375,11.4375,11.46875,11.625,11.8125,11.90625,11.9375,11.96875,12.28125,12.5625,12.75,12.78125,13.0,13.03125,13.15625,13.1875,13.21875,13.25,13.46875,13.53125,13.5625,13.71875,13.78125,13.8125,13.9375,14.3125,14.34375,14.5625,14.59375,14.625,14.65625,14.90625,15.125,15.34375,15.59375,15.65625,15.6875,15.8125,15.875,16.125,16.15625,16.28125,16.5,16.53125,16.78125,16.9375,17.21875,17.46875,17.6875,17.71875,17.90625,18.21875,19.0,19.25,19.34375,19.8125,20.59375,20.8125,20.96875,21.1875,21.21875,21.34375,21.375,21.46875,21.59375,22.125,22.15625,22.40625,22.84375,22.90625,23.0625,23.1875,23.34375,24.46875,24.5,24.71875,24.9375,25.21875,25.25,25.53125,26.03125,26.3125,26.84375,27.09375,27.59375,27.625,27.84375,28.0,28.46875,28.65625,29.4375,29.96875,32.28125,33.625,34.90625,35.40625,35.4375,35.5,35.65625,38.03125,38.5625,38.78125,39.25,41.125,41.6875,41.90625,44.78125,46.625,46.75,47.40625,56.0,59.125,65.375,68.5,77.875};
		
		ArrayList<ArrayList<Integer>> List_KeyInKeyClass_NRK=new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tmpKey=new ArrayList<Integer>();
		
		//Read KeyInKeyClass_NRK from file "KeyInKeyClass_NRK.txt"
		File array_file=new File("KeyInKeyClass_NRK.txt");
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
		
		BigDecimal N_divide_2basic4=new BigDecimal(0);
		N_divide_2basic4=N.divide(TWO.pow((basic+2)),globalDivideScale,rmd);
		
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
		
		BigDecimal tmpCoe=new BigDecimal(0);
		BigDecimal tmpV=new BigDecimal(0);
		
		double Pe_Each[]=new double[TotalClassNum];
		double TotalPe=0.0;
		
		double ObtainedKeyInfo=0.0;
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			ObtainedKeyInfo+=(-1*((Pr_Each[ClassNum]).doubleValue())*Math.log((Pr_Each[ClassNum]).doubleValue())/Math.log(2));
		}
		
		BigDecimal tmpRange[][]=new BigDecimal[TotalClassNum][2];
		BigDecimal maxRange=N;
		BigDecimal minRange=N.negate();
		
		BigDecimal solution=new BigDecimal(0);
		BigDecimal locV=new BigDecimal(0);
		BigDecimal scaleV=new BigDecimal(0);
		double up;
		double down;
		
		NormalDistribution NormD=new NormalDistribution();
		
		boolean changed=false;
		
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			Pe_Each[ClassNum]=0;
			
			locV=(LinearCor[ClassNum].multiply(N_divide_2basic4)).multiply(TWO);
			scaleV=((N.divide(TWO)).multiply(B)).sqrt(mc);
			
			for(int i=0;i<TotalClassNum;i++)
			{
				if(i!=ClassNum)
				{
					for(int t=0;t<TotalClassNum;t++)
					{
						tmpRange[t][0]=BigDecimal.ZERO;
						tmpRange[t][1]=BigDecimal.ZERO;
					}
					for(int t=0;t<TotalClassNum;t++)
					{
						if(t!=i)
						{
							solution=((B.multiply(TWO.pow(basic)).multiply(new BigDecimal(Double.toString(Math.log(ClassSize[t]/(1.0*ClassSize[i]))/Math.log(2))))).divide((LinearCor[i]).subtract(LinearCor[t]),globalDivideScale,rmd)).add(N_divide_2basic4.multiply((LinearCor[i]).add(LinearCor[t])));
							
							if((LinearCor[i].compareTo(LinearCor[t]))==-1)
							{
								tmpRange[t][0]=(BigDecimal.ONE).negate();
							}
							else
							{
								if((LinearCor[i].compareTo(LinearCor[t]))==1)
								{
									tmpRange[t][0]=BigDecimal.ONE;
								}
								else
								{
									tmpRange[t][0]=BigDecimal.ZERO;
								}
							}
							
							tmpRange[t][1]=solution;
							
						}
					}
					
					for(int t=0;t<TotalClassNum;t++)
					{
						if(t!=i)
						{
							if(tmpRange[t][0].compareTo(BigDecimal.ZERO)!=0)
							{
								if((tmpRange[t][1].compareTo(N.negate())==-1)||(tmpRange[t][1].compareTo(N)==1))
								{
									tmpRange[t][0]=BigDecimal.ZERO;
									tmpRange[t][1]=BigDecimal.ZERO;
								}
							}
						}
					}
					
					maxRange=N;
					minRange=N.negate();
					
					changed=false;
					for(int t=0;t<TotalClassNum;t++)
					{
						if(t!=i)
						{
							if(tmpRange[t][0].compareTo((BigDecimal.ONE).negate())==0)
							{
								if(tmpRange[t][1].compareTo(maxRange)!=1)
								{
									changed=true;
									maxRange=tmpRange[t][1];
								}
							}
							else
							{
								if(tmpRange[t][0].compareTo((BigDecimal.ONE))==0)
								{
									if(tmpRange[t][1].compareTo(minRange)!=-1)
									{
										changed=true;
										minRange=tmpRange[t][1];
									}
								}
							}
						}
					}
					
					if((changed==false)||(minRange.compareTo(maxRange)!=-1))
					{
						Pe_Each[ClassNum]+=0.0;
					}
					else
					{
						up=((maxRange.subtract(locV)).divide(scaleV,globalDivideScale,rmd)).doubleValue();
						down=((minRange.subtract(locV)).divide(scaleV,globalDivideScale,rmd)).doubleValue();
						
						Pe_Each[ClassNum]+=(NormD.cumulativeProbability(up)-NormD.cumulativeProbability(down));
						
						//System.out.print("Proceeding ClassNum="+ClassNum+", i="+i);
						
						//System.out.println(" result= "+up+" "+down+" "+(NormD.cumulativeProbability(up)-NormD.cumulativeProbability(down))+" "+maxRange+" "+minRange+" "+N+" "+locV+" "+scaleV);
					}
					
				}
			}
			
			TotalPe+=(((Pr_Each[ClassNum]).doubleValue())*Pe_Each[ClassNum]);
			
			//System.out.println("ClassNum "+(ClassNum)+" sum "+(Pe_Each[ClassNum])+" "+((Pr_Each[ClassNum]).doubleValue())+" "+((((Pr_Each[ClassNum]).doubleValue())*Pe_Each[ClassNum])));
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



























