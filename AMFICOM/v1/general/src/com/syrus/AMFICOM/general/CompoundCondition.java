/*
 * $Id: CompoundCondition.java,v 1.14 2005/02/17 14:20:14 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.CompoundCondition_Transferable;
import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.TypicalCondition_Transferable;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * Compound condition such as (A & B & C & ... etc), (A | B | C | ... etc) where A, B, C .. are
 * conditions (they can be also compound condition too)
 * 
 * @version $Revision: 1.14 $, $Date: 2005/02/17 14:20:14 $
 * @author $Author: bob $
 * @module general_v1
 */
public class CompoundCondition implements StorableObjectCondition {

	private static final ORB		ORB_INSTANCE	= JavaSoftORBUtil.getInstance().getORB();

	private int						operation;

	/**
	 * Collection&lt;StorableObjectCondition&gt;
	 */
	private Collection				conditions;

	private Short entityCode  = new Short(ObjectEntities.UNKNOWN_ENTITY_CODE);

	private boolean doCompare(	boolean firstResult,
								boolean secondResult) throws ApplicationException {
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
		this(Arrays.asList(new Object[] {firstCondition, secondCondition}) , operation);
	}

	public CompoundCondition(Collection conditions, CompoundConditionSort operation) throws CreateObjectException {
		if (conditions == null)
			throw new CreateObjectException("Unable to create CompoundCondition for null conditions");
		
		if (conditions.size() <= 1)
			throw new CreateObjectException("Unable to create CompoundCondition for alone condition, use condition itself");

		short code = ObjectEntities.UNKNOWN_ENTITY_CODE;

		for (Iterator it = conditions.iterator(); it.hasNext();) {
			Object object = it.next();
			if (object instanceof StorableObjectCondition) {
				StorableObjectCondition condition = (StorableObjectCondition) object;
				if (code == ObjectEntities.UNKNOWN_ENTITY_CODE) {
					this.entityCode = condition.getEntityCode();
					code = this.entityCode.shortValue();
				}
				else if (code != condition.getEntityCode().shortValue())
					throw new CreateObjectException(
													"Unable to create CompoundCondition for conditions for different entities");
			} else
				throw new CreateObjectException(
												"Unable to create CompoundCondition for conditions containing not StorableObjectCondition objects");
		}

		this.operation = operation.value();
		this.conditions = conditions;
	}
	
	protected StorableObjectCondition restoreCondition(Serializable serializable)
			throws IllegalDataException {
		StorableObjectCondition condition;
		if (serializable instanceof TypicalCondition_Transferable) {
			TypicalCondition_Transferable tct = (TypicalCondition_Transferable) serializable;
			condition = new TypicalCondition(tct);
		} else if (serializable instanceof EquivalentCondition_Transferable) {
			EquivalentCondition_Transferable ect = (EquivalentCondition_Transferable) serializable;
			condition = new EquivalentCondition(ect);
		} else if (serializable instanceof LinkedIdsCondition_Transferable) {
			LinkedIdsCondition_Transferable lict = (LinkedIdsCondition_Transferable) serializable;
			condition = new LinkedIdsCondition(lict);
		} else if (serializable instanceof CompoundCondition_Transferable) {
			CompoundCondition_Transferable cct = (CompoundCondition_Transferable) serializable;
			condition = new CompoundCondition(cct);
		} else {
			throw new IllegalDataException("Wrong StorableObjectCondition " + serializable.getClass().getName());
		}
		return condition;
	}

	public CompoundCondition(CompoundCondition_Transferable transferable) throws IllegalDataException {
		this.operation = transferable.sort.value();
		Any[] anies = transferable.innerConditions;
		if (anies.length <= 1)
			throw new IllegalDataException("Unable to create CompoundCondition for " + anies.length + "  condition");
		this.conditions = new ArrayList(anies.length);
		for (int i = 0; i < anies.length; i++) {
			this.conditions.add(this.restoreCondition(anies[i].extract_Value()));
		}
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean firstItem = true;
		boolean result = false;
		
		for (Iterator it = this.conditions.iterator(); it.hasNext();) {
			StorableObjectCondition condition = (StorableObjectCondition) it.next();
			if (firstItem) {
				result = condition.isConditionTrue(object);
				firstItem = false;
			} else {
				result = this.doCompare(result, condition.isConditionTrue(object));
			}
		}
		
		return result;
	}

	public boolean isNeedMore(Collection collection) throws ApplicationException {
		boolean firstItem = true;
		boolean result = false;
		
		for (Iterator it = this.conditions.iterator(); it.hasNext();) {
			StorableObjectCondition condition = (StorableObjectCondition) it.next();
			if (firstItem) {
				result = condition.isNeedMore(collection);
				firstItem = false;
			} else {
				result = this.doCompare(result, condition.isNeedMore(collection));
			}
		}
		return result;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(Short entityCode) {
		throw new UnsupportedOperationException();
	}

	private StorableObjectCondition_Transferable getTransefable(StorableObjectCondition condition) {
		StorableObjectCondition_Transferable transferable = new StorableObjectCondition_Transferable();
		if (condition instanceof TypicalCondition) {
			TypicalCondition typicalCondition = (TypicalCondition) condition;
			transferable.typicalCondition((TypicalCondition_Transferable) typicalCondition.getTransferable());
		} else if (condition instanceof EquivalentCondition) {
			EquivalentCondition equivalentCondition = (EquivalentCondition) condition;
			transferable.equialentCondition((EquivalentCondition_Transferable) equivalentCondition.getTransferable());
		} else if (condition instanceof LinkedIdsCondition) {
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition) condition;
			transferable.linkedIdsCondition((LinkedIdsCondition_Transferable) linkedIdsCondition.getTransferable());
		} else if (condition instanceof CompoundCondition) {
			CompoundCondition compoundCondition = (CompoundCondition) condition;
			transferable.compoundCondition((CompoundCondition_Transferable) compoundCondition.getTransferable());
		}
		return transferable;
	}
	
	public Object getTransferable() {
		CompoundCondition_Transferable transferable = new CompoundCondition_Transferable();
		transferable.sort = CompoundConditionSort.from_int(this.operation);
		transferable.innerConditions = new org.omg.CORBA.Any[this.conditions.size()];
		int i = 0;
		for (Iterator it = this.conditions.iterator(); it.hasNext();i++) {
			StorableObjectCondition condition = (StorableObjectCondition) it.next();
			Any any = ORB_INSTANCE.create_any();
			any.insert_Value(this.getTransefable(condition));
			transferable.innerConditions[i] = any;
		}		
		return transferable;
	}
	

	/**
	 * @return Returns the operation.
	 */
	public int getOperation() {
		return this.operation;
	}

	public Collection getConditions() {
		return Collections.unmodifiableCollection(this.conditions);
	}
}
