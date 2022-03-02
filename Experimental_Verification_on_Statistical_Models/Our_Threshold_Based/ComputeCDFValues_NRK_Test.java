import org.apache.commons.math3.distribution.NormalDistribution;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class ComputeCDFValues_NRK_Test{
	
	public static void main(String[] args){
		
		double Chosen_NIndex=Double.parseDouble(args[0]);
		int attack_type=Integer.parseInt(args[1]);
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
		
		//Following three variables influences accuracy !!!
		int globalDivideScale=50;
		MathContext mc=MathContext.DECIMAL128;
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
		
		BigDecimal LinearCor[]=new BigDecimal[TotalClassNum];
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			LinearCor[ClassNum]=new BigDecimal(Correlation[ClassNum]);
		}
		
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
		double up;
		double down;
		
		NormalDistribution NormD=new NormalDistribution();
		
		for(int ClassNum=0;ClassNum<TotalClassNum;ClassNum++)
		{
			Pe_Each[ClassNum]=0;
			tmpCoe=(N.divide((B.multiply((TWO.pow((2*basic))).subtract(LinearCor[ClassNum].pow(2)))),globalDivideScale,rmd)).sqrt(mc);
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
			System.out.println();
			
		}
		
		System.out.println("Chosen_NIndex= "+Chosen_NIndex+" Using 2**("+(Chosen_NIndex)+") data in total and then we can obtain "+ObtainedKeyInfo+" bits key information with Success Probability "+(1-TotalPe)+".");
		
	}
	

}



























