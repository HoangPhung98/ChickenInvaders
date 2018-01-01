import java.awt.Point;
import java.awt.print.Book;

public class Ducks {
	//logic
	public boolean hasDecreasBloodOfCity = false;
	// Position
	public int xCoordinate;
	public int yCoordinate;
	public int xdirection;
	public int ydirection;
	public int xBoomCoordinate;
	public int yBoomCoordinate;
	public int xBoomDicrection;
	public int yBoomDicrection;
	private int xDuckDeathAtCoordinate;
	private int yDuckDeathAtCoordinate;
	int degree;
	private final int FROM_LEFT_WALL = 0;
	private final int FROM_BOTTOM_WALL = 1;
	private final int FROM_RIGHT_WALL = 2;
	private final int SPLASH_BLOOD_TIME = 1000;
	private final int ONE_MOVE_OF_DUCK = 17;

	// Animation
	String[] duckRightAnimation = new String[3];
	String[] duckLeftAnimation = new String[3];
	String[] duckDeadAnimation = new String[3];
	String[] splashBloodAnimation = new String[6];
	String[] boom = new String[5];
	String[] boomIsShotedExplosion = new String[25];
	String[] boomHitGroundExplosion = new String[25];
	String splashBloodPicture;
	int duckAnimationStatus = 0;
	int duckDeadAnimationStatus = 1;
	int boomExplosionStatus = 0;
	public boolean isAlive;
	final int VALOCITY_OF_BOOM_FALLING = 20;
	final int TIME_MOVE_OF_BOOM_FALLING = 20;
	boolean duckHasBoom = true;
	boolean canBoomExplosion = true;
	boolean canPlayExplosionSound = true;
	int boomExplosionPosition;
	boolean boomIsShoted = false;

	// Color
	private String[] colorAray = { "Blue", "Green", "Red" };
	private String color;

	// Time
	private long duckDieAtTime;
	static long timeStayOnAir = 500;
	long time;

	// ===============================================================================
	public Ducks() {
		color = colorAray[StdRandom.uniform(0, 3)];
		setDuckRightAnimation();
		setDuckLeftAnimation();
		setDuckDeadAnimation();
		setSplashBloodPicture();
		setRamdomBoomExplosionPosition();
		setBoomHitGroundAnimation(color);
		setBoomIsShottedAnimation(color);
	}

	// < Process Things > ****************
	private void checkHitWall() {
		// if the duck hit the wall 4 times then it can fly away
		if (this.yCoordinate + 20 > GameFrame.getHeight()) {
			// if (!checkHit4Times(this)) {
			this.ydirection *= -1;
			if (StdRandom.uniform(2)==1 && duckHasBoom)
				setABoom();
			// this.hitWallTimes++;
		} else {
			// this.isGone = true;
		}

		// the duck is turn back if hit the wall
		if (this.xCoordinate + 20 > GameFrame.getWidth() || this.xCoordinate - 20 < 0) {
			this.xdirection *= -1;
			// this.hitWallTimes++;
		}
		if (this.yCoordinate - 20 < 0) {
			if (this.isAlive) {
				this.ydirection *= -1;
				// this.hitWallTimes++;
			} else {
				this.ydirection = 0;
				this.xdirection = 0;
			}
		}
		updateDuckDegree();
	}
	// </ Process Things >

	// <Update Things> **********
	private void updateBoomPosition() {
		yBoomCoordinate += yBoomDicrection;
	}

	private void updateDuckDegree() {
		if (this.xdirection > 0) {
			if (this.ydirection > 0)
				this.degree = 10;
			else
				this.degree = 315;
		} else {
			if (this.ydirection > 0)
				this.degree = 340;
			else
				this.degree = 45;
		}
	}

	void updateDuckPosition() {
		this.xCoordinate += this.xdirection;
		this.yCoordinate += this.ydirection;
		updateDuckAnimationStatus();
		checkHitWall();
	}

	private void updateDuckAnimationStatus() {
		if (this.isAlive) {
			this.duckAnimationStatus++;
			this.duckAnimationStatus %= 3;
		} else {
			this.duckDeadAnimationStatus %= 2;
			this.duckDeadAnimationStatus++;
		}
	}
	// </ Update Thing>

	// <Draw Things> *********
	public void drawSplashBlood() {
		if (!this.isAlive) {
			if (System.currentTimeMillis() - duckDieAtTime < SPLASH_BLOOD_TIME) {
				StdDraw.picture(xDuckDeathAtCoordinate, yDuckDeathAtCoordinate, splashBloodPicture);
			}
		}
	}

