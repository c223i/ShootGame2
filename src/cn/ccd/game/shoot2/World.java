package cn.ccd.game.shoot2;

/**
 * 
 *	@Author c5913
 *	@CopyRight (C) 2019 c5913
 * 
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

/** 游戏主类 */
public class World extends JPanel implements Versions {

	private static final long serialVersionUID = 253934765297834956L; // UID

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
	private static int RunTime = 0;

	/* 创建游戏内容/对象 */
	private Sky sky = new Sky();
	private Hero hero = new Hero();
	private FlyingObject[] enemies = {};
	private Bullet[] bullet = {};
	private ProtectedCover protectedCover = new ProtectedCover();

	/* 创建旋转子弹的控制键，，，，，这个是新增加的功能类 */
	private boolean revolveKey = false;
	private int i = 0;
	private RevolveBullet[] rBullet = null;// 创建旋转子弹的类

	/* 增加一个控制暂停的键 */
	private boolean pauseKey = false;
	private boolean startKey = false;
	private boolean ctrlKey = false;
	private boolean shiftKey = false;
	private boolean pKey = false;
	private boolean spBFlag = false;

	/* 创建boss机相关的内容 */
	private BossBullet[] bossbullet = {};
	private Boss boss;
	private boolean bossflag = true;
	private int bossbulletAction = 0;
	private int index = 0;

	/* 创建联邦宇宙飞船的对象 */
	public static boolean SpaceshipFlag = false;
	private Spaceship sp;
	private Shot[] shot;
	private boolean shotFlag = false;
	private boolean ssBulletFlag = false;
	List<SpaceshipBullet> tempSpBu;
	Iterator<SpaceshipBullet> spB;

	// 这是一个分界象
	/* 生成敌机类型的方法 */
	public FlyingObject generateTheEnemy() {

		int type = new Random().nextInt(55) + 1;

		if (type <= 5) {

			return new Bee();

		} else if (type <= 35) {

			return new Airplane();

		} else {

			return new BigAirplane();

		}

	}

	private int enemiesEnterActionNum = 40; // 敌机/空投刷新频率控制
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

