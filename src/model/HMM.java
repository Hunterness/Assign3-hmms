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

	public HMM(OurLocalizer ol, Sensor sen) {
		int nRow = ol.getNumRows();
		int nCol = ol.getNumCols();
		int nHead = ol.getNumHead();

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

		for (int i = 0; i < nbrStates; i++) {
			for (int j = 0; j < nbrStates; j++) {
				System.out.print(O[0].get(i, j) + " ");
			}
			System.out.print("\n");
		}
	}

	private Matrix makeO(int nbrStates, int index, int nR, int nC) {
		double[][] O = new double[nbrStates][nbrStates];
		Reading r = readings[index];

		for (int i = 0; i < nbrStates; i++) {
			double prob = 0;

			if (r.getX() == -1 || r.getY() == -1) {
				// O-matrix for nothing
				
				int nbrS1 = Reading.getNbrNeighbouds(i, i, 1, nR, nC);
				int nbrS2 = Reading.getNbrNeighbouds(i, i, 2, nR, nC);
				
				prob = sensor.getProbability(-1, -1, i, i, nbrS1, nbrS2);

			} else {
				prob = sensor.getProbability(i, i, r.getX(), r.getY(), r.getNbrS1(), r.getNbrS2());
			}

			O[i][i] = prob;

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
						if (to.getHeading() == from.getHeading()) { // Should be
																	// unnecessary
																	// since not
																	// a
																	// possible
																	// move
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

	public static void main(String[] args) {
		// Test main
		HMM h = new HMM(new OurLocalizer(4, 4, 4), new Sensor());

	}
}
