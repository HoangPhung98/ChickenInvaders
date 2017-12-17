import java.awt.Point;

public class Ducks {

	public int xCoordinate;
	public int yCoordinate;
	public int xdirection;
	public int ydirection;
	int degree;
	public boolean isAlive;
	public boolean isGone;
	public boolean canFlyAway;
	public int hitWallTimes;
	private final int FROM_LEFT_WALL = 0;
	private final int FROM_BOTTOM_WALL = 1;
	private final int FROM_RIGHT_WALL = 2;
	String[] duckRightAnimation = new String[3];
	String[] duckLeftAnimation = new String[3];
	String[] duckDeadAnimation  = new String[2];
	int duckAnimationStatus = 0;
	static long timeStayOnAir = 500;
	private String [] colorAray = {"blue", "green","red"};
	private String color;
	long time;
	public Ducks(){
		color = colorAray[StdRandom.uniform(0, 3)];
		setDuckRightAnimation();
		setDuckLeftAnimation();
		setDuckDeadAnimation();
	}
	private void setDuckRightAnimation(){
		this.duckRightAnimation[0] = this.color + "DuckR0.png";
		this.duckRightAnimation[1] = this.color + "DuckR1.png";
		this.duckRightAnimation[2] = this.color + "DuckR2.png";
	}
	private void setDuckLeftAnimation(){
		this.duckLeftAnimation[0] = this.color + "DuckL0.png";
		this.duckLeftAnimation[1] = this.color + "DuckL1.png";
		this.duckLeftAnimation[2] = this.color + "DuckL2.png";
	}
	private void setDuckDeadAnimation(){
		duckDeadAnimation[0] = this.color + "DuckDead0.png";
		duckDeadAnimation[1] = this.color + "DuckDead1.png";
	}
	String getDuck_R_or_Left_Animation(){
		if(this.isAlive){
			if(xdirection > 0) return duckRightAnimation[this.duckAnimationStatus];
			else return duckLeftAnimation[this.duckAnimationStatus];
		}else{
			time = System.currentTimeMillis();
			
//			if()
			return duckDeadAnimation[0];
		}
	}
	private void updateDuckDegree(){
		if(this.xdirection > 0){
			if(this.ydirection > 0) this.degree = 10;
			else this.degree = 315;
		}else{
			if(this.ydirection > 0) this.degree = 340;
			else this.degree = 45;
		}
	}
	void addDuck() {
		this.yCoordinate = getCoordinateOfDuck().y;
		this.xCoordinate = getCoordinateOfDuck().x;
		setRamdomDirection();
		updateDuckDegree();

	}

	void updateDuckPosition() {
		this.xCoordinate += this.xdirection;
		this.yCoordinate += this.ydirection;
		updateDuckAnimationStatus();
		checkHitWall();
	}
	private void updateDuckAnimationStatus(){
		this.duckAnimationStatus++;
		this.duckAnimationStatus%=3;
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

	private void setRamdomDirection() {
		if (StdRandom.uniform(0, 2) == 0) {
			this.xdirection = -35;
		} else
			this.xdirection = 35;
		if (StdRandom.uniform(0, 2) == 0) {
			this.ydirection = -35;
		} else
			this.ydirection = 35;
		System.out.println("x: "+ xdirection + " y: "+ydirection);
	}

	void setDuckDead() {
		setDuckDeadStayOnAir();
		this.isAlive = false;
	}
	void setDuckDeadStayOnAir(){
		time = System.currentTimeMillis();
		this.xdirection = 0;
		this.ydirection = 0;
	}
	void setDuckFallDown(){
		this.xdirection = 0;
		this.ydirection = -50;
	}

	private void checkHitWall() {
		// if the duck hit the wall 4 times then it can fly away
		if (this.yCoordinate + 20 > GameFrame.getHeight()) {
			if (!checkHit4Times(this)) {
				this.ydirection *= -1;
				this.hitWallTimes++;
			} else {
				this.isGone = true;
			}
		}

		// the duck is turn back if hit the wall
		if (this.xCoordinate + 20 > GameFrame.getWidth() || this.xCoordinate - 20 < 0) {
			this.xdirection *= -1;
			this.hitWallTimes++;
		}
		if (this.yCoordinate - 20 < 0) {
			if(this.isAlive){
				this.ydirection *= -1;
				this.hitWallTimes++;
			}else { this.ydirection =0; this.xdirection = 0;}
		}
		updateDuckDegree();
	}

	private boolean checkHit4Times(Ducks duck) {
		if (duck.hitWallTimes > 4)
			return true;
		else
			return false;
	}

}
