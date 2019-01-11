package myFirstGame;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
public class Rock extends Polygon{
	
	//upper left hand corner of the polygon..
	int uLeftXPos, uLeftYPos;
	
	//used to change the directions of the asteroid when
	//it hits something and determines how fast it moves..
	
	int xDirection;
	int yDirection;
	
	//For JApplet
	//int width =  ExampleBoard.WIDTH;
	//int height = ExampleBoard.HEIGHT;
	
	//get the board width and height
	
	int width = GameBoard.boardWidth;
	int height = GameBoard.boardHeight;
	
	//will hold the x and y coordinates for the polygon..
	
	//int[] polyXArray, polyYArray;
	
	//x and y positions available for other methods
	//there will be more polygons points available later
	
	public static int[] sPolyXArray = {10, 17, 26, 34, 27, 36, 26, 14, 8, 1, 5, 1};
	public static int[] sPolyYArray = {0, 5, 1, 8, 13, 20, 31, 28, 31, 22, 16, 7};
	
	public int rockHeight = 31;
	public int rockWidth = 35;
	
	public boolean onScreen = false;
	
	
	static ArrayList<Rock> rocks = new ArrayList<Rock>();
	//creates a new asteroid..
	
	public Rock(int[] polyXArray, int[] polyYArray, int pointsInPoly, int randomStartXPos, int randomStartYPos) {
		
		
		//creates a polygon by calling the super or parent class constructer of Rock : Polygon
		super(polyXArray, polyYArray, pointsInPoly);
		
		onScreen = true;
		//Randomly generate a speed for the polygon
		this.xDirection = (int)(Math.random()*3 + 1);
		this.yDirection = (int)(Math.random()*3 + 1);
		
		//holds the starting x and y positions for the rock.
		this.uLeftXPos = randomStartXPos;
		this.uLeftYPos = randomStartYPos;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(super.xpoints[0], super.ypoints[0], rockWidth, rockHeight);
	}
	public void move(SpaceShip theShip, ArrayList<PhotonTorpedo> torpedos) {
		
		Rectangle rockToCheck = this.getBounds();
		
		for(Rock rock : rocks) {
			
		 if(rock.onScreen) {	
			 
			 
			 
			 
			 
			 
			 
			Rectangle otherRock = rock.getBounds();
			if(rock != this && otherRock.intersects(rockToCheck)) {
				int tempXDirection = this.xDirection;
				int tempYDirection = this.yDirection;
				
				this.xDirection = rock.xDirection;
				this.yDirection = rock.yDirection;
				
				rock.xDirection = tempXDirection;
				rock.yDirection = tempYDirection;
			}
			
			Rectangle shipBox = theShip.getBounds();
			if(otherRock.intersects(shipBox)) {
				theShip.setXCenter(theShip.gbWidth/2);
				theShip.setYCenter(theShip.gbHeight/2);
				theShip.setXVelocity(0);
				theShip.setYVelocity(0);
				
			}
			
			for(PhotonTorpedo torpedo : torpedos) {
				if(torpedo.onScreen) {
					if(otherRock.contains(torpedo.getXCenter(), torpedo.getYCenter())) {
						rock.onScreen = false;GameBoard.count--;System.out.println(GameBoard.count);
						torpedo.onScreen = false;
						
					}
				}
			}
		}
		 
		
	}	
		//get the upper left and the top most point for the polygon 
		//this will be dynamic later on
		
		int uLeftXPos = super.xpoints[0];//System.out.println(super.xpoints[0]);
		int uLeftYPos = super.ypoints[0];//System.out.println(super.ypoints[0]);
		
		//if the rock hits a wall it will go in the opposite direction.
		
		if(uLeftXPos < 0 || uLeftXPos + 25 > width)xDirection = -xDirection;
		if(uLeftYPos < 0 || uLeftYPos + 25 > height)yDirection = -yDirection;
		
		//change the values of the points for the polygon..
		for(int i=0;i < super.xpoints.length;i++) {
			super.xpoints[i] += xDirection;
			super.ypoints[i] += yDirection;
		}
		
	}
	
	//public method available for creating Polygon xpoint arrays
	public static int[] getPolyXArray(int randomStartXPos) {
		//clones the array so that the original shape isn't changed for the asteroid
		int[] tempPolyXArray = (int[])sPolyXArray.clone();
		for(int i = 0;i < tempPolyXArray.length;i++) {
			tempPolyXArray[i] += randomStartXPos;
		}
		
		return tempPolyXArray;
	}
		
	//public method available for creating Polygon ypoint arrays
	public static int[] getPolyYArray(int randomStartYPos) {
		//clones the array so that the original shape isn't changed for the asteroid
		int[] tempPolyYArray = (int[])sPolyYArray.clone();
		for(int i = 0;i < tempPolyYArray.length;i++) {
			tempPolyYArray[i] += randomStartYPos;
		}
		
		return tempPolyYArray;
	}
}

