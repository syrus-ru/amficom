/*
 * $Id: Populatable.java,v 1.6 2006/06/01 14:26:59 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.logic;

/**
 * @version $Revision: 1.6 $, $Date: 2006/06/01 14:26:59 $
 * @author $Author: stas $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public interface Populatable {

	void populate();

	boolean isPopulated();
}
