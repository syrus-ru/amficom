/*-
 * $Id: DataType.java,v 1.1 2005/07/13 16:02:05 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.IdlDataType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/07/13 16:02:05 $
 * @author $Author: arseniy $
 * @module general
 */
public enum DataType implements TransferableObject {
	INTEGER("integer", "Integer"),
	DOUBLE("double", "Double"),
	STRING("string", "String"),
	DATE("date", "Date"),
	LONG("long", "Long"),
	RAW("raw", "Raw"),
	BOOLEAN("boolean", "Boolean");

	private final String codename;
	private final String description;

	private DataType(final String codename, final String description) {
		this.codename = codename;
		this.description = description;
	}

	public static DataType fromInt(final int code) {
		switch (code) {
			case IdlDataType._DATA_TYPE_INTEGER:
				return INTEGER;
			case IdlDataType._DATA_TYPE_DOUBLE:
				return DOUBLE;
			case IdlDataType._DATA_TYPE_STRING:
				return STRING;
			case IdlDataType._DATA_TYPE_DATE:
				return DATE;
			case IdlDataType._DATA_TYPE_LONG:
				return LONG;
			case IdlDataType._DATA_TYPE_RAW:
				return RAW;
			case IdlDataType._DATA_TYPE_BOOLEAN:
				return BOOLEAN;
			default:
				Log.errorMessage("Illegal IDL data type: " + code);
				return RAW;
		}
	}

	public static DataType fromTransferable(final IdlDataType idlDataType) {
		return fromInt(idlDataType.value());
	}

	public int getCode() {
		return this.ordinal();
	}

	public String getCodename() {
		return this.codename;
	}

	public String getDescription() {
		return this.description;
	}

	@SuppressWarnings("unused")
	public IDLEntity getTransferable(final ORB orb) {
		return IdlDataType.from_int(this.getCode());
	}
}
