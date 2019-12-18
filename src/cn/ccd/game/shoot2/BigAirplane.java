package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;
import java.util.Random;

/** 大敌机 */
public class BigAirplane extends FlyingObject implements Enemy, EnemiesHp {

	private int speed;
	private int BigAirplaneType; // 大敌机贴图，我们有3种贴图
	private int hp; // 血量

	/* 构造器 */
	public BigAirplane() {

		super(120, 100);
		this.speed = 2;
		this.BigAirplaneType = new Random().nextInt(3); // 随机贴图
		this.hp = bigAirplaneHp; // 血量

	}

	/* 移动方法 */
	public void step() {

		this.y += this.speed;

	}

	/* 返回图片 */
	public BufferedImage getImages() {

		if (isLife()) {

			return Images.bigAirplanes[this.BigAirplaneType]; // 根据BigAirplaneType返回对应贴图

		} else if (isDead()) {

			state = REMOVE;

		}

		return null;

	}

	/* 返回随机分数 */
	public int getScore() {

		return new Random().nextInt(3) + 3; // 3-5

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

		if (this.hp < 0) { // 扣到0就死亡
			this.goDead();
		}

	}

}
