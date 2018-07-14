package com.draglantix.assets;

import com.draglantix.audio.AudioMaster;

public class MusicAssets {

	public int insanityMusic;
	
	public MusicAssets() {
		insanityMusic = AudioMaster.loadSound("Insanity.wav");
	}
	
}
