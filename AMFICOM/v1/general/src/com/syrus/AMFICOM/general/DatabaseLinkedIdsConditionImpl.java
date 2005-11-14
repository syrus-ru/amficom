/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.17 2005/11/14 11:22:02 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
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

/**
 * @version $Revision: 1.17 $, $Date: 2005/11/14 11:22:02 $
 * @author $Author: arseniy $
 * @module general
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case CHARACTERISTIC_CODE:
				switch (super.condition.getLinkedEntityCode()) {

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

					/* Resource */
					case LAYOUT_ITEM_CODE:

					/* Map */
					case COLLECTOR_CODE:
					case MARK_CODE:
					case NODELINK_CODE:
					case PHYSICALLINK_CODE:
					case PHYSICALLINK_TYPE_CODE:
					case SITENODE_CODE:
					case SITENODE_TYPE_CODE:
					case TOPOLOGICALNODE_CODE:

						return super.getQuery(CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID);
//					case ObjectEntities.CHARACTERISTIC_TYPE_CODE:
//						return super.getQuery(StorableObjectWrapper.COLUMN_TYPE_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}

}
