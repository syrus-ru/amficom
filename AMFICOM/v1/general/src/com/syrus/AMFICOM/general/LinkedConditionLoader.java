/*-
 * $Id: LinkedConditionLoader.java,v 1.3 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Collection;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
 * @module general
 */
public interface LinkedConditionLoader {
	
	Collection loadLinkedCondition(short linkedEntityCode) throws IllegalDataException;

}
