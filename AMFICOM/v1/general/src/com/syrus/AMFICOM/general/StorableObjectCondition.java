/*
 * $Id: StorableObjectCondition.java,v 1.2 2004/10/03 12:44:19 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.2 $, $Date: 2004/10/03 12:44:19 $
 * @author $Author: bob $
 * @module general_v1
 */
public interface StorableObjectCondition {

	boolean isConditionTrue(Object object) throws ApplicationException;	

	Short getEntityCode();
	
	void setEntityCode(Short entityCode);
	
	Object getTransferable();
}
