/*-
 * $Id: GeneralDatabaseContext.java,v 1.6 2005/04/01 10:27:37 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/04/01 10:27:37 $
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
				return getParameterTypeDatabase();
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				return getCharacteristicTypeDatabase();
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				return getCharacteristicDatabase();
			default:
				Log.errorMessage("GeneralDatabaseContext.getDatabase | Unknown entity: " + entityCode); //$NON-NLS-1$
				return null;       
		}
	}

	public static ParameterTypeDatabase getParameterTypeDatabase() {
		return parameterTypeDatabase;
	}

	public static CharacteristicDatabase getCharacteristicDatabase() {
		return characteristicDatabase;
	}

	public static CharacteristicTypeDatabase getCharacteristicTypeDatabase() {
		return characteristicTypeDatabase;
	}
}
