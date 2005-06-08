/*-
 * $Id: FifoFileFilter.java,v 1.2 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.io;

import java.io.File;
import java.io.FileFilter;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/08 13:49:06 $
 * @author $Author: bass $
 * @module util
 */
public final class FifoFileFilter implements FileFilter {

	public boolean accept(File pathname) {
		String fileName = pathname.getName();
		if (fileName.indexOf(FIFOSaver.EXTENSION) != -1)
			return true;
		return false;
	}
}
