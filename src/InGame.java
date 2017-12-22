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
	private final long TIME_BETWEEN_SHOTS = 200;
	private final long TIME_OF_A_LEVEL = 20000;
	private final int NUMBER_OF_DUCKS = 5;
	private final long TIME_BETWEEN_CONSECUTIVE_SHOT = 2250;

	static Thread thread, threadMouse;
	static double xCross, yCross;
	double crossDegree = 0;
	static int valocity = 150;
	final int duckSize = 65;
	final int duckPading = 32;
	private long time;
	private static long timeStartThisLevel;
	private long timeRunningStatus;
	private long timeOfPreviousShot = 0;
	private int consecutiveShotStatus = -1;
	private static int round;
	private int deadDuckNumber;
	private long shotTime = 0;
	Ducks [] flyingDucks;
	
	String[] killSound;
	
	Gun gun;
	

	Point point;
	public InGame() {
		thread = new Thread(this);
		thread.start();
		
		gameFrame = new GameFrame();
		GameFrame.started = true;
		drawBackGround();
		
		gun = new Gun();
		//vẽ tâm ban đầu
		xCross = StdDraw.mouseX(); 
		yCross = StdDraw.mouseY();
		drawCross();
		createMatrix();
		deadDuckNumber = 0;
		timeRunningStatus = TIME_OF_A_LEVEL;
		round = 1;
		
		setKillSoundArray();
		
		run();
		
	} 
	private void setKillSoundArray(){
		killSound = new String[5];
		killSound[0] = "killSound0.wav";
		killSound[1] = "killSound1.wav";
		killSound[2] = "killSound2.wav";
		killSound[3] = "killSound3.wav";
		killSound[4] = "killSound4.wav";
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
				flyingDucks[i].drawSplashBlood();
			}
		}
		StdDraw.picture(GameFrame.HALF_WIDTH, 85, "grass.png", GameFrame.WIDTH, 169.6493);
	}
	private void drawDeadDuck(int i){
		if(System.currentTimeMillis() - flyingDucks[i].time <= Ducks.timeStayOnAir){
			StdDraw.picture(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate, flyingDucks[i].duckDeadAnimation[0], duckSize, duckSize);	
		}else{
			flyingDucks[i].setDuckFallDown();
			StdDraw.picture(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate, flyingDucks[i].duckDeadAnimation[flyingDucks[i].duckDeadAnimationStatus], duckSize, duckSize);	
		}
	}
	private void drawAliveDuck(int i){
		/*if(!flyingDucks[i].isGone)*/StdDraw.picture(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate, flyingDucks[i].getDuck_R_or_Left_Animation(),duckSize,duckSize,flyingDucks[i].degree);
	}
	private void drawTimeRunning(){
		updateTimeRunning();
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setPenRadius(0.04);
		StdDraw.line(0, gameFrame.HEIGHT-22, getTimeRunningLength(), gameFrame.HEIGHT -22);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.setPenRadius(0.015);
		StdDraw.line(0, gameFrame.HEIGHT-22, getTimeRunningLength(), gameFrame.HEIGHT -22);
		StdDraw.picture(getTimeRunningLength(), gameFrame.HEIGHT-22, "timeRunningDuck.png", 100, 100);
	}
	
	private void drawNextRound(){
		StdDraw.picture(gameFrame.HALF_WIDTH, gameFrame.HALF_HEIGHT, "transparent4DucksWallpaper.png",gameFrame.WIDTH,gameFrame.HEIGHT);
		StdDraw.setPenColor(119,10,10);
		StdDraw.text(350, 450, "Round ");
		StdDraw.text(350, 320, round+"");
		StdDraw.show(1500);
		
	}
	private void drawGameOver(){
		StdDraw.picture(gameFrame.HALF_WIDTH, gameFrame.HALF_HEIGHT, "gameOver.jpg");
		StdDraw.show(2500);
	}
	private void drawReplayGameWallpaper(){
		StdDraw.picture(GameFrame.HALF_WIDTH, GameFrame.HALF_HEIGHT, "replayWallpaper.png", GameFrame.WIDTH, GameFrame.HEIGHT);
		StdDraw.show(10);

	}
	@Override
	public void run() {
		time = System.currentTimeMillis();
		timeStartThisLevel = System.currentTimeMillis();
		
		for(int i=0; i<NUMBER_OF_DUCKS; i++){
			flyingDucks[i].addDuck();
		}
		
		while(true){
			if(!checkGameOver()){
				drawCross();
				if(checkCanMove(time)){
					for(int i=0; i<NUMBER_OF_DUCKS; i++)flyingDucks[i].updateDuckPosition();
					time= System.currentTimeMillis();
				}
				drawTimeRunning();
				checkShotDuck();
				drawDuck();
				gun.drawBullet();
				if(gun.canReloadAmo) gun.DrawReloadGunAmo();
				else gun.drawGunWithDegree();
				
				if(hasKillAllOfDuck()) {
					try {
						thread.sleep(1000);
					} catch (InterruptedException e) {e.printStackTrace();}
					updateNewRound();
					drawNextRound();
				}
				
				if(checkGameOver() == true){
					
					gameFrame.isPlaying = false;
					drawGameOver();
					drawReplayGameWallpaper();
					gameFrame.waitForStartGame();
					creatNewInGame();
					System.out.println("xxx");
				}
				StdDraw.show(4);
				try {
					thread.sleep(8);
				} catch (InterruptedException e) {e.printStackTrace();}
				
			}
		}
		
	}
	
	
	/////////////////////////////////////////////
	private void checkShotDuck(){
		if(System.currentTimeMillis() - shotTime > TIME_BETWEEN_SHOTS ){
		if(StdDraw.isMousePressed()){
			gun.drawExplosion();
			gun.bulletNumber-- ;
			gun.drawShotingLightRay();
			StdAudio.play("shot.wav");
			StdAudio.play("gunReload.wav");

			Gun.canReloadAmo = true;// draw gun reloading amo

			
			for(int i=0; i< NUMBER_OF_DUCKS; i++){
				if(flyingDucks[i].isAlive){
					if ( StdDraw.mouseX() < flyingDucks[i].xCoordinate + duckPading && StdDraw.mouseX() > flyingDucks[i].xCoordinate - duckPading
							&& StdDraw.mouseY() < flyingDucks[i].yCoordinate + duckPading && StdDraw.mouseY() > flyingDucks[i].yCoordinate - duckPading	) {
						flyingDucks[i].setDuckDead();
						deadDuckNumber ++;
						StdAudio.play("quak.wav");
						
						if(deadDuckNumber == 1) timeOfPreviousShot = System.currentTimeMillis();
						if(System.currentTimeMillis()-timeOfPreviousShot < TIME_BETWEEN_CONSECUTIVE_SHOT){
							consecutiveShotStatus ++;
							StdAudio.play(killSound[consecutiveShotStatus]);
							timeOfPreviousShot = System.currentTimeMillis();
						}else{
							consecutiveShotStatus = 0;
							StdAudio.play(killSound[0]);
							timeOfPreviousShot = System.currentTimeMillis();
						}
					}
				}
			}
		}
		shotTime = System.currentTimeMillis();
		}
		
	}
	private boolean checkGameOver(){
		if(timeRunningStatus < 0 || gun.bulletNumber == 0 ){
			return true;
		}else return false;
	
	}
	private void updateTimeRunning(){
		timeRunningStatus = TIME_OF_A_LEVEL - (System.currentTimeMillis() - timeStartThisLevel);
	}
	private int getTimeRunningLength(){
		return (int)(((double)gameFrame.WIDTH/(double)TIME_OF_A_LEVEL)*timeRunningStatus);
	}
	private void creatNewInGame(){
		round = 0;
		updateNewRound();
		valocity = 150;
	}
	
	private void updateNewRound(){
		for(int i=0; i<NUMBER_OF_DUCKS; i++){
			flyingDucks[i].addDuck();
		}
		updaterNewStatusOfDucks();
		timeStartThisLevel = System.currentTimeMillis();
		updateTimeRunning();
		round++;
		deadDuckNumber = 0;
		consecutiveShotStatus = -1;
		gun.bulletNumber = gun.BULLET_NUMBER;
		valocity -=(double)60/(double)round;
		System.out.println(valocity);
		
		
	}
	private boolean hasKillAllOfDuck(){
		if(deadDuckNumber == NUMBER_OF_DUCKS) return true;
		else return false;
	}
	private boolean checkCanMove(long time){
		if(System.currentTimeMillis() - time > valocity) return true;
		return false;
	}
	private void updaterNewStatusOfDucks(){
		for(int i=0; i< NUMBER_OF_DUCKS; i++){
		flyingDucks[i].isAlive = true;
//		flyingDucks[i].isGone = false;
//		flyingDucks[i].hitWallTimes	=0;
		}
	}
	private void createMatrix(){
		flyingDucks = new Ducks[NUMBER_OF_DUCKS];
		for(int i=0; i<NUMBER_OF_DUCKS; i++){
			flyingDucks[i] = new Ducks();
			flyingDucks[i].isAlive = true;
//			flyingDucks[i].isGone = false;
//			flyingDucks[i].hitWallTimes	=0;
		}
	}
}
