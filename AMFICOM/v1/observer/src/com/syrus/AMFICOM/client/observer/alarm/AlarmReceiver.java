/*-
 * $Id: AlarmReceiver.java,v 1.2 2005/10/30 14:48:52 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer.alarm;

import java.util.Set;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.Marker;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.event.PopupMessageReceiver;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class AlarmReceiver implements PopupMessageReceiver {
	private ApplicationContext aContext;
	private static AlarmReceiver instance;
	
	public static AlarmReceiver getInstance() {
		if (instance == null) {
			instance = new AlarmReceiver();
		}
		return instance;
	}
	
	private AlarmReceiver() {
		final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();
		clientSessionEnvironment.addPopupMessageReceiver(this);
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;		
	}

	public void receiveMessage(Event event) {
		
//		PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent)event;
//		Identifier resultId = popupNotificationEvent.getResultId();
//		double optDistance = popupNotificationEvent.getMismatchOpticalDistance();
		
		Identifier resultId = new Identifier("Result_985");
		
		double optDistance = 1000;
		
		try {
			Result result = StorableObjectPool.getStorableObject(resultId, true);
			if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
				Measurement m = (Measurement)result.getAction();
				
				// notify about measurement
				this.aContext.getDispatcher().firePropertyChange(
						new ObjectSelectedEvent(this, m, null, ObjectSelectedEvent.MEASUREMENT));
				
				Identifier meId = m.getMonitoredElementId();
				MonitoredElement me = StorableObjectPool.getStorableObject(meId, true);
				Set<Identifier> tpathIds = me.getMonitoredDomainMemberIds();

				if (!tpathIds.isEmpty()) {
					Set<SchemePath> schemePaths = StorableObjectPool.getStorableObjectsByCondition(
							new LinkedIdsCondition(tpathIds.iterator().next(), ObjectEntities.SCHEMEPATH_CODE), true);
					
					if (!schemePaths.isEmpty()) {
						SchemePath path = schemePaths.iterator().next();
						PathElement pe = path.getPathElementByOpticalDistance(optDistance);

						Marker marker = new Marker("", 0);
						MarkerEvent mEvent = new MarkerEvent(this, MarkerEvent.ALARMMARKER_CREATED_EVENT,
								marker.getId(), optDistance, path.getId(), meId, pe.getId());

						this.aContext.getDispatcher().firePropertyChange(mEvent);
//						
//						MarkerEvent mEvent2 = new MarkerEvent(this, MarkerEvent.MARKER_DELETED_EVENT,
//						marker.getId(), distance, path.getId(), meId, pathElementId);
//						
//						this.aContext.getDispatcher().firePropertyChange(mEvent2);
					}
				}
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}
