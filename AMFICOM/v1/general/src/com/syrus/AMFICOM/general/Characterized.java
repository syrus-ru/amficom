/*
 * $Id: Characterized.java,v 1.3 2005/01/28 08:07:22 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.List;

/**
 * @version $Revision: 1.3 $, $Date: 2005/01/28 08:07:22 $
 * @author $Author: bob $
 * @module general_v1
 */

public interface Characterized extends Identified {

	List getCharacteristics();

	void setCharacteristics(List characteristics);
}
