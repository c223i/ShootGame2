package cn.ccd.game.shoot2;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/** 背景(天空) */
public class Sky extends FlyingObject {

	private int speed;
	private int y2; // 第二张背景图y坐标
	private int type;// 天空背景贴图标记

	/* 构造器 */
	public Sky() {

		super(World.WIDTH, World.HEIGHT, 0, 0);
		this.speed = 1;
		this.y2 = -this.height;
		this.type = new Random().nextInt(4);// 随机天空背景贴图(0-3)

	}

	/* 天空移动方法 */
	public void step() {

		this.y += speed;
		this.y2 += speed;

		if (y >= World.HEIGHT) {

			y = -World.HEIGHT;

		}

		if (y2 >= World.HEIGHT) {

			y2 = -World.HEIGHT;

		}

	}

	/* 返回图片 */
	public BufferedImage getImages() {

		return Images.sky[this.type];// 根据Type返回对应贴图，

	}

	/* 天空重画方法 */
	public void paintObject(Graphics g) {

		g.drawImage(this.getImages(), this.x, this.y, null);
		g.drawImage(this.getImages(), this.x, this.y2, null);

	}

}
