package model;

public class HMM {

	private double[][] T;
	private double[][] O;
	private Sensor sensor;
	
	public HMM(OurLocalizer ol, Sensor sen){
		int nbrStates = ol.getNumRows()*ol.getNumCols()*ol.getNumHead();
		T = new double[nbrStates][nbrStates];
		O = new double[nbrStates][nbrStates];
		
		sensor = sen;
		
		
		for (int i = 0; i < nbrStates; i++){
			T[i][i] = 0; // will always move one step 
			
			for(int j = 0; j < nbrStates && i != j; j++){
				
			}
			// can only move straight.
		}
	}
}
