import java.awt.Color;
import java.awt.Graphics;

public class Player {
	private int row, col;
	static final int cellLength = 10;
	
	//Put the player at the start.
	public Player(){
		row = 1;
		col = 1;
	}
	
	//Draw player.
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillOval(col * cellLength, row * cellLength, cellLength, cellLength);
	}

	//Move player.
	public void move(int r, int c) {
		row += r;
		col += c;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
}
