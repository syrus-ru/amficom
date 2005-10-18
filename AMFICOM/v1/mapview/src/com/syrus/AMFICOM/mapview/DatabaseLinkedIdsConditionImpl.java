/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.2 2005/10/18 12:52:06 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mapview;

import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPVIEW_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.COLUMN_MAP_ID;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.2 $, $Date: 2005/10/18 12:52:06 $
 * @module mapview
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}
	
	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case MAPVIEW_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case DOMAIN_CODE:
						return super.getQuery(COLUMN_DOMAIN_ID);
					case MAP_CODE:
						return super.getQuery(COLUMN_MAP_ID);
					default:
						throw newExceptionLinkedEntityIllegal();
				}
			default:
				throw newExceptionEntityIllegal();
		}
	}
}
