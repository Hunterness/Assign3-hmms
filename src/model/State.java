package model;

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
	
	public State(int row, int col, int head, int nbrRows, int nbrCols){
		x = row;
		y = col;
		heading = head;
		this.nbrRows = nbrRows;
		this.nbrCols = nbrCols;
	}
	
	/**
	 * @return the current x value
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * @return the current y value
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Returns EAST, SOUTH, WEST or NORTH as heading
	 * @return the current heading
	 */
	public int getHeading(){
		return heading;
	}
	
	/**
	 * Calculates which headings is allowed and returns a list of the headings 
	 * in order EAST, SOUTH, WEST or NORTH.
	 * 
	 * Assumes the current position is not outside of the grid.
	 * 
	 * @return a list containing the heading when allowed and -1 otherwise 
	 * 			(the order in the list are EAST, SOUTH, WEST or NORTH)
	 */
	public int[] allowedHeadings(){
		int[] head = new int[4];
		
		if (x == 0){
			head[SOUTH] = SOUTH;
			head[NORTH] = -1;
		} else if (x == nbrRows-1) {
			head[SOUTH] = -1;
			head[NORTH] = NORTH;
		} else {
			head[SOUTH] = SOUTH;
			head[NORTH] = NORTH;
		}
		
		if (y == 0){
			head[EAST] = EAST;
			head[WEST] = -1;
		} else if (y == nbrCols-1) {
			head[EAST] = -1;
			head[WEST] = WEST;
		} else {
			head[EAST] = EAST;
			head[WEST] = WEST;
		}
		
		return head;
	}
	
	/**
	 * Update the state for taking a step in the direction head 
	 * if the direction is allowed (no walls in the way)
	 * 
	 * @param head - the direction the step will be taken in (assumes EAST, SOUTH, WEST or NORTH)
	 * @return true if updated state (step taken), false if state could not be updated
	 */
	public boolean updateState(int head){
		int[] check = allowedHeadings();
		
		if(check[head] == -1){
			return false;
		}
		
		switch (head){
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
		return true;
	}
	
}
