/*-
 * $Id: VisualManager.java,v 1.2 2005/08/02 13:03:21 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/02 13:03:21 $
 * @module commonclient
 */

public interface VisualManager {
	StorableObjectEditor getGeneralPropertiesPanel(); 
	StorableObjectEditor getCharacteristicPropertiesPanel();
	StorableObjectEditor getAdditionalPropertiesPanel();
	StorableObjectWrapper getController();
}