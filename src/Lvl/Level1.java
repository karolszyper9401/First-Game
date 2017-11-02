package Lvl;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.Heart;
import Entity.Player;
import Entity.Enemies.Soldier;
import GameState.GameState;
import GameState.GameStateManager;
import Main.GamePanel;
import TileMap.Background;
import TileMap.BlocksMap;

public class Level1 extends GameState {
	
	private BlocksMap blockMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private Heart heart;
	
	public Level1(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		blockMap = new BlocksMap(30);
		
		//Downloading map blocks
		blockMap.loadTiles("/Blocks/subsoil.gif");
		
		//Downloading map
		blockMap.loadMap("/Maps/testmap.map");
		blockMap.setPosition(0, 0);
		blockMap.setTween(1);
		
		//Downloading background lvl 1
		bg = new Background("/Backgrounds/backgroundlvl1.jpg", 0);
		
		//Setting the player's starting position
		player = new Player(blockMap);
		player.setPosition(90, 100);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		heart = new Heart(player);
		
	}
	
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		
		
		//Position and number of enemies
		Soldier s;
		Point[] points = new Point[] {
			new Point(150,100),
			new Point(200, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200),
			new Point(2650, 200),
			new Point(2750, 200)
		};
		for(int i = 0; i < points.length; i++) {
			s = new Soldier(blockMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
	}
	
	public void update() {
		
		//Update player
		player.update();
		blockMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		
		//Set background
		bg.setPosition(blockMap.getx(), blockMap.gety());
		
		//Attack enemies
		player.checkAttack(enemies);
		
		//Update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(
					new Explosion(e.getx(), e.gety()));
			}
		}
		
		//Update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		//Draw background
		bg.draw(g);
		
		//Draw block
		blockMap.draw(g);
		
		//Draw player
		player.draw(g);
		
		//Draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		//Draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
				(int)blockMap.getx(), (int)blockMap.gety());
			explosions.get(i).draw(g);
		}
		
		//Draw Heart
		heart.draw(g);
		
	}
	//Setting control
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setJumping(true);
		if(k == KeyEvent.VK_SPACE) player.setGliding(true);
		if(k == KeyEvent.VK_CONTROL) player.setFiring();
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setJumping(false);
		if(k == KeyEvent.VK_SPACE) player.setGliding(false);
	}
	
}












