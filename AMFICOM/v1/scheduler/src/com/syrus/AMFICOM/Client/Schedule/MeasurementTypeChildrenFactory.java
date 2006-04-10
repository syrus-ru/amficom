/*-
* $Id: MeasurementTypeChildrenFactory.java,v 1.18.4.1 2006/04/10 11:46:00 saa Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
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
 * @version $Revision: 1.18.4.1 $, $Date: 2006/04/10 11:46:00 $
 * @author $Author: saa $
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
//		Map<KIS, Set<MeasurementType>> kisMeasurementTypes = new HashMap<KIS, Set<MeasurementType>>();
		Map<KIS, Set<MeasurementPort>> kisMeasurementPorts = new HashMap<KIS, Set<MeasurementPort>>();
		try {
			// определяем все КИСы для данного типа измерения
			final MeasurementType measurementType = (MeasurementType)item.getObject();
			final Set<KIS> kiss1 = measurementType.getProperKISs();

			// все КИСы
//			final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(domainCondition, true, true);

			domainCondition.setEntityCode(ObjectEntities.MONITOREDELEMENT_CODE);
			// все ME
			final Set<MonitoredElement> monitoredElements = StorableObjectPool.getStorableObjectsByCondition(domainCondition, true, true);

			for (final KIS kis : kiss1) {
				LinkedIdsCondition measurementPortCondition =
					new LinkedIdsCondition(kis.getId(),
						ObjectEntities.MEASUREMENTPORT_CODE);

				// все порты данного КИС - запоминаем в kisMeasurementPorts
				final Set<MeasurementPort> measurementPorts = 
					StorableObjectPool.getStorableObjectsByCondition(measurementPortCondition, true, true);
				kisMeasurementPorts.put(kis, measurementPorts);

//				// все типы измерения всех портов данного КИСа - запоминаем в kisMeasurementTypes
//				Set<MeasurementType> measurementTypes = new HashSet<MeasurementType>();
//				for (MeasurementPort measurementPort: measurementPorts) {
//					MeasurementPortType measurementPortType = measurementPort.getType();
//					measurementTypes.addAll(measurementPortType.getMeasurementTypes());
//				}
//				kisMeasurementTypes.put(kis, measurementTypes);
			}

			// для каждого КИСа, который имеет заданный тип измерения
			for (final KIS kis : kiss1) {
				{
					// создаем item
					IconPopulatableItem kisItem = new IconPopulatableItem();
					kisItem.setName(kis.getName());
					kisItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_TESTING));
					kisItem.setObject(kis.getId());
					item.addChild(kisItem);

					// для каждого порта этого КИСа
					for (MeasurementPort measurementPort2: kisMeasurementPorts.get(kis)) {

						// создаем item
						IconPopulatableItem measurementPortItem = new IconPopulatableItem();
						measurementPortItem.setName(measurementPort2.getName());
						measurementPortItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_PORT));
						measurementPortItem.setObject(measurementPort2.getId());
						kisItem.addChild(measurementPortItem);

						// для каждого ME из вселенной, порт которого совпадает с текущим
						for (final MonitoredElement monitoredElement : monitoredElements) {
							if (monitoredElement.getMeasurementPortId().equals(measurementPort2)) {

								// создаем item
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

