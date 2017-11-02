package Entity;

import Main.GamePanel;
import TileMap.BlocksMap;
import TileMap.Blocks;

import java.awt.Rectangle;

public abstract class MapObject {
	
	//Blocks
	protected BlocksMap blockMap;
	protected int blockSize;
	protected double xmap;
	protected double ymap;
	
	//Position and direction
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	//Dimensions
	protected int width;
	protected int height;
	
	//Collision box
	protected int cwidth;
	protected int cheight;
	
	//Collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	//Animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	//Movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	//Movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	//Map maker
	public MapObject(BlocksMap bM) {
		blockMap = bM;
		blockSize = bM.getTileSize(); 
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)x - cwidth,
				(int)y - cheight,
				cwidth,
				cheight
		);
	}
	
	public void calculateCorners(double x, double y) {
		
		int leftTile = (int)(x - cwidth / 2) / blockSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / blockSize;
		int topTile = (int)(y - cheight / 2) / blockSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / blockSize;
		
		int tl = blockMap.getType(topTile, leftTile);
		int tr = blockMap.getType(topTile, rightTile);
		int bl = blockMap.getType(bottomTile, leftTile);
		int br = blockMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Blocks.BLOCKED;
		topRight = tr == Blocks.BLOCKED;
		bottomLeft = bl == Blocks.BLOCKED;
		bottomRight = br == Blocks.BLOCKED;
		
	}
	//Collision checking
	public void checkBlockMapCollision() {
		
		currCol = (int)x / blockSize;
		currRow = (int)y / blockSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		//Calculating the corner of a block
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * blockSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * blockSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * blockSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * blockSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * blockSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * blockSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
		
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {
		xmap = blockMap.getx();
		ymap = blockMap.gety();
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
		}
	}
	
}



















