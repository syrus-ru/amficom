/*
 * $Id: DatabaseTypicalConditionImpl.java,v 1.18 2005/08/31 05:50:36 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.18 $, $Date: 2005/08/31 05:50:36 $
 * @author $Author: bass $
 * @module map
 */
final class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	@Override
	protected String getColumnName() throws IllegalObjectEntityException {
		String columnName = null;
		/* check key support */
		String key = this.condition.getKey();
		switch (super.condition.getEntityCode().shortValue()) {
			case PHYSICALLINK_CODE:
				if (key.equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				if (key.equals(StorableObjectWrapper.COLUMN_NAME)) {
					return StorableObjectWrapper.COLUMN_NAME;
				}
				if (key.equals(PhysicalLinkWrapper.COLUMN_STREET)) {
					return PhysicalLinkWrapper.COLUMN_STREET;
				}
				break;
			case SITENODE_TYPE_CODE:
				if (key.equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				if (key.equals(StorableObjectWrapper.COLUMN_NAME)) {
					return StorableObjectWrapper.COLUMN_NAME;
				}
				if (key.equals(SiteNodeWrapper.COLUMN_STREET)) {
					return SiteNodeWrapper.COLUMN_STREET;
				}
				break;
			case PHYSICALLINK_TYPE_CODE:
				if (key.equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				break;
			case MAPLIBRARY_CODE:
				if (key.equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				break;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' and key '" + key + "' are not supported.",
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return columnName;
	}

	@Override
	protected String getLinkedColumnName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	@Override
	protected String getLinkedTableName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

}
