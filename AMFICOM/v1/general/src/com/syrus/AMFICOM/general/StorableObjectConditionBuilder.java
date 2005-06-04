/*
 * $Id: StorableObjectConditionBuilder.java,v 1.4 2005/06/04 16:56:18 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.StorableObjectConditionSort;
import com.syrus.util.Log;

public final class StorableObjectConditionBuilder {

	private StorableObjectConditionBuilder() {
		// singleton
		assert false;
	}

	public static StorableObjectCondition restoreCondition(StorableObjectCondition_Transferable transferable)
			throws IllegalDataException {
		StorableObjectCondition condition = null;
		switch (transferable.discriminator().value()) {
			case StorableObjectConditionSort._COMPOUND:
				condition = new CompoundCondition(transferable.compoundCondition());
				break;
			case StorableObjectConditionSort._LINKED_IDS:
				condition = new LinkedIdsCondition(transferable.linkedIdsCondition());
				break;
			case StorableObjectConditionSort._TYPICAL:
				condition = new TypicalCondition(transferable.typicalCondition());
				break;
			case StorableObjectConditionSort._EQUIVALENT:
				condition = new EquivalentCondition(transferable.equivalentCondition());
				break;
			default:
				String msg = "StorableObjectConditionBuilder.restoreCondition | condition class "
						+ transferable.getClass().getName() + " is not suppoted";
				Log.errorMessage(msg);
				throw new IllegalDataException(msg);
		}
		return condition;
	}
}
