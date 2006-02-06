/*-
 * $Id: AnalysisType.java,v 1.107.2.1 2006/02/06 14:46:30 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.EnumSet;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisType;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.107.2.1 $, $Date: 2006/02/06 14:46:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public enum AnalysisType implements IdlTransferableObject<IdlAnalysisType>,
		ActionType<IdlAnalysisType>, Describable {
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

	public static AnalysisType valueOf(final int code) {
		switch (code) {
			case IdlAnalysisType._DADARA:
				return DADARA;
			case IdlAnalysisType._UNKNOWN_ANALYSISTYPE:
				return UNKNOWN;
			default:
				Log.errorMessage("Illegal IDL code: " + code + ", returning UNKNOWN");
				return UNKNOWN;
		}
	}

	public static AnalysisType fromTransferable(final IdlAnalysisType idlAnalysisType) {
		return valueOf(idlAnalysisType.value());
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

	@Deprecated
	public String getName() {
		return this.description;
	}	
	
	public void setDescription(String description) {
		throw new UnsupportedOperationException(
				"AnalysisType.setDescription() is unsupported");		
	}
	
	public void setName(String name) {
		throw new UnsupportedOperationException(
				"AnalysisType.setName() is unsupported");		
	}

	public IdlAnalysisType getIdlTransferable(final ORB orb) {
		try {
			return IdlAnalysisType.from_int(this.ordinal());
		} catch (final BAD_PARAM bp) {
			Log.errorMessage("Illegal code: " + this.ordinal() + ", returning UNKNOWN");
			return IdlAnalysisType.UNKNOWN_ANALYSISTYPE;
		}
	}

	@Override
	public String toString() {
		return this.getCodename();
	}
}
