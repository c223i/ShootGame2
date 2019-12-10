package cn.ccd.game.shoot2;

import java.awt.Graphics;
import java.util.Random;
import java.awt.image.BufferedImage;

/** 超类 */
public abstract class FlyingObject {

	/* 全局飞行物状态 */
	protected static final int LIFE = 0; // 存活
	protected static final int DEAD = 1; // 死亡
	protected static final int REMOVE = 2; // 删除

	/* 全局飞行物默认状态 */
	protected int state = LIFE; // 存活

	/* 全局飞行物基本数据 */
	protected int width;
	protected int height;
	protected int x;
	protected int y;

	/* 全局飞行物构造器1 */
	public FlyingObject(int width, int height, int x, int y) {

		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;

	}

	/* 全局飞行物构造器2 */
	public FlyingObject(int width, int height) {

		this.width = width;
		this.height = height;
		this.x = new Random().nextInt(World.WIDTH - this.width - 2) + 1;
		this.y = -this.height;

	}

	/* 全局飞行物移动方法 */
	public abstract void step();

	/* 全局飞行物存活状态判断 */
	public boolean isLife() {

		return state == LIFE;

	}

	/* 全局飞行物死亡状态判断 */
	public boolean isDead() {

		return state == DEAD;

	}

	/* 全局飞行物删除状态判断 */
	public boolean isRemove() {

		return state == REMOVE;

	}

	/* 全局飞行物返回图片(贴图) */
	public abstract BufferedImage getImages();

	/* 全局飞行物显示在窗口方法 */
	public void paintObject(Graphics g) {

		g.drawImage(this.getImages(), this.x, this.y, null);

	}

	/* 全局飞行物出界检测 */
	public boolean outOfBounds() {

		return this.y > World.HEIGHT;

	}

	/* 全局飞行物碰撞检测 */
	public boolean hit(FlyingObject other) {

		int x1 = this.x - other.width;
		int x2 = this.x + this.width;
		int y1 = this.y - other.height;
		int y2 = this.y + this.height;

		return other.x >= x1 && other.x <= x2 && other.y >= y1 && other.y <= y2;

	}

	/* 全局飞行物设定死亡状态方法 */
	public void goDead() {

		this.state = DEAD;

	}

}
