/*-
 * $Id: Bug136Remover.java,v 1.1 2006/04/07 13:53:29 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client_.configuration.ui;

import static com.syrus.AMFICOM.configuration.EquipmentTypeCodename.BUG_136;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.Collection;
import java.util.Set;

import com.syrus.AMFICOM.bugs.Crutch136;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2006/04/07 13:53:29 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module schemeclient
 */
@Crutch136(notes = "Stub for SchemeElement without Equipment")
final class Bug136Remover {
	private static StorableObjectCondition equipmentTypeBug136Condition;

	/**
	 * Remove EquipmentType with codename BUG_136.
	 * @todo Fix this bug.
	 */
	public static void removeEquipmentTypeBug136(final Collection<EquipmentType> equipmentTypes) {
		assert equipmentTypes != null : NON_NULL_EXPECTED;
		if (equipmentTypeBug136Condition == null) {
			equipmentTypeBug136Condition = new TypicalCondition(BUG_136.stringValue(),
						OPERATION_EQUALS,
						EQUIPMENT_TYPE_CODE,
						COLUMN_CODENAME);
		}

		try {
			final Set<Identifier> ids = StorableObjectPool.getIdentifiersByCondition(equipmentTypeBug136Condition, true);
			if (!ids.isEmpty()) {
				assert ids.size() == 1 : ONLY_ONE_EXPECTED + ": " + ids;
				final Identifier equipmentTypeBug136Id = ids.iterator().next();
				equipmentTypes.remove(equipmentTypeBug136Id);
			}
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
	}
}
