import org.apache.commons.math3.distribution.NormalDistribution;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class ComputeCDFValues_NRK_DKP_Test{
	
	public static void main(String[] args){
		
		double Chosen_NIndex=Double.parseDouble(args[0]);
		
		if((Chosen_NIndex<20.0)||(Chosen_NIndex>24.5))
		{
			System.out.println("ERROR!!! Chosen_NIndex here is restricted to be 20.0 to 24.5 with the 0.5-step.");
		}
		
		BigDecimal N=new BigDecimal(0);
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
		};//corresponding to 2^20.0 to 2^24.5
		BigDecimal ArrDecimal[]=new BigDecimal[10];
		for(int i=0;i<10;i++)
		{
			ArrDecimal[i]=new BigDecimal(Arr[i]);
		}
		N=ArrDecimal[(int)((Chosen_NIndex-20)/0.5)];
		
		int globalDivideScale=20;
		MathContext mc=MathContext.DECIMAL32;
		RoundingMode rmd=RoundingMode.HALF_UP;
		
		int basic=10;
		double Correlation[]=new double[]{-2.5,-0.5,0.5,2.5};
		int ClassSize[]=new int[]{1,3,3,1};
		int TotalClassNum=Correlation.length;
		int totalKey=0;
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			totalKey+=ClassSize[ClassNum];
		}
		
		BigDecimal TWO=new BigDecimal(2);
		BigDecimal FOUR=new BigDecimal(4);
		BigDecimal logTWO=new BigDecimal(Math.log(2));
		
		BigDecimal LinearPr[]=new BigDecimal[TotalClassNum];
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			LinearPr[ClassNum]=((BigDecimal.ONE).add(new BigDecimal(Correlation[ClassNum]).divide(TWO.pow(basic)))).divide(TWO);
		}
		
		BigDecimal Pr_Each[]=new BigDecimal[TotalClassNum];
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			Pr_Each[ClassNum]=(new BigDecimal(ClassSize[ClassNum])).divide(new BigDecimal(totalKey),globalDivideScale,rmd);
		}
			
		BigDecimal B=new BigDecimal(0);
		B=((TWO.pow(128)).subtract(N)).divide((TWO.pow(128)).subtract(BigDecimal.ONE),globalDivideScale,rmd);
		
		BigDecimal CoeA=new BigDecimal(0);
		BigDecimal CoeB=new BigDecimal(0);
		BigDecimal CoeC=new BigDecimal(0);
		BigDecimal Delta=new BigDecimal(0);
		
		double Pe_Each[]=new double[TotalClassNum];
		double TotalPe=0.0;
		
		double ObtainedKeyInfo=0.0;
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			ObtainedKeyInfo+=(-1*((Pr_Each[ClassNum]).doubleValue())*Math.log((Pr_Each[ClassNum]).doubleValue())/Math.log(2));
		}
		
		BigDecimal tmpRange[][][]=new BigDecimal[TotalClassNum][2][2];
		BigDecimal updatedRange[][]=new BigDecimal[2][2];
		ArrayList<BigDecimal[]> Interval=new ArrayList<BigDecimal[]>();
		ArrayList<BigDecimal[]> tmpInterval=new ArrayList<BigDecimal[]>();
		int start_t;
		
		BigDecimal solution_1=new BigDecimal(0);
		BigDecimal solution_2=new BigDecimal(0);
		BigDecimal locV=new BigDecimal(0);
		BigDecimal scaleV=new BigDecimal(0);
		double up;
		double down;
		
		NormalDistribution NormD=new NormalDistribution();
		
		boolean stoped=false;
		
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			Pe_Each[ClassNum]=0;
			
			locV=N.multiply(LinearPr[ClassNum]);
			scaleV=(N.multiply((LinearPr[ClassNum]).subtract((LinearPr[ClassNum]).pow(2))).multiply(B)).sqrt(mc);
			
			for(int i=0;i<TotalClassNum;i++)
			{
				if(i!=ClassNum)
				{
					System.out.println("Proceeding ClassNum="+ClassNum+", i="+i);
					
					for(int t=0;t<TotalClassNum;t++)
					{
						tmpRange[t][0][0]=BigDecimal.ZERO;
						tmpRange[t][0][1]=BigDecimal.ZERO;
						tmpRange[t][1][0]=BigDecimal.ZERO;
						tmpRange[t][1][1]=BigDecimal.ZERO;
					}
					
					Interval.clear();
					
					for(int t=0;t<TotalClassNum;t++)
					{
						if(t!=i)
						{
							CoeA=(LinearPr[i].subtract(LinearPr[i].pow(2))).subtract(LinearPr[t].subtract(LinearPr[t].pow(2)));
							//System.out.print("CoeA=");
							//System.out.println(CoeA);
							CoeB=((TWO).multiply(N)).multiply((LinearPr[t].multiply(LinearPr[i].pow(2))).subtract(LinearPr[i].multiply(LinearPr[t].pow(2))));
							//System.out.print("CoeB=");
							//System.out.println(CoeB);
							CoeC=(((N.pow(2)).multiply(LinearPr[t]).multiply(LinearPr[i])).multiply((LinearPr[t]).subtract(LinearPr[i]))).add(N.multiply(B).multiply(LinearPr[i]).multiply((BigDecimal.ONE).subtract(LinearPr[i])).multiply(LinearPr[t]).multiply((BigDecimal.ONE).subtract(LinearPr[t])).multiply((BigDecimalUtil.ln(((Pr_Each[i].pow(2)).multiply((LinearPr[t]).subtract(LinearPr[t].pow(2)))),globalDivideScale).subtract(BigDecimalUtil.ln(((Pr_Each[t].pow(2)).multiply((LinearPr[i]).subtract(LinearPr[i].pow(2)))),globalDivideScale)))));
							//System.out.print("CoeC=");
							//System.out.println(CoeC);
							if(CoeA.signum()==0)
							{
								solution_1=(CoeC.divide(CoeB,globalDivideScale,rmd)).negate();
								if(CoeB.signum()==1)
								{
									if(solution_1.compareTo(N)==1)
									{
										tmpRange[t][0][0]=BigDecimal.ZERO;
										tmpRange[t][0][1]=BigDecimal.ZERO;
									}
									else
									{
										if(solution_1.compareTo(BigDecimal.ZERO)!=1)
										{
											tmpRange[t][0][0]=BigDecimal.ZERO;
										}
										else
										{
											tmpRange[t][0][0]=solution_1;
										}
										tmpRange[t][0][1]=N;
									}
								}
								else
								{
									if(solution_1.compareTo(BigDecimal.ZERO)==-1)
									{
										tmpRange[t][0][0]=BigDecimal.ZERO;
										tmpRange[t][0][1]=BigDecimal.ZERO;
									}
									else
									{
										if(solution_1.compareTo(N)!=-1)
										{
											tmpRange[t][0][1]=N;
										}
										else
										{
											tmpRange[t][0][1]=solution_1;
										}
										tmpRange[t][0][0]=BigDecimal.ZERO;
									}
								}
							}
							else
							{
								Delta=(CoeB.pow(2)).subtract(FOUR.multiply(CoeA).multiply(CoeC));
								
								//System.out.print("Delta=");
								//System.out.println(Delta);
								
								if(Delta.signum()==1)
								{
									solution_1=((CoeB.negate()).add(Delta.sqrt(mc))).divide((TWO.multiply(CoeA)),globalDivideScale,rmd);
									solution_2=((CoeB.negate()).subtract(Delta.sqrt(mc))).divide((TWO.multiply(CoeA)),globalDivideScale,rmd);
									
									if(CoeA.signum()==1)
									{
										if(solution_1.compareTo(N)==1)
										{
											tmpRange[t][0][0]=BigDecimal.ZERO;
											tmpRange[t][0][1]=BigDecimal.ZERO;
										}
										else
										{
											if(solution_1.compareTo(BigDecimal.ZERO)!=1)
											{
												tmpRange[t][0][0]=BigDecimal.ZERO;
											}
											else
											{
												tmpRange[t][0][0]=solution_1;
											}
											tmpRange[t][0][1]=N;
										}
										
										if(solution_2.compareTo(BigDecimal.ZERO)==-1)
										{
											tmpRange[t][1][0]=BigDecimal.ZERO;
											tmpRange[t][1][1]=BigDecimal.ZERO;
										}
										else
										{
											if(solution_2.compareTo(N)!=-1)
											{
												tmpRange[t][1][1]=N;
											}
											else
											{
												tmpRange[t][1][1]=solution_2;
											}
											tmpRange[t][1][0]=BigDecimal.ZERO;
										}
									}
									else
									{
										tmpRange[t][1][0]=BigDecimal.ZERO;
										tmpRange[t][1][1]=BigDecimal.ZERO;
										if((solution_1.compareTo(N)==1)||(solution_2.compareTo(BigDecimal.ZERO)==-1))
										{
											tmpRange[t][0][0]=BigDecimal.ZERO;
											tmpRange[t][0][1]=BigDecimal.ZERO;
										}
										else
										{
											if(solution_1.compareTo(BigDecimal.ZERO)==-1)
											{
												tmpRange[t][0][0]=BigDecimal.ZERO;
											}
											else
											{
												tmpRange[t][0][0]=solution_1;
											}
											if(solution_2.compareTo(N)==1)
											{
												tmpRange[t][0][1]=N;
											}
											else
											{
												tmpRange[t][0][1]=solution_2;
											}
										}
									}
								}
								else
								{
									System.out.print("ERROR!!!! Delta<=0 in ");
									System.out.print(ClassNum);
									System.out.print(i);
									System.out.println(t);
								}
							}
							
						}
					}
					start_t=0;
					for(int t=0;t<TotalClassNum;t++)
					{
						if(t!=i)
						{
							if((tmpRange[t][0][0].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[t][0][1].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[t][1][0].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[t][1][1].compareTo(BigDecimal.ZERO)==0))
							{
								;
							}
							else
							{
								start_t=t;
								break;
							}
						}
					}
					if((tmpRange[start_t][0][0].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[start_t][0][1].compareTo(BigDecimal.ZERO)==0))
					{
						Interval.add(new BigDecimal[]{tmpRange[start_t][1][0],tmpRange[start_t][1][1]});
					}
					else 
					{
						if((tmpRange[start_t][1][0].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[start_t][1][1].compareTo(BigDecimal.ZERO)==0))
						{
							Interval.add(new BigDecimal[]{tmpRange[start_t][0][0],tmpRange[start_t][0][1]});
						}
						else
						{
							Interval.add(new BigDecimal[]{tmpRange[start_t][0][0],tmpRange[start_t][0][1]});
							Interval.add(new BigDecimal[]{tmpRange[start_t][1][0],tmpRange[start_t][1][1]});
						}
					}
					
					stoped=false;
					for(int t=start_t+1;t<TotalClassNum;t++)
					{
						if(t!=i)
						{
							///*
							System.out.println();
							
							System.out.print(tmpRange[t][1][0]);
							System.out.print(" , ");
							System.out.print(tmpRange[t][1][1]);
							System.out.print("     ");
							System.out.print(tmpRange[t][0][0]);
							System.out.print(" , ");
							System.out.println(tmpRange[t][0][1]);
							
							System.out.println("Current:");
							for(int index=0;index<Interval.size();index++)
							{
								System.out.print(Interval.get(index)[0]);
								System.out.print(" , ");
								System.out.println(Interval.get(index)[1]);
							}
							System.out.println();
							//*/
							if((tmpRange[t][0][0].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[t][0][1].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[t][1][0].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[t][1][1].compareTo(BigDecimal.ZERO)==0))
							{
								stoped=true;
								Interval.clear();
								break;
							}
							else
							{
								tmpInterval.clear();
								
								if((tmpRange[t][0][0].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[t][0][1].compareTo(BigDecimal.ZERO)==0)&&((tmpRange[t][1][0].compareTo(BigDecimal.ZERO)!=0)||(tmpRange[t][1][1].compareTo(BigDecimal.ZERO)!=0)))
								{
									//System.out.println("null, A2");
									//null, A2
									for(int index=0;index<Interval.size();index++)
									{
										if(((Interval.get(index)[0]).compareTo(tmpRange[t][1][1])==1)||((Interval.get(index)[1]).compareTo(tmpRange[t][1][0])==-1))
										{
											;
										}
										else
										{
											if((Interval.get(index)[0]).compareTo(tmpRange[t][1][0])==-1)
											{
												updatedRange[0][0]=tmpRange[t][1][0];
											}
											else
											{
												updatedRange[0][0]=Interval.get(index)[0];
											}
											
											if((Interval.get(index)[1]).compareTo(tmpRange[t][1][1])==1)
											{
												updatedRange[0][1]=tmpRange[t][1][1];
											}
											else
											{
												updatedRange[0][1]=Interval.get(index)[1];
											}
											
											tmpInterval.add(new BigDecimal[]{updatedRange[0][0],updatedRange[0][1]});
										}
									}
									/*
									for(int index=0;index<tmpInterval.size();index++)
									{
										System.out.print(tmpInterval.get(index)[0]);
										System.out.print(" , ");
										System.out.println(tmpInterval.get(index)[1]);
									}
									System.out.println();
									*/
								}
								
								if((tmpRange[t][1][0].compareTo(BigDecimal.ZERO)==0)&&(tmpRange[t][1][1].compareTo(BigDecimal.ZERO)==0)&&((tmpRange[t][0][0].compareTo(BigDecimal.ZERO)!=0)||(tmpRange[t][0][1].compareTo(BigDecimal.ZERO)!=0)))
								{
									//System.out.println("A1, null");
									//A1, null
									for(int index=0;index<Interval.size();index++)
									{
										if(((Interval.get(index)[0]).compareTo(tmpRange[t][0][1])==1)||((Interval.get(index)[1]).compareTo(tmpRange[t][0][0])==-1))
										{
											;
										}
										else
										{
											if((Interval.get(index)[0]).compareTo(tmpRange[t][0][0])==-1)
											{
												updatedRange[0][0]=tmpRange[t][0][0];
											}
											else
											{
												updatedRange[0][0]=Interval.get(index)[0];
											}
											
											if((Interval.get(index)[1]).compareTo(tmpRange[t][0][1])==1)
											{
												updatedRange[0][1]=tmpRange[t][0][1];
											}
											else
											{
												updatedRange[0][1]=Interval.get(index)[1];
											}
											
											tmpInterval.add(new BigDecimal[]{updatedRange[0][0],updatedRange[0][1]});
										}
									}
									/*
									for(int index=0;index<tmpInterval.size();index++)
									{
										System.out.print(tmpInterval.get(index)[0]);
										System.out.print(" , ");
										System.out.println(tmpInterval.get(index)[1]);
									}
									System.out.println();
									*/
								}
								
								if(((tmpRange[t][0][0].compareTo(BigDecimal.ZERO)!=0)||(tmpRange[t][0][1].compareTo(BigDecimal.ZERO)!=0))&&((tmpRange[t][1][0].compareTo(BigDecimal.ZERO)!=0)||(tmpRange[t][1][1].compareTo(BigDecimal.ZERO)!=0)))
								{
									//System.out.print("A1, A2");
									//A1, A2
									for(int index=0;index<Interval.size();index++)
									{
										if(((Interval.get(index)[1]).compareTo(tmpRange[t][1][0])==-1)||((Interval.get(index)[0]).compareTo(tmpRange[t][0][1])==1)||(((Interval.get(index)[0]).compareTo(tmpRange[t][1][1])==1)&&((Interval.get(index)[1]).compareTo(tmpRange[t][0][0])==-1)))
										{
											;
										}
										else
										{
											if((Interval.get(index)[1]).compareTo(tmpRange[t][0][0])==-1)
											{
												//System.out.println(" case 1");
												if((Interval.get(index)[1]).compareTo(tmpRange[t][1][1])==1)
												{
													updatedRange[0][1]=tmpRange[t][1][1];
												}
												else
												{
													updatedRange[0][1]=Interval.get(index)[1];
												}
												
												if((Interval.get(index)[0]).compareTo(tmpRange[t][1][0])==-1)
												{
													updatedRange[0][0]=tmpRange[t][1][0];
												}
												else
												{
													updatedRange[0][0]=Interval.get(index)[0];
												}
												tmpInterval.add(new BigDecimal[]{updatedRange[0][0],updatedRange[0][1]});
											}
											else
											{
												if((Interval.get(index)[0]).compareTo(tmpRange[t][1][1])==1)
												{
													//System.out.println(" case 2");
													if((Interval.get(index)[1]).compareTo(tmpRange[t][0][1])==1)
													{
														updatedRange[0][1]=tmpRange[t][0][1];
													}
													else
													{
														updatedRange[0][1]=Interval.get(index)[1];
													}
													
													if((Interval.get(index)[0]).compareTo(tmpRange[t][0][0])==-1)
													{
														updatedRange[0][0]=tmpRange[t][0][0];
													}
													else
													{
														updatedRange[0][0]=Interval.get(index)[0];
													}
													tmpInterval.add(new BigDecimal[]{updatedRange[0][0],updatedRange[0][1]});
												}
												else
												{
													//System.out.println(" case 3");
													if((Interval.get(index)[1]).compareTo(tmpRange[t][0][1])==1)
													{
														updatedRange[0][1]=tmpRange[t][0][1];
													}
													else
													{
														updatedRange[0][1]=Interval.get(index)[1];
													}
													updatedRange[0][0]=tmpRange[t][0][0];
													
													tmpInterval.add(new BigDecimal[]{updatedRange[0][0],updatedRange[0][1]});
													
													if((Interval.get(index)[0]).compareTo(tmpRange[t][1][0])==-1)
													{
														updatedRange[1][0]=tmpRange[t][1][0];
													}
													else
													{
														updatedRange[1][0]=Interval.get(index)[0];
													}
													updatedRange[1][1]=tmpRange[t][1][1];
													
													tmpInterval.add(new BigDecimal[]{updatedRange[1][0],updatedRange[1][1]});
												}
											}
										}
									}
									/*
									for(int index=0;index<tmpInterval.size();index++)
									{
										System.out.print(tmpInterval.get(index)[0]);
										System.out.print(" , ");
										System.out.println(tmpInterval.get(index)[1]);
									}
									System.out.println();
									*/
								}
								
								Interval.clear();
								for(int index=0;index<tmpInterval.size();index++)
								{
									Interval.add(new BigDecimal[]{tmpInterval.get(index)[0],tmpInterval.get(index)[1]});
								}
							}
							
							if(Interval.isEmpty())
							{
								stoped=true;
								break;
							}
							//*
							System.out.println("Updated:");
							for(int index=0;index<Interval.size();index++)
							{
								System.out.print(Interval.get(index)[0]);
								System.out.print(" , ");
								System.out.println(Interval.get(index)[1]);
							}
							System.out.println();
							//*/
						}
					}
					
					//*
					System.out.println(stoped);
					
					for(int index=0;index<Interval.size();index++)
					{
						System.out.print(Interval.get(index)[0]);
						System.out.print(" ");
						System.out.println(Interval.get(index)[1]);
					}
					//*/
					
					if(stoped)
					{
						Pe_Each[ClassNum]+=0.0;
					}
					else
					{
						for(int index=0;index<Interval.size();index++)
						{
							up=(((Interval.get(index)[1]).subtract(locV)).divide(scaleV,globalDivideScale,rmd)).doubleValue();
							down=(((Interval.get(index)[0]).subtract(locV)).divide(scaleV,globalDivideScale,rmd)).doubleValue();
							
							Pe_Each[ClassNum]+=(NormD.cumulativeProbability(up)-NormD.cumulativeProbability(down));
							///*
							System.out.print("HERE up=");
							System.out.print(up);
							System.out.print(" down=");
							System.out.print(down);
							System.out.print(" PR= ");
							System.out.println((NormD.cumulativeProbability(up)-NormD.cumulativeProbability(down)));
							//*/
						}
					}
				}
			}
			
			TotalPe+=(((Pr_Each[ClassNum]).doubleValue())*Pe_Each[ClassNum]);
			
			System.out.println("ClassNum "+(ClassNum)+" sum "+(Pe_Each[ClassNum])+" "+((Pr_Each[ClassNum]).doubleValue())+" "+((((Pr_Each[ClassNum]).doubleValue())*Pe_Each[ClassNum])));
			System.out.println();
			
		}
		
		System.out.println("Chosen_NIndex= "+Chosen_NIndex+" Using 2**("+(Chosen_NIndex)+") data in total and then we can obtain "+ObtainedKeyInfo+" bits key information with Success Probability "+(1-TotalPe)+".");
		
	}
	

}



























