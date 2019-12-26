package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceshipBullet extends FlyingObject implements EnemiesHp{
	private int spleed;
	private int hp;

	public SpaceshipBullet(int x, int y) {
		super(68, 300, x, y);
		spleed = 10;
		hp=2;
	}
	int i=0;
	@Override
	public void step() {
		if (isLife()&&i++>100)
			this.y -= spleed;
	}
	@Override
	public int getHp() {
		
		return hp;
	}

	@Override
	public void reduceHp(int heroLevel) {
		hp--;
		
	}

	public void shotStep() {

	}

	private int index = 0;

	@Override
	public BufferedImage getImages() {
		if (hp>=0) {
			return Images.spaceshipBullet[index++ % Images.spaceshipBullet.length];
		}
		return null;
	}

	/* 发射散花子弹子弹 */
	public Shot[] generateShot() {
		Shot[] st = new Shot[16];// -40=0=+40
		for (int i = 0, j = 80; i < st.length; i++, j -= 5) {
			if (i < 4) {// 15 0
				st[i] = new Shot(this.x + i * 5, this.y + (i * 5 - j / 4), i);
			} else if (i < 8) {// 0 20
				st[i] = new Shot(this.x + (j / 2 - i * 5), this.y + (i * 5 - j / 4), i);
			} else if (i < 12) {// -15 0
				st[i] = new Shot(this.x + (j / 2 - i * 5), this.y + (-i * 5+ j / 4 * 3), i);

			} else if (i < 16) {// 0 -20
				st[i] = new Shot(this.x + (i * 5-j), this.y +(-i * 5 + j / 4 * 3), i);

			}
		}
		return st;
	}

	public static void main(String[] args) {
		List<String> list=new ArrayList<String>();
		list.add("北京");
		list.add("上海");
		int i=0;
		Iterator<String> it=list.iterator();
		while (it.hasNext()) {
			i++;
//			String string = (String) it.next();
			System.out.println(it.next());
		}
		System.out.println(i);
	/*	for (int i = 0, j = 80; i < 16; i++) {
			if (i < 4) {
				System.out.println("第一象限");
				System.out.print("x=" + i * 5);
				System.out.println("y=" + (i * 5 - j / 4));
			} else if (i < 8) {
				System.out.println("第二象限");
				System.out.print("x=" + (j / 2 - i * 5));
				System.out.println("y=" + (i * 5 - j / 4));

			} else if (i < 12) {
				System.out.println("第三象限");
				System.out.print("x=" + (j / 2 - i * 5));
				System.out.println("y=" + (-i * 5+ j / 4 * 3));

			} else if (i < 16) {
				System.out.println("第四象限");
				System.out.print("x=" + (i * 5-j));
				System.out.println("y=" + (-i * 5 + j / 4 * 3));

			}

		}*/
	}

	

}
