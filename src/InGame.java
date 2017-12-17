import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JPanel;


public class InGame  implements Runnable{
	
	 GameFrame gameFrame;
	Thread thread, threadMouse;
	static double xCross, yCross;
	double crossDegree = 0;
	final int valocity = 100;
	final int duckSize = 65;
	final int duckPading = 32;
	private long time;
	private final int NUMBER_OF_DUCKS = 5;
	private final long TIME_BETWEEN_SHOTS = 200;
	private long shotTime = 0;
	Ducks [] flyingDucks;
	
	

	Point point;
	public InGame() {
		thread = new Thread(this);
		thread.start();
		
		gameFrame = new GameFrame();
		drawBackGround();

		//vẽ tâm ban đầu
		xCross = StdDraw.mouseX(); 
		yCross = StdDraw.mouseY();
		drawCross();
		createMatrix();
		run();
		
	} 
	
	public void drawBackGround() {
		StdDraw.picture(gameFrame.getHalfWidth(), gameFrame.getHalfHeight(), "background.jpg", GameFrame.WIDTH, GameFrame.HEIGHT);
	}
	
	public void drawCross(){
		drawBackGround();
		StdDraw.picture(StdDraw.mouseX(), StdDraw.mouseY(), "crosshair.png",crossDegree);
	}
	
	private void drawDuck(){
		for(int i =0 ; i<NUMBER_OF_DUCKS; i++){
			if(flyingDucks[i].isAlive){
				drawAliveDuck(i);
			}else{
				drawDeadDuck(i);
			}
		}
		StdDraw.picture(GameFrame.HALF_WIDTH, 85, "grass.png", GameFrame.WIDTH, 169.6493);
		StdDraw.show(10);
	}
	private void drawDeadDuck(int i){
		if(System.currentTimeMillis() - flyingDucks[i].time <= Ducks.timeStayOnAir){
			StdDraw.picture(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate, flyingDucks[i].duckDeadAnimation[0], duckSize, duckSize);	
		}else{
			flyingDucks[i].setDuckFallDown();
			StdDraw.picture(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate, flyingDucks[i].duckDeadAnimation[flyingDucks[i].duckDeadAnimationStatus], duckSize, duckSize);	
			System.out.println(flyingDucks[i].duckDeadAnimationStatus+"---");
		}
	}
	private void drawAliveDuck(int i){
		if(!flyingDucks[i].isGone)StdDraw.picture(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate, flyingDucks[i].getDuck_R_or_Left_Animation(),duckSize,duckSize,flyingDucks[i].degree);
	}
	
	@Override
	public void run() {
		time = System.currentTimeMillis();
		for(int i=0; i<NUMBER_OF_DUCKS; i++){
			flyingDucks[i].addDuck();
		}
		
		while(true){
			drawCross();
			if(checkCanMove(time)){
				for(int i=0; i<NUMBER_OF_DUCKS; i++)flyingDucks[i].updateDuckPosition();
				time= System.currentTimeMillis();
			}

			drawDuck();
			checkShotDuck();
			try {
				thread.sleep(20);
			} catch (InterruptedException e) {e.printStackTrace();}
			
		}
		
	}
	
	
	/////////////////////////////////////////////
	private void checkShotDuck(){
		if(System.currentTimeMillis() - shotTime > TIME_BETWEEN_SHOTS ){
		if(StdDraw.isMousePressed()){
			StdAudio.play("shot.wav");
			for(int i=0; i< NUMBER_OF_DUCKS; i++){
				if ( StdDraw.mouseX() < flyingDucks[i].xCoordinate + duckPading && StdDraw.mouseX() > flyingDucks[i].xCoordinate - duckPading
						&& StdDraw.mouseY() < flyingDucks[i].yCoordinate + duckPading && StdDraw.mouseY() > flyingDucks[i].yCoordinate - duckPading	) {
					System.out.println("shoted");
					flyingDucks[i].setDuckDead();
				}
			}
		}
		shotTime = System.currentTimeMillis();
		}
		
	}

	private boolean checkCanMove(long time){
		if(System.currentTimeMillis() - time > valocity) return true;
		return false;
	}

	private void createMatrix(){
		flyingDucks = new Ducks[NUMBER_OF_DUCKS];
		for(int i=0; i<NUMBER_OF_DUCKS; i++){
			flyingDucks[i] = new Ducks();
			flyingDucks[i].isAlive = true;
			flyingDucks[i].isGone = false;
			flyingDucks[i].hitWallTimes	=0;
		}
	}
}
