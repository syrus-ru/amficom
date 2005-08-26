/*
 * $Id: DatabaseTypicalConditionImpl.java,v 1.15 2005/08/26 12:11:16 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.15 $, $Date: 2005/08/26 12:11:16 $
 * @author $Author: max $
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
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.PHYSICALLINK_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME)) {
					return StorableObjectWrapper.COLUMN_NAME;
				}
				if (this.condition.getKey().equals(PhysicalLinkWrapper.COLUMN_STREET)) {
					return PhysicalLinkWrapper.COLUMN_STREET;
				}
				break;
			case ObjectEntities.SITENODE_TYPE_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_NAME)) {
					return StorableObjectWrapper.COLUMN_NAME;
				}
				if (this.condition.getKey().equals(SiteNodeWrapper.COLUMN_STREET)) {
					return SiteNodeWrapper.COLUMN_STREET;
				}
				break;
			case ObjectEntities.PHYSICALLINK_TYPE_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				break;
			case ObjectEntities.MAPLIBRARY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME)) {
					return StorableObjectWrapper.COLUMN_CODENAME;
				}
				break;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' and key '" + this.condition.getKey() + "' are not supported.",
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return columnName;
	}

}
