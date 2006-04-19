/*-
 * $Id: DataType.java,v 1.19 2006/04/19 13:22:17 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.IdlDataType;
import com.syrus.AMFICOM.general.xml.XmlDataType;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlTransferableObject;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * @version $Revision: 1.19 $, $Date: 2006/04/19 13:22:17 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public enum DataType implements IdlTransferableObject<IdlDataType> {
	INTEGER("integer"),
	DOUBLE("double"),
	STRING("string"),
	DATE("date"),
	LONG("long"),
	RAW("raw"),
	BOOLEAN("boolean");

	private static final DataType VALUES[] = values();

	private static final String KEY_ROOT = "DataType.Description.";

	private final String codename;
	private final String description;


	private DataType(final String codename) {
		this.codename = codename;
		this.description = LangModelGeneral.getString(KEY_ROOT + this.codename);
	}

	/**
	 * @param code
	 * @does_not_throw ArrayIndexOutOfBoundsException
	 */
	public static DataType valueOf(final int code) {
		try {
			return VALUES[code];
		} catch (final ArrayIndexOutOfBoundsException aioobe) {
			Log.errorMessage("Illegal IDL code: " + code + ", returning RAW");
			return RAW;
		}
	}

	/**
	 * @param dataType
	 * @does_not_throw ArrayIndexOutOfBoundsException
	 */
	public static DataType valueOf(final IdlDataType dataType) {
		return valueOf(dataType.value());
	}

	/**
	 * @param dataType
	 * @does_not_throw ArrayIndexOutOfBoundsException
	 */
	public static DataType valueOf(final XmlDataType dataType) {
		return valueOf(dataType.enumValue().intValue() - 1);
	}

	public String getCodename() {
		return this.codename;
	}

	public String getDescription() {
		return this.description;
	}

	public IdlDataType getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public IdlDataType getIdlTransferable() {
		return IdlDataType.from_int(this.ordinal());
	}

	@Override
	public String toString() {
		return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
	}


	/**
	 * A mutable holder for immutable enum instances.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.19 $, $Date: 2006/04/19 13:22:17 $
	 * @module general
	 */
	public static final class Proxy
			implements IdlTransferableObjectExt<IdlDataType>,
			XmlTransferableObject<XmlDataType>, Serializable {
		private static final long serialVersionUID = -2859495371772821057L;

		private DataType value;

		/**
		 * Creates a new uninitialized instance.
		 */
		public Proxy() {
			// empty
		}

		public Proxy(final DataType value) {
			this.value = value;
		}

		public DataType getValue() {
			return this.value;
		}

		public void setValue(final DataType value) {
			this.value = value;
		}

		/**
		 * @param orb
		 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
		 */
		public IdlDataType getIdlTransferable(final ORB orb) {
			return this.value.getIdlTransferable(orb);
		}

		/**
		 * @param dataType
		 * @see IdlTransferableObjectExt#fromIdlTransferable(IDLEntity)
		 */
		public void fromIdlTransferable(final IdlDataType dataType) {
			this.value = valueOf(dataType);
		}

		/**
		 * @param dataType
		 * @param importType
		 * @param usePool
		 * @throws XmlConversionException
		 * @see XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
		 */
		public void getXmlTransferable(
				final XmlDataType dataType,
				final String importType,
				final boolean usePool)
		throws XmlConversionException {
			dataType.set(XmlDataType.Enum.forInt(this.value.ordinal() + 1));
		}

		/**
		 * @param dataType
		 * @param importType
		 * @throws XmlConversionException
		 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
		 */
		public void fromXmlTransferable(
				final XmlDataType dataType,
				final String importType)
		throws XmlConversionException {
			this.value = valueOf(dataType);
		}
	}
}
