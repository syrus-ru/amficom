/*
 * $Id: ConfigurationDatabaseContext.java,v 1.14 2004/08/09 11:12:20 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.14 $, $Date: 2004/08/09 11:12:20 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class ConfigurationDatabaseContext {
	protected static StorableObjectDatabase characteristicDatabase;
	protected static StorableObjectDatabase characteristicTypeDatabase;
	protected static StorableObjectDatabase portDatabase;
	protected static StorableObjectDatabase monitoredElementDatabase;
	protected static StorableObjectDatabase kisDatabase;
	protected static StorableObjectDatabase equipmentDatabase;
	protected static StorableObjectDatabase equipmentTypeDatabase;
	protected static StorableObjectDatabase transmissionPathDatabase;
	protected static StorableObjectDatabase mcmDatabase;
	protected static StorableObjectDatabase serverDatabase;
	
	private ConfigurationDatabaseContext() {	
	}

	public static void init(StorableObjectDatabase characteristicDatabase1,
													StorableObjectDatabase characteristicTypeDatabase1,
													StorableObjectDatabase portDatabase1,
													StorableObjectDatabase monitoredElementDatabase1,
													StorableObjectDatabase kisDatabase1,
													StorableObjectDatabase equipmentDatabase1,
													StorableObjectDatabase equipmentTypeDatabase1,
													StorableObjectDatabase transmissionPathDatabase1,
													StorableObjectDatabase mcmDatabase1,
													StorableObjectDatabase serverDatabase1) {
		characteristicDatabase = characteristicDatabase1;
		characteristicTypeDatabase = characteristicTypeDatabase1;
		portDatabase = portDatabase1;
		monitoredElementDatabase = monitoredElementDatabase1;
		kisDatabase = kisDatabase1;
		equipmentDatabase = equipmentDatabase1;
		equipmentTypeDatabase = equipmentTypeDatabase1;
		transmissionPathDatabase = transmissionPathDatabase1;
		mcmDatabase = mcmDatabase1;
		serverDatabase = serverDatabase1;
	}
}
