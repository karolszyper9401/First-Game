package Entity;

import TileMap.BlocksMap;

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	
	protected boolean lossHp;
	protected long lossHpTimer;
	
	public Enemy(BlocksMap bM) {
		super(bM);
	}
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	//Injury inflicted
	public void hit(int damage) {
		if(dead || lossHp) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		lossHp = true;
		lossHpTimer = System.nanoTime();
	}
	
	public void update() {}
	
}














