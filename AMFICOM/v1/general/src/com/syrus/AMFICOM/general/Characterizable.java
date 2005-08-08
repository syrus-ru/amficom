/*
 * $Id: Characterizable.java,v 1.10 2005/08/08 14:22:15 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.10 $, $Date: 2005/08/08 14:22:15 $
 * @author $Author: arseniy $
 * @module general
 */

public interface Characterizable extends Identifiable {

	Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException;

}
