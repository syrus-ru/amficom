/*
 * $Id: XMLPoolContext.java,v 1.2 2005/05/23 08:40:15 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.io.File;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.XMLAdministrationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.XMLConfigurationObjectLoader;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.XMLMapObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.XMLMeasurementObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.XMLResourceObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.XMLSchemeObjectLoader;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/23 08:40:15 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
public final class XMLPoolContext extends PoolContext {
	private static final String KEY_CACHE_PATH = "CachePath";
	private static final String CACHE_PATH = "cache";

	public void init() {
		File cachePath = new File(ApplicationProperties.getString(KEY_CACHE_PATH, CACHE_PATH));
		Class cacheClass = StorableObjectResizableLRUMap.class;

		GeneralStorableObjectPool.init(new XMLGeneralObjectLoader(cachePath), cacheClass);
		AdministrationStorableObjectPool.init(new XMLAdministrationObjectLoader(cachePath), cacheClass);
		ConfigurationStorableObjectPool.init(new XMLConfigurationObjectLoader(cachePath), cacheClass);
		MeasurementStorableObjectPool.init(new XMLMeasurementObjectLoader(cachePath), cacheClass);
		//EventStorableObjectPool.init(new XMLEventObjectLoader(cachePath), cacheClass);
		ResourceStorableObjectPool.init(new XMLResourceObjectLoader(cachePath), cacheClass);
		MapStorableObjectPool.init(new XMLMapObjectLoader(cachePath), cacheClass);
		//MapViewStorableObjectPool.init(new XMLMapViewObjectLoader(cachePath), cacheClass);
		SchemeStorableObjectPool.init(new XMLSchemeObjectLoader(cachePath), cacheClass);
	}
}
