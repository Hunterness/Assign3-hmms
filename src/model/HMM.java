package model;

import Jama.Matrix;


/**
 * @author dat13tma, elt13hli
 *
 */
public class HMM {

	private static final double PROB_DONT_CHANGE_HEAD_NO_WALL = 0.7;
	private static final double PROB_CHANGE_HEAD_NO_WALL = 0.3;
	private static final double PROB_DONT_CHANGE_HEAD_WALL = 0;
	private static final double PROB_CHANGE_HEAD_WALL = 1;

	private Matrix T;
	private Matrix[] O;
	private Sensor sensor;
	private State[] states;
	private Reading[] readings;
	private int nCol, nRow, nHead;

	public HMM(OurLocalizer ol, Sensor sen) {
		nRow = ol.getNumRows();
		nCol = ol.getNumCols();
		nHead = ol.getNumHead();

		int nbrStates = nRow * nCol * nHead;

		sensor = sen;
		
		// the possible states
		states = new State[nbrStates];
		int index = 0;
		for (int i = 0; i < nRow; i++) {
			for (int j = 0; j < nCol; j++) {
				states[index] = new State(i, j, State.EAST, nRow, nCol);
				states[index + 1] = new State(i, j, State.SOUTH, nRow, nCol);
				states[index + 2] = new State(i, j, State.WEST, nRow, nCol);
				states[index + 3] = new State(i, j, State.NORTH, nRow, nCol);
				index += nHead;
			}

		}

		// The T matrix
		double[][] tmp = makeT(nbrStates);
		T = new Matrix(tmp);

		// the possible readings from the sensor
		readings = new Reading[nRow * nCol + 1];
		index = 0;
		for (int i = 0; i < nRow; i++) {
			for (int j = 0; j < nCol; j++) {
				readings[index] = new Reading(i, j, nRow, nCol);
				index++;
			}
		}
		readings[index] = new Reading();

		// the O matrices
		int size = nRow * nCol + 1;
		O = new Matrix[size];
		for (int i = 0; i < size; i++) {
			O[i] = makeO(nbrStates, i, nRow, nCol);
		}
	}

	private Matrix makeO(int nbrStates, int index, int nR, int nC) {
		double[][] O = new double[nbrStates][nbrStates];
		Reading r = readings[index];

		for (int i = 0; i < nbrStates; i += 4) {
			double prob = 0;
			State s = states[i];

			if (r.getX() == -1 || r.getY() == -1) {
				// O-matrix for nothing
				
				int nbrS1 = s.getNbrNeighbours(1);
				int nbrS2 = s.getNbrNeighbours(2);
				
				prob = sensor.getProbability(-1, -1, s.getX(), s.getY(), nbrS1, nbrS2);

			} else {
				prob = sensor.getProbability(s.getX(), s.getY(), r.getX(), r.getY(), r.getNbrS1(), r.getNbrS2());
			}

			O[i][i] = prob;
			O[i+1][i+1] = prob;
			O[i+2][i+2] = prob;
			O[i+3][i+3] = prob;

		}
		Matrix ret = new Matrix(O);
		return ret;
	}

	private double[][] makeT(int nbrStates) {
		double[][] T = new double[nbrStates][nbrStates];

		for (int i = 0; i < nbrStates; i++) {
			State from = states[i];

			int[] allHead = from.allowedHeadings();
			int nbrPoss = 0;
			for (int k = 0; k < 4; k++) {
				if (allHead[k] != -1) {
					nbrPoss++;
				}
			}

			for (int j = 0; j < nbrStates; j++) {
				State to = states[j];
				boolean possibleMove = from.allowedMove(to);

				if (possibleMove) {
					double ProbabilityForMove = 0;

					if (from.faceWall()) {
						if (to.getHeading() == from.getHeading()) { 
							// Should be unnecessary since not a possible move
							ProbabilityForMove = PROB_DONT_CHANGE_HEAD_WALL;
						} else {
							ProbabilityForMove = PROB_CHANGE_HEAD_WALL / nbrPoss;
							// nbrPoss should be more then 0 since at least one
							// move possible (this one)
						}
					} else {
						if (to.getHeading() == from.getHeading()) {
							ProbabilityForMove = PROB_DONT_CHANGE_HEAD_NO_WALL;
						} else {
							ProbabilityForMove = PROB_CHANGE_HEAD_NO_WALL / (nbrPoss - 1);
							// nbrPoss should be more then 0 since at least one
							// move possible (this one)
							// -1 since have to change heading and not changing
							// is allowed
						}
					}

					T[i][j] = ProbabilityForMove;
				} else {
					T[i][j] = 0;
				}
			}
			// will always move one step and can only move straight.
		}
		return T;
	}
	
	/**
	 * Gives back the O matrix for the given reading
	 * 
	 * @param r - the current reading from the sensor
	 * @return the O matrix for the reading r
	 */
	public Matrix getO(Reading r) {
		if (r.getX() == -1 && r.getY() == -1)
			return O[O.length-1];
		int index = -1;
		for (int i = 0 ; i < readings.length ; i++) {
			if (readings[i].getX() == r.getX() && readings[i].getY() == r.getY()) {
				index = i;
				break;
			} // will always change sine readings contain all possible readings
		}
		return O[index];
	}
	
	/**
	 * @return the T matrix
	 */
	public Matrix getT() {
		return T;
	}
	
	/**
	 * Returns the probability entry of the sensor matrices O to get reading r when actually in position (x,y).
	 * If rX or rY (or both) are -1 the method return the probability for the sensor 
	 * to return "nothing" given the robot is in position (x, y)
	 * 
	 * @param r - the reading
	 * @param x - the x-coordinate
	 * @param y - the y-coordinate
	 * 
	 * @return the probability to get r when in (x,y) 
	 * 			or the probability for "nothing" for (x,y)
	 * 			if either (or both) coordinates of r is -1
	 */
	public double getOrXY(Reading r, int x, int y)  {
		if (r.getX() == -1 || r.getY() == -1)
			return O[O.length-1].get(x, y);
		
		int indexReadingState = -1;
		int indexTrueState  = -1;
		for (int i = 0 ; i < readings.length ; i++) {
			if (readings[i].getX() == x && readings[i].getY() == y) {
				indexTrueState = i;
			}
			if (readings[i].getX() == r.getX() && readings[i].getY() == r.getY()) {
				indexReadingState = i;
			}
		}
		Matrix o = O[indexReadingState];
		return o.get(indexTrueState*nHead, indexTrueState*nHead);
	}
	
	/**
	 * Gives the probability entry of the transition matrix T for
	 * going from state from to state to
	 * 
	 * @param from - the state to go from
	 * @param to - the state to go to
	 * 
	 * @return the probability for the transition from state from to state to
	 */
	public double getTProb(State from, State to) {
		int indexS1= -1;
		int indexS2  = -1;
		for (int i = 0 ; i < states.length ; i++) {
			if (states[i].equals(from)) {
				indexS1 = i;
			}
			if (states[i].equals(to)) {
				indexS2 = i;
			}
			// will change both index since all possible states is in states
		}
		return T.get(indexS1, indexS2);
	}
	
}
