/*
* $Id: DatabaseEquivalentCondition.java,v 1.4 2005/06/04 16:56:18 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.4 $, $Date: 2005/06/04 16:56:18 $
 * @author $Author: bass $
 * @module general_v1
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
