/*-
 * $Id: Characterizable.java,v 1.15 2005/10/05 13:43:32 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.general.StorableObject.StorableObjectContainerWrappee;

/**
 * @version $Revision: 1.15 $, $Date: 2005/10/05 13:43:32 $
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
