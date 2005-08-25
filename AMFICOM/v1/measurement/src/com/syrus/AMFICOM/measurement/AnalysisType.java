/*-
 * $Id: AnalysisType.java,v 1.94 2005/08/25 20:13:56 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.94 $, $Date: 2005/08/25 20:13:56 $
 * @author $Author: arseniy $
 * @module measurement
 */
public enum AnalysisType implements TransferableObject, ActionType {
	DADARA("dadara",
			EnumSet.of(ParameterType.REFLECTOGRAMMA),
			EnumSet.of(ParameterType.DADARA_CRITERIA),
			EnumSet.of(ParameterType.DADARA_ETALON),
			EnumSet.of(ParameterType.DADARA_ANALYSIS_RESULT, ParameterType.DADARA_ALARMS),
			EnumSet.of(MeasurementType.REFLECTOMETRY)),
	UNKNOWN("unknown",
			EnumSet.noneOf(ParameterType.class),
			EnumSet.noneOf(ParameterType.class),
			EnumSet.noneOf(ParameterType.class),
			EnumSet.noneOf(ParameterType.class),
			EnumSet.noneOf(MeasurementType.class));

	private static final String KEY_ROOT = "AnalysisType.Description.";

	private String codename;
	private EnumSet<ParameterType> inParameterTypes;
	private EnumSet<ParameterType> criteriaParameterTypes;
	private EnumSet<ParameterType> etalonParameterTypes;
	private EnumSet<ParameterType> outParameterTypes;
	private EnumSet<MeasurementType> measurementTypes;
	private String description;


	private AnalysisType(final String codename,
			final EnumSet<ParameterType> inParameterTypes,
			final EnumSet<ParameterType> criParameterTypes,
			final EnumSet<ParameterType> etaParameterTypes,
			final EnumSet<ParameterType> outParameterTypes,
			final EnumSet<MeasurementType> measurementTypes) {
		this.codename = codename;
		this.inParameterTypes = inParameterTypes;
		this.criteriaParameterTypes = criParameterTypes;
		this.etalonParameterTypes = etaParameterTypes;
		this.outParameterTypes = outParameterTypes;
		this.measurementTypes = measurementTypes;
		this.description = LangModelMeasurement.getString(KEY_ROOT + this.codename);
	}

	public static AnalysisType fromInt(final int code) {
		switch (code) {
			case IdlAnalysisType._DADARA:
				return DADARA;
			default:
				Log.errorMessage("Illegal IDL analysis type: " + code);
			return UNKNOWN;
		}
	}

	public static AnalysisType fromTransferable(final IdlAnalysisType idlAnalysisType) {
		return fromInt(idlAnalysisType.value());
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

	public EnumSet<ParameterType> getCriteriaParameterTypes() {
		return this.criteriaParameterTypes.clone();
	}

	public EnumSet<ParameterType> getEtalonParameterTypes() {
		return this.etalonParameterTypes.clone();
	}

	public EnumSet<ParameterType> getOutParameterTypes() {
		return this.outParameterTypes.clone();
	}

	public EnumSet<MeasurementType> getMeasurementTypes() {
		return this.measurementTypes.clone();
	}

	public String getDescription() {
		return this.description;
	}

	@SuppressWarnings("unused")
	public IdlAnalysisType getTransferable(final ORB orb) {
		return IdlAnalysisType.from_int(this.getCode());
	}

}
