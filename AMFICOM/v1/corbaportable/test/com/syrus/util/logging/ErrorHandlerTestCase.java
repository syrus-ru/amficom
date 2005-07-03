/*
 * $Id: ErrorHandlerTestCase.java,v 1.1 2005/06/08 14:24:41 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util.logging;

import junit.framework.TestCase;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/08 14:24:41 $
 * @deprecated
 * @module util
 */
public final class ErrorHandlerTestCase extends TestCase {
	public static void main(String args[]) {
		junit.awtui.TestRunner.run(ErrorHandlerTestCase.class);
	}

	public void testFqnToShortName() {
		System.err.println(ErrorHandler.fqnToShortName(Exception.class));
	}
}
