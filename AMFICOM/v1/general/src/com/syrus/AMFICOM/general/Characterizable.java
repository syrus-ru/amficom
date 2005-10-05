/*-
 * $Id: Characterizable.java,v 1.14 2005/10/05 13:10:16 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.14 $, $Date: 2005/10/05 13:10:16 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface Characterizable extends Identifiable {
	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 */
	void addCharacteristic(final Characteristic characteristic, final boolean usePool)
	throws ApplicationException;

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 */
	void removeCharacteristic(final Characteristic characteristic, final boolean usePool)
	throws ApplicationException;

	/**
	 * @param usePool
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics(final boolean usePool)
	throws ApplicationException;

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setCharacteristics(final Set<Characteristic> characteristics, final boolean usePool)
	throws ApplicationException;
}
