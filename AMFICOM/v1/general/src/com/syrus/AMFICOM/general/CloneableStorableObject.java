/*
 * $Id: CloneableStorableObject.java,v 1.3 2005/03/15 17:46:14 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/15 17:46:14 $
 * @module general_v1
 */
public interface CloneableStorableObject extends Cloneable {
	Object clone();
}
