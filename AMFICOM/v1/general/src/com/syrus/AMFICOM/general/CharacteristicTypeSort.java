/*-
 * $Id: CharacteristicTypeSort.java,v 1.1 2005/12/06 11:31:12 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.util.IdlTransferableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/12/06 11:31:12 $
 * @module general
 */
public enum CharacteristicTypeSort implements IdlTransferableObject<IdlCharacteristicTypeSort> {
	OPTICAL,
	ELECTRICAL,
	OPERATIONAL,
	INTERFACE,
	VISUAL;

	private static final CharacteristicTypeSort VALUES[] = values();

	/**
	 * @param orb
	 * @see IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlCharacteristicTypeSort getIdlTransferable(final ORB orb) {
		return IdlCharacteristicTypeSort.from_int(this.ordinal());
	}

	/**
	 * @param i
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static CharacteristicTypeSort valueOf(final int i) {
		return VALUES[i];
	}

	/**
	 * @param characteristicTypeSort
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static CharacteristicTypeSort valueOf(
			final IdlCharacteristicTypeSort characteristicTypeSort) {
		return valueOf(characteristicTypeSort.value());
	}
}
