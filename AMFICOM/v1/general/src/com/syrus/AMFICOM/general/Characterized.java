/*
 * $Id: Characterized.java,v 1.4 2005/02/01 07:41:10 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.List;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/01 07:41:10 $
 * @author $Author: bob $
 * @module general_v1
 */

public interface Characterized extends Identified {

	List getCharacteristics();

	void setCharacteristics(List characteristics);
	
	void addCharacteristic(Characteristic characteristic);
	
	void removeCharacteristic(Characteristic characteristic);
}
