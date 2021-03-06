package model;

import java.util.Random;

/**
 * @author dat13tma, elt13hli
 *
 */
public class Sensor {
	private final double probabilityCorrect = 0.1, probabilityS1 = 0.05, probabilityS2 = 0.025;

	public Sensor() {
	}

	/**
	 * Returns the probability for the sensor giving position (rX,rY) when the
	 * true position is (x,y).
	 * 
	 * Assumes that all positions are within the grid.
	 * 
	 * @param rX
	 *            - x coordinate of wanted position, -1 if a "nothing"-reading
	 * @param rY
	 *            - y coordinate of wanted position, -1 if a "nothing"-reading
	 * @param x
	 *            - x coordinate of true position
	 * @param y
	 *            - y coordinate of true position
	 * @param nbrS1
	 *            - number of positions one step from (x,y) ∈ {3,5,8}
	 * @param nbrS2
	 *            - number of positions two steps from (x,y) ∈ {5, 6, 7, 9, 11,
	 *            16}
	 * 
	 * @return the probability of the sensor for the position (rX, rY) when
	 *         actually in position (x, y)
	 */
	public double getProbability(int rX, int rY, int x, int y, int nbrS1, int nbrS2) {

		if (rX != -1 && rY != -1) {

			if (x == rX && y == rY) {
				return probabilityCorrect;
			}

			if ((x == rX - 1 || x == rX + 1 || x == rX) && (y == rY - 1 || y == rY + 1 || y == rY)) {
				return probabilityS1;
			}

			if ((x == rX - 2 || x == rX + 2 || x == rX - 1 || x == rX + 1 || x == rX)
					&& (y == rY - 2 || y == rY + 2 || y == rY - 1 || y == rY + 1 || y == rY)) {
				return probabilityS2;
			}

			return 0;
		}
		double probabilityNothing = 1;

		probabilityNothing -= probabilityCorrect;
		probabilityNothing -= probabilityS1 * nbrS1;
		probabilityNothing -= probabilityS2 * nbrS2;

		return probabilityNothing;
	}

	/**
	 * The sensor makes a new reading and returns it.
	 * 
	 * @param currState
	 *            - the current true state of the robot
	 * @param nbrOfRows
	 *            - number of rows in the grid
	 * @param nbrOfCols
	 *            - number of columns in the grid
	 * 
	 * @return the reading of the Sensor
	 */

	public Reading getNewReading(State currState, int nbrOfRows, int nbrOfCols) {
		double p = Math.random();
		double prob = probabilityCorrect;
		
		if (p <= prob) {
			return new Reading(currState.getX(), currState.getY(), nbrOfRows, nbrOfCols);
		}
		
		Reading[] ns1 = currState.getNeighbours(1);
		int nbrNeigh1 = ns1.length;
		for (int i = 0; i < nbrNeigh1; i++){
			prob += probabilityS1;
			if (p <= prob) {
				return ns1[i];
			}
		}
		
		Reading[] ns2 = currState.getNeighbours(2);
		int nbrNeigh2 = ns2.length;
		for (int i = 0; i < nbrNeigh2; i++){
			prob += probabilityS2;
			if (p <= prob) {
				return ns2[i];
			}
		}
		
		return new Reading();

	}

}
