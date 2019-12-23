package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;

/** 英雄机 */
public class Hero extends FlyingObject {

	private int life; // 生命数
	private int hp; // 生命值(血量)
	private int protectedCover; // 保护罩
	private int level; // 伤害
	private int fireMode; // 开火模式

	/* 构造器 */
	public Hero() {

		super(100, 100, World.WIDTH / 2 - 97 / 2, World.HEIGHT - 300);
		this.life = 3;
		this.hp = 100;
		this.level = 1;
		this.fireMode = 1;
		this.protectedCover = 0;

	}

	/* 移动方法1(弃用) */
	public void step() {
		// 空，不能删除此方法，因为超类中有且还剩抽象方法必须重写
	}

	/* 移动方法2-鼠标控制 */
	public void moveTo(int x, int y) {

		this.x = x - this.width / 2;
		this.y = y - this.height / 2;

	}

	private int index = 0; // getImages()方法的图片切换指示
	/* 返回图片 */

	public BufferedImage getImages() {

		return Images.heros[index++ % Images.heros.length];

	}

	/* 生成子弹 */
	public Bullet[] generateTheBullet() {

		int x = (this.width / 6) - 1;// 把英雄机宽分成6个值，-1是强迫症微调

		Bullet[] tempBullet;
		switch (this.fireMode) {// 根据开火模式生成特定子弹结构

		default:// 普通模式

			tempBullet = new Bullet[1];
			tempBullet[0] = new Bullet(this.x + x * 3, this.y, 0, level);
			return tempBullet;

		case 2:// 双开火模式

			tempBullet = new Bullet[2];
			tempBullet[0] = new Bullet(this.x + x * 2, this.y, 0, level);
			tempBullet[1] = new Bullet(this.x + x * 4, this.y, 0, level);
			return tempBullet;

		case 3:// 三开火模式

			tempBullet = new Bullet[3];
			tempBullet[0] = new Bullet(this.x + x * 1, this.y, 0, level);
			tempBullet[1] = new Bullet(this.x + x * 3, this.y, 0, level);
			tempBullet[2] = new Bullet(this.x + x * 5, this.y, 0, level);
			return tempBullet;

		case 4:// 五开火模式

			tempBullet = new Bullet[] { new Bullet(this.x + x * 1, this.y, 1, level),
					new Bullet(this.x + x * 2, this.y, 0, level), new Bullet(this.x + x * 3, this.y, 0, level),
					new Bullet(this.x + x * 4, this.y, 0, level), new Bullet(this.x + x * 5, this.y, -1, level) };
			return tempBullet;

		case 5:// 超级开火模式(大型散弹)

			tempBullet = new Bullet[7];
			for (int i = 0, j = tempBullet.length / 2; i < tempBullet.length; i++) {
				tempBullet[i] = new Bullet(this.x + x * 3, this.y, j--, level);
			}
			return tempBullet;
		}

	}

	// ---------------------------------血量方法---------------------------------
	/* 加血 */
	public synchronized void addHp() {

		this.hp += 50;

		if (this.hp > 100) { // 上限100

			this.hp = 100;

		}

	}

	/* 扣血 */
	public synchronized void subtractHp() {

		this.hp -= 10; // 一次10血

		if (this.hp <= 0) {// 如果扣到0则扣生命数换一条血

			subtractLife();
			this.hp = 100;

		}

	}

	/* 返回血量 */
	public int getHp() {

		return hp;

	}

	// ---------------------------------生命数量方法---------------------------------
	/* 加命 */
	public void addLife() {

		this.life++;

	}

	/* 减命 */
	public synchronized void subtractLife() {

		if (this.life > 0) { // 防止扣到负数

			this.life--;

		}

	}

	/* 返回生命数 */
	public int getLife() {

		return this.life;

	}

	// ---------------------------------伤害等级方法---------------------------------
	/* 增加伤害等级 */
	public synchronized void addLevel() {

		if (this.level < 4) { // 防止超过4

			this.level++;

		}

	}

	/* 减少伤害等级 */
	public synchronized void subtractLevel() {

		if (this.level > 1) { // 防止扣到0

			this.level--;

		}

	}

	/* 返回伤害等级 */
	public int getLevel() {

		return level;

	}

	// ---------------------------------开火模式---------------------------------
	/* 升级开火模式 */
	public synchronized void addFireMode() {

		if (this.fireMode < 5) { // 防止超过5

			this.fireMode++;

		}

	}

	/* 降级开火模式 */
	public synchronized void subtractFireMode() {

		if (this.fireMode > 1) { // 防止扣到0

			this.fireMode--;

		}

	}

	/* 返回开火 */
	public int getFireMode() {

		return fireMode;

	}

	// ---------------------------------保护罩---------------------------------
	/* 获得保护罩能量 */
	public synchronized void addProtectedCover() {

		this.protectedCover += 100;

		if (this.protectedCover > 100) {// 封顶100

			this.protectedCover = 100;

		}

	}

	/* 减少保护罩能量 */
	public synchronized void subtractProtectedCover() {

		if (this.protectedCover > 0) { // 防止扣到0

			this.protectedCover--;

		}

	}

	/* 返回保护罩能量值 */
	public int getProtectedCoverNum() {

		return protectedCover;

	}

}
