/*-
 * $Id: ModelingType.java,v 1.64 2005/12/06 09:45:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.util.EnumSet;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.corba.IdlModelingType;
import com.syrus.util.Log;
import com.syrus.util.IdlTransferableObject;

/**
 * @version $Revision: 1.64 $, $Date: 2005/12/06 09:45:11 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public enum ModelingType implements IdlTransferableObject<IdlModelingType>,
		ActionType<IdlModelingType> {
	DADARA_MODELING("dadara_modeling",
			EnumSet.noneOf(ParameterType.class),
			EnumSet.noneOf(ParameterType.class)),
	UNKNOWN("unknown",
			EnumSet.noneOf(ParameterType.class),
			EnumSet.noneOf(ParameterType.class));

	private static final String KEY_ROOT = "ModelingType.Description.";

	private String codename;
	private EnumSet<ParameterType> inParameterTypes;
	private EnumSet<ParameterType> outParameterTypes;
	private String description;


	private ModelingType(final String codename,
			final EnumSet<ParameterType> inParameterTypes,
			final EnumSet<ParameterType> outParameterTypes) {
		this.codename = codename;
		this.inParameterTypes = inParameterTypes;
		this.outParameterTypes = outParameterTypes;
		this.description = LangModelMeasurement.getString(KEY_ROOT + this.codename);
	}

	public static ModelingType valueOf(final int code) {
		switch (code) {
			case IdlModelingType._DADARA_MODELING:
				return DADARA_MODELING;
			case IdlModelingType._UNKNOWN_MODELINGTYPE:
				return UNKNOWN;
			default:
				Log.errorMessage("Illegal IDL code: " + code + ", returning UNKNOWN");
				return UNKNOWN;
		}
	}

	public static ModelingType fromTransferable(final IdlModelingType idlModelingType) {
		return valueOf(idlModelingType.value());
	}

	public int getCode() {
		return this.ordinal();
	}

	public String getCodename() {
		return this.codename;
	}

	public EnumSet<ParameterType> getInParameterTypes() {
		return this.inParameterTypes.clone();
	}

	public EnumSet<ParameterType> getOutParameterTypes() {
		return this.outParameterTypes.clone();
	}

	public String getDescription() {
		return this.description;
	}

	public IdlModelingType getIdlTransferable(final ORB orb) {
		try {
			return IdlModelingType.from_int(this.getCode());
		} catch (final BAD_PARAM bp) {
			Log.errorMessage("Illegal code: " + this.getCode() + ", returning UNKNOWN");
			return IdlModelingType.UNKNOWN_MODELINGTYPE;
		}
	}

	@Override
	public String toString() {
		return this.getCodename();
	}
}
