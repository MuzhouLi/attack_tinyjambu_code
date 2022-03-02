import org.apache.commons.math3.distribution.NormalDistribution;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class ComputeCDFValues_NRK_KP_Test{
	
	public static void main(String[] args){
		
		double Chosen_NIndex=Double.parseDouble(args[0]);
		BigDecimal N=new BigDecimal(0);
		
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
		BigDecimal lnTWO=new BigDecimal(Math.log(2));
		
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
		BigDecimal minRange=BigDecimal.ZERO;
		BigDecimal maxRangeInt=BigDecimal.ZERO;
		BigDecimal minRangeInt=BigDecimal.ZERO;
		
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
			for(int i=0;i<TotalClassNum;i++)
			{
				if(i!=ClassNum)
				{
					System.out.println("Proceeding ClassNum="+ClassNum+", i="+i);
					
					for(int t=0;t<TotalClassNum;t++)
					{
						tmpRange[t][0]=BigDecimal.ZERO;
						tmpRange[t][1]=BigDecimal.ZERO;
					}
					for(int t=0;t<TotalClassNum;t++)
					{
						if(t!=i)
						{
							tmpCoe=(BigDecimalUtil.ln((LinearPr[i].multiply((BigDecimal.ONE).subtract(LinearPr[t]))).divide((LinearPr[t].multiply((BigDecimal.ONE).subtract(LinearPr[i]))),globalDivideScale,rmd),globalDivideScale)).divide(lnTWO,globalDivideScale,rmd);
							
							tmpV=(new BigDecimal(Double.toString(Math.log(ClassSize[t]/(1.0*ClassSize[i]))/Math.log(2)))).add(N.multiply(((BigDecimalUtil.ln((((BigDecimal.ONE).subtract(LinearPr[t])).divide((BigDecimal.ONE).subtract(LinearPr[i]),globalDivideScale,rmd)),globalDivideScale)).divide(lnTWO,globalDivideScale,rmd))));
							
							solution=tmpV.divide(tmpCoe,globalDivideScale,rmd);
							
							if((LinearPr[i].compareTo(LinearPr[t]))==-1)
							{
								tmpRange[t][0]=(BigDecimal.ONE).negate();
							}
							else
							{
								if((LinearPr[i].compareTo(LinearPr[t]))==1)
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
								if((tmpRange[t][1].compareTo(BigDecimal.ZERO)==-1)||(tmpRange[t][1].compareTo(N)==1))
								{
									tmpRange[t][0]=BigDecimal.ZERO;
									tmpRange[t][1]=BigDecimal.ZERO;
								}
							}
						}
					}
					
					maxRange=N;
					minRange=BigDecimal.ZERO;
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
					
					maxRangeInt=new BigDecimal(maxRange.toBigInteger());
					minRangeInt=new BigDecimal((minRange.toBigInteger()).add(BigInteger.ONE));
					
					if((changed==false)||(minRangeInt.compareTo(maxRangeInt)!=-1))
					{
						Pe_Each[ClassNum]+=0.0;
					}
					else
					{
						locV=N.multiply(LinearPr[ClassNum]);
						scaleV=(N.multiply((LinearPr[ClassNum]).subtract((LinearPr[ClassNum]).pow(2))).multiply(B)).sqrt(mc);
						
						up=((maxRangeInt.subtract(locV)).divide(scaleV,globalDivideScale,rmd)).doubleValue();
						down=((minRangeInt.subtract(locV)).divide(scaleV,globalDivideScale,rmd)).doubleValue();
						
						Pe_Each[ClassNum]+=(NormD.cumulativeProbability(up)-NormD.cumulativeProbability(down));
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



























