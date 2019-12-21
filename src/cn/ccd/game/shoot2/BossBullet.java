package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.Arrays;

public class BossBullet extends FlyingObject{
	
	private static BufferedImage image;
	private int speed;
	
	public BossBullet(int x,int y) {
		super(24,95,x,y);
		speed = 4;
	}
		
	public void step() { 
		y += speed;
	}
	
	public BufferedImage getImages() {
		if(isLife()) {
			return Images.bossbullet;
		}else {
			state = REMOVE;
		}
		return null;
	}
	
	public boolean outOfBounds() {

		return this.y < -this.height || this.x > World.WIDTH || this.x < -this.width;

	}

}
