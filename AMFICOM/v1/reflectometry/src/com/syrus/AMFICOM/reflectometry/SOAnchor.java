/*-
 * $Id: SOAnchor.java,v 1.4 2005/10/10 09:51:22 saa Exp $
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
 * @author $Author: saa $
 * @version $Revision: 1.4 $, $Date: 2005/10/10 09:51:22 $
 * @module reflectometry
 */
public interface SOAnchor {
	/**
	 * возвращает
	 * long-представление идентификатора StorableObject
	 * @return
	 * long-представление идентификатора StorableObject
	 */
	long getValue();
}
