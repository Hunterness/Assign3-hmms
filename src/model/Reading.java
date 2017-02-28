package model;

public class Reading {

	private int x, y, nbrS1, nbrS2;

	/**
	 * Describes the reading (x,y) from the sensor
	 * 
	 * @param x
	 * @param y
	 */
	public Reading(int x, int y, int nbrRows, int nbrCols) {
		this.x = x;
		this.y = y;

		setNeighbours(nbrRows, nbrCols);
	}

	private void setNeighbours(int nR, int nC) {
		if ((x == 0 && y == 0) || (x == 0 && y == nC - 1) || (x == nR - 1 && y == 0) || (x == nR - 1 && y == nC - 1)) {
			nbrS1 = 3;
			nbrS2 = 5;
			return;
		}

		if ((x == 0) || (x == nR - 1)) {
			nbrS1 = 5;

			if (y == 1 || y == nC - 2) {
				nbrS2 = 6;
			} else {
				nbrS2 = 9;
			}

			return;
		}

		if ((y == 0) || (y == nC - 1)) {
			nbrS1 = 5;

			if (x == 1 || x == nR - 2) {
				nbrS2 = 6;
			} else {
				nbrS2 = 9;
			}

			return;
		}

		if ((x == 1) || (x == nR - 2)) {
			nbrS1 = 8;

			if (y == 1 || y == nC - 2) {
				nbrS2 = 7;
			} else {
				nbrS2 = 11;
			}

			return;
		}

		if ((y == 1) || (y == nC - 2)) {
			nbrS1 = 8;

			if (x == 1 || x == nR - 2) {
				nbrS2 = 7;
			} else {
				nbrS2 = 11;
			}

			return;
		}

		nbrS1 = 8;
		nbrS2 = 16;
	}

	/**
	 * Describes an "empty" reading, this is for the nothing answer from the
	 * sensor
	 */
	public Reading() {
		x = -1;
		y = -1;
		nbrS1 = 0;
		nbrS2 = 0;
	}

	/**
	 * returns the number of step away neighbours
	 * 
	 * @param x
	 *            - x coordinate
	 * @param y
	 *            - y coordinate
	 * @param step
	 *            - the step: 1 or 2
	 * @param nR
	 *            - number rows
	 * @param nC
	 *            - number cols
	 * @return the number of step away neighbours
	 */
	public static int getNbrNeighbours(int x, int y, int step, int nR, int nC) {
		
		int nbr = 0;

		if (step == 1) {
			if ((x == 0 && y == 0) || (x == 0 && y == nC - 1) || (x == nR - 1 && y == 0)
					|| (x == nR - 1 && y == nC - 1)) {
				nbr = 3;
			} else if ((x == 0) || (x == nR - 1) || (y == 0) || (y == nC - 1)) {
				nbr = 5;
			} else {
				nbr = 8;
			}
		} else {
			if ((x == 0 && y == 0) || (x == 0 && y == nC - 1) || (x == nR - 1 && y == 0)
					|| (x == nR - 1 && y == nC - 1)) {
				nbr = 5;
			} else if (x == 0 || x == nR - 1) {
				if (y == 1 || y == nC - 2) {
					nbr = 6;
				} else {
					nbr = 9;
				}
			} else if ((y == 0) || (y == nC - 1)) {
				if (x == 1 || x == nR - 2) {
					nbr = 6;
				} else {
					nbr = 9;
				}
			} else if ((x == 1) || (x == nR - 2)) {
				if (y == 1 || y == nC - 2) {
					nbr = 7;
				} else {
					nbr = 11;
				}
			} else if ((y == 1) || (y == nC - 2)) {
				if (x == 1 || x == nR - 2) {
					nbr = 7;
				} else {
					nbr = 11;

				}
			} else {
				nbr = 16;
			}
		}

		return nbr;
	}

	/**
	 * @return the x value of the reading, -1 if nothing
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y value of the reading, -1 if nothing
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the number of step 1 neighbours
	 */
	public int getNbrS1() {
		return nbrS1;
	}

	/**
	 * @return the number of step 2 neighbours
	 */
	public int getNbrS2() {
		return nbrS2;
	}

}
