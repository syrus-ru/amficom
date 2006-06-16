/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.50 2006/06/16 10:39:35 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.UPDIKE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.50 $, $Date: 2006/06/16 10:39:35 $
 * @module scheme
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {
	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}

	/**
	 * @see com.syrus.AMFICOM.general.DatabaseStorableObjectCondition#getSQLQuery()
	 */
	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (this.condition.getEntityCode().shortValue()) {
		case SCHEMEPROTOGROUP_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEPROTOGROUP_CODE:
			case UPDIKE_CODE:
				return this.getQuery(SchemeProtoGroupWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMEPROTOELEMENT_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEPROTOELEMENT_CODE:
				return this.getQuery(SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID);
			case SCHEMEPROTOGROUP_CODE:
				return this.getQuery(SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID);
			case PROTOEQUIPMENT_CODE:
				return this.getQuery(SchemeProtoElementWrapper.COLUMN_PROTO_EQUIPMENT_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEME_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEELEMENT_CODE:
			case UPDIKE_CODE:
				return this.getQuery(SchemeWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
			case DOMAIN_CODE:
				return this.getQuery(SchemeWrapper.COLUMN_DOMAIN_ID);
			case MAP_CODE:
				return this.getQuery(SchemeWrapper.COLUMN_MAP_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();	
			}
		case SCHEMEELEMENT_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEELEMENT_CODE:
				return this.getQuery(SchemeElementWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
			case SCHEME_CODE:
				return this.getQuery(SchemeElementWrapper.COLUMN_PARENT_SCHEME_ID);
			case SITENODE_CODE:
				return this.getQuery(SchemeElementWrapper.COLUMN_SITE_NODE_ID);
			case PROTOEQUIPMENT_CODE:
				return this.getQuery(SchemeElementWrapper.COLUMN_PROTO_EQUIPMENT_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMEOPTIMIZEINFO_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEME_CODE:
				return this.getQuery(SchemeOptimizeInfoWrapper.COLUMN_PARENT_SCHEME_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMEOPTIMIZEINFOSWITCH_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEOPTIMIZEINFO_CODE:
				return this.getQuery(SchemeOptimizeInfoSwitchWrapper.COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMEOPTIMIZEINFORTU_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEOPTIMIZEINFO_CODE:
				return this.getQuery(SchemeOptimizeInfoRtuWrapper.COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMEMONITORINGSOLUTION_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEME_CODE:
				return this.getQuery(SchemeMonitoringSolutionWrapper.COLUMN_PARENT_SCHEME_ID);
			case SCHEMEOPTIMIZEINFO_CODE:
				return this.getQuery(SchemeMonitoringSolutionWrapper.COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMEDEVICE_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEELEMENT_CODE:
				return this.getQuery(SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
			case SCHEMEPROTOELEMENT_CODE:
				return this.getQuery(SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMEPORT_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEDEVICE_CODE:
				return this.getQuery(SchemePortWrapper.COLUMN_PARENT_DEVICE_ID);
			case PORT_TYPE_CODE:
				return this.getQuery(SchemePortWrapper.COLUMN_PORT_TYPE_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();	
			}
		case SCHEMECABLEPORT_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEDEVICE_CODE:
				return this.getQuery(SchemeCablePortWrapper.COLUMN_PARENT_DEVICE_ID);
			case PORT_TYPE_CODE:
				return this.getQuery(SchemeCablePortWrapper.COLUMN_CABLE_PORT_TYPE_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMELINK_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEPORT_CODE:
				return OPEN_BRACKET
						+ this.getQuery(SchemeLinkWrapper.COLUMN_SOURCE_SCHEME_PORT_ID)
						+ SQL_OR
						+ this.getQuery(SchemeLinkWrapper.COLUMN_TARGET_SCHEME_PORT_ID)
						+ CLOSE_BRACKET;
			case SCHEMEPROTOELEMENT_CODE:
				return this.getQuery(SchemeLinkWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID);
			case SCHEME_CODE:
				return this.getQuery(SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ID);
			case SCHEMEELEMENT_CODE:
				return this.getQuery(SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
			case LINK_TYPE_CODE:
				return this.getQuery(SchemeLinkWrapper.COLUMN_LINK_TYPE_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMECABLELINK_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMECABLEPORT_CODE:
				return OPEN_BRACKET
						+ this.getQuery(SchemeCableLinkWrapper.COLUMN_SOURCE_SCHEME_CABLE_PORT_ID)
						+ SQL_OR
						+ this.getQuery(SchemeCableLinkWrapper.COLUMN_TARGET_SCHEME_CABLE_PORT_ID)
						+ CLOSE_BRACKET;
			case SCHEME_CODE:
				return this.getQuery(SchemeCableLinkWrapper.COLUMN_PARENT_SCHEME_ID);
			case CABLELINK_TYPE_CODE:
				return this.getQuery(SchemeCableLinkWrapper.COLUMN_CABLE_LINK_TYPE_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMECABLETHREAD_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEPORT_CODE:
				return OPEN_BRACKET
						+ this.getQuery(SchemeCableThreadWrapper.COLUMN_SOURCE_SCHEME_PORT_ID)
						+ SQL_OR
						+ this.getQuery(SchemeCableThreadWrapper.COLUMN_TARGET_SCHEME_PORT_ID)
						+ CLOSE_BRACKET;
			case SCHEMECABLELINK_CODE:
				return this.getQuery(SchemeCableThreadWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case CABLECHANNELINGITEM_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMECABLELINK_CODE:
				return this.getQuery(CableChannelingItemWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID);
			case SITENODE_CODE:
				return OPEN_BRACKET
						+ this.getQuery(CableChannelingItemWrapper.COLUMN_START_SITE_NODE_ID)
						+ SQL_OR
						+ this.getQuery(CableChannelingItemWrapper.COLUMN_END_SITE_NODE_ID)
						+ CLOSE_BRACKET;
			case PHYSICALLINK_CODE:
				return this.getQuery(CableChannelingItemWrapper.COLUMN_PHYSICAL_LINK_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case SCHEMEPATH_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case TRANSMISSIONPATH_CODE:
				return this.getQuery(SchemePathWrapper.COLUMN_TRANSMISSION_PATH_ID);
			case SCHEMEMONITORINGSOLUTION_CODE:
				return this.getQuery(SchemePathWrapper.COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		case PATHELEMENT_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case SCHEMEPATH_CODE:
				return this.getQuery(PathElementWrapper.COLUMN_PARENT_SCHEME_PATH_ID);
			case SCHEMELINK_CODE:
				return this.getQuery(PathElementWrapper.COLUMN_SCHEME_LINK_ID);
			case SCHEMEELEMENT_CODE:
				//TODO: simplify this thing
				final StringBuffer buffer = new StringBuffer();
				buffer.append(PathElementWrapper.COLUMN_START_ABSTRACT_SCHEME_PORT_ID);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
				buffer.append(SQL_SELECT);
				buffer.append(COLUMN_ID);
				buffer.append(SQL_FROM);
				buffer.append(SCHEMEPORT);
				buffer.append(SQL_WHERE);
				
				buffer.append(SchemePortWrapper.COLUMN_PARENT_DEVICE_ID);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
				buffer.append(SQL_SELECT);
				buffer.append(COLUMN_ID);
				buffer.append(SQL_FROM);
				buffer.append(SCHEMEDEVICE);
				buffer.append(SQL_WHERE);
				
				buffer.append(this.getQuery(SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
				buffer.append(CLOSE_BRACKET);
				buffer.append(CLOSE_BRACKET);
				
				buffer.append(SQL_OR);
				
				buffer.append(PathElementWrapper.COLUMN_END_ABSTRACT_SCHEME_PORT_ID);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
				buffer.append(SQL_SELECT);
				buffer.append(COLUMN_ID);
				buffer.append(SQL_FROM);
				buffer.append(SCHEMEPORT);
				buffer.append(SQL_WHERE);
				
				buffer.append(SchemePortWrapper.COLUMN_PARENT_DEVICE_ID);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
				buffer.append(SQL_SELECT);
				buffer.append(COLUMN_ID);
				buffer.append(SQL_FROM);
				buffer.append(SCHEMEDEVICE);
				buffer.append(SQL_WHERE);
				buffer.append(this.getQuery(SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
				buffer.append(CLOSE_BRACKET);
				buffer.append(CLOSE_BRACKET);
				
				buffer.append(SQL_OR);
				
				buffer.append(PathElementWrapper.COLUMN_START_ABSTRACT_SCHEME_PORT_ID);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
				buffer.append(SQL_SELECT);
				buffer.append(COLUMN_ID);
				buffer.append(SQL_FROM);
				buffer.append(SCHEMECABLEPORT);
				buffer.append(SQL_WHERE);
				
				buffer.append(SchemePortWrapper.COLUMN_PARENT_DEVICE_ID);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
				buffer.append(SQL_SELECT);
				buffer.append(COLUMN_ID);
				buffer.append(SQL_FROM);
				buffer.append(SCHEMEDEVICE);
				buffer.append(SQL_WHERE);
				
				buffer.append(this.getQuery(SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
				buffer.append(CLOSE_BRACKET);
				buffer.append(CLOSE_BRACKET);
				
				buffer.append(SQL_OR);
				
				buffer.append(PathElementWrapper.COLUMN_END_ABSTRACT_SCHEME_PORT_ID);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
				buffer.append(SQL_SELECT);
				buffer.append(COLUMN_ID);
				buffer.append(SQL_FROM);
				buffer.append(SCHEMECABLEPORT);
				buffer.append(SQL_WHERE);
				
				buffer.append(SchemePortWrapper.COLUMN_PARENT_DEVICE_ID);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
				buffer.append(SQL_SELECT);
				buffer.append(COLUMN_ID);
				buffer.append(SQL_FROM);
				buffer.append(SCHEMEDEVICE);
				buffer.append(SQL_WHERE);
				buffer.append(this.getQuery(SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
				buffer.append(CLOSE_BRACKET);
				buffer.append(CLOSE_BRACKET);
				return buffer.toString();
			case SCHEMECABLELINK_CODE:
				return this.getLinkedQuery(PathElementWrapper.COLUMN_SCHEME_CABLE_THREAD_ID, COLUMN_ID, SchemeCableThreadWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID, SCHEMECABLETHREAD);
			case MEASUREMENTPORT_CODE:
				final StringBuffer buffer3 = new StringBuffer();
				buffer3.append(this.getLinkedQuery(PathElementWrapper.COLUMN_END_ABSTRACT_SCHEME_PORT_ID, COLUMN_ID, SchemeCablePortWrapper.COLUMN_MEASUREMENT_PORT_ID, SCHEMECABLEPORT));
				buffer3.append(SQL_OR);
				buffer3.append(this.getLinkedQuery(PathElementWrapper.COLUMN_END_ABSTRACT_SCHEME_PORT_ID, COLUMN_ID, SchemePortWrapper.COLUMN_MEASUREMENT_PORT_ID, SCHEMEPORT));
				return buffer3.toString();
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		default:
			throw this.newExceptionEntityIllegal();
		}
	}
}
