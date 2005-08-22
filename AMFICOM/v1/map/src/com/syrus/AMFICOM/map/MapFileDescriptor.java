/*-
 * $Id: MapFileDescriptor.java,v 1.1 2005/08/22 06:21:18 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/08/22 06:21:18 $
 * @module map
 */

public interface MapFileDescriptor {

	public String getFileName();
	public String getFilePathName();
	
	public long getLength();
	public long getLastModified();
}
