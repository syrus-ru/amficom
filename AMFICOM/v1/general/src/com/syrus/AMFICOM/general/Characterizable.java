/*-
 * $Id: Characterizable.java,v 1.13 2005/09/23 11:45:45 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.general.StorableObject.StorableObjectContainerWrappee;

/**
 * @version $Revision: 1.13 $, $Date: 2005/09/23 11:45:45 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface Characterizable extends Identifiable {
	StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee();

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
