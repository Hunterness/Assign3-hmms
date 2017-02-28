package model;

import Jama.Matrix;

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

		double[][] tmp = makeT(nbrStates);
		T = new Matrix(tmp);

		O = new Matrix[nRow * nCol + 1];
		readings = new Reading[nRow * nCol + 1];
		index = 0;
		for (int i = 0; i < nRow; i++) {
			for (int j = 0; j < nCol; j++) {
				readings[index] = new Reading(i, j, nRow, nCol);
				index++;
			}
		}
		readings[index] = new Reading();

		index = 0;
		for (int i = 0; i < nRow; i++) {
			for (int j = 0; j < nCol; j++) {
				O[index] = makeO(nbrStates, index, nRow, nCol);
				index++;
			}
		}
		
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				System.out.print(T.get(i, j) + " ");
			}
			System.out.println();
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
							// move possible (this)
						}
					} else {
						if (to.getHeading() == from.getHeading()) {
							ProbabilityForMove = PROB_DONT_CHANGE_HEAD_NO_WALL;
						} else {
							ProbabilityForMove = PROB_CHANGE_HEAD_NO_WALL / (nbrPoss - 1);
							// nbrPoss should be more then 0 since at least one
							// move possible (this)
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
	
	public double getOrXY(Reading r, int x, int y)  { // TODO Correct?
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
	
	public double getTProb(State s1, State s2) {
		int indexS1= -1;
		int indexS2  = -1;
		for (int i = 0 ; i < states.length ; i++) {
			if (states[i].equals(s1)) {
				indexS1 = i;
			}
			if (states[i].equals(s2)) {
				indexS2 = i;
			}
		}
//		System.out.println(indexS1 + "   " + indexS2 + "   " + T.get(indexS1, indexS2));
//		System.out.println("   " + s1.getX() + "   " + s1.getY() + "   " + s1.getHeading() +" ----" + s2.getX() + "   " + s2.getY() + "  " + s2.getHeading());
		return T.get(indexS1, indexS2);
	}

	public State[] getStates() {
		return states;
	}
	
	public static void main(String[] args) {
		// Test main
		HMM h = new HMM(new OurLocalizer(4, 4, 4), new Sensor());
		System.out.println(h.getTProb(new State(0,0,State.NORTH,4,4), new State(1,0,State.SOUTH,4,4)));
	}
}
