/*-
 * $Id: EtalonMTMListener.java,v 1.1 2005/03/31 17:33:52 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/03/31 17:33:52 $
 * @module
 */
public interface EtalonMTMListener
{
	void etalonMTMCUpdated(); // created or changed 
	void etalonMTMRemoved(); // removed
}