	public void drawBoombing() {
		if (!boomIsShoted) {
			if (yBoomCoordinate > boomExplosionPosition) {
				StdDraw.picture(xBoomCoordinate, yBoomCoordinate, "Images/Wepon/boom"+this.color+".png", 100, 100);
				updateBoomPosition();
			} else {
				if(!hasDecreasBloodOfCity){
					City.bloodStatus-=50;
					if(City.bloodStatus<0)City.bloodStatus=0;
					hasDecreasBloodOfCity=true;
				}
				drawBoomHitGroundExplosion();
				// }
				if (canPlayExplosionSound) {
					StdAudio.play("Audio/Effect/Boom/boomFallingExplosionSound.wav");
					canPlayExplosionSound = false;
				}
			}
		} else {
			drawBoomIsShotedExplosion();
			if(canPlayExplosionSound){
				StdAudio.play("Audio/Effect/Boom/boomExplosionSound"+this.color+".Wav");
				canPlayExplosionSound =false;
			}
		}

	}

	public void drawBoomHitGroundExplosion() {
		if (canBoomExplosion) {
			StdDraw.picture(xBoomCoordinate, yBoomCoordinate,boomHitGroundExplosion[boomExplosionStatus], 200, 200);
			boomExplosionStatus++;
			if (boomHitGroundExplosion[boomExplosionStatus] == null) {
				canBoomExplosion = false;
				boomExplosionStatus = 0;
			}
		}
	}
	public void drawBoomIsShotedExplosion() {
		if (canBoomExplosion) {
			StdDraw.picture(xBoomCoordinate, yBoomCoordinate,boomIsShotedExplosion[boomExplosionStatus], 200, 200);
			boomExplosionStatus++;
			if (boomIsShotedExplosion[boomExplosionStatus] == null) {
				canBoomExplosion = false;
				boomExplosionStatus = 0;
			}
		}
	}


	// </Draw Things>
	// < Set Things? ***********
	private void setRamdomBoomExplosionPosition() {
		boomExplosionPosition = StdRandom.uniform(180, 320);
	}

	private void setABoom() {
		xBoomCoordinate = this.xCoordinate;
		yBoomCoordinate = this.yCoordinate;
		xBoomDicrection = 0;
		yBoomDicrection = -14;
		duckHasBoom = false;
		StdAudio.play("Audio/Effect/Boom/boomFallingSound.wav");

	}

	private void setRamdomSplashBlood() {
		splashBloodPicture = splashBloodAnimation[StdRandom.uniform(6)];
	}

	private void setRamdomDirection() {
		if (StdRandom.uniform(0, 2) == 0) {
			this.xdirection = -ONE_MOVE_OF_DUCK;
		} else
			this.xdirection = ONE_MOVE_OF_DUCK;
		if (StdRandom.uniform(0, 2) == 0) {
			this.ydirection = -ONE_MOVE_OF_DUCK;
		} else
			this.ydirection = ONE_MOVE_OF_DUCK;
	}

	void setDuckDeadStayOnAir() {
		time = System.currentTimeMillis();
		this.xdirection = 0;
		this.ydirection = 0;
	}

	void setDuckFallDown() {
		this.xdirection = 0;
		this.ydirection = -50;
	}

	void setDuckDead() {
		setDuckDeadStayOnAir();
		this.isAlive = false;
		this.duckDieAtTime = System.currentTimeMillis();
		this.xDuckDeathAtCoordinate = (int) StdDraw.mouseX();
		this.yDuckDeathAtCoordinate = (int) StdDraw.mouseY();
	}

	public void setBoomIsShoted() {
		boomIsShoted = true;
	}
	// </Set Thing>

	// < Get Things> ***********
	String getDuck_R_or_Left_Animation() {
		if (this.isAlive) {
			if (xdirection > 0)
				return duckRightAnimation[this.duckAnimationStatus];
			else
				return duckLeftAnimation[this.duckAnimationStatus];
		} else {
			time = System.currentTimeMillis();

			return duckDeadAnimation[0];
		}
	}

	private Point getCoordinateOfDuck() {

		int y = 20;
		int x = StdRandom.uniform(20, GameFrame.getWidth() - 20);
		if (StdRandom.uniform(3) == FROM_LEFT_WALL) {
			x = 20;
			y = StdRandom.uniform(20, GameFrame.HEIGHT - 20);
		}
		if (StdRandom.uniform(3) == FROM_BOTTOM_WALL) {
			y = 20;
			x = StdRandom.uniform(20, GameFrame.getWidth() - 20);
		}
		if (StdRandom.uniform(3) == FROM_RIGHT_WALL) {
			x = GameFrame.WIDTH - 20;
			y = StdRandom.uniform(20, GameFrame.HEIGHT - 20);
		}

		return new Point(x, y);

	}
	// </ Set Things >

	// < Init Things > *********************
	void addDuck() {
		color = colorAray[StdRandom.uniform(0, 3)];
		setDuckRightAnimation();
		setDuckLeftAnimation();
		setDuckDeadAnimation();
		setRamdomBoomExplosionPosition();
		setBoomHitGroundAnimation(color);
		setBoomIsShottedAnimation(color);
		
		this.yCoordinate = getCoordinateOfDuck().y;
		this.xCoordinate = getCoordinateOfDuck().x;
		setRamdomDirection();
		setRamdomSplashBlood();
		updateDuckDegree();

	}

