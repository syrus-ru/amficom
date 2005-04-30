/*-
 * $Id: BsHashChangeListener.java,v 1.2 2005/04/30 06:14:03 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.io.BellcoreStructure;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/04/30 06:14:03 $
 * @module
 */
public interface BsHashChangeListener
{
	void bsHashAdded(String key, BellcoreStructure bs);
	void bsHashRemoved(String key);
	void bsHashRemovedAll();
}

