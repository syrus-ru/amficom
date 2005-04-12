/*-
 * $Id: LinkedConditionLoader.java,v 1.2 2005/04/12 13:19:12 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Collection;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/12 13:19:12 $
 * @author $Author: max $
 * @module general_v1
 */
public interface LinkedConditionLoader {
	
	Collection loadLinkedCondition(short linkedEntityCode) throws IllegalDataException;

}
