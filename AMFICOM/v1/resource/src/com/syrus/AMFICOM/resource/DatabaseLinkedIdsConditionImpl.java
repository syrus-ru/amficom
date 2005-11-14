/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.1 2005/11/14 06:53:35 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import static com.syrus.AMFICOM.general.ObjectEntities.*;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @version $Revision: 1.1 $, $Date: 2005/11/14 06:53:35 $
 * @author $Author: bob $
 * @module resources
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case LAYOUT_ITEM_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case LAYOUT_ITEM_CODE:
						return super.getQuery(LayoutItemWrapper.COLUMN_PARENT_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				} 				
			default:
				throw super.newExceptionEntityIllegal();
		}
	}

}
