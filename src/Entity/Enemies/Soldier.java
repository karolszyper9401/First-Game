package Entity.Enemies;

import Entity.*;
import TileMap.BlocksMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class Soldier extends Enemy {
	
	private BufferedImage[] sprites;
	
	public Soldier(BlocksMap tm) {
		
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 20.0;
		
		width = 20;
		height = 22;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = 2;
		damage = 1;
		
		//Download animated soldier
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/soldier.gif"
				)
			);
			
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
		
	}
	
	private void getNextPosition() {
		
		//Movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		//Falling
		if(falling) {
			dy += fallSpeed;
		}
		
	}
	
	public void update() {
		
		//Update position
		getNextPosition();
		checkBlockMapCollision();
		setPosition(xtemp, ytemp);
		
		//Check lossHp
		if(lossHp) {
			long elapsed =
				(System.nanoTime() - lossHpTimer) / 1000000;
			if(elapsed > 400) {
				lossHp = false;
			}
		}
		
		//If it hits a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		//Update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		//if(notOnScreen()) return;
		
		//Positioning on the map
		setMapPosition();
		
		super.draw(g);
		
	}
	
}











