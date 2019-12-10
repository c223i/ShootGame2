package cn.ccd.game.shoot2;

/**
 * 
 *	@Author c5913
 *	@CopyRight (C) 2019 c5913
 * 
 */

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** 游戏主类 */
public class World extends JPanel implements Versions, GameLevel {

	private static final long serialVersionUID = 77L; // UID

	/* 全局游戏窗口大小 */
	public static final int WIDTH = 650;
	public static final int HEIGHT = 975;

	/* 游戏状态 */
	private static final int START = 0;// 等待开始
	private static final int RUNNING = 1;// 游戏中
	private static final int PAUSE = 2;// 暂停
	private static final int GAME_OVRE = 3;// 游戏结束

	/* 默认游戏状态 */
	private int state = START;

	/* 游戏分数 */
	private int newScore = 0;

	/* 运行时间 */
	private int RunTime = 0;

	/* 创建游戏内容/对象 */
	private Sky sky = new Sky();
	private Hero hero = new Hero();
	private FlyingObject[] enemies = {};
	private Bullet[] bullet = {};
	private ProtectedCover protectedCover = new ProtectedCover();

	/* 游戏难度设定方法 */
	public void setGameModeLevel(int gameModelevel) {

		switch (gameModelevel) {

		case GAME_MODE_LEVEL_1: // 1

			enemiesEnterActionNum = 70; // 敌机刷新率，700毫秒一只
			bulletShootActionNum = 15; // 玩家子弹发射频率，150秒一发
			break;

		case GAME_MODE_LEVEL_2: // 2

			enemiesEnterActionNum = 40;
			bulletShootActionNum = 12;
			break;

		case GAME_MODE_LEVEL_3: // 3

			enemiesEnterActionNum = 16;
			bulletShootActionNum = 8;
			break;

		}

	}

	/* 生成敌机类型 */
	public FlyingObject generateTheEnemy() {

		Random ran = new Random();
		int type = ran.nextInt(56);

		if (type <= 2) {

			System.out.println("刷出-空投"); // 打转代码--------------------------------------------------
			return new Bee();

		} else if (type <= 30) {
			System.out.println("刷出-小敌机");// 打转代码--------------------------------------------------
			return new Airplane();

		} else {
			System.out.println("刷出-大敌机");// 打转代码--------------------------------------------------
			return new BigAirplane();

		}

	}

	private int enemiesEnterActionNum = 10; // 敌机/空投刷新频率控制(由游戏难度控制)
	private int enemiesEnterActionIndex = 0; // 敌机/空投刷新频率变量

	/* 生成敌人 */
	public void enemiesEnterAction() {

		enemiesEnterActionIndex++;
		if (enemiesEnterActionIndex % enemiesEnterActionNum == 0) {

			FlyingObject tempEnemies = generateTheEnemy();
			enemies = Arrays.copyOf(enemies, enemies.length + 1);
			enemies[enemies.length - 1] = tempEnemies;

		}

	}

	private int bulletShootActionNum = 10; // 子弹刷新频率控制(由游戏难度控制)
	private int bulletShootActionIndex = 0; // 子弹刷新频率变量

	/* 生成子弹 */
	public void bulletShootAction() {

		bulletShootActionIndex++;
		if (bulletShootActionIndex % bulletShootActionNum == 0) {

			FlyingObject[] tempBullet = hero.generateTheBullet();
			bullet = Arrays.copyOf(bullet, bullet.length + tempBullet.length);
			System.arraycopy(tempBullet, 0, bullet, bullet.length - tempBullet.length, tempBullet.length);

		}

	}

	/* 全局飞行物开始移动 */
	public void stepAction() {

		sky.step();
		for (int i = 0; i < enemies.length; i++) {

			enemies[i].step();

		}
		for (int i = 0; i < bullet.length; i++) {

			bullet[i].step();

		}

	}

