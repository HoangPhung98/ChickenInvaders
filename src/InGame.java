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
	City city;
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
	// decoration
	TextAndNumber textAndNumber;

	// time
	private long time;
	private static long timeStartThisLevel;
	private long timeRunningStatus;
	private long timeOfPreviousShot = 0;
	// status
	private int totalDeadDuck;
	private int consecutiveShotStatus = -1;
	private static int round;
	private int deadDuckNumber;
	private long shotTime = 0;

	Ducks[] flyingDucks;
	String[] inGameBackground;
	String[] killSound;
	String[] scoreNumber;

	Gun gun;

	Point point;

	// ===============================================================================
	public InGame() {
		thread = new Thread(this);
		thread.start();

		gameFrame = new GameFrame();
		GameFrame.started = true;
		city = new City();
		
		initScoreNumberSprites();
		initInGameBackground();
		setKillSoundArray();
		createMatrix();
		round = 1;

		drawBackGround();
		gun = new Gun();
		textAndNumber = new TextAndNumber();

		// vẽ tâm ban đầu
		xCross = StdDraw.mouseX();
		yCross = StdDraw.mouseY();
		drawCross();

		deadDuckNumber = 0;
		totalDeadDuck = 0;
		numberOfDuckThisRound = NUMBER_OF_DUCKS;
		timeRunningStatus = TIME_OF_A_LEVEL;

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
				if (checkCanMove(time)) {
					for (int i = 0; i < numberOfDuckThisRound; i++)
						flyingDucks[i].updateDuckPosition();
					time = System.currentTimeMillis();
				}
				drawBackGround();
				processShot();
				drawTimeRunning();
				drawDuck();
				drawSandBag();
				city.drawBloodBar();
				drawCross();
				drawVietNamFlag();

				gun.drawBullet();
				if (gun.canReloadAmo) {
					gun.DrawReloadGunAmo_GunExplotion_DisappearEffect();
				} else
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
	private void processShot() {
		if (System.currentTimeMillis() - shotTime > TIME_BETWEEN_SHOTS) {
			if (StdDraw.isMousePressed()) {
				setGunShotEffect();
				checkDuckIsShoted();
				checkBoomIsShoted();
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
					StdAudio.play("Audio/Duck/quak.wav");
					processConsecutiveShot();
				}
			}
		}
	}
	private void checkBoomIsShoted(){
		for (int i = 0; i < numberOfDuckThisRound; i++) {
			if (!flyingDucks[i].duckHasBoom) {
				if (StdDraw.mouseX() < flyingDucks[i].xBoomCoordinate + duckPading
						&& StdDraw.mouseX() > flyingDucks[i].xBoomCoordinate - duckPading
						&& StdDraw.mouseY() < flyingDucks[i].yBoomCoordinate + duckPading
						&& StdDraw.mouseY() > flyingDucks[i].yBoomCoordinate - duckPading) {
					flyingDucks[i].setBoomIsShoted();
				}
			}
		}
		
	}
	private void setGunShotEffect() {
		gun.drawExplosion();
		gun.bulletNumber--;
		if(gun.bulletNumber<12) gun.canDrawDisappearEffect = true;
		gun.drawShotingLightRay();
		StdAudio.play("Audio/GunSound/gunShot.wav");
		StdAudio.play("Audio/GunSound/gunReload.wav");
		Gun.canGunExplosion = true;
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
		round++;
		deadDuckNumber = 0;
		consecutiveShotStatus = -1;
		gun.bulletNumber = gun.BULLET_NUMBER + round;
		valocity -= (double) 28 / (double) round;
		timeStartThisLevel = System.currentTimeMillis();
		timeRunningStatus= TIME_OF_A_LEVEL;
		System.out.println(valocity);

	}

	private void updaterNewStatusOfDucks() {
		for (int i = 0; i < numberOfDuckThisRound; i++) {
			flyingDucks[i].isAlive = true;
			flyingDucks[i].duckHasBoom = true;
			flyingDucks[i].canBoomExplosion = true;
			flyingDucks[i].boomIsShoted=false;
			flyingDucks[i].canPlayExplosionSound = true;
			flyingDucks[i].hasDecreasBloodOfCity = false;

		}
	}

	private void creatNewInGame() {
		numberOfDuckThisRound = NUMBER_OF_DUCKS;
		totalDeadDuck = 0;
		round = 0;
		updateNextRound();
		valocity = 80;
		city.resetBlood();
	}

	private int getTimeRunningLength() {
		return (int) (((double) gameFrame.WIDTH / (double) TIME_OF_A_LEVEL) * timeRunningStatus);
	}

	// </Update Thing>

	// < Check Things > **************
	private boolean checkGameOver() {
		if (timeRunningStatus < 0 || gun.bulletNumber == 0 || city.bloodStatus ==0) {
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
		StdDraw.picture(gameFrame.getHalfWidth(), gameFrame.getHalfHeight(), inGameBackground[round - 1],
				GameFrame.WIDTH, GameFrame.HEIGHT);
	}

	private void drawSandBag() {
		StdDraw.picture(GameFrame.HALF_WIDTH, 85, "Images/Background/sandbag.png", GameFrame.HALF_WIDTH + 350, 300);
	}

	private void drawVietNamFlag() {
		StdDraw.picture(gameFrame.WIDTH - 200, 100, "Images/Decoration/VietNamFlagTrans.gif");
	}

	public void drawCross() {
		StdDraw.picture(StdDraw.mouseX(), StdDraw.mouseY(), "Images/Wepon/gunCross2.png");
	}

	private void drawDuck() {
		for (int i = 0; i < numberOfDuckThisRound; i++) {
			if(!flyingDucks[i].duckHasBoom)flyingDucks[i].drawBoombing();
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
		StdDraw.picture(getTimeRunningLength(), gameFrame.HEIGHT - 22, "Images/Decoration/timeRunningDuck.png", 100,
				100);
	}

	private void drawGameOver() {
		drawReasonOfGameOver();
		for (int i = 0; i < GameFrame.HALF_WIDTH; i += 15) {
			drawBasicThingOnScreen();
			StdDraw.picture(i, gameFrame.HALF_HEIGHT, "Images/NumberWord/gameOver.jpg");
			StdDraw.show(3);
		}
		
		for (int i = 1; i <= totalDeadDuck; i++) {
			drawScoreNumber(i);
		}

		StdDraw.show(2000);
	}
	private void drawReasonOfGameOver(){
		for (int i = 0; i < gameFrame.HALF_WIDTH; i += 20) {
			drawBasicThingOnScreen();

			// draw round board at left side
			StdDraw.picture(i , gameFrame.HALF_HEIGHT-10, "Images/NumberWord/metalBoardEditted.png", 472, 250);
			if(gun.bulletNumber==0){
			textAndNumber.drawWord(i - 145, gameFrame.HALF_HEIGHT, "het", 40, 135);
			textAndNumber.drawWord(i +20, gameFrame.HALF_HEIGHT, "dan", 40, 135);
			}else
				if(city.bloodStatus==0){
					textAndNumber.drawWord(i - 160, gameFrame.HALF_HEIGHT, "city", 40, 135);
					textAndNumber.drawWord(i +55, gameFrame.HALF_HEIGHT, "ruin", 40, 135);
				}else{
					textAndNumber.drawWord(i - 150, gameFrame.HALF_HEIGHT, "time", 40, 135);
					textAndNumber.drawWord(i +80, gameFrame.HALF_HEIGHT, "up", 40, 135);
				}
//			textAndNumber.drawNumber(i - 5, 529, round, 40, 135);
			StdDraw.show(3);
		}
		StdDraw.show(1800);
	}
	private void drawScoreNumber(int i) {
		StdDraw.picture(gameFrame.HALF_WIDTH - 60, gameFrame.HALF_HEIGHT + 200, scoreNumber[i / 10], 80, 200);
		StdDraw.picture(gameFrame.HALF_WIDTH + 25, gameFrame.HALF_HEIGHT + 200, scoreNumber[i % 10], 80, 200);
		StdDraw.show(100);

	}

	private void drawReplayGameWallpaper() {
		StdDraw.picture(GameFrame.HALF_WIDTH, GameFrame.HALF_HEIGHT, "Images/Background/replayWallpaper.png",
				GameFrame.WIDTH, GameFrame.HEIGHT);
		StdDraw.show(10);

	}

	private void drawNextRound() {
		for (int i = 0; i < 430; i += 20) {
			drawBasicThingOnScreen();

			// draw 4 ducks right side
			StdDraw.picture(gameFrame.WIDTH - i - 270, gameFrame.HALF_HEIGHT,
					"Images/Background/transparent4DucksWallpaper.png", gameFrame.WIDTH, gameFrame.HEIGHT);

			// draw round board at left side
			StdDraw.picture(i - 110, 525, "Images/NumberWord/metalBoardEditted.png", 472, 250);
			textAndNumber.drawWord(i - 255, 529, "round", 40, 135);
			textAndNumber.drawNumber(i - 5, 529, round, 40, 135);
			StdDraw.show(3);
		}
		StdDraw.show(1200);
		for (int i = 430; i >=-100 ; i -= 20) {
			
			drawBasicThingOnScreen();
			// draw 4 ducks right side
			StdDraw.picture(gameFrame.WIDTH - i - 270, gameFrame.HALF_HEIGHT,
					"Images/Background/transparent4DucksWallpaper.png", gameFrame.WIDTH, gameFrame.HEIGHT);

			// draw round board at left side
			StdDraw.picture(i - 110, 525, "Images/NumberWord/metalBoardEditted.png", 472, 250);
			textAndNumber.drawWord(i - 255, 529, "round", 40, 135);
			textAndNumber.drawNumber(i - 5, 529, round, 40, 135);
			StdDraw.show(3);
		}
		
		
		timeStartThisLevel = System.currentTimeMillis();
		updateTimeRunning();

	}
	private void drawBasicThingOnScreen(){
		drawBackGround();
		drawSandBag();
		city.drawBloodBar();
		gun.drawGunWithDegree();
		gun.drawBullet();
		drawCross();
		drawVietNamFlag();
	}

	// </DrawThing>
	// <Init Things > *************
	private void initInGameBackground() {
		inGameBackground = new String[10];
		inGameBackground[1] = "Images/Background/inGameBackground1.jpg";
		inGameBackground[0] = "Images/Background/inGameBackground0.jpg";
		inGameBackground[2] = "Images/Background/inGameBackground2.jpg";
		inGameBackground[3] = "Images/Background/inGameBackground3.jpg";
		inGameBackground[4] = "Images/Background/inGameBackground4.jpg";
		inGameBackground[5] = "Images/Background/inGameBackground5.jpg";
		inGameBackground[6] = "Images/Background/inGameBackground6.jpg";
		inGameBackground[7] = "Images/Background/inGameBackground7.jpg";
		inGameBackground[8] = "Images/Background/inGameBackground8.jpg";
		inGameBackground[9] = "Images/Background/inGameBackground9.jpg";
	}

	private void initScoreNumberSprites() {
		scoreNumber = new String[10];
		scoreNumber[0] = "Images/NumberWord/Number/scoreBoard0.png";
		scoreNumber[1] = "Images/NumberWord/Number/scoreBoard1.png";
		scoreNumber[2] = "Images/NumberWord/Number/scoreBoard2.png";
		scoreNumber[3] = "Images/NumberWord/Number/scoreBoard3.png";
		scoreNumber[4] = "Images/NumberWord/Number/scoreBoard4.png";
		scoreNumber[5] = "Images/NumberWord/Number/scoreBoard5.png";
		scoreNumber[6] = "Images/NumberWord/Number/scoreBoard6.png";
		scoreNumber[7] = "Images/NumberWord/Number/scoreBoard7.png";
		scoreNumber[8] = "Images/NumberWord/Number/scoreBoard8.png";
		scoreNumber[9] = "Images/NumberWord/Number/scoreBoard9.png";
	}

	private void setKillSoundArray() {
		killSound = new String[5];
		killSound[0] = "Audio/Effect/KillSound/killSound0.wav";
		killSound[1] = "Audio/Effect/KillSound/killSound1.wav";
		killSound[2] = "Audio/Effect/KillSound/killSound2.wav";
		killSound[3] = "Audio/Effect/KillSound/killSound3.wav";
		killSound[4] = "Audio/Effect/KillSound/killSound4.wav";
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
