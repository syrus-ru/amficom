/*
 * $Id: PropertiesMananager.java,v 1.2 2005/03/14 13:36:19 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client_.general.ui_.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/14 13:36:19 $
 * @module schemeclient_v1
 */

public interface PropertiesMananager {
	StorableObjectEditor getGeneralPropertiesPanel(); 
	StorableObjectEditor getCharacteristicPropertiesPanel();
}
