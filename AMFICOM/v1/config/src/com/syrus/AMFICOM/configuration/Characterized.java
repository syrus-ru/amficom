/*
 * $Id: Characterized.java,v 1.7 2004/12/22 08:38:25 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import com.syrus.AMFICOM.general.Identified;

/**
 * @version $Revision: 1.7 $, $Date: 2004/12/22 08:38:25 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public interface Characterized extends Identified {

	List getCharacteristics();

	void setCharacteristics(List characteristics);
	
	void addCharacteristic(Characteristic characteristic);
	
	void removeCharacteristic(Characteristic characteristic);
}
