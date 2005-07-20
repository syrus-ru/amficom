/*-
 * $Id: BsHashChangeListener.java,v 1.3 2005/07/20 14:31:22 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.io.BellcoreStructure;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/07/20 14:31:22 $
 * @module
 */
public interface BsHashChangeListener
{
	void bsHashAdded(String key, BellcoreStructure bs);
	void bsHashRemoved(String key);
	void bsHashRemovedAll();
}
