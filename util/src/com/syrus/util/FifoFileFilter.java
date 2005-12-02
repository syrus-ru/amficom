/*-
 * $Id: FifoFileFilter.java,v 1.3 2005/12/02 10:58:12 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.io.File;
import java.io.FileFilter;


/**
 * @version $Revision: 1.3 $, $Date: 2005/12/02 10:58:12 $
 * @author $Author: arseniy $
 * @module util
 */
final class FifoFileFilter implements FileFilter {

	public boolean accept(final File pathname) {
		final String fileName = pathname.getName();
		if (fileName.indexOf(FifoSaver.FILE_SUFFIX) != -1) {
			return true;
		}
		return false;
	}
}
