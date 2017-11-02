package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Menu extends GameState {

	private Background bg;

	private int currentChoice = 0;

	// Menu options
	private String[] options = { "Start", "Help", "Quit" };

	private Color blockColor;
	private Font blockFont;

	private Font font;

	public Menu(GameStateManager gsm) {

		this.gsm = gsm;

		try {
			
			// Background menu download
			bg = new Background("/Backgrounds/menubg.jpg", 1);
			bg.setVector(0, 0);

			// Setting the font
			blockColor = new Color(128, 0, 0);
			blockFont = new Font("Arial", Font.PLAIN, 30);

			font = new Font("Arial", Font.PLAIN, 15);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void init() {
	}

	public void update() {
		bg.update();
	}

	public void draw(Graphics2D g) {

		// Drawing background
		bg.draw(g);

		// Drawing title
		g.setColor(blockColor);
		g.setFont(blockFont);
		g.drawString("First Game", 100, 80);

		// Draw menu options
		g.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.GRAY);
			}
			g.drawString(options[i], 145, 140 + i * 20);
		}

	}

	private void select() {
		if (currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if (currentChoice == 1) {
			// help
		}
		if (currentChoice == 2) {
			System.exit(0);
		}
	}

	//Navigating the menus
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}

	public void keyReleased(int k) {
	}

}
