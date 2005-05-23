/*-
 * $Id: GeneralDatabaseContext.java,v 1.8 2005/05/23 18:45:13 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.8 $, $Date: 2005/05/23 18:45:13 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class GeneralDatabaseContext {
	private static ParameterTypeDatabase		parameterTypeDatabase;
	private static CharacteristicTypeDatabase	characteristicTypeDatabase;
	private static CharacteristicDatabase		characteristicDatabase;

	private GeneralDatabaseContext() {
		assert false;
	}

	public static void init(
			final ParameterTypeDatabase		parameterTypeDatabase1,
			final CharacteristicTypeDatabase	characteristicTypeDatabase1,
			final CharacteristicDatabase		characteristicDatabase1) {
		if (parameterTypeDatabase1 != null)
			parameterTypeDatabase = parameterTypeDatabase1;
		if (characteristicTypeDatabase1 != null)
			characteristicTypeDatabase = characteristicTypeDatabase1;
		if (characteristicDatabase1 != null)
			characteristicDatabase = characteristicDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode ) {
		switch (entityCode) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				return parameterTypeDatabase;
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				return characteristicTypeDatabase;
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				return characteristicDatabase;
			default:
				Log.errorMessage("GeneralDatabaseContext.getDatabase | Unknown entity: " + entityCode);
				return null;
		}
	}
}
