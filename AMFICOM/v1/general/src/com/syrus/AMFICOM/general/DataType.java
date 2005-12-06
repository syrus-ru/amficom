/*-
 * $Id: DataType.java,v 1.14 2005/12/06 09:42:52 bass Exp $
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
import com.syrus.util.IdlTransferableObject;

/**
 * @version $Revision: 1.14 $, $Date: 2005/12/06 09:42:52 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public enum DataType implements IdlTransferableObject<IdlDataType>, Codeable {
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

	public static DataType valueOf(final int code) {
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
				Log.errorMessage("Illegal IDL code: " + code + ", returning RAW");
				return RAW;
		}
	}

	public static DataType fromTransferable(final IdlDataType idlDataType) {
		return valueOf(idlDataType.value());
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
	public IdlDataType getIdlTransferable(final ORB orb) {
		try {
			return IdlDataType.from_int(this.getCode());
		} catch (org.omg.CORBA.BAD_PARAM bp) {
			Log.errorMessage("Illegal code: " + this.getCode() + ", returning RAW");
			return IdlDataType.DATA_TYPE_RAW;
		}
	}
}