	private int bulletShootActionNum = 12; // 子弹刷新频率控制
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
						Audio.getAudioMapLoop("a1UP").play();
						hero.addLife();
						return;
					case Award.BULLRT_LEVEL_UP:
						Audio.getAudioMapLoop("a1").play();
						hero.addFireMode();
						return;
					case Award.PROTECTED_COVER:
						Audio.getAudioMapLoop("a1").play();
						hero.addProtectedCover();// 奖励保护罩能量值
						protectedCover.restartProtectedCover();// 开启保护罩
						return;
					case Award.HERO_LEVEL_UP:
						Audio.getAudioMapLoop("a1").play();
						hero.addLevel();
						return;
					case Award.INCREASE_HP:
						Audio.getAudioMapLoop("a1").play();
						hero.addHp();
						return;
					}

				}
				/* 如果是撞到敌机 */
				if (enemies[i] instanceof Enemy) {

					Enemy temp = (Enemy) enemies[i];
					newScore += temp.getScore();

				}

				if (!protectedCover.isLife() && !SpaceshipFlag) { // 判断撞到时是否有保护罩保护，如果没有则执行扣血操作

					hero.subtractHp();// 扣血

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

	/* 旋转子弹的方法 */
	public void revolveBulletAction() {
		if (rBullet == null && revolveKey == true) {
			rBullet = new RevolveBullet[3];// 创建旋转子弹的类}
			for (int i = 0; i < rBullet.length; i++) {
				rBullet[i] = new RevolveBullet(i * 200 + 100, World.HEIGHT - 20);
			}

		}
		if (revolveKey == true) {
			revolveKey = true;
			for (RevolveBullet rb : rBullet) {
				rb.step();// 旋转子弹移动
				for (FlyingObject f : enemies) {
					if (rb.isLife() && f.isLife() && f.hit(rb)) {
						if ((f instanceof Bee)) {// 跳过空投类型
							continue;
						}
						rb.subtractLife();
						f.goDead();
					}

				}
				for (BossBullet bBullet : bossbullet) {
					if (rb.isLife() && bBullet.isLife() && rb.hit(bBullet)) {
						rb.subtractLife();
						bBullet.goDead();
					}

				}
				if (!bossflag) {
					if (rb.isLife() && boss.isLife() && rb.hit(boss)) {
						rb.goDead();
						boss.reduceHp(2);
					}
				}
				if (rb.getLife() < 1 || rb.outOfBounds()) {
					i++;
				}
				if (i == 3) {
					rBullet = null;
					revolveKey = false;
					i = 0;
				}
			}
		}

	}

	/* 暂停键控制方法 */
	public void pauseKyeAction() {
		if (pauseKey == true) {
			state = PAUSE;
			pauseKey = false;
		}
		if (startKey == true) {
			state = RUNNING;
			startKey = false;
		}

	}

	public void startKyeAction() {

	}

	boolean gameRunAudioPlayState = false;
	int lastState = -1;
	int START_Audio = -1;
	int RUNNING_Audio = -1;
	int GAME_OVRE_Audio = 1;

	/* 音效 */
	public void playAudio() {

		if (state != lastState) {
			if (state == START) {
				Audio.getAudioMapLoop("o" + GAME_OVRE_Audio).stop();
				int temp = new Random().nextInt(2) + 1;
				Audio.getAudioMapLoop("s" + temp).loop();
				START_Audio = temp;
			} else if (state == RUNNING) {
				Audio.getAudioMapLoop("s" + START_Audio).stop();
				Audio.getAudioMapLoop("shoot").loop();

				if (gameRunAudioPlayState == false) {
					gameRunAudioPlayState = true;
					int temp = new Random().nextInt(6) + 1;
					Audio.getAudioMapLoop("r" + temp).loop();
					RUNNING_Audio = temp;
				}
			} else if (state == PAUSE) {
				Audio.getAudioMapLoop("shoot").stop();
			} else {
				Audio.getAudioMapLoop("shoot").stop();
				gameRunAudioPlayState = false;
				Audio.getAudioMapLoop("r" + RUNNING_Audio).stop();
				int temp = new Random().nextInt(3) + 1;
				Audio.getAudioMapLoop("o" + temp).play();
				GAME_OVRE_Audio = temp;
			}
			lastState = state;
		}

	}

	/* 实现boss出现 */
	public void bossAction() {
		/* 生成boss对象 */
		if (bossflag) {
			index++;
			if (index % 500 == 0) {
				boss = new Boss();
				bossflag = false;
				boss.shoot();
				boss.gethp();
			}
		}

		/* boss子弹入场 */

		if (boss != null) {
			bossbulletAction++;
			if (bossbulletAction % 250 == 0) {
				bossbullet = boss.shoot();
			}

			/* boss子弹与英雄机子弹与英雄机碰撞检测 */

			boss.step();
			boss.shoot();
			for (BossBullet bB : bossbullet) {
				bB.step();
				for (Bullet b : bullet) {
					if (bB.isLife() && b.isLife() && bB.hit(b)) {
						b.goDead();
						bB.goDead();
					}
				}

				if (bB.isLife() && hero.isLife() && hero.hit(bB)) {// 与英雄机碰撞检测
					if (!protectedCover.isLife() && !SpaceshipFlag) { // 判断撞到时是否有保护罩保护，如果没有则执行扣血操作
						hero.subtractHp();// 扣血
						hero.subtractHp();
					}
					bB.goDead();
				}

			}

			/* boss与英雄机碰撞检测 */
			if (boss.isLife() && hero.isLife() && hero.hit(boss)) {
				if (!protectedCover.isLife() && !SpaceshipFlag) { // 判断撞到时是否有保护罩保护，如果没有则执行扣血操作
					hero.subtractHp();// 扣血
					hero.subtractHp();
					boss.subtracthp();
				}
			}

			/* boss与英雄机子弹碰撞检测 */
			for (Bullet b : bullet) {
				if (b.isLife() && boss.isLife() && boss.hit(b)) {
					boss.subtracthp();
					b.goDead();
				}
			}
			if (boss.gethp() <= 0) {
				boss = null;
				bossflag = true;
			}
		}
	}

	/* 召唤宇宙飞船 */
	public void spaceshipAction() {
		if (sp == null && ctrlKey && shiftKey && pKey) {// 召唤宇宙飞船s
			sp = new Spaceship();
			SpaceshipFlag = true;
			i = 0;
			hero.addFireMode();
			hero.addFireMode();
		}
		if (SpaceshipFlag && ctrlKey && spBFlag) {// 生成导弹
			tempSpBu = sp.generateTheBullet();
			ssBulletFlag = true;
		}
		// 宇宙飞船移动，及导弹射击
		if (SpaceshipFlag) {
			hero.x = sp.x + 100;
			hero.y = sp.y + 120;
			protectedCover.moveTo(hero.x, hero.y - 40);// 固定英雄机的位置
			sp.ssStep();
			for (FlyingObject e : enemies) {
				if ((e instanceof Bee)) {// 跳过空投类型
					continue;
				}
				if (sp.isLife() && e.isLife() && sp.hit(e)) {// 飞船与敌人的判断
					e.goDead();
				}
			}
			if (!bossflag) {
				for (BossBullet b : bossbullet) {
					if (sp.isLife() && b.isLife() && sp.hit(b)) {
						b.goDead();
					}
				}
			}
		}
		/* 巡航导弹的移动 碰撞 */
		if (ssBulletFlag) {
			spB = tempSpBu.iterator();
			while (spB.hasNext()) {
				SpaceshipBullet sb = spB.next();
				sb.step();
				if (!bossflag) {
					for (BossBullet b : bossbullet) {
						if (sb.isLife() && b.isLife() && sb.hit(b)) {
							b.goDead();
							sb.reduceHp(1);
						}
					}
					if (boss.isLife() && sb.isLife() && sb.hit(boss)) {
						boss.reduceHp(1);
						sb.reduceHp(1);
					}
				}
				for (FlyingObject e : enemies) {
					if ((e instanceof Bee)) {// 跳过空投类型
						continue;
					}
					if (sb.isLife() && e.isLife() && sb.hit(e)) {
						e.goDead();
						sb.reduceHp(1);
					}
				}
				if (sb.getHp() <= 0) {// 并且变成散花子弹
					shotFlag = true;
					shot = sb.generateShot();
					spB.remove();
				}
				if (i == 2) {
					ssBulletFlag = false;
					tempSpBu = null;
				}
			}

		}
		if (shotFlag) {
			for (Shot s : shot) {
				s.step();
				if (sp.isLife() && s.isLife() && s.hit(sp)) {
					s.goDead();
				}
				for (BossBullet b : bossbullet) {

					if (s.isLife() && b.isLife() && b.hit(s)) {
						b.goDead();
						s.goDead();
					}

				}
				if (!bossflag && boss.isLife() && boss.hit(s)) {
					boss.reduceHp(1);
					s.goDead();
				}
				for (FlyingObject e : enemies) {
					if ((e instanceof Bee)) {// 跳过空投类型
						continue;
					}
					if (s.isLife() && e.isLife() && e.hit(s)) {
						e.goDead();
						s.goDead();
					}
				}
			}
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
					World.RunTime = 0;

					bossbullet = new BossBullet[0];
					boss = new Boss();
					bossflag = true;
					bossbulletAction = 0;
					index = 0;

					World.SpaceshipFlag = false;
					shot = null;
					shotFlag = false;
					ssBulletFlag = false;
					tempSpBu = null;
					state = START;

					break;

				}

			}

			/* 鼠标移动捕获 */
			public void mouseMoved(MouseEvent e) {

				if (state == RUNNING && !SpaceshipFlag) {

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

				pauseKyeAction();
				startKyeAction();
				playAudio();

				if (state == RUNNING) {
					enemiesEnterAction();
					bulletShootAction();
					stepAction();
					outOfBoundsAction();
					bulletBangAction();
					protectedCoverBangAction();
					heroBangAction();
					checkGamoOvreAction();
					revolveBulletAction();

					bossAction();
					spaceshipAction();
				}

				World.RunTime++;
				repaint();

			}

		}, time, time);
	}

	/* 窗口动画 */
	public synchronized void paint(Graphics g) {

		g.setColor(Color.YELLOW);

		sky.paintObject(g);

		if (ssBulletFlag) {
			for (SpaceshipBullet sSB : tempSpBu) {
				sSB.paintObject(g);
			}
		}

		g.drawImage(Images.bar, WIDTH - 110, HEIGHT - 130, null);

		if (SpaceshipFlag) {
			sp.paintObject(g);
		}

		hero.paintObject(g);

		if (SpaceshipFlag) {
			protectedCover.paintObject(g);
		}

		if (shotFlag) {
			for (Shot st : shot) {
				st.paintObject(g);
			}
		}

		if (rBullet != null) {
			try {
				for (RevolveBullet rb : rBullet) {
					rb.paintObject(g);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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

			/* boss机血量 */
			if (boss != null) {
				if (boss.gethp() != (EnemiesHp.bosshp + 1)) {
					g.setColor(Color.WHITE);
					g.drawRect(boss.x, boss.y - 20, EnemiesHp.bosshp + 3, 20);

					if (boss.gethp() > EnemiesHp.bosshp * 2 / 3) {
						g.setColor(Color.GREEN);
					} else if (boss.gethp() > EnemiesHp.bosshp / 3) {
						g.setColor(Color.YELLOW);
					} else {
						g.setColor(Color.RED);
					}
					g.fillRect(boss.x + 2, boss.y - 20 + 2, boss.gethp(), 17);
				}
			}

			/* boss */
			if (boss != null) {
				boss.paintObject(g);
				for (BossBullet b : bossbullet) {
					b.paintObject(g);
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

			g.setFont(new Font("微软雅黑",Font.BOLD,18));
			/* 玩家基本信息显示 */
			g.setColor(Color.YELLOW);
			g.drawString("当前分数：[ " + newScore + " ]", 10, 20);
			g.drawImage(Images.playerState_LIFE, 10, 30, null);
			g.drawString(" x" + (hero.getLife() - 1), 40, 50);

			g.drawImage(Images.playerState_INCREASE_HP, 10, 63, null);
			g.setColor(Color.WHITE);
			g.drawRect(46, 63, 100 + 3, 12);
			if (hero.getHp() > 50) {
				g.setColor(Color.GREEN);
			} else if (hero.getHp() > 30) {
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.RED);
			}
			g.fillRect(48, 65, hero.getHp(), 9);

			g.setColor(Color.YELLOW);
			g.drawImage(Images.playerState_HERO_LEVEL, 10, 85, null);
			g.drawString(" x" + hero.getLevel(), 40, 105);
			g.drawImage(Images.playerState_BULLRT_LEVEL, 10, 115, null);
			g.drawString(" x" + hero.getFireMode(), 40, 135);

			/* 英雄机保护罩显示 */
			if (protectedCover.isLife()) {// 如果保护罩存在才显示
				protectedCover.paintObject(g);
				g.drawImage(Images.playerState_PROTECTED_COVER, 12, 145, null);
				g.setColor(Color.WHITE);
				g.drawRect(46, 150, hero.width + 3, 12);
				g.fillRect(46 + 2, 152, hero.getProtectedCoverNum(), 9);
				if (World.RunTime % 3 == 0) {// 按照一定时间调用方法减少保护罩能量值
					hero.subtractProtectedCover();
				}
				if (hero.getProtectedCoverNum() == 0) {// 如果保护罩能量值为0则关闭保护罩
					protectedCover.goDead();
				}
			}
			
			g.setFont(new Font("微软雅黑",Font.BOLD,14));
			/* 游戏信息显示 */
			g.setColor(Color.YELLOW);
			g.drawString("时间：[  " + new SimpleDateFormat("HH : mm : ss").format(new Date()) + "  ]", World.WIDTH - 160,
					20);
			g.setColor(Color.YELLOW);
			g.drawString(PROJECT + " ( " + EDITION + " )", 10, World.HEIGHT - 120);
			g.drawString("Versions " + VERSIONS, 10, World.HEIGHT - 100);
			g.drawString("Author " + AUTHOR, 10, World.HEIGHT - 80);
			g.drawString(COPYRIGHT, 10, World.HEIGHT - 60);

		}
		/* 显示排行榜 */
		if (state == GAME_OVRE) { // 只在游戏结束时显示的内容
			g.setColor(Color.RED);
			g.setFont(new Font("微软雅黑",Font.BOLD,20));
			g.drawString("游戏结束，当前分数 [ " + newScore + " ] ， " + ScoreLeaderboard.getRanking(), 80, 200);
			g.drawString("游戏历史排行榜：", 80, 260);
			int[] tempScore = ScoreLeaderboard.getScore();
			for (int i = 0; i < tempScore.length; i++) {
				g.drawString(i + 1 + ":     [  " + tempScore[i] + "  ]", 80, 300 + i * 50);
			}
		}
	}

	/* 游戏(程序)入口 */
	public static void main(String[] ccd) {

		World world = new World();

		Versions.printInfo();

		JFrame frame = new JFrame();
		frame.add(world);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(World.WIDTH + 10, World.HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_T:
					world.spBFlag = true;
					break;
				case KeyEvent.VK_SPACE:
					world.revolveKey = true;
					break;
				case KeyEvent.VK_1:
					world.pauseKey = true;
					break;
				case KeyEvent.VK_2:
					world.startKey = true;
					break;
				case KeyEvent.VK_CONTROL:
					world.ctrlKey = true;
					break;
				case KeyEvent.VK_SHIFT:
					world.shiftKey = true;
					break;
				case KeyEvent.VK_P:
					world.pKey = true;
					break;
				case KeyEvent.VK_LEFT:
					Spaceship.leftKey = true;
					break;
				case KeyEvent.VK_UP:
					Spaceship.upKey = true;
					break;
				case KeyEvent.VK_RIGHT:
					Spaceship.rigthKey = true;
					break;
				case KeyEvent.VK_DOWN:
					Spaceship.downKey = true;
					break;

				default:
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_T:
					world.spBFlag = false;
					break;
				case KeyEvent.VK_P:
					world.pKey = false;
				case KeyEvent.VK_CONTROL:
					world.ctrlKey = false;
					break;
				case KeyEvent.VK_SHIFT:
					world.shiftKey = false;
					break;
				case KeyEvent.VK_LEFT:
					Spaceship.leftKey = false;
					break;
				case KeyEvent.VK_UP:
					Spaceship.upKey = false;
					break;
				case KeyEvent.VK_RIGHT:
					Spaceship.rigthKey = false;
					break;
				case KeyEvent.VK_DOWN:
					Spaceship.downKey = false;
					break;
				}
			}
		});

		world.action();

	}

}
