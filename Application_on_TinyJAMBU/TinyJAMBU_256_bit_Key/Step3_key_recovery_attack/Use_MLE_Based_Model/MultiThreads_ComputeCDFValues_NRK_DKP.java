import org.apache.commons.math3.distribution.NormalDistribution;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class MultiThreads_ComputeCDFValues_NRK_DKP{
	
	public static void main(String[] args){
		
		double Chosen_NIndex=Double.parseDouble(args[0]);
		int Chosen_ClassNum=Integer.parseInt(args[1]);
		
		BigDecimal N=new BigDecimal(0);
		
		if((Chosen_NIndex<96.6)||(Chosen_NIndex>99.4))
		{
			System.out.println("ERROR!!! Chosen_NIndex here is restricted to be 96.6 to 99.4 and only keeps two decimals.");
		}
		//Precomputed values for 2^96.6 to 2^99.4 using Casio since BigDecimal didn't support pow() with double type.
		String Arr[]=new String[]{
			"120087438457048570008728711694.679980716291803220565164677505917797407466501214385913755297722165050186",
			"137944243011419372085533552818.498678683550454899365827066250142245815723183337978885831359069724780203",
			"158456325028528675187087900672",
			"182018519899146395331313042446.756895756699562031968158377281580059454900020057750842859947849082348219",
			"209084374387144546547234016985.448592187286641246817354968992040002953376096320316690975849147858541922",
			"240174876914097140017457423389.359961432583606441130329355011835594814933002428771827510595444330100372",
			"275888486022838744171067105636.997357367100909798731654132500284491631446366675957771662718139449560406",
			"316912650057057350374175801344",
			"364037039798292790662626084893.513791513399124063936316754563160118909800040115501685719895698164696438",
			"418168748774289093094468033970.897184374573282493634709937984080005906752192640633381951698295717083843",
			"480349753828194280034914846778.719922865167212882260658710023671189629866004857543655021190888660200744",
			"551776972045677488342134211273.994714734201819597463308265000568983262892733351915543325436278899120812",
			"633825300114114700748351602688",
			"728074079596585581325252169787.027583026798248127872633509126320237819600080231003371439791396329392876",
			"836337497548578186188936067941.794368749146564987269419875968160011813504385281266763903396591434167686"
		};
		BigDecimal ArrDecimal[]=new BigDecimal[15];
		for(int i=0;i<15;i++)
		{
			ArrDecimal[i]=new BigDecimal(Arr[i]);
		}
		N=ArrDecimal[(int)((Chosen_NIndex-96.6)*5)];
		
		//Following three variables influences accuracy !!!
		int globalDivideScale=40;
		MathContext mc=MathContext.DECIMAL128;
		RoundingMode rmd=RoundingMode.HALF_UP;
		
		int basic=42;
		double Correlation[]=new double[]{-77.875,-68.5,-65.375,-59.125,-56.0,-47.40625,-46.75,-46.625,-44.78125,-41.90625,-41.6875,-41.125,-39.25,-38.78125,-38.5625,-38.03125,-35.65625,-35.5,-35.4375,-35.40625,-34.90625,-33.625,-32.28125,-29.96875,-29.4375,-28.65625,-28.46875,-28.0,-27.84375,-27.625,-27.59375,-27.09375,-26.84375,-26.3125,-26.03125,-25.53125,-25.25,-25.21875,-24.9375,-24.71875,-24.5,-24.46875,-23.34375,-23.1875,-23.0625,-22.90625,-22.84375,-22.40625,-22.15625,-22.125,-21.59375,-21.46875,-21.375,-21.34375,-21.21875,-21.1875,-20.96875,-20.8125,-20.59375,-19.8125,-19.34375,-19.25,-19.0,-18.21875,-17.90625,-17.71875,-17.6875,-17.46875,-17.21875,-16.9375,-16.78125,-16.53125,-16.5,-16.28125,-16.15625,-16.125,-15.875,-15.8125,-15.6875,-15.65625,-15.59375,-15.34375,-15.125,-14.90625,-14.65625,-14.625,-14.59375,-14.5625,-14.34375,-14.3125,-13.9375,-13.8125,-13.78125,-13.71875,-13.5625,-13.53125,-13.46875,-13.25,-13.21875,-13.1875,-13.15625,-13.03125,-13.0,-12.78125,-12.75,-12.5625,-12.28125,-11.96875,-11.9375,-11.90625,-11.8125,-11.625,-11.46875,-11.4375,-11.375,-11.3125,-11.28125,-11.21875,-11.1875,-10.90625,-10.6875,-10.65625,-10.4375,-10.40625,-10.1875,-9.90625,-9.875,-9.75,-9.71875,-9.65625,-9.625,-9.59375,-9.5625,-9.5,-9.46875,-9.4375,-9.40625,-9.125,-9.09375,-9.0,-8.875,-8.84375,-8.8125,-8.78125,-8.5625,-8.53125,-8.5,-8.46875,-8.34375,-8.3125,-8.25,-8.21875,-8.09375,-8.0625,-7.96875,-7.9375,-7.875,-7.84375,-7.71875,-7.6875,-7.625,-7.59375,-7.5625,-7.53125,-7.3125,-7.28125,-7.25,-7.21875,-7.1875,-7.15625,-7.09375,-7.0625,-7.0,-6.96875,-6.9375,-6.90625,-6.84375,-6.8125,-6.78125,-6.75,-6.6875,-6.65625,-6.53125,-6.5,-6.46875,-6.4375,-6.375,-6.34375,-6.1875,-6.0,-5.96875,-5.90625,-5.875,-5.84375,-5.8125,-5.78125,-5.75,-5.71875,-5.6875,-5.65625,-5.53125,-5.5,-5.4375,-5.40625,-5.375,-5.34375,-5.28125,-5.25,-5.21875,-5.1875,-5.15625,-5.125,-5.09375,-5.0625,-5.03125,-4.96875,-4.9375,-4.90625,-4.875,-4.8125,-4.78125,-4.75,-4.71875,-4.6875,-4.65625,-4.59375,-4.5625,-4.53125,-4.5,-4.46875,-4.4375,-4.40625,-4.375,-4.34375,-4.3125,-4.28125,-4.25,-4.1875,-4.15625,-4.125,-4.09375,-4.03125,-4.0,-3.96875,-3.9375,-3.875,-3.84375,-3.78125,-3.75,-3.71875,-3.6875,-3.65625,-3.625,-3.5625,-3.53125,-3.5,-3.46875,-3.4375,-3.40625,-3.375,-3.34375,-3.3125,-3.28125,-3.25,-3.21875,-3.1875,-3.15625,-3.09375,-3.0625,-3.03125,-3.0,-2.9375,-2.90625,-2.875,-2.84375,-2.8125,-2.78125,-2.75,-2.71875,-2.6875,-2.65625,-2.625,-2.59375,-2.5625,-2.53125,-2.46875,-2.4375,-2.40625,-2.375,-2.34375,-2.3125,-2.28125,-2.25,-2.21875,-2.1875,-2.15625,-2.125,-2.09375,-2.0625,-2.03125,-2.0,-1.96875,-1.9375,-1.90625,-1.875,-1.84375,-1.8125,-1.78125,-1.75,-1.71875,-1.6875,-1.65625,-1.625,-1.59375,-1.53125,-1.5,-1.46875,-1.4375,-1.40625,-1.375,-1.34375,-1.3125,-1.28125,-1.25,-1.21875,-1.1875,-1.15625,-1.125,-1.09375,-1.0625,-1.03125,-1.0,-0.96875,-0.9375,-0.90625,-0.875,-0.84375,-0.8125,-0.78125,-0.75,-0.71875,-0.6875,-0.65625,-0.625,-0.59375,-0.5625,-0.53125,-0.5,-0.46875,-0.4375,-0.40625,-0.375,-0.34375,-0.3125,-0.28125,-0.25,-0.21875,-0.1875,-0.15625,-0.125,-0.09375,-0.0625,-0.03125,0.0,0.03125,0.0625,0.09375,0.125,0.15625,0.1875,0.21875,0.25,0.28125,0.3125,0.34375,0.375,0.40625,0.4375,0.46875,0.5,0.53125,0.5625,0.59375,0.625,0.65625,0.6875,0.71875,0.75,0.78125,0.8125,0.84375,0.875,0.90625,0.9375,0.96875,1.0,1.03125,1.0625,1.09375,1.125,1.15625,1.1875,1.21875,1.25,1.28125,1.3125,1.34375,1.375,1.40625,1.4375,1.46875,1.5,1.53125,1.59375,1.625,1.65625,1.6875,1.71875,1.75,1.78125,1.8125,1.84375,1.875,1.90625,1.9375,1.96875,2.0,2.03125,2.0625,2.09375,2.125,2.15625,2.1875,2.21875,2.25,2.28125,2.3125,2.34375,2.375,2.40625,2.4375,2.46875,2.53125,2.5625,2.59375,2.625,2.65625,2.6875,2.71875,2.75,2.78125,2.8125,2.84375,2.875,2.90625,2.9375,3.0,3.03125,3.0625,3.09375,3.15625,3.1875,3.21875,3.25,3.28125,3.3125,3.34375,3.375,3.40625,3.4375,3.46875,3.5,3.53125,3.5625,3.625,3.65625,3.6875,3.71875,3.75,3.78125,3.84375,3.875,3.9375,3.96875,4.0,4.03125,4.09375,4.125,4.15625,4.1875,4.25,4.28125,4.3125,4.34375,4.375,4.40625,4.4375,4.46875,4.5,4.53125,4.5625,4.59375,4.65625,4.6875,4.71875,4.75,4.78125,4.8125,4.875,4.90625,4.9375,4.96875,5.03125,5.0625,5.09375,5.125,5.15625,5.1875,5.21875,5.25,5.28125,5.34375,5.375,5.40625,5.4375,5.5,5.53125,5.65625,5.6875,5.71875,5.75,5.78125,5.8125,5.84375,5.875,5.90625,5.96875,6.0,6.1875,6.34375,6.375,6.4375,6.46875,6.5,6.53125,6.65625,6.6875,6.75,6.78125,6.8125,6.84375,6.90625,6.9375,6.96875,7.0,7.0625,7.09375,7.15625,7.1875,7.21875,7.25,7.28125,7.3125,7.53125,7.5625,7.59375,7.625,7.6875,7.71875,7.84375,7.875,7.9375,7.96875,8.0625,8.09375,8.21875,8.25,8.3125,8.34375,8.46875,8.5,8.53125,8.5625,8.78125,8.8125,8.84375,8.875,9.0,9.09375,9.125,9.40625,9.4375,9.46875,9.5,9.5625,9.59375,9.625,9.65625,9.71875,9.75,9.875,9.90625,10.1875,10.40625,10.4375,10.65625,10.6875,10.90625,11.1875,11.21875,11.28125,11.3125,11.375,11.4375,11.46875,11.625,11.8125,11.90625,11.9375,11.96875,12.28125,12.5625,12.75,12.78125,13.0,13.03125,13.15625,13.1875,13.21875,13.25,13.46875,13.53125,13.5625,13.71875,13.78125,13.8125,13.9375,14.3125,14.34375,14.5625,14.59375,14.625,14.65625,14.90625,15.125,15.34375,15.59375,15.65625,15.6875,15.8125,15.875,16.125,16.15625,16.28125,16.5,16.53125,16.78125,16.9375,17.21875,17.46875,17.6875,17.71875,17.90625,18.21875,19.0,19.25,19.34375,19.8125,20.59375,20.8125,20.96875,21.1875,21.21875,21.34375,21.375,21.46875,21.59375,22.125,22.15625,22.40625,22.84375,22.90625,23.0625,23.1875,23.34375,24.46875,24.5,24.71875,24.9375,25.21875,25.25,25.53125,26.03125,26.3125,26.84375,27.09375,27.59375,27.625,27.84375,28.0,28.46875,28.65625,29.4375,29.96875,32.28125,33.625,34.90625,35.40625,35.4375,35.5,35.65625,38.03125,38.5625,38.78125,39.25,41.125,41.6875,41.90625,44.78125,46.625,46.75,47.40625,56.0,59.125,65.375,68.5,77.875};
		int ClassSize[]=new int[]{1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 2, 2, 1, 4, 4, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1, 1, 1, 2, 2, 2, 6, 1, 2, 1, 2, 2, 2, 2, 4, 4, 5, 4, 1, 4, 2, 2, 2, 2, 4, 2, 2, 2, 4, 2, 2, 1, 12, 8, 2, 1, 2, 2, 1, 2, 5, 11, 8, 1, 6, 2, 2, 2, 6, 5, 2, 8, 2, 1, 2, 1, 2, 8, 4, 5, 4, 6, 4, 4, 1, 1, 8, 6, 2, 4, 1, 2, 4, 2, 1, 2, 26, 12, 18, 21, 12, 8, 3, 1, 2, 2, 2, 11, 2, 2, 2, 4, 4, 8, 1, 19, 25, 10, 8, 6, 6, 6, 2, 5, 14, 10, 1, 2, 8, 2, 2, 2, 16, 8, 6, 2, 13, 4, 6, 1, 4, 4, 2, 28, 28, 2, 4, 27, 32, 2, 1, 20, 28, 2, 4, 23, 22, 4, 8, 12, 18, 4, 4, 1, 3, 1, 2, 6, 6, 2, 11, 4, 2, 10, 16, 8, 4, 12, 5, 20, 9, 2, 1, 10, 52, 44, 2, 4, 1, 4, 1, 44, 53, 5, 2, 14, 28, 6, 6, 2, 4, 13, 2, 13, 16, 2, 6, 1, 28, 26, 18, 14, 4, 2, 42, 43, 9, 2, 4, 2, 10, 8, 8, 8, 2, 49, 40, 4, 28, 22, 25, 12, 40, 48, 22, 10, 8, 16, 4, 2, 7, 16, 1, 2, 4, 2, 93, 110, 4, 2, 10, 16, 4, 83, 92, 15, 12, 4, 2, 6, 22, 13, 20, 16, 15, 18, 20, 32, 51, 73, 18, 8, 9, 86, 109, 2, 56, 65, 8, 10, 94, 89, 19, 40, 8, 6, 7, 115, 93, 2, 36, 22, 75, 80, 9, 56, 66, 32, 40, 16, 94, 99, 138, 114, 12, 90, 60, 29, 22, 123, 144, 57, 83, 90, 97, 45, 269, 270, 16, 82, 61, 55, 108, 62, 281, 280, 137, 138, 60, 80, 120, 396, 301, 42, 180, 129, 161, 274, 117, 282, 400, 346, 292, 164, 230, 194, 570, 458, 106, 558, 432, 360, 586, 260, 452, 685, 446, 455, 480, 455, 446, 685, 452, 260, 586, 360, 432, 558, 106, 458, 570, 194, 230, 164, 292, 346, 400, 282, 117, 274, 161, 129, 180, 42, 301, 396, 120, 80, 60, 138, 137, 280, 281, 62, 108, 55, 61, 82, 16, 270, 269, 45, 97, 90, 83, 57, 144, 123, 22, 29, 60, 90, 12, 114, 138, 99, 94, 16, 40, 32, 66, 56, 9, 80, 75, 22, 36, 2, 93, 115, 7, 6, 8, 40, 19, 89, 94, 10, 8, 65, 56, 2, 109, 86, 9, 8, 18, 73, 51, 32, 20, 18, 15, 16, 20, 13, 22, 6, 2, 4, 12, 15, 92, 83, 4, 16, 10, 2, 4, 110, 93, 2, 4, 2, 1, 16, 7, 2, 4, 16, 8, 10, 22, 48, 40, 12, 25, 22, 28, 4, 40, 49, 2, 8, 8, 8, 10, 2, 4, 2, 9, 43, 42, 2, 4, 14, 18, 26, 28, 1, 6, 2, 16, 13, 2, 13, 4, 2, 6, 6, 28, 14, 2, 5, 53, 44, 1, 4, 1, 4, 2, 44, 52, 10, 1, 2, 9, 20, 5, 12, 4, 8, 16, 10, 2, 4, 11, 2, 6, 6, 2, 1, 3, 1, 4, 4, 18, 12, 8, 4, 22, 23, 4, 2, 28, 20, 1, 2, 32, 27, 4, 2, 28, 28, 2, 4, 4, 1, 6, 4, 13, 2, 6, 8, 16, 2, 2, 2, 8, 2, 1, 10, 14, 5, 2, 6, 6, 6, 8, 10, 25, 19, 1, 8, 4, 4, 2, 2, 2, 11, 2, 2, 2, 1, 3, 8, 12, 21, 18, 12, 26, 2, 1, 2, 4, 2, 1, 4, 2, 6, 8, 1, 1, 4, 4, 6, 4, 5, 4, 8, 2, 1, 2, 1, 2, 8, 2, 5, 6, 2, 2, 2, 6, 1, 8, 11, 5, 2, 1, 2, 2, 1, 2, 8, 12, 1, 2, 2, 4, 2, 2, 2, 4, 2, 2, 2, 2, 4, 1, 4, 5, 4, 4, 2, 2, 2, 2, 1, 2, 1, 6, 2, 2, 2, 1, 1, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 4, 4, 1, 2, 2, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1};
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
		
		for(int ClassNum=Chosen_ClassNum;ClassNum<Chosen_ClassNum+1;ClassNum++)
		{
			Pe_Each[ClassNum]=0;
			
			locV=N.multiply(LinearPr[ClassNum]);
			scaleV=(N.multiply((LinearPr[ClassNum]).subtract((LinearPr[ClassNum]).pow(2))).multiply(B)).sqrt(mc);
			
			for(int i=0;i<TotalClassNum;i++)
			{
				if(i!=ClassNum)
				{
					//System.out.println("Proceeding ClassNum="+ClassNum+", i="+i);
					
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
							/*
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
							*/
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
							/*
							System.out.println("Updated:");
							for(int index=0;index<Interval.size();index++)
							{
								System.out.print(Interval.get(index)[0]);
								System.out.print(" , ");
								System.out.println(Interval.get(index)[1]);
							}
							System.out.println();
							*/
						}
					}
					
					/*
					System.out.println(stoped);
					
					for(int index=0;index<Interval.size();index++)
					{
						System.out.print(Interval.get(index)[0]);
						System.out.print(" ");
						System.out.println(Interval.get(index)[1]);
					}
					*/
					
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
							/*
							System.out.print("HERE up=");
							System.out.print(up);
							System.out.print(" down=");
							System.out.print(down);
							System.out.print(" PR= ");
							System.out.println((NormD.cumulativeProbability(up)-NormD.cumulativeProbability(down)));
							*/
						}
					}
				}
			}
			
			TotalPe+=(((Pr_Each[ClassNum]).doubleValue())*Pe_Each[ClassNum]);
			
			System.out.println("ClassNum "+(ClassNum)+" sum "+(Pe_Each[ClassNum])+" "+((Pr_Each[ClassNum]).doubleValue())+" "+((((Pr_Each[ClassNum]).doubleValue())*Pe_Each[ClassNum])));
			System.out.println();
			
		}
		
		//System.out.println("Chosen_NIndex= "+Chosen_NIndex+" Using 2**("+(Chosen_NIndex)+") data in total and then we can obtain "+ObtainedKeyInfo+" bits key information with Success Probability "+(1-TotalPe)+".");
		System.out.println("ObtainedKeyInfo is "+ObtainedKeyInfo);
	}
	

}



























