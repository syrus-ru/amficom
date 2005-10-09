/*-
 * $Id: SOAnchor.java,v 1.3 2005/10/09 15:04:33 bass Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * Иидентификатор для привязки к StorableObject,
 * не зависящий явно от StorableObject Framework.
 *
 * @author Old Wise Saa
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/09 15:04:33 $
 * @module reflectometry
 */
public interface SOAnchor {
	long getValue();
}
