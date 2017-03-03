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
	private double iterations, correctGuesses, manDist;

	public OurLocalizer(int rows, int cols, int head) {
		this.nbrOfRows = rows;
		this.nbrOfCols = cols;
		this.nbrOfhead = head; // hopefully this is 4
		sensor = new Sensor();
		currentState = new State(0, 0, State.NORTH, nbrOfRows, nbrOfCols);
		// start state right now (0,0) heading north

		currentRead = new Reading();
		hmm = new HMM(this, sensor);

		f = new Matrix(rows * cols * head, 1);
		f.set(0, 0, 0.25);
		f.set(1, 0, 0.25);
		f.set(2, 0, 0.25);
		f.set(3, 0, 0.25);
		iterations = 0;
		correctGuesses = 0;
		manDist = 0;
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
	 */
	public int getNumHead() {
		// a number of four headings makes obviously most sense...
		// the viewer can handle four headings at maximum in a useful way.
		return nbrOfhead;
	}

	/**
	 * Triggers one step of the estimation
	 * 
	 * i.e., true position, sensor reading and the probability distribution for
	 * the position estimate are updated one step after one method call.
	 */
	public void update() {
		// Update State
		double p = Math.random();
		if (!currentState.faceWall() && p >= (1 - HMM.PROB_DONT_CHANGE_HEAD_NO_WALL)) {
			boolean b = currentState.updateState(currentState.getHeading());
			if (!b) {
				System.out.println("Could not update state.");
			}
		} else {
			int[] heads = currentState.allowedHeadings();
			ArrayList<Integer> posIndexes = new ArrayList<Integer>();
			for (int i = 0; i < heads.length; i++) {
				if (heads[i] != -1) {
					posIndexes.add(i);
				}
			}
			Random rand = new Random();
			int i = rand.nextInt(posIndexes.size());
			boolean b = currentState.updateState(heads[posIndexes.get(i)]);
			if (!b) {
				System.out.println("Could not update state.");
			}
		}

		// Get reading from sensor
		currentRead = sensor.getNewReading(currentState, nbrOfRows, nbrOfCols);
		// System.out.println("Current reading: " + currentRead.getX() + " " +
		// currentRead.getY());

		// Update f
		Matrix O = hmm.getO(currentRead);
		Matrix T = hmm.getT().transpose();
		Matrix newF = O.times(T).times(f);
		double sum = 0;
		for (int i = 0; i < newF.getRowDimension(); i++) {
			sum += newF.get(i, 0);
		}
		double alpha = 1 / sum;
		newF = newF.times(alpha);
		f = newF;

		double max = Integer.MIN_VALUE;
		int xx = -1;
		int yy = -1;
		for (int x = 0; x < nbrOfRows; x++) {
			for (int y = 0; y < nbrOfCols; y++) {
				if (getCurrentProb(x, y) >= max) {
					max = getCurrentProb(x, y);
					xx = y;
					yy = x;
				}

			}
		}
		iterations++;
		if (currentState.getX() == xx && currentState.getY() == yy) {
			correctGuesses++;
		}
		manDist += Math.abs(currentState.getX() - xx) + Math.abs(currentState.getY() - yy);
//		System.out.println( correctGuesses/iterations);
		System.out.println(manDist/iterations);
//		System.out.println(currentState.getY() + "  " + currentState.getX() + "       " + yy + "  " + xx);
//		System.out.println((currentState.getY() - yy) + "  " + (currentState.getX() - xx));
		System.out.println();
	}

	/**
	 * @return the currently known true position i.e., after one simulation step
	 *         of the robot as (x,y)-pair.
	 */
	public int[] getCurrentTruePosition() { // our x is viewers y
		int[] ret = new int[2];

		ret[1] = currentState.getX();
		ret[0] = currentState.getY();

		return ret;
	}

	/**
	 * @return the currently available sensor reading obtained for the true
	 *         position after the simulation step
	 */
	public int[] getCurrentReading() { // our x is viewers y
		int[] ret = new int[2];

		ret[1] = currentRead.getX();
		ret[0] = currentRead.getY();

		return ret;
	}

	/**
	 * @return the currently estimated (summed) probability for the robot to be
	 *         in position (x,y) in the grid. The different headings are not
	 *         considered, as it makes the view somewhat unclear.
	 */
	public double getCurrentProb(int x, int y) {
		double ret = 0;
		int index = x * nbrOfRows + y * nbrOfCols * nbrOfhead;

		ret += f.get(index, 0);
		ret += f.get(index + 1, 0);
		ret += f.get(index + 2, 0);
		ret += f.get(index + 3, 0);

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
		// since we start in East and the visualisation on North
		nH = (nH + 3) % nbrOfhead;
		h = (h + 3) % nbrOfhead;

		State from = new State(x, y, h, nbrOfRows, nbrOfCols);
		State to = new State(nX, nY, nH, nbrOfRows, nbrOfCols);
		return hmm.getTProb(from, to);
	}

}
