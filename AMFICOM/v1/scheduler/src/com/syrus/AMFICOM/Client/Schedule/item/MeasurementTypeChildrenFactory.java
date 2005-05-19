/*-
* $Id: MeasurementTypeChildrenFactory.java,v 1.3 2005/05/19 14:32:22 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/05/19 14:32:22 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class MeasurementTypeChildrenFactory implements ChildrenFactory {

	private Identifier domainId; 
	
	
	public MeasurementTypeChildrenFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public MeasurementTypeChildrenFactory(Identifier domainId) {
		this.domainId = domainId;
	}
	
	public void populate(Item item) {
			LinkedIdsCondition domainCondition = new LinkedIdsCondition(this.domainId,
				ObjectEntities.KIS_ENTITY_CODE);
			Set measurementPortTypeIds = new LinkedHashSet();
			Map kisMeasurementTypes = new HashMap();
			Map kisMeasurementPorts = new HashMap();
			LinkedIdsCondition measurementPortCondition = null;
			try {
				Collection kiss = StorableObjectPool.getStorableObjectsByCondition(domainCondition, true);
				
				domainCondition.setEntityCode(ObjectEntities.ME_ENTITY_CODE);
				Collection monitoredElements = StorableObjectPool.getStorableObjectsByCondition(
					domainCondition, true);
				
				for (Iterator kisIterator = kiss.iterator(); kisIterator.hasNext();) {
					KIS kis = (KIS) kisIterator.next();
					if (measurementPortCondition == null)
						measurementPortCondition = new LinkedIdsCondition(kis.getId(),
																			ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
					else
						measurementPortCondition.setLinkedId(kis.getId());

					Collection measurementPorts = StorableObjectPool.getStorableObjectsByCondition(
						measurementPortCondition, true);
					kisMeasurementPorts.put(kis, measurementPorts);
					for (Iterator it = measurementPorts.iterator(); it.hasNext();) {
						MeasurementPort measurementPort = (MeasurementPort) it.next();

						MeasurementPortType measurementPortType = (MeasurementPortType) measurementPort.getType();
						measurementPortTypeIds.add(measurementPortType.getId());
					}

					LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(
																					measurementPortTypeIds,
																					ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);

					Collection measurementTypesFormeasurementPortType = StorableObjectPool
							.getStorableObjectsByCondition(linkedIdsCondition, true);
					
					List list = new ArrayList(measurementTypesFormeasurementPortType.size());
					for (Iterator iter = measurementTypesFormeasurementPortType.iterator(); iter.hasNext();) {
						StorableObject storableObject = (StorableObject) iter.next();
						list.add(storableObject.getId());
						
					}

					kisMeasurementTypes.put(kis, list);
				}	
				
				for (Iterator iterator = kisMeasurementTypes.keySet().iterator(); iterator.hasNext();) {
					KIS kis = (KIS) iterator.next();
					Collection measurementTypesFormeasurementPortType = (Collection) kisMeasurementTypes.get(kis);
					if (measurementTypesFormeasurementPortType.contains(item.getObject())) {
						KISItem kisItem = new KISItem(kis.getId());
						item.addChild(kisItem);
						Collection measurementPort = (Collection) kisMeasurementPorts.get(kis);
						for (Iterator measurementPortIterator = measurementPort.iterator(); measurementPortIterator
								.hasNext();) {
							MeasurementPort measurementPort2 = (MeasurementPort) measurementPortIterator.next();
							MeasurementPortItem measurementPortItem = new MeasurementPortItem(measurementPort2.getId());
							kisItem.addChild(measurementPortItem);
							for (Iterator monitoredElementIterator = monitoredElements.iterator(); monitoredElementIterator
									.hasNext();) {
								MonitoredElement monitoredElement = (MonitoredElement) monitoredElementIterator.next();
								if (monitoredElement.getMeasurementPortId().equals(measurementPort2.getId())) {
									MonitoredElementItem monitoredElementItem = new MonitoredElementItem(
																											monitoredElement.getId());
									measurementPortItem.addChild(monitoredElementItem);
								}
							}
						}
					}
				}
			} catch (ApplicationException e) {
				Log.debugException(e, Log.WARNING);
			}
		
	}



	
	public void setDomainId(Identifier domainId) {
		this.domainId = domainId;
	}
	

}

