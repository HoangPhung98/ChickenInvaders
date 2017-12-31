import java.awt.Point;

public class Gun {
	// Status
	static final int BULLET_NUMBER = 10;
	static int bulletNumber = BULLET_NUMBER;
	// Position
	double canhHuyen, canhX, canhY;
	static int gunDegree = 0;
	final int GUN_SIZE_X = 300;
	final int GUN_SIZE_Y = 630;
	final int GUN_EXPLOSION_SIZE = 135;

	// Animation
	String[] gunReloadingAnimation = new String[7];
	String[] gunExplotionAnimation = new String[3];
	String[] disappearEffect = new String[9];
	String gunStatic;
	String bulletPicture;
	String explosion;
	int reloadGunAmoStatus = -1;
	int gunExplotionStatus = -1;
	int disappearEffectStatus = -1;
	public static boolean canReloadAmo = false;
	public static boolean canGunExplosion = false;
	public static boolean canDrawDisappearEffect = false;
	TextAndNumber textAndNumber;
	// Time
	private long timeReloadGunAmo = 30;
	private long time = 0;

	// ==================================================================================
	public Gun() {
		init();
		textAndNumber = new TextAndNumber();
		// StdDraw.setPenRadius(0.01);
		// StdDraw.setPenColor(StdDraw.RED);
	}

	// <Draw Things>************
	public void drawExplosion() {
		StdDraw.picture(StdDraw.mouseX(), StdDraw.mouseY(), explosion);
	}

	public void drawGunWithDegree() {
		StdDraw.picture(GameFrame.HALF_WIDTH, 0, gunStatic, GUN_SIZE_X, GUN_SIZE_Y, getDegreeOfGun());
	}

	public void drawShotingLightRay() {
		StdDraw.setPenRadius(0.025);
		StdDraw.line(GameFrame.HALF_WIDTH, 25, StdDraw.mouseX(), StdDraw.mouseY());
	}

	public void drawBullet() {
		StdDraw.picture(200, 100, "Images/NumberWord/metalBoardEditted.png", 400, 200);
		StdDraw.picture(130, 135, "Images/NumberWord/bulletWord.png", 180, 40);
		drawBulletNumber(bulletNumber, 290, 135);
		if(bulletNumber<=12){
			for(int i=0; i<bulletNumber; i++)StdDraw.picture(50 + i * 25, 80, bulletPicture, 20, 35);
			//if(canDrawDisappearEffect)drawDisappearEffectOfBullet(bulletNumber);
		}else for(int i=0; i<12; i++)StdDraw.picture(50 + i * 25, 80, bulletPicture, 20, 35);
	}

	public void drawBulletNumber(int number, int x, int y) {
		StdDraw.picture(x, y, textAndNumber.Number[number / 10], 45, 60);
		StdDraw.picture(x + 55, y, textAndNumber.Number[number % 10], 45, 60);
	}
	private void drawDisappearEffectOfBullet(int index){
		for(int i=0 ;i<9; i++){
			StdDraw.picture(50+index*25	, 80, disappearEffect[i],100,100);
			StdDraw.show(1);
		}
		canDrawDisappearEffect = false;
	}
	public void DrawReloadGunAmo_GunExplotion_DisappearEffect() {
		if (System.currentTimeMillis() - this.time > timeReloadGunAmo) {
			this.reloadGunAmoStatus++;
			this.gunExplotionStatus++;
			if(canDrawDisappearEffect){
				this.disappearEffectStatus++;
				StdDraw.picture(50+bulletNumber*25, 80, disappearEffect[disappearEffectStatus],120,120);
			}
			StdDraw.picture(getPositionOfGunExplosion().x, getPositionOfGunExplosion().y,
					gunExplotionAnimation[this.gunExplotionStatus], GUN_EXPLOSION_SIZE, GUN_EXPLOSION_SIZE);
			StdDraw.picture(GameFrame.HALF_WIDTH, 20, gunReloadingAnimation[this.reloadGunAmoStatus], GUN_SIZE_X,
					GUN_SIZE_Y - 150, getDegreeOfGun());
			StdDraw.show(5);
			if(disappearEffectStatus == 8){
				canDrawDisappearEffect = false;
				disappearEffectStatus = -1;
			}
			if (gunExplotionStatus == 2) {
				canGunExplosion = false;
				gunExplotionStatus = -1;
			}
			if (reloadGunAmoStatus == 5) {
				reloadGunAmoStatus = -1;
				canReloadAmo = false;
			}
			this.time = System.currentTimeMillis();
		}

	}
	// </Draw Thinigs>

	// <Get Things>************
	private double getDegreeOfGun() {
		canhX = GameFrame.HALF_WIDTH - StdDraw.mouseX();
		canhY = StdDraw.mouseY();
		canhHuyen = Math.sqrt(Math.pow(canhX, 2) + Math.pow(canhY, 2));
		if (StdDraw.mouseX() < GameFrame.HALF_WIDTH) {
			return Math.toDegrees(Math.asin((double) (canhX / canhHuyen)));
		} else {
			return Math.toDegrees(Math.asin((double) (canhX / canhHuyen)));
		}
	}

	private Point getPositionOfGunExplosion() {
		int x;
		int y;
		x = (int) (GameFrame.HALF_WIDTH - 265 * ((Math.sin(Math.toRadians(getDegreeOfGun())))));
		y = (int) (265 * ((Math.abs(Math.cos(Math.toRadians(getDegreeOfGun()))))));
		return new Point(x, y);
	}
	// </Get Things>

	// <Init Thinigs>***********
	private void init() {
		gunStatic = "Images/Wepon/gunStaticFixed.png";
		bulletPicture = "Images/Wepon/bullet.png";
		explosion = "Images/Effect/explosion.png";
		gunReloadingAnimation[0] = "Images/Wepon/gunReloadingAnimation0.png";
		gunReloadingAnimation[1] = "Images/Wepon/gunReloadingAnimation1.png";
		gunReloadingAnimation[2] = "Images/Wepon/gunReloadingAnimation2.png";
		gunReloadingAnimation[3] = "Images/Wepon/gunReloadingAnimation3.png";
		gunReloadingAnimation[4] = "Images/Wepon/gunReloadingAnimation4.png";
		gunReloadingAnimation[5] = "Images/Wepon/gunReloadingAnimation5.png";

		gunExplotionAnimation[0] = "Images/Effect/gunExplosion0.png";
		gunExplotionAnimation[1] = "Images/Effect/gunExplosion1.png";
		gunExplotionAnimation[2] = "Images/Effect/gunExplosion2.png";
		
		disappearEffect[0] = "Images/Effect/disappearEffect0.png";
		disappearEffect[1] = "Images/Effect/disappearEffect1.png";
		disappearEffect[2] = "Images/Effect/disappearEffect2.png";
		disappearEffect[3] = "Images/Effect/disappearEffect3.png";
		disappearEffect[4] = "Images/Effect/disappearEffect4.png";
		disappearEffect[5] = "Images/Effect/disappearEffect6.png";
		disappearEffect[6] = "Images/Effect/disappearEffect6.png";
		disappearEffect[7] = "Images/Effect/disappearEffect7.png";
		disappearEffect[8] = "Images/Effect/disappearEffect8.png";

	}
	// </Init Things>
}