/*
 * $Id: ConfigurationDatabaseContext.java,v 1.18 2004/08/10 19:01:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.18 $, $Date: 2004/08/10 19:01:08 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class ConfigurationDatabaseContext {
	protected static StorableObjectDatabase characteristicTypeDatabase;
	protected static StorableObjectDatabase equipmentTypeDatabase;
	protected static StorableObjectDatabase characteristicDatabase;
	protected static StorableObjectDatabase userDatabase;
	protected static StorableObjectDatabase domainDatabase;
	protected static StorableObjectDatabase serverDatabase;
	protected static StorableObjectDatabase mcmDatabase;
	protected static StorableObjectDatabase equipmentDatabase;
	protected static StorableObjectDatabase portDatabase;
	protected static StorableObjectDatabase transmissionPathDatabase;
	protected static StorableObjectDatabase kisDatabase;
	protected static StorableObjectDatabase monitoredElementDatabase;
	
	private ConfigurationDatabaseContext() {	
	}

	public static void init(StorableObjectDatabase characteristicTypeDatabase1,
													StorableObjectDatabase equipmentTypeDatabase1,
													StorableObjectDatabase characteristicDatabase1,
													StorableObjectDatabase userDatabase1,
													StorableObjectDatabase domainDatabase1,
													StorableObjectDatabase serverDatabase1,
													StorableObjectDatabase mcmDatabase1,
													StorableObjectDatabase equipmentDatabase1,
													StorableObjectDatabase portDatabase1,
													StorableObjectDatabase transmissionPathDatabase1,
													StorableObjectDatabase kisDatabase1,
													StorableObjectDatabase monitoredElementDatabase1) {
		characteristicTypeDatabase = characteristicTypeDatabase1;
		equipmentTypeDatabase = equipmentTypeDatabase1;
		characteristicDatabase = characteristicDatabase1;
		userDatabase = userDatabase1;
		domainDatabase = domainDatabase1;
		serverDatabase = serverDatabase1;
		mcmDatabase = mcmDatabase1;
		equipmentDatabase = equipmentDatabase1;
		portDatabase = portDatabase1;
		transmissionPathDatabase = transmissionPathDatabase1;
		kisDatabase = kisDatabase1;
		monitoredElementDatabase = monitoredElementDatabase1;
	}
}
