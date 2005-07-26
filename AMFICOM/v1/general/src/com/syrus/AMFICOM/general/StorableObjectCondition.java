/*
 * $Id: StorableObjectCondition.java,v 1.17 2005/07/26 18:09:04 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;


/**
 * @version $Revision: 1.17 $, $Date: 2005/07/26 18:09:04 $
 * @author $Author: bass $
 * @module general_v1
 */
public interface StorableObjectCondition extends TransferableObject {
	boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException;
	
	/**
	 * Returns false if all objects that match this condition are already
	 * present in the local pool, and true otherwise (i. e. when certain
	 * objects <em>do need</em> loading.
	 *
	 * @param storableObjects objects present in the local pool.
	 * @return true if certain objects need to be loaded, false otherwise.
	 * @see StorableObjectPool#getStorableObjectsButIdsByCondition(Set, StorableObjectCondition, boolean, boolean)
	 */
	boolean isNeedMore(final Set<? extends StorableObject> storableObjects);

	Short getEntityCode();
	
	void setEntityCode(final Short entityCode) throws IllegalObjectEntityException;

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	IdlStorableObjectCondition getTransferable(final ORB orb);

	IdlStorableObjectCondition getTransferable();
}
