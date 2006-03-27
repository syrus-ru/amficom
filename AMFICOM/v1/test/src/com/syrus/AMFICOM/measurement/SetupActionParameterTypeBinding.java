/*-
 * $Id: SetupActionParameterTypeBinding.java,v 1.1.2.4 2006/03/27 09:36:00 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.measurement.AnalysisTypeCodename.DADARA;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename.REFLECTOMETRY_PK7600;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename.REFLECTOMETRY_QP1640A;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename.REFLECTOMETRY_QP1643A;
import static com.syrus.AMFICOM.measurement.MeasurementTypeCodename.REFLECTOMETRY;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.AVERAGE_COUNT;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ALARMS;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ANALYSIS_RESULT;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_CRITERIA;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ETALON;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_QUALITY_OVERALL_D;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_QUALITY_OVERALL_Q;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_QUALITY_PER_EVENT;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.FLAG_GAIN_SPLICE_ON;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.FLAG_LIFE_FIBER_DETECT;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.FLAG_PULSE_WIDTH_LOW_RES;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.FLAG_SMOOTH_FILTER;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.INDEX_OF_REFRACTION;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PREDICTION_TIME;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PREDICTION_TIME_END;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PREDICTION_TIME_START;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PULSE_WIDTH_M;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PULSE_WIDTH_NS;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.REFLECTOGRAMMA;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.REFLECTOGRAMMA_ETALON;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.RESOLUTION;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.TRACE_LENGTH;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.WAVE_LENGTH;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2006/03/27 09:36:00 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public class SetupActionParameterTypeBinding extends TestCase {

	public SetupActionParameterTypeBinding(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupActionParameterTypeBinding.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();


		/*	Retrieve all data*/

		/*	Parameter types*/
		final CompoundCondition parameterTypeCondition = new CompoundCondition(new TypicalCondition(WAVE_LENGTH.stringValue(),
					OPERATION_EQUALS,
					PARAMETER_TYPE_CODE,
					COLUMN_CODENAME),
				OR,
				new TypicalCondition(TRACE_LENGTH.stringValue(),
					OPERATION_EQUALS,
					PARAMETER_TYPE_CODE,
					COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(RESOLUTION.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(PULSE_WIDTH_NS.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(PULSE_WIDTH_M.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(INDEX_OF_REFRACTION.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(AVERAGE_COUNT.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(FLAG_PULSE_WIDTH_LOW_RES.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(FLAG_GAIN_SPLICE_ON.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(FLAG_LIFE_FIBER_DETECT.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(FLAG_SMOOTH_FILTER.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(REFLECTOGRAMMA.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(DADARA_CRITERIA.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(REFLECTOGRAMMA_ETALON.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(DADARA_ETALON.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(DADARA_ANALYSIS_RESULT.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(DADARA_ALARMS.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(DADARA_QUALITY_PER_EVENT.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(DADARA_QUALITY_OVERALL_D.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(DADARA_QUALITY_OVERALL_Q.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(PREDICTION_TIME.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(PREDICTION_TIME_START.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		parameterTypeCondition.addCondition(new TypicalCondition(PREDICTION_TIME_END.stringValue(),
				OPERATION_EQUALS,
				PARAMETER_TYPE_CODE,
				COLUMN_CODENAME));
		final Set<ParameterType> parameterTypes = StorableObjectPool.getStorableObjectsByCondition(parameterTypeCondition, true);
		assertTrue("Not all parameter types can be loaded", parameterTypes.size() == parameterTypeCondition.getConditionsNumber());
		final Map<String, ParameterType> parameterTypeCodenamesMap = StorableObjectType.createCodenamesMap(parameterTypes);

		/*	Measurement type*/
		final TypicalCondition measurementTypeCondition = new TypicalCondition(REFLECTOMETRY.stringValue(),
				OPERATION_EQUALS,
				MEASUREMENT_TYPE_CODE,
				COLUMN_CODENAME);
		final Set<MeasurementType> measurementTypes = StorableObjectPool.getStorableObjectsByCondition(measurementTypeCondition, true);
		assertTrue("Not one measurement type", measurementTypes.size() == 1);
		final MeasurementType measurementType = measurementTypes.iterator().next();

		/*	Analysis type*/
		final TypicalCondition analysisTypeCondition = new TypicalCondition(DADARA.stringValue(),
				OPERATION_EQUALS,
				ANALYSIS_TYPE_CODE,
				COLUMN_CODENAME);
		final Set<AnalysisType> analysisTypes = StorableObjectPool.getStorableObjectsByCondition(analysisTypeCondition, true);
		assertTrue("Not one analysis type", analysisTypes.size() == 1);
		final AnalysisType analysisType = analysisTypes.iterator().next();

		/*	Modeling type*/
		final TypicalCondition modelingTypeCondition = new TypicalCondition(ModelingTypeCodename.REFLECTOMETRY.stringValue(),
				OPERATION_EQUALS,
				MODELING_TYPE_CODE,
				COLUMN_CODENAME);
		final Set<ModelingType> modelingTypes = StorableObjectPool.getStorableObjectsByCondition(modelingTypeCondition, true);
		assertTrue("Not one modeling type", modelingTypes.size() == 1);
		final ModelingType modelingType = modelingTypes.iterator().next();

		/*	Measurement port types*/
		final CompoundCondition measurementPortTypeCondition = new CompoundCondition(new TypicalCondition(REFLECTOMETRY_QP1640A.stringValue(),
					OPERATION_EQUALS,
					MEASUREMENTPORT_TYPE_CODE,
					COLUMN_CODENAME),
				OR,
				new TypicalCondition(REFLECTOMETRY_QP1643A.stringValue(),
					OPERATION_EQUALS,
					MEASUREMENTPORT_TYPE_CODE,
					COLUMN_CODENAME));
		measurementPortTypeCondition.addCondition(new TypicalCondition(REFLECTOMETRY_PK7600.stringValue(),
				OPERATION_EQUALS,
				MEASUREMENTPORT_TYPE_CODE,
				COLUMN_CODENAME));
		final Set<MeasurementPortType> measurementPortTypes = StorableObjectPool.getStorableObjectsByCondition(measurementPortTypeCondition, true);
		assertTrue("Not all measurement port types can be loaded", measurementPortTypes.size() == measurementPortTypeCondition.getConditionsNumber());
		final Map<String, MeasurementPortType> measurementPortTypeCodenamesMap = StorableObjectType.createCodenamesMap(measurementPortTypes);


		/*	Create bindings*/

		final Set<ActionParameterTypeBinding> actionParameterTypeBindings = new HashSet<ActionParameterTypeBinding>();
		final Identifier measurementTypeId = measurementType.getId();
		final Identifier analysisTypeId = analysisType.getId();
		final Identifier modelingTypeId = modelingType.getId();

		/*	Bindings for QP1640A*/
		final Identifier qp1640aMeasurementPortTypeId = measurementPortTypeCodenamesMap.get(REFLECTOMETRY_QP1640A.stringValue()).getId();

		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(WAVE_LENGTH.stringValue()).getId(),
				measurementTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(TRACE_LENGTH.stringValue()).getId(),
				measurementTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(RESOLUTION.stringValue()).getId(),
				measurementTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(PULSE_WIDTH_NS.stringValue()).getId(),
				measurementTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(INDEX_OF_REFRACTION.stringValue()).getId(),
				measurementTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(AVERAGE_COUNT.stringValue()).getId(),
				measurementTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(FLAG_PULSE_WIDTH_LOW_RES.stringValue()).getId(),
				measurementTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(FLAG_GAIN_SPLICE_ON.stringValue()).getId(),
				measurementTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(FLAG_LIFE_FIBER_DETECT.stringValue()).getId(),
				measurementTypeId,
				qp1640aMeasurementPortTypeId));

		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(REFLECTOGRAMMA.stringValue()).getId(),
				analysisTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_CRITERIA.stringValue()).getId(),
				analysisTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(REFLECTOGRAMMA_ETALON.stringValue()).getId(),
				analysisTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_ETALON.stringValue()).getId(),
				analysisTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_ANALYSIS_RESULT.stringValue()).getId(),
				analysisTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_ALARMS.stringValue()).getId(),
				analysisTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_QUALITY_PER_EVENT.stringValue()).getId(),
				analysisTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_QUALITY_OVERALL_D.stringValue()).getId(),
				analysisTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_QUALITY_OVERALL_Q.stringValue()).getId(),
				analysisTypeId,
				qp1640aMeasurementPortTypeId));

		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(PREDICTION_TIME.stringValue()).getId(),
				modelingTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(PREDICTION_TIME_START.stringValue()).getId(),
				modelingTypeId,
				qp1640aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(PREDICTION_TIME_END.stringValue()).getId(),
				modelingTypeId,
				qp1640aMeasurementPortTypeId));

		/*	Bindings for QP1643A*/
		final Identifier qp1643aMeasurementPortTypeId = measurementPortTypeCodenamesMap.get(REFLECTOMETRY_QP1643A.stringValue()).getId();

		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(WAVE_LENGTH.stringValue()).getId(),
				measurementTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(TRACE_LENGTH.stringValue()).getId(),
				measurementTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(RESOLUTION.stringValue()).getId(),
				measurementTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(PULSE_WIDTH_NS.stringValue()).getId(),
				measurementTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(INDEX_OF_REFRACTION.stringValue()).getId(),
				measurementTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(AVERAGE_COUNT.stringValue()).getId(),
				measurementTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(FLAG_PULSE_WIDTH_LOW_RES.stringValue()).getId(),
				measurementTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(FLAG_GAIN_SPLICE_ON.stringValue()).getId(),
				measurementTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(FLAG_LIFE_FIBER_DETECT.stringValue()).getId(),
				measurementTypeId,
				qp1643aMeasurementPortTypeId));

		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(REFLECTOGRAMMA.stringValue()).getId(),
				analysisTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_CRITERIA.stringValue()).getId(),
				analysisTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(REFLECTOGRAMMA_ETALON.stringValue()).getId(),
				analysisTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_ETALON.stringValue()).getId(),
				analysisTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_ANALYSIS_RESULT.stringValue()).getId(),
				analysisTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_ALARMS.stringValue()).getId(),
				analysisTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_QUALITY_PER_EVENT.stringValue()).getId(),
				analysisTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_QUALITY_OVERALL_D.stringValue()).getId(),
				analysisTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_QUALITY_OVERALL_Q.stringValue()).getId(),
				analysisTypeId,
				qp1643aMeasurementPortTypeId));

		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(PREDICTION_TIME.stringValue()).getId(),
				modelingTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(PREDICTION_TIME_START.stringValue()).getId(),
				modelingTypeId,
				qp1643aMeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(PREDICTION_TIME_END.stringValue()).getId(),
				modelingTypeId,
				qp1643aMeasurementPortTypeId));

		/*	Bindings for PK7600*/
		final Identifier pk7600MeasurementPortTypeId = measurementPortTypeCodenamesMap.get(REFLECTOMETRY_PK7600.stringValue()).getId();

		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(WAVE_LENGTH.stringValue()).getId(),
				measurementTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(TRACE_LENGTH.stringValue()).getId(),
				measurementTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(RESOLUTION.stringValue()).getId(),
				measurementTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(PULSE_WIDTH_M.stringValue()).getId(),
				measurementTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(INDEX_OF_REFRACTION.stringValue()).getId(),
				measurementTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(AVERAGE_COUNT.stringValue()).getId(),
				measurementTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.ENUMERATED,
				parameterTypeCodenamesMap.get(FLAG_SMOOTH_FILTER.stringValue()).getId(),
				measurementTypeId,
				pk7600MeasurementPortTypeId));

		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(REFLECTOGRAMMA.stringValue()).getId(),
				analysisTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_CRITERIA.stringValue()).getId(),
				analysisTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(REFLECTOGRAMMA_ETALON.stringValue()).getId(),
				analysisTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_ETALON.stringValue()).getId(),
				analysisTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_ANALYSIS_RESULT.stringValue()).getId(),
				analysisTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_ALARMS.stringValue()).getId(),
				analysisTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_QUALITY_PER_EVENT.stringValue()).getId(),
				analysisTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_QUALITY_OVERALL_D.stringValue()).getId(),
				analysisTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(DADARA_QUALITY_OVERALL_Q.stringValue()).getId(),
				analysisTypeId,
				pk7600MeasurementPortTypeId));

		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(PREDICTION_TIME.stringValue()).getId(),
				modelingTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(PREDICTION_TIME_START.stringValue()).getId(),
				modelingTypeId,
				pk7600MeasurementPortTypeId));
		actionParameterTypeBindings.add(ActionParameterTypeBinding.createInstance(creatorId,
				ParameterValueKind.CONTINUOUS,
				parameterTypeCodenamesMap.get(PREDICTION_TIME_END.stringValue()).getId(),
				modelingTypeId,
				pk7600MeasurementPortTypeId));


		/*	Save new bindings*/
		StorableObjectPool.flush(actionParameterTypeBindings, creatorId, false);

	}
}
