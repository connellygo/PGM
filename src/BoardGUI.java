import javax.swing.JFrame;

//Class to display the window.
public class BoardGUI extends JFrame{	
	public static void main(String[] args) {
		
		//Create new Gui Object, Game Object, and add the game to the GUI.
		BoardGUI frame = new BoardGUI();
		Board game = new Board();
		frame.add(game);
		
		//Make window 400 x 420, stop program from running when the window is closed,
		//and make the window visible.
		frame.setSize(400,420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
