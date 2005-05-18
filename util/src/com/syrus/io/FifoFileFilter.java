/*-
 * $Id: FifoFileFilter.java,v 1.1 2005/05/18 09:42:49 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.io;

import java.io.File;
import java.io.FileFilter;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/18 09:42:49 $
 * @author $Author: max $
 * @module util
 */
public class FifoFileFilter implements FileFilter {

	public boolean accept(File pathname) {
		String fileName = pathname.getName();
		if (fileName.indexOf(FIFOSaver.EXTENSION) != -1)
			return true;
		return false;
	}
}
