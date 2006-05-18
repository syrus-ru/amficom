/*-
 * $Id: Characterizable.java,v 1.15.8.1 2006/05/18 17:46:35 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.general.StorableObject.StorableObjectContainerWrappee;

/**
 * @version $Revision: 1.15.8.1 $, $Date: 2006/05/18 17:46:35 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface Characterizable extends Identifiable {
	StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee();

	/**
	 * @param characteristic
	 * @throws ApplicationException
	 */
	void addCharacteristic(final Characteristic characteristic)
	throws ApplicationException;

	/**
	 * @param characteristic
	 * @throws ApplicationException
	 */
	void removeCharacteristic(final Characteristic characteristic)
	throws ApplicationException;

	/**
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics()
	throws ApplicationException;

	/**
	 * @param characteristics
	 * @throws ApplicationException
	 */
	void setCharacteristics(final Set<Characteristic> characteristics)
	throws ApplicationException;
}
