/*-
 * $Id: Alarm.java,v 1.7 2006/03/13 13:54:01 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer.alarm;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.Action;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 * @author krupenn
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2006/03/13 13:54:01 $
 * @module observer
 */
public final class Alarm extends StorableObject {
	private static final long serialVersionUID = -8320740053940064854L;

	private PopupNotificationEvent event;
	private SchemePath path;
	private PathElement pathElement;
	private Measurement m;
	
	public Alarm(final PopupNotificationEvent event) {
		this.event = event;
		try {
			this.id = LocalIdentifierGenerator.generateIdentifier(ObjectEntities.EVENT_CODE);
		} catch (IllegalObjectEntityException e1) {
			Log.errorMessage(e1);
		}
		
		Identifier resultId = event.getResultId();
		double optDistance = event.getMismatchOpticalDistance();
		
		try {
			Result result = StorableObjectPool.getStorableObject(resultId, true);
			if (result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
				final Action action = result.getAction();
				this.m = (Measurement) action;
				
				Identifier meId = m.getMonitoredElementId();
				MonitoredElement me = StorableObjectPool.getStorableObject(meId, true);
				Set<Identifier> tpathIds = me.getMonitoredDomainMemberIds();

				if (!tpathIds.isEmpty()) {
					Set<SchemePath> schemePaths = StorableObjectPool.getStorableObjectsByCondition(
							new LinkedIdsCondition(tpathIds.iterator().next(), ObjectEntities.SCHEMEPATH_CODE), true);
					
					if (!schemePaths.isEmpty()) {
						this.path = schemePaths.iterator().next();
						this.pathElement = this.path.getPathElementByOpticalDistance(optDistance);
					}
				}
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	public SchemePath getPath() {
		return this.path;
	}

	public PathElement getPathElement() {
		return this.pathElement;
	}
	
	public MonitoredElement getMonitoredElement() throws ApplicationException {
		return this.m.getMonitoredElement();
	}
	
	public Measurement getMeasurement() {
		return this.m;
	}

	public PopupNotificationEvent getEvent() {
		return this.event;
	}
	
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		return Collections.emptySet();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected StorableObjectWrapper<Alarm> getWrapper() {
		return new AlarmWrapper();
	}
}
