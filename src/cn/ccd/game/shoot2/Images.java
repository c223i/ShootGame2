package cn.ccd.game.shoot2;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/** 游戏贴图 */
public class Images {
	
	public static BufferedImage[] sky; // 背景
	public static BufferedImage[] bullet; // 英雄机子弹
	public static BufferedImage[] heros; // 英雄机
	public static BufferedImage[] airplanes; // 小敌机
	public static BufferedImage[] bigAirplanes; // 大敌机
	public static BufferedImage[] bees; // 空投
	public static BufferedImage start; // 游戏等待开始
	public static BufferedImage pause; // 游戏暂停
	public static BufferedImage gameOver; // 游戏结束
	public static BufferedImage[] ProtectedCover; // 英雄机保护罩
	public static BufferedImage revolveb;	//旋转子弹的图片
	
	static {

		
		sky = new BufferedImage[6];
		for (int i = 0; i < sky.length; i++) {
			sky[i] = loadImage(new File("./src/cn/ccd/game/shoot2/material/background/bg_" + i + ".jpg"));
		}

		bullet = new BufferedImage[4];
		for (int i = 0; i < bullet.length; i++) {
			bullet[i] = loadImage(new File("./src/cn/ccd/game/shoot2/material/bullet/bullet" + i + ".png"));
		}
		
		heros = new BufferedImage[] { 
				loadImage(new File("./src/cn/ccd/game/shoot2/material/player/hero0.png")), 
				loadImage(new File("./src/cn/ccd/game/shoot2/material/player/hero1.png")), 
		};

		airplanes = new BufferedImage[4];
		for (int i = 0; i < airplanes.length; i++) {
			airplanes[i] = loadImage(new File("./src/cn/ccd/game/shoot2/material/Enemy/airplane" + i + ".png"));
		}

		bigAirplanes = new BufferedImage[3];
		for (int i = 0; i < bigAirplanes.length; i++) {
			bigAirplanes[i] = loadImage(new File("./src/cn/ccd/game/shoot2/material/Enemy/bigplane" + i + ".png"));
		}

		bees = new BufferedImage[5];
		for (int i = 0; i < bees.length; i++) {
			bees[i] = loadImage(new File("./src/cn/ccd/game/shoot2/material/Bee/bee_" + i + ".png"));
		}

		ProtectedCover = new BufferedImage[4];
		for (int i = 0; i < ProtectedCover.length; i++) {
			ProtectedCover[i] = loadImage(new File("./src/cn/ccd/game/shoot2/material/ProtectedCover/clean" + i + ".png"));
		}

		start = loadImage(new File("./src/cn/ccd/game/shoot2/material/game_state/start_1.png"));
		pause = loadImage(new File("./src/cn/ccd/game/shoot2/material/game_state/pause_1.png"));
		gameOver = loadImage(new File("./src/cn/ccd/game/shoot2/material/game_state/gameover_1.jpg.png"));
		
		revolveb=loadImage(new File("./src/cn/ccd/game/shoot2/material/Explosion/bom2.png"));

	}

	public static BufferedImage loadImage(File file) {

		try {

			BufferedImage img = ImageIO.read(file);
			return img;

		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();

		}

	}

}
