package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;

/** 英雄机保护罩 */
public class ProtectedCover extends FlyingObject {

	public ProtectedCover() {
		super(220, 220);
		this.state = REMOVE; // 保护罩初始化为关闭(删除)状态

	}

	public void step() {

	};

	/* 移动方法-跟随鼠标 */
	public void moveTo(int x, int y) {

		this.x = x - this.width / 2;
		this.y = y - this.height / 2;

	}

	private int index = 0; // getImages()方法的图片切换指示
	/* 返回图片 */

	public BufferedImage getImages() {

		if(World.SpaceshipFlag) {
			return Images.pt[index++%Images.pt.length];
			
		}else if (isLife()) {

			return Images.ProtectedCover[index++ % Images.ProtectedCover.length];

		} else if (isDead()) {

			state = REMOVE;

		}

		return null;

	}

	/* 开启保护罩 */
	public void restartProtectedCover() {

		state = LIFE;

	}

}
