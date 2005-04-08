/*
 * $Id: StorableObjectConditionBuilder.java,v 1.3 2005/04/08 13:00:07 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.CompoundCondition_Transferable;
import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.TypicalCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.StorableObjectConditionSort;
import com.syrus.util.Log;

public final class StorableObjectConditionBuilder {

	private StorableObjectConditionBuilder() {
		// singleton
		assert false;
	}

	public static StorableObjectCondition_Transferable getConditionTransferable(StorableObjectCondition condition) {
		assert condition != null : "Supply EquivalentCondition instead";
		StorableObjectCondition_Transferable conditionTransferable = new StorableObjectCondition_Transferable();
		Object transferable = condition.getTransferable();
		if (condition instanceof LinkedIdsCondition) {
			conditionTransferable.linkedIdsCondition((LinkedIdsCondition_Transferable) transferable);
		} else if (condition instanceof CompoundCondition) {
			conditionTransferable.compoundCondition((CompoundCondition_Transferable) transferable);
		} else if (condition instanceof TypicalCondition) {
			conditionTransferable.typicalCondition((TypicalCondition_Transferable) transferable);
		} else if (condition instanceof EquivalentCondition) {
			conditionTransferable.equialentCondition((EquivalentCondition_Transferable) transferable);
		}
		return conditionTransferable;
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
				condition = new EquivalentCondition(transferable.equialentCondition());
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
