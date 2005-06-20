/*
 * $Id: Characterizable.java,v 1.6 2005/06/20 17:29:37 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/20 17:29:37 $
 * @author $Author: bass $
 * @module general_v1
 */

public interface Characterizable extends Identifiable {

	Set<Characteristic> getCharacteristics();

	void setCharacteristics(final Set characteristics);
	
	void addCharacteristic(final Characteristic characteristic);
	
	void removeCharacteristic(final Characteristic characteristic);

	/**
	 * NEVER call this method directly. Use setCharacteristics
	 * @param characteristics
	 */
	void setCharacteristics0(final Set<Characteristic> characteristics);
}
