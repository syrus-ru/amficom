/*
 * $Id: Characterizable.java,v 1.1 2005/03/04 13:29:36 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.List;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/04 13:29:36 $
 * @author $Author: bass $
 * @module general_v1
 */

public interface Characterizable extends Identifiable {

	List getCharacteristics();

	void setCharacteristics(final List characteristics);
	
	void addCharacteristic(final Characteristic characteristic);
	
	void removeCharacteristic(final Characteristic characteristic);
}
