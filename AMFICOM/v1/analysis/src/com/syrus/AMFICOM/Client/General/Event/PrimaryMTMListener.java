/*-
 * $Id: PrimaryMTMListener.java,v 1.1 2005/03/31 16:58:28 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/03/31 16:58:28 $
 * @module
 */
public interface PrimaryMTMListener
{
	void primaryMTMCUpdated(); // created or changed 
	void primaryMTMRemoved(); // removed
}
