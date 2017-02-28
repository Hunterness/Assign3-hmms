/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Random;

import Jama.Matrix;
import control.EstimatorInterface;

/**
 * @author dat13tma, elt13hli
 *
 */
public class OurLocalizer implements EstimatorInterface {

	private int nbrOfRows, nbrOfCols, nbrOfhead;
	private State currentState;
	private Reading currentRead;
	private Sensor sensor;
	private HMM hmm;
	private Matrix f;

	public OurLocalizer(int rows, int cols, int head) {
		this.nbrOfRows = rows;
		this.nbrOfCols = cols;
		this.nbrOfhead = head;
		sensor = new Sensor();
		currentState = new State(0, 0, State.NORTH, nbrOfRows, nbrOfCols); // start
																			// state
																			// right
																			// now
																			// (1,1)
																			// heading
																			// east
		currentRead = new Reading();
		hmm = new HMM(this, sensor);
		double[][] tmpF = new double[rows*cols*head][1];
		for (int i = 0 ; i < rows*cols*head ; i++) {
				tmpF[i][0] = 1.0/64.0;
		}
		f = new Matrix(tmpF);
//		f = new Matrix(rows*cols*head,1,0);
//		f.set(0, 0, 0.25);
//		f.set(1, 0, 0.25);
//		f.set(2, 0, 0.25);
//		f.set(3, 0, 0.25);
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
	 * @return the number of possible headings for the grid a number of four
	 *         headings makes obviously most sense... the viewer can handle four
	 *         headings at maximum in a useful way.
	 */
	public int getNumHead() {
		return nbrOfhead;
	}

	/**
	 * Triggers one step of the estimation, i.e., true position, sensor reading
	 * and the probability distribution for the position estimate are updated
	 * one step after one method call.
	 */
	public void update() { // TODO
		// Update State
		int[] heads = currentState.allowedHeadings();
		double p = Math.random();
		if (heads[currentState.getHeading()] != -1 && p >= 0.3) {
			currentState.updateState(currentState.getHeading());
		} else {
			ArrayList<Integer> posIndexes = new ArrayList<Integer>();
			for (int i = 0 ; i < heads.length ; i++) {
				if (heads[i] != -1) {
					posIndexes.add(i);
				}
			}
			Random rand = new Random();
			int i = rand.nextInt(posIndexes.size());
			currentState.updateState(heads[posIndexes.get(i)]);
		}
		// Get reading from sensor
		currentRead = sensor.getNewReading(currentState, nbrOfRows, nbrOfCols);
		// Update f
		Matrix O = hmm.getO(currentRead);
		Matrix T = hmm.getT().transpose();
		Matrix newF = O.times(T).times(f);
		double sum = 0;
		for (int i = 0 ; i < newF.getRowDimension() ; i++) {
			sum += newF.get(i, 0);
		}
		double alpha = 1/sum;
		newF = newF.times(alpha);
		f = newF;
	}

	/**
	 * @return the currently known true position i.e., after one simulation step
	 *         of the robot as (x,y)-pair.
	 */
	public int[] getCurrentTruePosition() {
		int[] ret = new int[2];

		ret[0] = currentState.getX();
		ret[1] = currentState.getY();

		return ret;
	}

	/**
	 * @return the currently available sensor reading obtained for the true
	 *         position after the simulation step
	 */
	public int[] getCurrentReading() { // TODO
		int[] ret = new int[2];
		ret[0] = currentRead.getX();
		ret[1] = currentRead.getY();
		return ret;
	}

	/**
	 * @return the currently estimated (summed) probability for the robot to be
	 *         in position (x,y) in the grid. The different headings are not
	 *         considered, as it makes the view somewhat unclear.
	 */
	public double getCurrentProb(int x, int y) { // TODO
		double ret = 0;
		ret += f.get(x*nbrOfCols+y*nbrOfhead, 0);
		ret += f.get(x*nbrOfCols+y*nbrOfhead+1, 0);
		ret += f.get(x*nbrOfCols+y*nbrOfhead+2, 0);
		ret += f.get(x*nbrOfCols+y*nbrOfhead+3, 0);
		return ret;
	}

	/**
	 * @return the probability entry of the sensor matrices O to get reading r
	 *         corresponding to position (rX, rY) when actually in position (x,
	 *         y). If rX or rY (or both) are -1, the method should return the
	 *         probability for the sensor to return "nothing" given the robot is
	 *         in position (x, y).
	 */
	public double getOrXY(int rX, int rY, int x, int y) { 
		/*
		 * note that you have to take care of potentially necessary
		 * transformations from states i = <x, y, h> to positions (x, y).
		 */
		Reading r = new Reading(rX, rY, nbrOfRows, nbrOfCols);
		return hmm.getOrXY(r, x, y);
	}

	/**
	 * @return the probability entry (Tij) of the transition matrix T to go from
	 *         pose i = (x, y, h) to pose j = (nX, nY, nH)
	 */
	public double getTProb(int x, int y, int h, int nX, int nY, int nH) {
		nH = (nH+3)%nbrOfhead; //WTF??????
		h = (h+3)%nbrOfhead;
		State s1 = new State(x,y,h, nbrOfRows, nbrOfCols);
		State s2 = new State(nX,nY,nH, nbrOfRows, nbrOfCols);
		return hmm.getTProb(s1, s2);
	}

}
