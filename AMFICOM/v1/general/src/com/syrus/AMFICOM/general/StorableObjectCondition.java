/*
 * $Id: StorableObjectCondition.java,v 1.20 2005/10/06 15:19:44 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;


/**
 * @version $Revision: 1.20 $, $Date: 2005/10/06 15:19:44 $
 * @author $Author: bass $
 * @module general
 */
public interface StorableObjectCondition
		extends TransferableObject<IdlStorableObjectCondition> {
	boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException;

	/**
	 * Returns false if all objects that match this condition are already present
	 * in the local pool, and true otherwise (i. e. when certain objects
	 * <em>do need</em> loading.
	 * 
	 * @param identifiables
	 *        objects (or their identifiers) that
	 *        a) present in the local pool,
	 *        b) supplied to
	 *        {@link StorableObjectPool#getStorableObjectsButIdsByCondition(Set, StorableObjectCondition, boolean)}
	 *        or
	 *        {@link StorableObjectPool#getStorableObjectsButIdsByCondition(Set, StorableObjectCondition, boolean, boolean)}
	 *        as the first argument, i. e. objects that no need to search in pool.
	 * @return true if certain objects need to be loaded, false otherwise.
	 * @see StorableObjectPool#getStorableObjectsButIdsByCondition(Set,
	 *      StorableObjectCondition, boolean, boolean)
	 */
	boolean isNeedMore(final Set<? extends Identifiable> identifiables);

	Short getEntityCode();
	
	void setEntityCode(final Short entityCode) throws IllegalObjectEntityException;

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	IdlStorableObjectCondition getTransferable(final ORB orb);

	IdlStorableObjectCondition getTransferable();
}
