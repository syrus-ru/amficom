/*-
 * $Id: LinkedConditionLoader.java,v 1.1 2005/08/09 21:09:44 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/09 21:09:44 $
 * @author $Author: arseniy $
 * @module filter
 */
public interface LinkedConditionLoader {
	
	Set<StorableObject> loadLinkedCondition(short linkedEntityCode) throws IllegalDataException;

}
