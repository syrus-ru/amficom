/*-
 * $Id: XMLMapViewObjectLoader.java,v 1.1 2005/06/03 17:57:41 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.io.File;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.XMLObjectLoader;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/03 17:57:41 $
 * @module csbridge_v1
 */
public final class XMLMapViewObjectLoader extends XMLObjectLoader implements MapViewObjectLoader {
	private StorableObjectXML mapViewXML;

	public XMLMapViewObjectLoader(final File path) {
		final StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "mapview");
		this.mapViewXML = new StorableObjectXML(driver);
	}

	public void delete(Set identifiables) {
		throw new UnsupportedOperationException();
	}

	public Set loadMapViews(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadMapViewsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public Set refresh(Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public void saveMapViews(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
