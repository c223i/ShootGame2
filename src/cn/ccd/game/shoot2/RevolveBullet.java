package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;

public class RevolveBullet extends FlyingObject {
	private int life;

	public RevolveBullet(int x, int y) {
		super(30, 30, x, y);
		life = 10;
	}

	int index = 0;

	public void step() {// 旋转子弹的移动
		index++;
		switch (index / 2) {
		case 1:
			x += 21;
			y -= 13;
			break;

		case 2:
			x += 9;
			y -= 30;
			break;
		case 3:
			x -= 9;
			y -= 30;
			break;
		case 4:
			x -= 21;
			y -= 13;
			break;
		case 5:
			x -= 21;
			y += 9;
			break;
		case 6:
			x -= 9;
			y += 21;
			break;
		case 7:
			x += 9;
			y += 21;
			break;
		case 8:
			x += 42;
			y += 9;
			index = 0;
			break;
		default:
			break;
		}
	}

	@Override
	public BufferedImage getImages() {
		if (isLife()) {// 若是活着的走这里返回图片，结束getIMamge方法
			return Images.revolveb;
		} else if (isDead()) {// 若是死了，修改当前状态行为
			state = REMOVE;
		}
		return null;
	}

	/**
	 * 对子弹的生命进行减法运算
	 */
	public void subtractLife() {
		life--;
	}

	/**
	 * 返回一个生命值
	 * 
	 * @return
	 */
	public int getLife() {
		return life;
	}

	public boolean outOfBounds() {

		return this.y < -this.height * 4;

	}

}
