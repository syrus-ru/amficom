/*
 * $Id: Characterizable.java,v 1.2 2005/03/05 21:25:04 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;

import com.syrus.AMFICOM.general.corba.CharacteristicSort;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/05 21:25:04 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface Characterizable extends Identifiable {

	Collection getCharacteristics();

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
