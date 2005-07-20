/*-
 * $Id: BsHashChangeListener.java,v 1.4 2005/07/20 14:32:37 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;


/**
 * @author $Author: saa $
 * @version $Revision: 1.4 $, $Date: 2005/07/20 14:32:37 $
 * @module
 */
public interface BsHashChangeListener
{
	void bsHashAdded(String key);
	void bsHashRemoved(String key);
	void bsHashRemovedAll();
}
