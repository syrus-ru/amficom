/*
 * $Id: PropertiesMananager.java,v 1.1 2005/03/10 08:09:08 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/10 08:09:08 $
 * @module schemeclient_v1
 */

public interface PropertiesMananager {
	ObjectResourcePropertiesPane getGeneralPropertiesPanel(); 
	ObjectResourcePropertiesPane getCharacteristicPropertiesPanel();
}
