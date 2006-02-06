/*-
 * $Id: EtalonComparisonListener.java,v 1.1 2005/10/17 13:11:25 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/17 13:11:25 $
 * @module
 */
public interface EtalonComparisonListener {
	void etalonComparisonCUpdated();
	void etalonComparisonRemoved();
}
