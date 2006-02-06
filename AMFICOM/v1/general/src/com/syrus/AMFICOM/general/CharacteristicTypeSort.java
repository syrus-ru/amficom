/*-
 * $Id: CharacteristicTypeSort.java,v 1.4.2.1 2006/02/06 14:44:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicTypeSort;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.4.2.1 $, $Date: 2006/02/06 14:44:02 $
 * @module general
 */
public enum CharacteristicTypeSort {
	OPTICAL,
	ELECTRICAL,
	OPERATIONAL,
	INTERFACE,
	VISUAL;

	private static final CharacteristicTypeSort VALUES[] = values();

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

	/**
	 * @param characteristicTypeSort
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static CharacteristicTypeSort valueOf(
			final XmlCharacteristicTypeSort characteristicTypeSort) {
		return valueOf(characteristicTypeSort.enumValue().intValue() - 1);
	}

	/**
	 * A mutable holder for immutable enum instances.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: arseniy $
	 * @version $Revision: 1.4.2.1 $, $Date: 2006/02/06 14:44:02 $
	 * @module general
	 */
	public static final class Proxy
			implements IdlTransferableObjectExt<IdlCharacteristicTypeSort>,
			XmlTransferableObject<XmlCharacteristicTypeSort> {
		private static final long serialVersionUID = -2859495371772821057L;

		private CharacteristicTypeSort value;

		/**
		 * Creates a new uninitialized instance.
		 */
		public Proxy() {
			// empty
		}

		public Proxy(final CharacteristicTypeSort value) {
			this.value = value;
		}

		public CharacteristicTypeSort getValue() {
			return this.value;
		}

		public void setValue(final CharacteristicTypeSort value) {
			this.value = value;
		}

		/**
		 * @param orb
		 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
		 */
		public IdlCharacteristicTypeSort getIdlTransferable(final ORB orb) {
			return IdlCharacteristicTypeSort.from_int(this.value.ordinal());
		}

		/**
		 * @param characteristicTypeSort
		 * @see IdlTransferableObjectExt#fromIdlTransferable(IDLEntity)
		 */
		public void fromIdlTransferable(final IdlCharacteristicTypeSort characteristicTypeSort) {
			this.value = valueOf(characteristicTypeSort);
		}

		/**
		 * @param characteristicTypeSort
		 * @param importType
		 * @param usePool
		 * @throws XmlConversionException
		 * @see XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
		 */
		public void getXmlTransferable(
				final XmlCharacteristicTypeSort characteristicTypeSort,
				final String importType,
				final boolean usePool)
		throws XmlConversionException {
			characteristicTypeSort.set(XmlCharacteristicTypeSort.Enum.forInt(this.value.ordinal() + 1));
		}

		/**
		 * @param characteristicTypeSort
		 * @param importType
		 * @throws XmlConversionException
		 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
		 */
		public void fromXmlTransferable(
				final XmlCharacteristicTypeSort characteristicTypeSort,
				final String importType)
		throws XmlConversionException {
			this.value = valueOf(characteristicTypeSort);
		}
	}
}
