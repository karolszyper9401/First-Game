package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Heart {
	
	private Player player;
	
	private BufferedImage image;
	private Font font;
	
	public Heart(Player p) {
		player = p;
		try {
			//Downloading animation heart
			image = ImageIO.read(
				getClass().getResourceAsStream(
					"/Heart/heart.gif"
				)
			);
			//Select the appearance of the digits
			font = new Font("DejaVu Sans", Font.PLAIN, 15);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		
		//Drawing heart animation
		g.drawImage(image, 0, 4, null);
		g.setFont(font);
		
		//Color choice of digits
		g.setColor(Color.WHITE);
		
		g.drawString(
			player.getHealth() + "/" + player.getMaxHealth(),
			30,
			25
		);
		
		
	}
	
}













