/*
 * $Id: Characterized.java,v 1.5 2004/07/28 12:54:18 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import com.syrus.AMFICOM.general.Identified;

/**
 * @version $Revision: 1.5 $, $Date: 2004/07/28 12:54:18 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public interface Characterized extends Identified {

	List getCharacteristicIds();

	void setCharacteristicIds(List characteristic_ids);
}
