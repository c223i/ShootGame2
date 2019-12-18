package cn.ccd.game.shoot2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/** 分数排行榜 */
public class ScoreLeaderboard {

	/* 设定分数数组长度10(只记录10个) */
	private static int[] oldScore = new int[10];

	/* 排行文本 */
	private static String ranking = null;

	/* 读取分数 */
	public synchronized static int[] getScore() {

		// 读取分数文件
		try {
			FileInputStream fis = new FileInputStream("./src/cn/ccd/game/shoot2/Score.dat");
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String str = null;
			int oldScoreIndex = 0;
			while ((str = br.readLine()) != null && oldScoreIndex < 10) {
				ScoreLeaderboard.oldScore[oldScoreIndex++] = Integer.parseInt(str);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在！");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("编码错误");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("读取异常");
			e.printStackTrace();
		}
		return ScoreLeaderboard.oldScore;

	}

	/* 保存分数 */
	public synchronized static void saveScore(int newScore) {

		getScore();

		try {
			// 判断新分数是否大于排行榜最后一位的分数
			if (ScoreLeaderboard.oldScore[ScoreLeaderboard.oldScore.length - 1] > newScore) {
				ScoreLeaderboard.ranking = "您未上榜";
				return;// 如果小于则结束该方法不写入
			}

			/* 如果以上条件成立则进行以下代码 */

			/* 排序 */
			ScoreLeaderboard.oldScore[ScoreLeaderboard.oldScore.length - 1] = newScore;
			for (int i = 0; i < ScoreLeaderboard.oldScore.length; i++) {
				for (int j = 0; j < oldScore.length - 1; j++) {
					if (ScoreLeaderboard.oldScore[j] < ScoreLeaderboard.oldScore[j + 1]) {
						int temp = ScoreLeaderboard.oldScore[j];
						ScoreLeaderboard.oldScore[j] = ScoreLeaderboard.oldScore[j + 1];
						ScoreLeaderboard.oldScore[j + 1] = temp;
					}
				}
			}

			/* 写入数据 */
			FileOutputStream fos = new FileOutputStream("./src/cn/ccd/game/shoot2/Score.dat");
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			PrintWriter pw = new PrintWriter(bw, true);
			for (int i = 0; i < ScoreLeaderboard.oldScore.length; i++) {
				pw.println(ScoreLeaderboard.oldScore[i]);
			}
			pw.close();

		} catch (FileNotFoundException e) {
			System.out.println("文件不存在！");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("编码错误");
			e.printStackTrace();
		}

		String str = null;
		for (int i = 0; i < ScoreLeaderboard.oldScore.length; i++) {
			if (newScore == ScoreLeaderboard.oldScore[i]) {
				str = "" + (i + 1);
				break;
			}
		}
		ScoreLeaderboard.ranking = "您的分数排在第  " + str + "  位";

	}

	public synchronized static String getRanking() {

		return ScoreLeaderboard.ranking;

	}

}
