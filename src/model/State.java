package model;

import java.util.ArrayList;

/**
 * @author dat13tma, elt13hli
 *
 */
public class State {
	public static final int EAST = 0;
	public static final int SOUTH = 1;
	public static final int WEST = 2;
	public static final int NORTH = 3;

	private int x, y, heading, nbrRows, nbrCols;
	private ArrayList<Pair> neighS1, neighS2;

	public State(int row, int col, int head, int nbrRows, int nbrCols) {
		
		x = row;
		y = col;
		heading = head;
		this.nbrRows = nbrRows;
		this.nbrCols = nbrCols;

		neighS1 = new ArrayList<Pair>();
		neighS2 = new ArrayList<Pair>();

		setNeighbours(1);
		setNeighbours(2);

	}

	private void setNeighbours(int step) {
		if (step == 1) {
			//one row above
			if (x-1 >= 0){
				neighS1.add(new Pair(x-1, y));
				if (y-1 >= 0){
					neighS1.add(new Pair(x-1, y-1));
				}
				if (y+1 < nbrCols){
					neighS1.add(new Pair(x-1, y+1));
				}
			}
			
			//same row
			if (y-1 >= 0){
				neighS1.add(new Pair(x, y-1));
			}
			if (y+1 < nbrCols){
				neighS1.add(new Pair(x, y+1));
			}
			
			//one row below
			if (x+1 < nbrRows){
				neighS1.add(new Pair(x+1, y));
				if (y-1 >= 0){
					neighS1.add(new Pair(x+1, y-1));
				}
				if (y+1 < nbrCols){
					neighS1.add(new Pair(x+1, y+1));
				}
			}
			
			
		} else {

			//two rows above
			if (x-2 >= 0){
				neighS2.add(new Pair(x-2, y));
				if (y-2 >= 0)
					neighS2.add(new Pair(x-2, y-2));
				if (y-1 >= 0)
					neighS2.add(new Pair(x-2, y-1));
				if (y+1 < nbrCols)
					neighS2.add(new Pair(x-2, y+1));
				if (y+2 < nbrCols)
					neighS2.add(new Pair(x-2, y+2));	
			}
			
			//one row above
			if(x-1 >= 0){
				if (y-2 >= 0){
					neighS2.add(new Pair(x-1, y-2));
				}
				if (y+2 < nbrCols){
					neighS2.add(new Pair(x-1, y+2));
				}
			}
			
			//same row
			if (y-2 >= 0){
				neighS2.add(new Pair(x, y-2));
			}
			if (y+2 < nbrCols){
				neighS2.add(new Pair(x, y+2));
			}
			
			//one row below
			if(x+1 < nbrRows){
				if (y-2 >= 0){
					neighS2.add(new Pair(x+1, y-2));
				}
				if (y+2 < nbrCols){
					neighS2.add(new Pair(x+1, y+2));
				}
			}
			
			//two rows below
			if (x+2 < nbrRows){
				neighS2.add(new Pair(x+2, y));
				if (y-2 >= 0)
					neighS2.add(new Pair(x+2, y-2));
				if (y-1 >= 0)
					neighS2.add(new Pair(x+2, y-1));
				if (y+1 < nbrCols)
					neighS2.add(new Pair(x+2, y+1));
				if (y+2 < nbrCols)
					neighS2.add(new Pair(x+2, y+2));
			}
		}

	}

	/**
	 * @return the current x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the current y value
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns EAST, SOUTH, WEST or NORTH as heading
	 * 
	 * @return the current heading
	 */
	public int getHeading() {
		return heading;
	}

	/**
	 * returns the number of step away neighbours
	 * 
	 * @param step
	 *            - the step: 1 or 2
	 * @return the number of step away neighbours
	 */
	public int getNbrNeighbours(int step) {
		if (step == 1) {
			return neighS1.size();
		} else {
			return neighS2.size();
		}
	}

