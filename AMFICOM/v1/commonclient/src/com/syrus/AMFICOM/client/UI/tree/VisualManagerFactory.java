/*-
 * $Id: VisualManagerFactory.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module generalclient_v1
 */

public interface VisualManagerFactory {
	VisualManager getVisualManager(Item item);
}
