/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.4 2005/04/12 16:40:21 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.4 $, $Date: 2005/04/12 16:40:21 $
 * @author $Author: arseniy $
 * @module config_v1
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() throws IllegalObjectEntityException {
		/* check key support */
//		switch(super.condition.getEntityCode().shortValue()) {
//			default:
		throw new IllegalObjectEntityException("DatabaseTypicalConditionImpl.getColumnName | entity "
				+ ObjectEntities.codeToString(this.condition.getEntityCode())
				+ " is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
//		}
	}

}
