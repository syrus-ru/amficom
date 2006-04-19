/*
* $Id: DatabaseEquivalentCondition.java,v 1.6 2006/04/19 13:22:17 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.6 $, $Date: 2006/04/19 13:22:17 $
 * @author $Author: bass $
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
