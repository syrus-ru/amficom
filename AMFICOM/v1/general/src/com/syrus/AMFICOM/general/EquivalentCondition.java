/*
* $Id: EquivalentCondition.java,v 1.8 2005/04/08 14:36:15 bob Exp $
*
* Copyright ø 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Set;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;


/**
 * Equivalent (identical, allways true) condition
 * written with especial cynicism
 * @version $Revision: 1.8 $, $Date: 2005/04/08 14:36:15 $
 * @author ÷÷œ‘ $Author: bob $
 * @module general_v1
 */
public final class EquivalentCondition implements StorableObjectCondition {

	private Short entityCode;
	
	public EquivalentCondition(short entityCode) {
		this(new Short(entityCode));
	}
	
	public EquivalentCondition(Short entityCode) {
		this.entityCode = entityCode;
	}

	public EquivalentCondition(EquivalentCondition_Transferable transferable) {
		this(transferable.entity_code);
	}

	public boolean isConditionTrue(final StorableObject storableObject) {
		return true;
	}

	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
	
	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(final Short entityCode) {
		this.entityCode = entityCode;

	}
	
	public void setEntityCode(final short entityCode) {
		this.setEntityCode(new Short(entityCode));

	}

	public IDLEntity getTransferable() {
		return new EquivalentCondition_Transferable(this.entityCode.shortValue());
	}

}
