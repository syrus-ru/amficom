/*
 * $Id: Characterized.java,v 1.6 2004/08/18 08:46:04 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import com.syrus.AMFICOM.general.Identified;

/**
 * @version $Revision: 1.6 $, $Date: 2004/08/18 08:46:04 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public interface Characterized extends Identified {

	List getCharacteristics();

	void setCharacteristics(List characteristic_ids);
}
