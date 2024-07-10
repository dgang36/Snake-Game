import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.Random;
import java.util.Random.*;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener { //JPanel allows for organizing and adjusting components. Used to group layouts of buttons, text fields, and labels.
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75; //the higher the delay, the faster the snake moves
	final int x[] = new int[GAME_UNITS]; //x coordinates for the snake body
	final int y[] = new int[GAME_UNITS]; //y coordinates for the snake body
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	int highscore = 0;
	char direction = 'R'; //the snake starts off going right
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	//Creates an apple and starts a timer
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	public void paintComponent(Graphics g) { //paintComponent is called automatically when the game panel is created
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if(running) {
			//grid lines
			/*
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) { //can replace SCREEN_HEIGHT with SCREEN_WIDTH
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); //i*UNIT_SIZE draws lines according to the spacing of the units
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); //draws the horizontal lines
			}*/
			
			//drawing the apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			/* drawing the snake
			 * snake starts top left since x and y will be 0
			 */
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) { //head of the snake
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else { //body of the snake
					g.setColor(new Color(45,180,0)); //RGB values for a shade of green different than the head. Allows for easy identification of the head when the body is very long.
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			//Score
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free", Font.PLAIN, 25));
			FontMetrics metrics1 = getFontMetrics(g.getFont()); //used to help adjust the positioning of the words in accordance with the string size
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/5, g.getFont().getSize());
			//Highscore
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free", Font.PLAIN, 25));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("Highscore: " + highscore, ((SCREEN_WIDTH - metrics2.stringWidth("Highscore: " + highscore))-40), g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; /*divide screen width by unit size to get 
																		a range that represents the number of columns. Then multiply that 
																		by unit size to get the actual position of the apple*/		
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		//moves the body to follow the head which follows the direction
		for(int i = bodyParts; i>0; i--){ 
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		//moves the head
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;//changes direction of the head only, then the for loop makes the body follow the head 
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		}
		//loops back to the start of the move method for the body to follow the head
	}
	public void checkApple() {
		if((x[0] == appleX) && y[0] == appleY) { //if apple is eaten
			bodyParts++;
			applesEaten++;
			if(highscore < applesEaten) {
				highscore++;
			}
			newApple();
		}
	}
	/**
	 * !!! Need to fix the border collisions. Some of the time the snake can go past
	 * 	   the border and survive if it turns back fast enough. 
	 *     Turns out that I needed to subtract the unit size from the width and height when checking
	 *     collisions since I need to check if the right side and bottom side of the unit pass 
	 *     the border.
	 */
	public void checkCollisions() {
		//checks if the head collides with any of the snake's body parts
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//checks if head collides with the borders
		if(x[0] < 0) { //left border
			running = false;
		}
		if(x[0] > SCREEN_WIDTH - UNIT_SIZE) { //right border
			/** Need to subract unit size to check for the right side of the unit 
			 *  touching the border instead of the left
			 */
			running = false;
		}
		if(y[0] < 0) { //top border
			running = false;
		}
		if(y[0] > SCREEN_HEIGHT - UNIT_SIZE) { //bottom border
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free", Font.BOLD, 25));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/5, g.getFont().getSize());
		//Game over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		//"Y or N?" text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free", Font.BOLD, 55));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Go again? Press Y or N", (SCREEN_WIDTH - metrics3.stringWidth("Go again? Press Y or N"))/2, g.getFont().getSize()*8);
		//Highscore
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free", Font.BOLD, 25));
		FontMetrics metrics4 = getFontMetrics(g.getFont());
		g.drawString("Highscore: " + highscore, ((SCREEN_WIDTH - metrics4.stringWidth("Highscore: " + highscore))-40), g.getFont().getSize());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT: //left arrow key
				if(direction != 'R') { //The snake cannot go back on itself. So it can only go left when the direction is not right
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT: //right arrow key
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP: //up arrow key
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN: //down arrow key
				if(direction != 'U') { 
					direction = 'D';
				}
				break;
			case KeyEvent.VK_A: //A arrow key
				if(direction != 'R') { //The snake cannot go back on itself. So it can only go left when the direction is not right
					direction = 'L';
				}
				break;
			case KeyEvent.VK_D: //D arrow key
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_W: //W arrow key
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_S: //S arrow key
				if(direction != 'U') { 
					direction = 'D';
				}
				break;
			case KeyEvent.VK_Y: //letter Y key
				if(!running) {
					restart();
				}
				break;
			case KeyEvent.VK_N: //letter N key
				if(!running) {
					System.exit(0);
				}
			}
			
		}
	}
	private void restart() {
		direction = 'R';
		bodyParts = 6;
		applesEaten = 0;
		running = true;
		
		//x[0] = 0; //this puts the head back in the starting position. 
		//y[0] = 0;
		/**Although there are traces of the old snake's body in its old position that are still moving when I restart.
		   So if I get game over in the top left, I cannot play again since there will be instant collision. 
		   I need to move the position of the entire body not just the head*/
		for(int i = bodyParts; i >= 0; i--) {
			x[i] = 0;
			y[i] = 0;
		}
		repaint();
		startGame();
		
	}

}
