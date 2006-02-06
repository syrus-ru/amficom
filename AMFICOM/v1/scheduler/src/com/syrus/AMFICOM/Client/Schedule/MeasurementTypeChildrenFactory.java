/*-
* $Id: MeasurementTypeChildrenFactory.java,v 1.18 2005/12/09 11:38:59 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.IconPopulatableItem;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;


/**
 * @version $Revision: 1.18 $, $Date: 2005/12/09 11:38:59 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public class MeasurementTypeChildrenFactory implements ChildrenFactory {

	private Identifier domainId; 
	
	
	public MeasurementTypeChildrenFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public MeasurementTypeChildrenFactory(Identifier domainId) {
		this.domainId = domainId;
	}
	
	public void populate(final Item item) {
		LinkedIdsCondition domainCondition = new LinkedIdsCondition(this.domainId,
			ObjectEntities.KIS_CODE);
		Set<Identifier> measurementPortTypeIds = new LinkedHashSet<Identifier>();
		Map<KIS, EnumSet<MeasurementType>> kisMeasurementTypes = new HashMap<KIS, EnumSet<MeasurementType>>();
		Map<KIS, Set<MeasurementPort>> kisMeasurementPorts = new HashMap<KIS, Set<MeasurementPort>>();
		LinkedIdsCondition measurementPortCondition = null;
		try {
			final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(domainCondition, true, true);
			
			domainCondition.setEntityCode(ObjectEntities.MONITOREDELEMENT_CODE);
			final Set<MonitoredElement> monitoredElements = StorableObjectPool.getStorableObjectsByCondition(domainCondition, true, true);
			
			for (final KIS kis : kiss) {
				if (measurementPortCondition == null)
					measurementPortCondition = new LinkedIdsCondition(kis.getId(),
																		ObjectEntities.MEASUREMENTPORT_CODE);
				else
					measurementPortCondition.setLinkedIdentifiable(kis.getId());

				final Set<MeasurementPort> measurementPorts = 
					StorableObjectPool.getStorableObjectsByCondition(measurementPortCondition, true, true);
				kisMeasurementPorts.put(kis, measurementPorts);
				
				EnumSet<MeasurementType> measurementTypes = EnumSet.noneOf(MeasurementType.class);
				
				for (Iterator it = measurementPorts.iterator(); it.hasNext();) {
					MeasurementPort measurementPort = (MeasurementPort) it.next();

					MeasurementPortType measurementPortType = measurementPort.getType();
					measurementPortTypeIds.add(measurementPortType.getId());
					measurementTypes.addAll(measurementPortType.getMeasurementTypes());
				}

				kisMeasurementTypes.put(kis, measurementTypes);
			}	
			
			for (final KIS kis : kisMeasurementTypes.keySet()) {
				EnumSet<MeasurementType> measurementTypesFormeasurementPortType = kisMeasurementTypes.get(kis);
				if (measurementTypesFormeasurementPortType.contains(item.getObject())) {
					IconPopulatableItem kisItem = new IconPopulatableItem();
					kisItem.setName(kis.getName());
					kisItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_TESTING));
					kisItem.setObject(kis.getId());
					item.addChild(kisItem);
					Set<MeasurementPort> measurementPort = kisMeasurementPorts.get(kis);
					for (Iterator measurementPortIterator = measurementPort.iterator(); measurementPortIterator
							.hasNext();) {
						MeasurementPort measurementPort2 = (MeasurementPort) measurementPortIterator.next();
						IconPopulatableItem measurementPortItem = new IconPopulatableItem();
						measurementPortItem.setName(measurementPort2.getName());
						measurementPortItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_PORT));
						measurementPortItem.setObject(measurementPort2.getId());
						kisItem.addChild(measurementPortItem);
						for (final MonitoredElement monitoredElement : monitoredElements) {
							if (monitoredElement.getMeasurementPortId().equals(measurementPort2)) {
								IconPopulatableItem monitoredElementItem = new IconPopulatableItem();
								monitoredElementItem.setName(monitoredElement.getName());
								monitoredElementItem.setObject(monitoredElement.getId());
								monitoredElementItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_PATHMODE));
								monitoredElementItem.setCanHaveChildren(false);
								measurementPortItem.addChild(monitoredElementItem);
							}
						}
					}
				}
			}
		} catch (final ApplicationException e) {
			AbstractMainFrame.showErrorMessage(
				I18N.getString("Scheduler.Error.CannotCreateTreeItems"));
			return;
		}		
	}
	
	public void setDomainId(Identifier domainId) {
		this.domainId = domainId;
	}	

}

