/*
* $Id: DatabaseStorableObjectCondition.java,v 1.3 2005/08/08 11:27:25 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
 * @module general
 */
public interface DatabaseStorableObjectCondition {
	String TRUE_CONDITION = "1=1";
	String FALSE_CONDITION = "1=0";
	
	Short getEntityCode();
	
	String getSQLQuery() throws IllegalObjectEntityException;
	
}
