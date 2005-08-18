/*-
 * $Id: FileLoader.java,v 1.3 2005/08/18 14:03:02 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//import com.syrus.util.Log;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.3 $, $Date: 2005/08/18 14:03:02 $
 * @module util
 */

public class FileLoader {
	
	public static final int BUFF_SIZE = 1024*1024;
	public static final byte[] NULL_STUB = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	
	public static byte[] fileToByte(final String pathName, final long offset) throws IOException {
		final File file = new File(pathName);
		file.length();
		final FileInputStream fis = new FileInputStream(file);
		byte[] fileBuffer = new byte[BUFF_SIZE];
		fis.skip(offset);
		int complition = fis.read(fileBuffer);
		if (complition != fileBuffer.length) {
			if (complition == -1) {
				return NULL_STUB; 
			}
			byte[] endOfFileBuff = new byte[complition];
			System.arraycopy(fileBuffer, 0, endOfFileBuff, 0, endOfFileBuff.length);
			return endOfFileBuff;
		}
		fis.close();
		return fileBuffer;					
	}
}
