/*
 * $Id: ImageResourceDataTestCase.java,v 1.1 2004/12/02 14:02:59 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource.corba;

import junit.framework.TestCase;
import org.omg.CORBA.BAD_OPERATION;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/12/02 14:02:59 $
 * @module idl_v1
 */
public final class ImageResourceDataTestCase extends TestCase {
	private static final String FILE_NAME = "C:\\msdos.sys"; //$NON-NLS-1$

	private static final String ERROR_MESSAGE = "Should have thrown ::CORBA::BAD_OPERATION"; //$NON-NLS-1$
	
	public static void main(String[] args) {
		junit.awtui.TestRunner.run(ImageResourceDataTestCase.class);
	}

	public void testFileName() {
		final ImageResourceData imageResourceData = new ImageResourceData();
		imageResourceData.image(ImageResourceSort.BYTES, new byte[0]);
		try {
			imageResourceData.fileName();
			fail(ERROR_MESSAGE);
		} catch (BAD_OPERATION bo) {
			assertTrue(true);
		}
		try {
			imageResourceData.fileName(imageResourceData.discriminator(), FILE_NAME);
			fail(ERROR_MESSAGE);
		} catch (BAD_OPERATION bo) {
			assertTrue(true);
		}
		imageResourceData.fileName(FILE_NAME);
		imageResourceData.fileName();
	}
}
