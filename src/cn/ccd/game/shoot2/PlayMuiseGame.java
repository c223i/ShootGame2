package cn.ccd.game.shoot2;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.swing.plaf.synth.SynthViewportUI;


public class PlayMuiseGame {
	private URI ur = null;
	private URL ur1=null;
	private AudioClip ac = null;

	public PlayMuiseGame(String name) {
		try {
			File f=new File(name);
			ur=f.toURI();
			ur1=ur.toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ac = Applet.newAudioClip(ur1);
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
