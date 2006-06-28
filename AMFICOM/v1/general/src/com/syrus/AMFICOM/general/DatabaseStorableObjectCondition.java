/*
* $Id: DatabaseStorableObjectCondition.java,v 1.4 2006/04/19 13:22:17 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.4 $, $Date: 2006/04/19 13:22:17 $
 * @author $Author: bass $
 * @module general
 */
public interface DatabaseStorableObjectCondition {
	String TRUE_CONDITION = "1=1";
	String FALSE_CONDITION = "1=0";

	Short getEntityCode();

	String getSQLQuery() throws IllegalObjectEntityException;
	
}
