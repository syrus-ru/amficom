/*
 * $Id: Populatable.java,v 1.4 2005/08/08 11:37:22 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.logic;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:37:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public interface Populatable {

	void populate();
	
	boolean isPopulated();
}
