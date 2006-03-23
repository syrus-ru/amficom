/*-
 * $Id: PredictionModel.java,v 1.3 2006/03/23 10:07:29 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import java.util.LinkedList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PredictionModel {
	public static final int Y0_LEVEL = 0;
	public static final int ATTENUATION = 1;
	public static final int REFL_AMPLITUDE = 2;
	public static final int REFLECTANCE = 3;
	public static final int LOSS = 4;
	public static final int NON_INITIALYZED = -1;
	
	private static PredictionManager manager;
	private static int eventType = NON_INITIALYZED;
	private static int eventNumber = NON_INITIALYZED;
	
	private static LinkedList<ChangeListener> changeListeners = new LinkedList<ChangeListener>();
	
	private PredictionModel() {
		// never
	}
	
	public static void addChangeListener(ChangeListener changeListener) {
		changeListeners.add(changeListener);
	}
	
	public static void removeChangeListener(ChangeListener changeListener) {
		changeListeners.remove(changeListener);
	}
	
	public static void initPredictionManager(PredictionManager manager1) {
		PredictionModel.manager = manager1;
	}
	
	private static void changeNotify() {
		for (ChangeListener changeListener : changeListeners) {
			changeListener.stateChanged(new ChangeEvent(PredictionModel.class));
		}
	}

	public static PredictionManager getPredictionManager() {
		return manager;
	}
	
	public static Statistics getCurrentStatistics() {
		switch (eventType) {
		case PredictionModel.ATTENUATION:
			return manager.getAttenuationInfo(eventNumber);
		case PredictionModel.REFL_AMPLITUDE:
			return manager.getReflectiveAmplitudeInfo(eventNumber);
		case PredictionModel.Y0_LEVEL:
			return manager.getY0Info(eventNumber);
		case PredictionModel.LOSS:
			return manager.getLossInfo(eventNumber);
		case PredictionModel.REFLECTANCE:
			return manager.getReflectanceInfo(eventNumber);
		default:
			throw new UnsupportedOperationException("Unsupported event type");
		}
	}

	public static int getEventNumber() {
		return eventNumber;
	}

	public static void setEventNumber(int eventNumber) {
		PredictionModel.eventNumber = eventNumber;
		if (eventType != NON_INITIALYZED && eventNumber != -1) {
			changeNotify();
		}
	}

	public static int getEventType() {
		return eventType;
	}

	public static void setEventType(int eventType) {
		PredictionModel.eventType = eventType;
		if (eventType != NON_INITIALYZED && eventNumber != -1) {
			changeNotify();
		}
	}
}
