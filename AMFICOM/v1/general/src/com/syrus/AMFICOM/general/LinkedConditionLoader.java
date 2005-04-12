/*-
 * $Id: LinkedConditionLoader.java,v 1.1 2005/04/12 13:12:45 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.List;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/12 13:12:45 $
 * @author $Author: max $
 * @module general_v1
 */
public interface LinkedConditionLoader {
	
	List loadLinkedCondition(short linkedEntityCode) throws IllegalDataException;

}
