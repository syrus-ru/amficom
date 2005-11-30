/*-
 * $Id: FifoFileFilter.java,v 1.1 2005/11/30 15:50:35 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.io.File;
import java.io.FileFilter;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/30 15:50:35 $
 * @author $Author: arseniy $
 * @module util
 */
final class FifoFileFilter implements FileFilter {

	public boolean accept(final File pathname) {
		final String fileName = pathname.getName();
		if (fileName.indexOf(FIFOSaver.EXTENSION) != -1) {
			return true;
		}
		return false;
	}
}
