/*
 * $Id: Characterized.java,v 1.4 2004/07/27 16:03:30 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import com.syrus.AMFICOM.general.Identified;

/**
 * @version $ $, $ $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public interface Characterized extends Identified {

	List getCharacteristicIds();

	void setCharacteristicIds(List characteristic_ids);
}
