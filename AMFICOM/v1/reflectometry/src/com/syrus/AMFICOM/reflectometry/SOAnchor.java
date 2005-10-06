/*-
 * $Id: SOAnchor.java,v 1.1 2005/10/06 16:06:13 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * Иидентификатор для привязки к StorableObject,
 * не зависящий явно от StorableObject Framework.
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/06 16:06:13 $
 * @module
 */
public interface SOAnchor {
	long getValue();
}
