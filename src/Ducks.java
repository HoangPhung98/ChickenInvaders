import java.awt.Point;

public class Ducks {
	
	 public  int xCoordinate ;
	 public  int yCoordinate ;
	 public  int xdirection ;
	 public  int ydirection ;
	 public  boolean isAlive ;
	 public  boolean isGone ;
	 public  boolean canFlyAway ;
	 public  int  hitWallTimes ;
	 private final int FROM_LEFT_WALL = 0;
	 private final int FROM_BOTTOM_WALL = 1;
	 private final int FROM_RIGHT_WALL= 2;
	 void addDuck(){
			this.yCoordinate = getCoordinateOfDuck().y;
			this.xCoordinate = getCoordinateOfDuck().x;
			setRamdomDirection();
			
			
	}
	void updateDuckPosition(){
			this.xCoordinate += this.xdirection;
			this.yCoordinate += this.ydirection;
			checkHitWall();
		}
	 private  Point getCoordinateOfDuck(){
			
			int y=20;
			int x = StdRandom.uniform(20, GameFrame.getWidth()-20);
			if(StdRandom.uniform(3)==FROM_LEFT_WALL) {
				x=20;
				y = StdRandom.uniform(20,GameFrame.HEIGHT-20);
			}
			if(StdRandom.uniform(3)==FROM_BOTTOM_WALL) {
				y=20;
				x = StdRandom.uniform(20, GameFrame.getWidth()-20);
			}
			if(StdRandom.uniform(3)==FROM_RIGHT_WALL) {
				x=GameFrame.WIDTH-20;
				y = StdRandom.uniform(20,GameFrame.HEIGHT-20);
			}
			
		
			return new Point(x, y);
		
		}
		private  void setRamdomDirection(){
			if(StdRandom.uniform(0,2)==0){
				this.xdirection=-50;
			}else this.xdirection = 50;
			if(StdRandom.uniform(0,2)==0){
				this.ydirection = -50;
			}else this.ydirection = 50;
		}
		void setDuckDead(){
			this.xdirection =0;
			this.ydirection =-100;
		}
		private void checkHitWall(){
			// if the duck hit the wall 4 times then it can fly away
			if(this.yCoordinate +20> GameFrame.getHeight()){
				if(!checkHit4Times(this)){
					this.ydirection *=-1;
					this.hitWallTimes++;
				}else{
					this.isGone = true;
				}
			}
			
			//the duck is turn back if hit the wall
			if(this.xCoordinate +20> GameFrame.getWidth() || this.xCoordinate -20< 0) {
				this.xdirection *=-1;
				this.hitWallTimes ++;
			}
			if( this.yCoordinate-20< 0) {
				this.ydirection *=-1;
				this.hitWallTimes ++;
			}
		}
		private boolean checkHit4Times(Ducks duck){
			if(duck.hitWallTimes>4) return true;
			else return false;
		}
	
	
}
