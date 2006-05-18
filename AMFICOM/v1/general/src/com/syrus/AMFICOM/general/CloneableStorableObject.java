/*-
 * $Id: CloneableStorableObject.java,v 1.5 2005/07/29 13:07:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Map;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/07/29 13:07:00 $
 * @module general
 */
public interface CloneableStorableObject extends Cloneable {
	Map<Identifier, Identifier> getClonedIdMap();
}
