/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.2 2005/02/09 11:50:45 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/09 11:50:45 $
 * @author $Author: bob $
 * @module admin_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	public DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalDataException {
		String query = null;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MCM_ENTITY_CODE:
				query = super.getLinkedQuery(MCMWrapper.LINK_COLUMN_MCM_ID, ObjectEntities.KIS_ENTITY,
					StorableObjectWrapper.COLUMN_ID);
				break;
		}
		return query;
	}

}
