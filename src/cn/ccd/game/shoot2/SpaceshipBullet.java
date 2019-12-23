package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;

public class SpaceshipBullet extends FlyingObject {
	private int spleed;

	public SpaceshipBullet(int x, int y) {
		super(68, 300, x, y);
		spleed = 10;
	}

	@Override
	public void step() {
		if(isLife())
		this.y -= spleed;
	}
	public void shotStep() {
		
	}

	private int index = 0;

	@Override
	public BufferedImage getImages() {
		if (isLife()) {
			return Images.spaceshipBullet[index++ % Images.spaceshipBullet.length];
		}
		return null;
	}

}
