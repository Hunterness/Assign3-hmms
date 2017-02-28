package model;

/**
 * @author dat13tma, elt13hli
 *
 */
public class Sensor {
	private double probabilityCorrect, probabilityS1, probabilityS2;

	public Sensor() {
		probabilityCorrect = 0.1;
		probabilityS1 = 0.05;
		probabilityS2 = 0.025;
	}

	/**
	 * Returns the probability for the sensor giving position (rX,rY) when the
	 * true position is (x,y).
	 * 
	 * Assumes that all positions are within the grid.
	 * 
	 * @param rX
	 *            - x coordinate of wanted position, -1 if nothing
	 * @param rY
	 *            - y coordinate of wanted position, -1 if nothing
	 * @param x
	 *            - x coordinate of true position
	 * @param y
	 *            - y coordinate of true position
	 * @param nbrS1
	 *            - number of positions one step from (x,y) ∈ {3,5,8}
	 * @param nbrS2
	 *            - number of positions two steps from (x,y) ∈ {5, 6, 7, 9, 11,
	 *            16}
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
		}
		double probabilityNothing = 1;

		probabilityNothing -= probabilityCorrect;
		probabilityNothing -= probabilityS1 * nbrS1;
		probabilityNothing -= probabilityS2 * nbrS2;

		return probabilityNothing;
	}

}
