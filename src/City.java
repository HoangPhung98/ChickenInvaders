
public class City {
	
	public static final int BLOOD = 100;
	public static int bloodStatus;
	public City(){
		bloodStatus = BLOOD;
	}
	public void resetBlood(){
		bloodStatus = BLOOD;
	}
	public void drawBloodBar(){
		StdDraw.filledRectangle(GameFrame.WIDTH-100, 40, 25, bloodStatus);
	}

}
