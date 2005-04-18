/*-
 * $Id: BsHashChangeListener.java,v 1.1 2005/04/18 12:41:05 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.io.BellcoreStructure;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 12:41:05 $
 * @module
 */
public interface BsHashChangeListener // FIXME: rename to BSHashChangeListener
{
	void bsHashAdded(String key, BellcoreStructure bs);
	void bsHashRemoved(String key);
	void bsHashRemovedAll();
}

