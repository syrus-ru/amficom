/*
* $Id: DatabaseEquivalentCondition.java,v 1.5 2005/08/08 11:27:25 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
 * @module general
 */
final class DatabaseEquivalentCondition implements DatabaseStorableObjectCondition {

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
