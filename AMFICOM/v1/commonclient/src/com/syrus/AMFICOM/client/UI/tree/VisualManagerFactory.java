/*-
 * $Id: VisualManagerFactory.java,v 1.2 2005/08/11 18:51:08 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/11 18:51:08 $
 * @module commonclient
 */

public interface VisualManagerFactory {
	VisualManager getVisualManager(Item item);
}
