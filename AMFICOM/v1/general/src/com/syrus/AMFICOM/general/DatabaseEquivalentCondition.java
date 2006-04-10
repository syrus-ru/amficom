/*
* $Id: DatabaseEquivalentCondition.java,v 1.5.4.1 2006/04/10 11:10:49 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.5.4.1 $, $Date: 2006/04/10 11:10:49 $
 * @author $Author: arseniy $
 * @module general
 */
final class DatabaseEquivalentCondition implements DatabaseStorableObjectCondition {

	private EquivalentCondition delegate;

	DatabaseEquivalentCondition(final EquivalentCondition delegate) {
		this.delegate = delegate;
	}

	public Short getEntityCode() {
		return this.delegate.getEntityCode();
	}

	public String getSQLQuery() {
		return TRUE_CONDITION;
	}

}
