/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.8 2005/07/14 20:17:02 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.8 $, $Date: 2005/07/14 20:17:02 $
 * @author $Author: arseniy $
 * @module resource_v1
 */
final class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	@Override
	protected String getColumnName() throws IllegalObjectEntityException {
		String columnName = null;
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.IMAGERESOURCE_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME))
					return StorableObjectWrapper.COLUMN_CODENAME;
				if (this.condition.getKey().equals(ImageResourceWrapper.COLUMN_DATA))
					return ImageResourceWrapper.COLUMN_DATA;
				if (this.condition.getKey().equals(ImageResourceWrapper.COLUMN_SORT))
					return ImageResourceWrapper.COLUMN_SORT;
				break;
			default:
				throw new IllegalObjectEntityException("DatabaseTypicalConditionImpl.getColumnName | entity "
						+ ObjectEntities.codeToString(this.condition.getEntityCode())
						+ " is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return columnName;
	}

}
