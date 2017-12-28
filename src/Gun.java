
public class Gun {
	//Status
	static final int BULLET_NUMBER = 10;
	static int bulletNumber = BULLET_NUMBER;
	//Position
	double canhHuyen, canhX, canhY;
	static int  gunDegree =0;
	
	//Animation
	String[] gunReloadingAnimation = new String[7];
	String[] numberSprites = new String[10];
	String gunStatic;
	String bulletPicture;
	String explosion;
	int reloadGunAmoStatus = -1;
	public static boolean canReloadAmo = false;
	//Time
	private long timeReloadGunAmo = 20;
	private long time = 0;
	//==================================================================================
	public Gun(){
	init();
//	StdDraw.setPenRadius(0.01);
//	StdDraw.setPenColor(StdDraw.RED);
	}
	
	//<Draw Things>************
	public void drawExplosion(){
		StdDraw.picture(StdDraw.mouseX(), StdDraw.mouseY(), explosion);
	}
	public void drawGunWithDegree(){
		StdDraw.picture(GameFrame.HALF_WIDTH, 0 , gunStatic, 300, 630, getDegreeOfGun());
	}
	public void drawShotingLightRay(){
		StdDraw.setPenRadius(0.025);
		StdDraw.line(GameFrame.HALF_WIDTH, 25, StdDraw.mouseX(), StdDraw.mouseY());
	}
	public void drawBullet(){
		StdDraw.picture(200, 100, "metalBoardEditted.png",400,200);
		StdDraw.picture(130, 100, "bulletWord.png",180,40);
		drawNumber(bulletNumber,300,100);
		
		for(int i = 0; i< bulletNumber; i++){
			StdDraw.picture(80+i*60, 40, bulletPicture,25,35);
		}
	}
	public void drawNumber(int number, int x, int y){
		StdDraw.picture(x, y, numberSprites[number/10],30,40);
		StdDraw.picture(x+50, y, numberSprites[number%10],30,40);
	}
	public void DrawReloadGunAmo(){
		if(System.currentTimeMillis() - this.time > timeReloadGunAmo){
			this.reloadGunAmoStatus++;
			StdDraw.picture(GameFrame.HALF_WIDTH, 20, gunReloadingAnimation[this.reloadGunAmoStatus], 300, 630, getDegreeOfGun());
			StdDraw.show(5);

			if(reloadGunAmoStatus==5){
				reloadGunAmoStatus=-1;
				canReloadAmo = false;
			}
			this.time = System.currentTimeMillis();
		}
		
	}
	//</Draw Thinigs>
	
	//<Get Things>************
	private double getDegreeOfGun(){
		canhX = GameFrame.HALF_WIDTH - StdDraw.mouseX();
		canhY = StdDraw.mouseY();
		canhHuyen = Math.sqrt(Math.pow(canhX,2)+Math.pow(canhY,2));
		if(StdDraw.mouseX()<GameFrame.HALF_WIDTH){
			return Math.toDegrees(Math.asin((double)(canhX/canhHuyen)));
		}else{
			return Math.toDegrees(Math.asin((double)(canhX/canhHuyen)));
		}
	}
	//</Get Things>
	
	//<Init Thinigs>***********
	private void init(){
		gunStatic  = "gunStaticFixed.png";
		bulletPicture = "bullet.png";
		explosion = "explosion.png";
		gunReloadingAnimation[0] = "gunReloadingAnimation0.png";
		gunReloadingAnimation[1] = "gunReloadingAnimation1.png";
		gunReloadingAnimation[2] = "gunReloadingAnimation2.png";
		gunReloadingAnimation[3] = "gunReloadingAnimation3.png";
		gunReloadingAnimation[4] = "gunReloadingAnimation4.png";
		gunReloadingAnimation[5] = "gunReloadingAnimation5.png";
		
		numberSprites[0] = "0.png";
		numberSprites[1] = "1.png";
		numberSprites[2] = "2.png";
		numberSprites[3] = "3.png";
		numberSprites[4] = "4.png";
		numberSprites[5] = "5.png";
		numberSprites[6] = "6.png";
		numberSprites[7] = "7.png";
		numberSprites[8] = "8.png";
		numberSprites[9] = "9.png";
		
	}
	//</Init Things>
}