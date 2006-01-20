/*-
 * $Id: TestContext.java,v 1.1 2006/01/20 17:08:24 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2006/01/20 17:08:24 $
 * @module
 */
public interface TestContext {
	/**
	 * Войти
	 */
	void setUp();
	/**
	 * Выйти
	 */
	void tearDown();
}
