/*
 * $Id: ConfigurationDatabaseContext.java,v 1.16 2004/08/09 13:52:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.16 $, $Date: 2004/08/09 13:52:07 $
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
	protected static StorableObjectDatabase userDatabase;
	
	private ConfigurationDatabaseContext() {	
	}

	public static void init(StorableObjectDatabase characteristicTypeDatabase1,
													StorableObjectDatabase equipmentTypeDatabase1,
													StorableObjectDatabase characteristicDatabase1,
													StorableObjectDatabase userDatabase1,
													StorableObjectDatabase serverDatabase1,
													StorableObjectDatabase mcmDatabase1,
													StorableObjectDatabase equipmentDatabase1,
													StorableObjectDatabase portDatabase1,
													StorableObjectDatabase transmissionPathDatabase1,
													StorableObjectDatabase monitoredElementDatabase1,
													StorableObjectDatabase kisDatabase1) {
		characteristicTypeDatabase = characteristicTypeDatabase1;
		equipmentTypeDatabase = equipmentTypeDatabase1;
		characteristicDatabase = characteristicDatabase1;
		userDatabase = userDatabase1;
		serverDatabase = serverDatabase1;
		mcmDatabase = mcmDatabase1;
		equipmentDatabase = equipmentDatabase1;
		portDatabase = portDatabase1;
		transmissionPathDatabase = transmissionPathDatabase1;
		kisDatabase = kisDatabase1;
		monitoredElementDatabase = monitoredElementDatabase1;
	}
}
