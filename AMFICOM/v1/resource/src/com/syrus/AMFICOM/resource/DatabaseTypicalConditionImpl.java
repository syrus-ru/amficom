/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.2 2005/02/25 12:06:35 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.2 $, $Date: 2005/02/25 12:06:35 $
 * @author $Author: bass $
 * @module resource_v1
 */
public class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() throws IllegalDataException {
		String columnName = null;
		/* check key support */
		switch(super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				if (this.condition.getKey().equals(StorableObjectWrapper.COLUMN_CODENAME))
					return StorableObjectWrapper.COLUMN_CODENAME;
				if (this.condition.getKey().equals(ImageResourceWrapper.COLUMN_DATA))
					return ImageResourceWrapper.COLUMN_DATA;
				if (this.condition.getKey().equals(ImageResourceWrapper.COLUMN_SORT))
					return ImageResourceWrapper.COLUMN_SORT;
				break;
			default:
				throw new IllegalDataException("DatabaseTypicalConditionImpl.getColumnName | entity " //$NON-NLS-1$
					+ ObjectEntities.codeToString(this.condition.getEntityCode()) + " is not supported."); //$NON-NLS-1$
		}
		return columnName;
	}

}
