/*
* $Id: CompoundCondition.java,v 1.2 2005/01/21 08:21:41 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.List;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.CompoundCondition_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StringFieldCondition_Transferable;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/21 08:21:41 $
 * @author $Author: bob $
 * @module general_v1
 */
public class CompoundCondition implements StorableObjectCondition {

	public static final int OPERATION_AND 	= 0;
	public static final int OPERATION_OR	= 1;
	public static final int OPERATION_XOR	= 2;
	
	private static final ORB orb =  JavaSoftORBUtil.getInstance().getORB();

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

	public Object getTransferable() {
		CompoundCondition_Transferable transferable = new CompoundCondition_Transferable();
		transferable.innerConditions = new org.omg.CORBA.Any[2];
		StorableObjectCondition_Transferable transferable2 = new StorableObjectCondition_Transferable();
		StorableObjectCondition_Transferable transferable3 = new StorableObjectCondition_Transferable();

		if (this.firstCondition instanceof StringFieldCondition) {
			StringFieldCondition stringFieldCondition = (StringFieldCondition) this.firstCondition;
			transferable2.stringFieldCondition((StringFieldCondition_Transferable) stringFieldCondition.getTransferable());
		} else if (this.firstCondition instanceof LinkedIdsCondition) {
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition) this.firstCondition;
			transferable2.linkedIdsCondition((LinkedIdsCondition_Transferable) linkedIdsCondition.getTransferable());			
		} else if (this.firstCondition instanceof CompoundCondition) {
			CompoundCondition compoundCondition = (CompoundCondition) this.firstCondition;
			transferable2.compoundCondition((CompoundCondition_Transferable) compoundCondition.getTransferable());
		} 

		if (this.secondCondition instanceof StringFieldCondition) {
			StringFieldCondition stringFieldCondition = (StringFieldCondition) this.secondCondition;
			transferable3.stringFieldCondition((StringFieldCondition_Transferable) stringFieldCondition.getTransferable());
		} else if (this.secondCondition instanceof LinkedIdsCondition) {
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition) this.secondCondition;
			transferable3.linkedIdsCondition((LinkedIdsCondition_Transferable) linkedIdsCondition.getTransferable());			
		} else if (this.secondCondition instanceof CompoundCondition) {
			CompoundCondition compoundCondition = (CompoundCondition) this.secondCondition;
			transferable3.compoundCondition((CompoundCondition_Transferable) compoundCondition.getTransferable());
		} 

		
		Any any1 = orb.create_any();
		any1.insert_Value(transferable2);
		transferable.innerConditions[0] = any1;

		Any any2 = orb.create_any();
		any1.insert_Value(transferable3);
		transferable.innerConditions[2] = any2;

		return transferable;
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
