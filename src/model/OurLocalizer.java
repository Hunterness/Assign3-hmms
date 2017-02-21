/**
 * 
 */
package model;

import control.EstimatorInterface;

/**
 * @author dat13tma, elt13hli
 *
 */
public class OurLocalizer implements EstimatorInterface {

	private int nbrOfRows, nbrOfCols, nbrOfhead;

	public OurLocalizer( int rows, int cols, int head) {
		this.nbrOfRows = rows;
		this.nbrOfCols = cols;
		this.nbrOfhead = head;
		
	}	
	
	public int getNumRows() {
		return nbrOfRows;
	}
	
	public int getNumCols() {
		return nbrOfCols;
	}
	
	public int getNumHead() {
		return nbrOfhead;
	}

	public void update() { //TODO
		
	}

	public int[] getCurrentTruePosition() { //TODO
		int[] ret = new int[2];
		
		ret[0] = nbrOfRows/2;
		ret[1] = nbrOfCols/2;
		
		return null;
	}

	public int[] getCurrentReading() { //TODO
		int[] ret = null;
		
		return null;
	}

	public double getCurrentProb(int x, int y) { //TODO
		double ret = 0.0;
		return 0;
	}

	public double getOrXY(int rX, int rY, int x, int y) { //TODO
		return 0;
	}

	public double getTProb(int x, int y, int h, int nX, int nY, int nH) { //TODO
		return 0;
	}

}
