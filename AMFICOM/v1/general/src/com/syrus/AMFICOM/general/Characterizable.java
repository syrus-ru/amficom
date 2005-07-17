/*
 * $Id: Characterizable.java,v 1.8 2005/07/17 05:17:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.8 $, $Date: 2005/07/17 05:17:13 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface Characterizable extends Identifiable {

	Set<Characteristic> getCharacteristics() throws ApplicationException;

}
