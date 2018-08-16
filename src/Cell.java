import java.awt.Color;
import java.awt.Graphics;

public class Cell {
	private int row;
	private int col;
	private String type;
	static final int cellLength = 10;
	
	//Default Constructor
	public Cell(){
		row = 0;
		col = 0;
		type = "Free";
	}
	
	//Constructor
	public Cell(int r, int c, String t){
		row = r;
		col = c;
		type = t;
	}
	public void setType(String s){
		type = s;
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return row;
	}
	public String getType() {
		return type;
	}
	
	//Draw cell. Code is for telling how far the cell is from the player.
	public void draw(Graphics g, int code) {
		if(code == 1){
			if(type.equals("Free")){
				g.setColor(Color.WHITE);
			} else if(type.equals("Wall")){
				g.setColor(Color.BLACK);
			} else if(type.equals("Start")){
				g.setColor(Color.YELLOW);
			} else if(type.equals("Finish")){
				g.setColor(Color.GREEN);
			}
		} else if(code == 2){
			if(type.equals("Wall")){
				g.setColor(Color.BLACK);
			} else g.setColor(Color.LIGHT_GRAY);
		}
		else g.setColor(Color.BLACK);
		g.fillRect(col*cellLength, row*cellLength, cellLength, cellLength);
		
	}
}
