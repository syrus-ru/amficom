/*
 * $Id: StorableObjectCondition.java,v 1.1 2004/09/30 14:34:56 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.1 $, $Date: 2004/09/30 14:34:56 $
 * @author $Author: bob $
 * @module general_v1
 */
public interface StorableObjectCondition {

	public boolean isConditionTrue(Object object) throws ApplicationException;	

	public Short getEntityCode();
	
	public void setEntityCode(Short entityCode);
	
}
