/*
 * $Id: StorableObjectConditionBuilder.java,v 1.6 2005/07/24 15:01:57 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlStorableObjectConditionSort;
import com.syrus.util.Log;

public final class StorableObjectConditionBuilder {

	private StorableObjectConditionBuilder() {
		// singleton
		assert false;
	}

	public static StorableObjectCondition restoreCondition(final IdlStorableObjectCondition transferable)
			throws IllegalDataException {
		StorableObjectCondition condition = null;
		switch (transferable.discriminator().value()) {
			case IdlStorableObjectConditionSort._COMPOUND:
				condition = new CompoundCondition(transferable.compoundCondition());
				break;
			case IdlStorableObjectConditionSort._LINKED_IDS:
				condition = new LinkedIdsCondition(transferable.linkedIdsCondition());
				break;
			case IdlStorableObjectConditionSort._TYPICAL:
				condition = new TypicalCondition(transferable.typicalCondition());
				break;
			case IdlStorableObjectConditionSort._EQUIVALENT:
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
