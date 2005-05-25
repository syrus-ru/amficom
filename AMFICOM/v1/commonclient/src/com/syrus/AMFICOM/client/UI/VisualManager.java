/*-
 * $Id: VisualManager.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module commonclient_v1
 */

public interface VisualManager {
	StorableObjectEditor getGeneralPropertiesPanel(); 
	StorableObjectEditor getCharacteristicPropertiesPanel();
	StorableObjectEditor getAdditionalPropertiesPanel();
	StorableObjectWrapper getController();
}