import java.awt.Font;

public class City {
	
	public static final int BLOOD = 600;
	public static int bloodStatus;
	public City(){
		bloodStatus = BLOOD;
	}
	public void resetBlood(){
		bloodStatus = BLOOD;
	}
	public void drawBloodBar(){
		StdDraw.picture(GameFrame.WIDTH-80, GameFrame.HALF_HEIGHT, "Images/Decoration/bloodBar.png",40,600);
		StdDraw.setFont(new Font("city", BLOOD, 25));
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.text(GameFrame.WIDTH-80, GameFrame.HALF_HEIGHT, "City is being Destroying!", -90);
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.filledRectangle(GameFrame.WIDTH-80, GameFrame.HALF_HEIGHT-(BLOOD-bloodStatus)/2-3, 15, bloodStatus/2);
	}

}
