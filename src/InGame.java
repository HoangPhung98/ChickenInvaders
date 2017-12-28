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

public class InGame implements Runnable {

	GameFrame gameFrame;
	private final int NUMBER_OF_DUCKS = 5;
	private int numberOfDuckThisRound;
	private final long TIME_BETWEEN_SHOTS = 175;
	private final long TIME_OF_A_LEVEL = 30000;
	private final long TIME_BETWEEN_CONSECUTIVE_SHOT = 2250;
	final int duckSize = 65;
	final int duckPading = 32;
	static double xCross, yCross;
	static int valocity = 80;

	static Thread thread, threadMouse;

	
	//time
	private long time;
	private static long timeStartThisLevel;
	private long timeRunningStatus;
	private long timeOfPreviousShot = 0;
	//status
	private int totalDeadDuck;
	private int consecutiveShotStatus = -1;
	private static int round;
	private int deadDuckNumber;
	private long shotTime = 0;
	
	Ducks[] flyingDucks;
	String[] inGameBackground;
	String[] killSound;

	Gun gun;

	Point point;
	//===============================================================================
	public InGame() {
		thread = new Thread(this);
		thread.start();

		gameFrame = new GameFrame();
		GameFrame.started = true;

		initInGameBackground();
		setKillSoundArray();
		createMatrix();

		drawBackGround();
		gun = new Gun();

		// vẽ tâm ban đầu
		xCross = StdDraw.mouseX();
		yCross = StdDraw.mouseY();
		drawCross();

		deadDuckNumber = 0;
		totalDeadDuck = 0;
		numberOfDuckThisRound = NUMBER_OF_DUCKS;
		timeRunningStatus = TIME_OF_A_LEVEL;
		round = 1;

		run();

	}

