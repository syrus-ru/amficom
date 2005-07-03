/*-
 * $Id: PrimaryMTAEListener.java,v 1.1 2005/04/29 06:41:05 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/04/29 06:41:05 $
 * @module
 */
public interface PrimaryMTAEListener
{
	void primaryMTMCUpdated(); // created or changed 
	void primaryMTMRemoved(); // removed
}
