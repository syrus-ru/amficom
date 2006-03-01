/*-
 * $Id: SetupActionParameter.java,v 1.1.2.2 2006/03/01 11:43:03 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.reflectometry;

import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename.REFLECTOMETRY_PK7600;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename.REFLECTOMETRY_QP1640A;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename.REFLECTOMETRY_QP1643A;
import static com.syrus.AMFICOM.measurement.MeasurementTypeCodename.REFLECTOMETRY;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.AVERAGE_COUNT;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.FLAG_GAIN_SPLICE_ON;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.FLAG_LIFE_FIBER_DETECT;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.FLAG_PULSE_WIDTH_LOW_RES;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.INDEX_OF_REFRACTION;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PULSE_WIDTH_M;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.PULSE_WIDTH_NS;
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
import com.syrus.AMFICOM.measurement.ActionParameter;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.util.ByteArray;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/03/01 11:43:03 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class SetupActionParameter extends TestCase {

	public SetupActionParameter(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupActionParameter.class);
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

		
		/*	Create parameters*/

		final Set<ActionParameter> actionParameters = new HashSet<ActionParameter>();

		MeasurementPortType measurementPortType;
		ParameterType parameterType;
		ActionParameterTypeBinding actionParameterTypeBinding;


		/*	QP1640A*/
		measurementPortType = measurementPortTypeCodenamesMap.get(REFLECTOMETRY_QP1640A.stringValue());

		/*	Wave length*/
		parameterType = parameterTypeCodenamesMap.get(WAVE_LENGTH.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1625),
				actionParameterTypeBinding.getId(),
				false));

		/*	Trace length*/
		parameterType = parameterTypeCodenamesMap.get(TRACE_LENGTH.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(5.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(20.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(50.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(75.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(125.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(250.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(300.0),
				actionParameterTypeBinding.getId(),
				false));

		/*	Resolution*/
		parameterType = parameterTypeCodenamesMap.get(RESOLUTION.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(0.125),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(0.25),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(0.5),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(2.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(4.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(8.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(16.0),
				actionParameterTypeBinding.getId(),
				false));

		/*	Pulse width*/
		parameterType = parameterTypeCodenamesMap.get(PULSE_WIDTH_NS.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(5),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(10),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(20),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(50),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(100),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(200),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(500),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(2000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(5000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(10000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(20000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(30000),
				actionParameterTypeBinding.getId(),
				false));

		/*	Index of refraction*/
		parameterType = parameterTypeCodenamesMap.get(INDEX_OF_REFRACTION.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1.4682),
				actionParameterTypeBinding.getId(),
				false));

		/*	Average count*/
		parameterType = parameterTypeCodenamesMap.get(AVERAGE_COUNT.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(4096),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(32768),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(262144),
				actionParameterTypeBinding.getId(),
				false));

		/*	Flag pulse width low res*/
		parameterType = parameterTypeCodenamesMap.get(FLAG_PULSE_WIDTH_LOW_RES.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(true),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(false),
				actionParameterTypeBinding.getId(),
				false));

		/*	Flag gain splice*/
		parameterType = parameterTypeCodenamesMap.get(FLAG_GAIN_SPLICE_ON.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(true),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(false),
				actionParameterTypeBinding.getId(),
				false));

		/*	Flag live fiber detect*/
		parameterType = parameterTypeCodenamesMap.get(FLAG_LIFE_FIBER_DETECT.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(true),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(false),
				actionParameterTypeBinding.getId(),
				false));


		/*	QP1643A*/
		measurementPortType = measurementPortTypeCodenamesMap.get(REFLECTOMETRY_QP1643A.stringValue());

		/*	Wave length*/
		parameterType = parameterTypeCodenamesMap.get(WAVE_LENGTH.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1625),
				actionParameterTypeBinding.getId(),
				false));

		/*	Trace length*/
		parameterType = parameterTypeCodenamesMap.get(TRACE_LENGTH.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(5.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(20.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(50.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(75.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(125.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(250.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(300.0),
				actionParameterTypeBinding.getId(),
				false));

		/*	Resolution*/
		parameterType = parameterTypeCodenamesMap.get(RESOLUTION.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(0.125),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(0.25),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(0.5),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(2.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(4.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(8.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(16.0),
				actionParameterTypeBinding.getId(),
				false));

		/*	Pulse width*/
		parameterType = parameterTypeCodenamesMap.get(PULSE_WIDTH_NS.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(5),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(10),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(20),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(50),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(100),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(200),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(500),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(2000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(5000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(10000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(20000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(30000),
				actionParameterTypeBinding.getId(),
				false));

		/*	Index of refraction*/
		parameterType = parameterTypeCodenamesMap.get(INDEX_OF_REFRACTION.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1.4682),
				actionParameterTypeBinding.getId(),
				false));

		/*	Average count*/
		parameterType = parameterTypeCodenamesMap.get(AVERAGE_COUNT.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(4096),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(45312),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(262144),
				actionParameterTypeBinding.getId(),
				false));

		/*	Flag pulse width low res*/
		parameterType = parameterTypeCodenamesMap.get(FLAG_PULSE_WIDTH_LOW_RES.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(true),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(false),
				actionParameterTypeBinding.getId(),
				false));

		/*	Flag gain splice*/
		parameterType = parameterTypeCodenamesMap.get(FLAG_GAIN_SPLICE_ON.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(true),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(false),
				actionParameterTypeBinding.getId(),
				false));

		/*	Flag live fiber detect*/
		parameterType = parameterTypeCodenamesMap.get(FLAG_LIFE_FIBER_DETECT.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(true),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(false),
				actionParameterTypeBinding.getId(),
				false));


		/*	PK7600*/
		measurementPortType = measurementPortTypeCodenamesMap.get(REFLECTOMETRY_PK7600.stringValue());

		/*	Wave length*/
		parameterType = parameterTypeCodenamesMap.get(WAVE_LENGTH.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1625),
				actionParameterTypeBinding.getId(),
				false));

		/*	Trace length*/
		parameterType = parameterTypeCodenamesMap.get(TRACE_LENGTH.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(4.096),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(8.192),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(16.384),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(32.768),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(65.536),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(131.072),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(262.144),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(320.0),
				actionParameterTypeBinding.getId(),
				false));

		/*	Resolution*/
		parameterType = parameterTypeCodenamesMap.get(RESOLUTION.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(0.25),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(0.5),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(2.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(4.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(8.0),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(16.0),
				actionParameterTypeBinding.getId(),
				false));

		/*	Pulse width*/
		parameterType = parameterTypeCodenamesMap.get(PULSE_WIDTH_M.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(5),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(10),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(20),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(50),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(100),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(500),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1000),
				actionParameterTypeBinding.getId(),
				false));
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(2000),
				actionParameterTypeBinding.getId(),
				false));

		/*	Index of refraction*/
		parameterType = parameterTypeCodenamesMap.get(INDEX_OF_REFRACTION.stringValue());
		actionParameterTypeBinding = ActionParameterTypeBinding.valueOf(parameterType, measurementType, measurementPortType);
		actionParameters.add(ActionParameter.createInstance(creatorId,
				ByteArray.toByteArray(1.4682),
				actionParameterTypeBinding.getId(),
				false));


		/*	Save all*/
		StorableObjectPool.flush(actionParameters, creatorId, false);
	}
}
