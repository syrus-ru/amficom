/*
 * $Id: ConfigurationDatabaseContext.java,v 1.11 2004/07/30 12:31:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.11 $, $Date: 2004/07/30 12:31:02 $
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
	protected static StorableObjectDatabase serverDatabase;

	public static void init(StorableObjectDatabase characteristicDatabase1,
													StorableObjectDatabase characteristicTypeDatabase1,
													StorableObjectDatabase portDatabase1,
													StorableObjectDatabase monitoredElementDatabase1,
													StorableObjectDatabase kisDatabase1,
													StorableObjectDatabase equipmentTypeDatabase1,
													StorableObjectDatabase mcmDatabase1,
													StorableObjectDatabase serverDatabase1) {
		characteristicDatabase = characteristicDatabase1;
		characteristicTypeDatabase = characteristicTypeDatabase1;
		portDatabase = portDatabase1;
		monitoredElementDatabase = monitoredElementDatabase1;
		kisDatabase = kisDatabase1;
		equipmentTypeDatabase = equipmentTypeDatabase1;
		mcmDatabase = mcmDatabase1;
		serverDatabase = serverDatabase1;
	}
}
