/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.1 2005/04/04 13:17:21 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.DatabaseStorableObjectCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/04/04 13:17:21 $
 * @module scheme_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}

	/**
	 * @throws IllegalDataException
	 * @see DatabaseStorableObjectCondition#getSQLQuery()
	 */
	public String getSQLQuery() throws IllegalDataException {
		throw new UnsupportedOperationException();
	}
}
