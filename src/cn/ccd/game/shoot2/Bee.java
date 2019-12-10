package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;
import java.util.Random;

/** 空投(源小蜜蜂) */
public class Bee extends FlyingObject implements Award, Enemy {

	private int xSpeed;
	private int ySpeed;
	private int ranX;// X轴初始方向标记
	private int awardType; // 奖励类型

	/* 构造器 */
	public Bee() {

		super(60, 103);
		ranX = new Random().nextInt(2);
		this.xSpeed = 1;
		this.ySpeed = 2;
		this.awardType = new Random().nextInt(5);// 随机奖励值类型(0-4)

	}

	/* 移动方法 */
	public void step() {

		this.y += ySpeed;

		if (ranX == 0) {

			this.x += this.xSpeed;// 往右

		} else {

			this.x -= this.xSpeed;// 往右

		}

		/* 边缘检测 */
		if (this.x <= 0 || this.x >= (World.WIDTH - this.width)) {

			this.xSpeed *= -1;// 触碰边缘往反方向移动(X轴)

		}

	}

	/* 返回图片 */
	public BufferedImage getImages() {

		if (isLife()) {

			switch (awardType) { // 根据awardType返回对应贴图，并重新设置空投宽高(个别奖励贴图宽高不同)

			case ADD_LIFE:

				this.width = 73;
				this.height = 67;
				return Images.bees[1]; // 返回心型贴图

			case PROTECTED_COVER:

				this.width = 60;
				this.height = 60;
				return Images.bees[2]; // 返回保护罩贴图

			case BULLRT_LEVEL_UP:

			case HERO_LEVEL_UP:

			case INCREASE_HP:

				return Images.bees[0]; // 返回普通贴图

			}

		} else if (isDead()) {

			state = REMOVE;

		}

		return null;

	}

	/* 返回奖励类型 */
	public int getAwardType() {

		return awardType;

	}

	/* 返回随机分数 */
	public int getScore() {
		return 5;
	}

}
