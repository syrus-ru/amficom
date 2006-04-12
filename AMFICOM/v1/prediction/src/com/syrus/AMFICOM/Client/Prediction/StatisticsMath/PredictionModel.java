/*-
 * $Id: PredictionModel.java,v 1.5 2006/04/12 14:52:10 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.analysis.TraceResource;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class PredictionModel {
	public static final int Y0_LEVEL = 0;
	public static final int ATTENUATION = 1;
	public static final int REFL_AMPLITUDE = 2;
	public static final int REFLECTANCE = 3;
	public static final int LOSS = 4;
	public static final int NON_INITIALYZED = -1;
	
	static PredictionManager manager;
	private static int eventType = NON_INITIALYZED;
	private static int eventNumber = NON_INITIALYZED;
	
	private static LinkedList<ChangeListener> changeListeners = new LinkedList<ChangeListener>();
	
	private static ApplicationContext aContext;
	private static PropertyChangeListener traceChooserListener;
	
	private PredictionModel() {
		// never
	}
	
	public static void init(final ApplicationContext aContext1) {
		if (traceChooserListener == null) {
			traceChooserListener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals(RefUpdateEvent.typ)) {
						RefUpdateEvent ev = (RefUpdateEvent)evt;
						if (ev.traceChanged()) {
							if (manager instanceof FilteredMTAEPredictionManager) {
								TraceResource tr = (TraceResource)evt.getNewValue();
								final String id = tr.getId();
								if (!id.equals(Heap.PRIMARY_TRACE_KEY) && !id.equals(Heap.ETALON_TRACE_KEY) 
										&& !id.equals(Heap.MODELED_TRACE_KEY)) {
									if (((FilteredMTAEPredictionManager)manager).containsTrace(id)) {
										((FilteredMTAEPredictionManager)manager).setActive(id, tr.isShown());
										changeNotify();
									}
								}
							}		
						}
					}
				}
			};
		}

		if (aContext != null) {
			aContext.getDispatcher().removePropertyChangeListener(RefUpdateEvent.typ, traceChooserListener);
		}
		aContext = aContext1;
		aContext.getDispatcher().addPropertyChangeListener(RefUpdateEvent.typ, traceChooserListener);
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
	
	static void changeNotify() {
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
