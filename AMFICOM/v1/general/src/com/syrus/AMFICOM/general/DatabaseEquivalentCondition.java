/*
* $Id: DatabaseEquivalentCondition.java,v 1.2 2005/02/07 12:59:33 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.2 $, $Date: 2005/02/07 12:59:33 $
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
		return "1=1";
	}

}
