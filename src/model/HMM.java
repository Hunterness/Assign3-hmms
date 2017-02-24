package model;

public class HMM {

	private double[][] T;
	private double[][] O;
	private Sensor sensor;
	private State[] states;
	
	public HMM(OurLocalizer ol, Sensor sen){
		int nRow = ol.getNumRows();
		int nCol = ol.getNumCols();
		int nHead = ol.getNumHead();
		
		int nbrStates = nRow*nCol*nHead;
		T = new double[nbrStates][nbrStates];
		O = new double[nbrStates][nbrStates];
		
		sensor = sen;
		states = new State[nbrStates];
		
		int index = 0;
		for (int i = 0; i < nRow; i++){
			for (int j = 0; j < nCol; j++){
				states[index] = new State(i,j,State.EAST,nRow,nCol);
				states[index+1] = new State(i,j,State.SOUTH,nRow,nCol);
				states[index+2] = new State(i,j,State.WEST,nRow,nCol);
				states[index+3] = new State(i,j,State.NORTH,nRow,nCol);
				index += nHead;
			}
			
		}
		
		
		for (int i = 0; i < nbrStates; i++){
			System.out.println("index " + i + ": " + states[i]);
			State from = states[i];
			for(int j = 0; j < nbrStates; j++){
				State to = states[j];
				boolean possibleMove = from.allowedMove(to);
				
				if(possibleMove){
					double ProbabilityForMove = 0;//TODO: Calculate/decide this...
					T[i][j] = ProbabilityForMove;
				} else {
					T[i][j] = 0;
				}
			}
			// will always move one step and can only move straight.
		}
	}
	
	
	
	public static void main(String[] args){
		//Test main
		HMM h = new HMM(new OurLocalizer(4,4,4), new Sensor());
	}
}
