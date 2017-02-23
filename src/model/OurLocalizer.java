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

	//unclear if needed and where to put but maybe nice to have:P
	private double probChangeHeadingNoWall, probDontChangeHeadingNoWall, probChangeHeadingWall, probDontChangeHeadingWall;
	
	private int nbrOfRows, nbrOfCols, nbrOfhead;
	private State currentState;

	public OurLocalizer( int rows, int cols, int head) {
		this.nbrOfRows = rows;
		this.nbrOfCols = cols;
		this.nbrOfhead = head;
		currentState = new State(1, 1, State.EAST, nbrOfRows, nbrOfCols); //start state right now (1,1) heading east
		
		probDontChangeHeadingNoWall = 0.7;
		probChangeHeadingNoWall = 0.3;
		probDontChangeHeadingWall = 0;
		probChangeHeadingWall = 1;
		
	}	
	
	
	/**
	 * @return the number of assumed rows
	 */
	public int getNumRows() {
		return nbrOfRows;
	}
	
	/**
	 * @return the number of assumed columns
	 */
	public int getNumCols() {
		return nbrOfCols;
	}
	
	/**
	 * @return the number of possible headings for the grid
	 * a number of four headings makes obviously most sense... the viewer can handle 
	 * four headings at maximum in a useful way.
	 */
	public int getNumHead() {
		return nbrOfhead;
	}
	
	
	/**
	 * Triggers one step of the estimation, i.e., true position, sensor reading and 
	 * the probability distribution for the position estimate are updated one step
	 * after one method call.
	 */
	public void update() { //TODO
		
	}
	
	
	/**
	 * @return the currently known true position i.e., after one simulation step
	 * of the robot as (x,y)-pair.
	 */
	public int[] getCurrentTruePosition() {
		int[] ret = new int[2];
		
		ret[0] = currentState.getX();
		ret[1] = currentState.getY();
		
		return ret;
	}

	
	/**
	 * @return the currently available sensor reading obtained for the true position 
	 * after the simulation step 
	 */
	public int[] getCurrentReading() { //TODO
		int[] ret = null;
		
		return ret;
	}

	
	/**
	 * @return the currently estimated (summed) probability for the robot to be in position
	 * (x,y) in the grid. The different headings are not considered, as it makes the 
	 * view somewhat unclear.
	 */
	public double getCurrentProb(int x, int y) { //TODO
		double ret = 0.0;
		return ret;
	}

	
	/**
	 * @return the probability entry of the sensor matrices O to get reading r corresponding 
	 * to position (rX, rY) when actually in position (x, y). If rX or rY (or both) are -1, 
	 * the method should return the probability for the sensor to return "nothing" given 
	 * the robot is in position (x, y).
	 */
	public double getOrXY(int rX, int rY, int x, int y) { //TODO
		/*
		 * note that you have to take care of potentially necessary transformations 
		 * from states i = <x, y, h> to positions (x, y). 
		 */
		return 0;
	}

	
	/**
	 * @return the probability entry (Tij) of the transition matrix T to go from pose 
	 * i = (x, y, h) to pose j = (nX, nY, nH)
	 */
	public double getTProb(int x, int y, int h, int nX, int nY, int nH) { //TODO
		return 0;
	}

}
