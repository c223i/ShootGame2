package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

public class Boss extends FlyingObject {
	private Boss boss;
	private int hp;
	private int xSpeed, ySpeed;

	public Boss() {
		super(300, 225, World.WIDTH / 2 - 300 / 2, -225);
		hp = 250;
		xSpeed = 1;
		ySpeed = 3;
	}

	public void step() {
		if (y < 20) {
			y += ySpeed;
		} else {
			x += xSpeed;
		}
		if (x <= 0) {
			xSpeed += 1;
		} else if (this.x > World.WIDTH - this.width) {
			xSpeed -= 1;
		}
	}

	public BufferedImage getImages() {
		if (isLife()) {
			return Images.boss;
		} else if (isDead()) {
			state = REMOVE;
		}
		return null;
	}

	public BossBullet[] shoot() {
		int bulletAction = 0;
		FlyingObject[] bullet = {};
		int xstep = this.width / 8;
		int ystep = 5;
		BossBullet[] blt = {};
		blt = new BossBullet[28];
		for (int i = 0; i < blt.length; i++) {
			if (i <= 14 && i != 7 && i != 8 && i != 23 && i != 24 && i != 25) {
				blt[i] = new BossBullet(this.x + ((2 * i - 1) - 10) * xstep, this.height + 150 + ystep);
			} else if (i == 21 || i == 22 || i == 23 || i == 7 || i == 8) {
				blt[i] = new BossBullet(this.x + (15) * xstep, this.height + ystep);
			} else {
				blt[i] = new BossBullet(this.x + ((2 * i - 30) - 10) * xstep, this.height + ystep);
			}
			blt[i].step();
		}
		return blt;
	}

	public void subtracthp() {
		hp--;
		if (hp <= 0) {
			state = REMOVE;
		}
	}

	public int gethp() {
		return hp;
	}

	public int getScore() {
		return 500;
	}

	public int getAwardType() {
		return 0;
	}

	public int getHp() {
		return hp;
	}

	public void reduceHp(int heroLevel) {

		switch (heroLevel) { // 根据英雄机伤害等级判断扣多少血
		case 1:
			this.hp -= new Random().nextInt(5) + 5;
			break;
		case 2:
			this.hp -= new Random().nextInt(5) + 8;
			break;
		case 3:
			this.hp -= new Random().nextInt(5) + 10;
			break;
		case 4:
			this.hp -= new Random().nextInt(5) + 15;
			break;
		}

		if (this.hp <= 0) { // 扣到0就死亡
			this.goDead();
		}

	}

}
