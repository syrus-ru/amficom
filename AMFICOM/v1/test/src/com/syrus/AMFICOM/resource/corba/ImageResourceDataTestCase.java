/*-
 * $Id: ImageResourceDataTestCase.java,v 1.2 2005/12/15 14:19:42 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource.corba;

import junit.framework.TestCase;

import org.omg.CORBA.BAD_OPERATION;

import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceData;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.IdlFileImageResourceData;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/12/15 14:19:42 $
 * @module idl_v1
 */
public final class ImageResourceDataTestCase extends TestCase {
	private static final String FILE_NAME = "C:\\msdos.sys"; //$NON-NLS-1$

	private static final String ERROR_MESSAGE = "Should have thrown ::CORBA::BAD_OPERATION"; //$NON-NLS-1$
	
	public static void main(String[] args) {
		junit.awtui.TestRunner.run(ImageResourceDataTestCase.class);
	}

	public void testFileName() {
		final IdlImageResourceData imageResourceData = new IdlImageResourceData();
		imageResourceData.image(ImageResourceSort.SCHEME, new byte[0]);
		try {
			imageResourceData.fileImageResourceData();
			fail(ERROR_MESSAGE);
		} catch (final BAD_OPERATION bo) {
			assertTrue(true);
		}
		try {
			imageResourceData.fileImageResourceData(imageResourceData.discriminator(), new IdlFileImageResourceData("codename", FILE_NAME));
			fail(ERROR_MESSAGE);
		} catch (final BAD_OPERATION bo) {
			assertTrue(true);
		}
		imageResourceData.fileImageResourceData(new IdlFileImageResourceData("codename", FILE_NAME));
		imageResourceData.fileImageResourceData();
	}
}
