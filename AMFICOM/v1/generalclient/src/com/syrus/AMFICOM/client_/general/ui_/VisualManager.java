/*-
 * $Id: VisualManager.java,v 1.2 2005/04/27 08:48:13 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/27 08:48:13 $
 * @module generalclient_v1
 */

public interface VisualManager {
	StorableObjectEditor getGeneralPropertiesPanel(); 
	StorableObjectEditor getCharacteristicPropertiesPanel();
	StorableObjectEditor getAdditionalPropertiesPanel();
	ObjectResourceController getController();
}