	/* 全局飞行物出界检测 */
	public void outOfBoundsAction() {

		/* 敌机/空投 */
		int outOfBoundsIndex = 0;
		FlyingObject[] newEnemies = new FlyingObject[enemies.length];
		for (int i = 0; i < enemies.length; i++) {

			if (!enemies[i].outOfBounds() && !enemies[i].isRemove()) {

				newEnemies[outOfBoundsIndex] = enemies[i];
				outOfBoundsIndex++;

			}

		}
		enemies = Arrays.copyOf(newEnemies, outOfBoundsIndex);

		/* 子弹 */
		outOfBoundsIndex = 0;
		Bullet[] newBullet = new Bullet[bullet.length];
		for (int i = 0; i < bullet.length; i++) {

			if (!bullet[i].outOfBounds() && !bullet[i].isRemove()) {

				newBullet[outOfBoundsIndex] = bullet[i];
				outOfBoundsIndex++;

			}

		}
		bullet = Arrays.copyOf(newBullet, outOfBoundsIndex);

	}

	/* 子弹与敌机碰撞检测 */
	public void bulletBangAction() {

		for (int i = 0; i < bullet.length; i++) {

			for (int j = 0; j < enemies.length; j++) {

				if (bullet[i].isLife() && enemies[j].isLife() && enemies[j].hit(bullet[i])) {

					if ((enemies[j] instanceof Bee)) {// 跳过空投类型
						continue;
					}

					bullet[i].goDead();

					if (enemies[j] instanceof EnemiesHp) {
						EnemiesHp tempEnemies = (EnemiesHp) enemies[j];
						tempEnemies.reduceHp(hero.getLevel());
					}

					if (enemies[j] instanceof Enemy) {

						Enemy temp = (Enemy) enemies[j];
						newScore += temp.getScore();

					}

				}

			}

		}

	}

	/* 保护罩与敌机碰撞检测 */
	public synchronized void protectedCoverBangAction() {

		for (int i = 0; i < enemies.length; i++) {

			if (protectedCover.isLife() && enemies[i].isLife() && enemies[i].hit(protectedCover)) {

				if (!(enemies[i] instanceof Bee)) {// 除空投类型外飞行物直接删除
					enemies[i].goDead();
				}

			}

		}

	}

	/* 英雄机与敌机碰撞检测 */
	public synchronized void heroBangAction() {

		for (int i = 0; i < enemies.length; i++) {

			if (hero.isLife() && enemies[i].isLife() && enemies[i].hit(hero)) {

				enemies[i].goDead();// 先把撞到的飞行物删除

				/* 如果是撞到空投则取得对应奖励结束方法不执行扣血操作 */
				if (enemies[i] instanceof Award) {

					Award temp = (Award) enemies[i];

					switch (temp.getAwardType()) {
					case Award.ADD_LIFE:
						hero.addLife();
						return;
					case Award.BULLRT_LEVEL_UP:
						hero.addFireMode();
						return;
					case Award.PROTECTED_COVER:
						hero.addProtectedCover();// 奖励保护罩能量值
						protectedCover.restartProtectedCover();// 开启保护罩
						return;
					case Award.HERO_LEVEL_UP:
						hero.addLevel();
						return;
					case Award.INCREASE_HP:
						hero.addHp();
						return;
					}

				}
				/* 如果是撞到敌机 */
				if (enemies[i] instanceof Enemy) {

					Enemy temp = (Enemy) enemies[i];
					newScore += temp.getScore();

				}

				if (!protectedCover.isLife()) { // 判断撞到时是否有保护罩保护，如果没有则执行扣血操作

					hero.subtractHp();// 扣血
					hero.subtractFireMode();// 将开火模式降级
					hero.subtractLevel();// 将伤害等级降级

				}

			}

		}

	}

	/* 游戏结束检测 */
	public synchronized void checkGamoOvreAction() {

		if (hero.getLife() <= 0) {

			state = GAME_OVRE;
			ScoreLeaderboard.saveScore(newScore);

		}

	}

	/* 运行核心 */
	public synchronized void action() {

		MouseAdapter l = new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				switch (state) {

				case START:

					state = RUNNING;
					break;

				case GAME_OVRE:

					newScore = 0;
					hero = new Hero();
					enemies = new FlyingObject[0];
					bullet = new Bullet[0];
					protectedCover = new ProtectedCover();
					state = START;
					break;

				}

			}

			/* 鼠标移动捕获 */
			public void mouseMoved(MouseEvent e) {

				if (state == RUNNING) {

					hero.moveTo(e.getX(), e.getY());
					protectedCover.moveTo(e.getX(), e.getY());

				}

			}

			/* 鼠标移出窗口检测 */
			public void mouseExited(MouseEvent e) {

				if (state == RUNNING) {

					state = PAUSE;

				}

			}

