/*
 * $Id: Characterizable.java,v 1.7 2005/06/20 20:54:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/20 20:54:25 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface Characterizable extends Identifiable {

	Set<Characteristic> getCharacteristics();

	void setCharacteristics(final Set<Characteristic> characteristics);
	
	void addCharacteristic(final Characteristic characteristic);
	
	void removeCharacteristic(final Characteristic characteristic);

	/**
	 * NEVER call this method directly. Use setCharacteristics
	 * @param characteristics
	 */
	void setCharacteristics0(final Set<Characteristic> characteristics);
}
