package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;

public class Spaceship extends FlyingObject {
	static boolean leftKey = false;
	public static boolean upKey = false;
	public static boolean rigthKey = false;
	public static boolean downKey = false;

	public Spaceship() {
		super(300, 260, World.WIDTH / 2 - 150, World.HEIGHT - 280);
	}

	@Override
	public void step() {

	}

	public void ssStep() {
		int type = 5;
		if (upKey && this.y > 333) {
			this.y -= type;
		} else if (leftKey && this.x > 0) {
			this.x -= type;
		} else if (rigthKey && this.x < World.WIDTH - width) {
			this.x += type;
		} else if (downKey && this.y < World.HEIGHT - height) {
			this.y += type;
		}

	}

	@Override
	public BufferedImage getImages() {
		if (isLife()) {
			return Images.spaceship;
		}
		return null;
	}

	/* 生成子弹 */
	public SpaceshipBullet[] generateTheBullet() {
		int i = 2;
		SpaceshipBullet[] tempSpaceshipBullet = { new SpaceshipBullet(this.width - i * 100, this.y + i),
				new SpaceshipBullet(this.width + i * 100, this.y + i) };

		return tempSpaceshipBullet;
	}

}
