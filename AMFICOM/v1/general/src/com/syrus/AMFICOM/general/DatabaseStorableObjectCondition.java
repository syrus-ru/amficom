/*
* $Id: DatabaseStorableObjectCondition.java,v 1.2 2005/04/12 16:33:30 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.2 $, $Date: 2005/04/12 16:33:30 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public interface DatabaseStorableObjectCondition {
	String TRUE_CONDITION = "1=1";
	String FALSE_CONDITION = "1=0";
	
	Short getEntityCode();
	
	String getSQLQuery() throws IllegalObjectEntityException;
	
}