	@Override
	public void run() {
		time = System.currentTimeMillis();
		timeStartThisLevel = System.currentTimeMillis();

		for (int i = 0; i < NUMBER_OF_DUCKS; i++) {
			flyingDucks[i].addDuck();
		}

		while (true) {
			if (!checkGameOver()) {
				drawCross();
				if (checkCanMove(time)) {
					for (int i = 0; i < numberOfDuckThisRound; i++)
						flyingDucks[i].updateDuckPosition();
					time = System.currentTimeMillis();
				}
				drawTimeRunning();
				processShotDuck();
				drawDuck();
				drawSandBag();
				gun.drawBullet();
				if (gun.canReloadAmo)
					gun.DrawReloadGunAmo();
				else
					gun.drawGunWithDegree();

				if (hasKillAllOfDuck()) {
					try {
						thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					updateNextRound();
					drawNextRound();
				}

				if (checkGameOver() == true) {

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
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	}

	/////////////////////////////////////////////

	// <Process Things ************
	private void processShotDuck() {
		if (System.currentTimeMillis() - shotTime > TIME_BETWEEN_SHOTS) {
			if (StdDraw.isMousePressed()) {
				setGunShotEffect();
				checkDuckIsShoted();
			}
			shotTime = System.currentTimeMillis();
		}

	}

	private void processConsecutiveShot() {
		if (deadDuckNumber == 1)
			timeOfPreviousShot = System.currentTimeMillis();
		if (System.currentTimeMillis() - timeOfPreviousShot < TIME_BETWEEN_CONSECUTIVE_SHOT) {
			consecutiveShotStatus++;
			if (consecutiveShotStatus < 4)
				StdAudio.play(killSound[consecutiveShotStatus]);
			else
				StdAudio.play(killSound[4]);
			timeOfPreviousShot = System.currentTimeMillis();
		} else {
			consecutiveShotStatus = 0;
			StdAudio.play(killSound[0]);
			timeOfPreviousShot = System.currentTimeMillis();
		}

	}

	private void checkDuckIsShoted() {
		for (int i = 0; i < numberOfDuckThisRound; i++) {
			if (flyingDucks[i].isAlive) {
				if (StdDraw.mouseX() < flyingDucks[i].xCoordinate + duckPading
						&& StdDraw.mouseX() > flyingDucks[i].xCoordinate - duckPading
						&& StdDraw.mouseY() < flyingDucks[i].yCoordinate + duckPading
						&& StdDraw.mouseY() > flyingDucks[i].yCoordinate - duckPading) {
					flyingDucks[i].setDuckDead();
					deadDuckNumber++;
					totalDeadDuck++;
					StdAudio.play("quak.wav");
					processConsecutiveShot();
				}
			}
		}
	}

	private void setGunShotEffect() {
		gun.drawExplosion();
		gun.bulletNumber--;
		gun.drawShotingLightRay();
		StdAudio.play("gunShot.wav");
		StdAudio.play("gunReload.wav");

		Gun.canReloadAmo = true;// draw gun reloading amo
	}
	// </Process Things

	// <Update Things>**********
	private void updateTimeRunning() {
		timeRunningStatus = TIME_OF_A_LEVEL - (System.currentTimeMillis() - timeStartThisLevel);
	}

	private void updateNextRound() {
		numberOfDuckThisRound = NUMBER_OF_DUCKS + (int) (round / 2);
		for (int i = 0; i < numberOfDuckThisRound; i++) {
			flyingDucks[i].addDuck();
		}
		updaterNewStatusOfDucks();
		timeStartThisLevel = System.currentTimeMillis();
		updateTimeRunning();
		round++;
		deadDuckNumber = 0;
		consecutiveShotStatus = -1;
		gun.bulletNumber = gun.BULLET_NUMBER + round;
		valocity -= (double) 28 / (double) round;
		System.out.println(valocity);

	}

	private void updaterNewStatusOfDucks() {
		for (int i = 0; i < numberOfDuckThisRound; i++) {
			flyingDucks[i].isAlive = true;

		}
	}

	private void creatNewInGame() {
		numberOfDuckThisRound = NUMBER_OF_DUCKS;
		round = 0;
		updateNextRound();
		valocity = 80;
	}

	private int getTimeRunningLength() {
		return (int) (((double) gameFrame.WIDTH / (double) TIME_OF_A_LEVEL) * timeRunningStatus);
	}

	// </Update Thing>

	// < Check Things > **************
	private boolean checkGameOver() {
		if (timeRunningStatus < 0 || gun.bulletNumber == 0) {
			return true;
		} else
			return false;

	}

	private boolean hasKillAllOfDuck() {
		if (deadDuckNumber == numberOfDuckThisRound)
			return true;
		else
			return false;
	}

	private boolean checkCanMove(long time) {
		if (System.currentTimeMillis() - time > valocity)
			return true;
		return false;
	}

	// </Check Thing >
	// <Draw things>
	public void drawBackGround() {
		StdDraw.picture(gameFrame.getHalfWidth(), gameFrame.getHalfHeight(), inGameBackground[round], GameFrame.WIDTH,GameFrame.HEIGHT);
	}
	private void drawSandBag(){
		StdDraw.picture(GameFrame.HALF_WIDTH, 85, "sandbag.png", GameFrame.HALF_WIDTH + 350, 300);
	}
	public void drawCross() {
		drawBackGround();
		StdDraw.picture(StdDraw.mouseX(), StdDraw.mouseY(), "gunCross2.png");
	}

	private void drawDuck() {
		for (int i = 0; i < numberOfDuckThisRound; i++) {
			if (flyingDucks[i].isAlive) {
				drawAliveDuck(i);
			} else {
				drawDeadDuck(i);
				flyingDucks[i].drawSplashBlood();
			}
		}
	}

	private void drawDeadDuck(int i) {
		if (System.currentTimeMillis() - flyingDucks[i].time <= Ducks.timeStayOnAir) {
			StdDraw.picture(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate, flyingDucks[i].duckDeadAnimation[0],
					duckSize, duckSize);
		} else {
			flyingDucks[i].setDuckFallDown();
			StdDraw.picture(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate,
					flyingDucks[i].duckDeadAnimation[flyingDucks[i].duckDeadAnimationStatus], duckSize, duckSize);
		}
	}

	private void drawAliveDuck(int i) {
		StdDraw.picture(flyingDucks[i].xCoordinate, flyingDucks[i].yCoordinate,
				flyingDucks[i].getDuck_R_or_Left_Animation(), duckSize, duckSize, flyingDucks[i].degree);
	}

	private void drawTimeRunning() {
		updateTimeRunning();
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setPenRadius(0.04);
		StdDraw.line(0, gameFrame.HEIGHT - 22, getTimeRunningLength(), gameFrame.HEIGHT - 22);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.setPenRadius(0.015);
		StdDraw.line(0, gameFrame.HEIGHT - 22, getTimeRunningLength(), gameFrame.HEIGHT - 22);
		StdDraw.picture(getTimeRunningLength(), gameFrame.HEIGHT - 22, "timeRunningDuck.png", 100, 100);
	}

	private void drawGameOver() {
		for(int i=0; i< GameFrame.HALF_WIDTH; i+=10){
			StdDraw.picture(i, gameFrame.HALF_HEIGHT, "gameOver.jpg");
			StdDraw.show(7);
		}
		for(int i=1; i<= totalDeadDuck; i++){
			gun.drawNumber(i,gameFrame.HALF_WIDTH,gameFrame.HALF_HEIGHT+200);
			StdDraw.show(100);
		}
		
			StdDraw.show(2500);
	}

	private void drawReplayGameWallpaper() {
		StdDraw.picture(GameFrame.HALF_WIDTH, GameFrame.HALF_HEIGHT, "replayWallpaper.png", GameFrame.WIDTH,
				GameFrame.HEIGHT);
		StdDraw.show(10);

	}

	private void drawNextRound() {
		StdDraw.picture(gameFrame.HALF_WIDTH, gameFrame.HALF_HEIGHT, "transparent4DucksWallpaper.png", gameFrame.WIDTH,
				gameFrame.HEIGHT);
		StdDraw.setPenColor(119, 10, 10);
		StdDraw.text(350, 450, "Round ");
		StdDraw.text(350, 320, round + "");
		StdDraw.show(1500);

	}

	// </DrawThing>
	// <Init Things > *************
	private void initInGameBackground() {
		inGameBackground = new String[9];
		inGameBackground[0] = "inGameBackground0.jpg";
		inGameBackground[1] = "inGameBackground1.jpg";
		inGameBackground[2] = "inGameBackground2.jpg";
		inGameBackground[3] = "inGameBackground3.jpg";
		inGameBackground[4] = "inGameBackground4.jpg";
		inGameBackground[5] = "inGameBackground5.jpg";
		inGameBackground[6] = "inGameBackground6.jpg";
		inGameBackground[7] = "inGameBackground7.jpg";
		inGameBackground[8] = "inGameBackground8.jpg";
	}

	private void setKillSoundArray() {
		killSound = new String[5];
		killSound[0] = "killSound0.wav";
		killSound[1] = "killSound1.wav";
		killSound[2] = "killSound2.wav";
		killSound[3] = "killSound3.wav";
		killSound[4] = "killSound4.wav";
	}

	private void createMatrix() {
		flyingDucks = new Ducks[30];
		for (int i = 0; i < 30; i++) {
			flyingDucks[i] = new Ducks();
			flyingDucks[i].isAlive = true;

		}
	}
	// </ Init Things >
}
