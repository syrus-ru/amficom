/*
 * $Id: Characterized.java,v 1.2 2005/01/17 08:26:35 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.List;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/17 08:26:35 $
 * @author $Author: bob $
 * @module general_v1
 */

public interface Characterized extends Identified {

	List getCharacteristics();

	void setCharacteristics(List characteristic_ids);
}
