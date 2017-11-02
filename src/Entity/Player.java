package Entity;

import TileMap.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject {
	
	//Attributes player
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean lossHp;
	private long lossHpTimer;
	
	//Fireball
	private boolean firing;

	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	//Hit
	private boolean hit;
	private int hitDamage;
	private int hitRange;
	
	//Mega jump
	private boolean megaJump;
	
	//Player animations 
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		1, 1, 1, 1, 1, 1, 1
	};
	
	//Animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int FALLING = 2;
	private static final int JUMPING = 3;
	private static final int FIREBALL = 4;
	private static final int MEGA_JUMP = 5;
	private static final int HIT = 6;
	
	public Player(BlocksMap bM) {
		//Player specification
		super(bM);
		
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		
		hitDamage = 8;
		hitRange = 40;
		
		// Downloading a player animation
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/test_animation.gif"
				)
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i++) {
				
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
					
					if(i != HIT) {
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,
								i * height,
								width * 2,
								height
						);
					}
					
				}
				
				sprites.add(bi);
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	public void setFiring() { 
		firing = true;
	}
	public void setScratching() {
		hit = true;
	}
	public void setGliding(boolean b) { 
		megaJump = b;
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			//Attack loop
			if(hit) {
				if(facingRight) {
					if(
						e.getx() > x &&
						e.getx() < x + hitRange && 
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(hitDamage);
					}
				}
				else {
					if(
						e.getx() < x &&
						e.getx() > x - hitRange &&
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(hitDamage);
					}
				}
			}
			
			//Fireballs
			for(int j = 0; j < fireBalls.size(); j++) {
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			
			//Check enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
			
		}
		
	}
	//Injury inflicted
	public void hit(int damage) {
		if(lossHp) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		lossHp = true;
		lossHpTimer = System.nanoTime();
	}
	
	private void getNextPosition() {
		
		//Movement player
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
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		//Cannot move while attacking, except in air
		if(
		(currentAction == HIT || currentAction == FIREBALL) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		//Jumping
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;	
		}
		
		//Falling
		if(falling) {
			
			if(dy > 0 && megaJump) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	
	public void update() {
		
		//Update position
		getNextPosition();
		checkBlockMapCollision();
		setPosition(xtemp, ytemp);
		
		//Check attack has stopped
		if(currentAction == HIT) {
			if(animation.hasPlayedOnce()) hit = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		//Fireball attack
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
				FireBall fb = new FireBall(blockMap, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		
		
		//Update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		//Check done flinching
		if(lossHp) {
			long elapsed =
				(System.nanoTime() - lossHpTimer) / 1000000;
			if(elapsed > 1000) {
				lossHp = false;
			}
		}
		
		//Set animation
		if(hit) {
			if(currentAction != HIT) {
				currentAction = HIT;
				animation.setFrames(sprites.get(HIT));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy > 0) {
			if(megaJump) {
				if(currentAction != MEGA_JUMP) {
					currentAction = MEGA_JUMP;
					animation.setFrames(sprites.get(MEGA_JUMP));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		animation.update();
		
		//Positioning
		if(currentAction != HIT && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		//Draw fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		//Draw player
		if(lossHp) {
			long elapsed =
				(System.nanoTime() - lossHpTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
		
	}
	
}
















