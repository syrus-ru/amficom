/*-
 * $Id: DataType.java,v 1.5 2005/08/28 13:48:40 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlDataType;
import com.syrus.util.Codeable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/28 13:48:40 $
 * @author $Author: arseniy $
 * @module general
 */
public enum DataType implements TransferableObject, Codeable {
	INTEGER("integer"),
	DOUBLE("double"),
	STRING("string"),
	DATE("date"),
	LONG("long"),
	RAW("raw"),
	BOOLEAN("boolean");

	private static final String KEY_ROOT = "DataType.Description.";

	private final String codename;
	private final String description;


	private DataType(final String codename) {
		this.codename = codename;
		this.description = LangModelGeneral.getString(KEY_ROOT + this.codename);
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
	public IdlDataType getTransferable(final ORB orb) {
		return IdlDataType.from_int(this.getCode());
	}
}
