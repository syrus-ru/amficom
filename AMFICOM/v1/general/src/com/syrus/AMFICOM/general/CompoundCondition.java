/*
 * $Id: CompoundCondition.java,v 1.12 2005/02/07 09:06:23 bob Exp $
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
import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.TypicalCondition_Transferable;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.util.Log;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * Compound condition such as (A & B), (A | B) , (A ^ B) where A and B is
 * conditions (they can be also compound condition too)
 * 
 * @version $Revision: 1.12 $, $Date: 2005/02/07 09:06:23 $
 * @author $Author: bob $
 * @module general_v1
 */
public class CompoundCondition implements StorableObjectCondition {

	private static final ORB		ORB_INSTANCE	= JavaSoftORBUtil.getInstance().getORB();

	private StorableObjectCondition	firstCondition;
	private int						operation;
	private StorableObjectCondition	secondCondition;

	// private Short entityCode;

	private boolean doCompare(boolean firstResult, boolean secondResult) throws ApplicationException {
		switch (this.operation) {
			case CompoundConditionSort._AND:
				return firstResult && secondResult;
			case CompoundConditionSort._OR:
				return firstResult || secondResult;
			default:
				throw new ApplicationException("CompoundCondition.doComapare Unsupported operation " + this.operation);
		}
	}

	public CompoundCondition(StorableObjectCondition firstCondition,
			CompoundConditionSort operation,
			StorableObjectCondition secondCondition) throws CreateObjectException {
		if (firstCondition.getEntityCode().intValue() != secondCondition.getEntityCode().intValue()) { throw new CreateObjectException(
																																		"Unable to create CompoundCondition for conditions with different entity codes"); }
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
			if (serializable1 instanceof TypicalCondition_Transferable) {
				TypicalCondition_Transferable tct = (TypicalCondition_Transferable) serializable1;
				this.firstCondition = new TypicalCondition(tct);
			} else if (serializable1 instanceof EquivalentCondition_Transferable) {
				EquivalentCondition_Transferable ect = (EquivalentCondition_Transferable) serializable1;
				this.firstCondition = new EquivalentCondition(ect);
			} else if (serializable1 instanceof LinkedIdsCondition_Transferable) {
				LinkedIdsCondition_Transferable lict = (LinkedIdsCondition_Transferable) serializable1;
				this.firstCondition = new LinkedIdsCondition(lict);
			} else if (serializable1 instanceof CompoundCondition_Transferable) {
				CompoundCondition_Transferable cct = (CompoundCondition_Transferable) serializable1;
				this.firstCondition = new CompoundCondition(cct);
			} else {
				throw new IllegalDataException("Wrong StorableObjectCondition " + serializable1.getClass().getName());
			}

			if (serializable1 instanceof TypicalCondition_Transferable) {
				TypicalCondition_Transferable tct = (TypicalCondition_Transferable) serializable1;
				this.secondCondition = new TypicalCondition(tct);
			} else if (serializable1 instanceof EquivalentCondition_Transferable) {
				EquivalentCondition_Transferable ect = (EquivalentCondition_Transferable) serializable1;
				this.secondCondition = new EquivalentCondition(ect);
			} else if (serializable2 instanceof LinkedIdsCondition_Transferable) {
				LinkedIdsCondition_Transferable lict = (LinkedIdsCondition_Transferable) serializable2;
				this.secondCondition = new LinkedIdsCondition(lict);
			} else if (serializable2 instanceof CompoundCondition_Transferable) {
				CompoundCondition_Transferable cct = (CompoundCondition_Transferable) serializable2;
				this.secondCondition = new CompoundCondition(cct);
			} else {
				throw new IllegalDataException("Wrong StorableObjectCondition " + serializable2.getClass().getName());
			}

		} else
			throw new IllegalDataException("Illegal condition count " + anies.length);
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean firstResult = this.firstCondition.isConditionTrue(object);
		Log.debugMessage("CompoundCondition.isConditionTrue | firstCondition is " + firstResult, Log.INFO);
		boolean secondResult = this.secondCondition.isConditionTrue(object);
		Log.debugMessage("CompoundCondition.isConditionTrue | secondCondition is " + secondResult, Log.INFO);
		boolean result = doCompare(firstResult, secondResult);
		Log.debugMessage("CompoundCondition.isConditionTrue | result is " + result, Log.INFO);
		return result;
	}

	public boolean isNeedMore(List list) throws ApplicationException {
		boolean firstResult = this.firstCondition.isNeedMore(list);
		boolean secondResult = this.secondCondition.isNeedMore(list);
		return doCompare(firstResult, secondResult);
	}

	public Short getEntityCode() {
		return this.firstCondition.getEntityCode();
	}

	public void setEntityCode(Short entityCode) {
		throw new UnsupportedOperationException();
	}

	public Object getTransferable() {
		CompoundCondition_Transferable transferable = new CompoundCondition_Transferable();
		transferable.innerConditions = new org.omg.CORBA.Any[2];
		StorableObjectCondition_Transferable transferable2 = new StorableObjectCondition_Transferable();
		StorableObjectCondition_Transferable transferable3 = new StorableObjectCondition_Transferable();

		if (this.firstCondition instanceof TypicalCondition) {
			TypicalCondition typicalCondition = (TypicalCondition) this.firstCondition;
			transferable2.typicalCondition((TypicalCondition_Transferable) typicalCondition.getTransferable());
		} else if (this.firstCondition instanceof EquivalentCondition) {
			EquivalentCondition equivalentCondition = (EquivalentCondition) this.firstCondition;
			transferable2.equialentCondition((EquivalentCondition_Transferable) equivalentCondition.getTransferable());
		} else if (this.firstCondition instanceof LinkedIdsCondition) {
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition) this.firstCondition;
			transferable2.linkedIdsCondition((LinkedIdsCondition_Transferable) linkedIdsCondition.getTransferable());
		} else if (this.firstCondition instanceof CompoundCondition) {
			CompoundCondition compoundCondition = (CompoundCondition) this.firstCondition;
			transferable2.compoundCondition((CompoundCondition_Transferable) compoundCondition.getTransferable());
		}

		if (this.secondCondition instanceof TypicalCondition) {
			TypicalCondition typicalCondition = (TypicalCondition) this.secondCondition;
			transferable2.typicalCondition((TypicalCondition_Transferable) typicalCondition.getTransferable());
		} else if (this.secondCondition instanceof EquivalentCondition) {
			EquivalentCondition equivalentCondition = (EquivalentCondition) this.secondCondition;
			transferable2.equialentCondition((EquivalentCondition_Transferable) equivalentCondition.getTransferable());
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
