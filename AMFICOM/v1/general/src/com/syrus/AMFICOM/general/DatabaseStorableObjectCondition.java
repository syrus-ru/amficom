/*
* $Id: DatabaseStorableObjectCondition.java,v 1.1 2005/02/03 14:56:10 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.1 $, $Date: 2005/02/03 14:56:10 $
 * @author $Author: bob $
 * @module general_v1
 */
public interface DatabaseStorableObjectCondition {
	
	Short getEntityCode();
	
	String getSQLQuery() throws IllegalDataException;
	
}
