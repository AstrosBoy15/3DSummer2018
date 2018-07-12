package com.draglantix.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

import java.util.*;

import org.lwjgl.openal.*;

public class AudioMaster{
	
	private static List<Integer> buffers = new ArrayList<>();
	public static List<Source> sources = new ArrayList<>();
	private static long device;
	private static long context;

	public static void init(){
		String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);

		int[] attributes = {0};
		context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);

		AL.createCapabilities(ALC.createCapabilities(device));
	}

	public static void setListenerData(float x, float y, float z){
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}

	public static int loadSound(String file){
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData wavFile = WaveData.create(file);
		AL10.alBufferData(buffer, wavFile.format, wavFile.data, wavFile.samplerate);
		wavFile.dispose();
		return buffer;
	}

	public static void cleanUp(){
		for (int buffer : buffers){
			alDeleteBuffers(buffer);
		}

		for(Source source: sources) {
			source.delete();
		}
		
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
}