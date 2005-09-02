/*
 * $Id: Populatable.java,v 1.5 2005/09/02 14:20:20 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.logic;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/02 14:20:20 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public interface Populatable {

	void populate();

	void repopulate();

	boolean isPopulated();
}
