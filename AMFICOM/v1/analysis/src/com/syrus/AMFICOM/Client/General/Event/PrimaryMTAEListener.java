/*-
 * $Id: PrimaryMTAEListener.java,v 1.2 2005/07/22 08:51:57 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/07/22 08:51:57 $
 * @module
 */
public interface PrimaryMTAEListener
{
	void primaryMTAECUpdated(); // created or changed 
	void primaryMTAERemoved(); // removed
}
