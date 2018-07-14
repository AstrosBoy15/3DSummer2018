package com.draglantix.audio;

import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;

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

	public static void setListenerData(Vector3f pos, float listenerAngle){
		AL10.alListener3f(AL10.AL_POSITION, pos.x, pos.y, pos.z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
		
		float[] directionvect = new float[6];
		directionvect[0] = (float) Math.sin(Math.toRadians(listenerAngle));
		directionvect[1] = 0;
		directionvect[2] = (float) Math.cos(Math.toRadians(listenerAngle));
		directionvect[3] = 0;
		directionvect[4] = 1;
		directionvect[5] = 0;
		AL10.alListenerfv(AL10.AL_ORIENTATION, directionvect);
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