/*-
 * $Id: DataType.java,v 1.16 2005/12/07 17:16:24 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlDataType;
import com.syrus.util.Codeable;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.16 $, $Date: 2005/12/07 17:16:24 $
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

	private static final DataType VALUES[] = values();

	private final String codename;
	private final String description;


	private DataType(final String codename) {
		this.codename = codename;
		this.description = LangModelGeneral.getString(KEY_ROOT + this.codename);
	}

	public static DataType valueOf(final int code) {
		try {
			return VALUES[code];
		} catch (final ArrayIndexOutOfBoundsException aioobe) {
			/*
			 * Arseniy, if you want error handling here, the task
			 * can be accomplished in a more convenient way:
			 */
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

	public IdlDataType getIdlTransferable(final ORB orb) {
		try {
			return IdlDataType.from_int(this.getCode());
		} catch (final BAD_PARAM bp) {
			Log.errorMessage("Illegal code: " + this.getCode() + ", returning RAW");
			return IdlDataType.DATA_TYPE_RAW;
		}
	}
}
