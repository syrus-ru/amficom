/*-
 * $Id: ModelingType.java,v 1.54 2005/08/26 18:15:03 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.util.EnumSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.measurement.corba.IdlModelingType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.54 $, $Date: 2005/08/26 18:15:03 $
 * @author $Author: arseniy $
 * @module measurement
 */
public enum ModelingType implements TransferableObject, ActionType {
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

	public static ModelingType fromInt(final int code) {
		switch (code) {
			case IdlModelingType._DADARA_MODELING:
				return DADARA_MODELING;
			default:
				Log.errorMessage("Illegal IDL analysis type: " + code);
			return UNKNOWN;
		}
	}

	public static ModelingType fromTransferable(final IdlModelingType idlModelingType) {
		return fromInt(idlModelingType.value());
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

	@SuppressWarnings("unused")
	public IdlModelingType getTransferable(final ORB orb) {
		return IdlModelingType.from_int(this.getCode());
	}

	@Override
	public String toString() {
		return this.getCodename();
	}
}
