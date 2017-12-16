import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.MemoryImageSource;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;


public class GameFrame  {
	
	final static int HALF_WIDTH = 700;
	final static int HALF_HEIGHT = 350;
	final static int HEIGHT = HALF_HEIGHT*2;
	final static int WIDTH = HALF_WIDTH*2;
	
	static InGame inGame;
	private static boolean isPlaying = false;

	public GameFrame(){
		


	}
	
	public static void main(String[] args) {
		
		GameFrame gameFrame = new GameFrame();
		setWindow();

		waitForStartGame();
		
	}
	
	private static void waitForStartGame(){
		while(!isPlaying){
			if(StdDraw.isKeyPressed(0)||StdDraw.isMousePressed()) isPlaying = true;
		}
		inGame = new InGame();
	}
	
	public static int getHeight() {
		return HEIGHT;
	}

	public static int getWidth() {
		return WIDTH;
	}

	
	



}
