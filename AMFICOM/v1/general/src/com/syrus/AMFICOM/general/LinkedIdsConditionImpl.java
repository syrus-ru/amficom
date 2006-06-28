/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.30.2.1 2006/06/28 10:34:12 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.COLLECTOR_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LAYOUT_ITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MARK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TOPOLOGICALNODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSPATH_TYPE_CODE;

import java.util.Set;

/**
 * @version $Revision: 1.30.2.1 $, $Date: 2006/06/28 10:34:12 $
 * @author $Author: arseniy $
 * @module general
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 *         <li>if {@link #entityCode}is {@link Characteristic}for all
	 *         characteristics for StorableObject identifier which can have
	 *         characteristics in identifier;</li>
	 *         </ul>
	 */
	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case CHARACTERISTIC_CODE:
				final Characteristic characteristic = (Characteristic) storableObject;
				switch (this.linkedEntityCode) {

					/* Administration */
					case SYSTEMUSER_CODE:
					case DOMAIN_CODE:
					case SERVER_CODE:
					case MCM_CODE:

					/* Configuration */
					case PORT_TYPE_CODE:
					case MEASUREMENTPORT_TYPE_CODE:
					case TRANSPATH_TYPE_CODE:
					case LINK_TYPE_CODE:
					case CABLELINK_TYPE_CODE:
					case CABLETHREAD_TYPE_CODE:
					case PROTOEQUIPMENT_CODE:
					case EQUIPMENT_CODE:
					case PORT_CODE:
					case MEASUREMENTPORT_CODE:
					case TRANSMISSIONPATH_CODE:
					case KIS_CODE:
					case LINK_CODE:
					case CABLELINK_CODE:

					/* Scheme */
					case SCHEMECABLELINK_CODE:
					case SCHEMELINK_CODE:
					case SCHEMEELEMENT_CODE:
					case SCHEMECABLEPORT_CODE:
					case SCHEMEPORT_CODE:
					case SCHEMECABLETHREAD_CODE:
					case SCHEMEDEVICE_CODE:
					case SCHEMEPATH_CODE:
					case SCHEMEPROTOELEMENT_CODE:

					/* Map */
					case COLLECTOR_CODE:
					case MARK_CODE:
					case NODELINK_CODE:
					case PHYSICALLINK_CODE:
					case PHYSICALLINK_TYPE_CODE:
					case SITENODE_CODE:
					case SITENODE_TYPE_CODE:
					case TOPOLOGICALNODE_CODE:
						
					/* Resource */
					case LAYOUT_ITEM_CODE:

						condition = super.conditionTest(characteristic.getParentCharacterizableId());
						break;
					case CHARACTERISTIC_TYPE_CODE:
						condition = super.conditionTest(characteristic.getTypeId());
						break;
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
				break;
			default:
				throw super.newExceptionEntityIllegal();
		}
		return condition;
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case CHARACTERISTIC_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw super.newExceptionEntityCodeToSetIsIllegal(entityCode);
		}
	}

	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}
}
