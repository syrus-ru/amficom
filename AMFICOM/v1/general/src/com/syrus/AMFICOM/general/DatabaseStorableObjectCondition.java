/*
* $Id: DatabaseStorableObjectCondition.java,v 1.3.4.1 2006/04/10 11:11:20 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.3.4.1 $, $Date: 2006/04/10 11:11:20 $
 * @author $Author: arseniy $
 * @module general
 */
public interface DatabaseStorableObjectCondition {
	String TRUE_CONDITION = "1=1";
	String FALSE_CONDITION = "1=0";

	Short getEntityCode();

	String getSQLQuery() throws IllegalObjectEntityException;
	
}
