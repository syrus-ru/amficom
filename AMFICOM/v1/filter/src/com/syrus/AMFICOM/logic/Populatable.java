/*
 * $Id: Populatable.java,v 1.3 2005/04/14 13:23:05 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.logic;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/14 13:23:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface Populatable {

	void populate();
	
	boolean isPopulated();
}
