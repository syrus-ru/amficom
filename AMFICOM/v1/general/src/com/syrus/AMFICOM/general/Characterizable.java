/*
 * $Id: Characterizable.java,v 1.3 2005/04/01 06:34:57 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.CharacteristicSort;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/01 06:34:57 $
 * @author $Author: bob $
 * @module general_v1
 */

public interface Characterizable extends Identifiable {

	Set getCharacteristics();

	void setCharacteristics(final Collection characteristics);
	
	void addCharacteristic(final Characteristic characteristic);
	
	void removeCharacteristic(final Characteristic characteristic);

	CharacteristicSort getCharacteristicSort();

	/**
	 * NEVER call this method directly. Use setCharacteristics
	 * @param characteristics
	 */
	void setCharacteristics0(final Collection characteristics);
}
