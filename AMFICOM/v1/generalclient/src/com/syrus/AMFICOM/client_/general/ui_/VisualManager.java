/*-
 * $Id: VisualManager.java,v 1.1 2005/03/30 13:29:14 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/30 13:29:14 $
 * @module generalclient_v1
 */

public interface VisualManager {
	StorableObjectEditor getGeneralPropertiesPanel(); 
	StorableObjectEditor getCharacteristicPropertiesPanel();
	ObjectResourceController getController();
}

