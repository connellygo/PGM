import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

//Object that creates the maze and controls the game.
public class Board extends JPanel implements KeyListener{
	
	//Number of rows and columns in the maze.
	static final int row = 40;
	static final int col = 40;
	
	//How many cells around the player that are visible.
	private int SIGHT_RANGE = 3;
	
	//2D array of Cell objects that contain the layout of the maze.
	private static Cell[][] board;
	
	//Object that contains information about the player.
	private Player player;
	
	//Necessary method for graphics library.
	public void addNotify() {
        super.addNotify();
        requestFocus();
    }
	
	//Default Constructor: Generates maze and adds player.
	public Board(){
		
		//Creates Cell array and Player object
		board = new Cell[row][col];
		player = new Player();
		
		//Enables the program to have key inputs.
		addKeyListener(this);
		
		//Set all cells to "Free". Meaning that a player can move there.
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				board[i][j] = new Cell(i,j,"Free");
			}
		}
		
		//Set the bordering cells to "Wall". Meaning that a player can't move there.
		for(int i = 0; i < row; i++){
			board[i][0].setType("Wall");
			board[i][row-1].setType("Wall");
		}
		for(int i = 0; i < col; i++){
			board[0][i].setType("Wall");
			board[col-1][i].setType("Wall");
		}
		
		//Cell (1,1) is the start of the maze.
		board[1][1].setType("Start");
		
		//Bottom Right cell is the end of the maze.
		board[row-2][col-2].setType("Finish");

		//Run the recursiveDivision algorithm to procedurally generate the maze.
		recursiveDivision(1, 1, row-2, col-2, "vertical", 0);
	}
	
	//Procedural maze generation algorithm. It works by recursively dividing the room in half.
	//This creates 2 rooms and the algorithm is called on these two rooms until it cannot be divided anymore.
	//Inputs: the top-left row, top-left column, bottom-right row, bottom-right column, a string to represent whether
	// the room should be divided horizontally or vertically, and a code to say if a wall couldn't be placed.
	private void recursiveDivision(int tlRow, int tlCol, int brRow, int brCol, String s, int code) {
		
		//creating variables to hold the room size.
		int colDif = Math.abs(brCol - tlCol);
		int rowDif = Math.abs(brRow - tlRow);
		
		//if a vertical wall is being placed
		Random r = new Random();
		if(s.equals("vertical")){
			//List of valid possible columns to place a wall.
			ArrayList<Integer> possibleColumns = new ArrayList<Integer>();
			for(int i = tlCol+1; i < brCol; i++){
				//If the wall doesn't block an entrance to the room, add it to the possibilities.
				if(board[tlRow-1][i].getType().equals("Wall") && board[brRow+1][i].getType().equals("Wall")) 
					possibleColumns.add(i);
			}
			
			//If there is an appropriate column to put the wall, choose a random column from the list and fill that row.
			if(possibleColumns.size() != 0){
				int randCol = possibleColumns.get(r.nextInt(possibleColumns.size()));
				for(int i = tlRow; i <= brRow; i++){
					board[i][randCol].setType("Wall");
				}
			
				//Selecting a random cell in previous wall that will be free to add an entry between the rooms.
				ArrayList<Integer> possibleRows = new ArrayList<Integer>();
				for(int i = tlRow; i <= brRow; i++){
					possibleRows.add(i);
				}
				int randRow = possibleRows.get(r.nextInt(possibleRows.size()));
				board[randRow][randCol].setType("Free");
			
				//Calling the method again setting a horizontal row 
				recursiveDivision(tlRow, randCol + 1, brRow, brCol, "horizontal", 0);
				recursiveDivision(tlRow, tlCol, brRow, randCol-1, "horizontal", 0);
			} 
			//If there is no place to put a wall, try calling the algorithm horizontally.
			else if(code == 0) recursiveDivision(tlRow, tlCol, brRow, brCol, "horizontal", 1);
			//A wall could not be placed horizontally or vertically. This is the base case.
			else return;
			
		}
		//if a horizontal wall is being placed and there is an appropriate place to put a wall.
		else if(rowDif > 1 && colDif > 0){
			
			//List of valid possible columns to place a wall.
			ArrayList<Integer> possibleRows = new ArrayList<Integer>();
			for(int i = tlRow+1; i < brRow; i++){
				//If the wall doesn't block an entrance to the room, add it to the possibilities.
				if(board[i][tlCol-1].getType().equals("Wall") && board[i][brCol+1].getType().equals("Wall")) 
					possibleRows.add(i);
			}
			
			//If there is an appropriate column to put the wall, choose a random column from the list and fill that row.
			if(possibleRows.size() != 0){
				int randRow = possibleRows.get(r.nextInt(possibleRows.size()));
				for(int i = tlCol; i <= brCol; i++){
					board[randRow][i].setType("Wall");
				}
			
				//Selecting a random cell in previous wall that will be free to add an entry between the rooms.
				ArrayList<Integer> possibleColumns = new ArrayList<Integer>();
				for(int i = tlCol; i <= brCol; i++){
					possibleColumns.add(i);
				}
				int randCol = possibleColumns.get(r.nextInt(possibleColumns.size()));
				board[randRow][randCol].setType("Free");			
			
				//Calling the function again setting a horizontal row 
				recursiveDivision(tlRow, tlCol, randRow - 1, brCol, "vertical", 0);
				recursiveDivision(randRow+1, tlCol, brRow, brCol, "vertical", 0);
			} 
			//If there is no place to put a wall, try calling the algorithm horizontally.
			else if(code == 0) recursiveDivision(tlRow, tlCol, brRow, brCol, "vertical", 1);
			//A wall could not be placed horizontally or vertically. This is the base case.
			else return;
		}
		//The algorithm can't divide the room anymore.
		else return;
	}

	//Draw maze and player on window.
	public void paintComponent(Graphics g)
	{
	  super.paintComponent(g);
	  
	  //Draw cells of maze.
	  for (int i = 0; i < row; i++){
		  for (int j = 0; j < col; j++){
			  
			  //If the cell is within seeing distance of the player, display the cell.
			  //Otherwise, display a black square.
			  int dif = Math.abs(player.getRow() - i) + Math.abs(player.getCol() - j);
			  if(dif == SIGHT_RANGE) board[i][j].draw(g, 2);
			  else if(dif < SIGHT_RANGE) board[i][j].draw(g, 1);
			  else board[i][j].draw(g,0);
		  }
	  }
	  //Draw player.
	  player.draw(g);
	}

	//Set the type of a cell in the maze.
	public void setCellType(int row, int col, String type) {
		board[row][col].setType(type);
	}


	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	//Controls for the player.
	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		//If the player pressed up arrow, move player up.
		case KeyEvent.VK_UP:
			if(checkFree(player.getRow() - 1, player.getCol())){
				player.move(-1,0);
				repaint();
			}
			break;
			
		//If the player pressed down arrow, move player down.
		case KeyEvent.VK_DOWN:
			if(checkFree(player.getRow() + 1, player.getCol())){
				player.move(1,0);
				repaint();
			}
			break;
			
		//If the player pressed up right, move player right.
		case KeyEvent.VK_RIGHT:
			if(checkFree(player.getRow(), player.getCol() + 1)){
				player.move(0,1);
				repaint();
			}
			break;
			
		//If the player pressed left arrow, move player left.
		case KeyEvent.VK_LEFT:
			if(checkFree(player.getRow(), player.getCol() - 1)){
				player.move(0,-1);
				repaint();
			}
			break;
		
		//If the player feels like cheating and presses E, reveal the maze to the player.
		case KeyEvent.VK_E:
			SIGHT_RANGE = 100;
			repaint();
		default:
			break;
		}		
		
		//If the player reaches the finish, give him a message.
		if(board[player.getCol()][player.getRow()].getType().equals("Finish") && e.getKeyCode() != KeyEvent.VK_ENTER) {
			JOptionPane.showMessageDialog(this, "Finished!");
		}

	}

	//Check whether the player can walk to this cell.
	private boolean checkFree(int row, int col) {
		if(board[row][col].getType().equals("Wall")) return false;
		else return true;
	}

}
