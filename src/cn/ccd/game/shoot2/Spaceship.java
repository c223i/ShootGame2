package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
		int type = 8;
		if (upKey && this.y > 333) {
			this.y -= type;
		} else if (leftKey && this.x > -this.width / 2) {
			this.x -= type;
		} else if (rigthKey && this.x < World.WIDTH - (this.width / 2)) {
			this.x += type;
		} else if (downKey && this.y < World.HEIGHT - (this.height / 2)) {
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

	/* 生成巡航导弹 */
	public List<SpaceshipBullet> generateTheBullet() {
		int i = 2;
		List<SpaceshipBullet> temp = new ArrayList<SpaceshipBullet>();
		temp.add(new SpaceshipBullet(this.width - i * 50, this.y - i * 50));
		temp.add(new SpaceshipBullet(this.width + i * 50, this.y - i * 50));
		return temp;

	}

}
