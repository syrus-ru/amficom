/*
 * $Id: GeneralDatabaseContext.java,v 1.3 2005/01/19 20:42:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.3 $, $Date: 2005/01/19 20:42:59 $
 * @author $Author: arseniy $
 * @todo Declare all fields private as<ol>
 *       <li>they have public accessors; and</li>
 *       <li>there's {@link #init(StorableObjectDatabase, StorableObjectDatabase)}
 *       method which is supposed to be the only modifier for class' fields.</li></ol>
 * @module general_v1
 */
public final class GeneralDatabaseContext {
	protected static StorableObjectDatabase	parameterTypeDatabase;
	protected static StorableObjectDatabase characteristicTypeDatabase;
	protected static StorableObjectDatabase characteristicDatabase;

	private GeneralDatabaseContext() {
		// private constructor 
	}

	public static void init(final StorableObjectDatabase parameterTypeDatabase1,
													final StorableObjectDatabase characteristicTypeDatabase1,
													final StorableObjectDatabase characteristicDatabase1) {
		if (parameterTypeDatabase1 != null)
			parameterTypeDatabase = parameterTypeDatabase1;

		if (characteristicTypeDatabase1 != null)
			characteristicTypeDatabase = characteristicTypeDatabase1;

		if (characteristicDatabase1 != null)
			characteristicDatabase = characteristicDatabase1;
	}

	public static StorableObjectDatabase getDatabase(short entityCode ) {
		switch (entityCode) {

			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				return parameterTypeDatabase;

			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				return characteristicTypeDatabase;

			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				return characteristicDatabase;

			default:
				return null;       
		}
	}

	public static StorableObjectDatabase getParameterTypeDatabase() {
		return parameterTypeDatabase;
	}

	public static StorableObjectDatabase getCharacteristicDatabase() {
		return characteristicDatabase;
	}

	public static StorableObjectDatabase getCharacteristicTypeDatabase() {
		return characteristicTypeDatabase;
	}
}
