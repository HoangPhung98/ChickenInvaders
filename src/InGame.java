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
	private long time;
	private final int NUMBER_OF_DUCKS = 1;
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
		drawCross(xCross, yCross);
		
		createMatrix();
		
		run();
		
	} 
	
	public void drawBackGround() {
		
		StdDraw.picture(gameFrame.getHalfWidth(), gameFrame.getHalfHeight(), "background.jpg", GameFrame.WIDTH, GameFrame.HEIGHT);
//		StdDraw.picture(0, -(GameFrame.HALF_HEIGHT-85), "grass.png", GameFrame.WIDTH, 169.6493);
		
	
	}
	
	public void drawCross(double x, double y){
		
		drawBackGround();
		StdDraw.picture(x, y, "crosshair.png",crossDegree);
		
	}
	
	private void drawDuck(){
		
		StdDraw.setPenColor(StdDraw.BLACK);
		for(int i =0 ; i<NUMBER_OF_DUCKS; i++){
			if(!flyingDucks[i].isGone)StdDraw.filledCircle(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate, 20);
		}
		
	}
	
		private  Point getCoordinateOfDuck(){
		
			int y=0;
			int x = StdRandom.uniform(0, gameFrame.getWidth());
		
			return new Point(x, y);
		
		}
	private  void addDuck(int i){
		flyingDucks[i].yCoordinate = 0;
		flyingDucks[i].xCoordinate = getCoordinateOfDuck().x;
		setRamdomDirection(i);
		
		
	}
	
	
	@Override
	public void run() {
		time = System.currentTimeMillis();
		for(int i=0; i<NUMBER_OF_DUCKS; i++){
			addDuck(i);
	
		}

		while(true){
			
			drawCross(StdDraw.mouseX(), StdDraw.mouseY());
			setNewXYCross();
			
			if(checkCanMove(time)){
				for(int i=0; i<NUMBER_OF_DUCKS; i++)updateDuckPosition(i);
				time= System.currentTimeMillis();
			}
			drawDuck();
			


			try {
				thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	private void updateDuckPosition(int i){
		flyingDucks[i].xCoordinate += flyingDucks[i].xdirection;
		flyingDucks[i].yCoordinate += flyingDucks[i].ydirection;
		checkHitWall(i);
	}
	private  void setRamdomDirection(int i){
		if(StdRandom.uniform(0,2)==0){
			flyingDucks[i].xdirection=-50;
		}else flyingDucks[i].xdirection = 50;
		if(StdRandom.uniform(0,2)==0){
			flyingDucks[i].ydirection = -50;
		}else flyingDucks[i].ydirection = 50;
	}
	private void checkHitWall(int i){
		// if the duck hit the wall 4 times then it can fly away
		if(!checkHit4Times(flyingDucks[i])){
			if(flyingDucks[i].yCoordinate+flyingDucks[i].ydirection-20 > gameFrame.getHeight())flyingDucks[i].ydirection *=-1;
//			flyingDucks[i].isGone = true;
		}
		
		//the duck is turn back if hit the wall
		if(flyingDucks[i].xCoordinate+flyingDucks[i].xdirection-20 > gameFrame.getWidth() || flyingDucks[i].xCoordinate+flyingDucks[i].xdirection-20 < 0) {
			flyingDucks[i].xdirection *=-1;
			flyingDucks[i].hitWallTimes ++;
		}
		if( flyingDucks[i].yCoordinate+flyingDucks[i].ydirection-20 < 0) {
			flyingDucks[i].ydirection *=-1;
			flyingDucks[i].hitWallTimes ++;
		}
	}
	private boolean checkHit4Times(Ducks duck){
		if(duck.hitWallTimes>4) return true;
		else return false;
	}
	private boolean checkCanMove(long time){
		if(System.currentTimeMillis() - time > valocity) return true;
		return false;
	}
	private boolean compareXY(){
		if(StdDraw.mouseX()!= xCross || StdDraw.mouseY()!= yCross ) return false;
		return true;
	}
	private void setNewXYCross(){
		this.xCross = StdDraw.mouseX();
		this.yCross = StdDraw.mouseY();
	}
	private void createMatrix(){
		flyingDucks = new Ducks[3];
		for(int i=0; i<NUMBER_OF_DUCKS; i++){
			flyingDucks[i] = new Ducks();
			flyingDucks[i].isAlive = true;
			flyingDucks[i].isGone = false;
			flyingDucks[i].hitWallTimes	=0;
		}
	}
}
