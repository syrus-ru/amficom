/*
 * $Id: Characterizable.java,v 1.5 2005/05/26 15:31:14 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/26 15:31:14 $
 * @author $Author: bass $
 * @module general_v1
 */

public interface Characterizable extends Identifiable {

	Set getCharacteristics();

	void setCharacteristics(final Set characteristics);
	
	void addCharacteristic(final Characteristic characteristic);
	
	void removeCharacteristic(final Characteristic characteristic);

	/**
	 * NEVER call this method directly. Use setCharacteristics
	 * @param characteristics
	 */
	void setCharacteristics0(final Set characteristics);
}
