/*
 * $Id: Characterized.java,v 1.1 2005/01/13 14:16:11 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.List;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/13 14:16:11 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public interface Characterized extends Identified {

	List getCharacteristics();

	void setCharacteristics(List characteristic_ids);
}