	private void setBoomIsShottedAnimation(String color){
		boomIsShotedExplosion[0] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/0.png";
		boomIsShotedExplosion[1] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/1.png";
		boomIsShotedExplosion[2] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/2.png";
		boomIsShotedExplosion[3] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/3.png";
		boomIsShotedExplosion[4] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/4.png";
		boomIsShotedExplosion[5] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/5.png";
		boomIsShotedExplosion[6] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/6.png";
		boomIsShotedExplosion[7] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/7.png";
		boomIsShotedExplosion[8] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/8.png";
		boomIsShotedExplosion[9] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/9.png";
		boomIsShotedExplosion[10] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/10.png";
		boomIsShotedExplosion[11] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/11.png";
		boomIsShotedExplosion[12] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/12.png";
		boomIsShotedExplosion[13] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/13.png";
		boomIsShotedExplosion[14] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/14.png";
		boomIsShotedExplosion[15] = "Images/Effect/BoomExplosion/IsShoted/"+color+"/15.png";
	}
	private void setBoomHitGroundAnimation(String color){
		boomHitGroundExplosion[0] = "Images/Effect/BoomExplosion/HitGround/"+color+"/0.png";
		boomHitGroundExplosion[1] = "Images/Effect/BoomExplosion/HitGround/"+color+"/1.png";
		boomHitGroundExplosion[2] = "Images/Effect/BoomExplosion/HitGround/"+color+"/2.png";
		boomHitGroundExplosion[3] = "Images/Effect/BoomExplosion/HitGround/"+color+"/3.png";
		boomHitGroundExplosion[4] = "Images/Effect/BoomExplosion/HitGround/"+color+"/4.png";
		boomHitGroundExplosion[5] = "Images/Effect/BoomExplosion/HitGround/"+color+"/5.png";
		boomHitGroundExplosion[6] = "Images/Effect/BoomExplosion/HitGround/"+color+"/6.png";
		boomHitGroundExplosion[7] = "Images/Effect/BoomExplosion/HitGround/"+color+"/7.png";
		boomHitGroundExplosion[8] = "Images/Effect/BoomExplosion/HitGround/"+color+"/8.png";
		boomHitGroundExplosion[9] = "Images/Effect/BoomExplosion/HitGround/"+color+"/9.png";
		boomHitGroundExplosion[10] = "Images/Effect/BoomExplosion/HitGround/"+color+"/10.png";
		boomHitGroundExplosion[11] = "Images/Effect/BoomExplosion/HitGround/"+color+"/11.png";
		boomHitGroundExplosion[12] = "Images/Effect/BoomExplosion/HitGround/"+color+"/12.png";
		boomHitGroundExplosion[13] = "Images/Effect/BoomExplosion/HitGround/"+color+"/13.png";
		boomHitGroundExplosion[14] = "Images/Effect/BoomExplosion/HitGround/"+color+"/14.png";
		boomHitGroundExplosion[15] = "Images/Effect/BoomExplosion/HitGround/"+color+"/15.png";
		
	}
	private void setDuckRightAnimation() {
		this.duckRightAnimation[0] = "Images/Duck/" + this.color + "Duck/" + this.color + "DuckR0.png";
		this.duckRightAnimation[1] = "Images/Duck/" + this.color + "Duck/" + this.color + "DuckR1.png";
		this.duckRightAnimation[2] = "Images/Duck/" + this.color + "Duck/" + this.color + "DuckR2.png";
	}

	private void setDuckLeftAnimation() {
		this.duckLeftAnimation[0] = "Images/Duck/" + this.color + "Duck/" + this.color + "DuckL0.png";
		this.duckLeftAnimation[1] = "Images/Duck/" + this.color + "Duck/" + this.color + "DuckL1.png";
		this.duckLeftAnimation[2] = "Images/Duck/" + this.color + "Duck/" + this.color + "DuckL2.png";
	}

	private void setDuckDeadAnimation() {
		duckDeadAnimation[0] = "Images/Duck/" + this.color + "Duck/" + this.color + "DuckDead0.png";
		duckDeadAnimation[1] = "Images/Duck/" + this.color + "Duck/" + this.color + "DuckDead1.png";
		duckDeadAnimation[2] = "Images/Duck/" + this.color + "Duck/" + this.color + "DuckDead2.png";
	}

	private void setSplashBloodPicture() {
		splashBloodAnimation[0] = "Images/Effect/Splash/bloodSplash0.png";
		splashBloodAnimation[1] = "Images/Effect/Splash/bloodSplash1.png";
		splashBloodAnimation[2] = "Images/Effect/Splash/bloodSplash2.png";
		splashBloodAnimation[3] = "Images/Effect/Splash/bloodSplash3.png";
		splashBloodAnimation[4] = "Images/Effect/Splash/bloodSplash4.png";
		splashBloodAnimation[5] = "Images/Effect/Splash/bloodSplash5.png";
	}
	// </ Init Things>

}
