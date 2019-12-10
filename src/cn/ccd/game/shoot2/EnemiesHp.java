package cn.ccd.game.shoot2;

/** 敌机HP接口 */
public interface EnemiesHp {

	public static final int airplaneHp = 50; // 小敌机血量
	public static final int bigAirplaneHp = 100; // 大敌机血量

	public abstract int getHp(); // 获取敌机当前血量

	public abstract void reduceHp(int heroLevel); // 敌机扣血

}
