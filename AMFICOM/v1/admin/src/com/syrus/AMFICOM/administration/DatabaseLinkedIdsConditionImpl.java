/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.1 2005/02/08 15:13:34 bob Exp $
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

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/08 15:13:34 $
 * @author $Author: bob $
 * @module admin_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	public DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	protected String getColumnName(short entityCode) {
		return null;
	}

	public String getSQLQuery() throws IllegalDataException {
		String query = null;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MCM_ENTITY_CODE:
				query = super.getLinkedQuery(MCMWrapper.LINK_COLUMN_MCM_ID, ObjectEntities.KIS_ENTITY);
				break;
		}
		return query;
	}

}
