/*
 * $Id: GeneralDatabaseContext.java,v 1.1 2005/01/13 14:16:11 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/13 14:16:11 $
 * @author $Author: arseniy $
 * @todo Declare all fields private as<ol>
 *       <li>they have public accessors; and</li>
 *       <li>there's {@link #init(StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase)}
 *       method which is supposed to be the only modifier for class' fields.</li></ol>
 * @module general_v1
 */
public final class GeneralDatabaseContext {
	protected static StorableObjectDatabase characteristicTypeDatabase;
	protected static StorableObjectDatabase characteristicDatabase;
    
	private GeneralDatabaseContext() {
		// private constructor 
	}

	public static void init(final StorableObjectDatabase characteristicTypeDatabase1,
												final StorableObjectDatabase characteristicDatabase1) {
		characteristicTypeDatabase = characteristicTypeDatabase1;
		characteristicDatabase = characteristicDatabase1;
	}

	public static StorableObjectDatabase getCharacteristicDatabase() {
		return characteristicDatabase;
	}

	public static StorableObjectDatabase getCharacteristicTypeDatabase() {
		return characteristicTypeDatabase;
	}
}
