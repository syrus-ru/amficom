/*
* $Id: CompoundCondition.java,v 1.5 2005/01/21 10:37:17 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.io.Serializable;
import java.util.List;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.CompoundCondition_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @todo:
 * TODO: WARNING ! UNTESTED YET !
 * 
 * Compound condition such as (A & B), (A | B) , (A ^ B)
 * where A and B is conditions (they can be also compound condition too)
 *  
 * @version $Revision: 1.5 $, $Date: 2005/01/21 10:37:17 $
 * @author $Author: bob $
 * @module general_v1
 */
public class CompoundCondition implements StorableObjectCondition {

	private static final ORB ORB_INSTANCE =  JavaSoftORBUtil.getInstance().getORB();

	private StorableObjectCondition firstCondition;
	private int operation;
	private StorableObjectCondition secondCondition;
	
	private Short entityCode;
	
	public CompoundCondition(StorableObjectCondition firstCondition, 
	                         CompoundConditionSort operation, 
	                         StorableObjectCondition secondCondition) {
		this.firstCondition = firstCondition;
		this.operation = operation.value();
		this.secondCondition = secondCondition;
	}
	
	
	public CompoundCondition(CompoundCondition_Transferable transferable) throws IllegalDataException {
		this.operation = transferable.sort.value();
		Any[] anies = transferable.innerConditions;
		if (anies.length == 2) {
			Serializable serializable1 = anies[0].extract_Value();
			Serializable serializable2 = anies[1].extract_Value();
			if (serializable1 instanceof StringFieldCondition_Transferable) {
				StringFieldCondition_Transferable stringFieldCondition_Transferable = (StringFieldCondition_Transferable) serializable1;
				this.firstCondition = new StringFieldCondition(stringFieldCondition_Transferable);
			} else if (serializable1 instanceof LinkedIdsCondition_Transferable) {
				LinkedIdsCondition_Transferable linkedIdsCondition_Transferable = (LinkedIdsCondition_Transferable) serializable1;
				this.firstCondition = new LinkedIdsCondition(linkedIdsCondition_Transferable);				
			} else if (serializable1 instanceof CompoundCondition_Transferable) {
				CompoundCondition_Transferable compoundCondition_Transferable = (CompoundCondition_Transferable) serializable1;
				this.firstCondition = new CompoundCondition(compoundCondition_Transferable);
			}
			
			if (serializable2 instanceof StringFieldCondition_Transferable) {
				StringFieldCondition_Transferable stringFieldCondition_Transferable = (StringFieldCondition_Transferable) serializable2;
				this.secondCondition = new StringFieldCondition(stringFieldCondition_Transferable);
			} else if (serializable2 instanceof LinkedIdsCondition_Transferable) {
				LinkedIdsCondition_Transferable linkedIdsCondition_Transferable = (LinkedIdsCondition_Transferable) serializable2;
				this.secondCondition = new LinkedIdsCondition(linkedIdsCondition_Transferable);				
			} else if (serializable2 instanceof CompoundCondition_Transferable) {
				CompoundCondition_Transferable compoundCondition_Transferable = (CompoundCondition_Transferable) serializable2;
				this.secondCondition = new CompoundCondition(compoundCondition_Transferable);
			}
		} else 
			throw new IllegalDataException("Illegal contition count " + anies.length);
	}
	
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean result = false;
		boolean firstResult = this.firstCondition.isConditionTrue(object);
		switch (this.operation) {
			case CompoundConditionSort._AND:
				result = firstResult && this.secondCondition.isConditionTrue(object);
				break;
			case CompoundConditionSort._OR:
				result = firstResult || this.secondCondition.isConditionTrue(object);
				break;
				
			case CompoundConditionSort._XOR:
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
			case CompoundConditionSort._AND:
				result = firstResult && this.secondCondition.isNeedMore(list);
				break;
			case CompoundConditionSort._OR:
				result = firstResult || this.secondCondition.isNeedMore(list);
				break;
				
			case CompoundConditionSort._XOR:
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

		
		Any any1 = ORB_INSTANCE.create_any();
		any1.insert_Value(transferable2);
		transferable.innerConditions[0] = any1;

		Any any2 = ORB_INSTANCE.create_any();
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
