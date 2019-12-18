package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;
import java.util.Random;

/** 小敌机 */
public class Airplane extends FlyingObject implements Enemy, EnemiesHp {

	private int xSpeed;
	private int ySpeed;
	private int ranX;// X轴初始方向标记
	private int AirplaneType; // 小敌机种类，我们有4种贴图
	private int hp;// 血量

	/* 构造器 */
	public Airplane() {

		super(60, 40);
		ranX = new Random().nextInt(2);// 0-1
		this.xSpeed = 1;
		this.ySpeed = 2;
		this.AirplaneType = new Random().nextInt(4); // 随机贴图
		this.hp = airplaneHp; // 血量

	}

	/* 移动方法 */
	public void step() {

		this.y += this.ySpeed;

		if (ranX == 0) {

			this.x += this.xSpeed; // 往右

		} else {

			this.x -= this.xSpeed; // 往左

		}

		/* 边缘检测 */
		if (this.x <= 0 || this.x >= World.WIDTH - this.width) {

			this.xSpeed *= -1;// 触碰边缘往反方向移动(X轴)

		}

	}

	/* 返回图片 */
	public BufferedImage getImages() {

		if (isLife()) {

			return Images.airplanes[this.AirplaneType]; // 根据AirplaneType返回对应贴图

		} else if (isDead()) {

			state = REMOVE;

		}

		return null;

	}

	/* 返回随机分数 */
	public int getScore() {

		return new Random().nextInt(3) + 1; // 1-3

	}

	/* 返回血量 */
	public int getHp() {

		return hp;

	}

	/* 敌机扣血方法 */
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
