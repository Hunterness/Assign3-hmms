package model;

public class State {
	public static final int EAST = 0;
	public static final int SOUTH = 1;
	public static final int WEST = 2;
	public static final int NORTH = 3;
	
	private int rowCoord, colCoord, heading;
	
	public State(int row, int col, int head){
		rowCoord = row;
		colCoord = col;
		heading = head;
	}
	
	public int getX(){
		return rowCoord;
	}
	
	public int getY(){
		return colCoord;
	}
	
	/**
	 * Returns EAST, SOUTH, WEST or NORTH as heading
	 * @return the current heading
	 */
	public int getHeading(){
		return heading;
	}
	
	/**
	 * Update the state after taking a step in the direction head
	 * 
	 * Does not check for walls!
	 * 
	 * @param head - the direction the step were taken in (EAST,SOUTH,WEST,NORTH)
	 */
	public boolean updateState(int head){
		switch (head){
			case EAST:
				colCoord += 1;
				break;
			case SOUTH:
				rowCoord += 1;
				break;
			case WEST:
				colCoord -= 1;
				break;
			case NORTH:
				rowCoord -= 1;
				break;
			default:
				return false;
		}
		
		heading = head;
		return true;
	}
	
}
