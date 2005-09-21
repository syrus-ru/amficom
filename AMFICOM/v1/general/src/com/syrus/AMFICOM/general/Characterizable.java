/*-
 * $Id: Characterizable.java,v 1.12 2005/09/21 13:42:21 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.12 $, $Date: 2005/09/21 13:42:21 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface Characterizable extends Identifiable {
	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics(final boolean usePool)
	throws ApplicationException;
}
