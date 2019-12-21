package cn.ccd.game.shoot2;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 游戏音频
 * 
 * @author c223i
 *
 */
public class Audio {

	private static Map<String, AudioClip> audioMap = new HashMap<String, AudioClip>();
	private static ExecutorService threadPool = Executors.newFixedThreadPool(10);
	static {

		initialAudioMap();

	}

	private static void initialAudioMap() {

		/* 运行前音效 */
		audioMap.put("s1", newAudioClip("game_start_1.wav"));
		audioMap.put("s2", newAudioClip("game_start_2.wav"));

		/* 运行背景音效 */
		for (int i = 1; i <= 6; i++) {
			audioMap.put("r" + i, newAudioClip("game_run_" + i + ".wav"));
		}

		/* Boss音效 */
		audioMap.put("b1", newAudioClip("game_boss_1.wav"));
		audioMap.put("b2", newAudioClip("game_boss_2.wav"));
		audioMap.put("b_boom", newAudioClip("boss_boom.wav"));

		/* 奖励音效 */
		audioMap.put("a1", newAudioClip("game_award_1.wav"));// 普通奖励
		audioMap.put("a1UP", newAudioClip("game_award_1UP.wav"));// 生命奖励

		/* 游戏结束音效 */
		audioMap.put("o1", newAudioClip("game_over_1.wav"));
		audioMap.put("o2", newAudioClip("game_over_2.wav"));
		audioMap.put("o3", newAudioClip("game_over_3.wav"));

		/* 子弹音效 */
		audioMap.put("shoot", newAudioClip("shoot.wav"));

		/* 其他/未使用的音效 */
		audioMap.put("other_1", newAudioClip("game_other_1.wav"));

	}

	private static AudioClip newAudioClip(String name) {

		return Applet.newAudioClip(Audio.class.getResource(name));

	}

	public static synchronized AudioClip getAudioMapLoop(String audioName) {

		return audioMap.get(audioName);

	}
	
	
	public static void getAudioMapPlay(String audioName) {

		Thread t = new Thread() {
			public void run() {
				System.out.println(Thread.currentThread().getName());
				getAudioMapLoop(audioName).play();
			}
		};
		threadPool.execute(t);
	}
	

}
