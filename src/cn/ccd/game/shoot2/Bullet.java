package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;

/** 英雄机子弹 */
public class Bullet extends FlyingObject {

	private int ySpeed;
	private int xSpeed;
	private int bulletType; // 子弹贴图

	/* 构造器 */
	public Bullet(int x, int y, int xSpeed, int heroLevel) {

		super(7, 21, x, y);
		this.ySpeed = 6;
		this.xSpeed = xSpeed;
		this.bulletType = heroLevel; // 根据英雄机伤害等级设定子弹贴图

	}

	/* 移动方法 */
	public void step() {

		this.y -= this.ySpeed;
		if(this.bulletType!=3&&World.SpaceshipFlag) {
			this.x -= this.xSpeed;
		}
		

	}

	/* 返回图片 */
	public BufferedImage getImages() {

		if (isLife()) {

			switch (bulletType) { // 根据bulletType设定子弹贴图，并重新设置空投宽高(每个子弹类型贴图宽高不同)
			case 1:
				return Images.bullet[bulletType - 1];
			case 2:
				this.width = 13;
				this.height = 30;
				this.ySpeed = 7;
				return Images.bullet[bulletType - 1];
			case 3:
				this.width = 10;
				this.height = 42;
				this.ySpeed = 8;
				if(World.SpaceshipFlag) {
				return Images.bullet[bulletType];
				}
				return Images.bullet[bulletType - 1];
			case 4:
				this.width = 19;
				this.height = 40;
				this.ySpeed = 10;
				return Images.bullet[bulletType - 1];
			}

		} else if (isDead()) {

			state = REMOVE;

		}

		return null;

	}

	/* 子弹出界检测 */
	public boolean outOfBounds() {

		return this.y < -this.height || this.x > World.WIDTH || this.x < -this.width;

	}

}
