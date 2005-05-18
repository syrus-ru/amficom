/*-
 * $Id: VisualManager.java,v 1.3 2005/05/18 14:01:19 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/18 14:01:19 $
 * @module generalclient_v1
 */

public interface VisualManager {
	StorableObjectEditor getGeneralPropertiesPanel();
	StorableObjectEditor getCharacteristicPropertiesPanel();
	StorableObjectEditor getAdditionalPropertiesPanel();
	ObjectResourceController getController();
}

