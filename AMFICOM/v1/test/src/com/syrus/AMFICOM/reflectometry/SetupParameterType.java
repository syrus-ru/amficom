/*-
 * $Id: SetupParameterType.java,v 1.1.2.3 2006/02/22 15:00:59 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.reflectometry;

import static com.syrus.AMFICOM.general.DataType.BOOLEAN;
import static com.syrus.AMFICOM.general.DataType.DATE;
import static com.syrus.AMFICOM.general.DataType.DOUBLE;
import static com.syrus.AMFICOM.general.DataType.INTEGER;
import static com.syrus.AMFICOM.general.DataType.RAW;
import static com.syrus.AMFICOM.general.MeasurementUnit.KILOMETER;
import static com.syrus.AMFICOM.general.MeasurementUnit.METER;
import static com.syrus.AMFICOM.general.MeasurementUnit.NANOMETER;
import static com.syrus.AMFICOM.general.MeasurementUnit.NANOSECOND;
import static com.syrus.AMFICOM.general.MeasurementUnit.NONDIMENSIONAL;
import static com.syrus.AMFICOM.general.MeasurementUnit.SECOND;
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
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.1.2.3 $, $Date: 2006/02/22 15:00:59 $
 * @author $Author: arseniy $
 * @module test
 */
public final class SetupParameterType extends TestCase {
	private static final String RESOURCE_KEY_ROOT = "ParameterType.Description.";

	public SetupParameterType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupParameterType.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();
		String typeCodename;
		final Set<ParameterType> parameterTypes = new HashSet<ParameterType>();


		/*	Measurement parameters*/

		typeCodename = WAVE_LENGTH.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				INTEGER,
				NANOMETER));

		typeCodename = TRACE_LENGTH.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				DOUBLE,
				KILOMETER));

		typeCodename = RESOLUTION.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				DOUBLE,
				METER));

		typeCodename = PULSE_WIDTH_NS.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				INTEGER,
				NANOSECOND));

		typeCodename = PULSE_WIDTH_M.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				INTEGER,
				METER));

		typeCodename = INDEX_OF_REFRACTION.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				DOUBLE,
				NONDIMENSIONAL));

		typeCodename = AVERAGE_COUNT.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				DOUBLE,
				NONDIMENSIONAL));

		typeCodename = FLAG_PULSE_WIDTH_LOW_RES.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				BOOLEAN,
				NONDIMENSIONAL));

		typeCodename = FLAG_GAIN_SPLICE_ON.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				BOOLEAN,
				NONDIMENSIONAL));

		typeCodename = FLAG_LIFE_FIBER_DETECT.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				BOOLEAN,
				NONDIMENSIONAL));


		/*	Analysis parameters*/

		typeCodename = REFLECTOGRAMMA.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				RAW,
				NONDIMENSIONAL));

		typeCodename = DADARA_CRITERIA.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				RAW,
				NONDIMENSIONAL));

		typeCodename = REFLECTOGRAMMA_ETALON.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				RAW,
				NONDIMENSIONAL));

		typeCodename = DADARA_ETALON.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				RAW,
				NONDIMENSIONAL));

		typeCodename = DADARA_ANALYSIS_RESULT.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				RAW,
				NONDIMENSIONAL));

		typeCodename = DADARA_ALARMS.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				RAW,
				NONDIMENSIONAL));

		typeCodename = DADARA_QUALITY_PER_EVENT.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				RAW,
				NONDIMENSIONAL));

		typeCodename = DADARA_QUALITY_OVERALL_D.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				DOUBLE,
				NONDIMENSIONAL));

		typeCodename = DADARA_QUALITY_OVERALL_Q.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				DOUBLE,
				NONDIMENSIONAL));


		/*	Modeling parameters*/

		typeCodename = PREDICTION_TIME.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				DATE,
				SECOND));

		typeCodename = PREDICTION_TIME_START.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				DATE,
				SECOND));

		typeCodename = PREDICTION_TIME_END.stringValue();
		parameterTypes.add(ParameterType.createInstance(creatorId,
				typeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + typeCodename),
				DATE,
				SECOND));


		/*	Save all*/

		StorableObjectPool.flush(parameterTypes, creatorId, false);

	}
}
