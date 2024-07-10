import javax.swing.JFrame;

public class GameFrame extends JFrame{
	
	GameFrame(){
		
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false); //prevents the frame from being resized
		this.pack(); //packs everything snugly into the frame
		this.setVisible(true); //makes the frame appear on the screen
		this.setLocationRelativeTo(null); //puts the frame in the center of the screen
		
	}
}
