/*
* $Id: CompoundCondition.java,v 1.1 2005/01/20 15:08:28 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.List;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/20 15:08:28 $
 * @author $Author: bob $
 * @module general_v1
 */
public class CompoundCondition implements StorableObjectCondition {

	public static final int OPERATION_AND 	= 0;
	public static final int OPERATION_OR	= 1;
	public static final int OPERATION_XOR	= 2;

	private StorableObjectCondition firstCondition;
	private int operation;
	private StorableObjectCondition secondCondition;
	
	private Short entityCode;
	
	public CompoundCondition(StorableObjectCondition firstCondition, 
	                         int operation, 
	                         StorableObjectCondition secondCondition) throws IllegalDataException {
		if (firstCondition == null || operation < OPERATION_AND 
				|| operation > OPERATION_XOR || secondCondition == null)
			throw new IllegalDataException("Illegal input parameters");
		
		this.firstCondition = firstCondition;
		this.operation = operation;
		this.secondCondition = secondCondition;
	}
	
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean result = false;
		boolean firstResult = this.firstCondition.isConditionTrue(object);
		switch (this.operation) {
			case OPERATION_AND:
				result = firstResult && this.secondCondition.isConditionTrue(object);
				break;
			case OPERATION_OR:
				result = firstResult || this.secondCondition.isConditionTrue(object);
				break;
				
			case OPERATION_XOR:
				boolean secondResult = this.secondCondition.isConditionTrue(object);
				result = (!(firstResult && secondResult)) && (firstResult && secondResult); 
				break;
				
		}
		return result;
	}

	public boolean isNeedMore(List list) throws ApplicationException {
		boolean result = false;
		boolean firstResult = this.firstCondition.isNeedMore(list);
		switch (this.operation) {
			case OPERATION_AND:
				result = firstResult && this.secondCondition.isNeedMore(list);
				break;
			case OPERATION_OR:
				result = firstResult || this.secondCondition.isNeedMore(list);
				break;
				
			case OPERATION_XOR:
				boolean secondResult = this.secondCondition.isNeedMore(list);
				result = (!(firstResult && secondResult)) && (firstResult && secondResult); 
				break;
				
		}
		return result;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(Short entityCode) {
		this.entityCode = entityCode;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#getTransferable()
	 */
	public Object getTransferable() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return Returns the firstCondition.
	 */
	public StorableObjectCondition getFirstCondition() {
		return this.firstCondition;
	}
	/**
	 * @return Returns the operation.
	 */
	public int getOperation() {
		return this.operation;
	}
	/**
	 * @return Returns the secondCondition.
	 */
	public StorableObjectCondition getSecondCondition() {
		return this.secondCondition;
	}
}
