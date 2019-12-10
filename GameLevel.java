package cn.ccd.game.shoot2;

/** 游戏难度接口 */
public interface GameLevel {

	public static final int GAME_MODE_LEVEL_1 = 1;
	public static final int GAME_MODE_LEVEL_2 = 2;
	public static final int GAME_MODE_LEVEL_3 = 3;

	public abstract void setGameModeLevel(int level);// 设定游戏难度

}
