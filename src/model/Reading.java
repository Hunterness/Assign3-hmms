package model;


/**
 * @author dat13tma, elt13hli
 *
 */
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

		setNbrNeighbours(nbrRows, nbrCols);
	}

	private void setNbrNeighbours(int nR, int nC) {
		//nothing
		if (x == -1 || y == -1){
			nbrS1 = 0;
			nbrS2 = 0;
			return;
		}
		
		// corner
		if ((x == 0 && y == 0) || (x == 0 && y == nC - 1) || (x == nR - 1 && y == 0) || (x == nR - 1 && y == nC - 1)) {
			nbrS1 = 3;
			nbrS2 = 5;
			return;
		}
		
		// top and bottom row
		if ((x == 0) || (x == nR - 1)) {
			nbrS1 = 5;

			if (y == 1 || y == nC - 2) {
				nbrS2 = 6;
			} else {
				nbrS2 = 9;
			}

			return;
		}

		// first and last column
		if ((y == 0) || (y == nC - 1)) {
			nbrS1 = 5;

			if (x == 1 || x == nR - 2) {
				nbrS2 = 6;
			} else {
				nbrS2 = 9;
			}

			return;
		}

		// second and second to last row
		if ((x == 1) || (x == nR - 2)) {
			nbrS1 = 8;

			if (y == 1 || y == nC - 2) {
				nbrS2 = 7;
			} else {
				nbrS2 = 11;
			}

			return;
		}

		// second and second to last column
		if ((y == 1) || (y == nC - 2)) {
			nbrS1 = 8;

			if (x == 1 || x == nR - 2) {
				nbrS2 = 7;
			} else {
				nbrS2 = 11;
			}

			return;
		}

		// middle of the matrix
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
	 * @return the number of 1 step away neighbours
	 */
	public int getNbrS1() {
		return nbrS1;
	}

	/**
	 * @return the number of 2 step away neighbours
	 */
	public int getNbrS2() {
		return nbrS2;
	}

}
