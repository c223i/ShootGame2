package cn.ccd.game.shoot2;

/** 奖励接口 */
public interface Award {

	public static final int ADD_LIFE = 0; // 加生命数量
	public static final int BULLRT_LEVEL_UP = 1; // 加子弹发射模式(火力值)
	public static final int PROTECTED_COVER = 2; // 开启保护罩
	public static final int HERO_LEVEL_UP = 3; // 加伤害值
	public static final int INCREASE_HP = 4;// 加血

	/* 返回奖励值类型 */
	public abstract int getAwardType();

}