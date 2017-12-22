
public class Gun {
	static int  gunDegree =0;
	String[] gunReloadingAnimation = new String[7];
	String gunStatic;
	String bulletPicture;
	String explosion;
	double canhHuyen, canhX, canhY;
	public static boolean canReloadAmo = false;
	int reloadGunAmoStatus = -1;
	private long timeReloadGunAmo = 20;
	private long time = 0;
	static final int BULLET_NUMBER = 10;
	static int bulletNumber = BULLET_NUMBER;

	public Gun(){
	gunStatic  = "gunStaticFixed.png";
	bulletPicture = "bullet.png";
	explosion = "explosion.png";
	gunReloadingAnimation[0] = "gunReloadingAnimation0.png";
	gunReloadingAnimation[1] = "gunReloadingAnimation1.png";
	gunReloadingAnimation[2] = "gunReloadingAnimation2.png";
	gunReloadingAnimation[3] = "gunReloadingAnimation3.png";
	gunReloadingAnimation[4] = "gunReloadingAnimation4.png";
	gunReloadingAnimation[5] = "gunReloadingAnimation5.png";
	
	StdDraw.setPenRadius(0.01);
	StdDraw.setPenColor(StdDraw.RED);
	}
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
		for(int i = 0; i< bulletNumber; i++){
			StdDraw.picture(80+i*60, 40, bulletPicture,25,35);
		}
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
}