			/* 鼠标移入窗口检测 */
			public void mouseEntered(MouseEvent e) {

				if (state == PAUSE) {

					state = RUNNING;

				}

			}

		};

		this.addMouseListener(l);
		this.addMouseMotionListener(l);

		/* 游戏运行内核 */
		Timer timer = new Timer();
		int time = 10;
		timer.schedule(new TimerTask() {

			public void run() {

				if (state == RUNNING) {

					enemiesEnterAction();
					bulletShootAction();
					stepAction();
					outOfBoundsAction();
					bulletBangAction();
					protectedCoverBangAction();
					heroBangAction();
					checkGamoOvreAction();

				}

				repaint();

			}

		}, time, time);
	}

	/* 窗口动画 */
	public synchronized void paint(Graphics g) {

		g.setColor(Color.YELLOW);

		sky.paintObject(g);
		hero.paintObject(g);
		protectedCover.paintObject(g);
		for (int i = 0; i < enemies.length; i++) {

			enemies[i].paintObject(g);

		}
		for (int i = 0; i < bullet.length; i++) {

			bullet[i].paintObject(g);

		}
		switch (state) {

		case START:

			g.drawImage(Images.start, 0, 0, null);
			break;

		case PAUSE:

			g.drawImage(Images.pause, 0, 0, null);
			break;

		case GAME_OVRE:

			g.drawImage(Images.gameOver, 0, 0, null);
			break;

		}
		
		if (state == RUNNING) { // 只在游戏运行时显示的内容

			/* 玩家基本信息显示 */
			g.setColor(Color.YELLOW);
			g.drawString("当前分数：[ " + newScore + " ]", 10, 20);
			g.drawString("剩余生命：" + (hero.getLife() - 1), 10, 40);
			g.drawString("伤害等级：" + hero.getLevel(), 10, 60);
			g.drawString("开火等级：" + hero.getFireMode(), 10, 80);

			/* 英雄机血条显示 */
			g.setColor(Color.YELLOW);
			g.drawString("x " + (hero.getLife() - 1), hero.x + hero.width + 7, hero.y + hero.height + 8);
			g.setColor(Color.WHITE);
			g.drawRect(hero.x, hero.y + hero.height, hero.width + 3, 8);
			if (hero.getHp() > 60) {
				g.setColor(Color.GREEN);
			} else if (hero.getHp() > 30) {
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.RED);
			}
			g.fillRect(hero.x + 2, hero.y + hero.height + 2, hero.getHp(), 5);

			/* 英雄机保护罩显示 */
			if (protectedCover.isLife()) {// 如果保护罩存在才显示
				g.setColor(Color.WHITE);
				g.drawRect(hero.x, hero.y + hero.height + 13, hero.width + 3, 8);
				g.fillRect(hero.x + 2, hero.y + hero.height + 15, hero.getProtectedCoverNum(), 5);
				if (RunTime % 8 == 0) {// 按照一定时间调用方法减少保护罩能量值
					hero.subtractProtectedCover();
				}
				if (hero.getProtectedCoverNum() == 0) {// 如果保护罩能量值为0则关闭保护罩
					protectedCover.goDead();
				}

			}

			/* 空投内容信息显示(后续可关闭，用贴图代替) */
			g.setColor(Color.YELLOW);
			for (int i = 0; i < enemies.length; i++) {
				if ((enemies[i] instanceof Bee)) {
					Bee temp = (Bee) enemies[i];
					int type = temp.getAwardType();
					String str = null;
					switch (type) {
					case Award.ADD_LIFE:
						str = "生命 +1";
						break;
					case Award.BULLRT_LEVEL_UP:
						str = "火力 +1";
						break;
					case Award.PROTECTED_COVER:
						str = "保护罩能量 +100";
						break;
					case Award.HERO_LEVEL_UP:
						str = "伤害 +1";
						break;
					case Award.INCREASE_HP:
						str = "血量回复 +50";
						break;
					}
					g.drawString(str, enemies[i].x, enemies[i].y + enemies[i].height);
				}
			}

			/* 显示敌机血量 */
			g.setColor(Color.YELLOW);
			for (int i = 0; i < enemies.length; i++) {
				/* 小敌机 */
				if (enemies[i] instanceof Airplane) {
					Airplane tempAirplane = (Airplane) enemies[i];
					if (tempAirplane.getHp() != EnemiesHp.airplaneHp) {
						g.setColor(Color.WHITE);
						g.drawRect(tempAirplane.x, tempAirplane.y - 10, EnemiesHp.airplaneHp + 3, 6);
						if (tempAirplane.getHp() > EnemiesHp.airplaneHp / 2) {
							g.setColor(Color.GREEN);
						} else if (tempAirplane.getHp() > EnemiesHp.airplaneHp / 3) {
							g.setColor(Color.YELLOW);
						} else {
							g.setColor(Color.RED);
						}
						g.fillRect(tempAirplane.x + 2, tempAirplane.y - 10 + 2, tempAirplane.getHp(), 3);
					}
				}

				/* 大敌机 */
				if (enemies[i] instanceof BigAirplane) {
					BigAirplane tempBigAirplane = (BigAirplane) enemies[i];
					if (tempBigAirplane.getHp() != EnemiesHp.bigAirplaneHp) {
						g.setColor(Color.WHITE);
						g.drawRect(tempBigAirplane.x, tempBigAirplane.y - 10, EnemiesHp.bigAirplaneHp + 3, 6);
						if (tempBigAirplane.getHp() > EnemiesHp.bigAirplaneHp / 2) {
							g.setColor(Color.GREEN);
						} else if (tempBigAirplane.getHp() > EnemiesHp.bigAirplaneHp / 3) {
							g.setColor(Color.YELLOW);
						} else {
							g.setColor(Color.RED);
						}
						g.fillRect(tempBigAirplane.x + 2, tempBigAirplane.y - 10 + 2, tempBigAirplane.getHp(), 3);
					}
				}
			}

			/* 游戏信息显示 */
			g.setColor(Color.YELLOW);
			g.drawString(PROJECT + " ( " + EDITION + " )", 10, World.HEIGHT - 140);
			g.drawString("Versions " + VERSIONS, 10, World.HEIGHT - 120);
			g.drawString("Author " + AUTHOR, 10, World.HEIGHT - 100);
			g.drawString(COPYRIGHT, 10, World.HEIGHT - 80);
			g.drawString("RunTime[" + (RunTime++) + "]", 10, World.HEIGHT - 60);
			
		}
		
		/*显示排行榜*/
		if (state == GAME_OVRE) { // 只在游戏结束时显示的内容
			g.setColor(Color.YELLOW);
			g.drawString("游戏结束，当前分数 [ " + newScore + " ] ， " +ScoreLeaderboard.getRanking() , 80, 200);
			g.drawString("游戏历史排行榜：", 80, 260);
			int[] tempScore = ScoreLeaderboard.getScore();
			for (int i = 0; i < tempScore.length; i++) {
				g.drawString(i+1+":     [  " + tempScore[i] + "  ]", 80, 300+i*50);
			}
		}
	}

	/* 游戏(程序)入口 */
	public static void main(String[] ccd) {

		World world = new World();
		Scanner scan = new Scanner(System.in);
		System.out.println("[ "+PROJECT+" ] " + EDITION + "_" + VERSIONS + "\n");
		System.out.println("欢迎体验本游戏");
		System.out.println("在此声明：本游戏基于" + SOURCE_CODE + "(" + SOURCE_CODE_VERSIONS + ")" + "开发");
		System.out.println("开发此游戏目的只是为了代码的学习，研究和测试");
		System.out.println("在游戏中出现某些未预料的BUG属于正常现象");
		System.out.println("此版本不是最终版本，但也不代表后续会进行更新，可能随时弃坑\n");
		System.out.println("请输入数字选择游戏难度 [难度越高敌机越多]");
		System.out.println("1.【初级难度】");
		System.out.println("2.【高级难度】");
		System.out.println("3.【变态难度】");
		System.out.println("请选择：");
		int gameMode;
		do {

			gameMode = scan.nextInt();

			if (gameMode < 1 || gameMode > 3) {
				System.out.println("输入错误，请重新选择：");
				continue;
			}
			world.setGameModeLevel(gameMode);
			break;

		} while (true);

		scan.close();
		JFrame frame = new JFrame();
		frame.add(world);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(World.WIDTH + 10, World.HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		world.action();

	}

}