	/**
	 * Returns a list of neighbours the number step away
	 * 
	 * @param step
	 *            - the step: 1 or 2
	 * @return a list of neighbouring states
	 */
	public Reading[] getNeighbours(int step) {

		if (step == 1) {
			int size = neighS1.size();
			Reading[] ret = new Reading[size];
			Pair p;
			for (int i = 0; i < size; i++) {
				p = neighS1.get(i);
				ret[i] = new Reading(p.x,p.y,nbrRows,nbrCols);
			}

			return ret;
		} else {
			int size = neighS2.size();
			Reading[] ret = new Reading[size];
			Pair p;
			for (int i = 0; i < size; i++) {
				p = neighS2.get(i);
				ret[i] = new Reading(p.x,p.y,nbrRows,nbrCols);
			}

			return ret;
		}
	}

	/**
	 * Calculates which headings is allowed and returns a list of the headings
	 * in order EAST, SOUTH, WEST or NORTH.
	 * 
	 * Assumes the current position is not outside of the grid.
	 * 
	 * @return a list containing the heading when allowed and -1 otherwise (the
	 *         order in the list are EAST, SOUTH, WEST or NORTH)
	 */
	public int[] allowedHeadings() {
		int[] head = new int[4];

		if (x == 0) {
			head[SOUTH] = SOUTH;
			head[NORTH] = -1;
		} else if (x == nbrRows - 1) {
			head[SOUTH] = -1;
			head[NORTH] = NORTH;
		} else {
			head[SOUTH] = SOUTH;
			head[NORTH] = NORTH;
		}

		if (y == 0) {
			head[EAST] = EAST;
			head[WEST] = -1;
		} else if (y == nbrCols - 1) {
			head[EAST] = -1;
			head[WEST] = WEST;
		} else {
			head[EAST] = EAST;
			head[WEST] = WEST;
		}

		return head;
	}

	/**
	 * Update the state for taking a step in the direction head if the direction
	 * is allowed (no walls in the way)
	 * 
	 * @param head
	 *            - the direction the step will be taken in (assumes EAST,
	 *            SOUTH, WEST or NORTH)
	 * @return true if updated state (step taken), false if state could not be
	 *         updated
	 */
	public boolean updateState(int head) {
		int[] check = allowedHeadings();

		if (check[head] == -1) {
			return false;
		}

		switch (head) {
		case EAST:
			y += 1;
			break;
		case SOUTH:
			x += 1;
			break;
		case WEST:
			y -= 1;
			break;
		case NORTH:
			x -= 1;
			break;
		default:
			return false; // not needed since assumed one of values above
		}

		heading = head;
		neighS1.clear();
		neighS2.clear();
		setNeighbours(1);
		setNeighbours(2);
		
		return true;
	}

	/**
	 * Checks if it is allowed to move from this state to the state to.
	 * 
	 * @param to
	 *            - the state to check if allowed to move to
	 * @return true if allowed to move from here to s
	 */
	public boolean allowedMove(State to) {
		if ((x + 1 == to.x && y == to.y && to.heading == SOUTH)
				|| (x - 1 == to.x && y == to.y && to.heading == NORTH)) {
			return true;
		}
		if ((x == to.x && y + 1 == to.y && to.heading == EAST) || (x == to.x && y - 1 == to.y && to.heading == WEST)) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if there is a wall in front of the state (direction heading)
	 * 
	 * @return true if the end of the grid is to the heading of the coordinates
	 *         of the state
	 */
	public boolean faceWall() {
		if (x == 0 && heading == State.NORTH) {
			return true;
		}
		if (x == nbrRows - 1 && heading == State.SOUTH) {
			return true;
		}
		if (y == 0 && heading == State.WEST) {
			return true;
		}
		if (y == nbrCols - 1 && heading == State.EAST) {
			return true;
		}

		return false;
	}
	
	public boolean equals(Object obj){
		if (obj instanceof State) {
			State r = (State)obj;
			return r.getX()==x && r.getY()==y && r.getHeading() == heading;
		}
		return false;
	}

	public String toString() {
		String head;
		if (heading == EAST)
			head = "East";
		else if (heading == SOUTH)
			head = "South";
		else if (heading == WEST)
			head = "West";
		else
			head = "North";
		return "Coordinates: (" + x + ", " + y + "), heading: " + head;
	}

	/**
	 * Keeps hold of a coordinate in the grid.
	 * Is used for the neighbours.
	 * 
	 * @author elt13hli
	 *
	 */
	private class Pair {
		int x, y;
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
