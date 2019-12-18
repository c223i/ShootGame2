package cn.ccd.game.shoot2;

import java.applet.Applet;
import java.applet.AudioClip;

public class PlayMuiseGame {
	private AudioClip ac = null;
	public PlayMuiseGame(String name) {
		ac = Applet.newAudioClip(PlayMuiseGame.class.getResource(name));
	}

	public synchronized void play1() {

		ac.play();

	}

	public synchronized void play2() {

		ac.loop();

	}

	public synchronized void play3() {
		ac.stop();
	}

}
