/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.1 2005/02/07 10:00:25 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.1 $, $Date: 2005/02/07 10:00:25 $
 * @author $Author: bob $
 * @module admin_v1
 */
public class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	DatabaseTypicalConditionImpl(TypicalCondition typicalCondition){
		super(typicalCondition);
	}
	
	protected String getColumnName() throws IllegalDataException {
		/* check key support */
//		switch(super.condition.getEntityCode().shortValue()) {
//			default:
			throw new IllegalDataException("DatabaseTypicalConditionImpl.getColumnName | entity "
				+ ObjectEntities.codeToString(this.condition.getEntityCode()) + " is not supported.");
//		}
	}

}
