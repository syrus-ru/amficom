/*
 * $Id: ConfigurationDatabaseContext.java,v 1.9 2004/07/27 16:03:30 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $ $, $ $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public abstract class ConfigurationDatabaseContext {
	protected static StorableObjectDatabase characteristicDatabase;
	protected static StorableObjectDatabase characteristicTypeDatabase;
	protected static StorableObjectDatabase portDatabase;
	protected static StorableObjectDatabase monitoredElementDatabase;
	protected static StorableObjectDatabase kisDatabase;
	protected static StorableObjectDatabase equipmentTypeDatabase;
	protected static StorableObjectDatabase mcmDatabase;

	public static void init(StorableObjectDatabase characteristicDatabase1,
													StorableObjectDatabase characteristicTypeDatabase1,
													StorableObjectDatabase portDatabase1,
													StorableObjectDatabase monitoredElementDatabase1,
													StorableObjectDatabase kisDatabase1,
													StorableObjectDatabase equipmentTypeDatabase1,
													StorableObjectDatabase mcmDatabase1) {
		characteristicDatabase = characteristicDatabase1;
		characteristicTypeDatabase = characteristicTypeDatabase1;
		portDatabase = portDatabase1;
		monitoredElementDatabase = monitoredElementDatabase1;
		kisDatabase = kisDatabase1;
		equipmentTypeDatabase = equipmentTypeDatabase1;
		mcmDatabase = mcmDatabase1;
	}
}
