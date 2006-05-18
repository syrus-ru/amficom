/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.20 2005/11/11 09:15:16 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.20 $, $Date: 2005/11/11 09:15:16 $
 * @author $Author: arseniy $
 * @module map
 */
final class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	@Override
	protected String getLinkedThisColumnName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
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

	@Override
	protected boolean isKeySupported(final String key) {
		switch (this.condition.getEntityCode().shortValue()) {
		case PHYSICALLINK_CODE:
			return key == COLUMN_CODENAME
					|| key == COLUMN_NAME
					|| key == PhysicalLinkWrapper.COLUMN_STREET;
		case SITENODE_TYPE_CODE:
			return key == COLUMN_CODENAME
					|| key == COLUMN_NAME
					|| key == SiteNodeWrapper.COLUMN_STREET;
		case PHYSICALLINK_TYPE_CODE:
			return key == COLUMN_CODENAME;
		case MAPLIBRARY_CODE:
			return key == COLUMN_CODENAME;
		default:
			return false;
		}
	}
}
