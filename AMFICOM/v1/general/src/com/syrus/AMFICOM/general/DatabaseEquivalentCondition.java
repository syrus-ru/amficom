/*
* $Id: DatabaseEquivalentCondition.java,v 1.3 2005/04/13 12:20:24 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.3 $, $Date: 2005/04/13 12:20:24 $
 * @author $Author: arseniy $
 * @module general_v1
 */
class DatabaseEquivalentCondition implements DatabaseStorableObjectCondition {

	private EquivalentCondition delegate;

	DatabaseEquivalentCondition(EquivalentCondition delegate) {
		this.delegate = delegate;
	}

	public Short getEntityCode() {
		return this.delegate.getEntityCode();
	}

	public String getSQLQuery() {
		return TRUE_CONDITION;
	}

}
