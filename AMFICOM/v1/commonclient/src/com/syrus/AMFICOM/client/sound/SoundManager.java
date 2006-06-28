/*-
 * $Id: SoundManager.java,v 1.1 2006/03/30 13:42:45 bass Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.sound;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.syrus.util.Log;

/**
 * @author Stanislav Khol'shin
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/03/30 13:42:45 $
 * @module commonclient
 */
public final class SoundManager {
	static AudioInputStream input;
	static Clip clip;
	
	private SoundManager() {
		assert false;
	}

	/**
	 * Plays sound until stop method is called. Invocation of this method while already 
	 * playing will result in nothing. 
	 * @param sound - file name of sound
	 */
	public static void loop(final String sound) {
		if (clip == null) { // initialize
			try {
				clip = AudioSystem.getClip();
				clip.addLineListener(new LineListener(){
					public void update(LineEvent event) {
						if (event.getType().equals(LineEvent.Type.STOP)) {
							clip.flush();
							clip.close();
						}
					}
				});
			} catch (LineUnavailableException e2) {
				Log.errorMessage(e2);
				return;
			}
		}
		
		if (!clip.isActive()) {
			try {
				input = AudioSystem.getAudioInputStream(new File(sound));
				clip.open(input);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (UnsupportedAudioFileException e) {
				Log.errorMessage(e);
			} catch (IOException e) {
				Log.errorMessage(e);
			} catch (LineUnavailableException e) {
				Log.errorMessage(e);
			}
		} else {
			Log.debugMessage("Device is busy", Level.FINER);
		}
	}
	
	/**
	 * Stops currently looping sound
	 */
	public static void stop() {
		if (clip != null && clip.isActive()) {
			clip.stop();
		} else {
			Log.debugMessage("Sound is not looping", Level.FINER);
		}
	}
	
	/**
	 * Plays sound once as separate stream. May be called many times - mixer will play
	 * several streams simultaneosly. But can not be stopped (plays until file ends).
	 * @param sound - file name of sound
	 */
	public static void play(final String sound) {
		play(sound, 1);
	}

	/**
	 * Plays sound as separate stream. May be called many times - mixer will play
	 * several streams simultaneosly. But can not be stopped (plays until file ends).
	 * So if called with count == Clip.LOOP_CONTINUOUSLY it never stops
	 * @param sound - file name of sound
	 * @param count - times to play
	 */
	public static void play(final String sound, final int count) {
		try {
			final AudioInputStream input1 = AudioSystem.getAudioInputStream(new File (sound));
			final Clip clip1 = AudioSystem.getClip();
			clip1.addLineListener(new LineListener(){
				public void update(LineEvent event) {
					if (event.getType().equals(LineEvent.Type.STOP)) {
						clip1.flush();
						clip1.close();
					}
				}
			});
			clip1.open(input1);
			if (count == Clip.LOOP_CONTINUOUSLY) {
				clip1.loop(count);
			} else {
				clip1.loop(count - 1);
			}
		} catch (UnsupportedAudioFileException e) {
			Log.errorMessage(e);
		} catch (IOException e) {
			Log.errorMessage(e);
		} catch (LineUnavailableException e) {
			Log.errorMessage(e);
		}		
	}
}
