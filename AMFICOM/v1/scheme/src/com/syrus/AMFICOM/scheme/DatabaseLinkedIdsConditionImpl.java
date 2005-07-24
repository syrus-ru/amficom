/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.26 2005/07/24 17:10:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.UPDIKE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.26 $, $Date: 2005/07/24 17:10:19 $
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
		switch (super.condition.getEntityCode().shortValue()) {
			case SCHEME_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case DOMAIN_CODE:
					return super.getQuery(SchemeWrapper.COLUMN_DOMAIN_ID);
				case SCHEMEELEMENT_CODE:
					return super.getQuery(SchemeWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
				default:
					throw super.newExceptionLinkedEntityIllegal();	
				}
			case SCHEMEPORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SCHEMEDEVICE_CODE:
						return super.getQuery(SchemePortWrapper.COLUMN_PARENT_DEVICE_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();	
				}
			case SCHEMECABLEPORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SCHEMEDEVICE_CODE:
						return super.getQuery(SchemeCablePortWrapper.COLUMN_PARENT_DEVICE_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case SCHEMELINK_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SCHEMEPORT_CODE:
						return OPEN_BRACKET
								+ super.getQuery(SchemeLinkWrapper.COLUMN_SOURCE_SCHEME_PORT_ID)
								+ SQL_OR
								+ super.getQuery(SchemeLinkWrapper.COLUMN_TARGET_SCHEME_PORT_ID)
								+ CLOSE_BRACKET;
					case SCHEMEPROTOELEMENT_CODE:
						return super.getQuery(SchemeLinkWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID);
					case SCHEME_CODE:
						return super.getQuery(SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ID);
					case SCHEMEELEMENT_CODE:
						return super.getQuery(SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case SCHEMECABLELINK_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SCHEMECABLEPORT_CODE:
						return OPEN_BRACKET
								+ super.getQuery(SchemeCableLinkWrapper.COLUMN_SOURCE_SCHEME_CABLE_PORT_ID)
								+ SQL_OR
								+ super.getQuery(SchemeCableLinkWrapper.COLUMN_TARGET_SCHEME_CABLE_PORT_ID)
								+ CLOSE_BRACKET;
					case SCHEME_CODE:
						return super.getQuery(SchemeCableLinkWrapper.COLUMN_PARENT_SCHEME_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case SCHEMECABLETHREAD_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SCHEMEPORT_CODE:
						return OPEN_BRACKET
								+ super.getQuery(SchemeCableThreadWrapper.COLUMN_SOURCE_SCHEME_PORT_ID)
								+ SQL_OR
								+ super.getQuery(SchemeCableThreadWrapper.COLUMN_TARGET_SCHEME_PORT_ID)
								+ CLOSE_BRACKET;
					case SCHEMECABLELINK_CODE:
						return super.getQuery(SchemeCableThreadWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case SCHEMEELEMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case SCHEMEELEMENT_CODE:
					return super.getQuery(SchemeElementWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
				case SCHEME_CODE:
					return super.getQuery(SchemeElementWrapper.COLUMN_PARENT_SCHEME_ID);
				default:
					throw super.newExceptionLinkedEntityIllegal();
				}
			case SCHEMEDEVICE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case SCHEMEELEMENT_CODE:
					return super.getQuery(SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
				case SCHEMEPROTOELEMENT_CODE:
					return super.getQuery(SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID);
				default:
					throw super.newExceptionLinkedEntityIllegal();
				}
			case PATHELEMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case SCHEMEPATH_CODE:
					return super.getQuery(PathElementWrapper.COLUMN_PARENT_SCHEME_PATH_ID);
				default:
					throw super.newExceptionLinkedEntityIllegal();
				}
			case SCHEMEPATH_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case SCHEME_CODE:
					return super.getQuery(SchemePathWrapper.COLUMN_PARENT_SCHEME_ID);
				default:
					throw super.newExceptionLinkedEntityIllegal();
				}
			case SCHEMEPROTOELEMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case SCHEMEPROTOELEMENT_CODE:
					return super.getQuery(SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID);
				case SCHEMEPROTOGROUP_CODE:
					return super.getQuery(SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID);
				default:
					throw super.newExceptionLinkedEntityIllegal();
				}
			case SCHEMEPROTOGROUP_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case SCHEMEPROTOGROUP_CODE:
					return super.getQuery(SchemeProtoGroupWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID);
				case UPDIKE_CODE:
					return super.getQuery(SchemeProtoGroupWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID);
				default:
					throw super.newExceptionLinkedEntityIllegal();
				}
			case CABLECHANNELINGITEM_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case SCHEMECABLELINK_CODE:
					return super.getQuery(CableChannelingItemWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID);
				default:
					throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}
}
