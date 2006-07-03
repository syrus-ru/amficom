/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.3 2006/07/03 12:29:40 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.UPDIKE_CODE;

import com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper;
import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2006/07/03 12:29:40 $
 * @module event
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {
	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}

	/**
	 * @see com.syrus.AMFICOM.general.DatabaseStorableObjectCondition#getSQLQuery()
	 */
	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (this.condition.getEntityCode().shortValue()) {
		case LINEMISMATCHEVENT_CODE:
			switch (this.condition.getLinkedEntityCode()) {
			case LINEMISMATCHEVENT_CODE:
			case UPDIKE_CODE:
				return this.getQuery(LineMismatchEventWrapper.COLUMN_PARENT_LINE_MISMATCH_EVENT_ID);
			case PATHELEMENT_CODE:
				return this.getQuery(LineMismatchEventWrapper.COLUMN_AFFECTED_PATH_ELEMENT_ID);
			default:
				throw this.newExceptionLinkedEntityIllegal();
			}
		default:
			throw super.newExceptionEntityIllegal();
		}
	}
}
