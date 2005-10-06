/*-
 * $Id: SOAnchor.java,v 1.8 2005/10/06 14:32:34 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

/**
 * Иидентификатор для привязки к StorableObject,
 * не зависящий явно от StorableObject Framework.
 * @author $Author: saa $
 * @version $Revision: 1.8 $, $Date: 2005/10/06 14:32:34 $
 * @module
 */
public interface SOAnchor {
	long getValue();
}
