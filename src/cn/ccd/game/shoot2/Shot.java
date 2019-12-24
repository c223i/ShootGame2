package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;

public class Shot extends FlyingObject {
	private int quadrant;

	public Shot(int x, int y, int quadrant) {
		super(30, 30, x, y);
		this.quadrant = quadrant;
	}

	@Override
	public void step() {
		
		switch (quadrant) {
		case 0:
			this.y += 4;
			break;
		case 1:
			this.x += 2;
			this.y += 6;
			break;
		case 2:
			this.x += 4;
			this.y += 4;
			break;
		case 3:
			this.x += 6;
			this.y += 2;
			break;
		case 4:
			this.x +=4;
			break;
		case 5:
			this.x += 6;
			this.y -= 2;
			break;
		case 6:
			this.x += 4;
			this.y -= 4;
			break;
		case 7:
			this.x +=2;
			this.y -= 6;
			break;
		case 8:
			this.y -= 4;
			break;
		case 9:
			this.x -= 2;
			this.y -= 6;
			break;
		case 10:
			this.x -= 4;
			this.y -= 4;
			break;
		case 11:
			this.x -= 6;
			this.y -= 2;
			break;
		case 12:
			this.x -= 4;
			break;
		case 13:
			this.x -= 6;
			this.y += 2;
			break;
		case 14:
			this.x -= 4;
			this.y += 4;
			break;
		case 15:
			this.x -= 2;
			this.y += 6;
			break;

		default:
			break;
		}
	}

	@Override
	public BufferedImage getImages() {
		if (isLife()) {
			return Images.shot;
		}
		return null;
	}

}
