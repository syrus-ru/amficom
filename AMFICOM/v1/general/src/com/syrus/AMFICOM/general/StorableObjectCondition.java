/*
 * $Id: StorableObjectCondition.java,v 1.27 2006/03/15 17:36:54 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.util.transport.idl.IdlTransferableObject;


/**
 * @version $Revision: 1.27 $, $Date: 2006/03/15 17:36:54 $
 * @author $Author: bass $
 * @module general
 */
public interface StorableObjectCondition
		extends IdlTransferableObject<IdlStorableObjectCondition> {
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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	IdlStorableObjectCondition getIdlTransferable(final ORB orb);

	IdlStorableObjectCondition getIdlTransferable();
}
