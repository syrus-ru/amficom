/*-
 * $Id: SetTest.java,v 1.1.1.1 2005/04/14 12:54:06 cvsadmin Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.Test;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2005/04/14 12:54:06 $
 * @author $Author: cvsadmin $
 * @author Vladimir Dolzhenko
 * @module measurement_v1
 */
public class SetTest extends AbstractMeasurementTestCase {

	public SetTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SetTest.class);
	}
	
	public static Test suite() {
		return suiteWrapper(SetTest.class);
	}

	public void testSet() throws ApplicationException {
		
		Identifier meId = new Identifier("MonitoredElement_46");
		TypicalCondition waveLengthCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_WAVELENGTH,
																	OperationSort.OPERATION_EQUALS,
																	ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																	StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition traceLengthCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_LENGTH,
																		OperationSort.OPERATION_EQUALS,
																		ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																		StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition resolutionCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_RESOLUTION,
																	OperationSort.OPERATION_EQUALS,
																	ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																	StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition pulseWidthCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_PULSE_WIDTH,
																	OperationSort.OPERATION_EQUALS,
																	ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																	StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition indexOfRefractionCondition = new TypicalCondition(
																			ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION,
																			OperationSort.OPERATION_EQUALS,
																			ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																			StorableObjectWrapper.COLUMN_CODENAME);

		TypicalCondition averageCountCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_AVERAGE_COUNT,
																		OperationSort.OPERATION_EQUALS,
																		ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
																		StorableObjectWrapper.COLUMN_CODENAME);

		java.util.Set conditions = new HashSet(6);
		conditions.add(waveLengthCondition);
		conditions.add(traceLengthCondition);
		conditions.add(resolutionCondition);
		conditions.add(pulseWidthCondition);
		conditions.add(indexOfRefractionCondition);
		conditions.add(averageCountCondition);

		CompoundCondition compoundCondition = new CompoundCondition(conditions, CompoundConditionSort.OR);

		Collection parameterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);

		ParameterType wvlenParameterType = null;
		ParameterType trclenParameterType = null;
		ParameterType resParameterType = null;
		ParameterType pulswdParameterType = null;
		ParameterType iorParameterType = null;
		ParameterType scansParameterType = null;

		for (Iterator iter = parameterTypes.iterator(); iter.hasNext();) {
			ParameterType parameterType = (ParameterType) iter.next();
			String codeName = parameterType.getCodename();
			if (codeName.equals(waveLengthCondition.getValue()))
				wvlenParameterType = parameterType;
			else if (codeName.equals(traceLengthCondition.getValue()))
				trclenParameterType = parameterType;
			else if (codeName.equals(resolutionCondition.getValue()))
				resParameterType = parameterType;
			else if (codeName.equals(pulseWidthCondition.getValue()))
				pulswdParameterType = parameterType;
			else if (codeName.equals(indexOfRefractionCondition.getValue()))
				iorParameterType = parameterType;
			else if (codeName.equals(averageCountCondition.getValue()))
				scansParameterType = parameterType;
		}

		SetParameter[] params = new SetParameter[7];

		ByteArray byteArray;

		byteArray = this.getByteArray("1625", wvlenParameterType);

		params[0] = SetParameter.createInstance(wvlenParameterType, byteArray.getBytes());

		byteArray = this.getByteArray("75.0", trclenParameterType);

		params[1] = SetParameter.createInstance(trclenParameterType, byteArray.getBytes());

		byteArray = this.getByteArray("0.5", resParameterType);

		params[2] = SetParameter.createInstance(resParameterType, byteArray.getBytes());

		byteArray = this.getByteArray("5", pulswdParameterType);

		params[3] = SetParameter.createInstance(pulswdParameterType, byteArray.getBytes());

		byteArray = this.getByteArray("1.4682", iorParameterType);

		params[4] = SetParameter.createInstance(iorParameterType, byteArray.getBytes());

		byteArray = this.getByteArray("4096.0", scansParameterType);

		params[5] = SetParameter.createInstance(scansParameterType, byteArray.getBytes());

		Set set = Set.createInstance(creatorId, SetSort.SET_SORT_MEASUREMENT_PARAMETERS,
			"Set created by Scheduler", params, Collections.singleton(meId));
		Log.debugMessage("SetTest.testSet | set id is " + set.getId(), Log.FINEST);
		MeasurementStorableObjectPool.putStorableObject(set);
		MeasurementStorableObjectPool.flush(true);
	}
	
	private ByteArray getByteArray(String value, ParameterType parameterType) {
		ByteArray byteArray = null;
		DataType dataType = parameterType.getDataType();
		switch (dataType.value()) {
			case DataType._DATA_TYPE_INTEGER:
				byteArray = new ByteArray(Integer.parseInt(value));
				break;
			case DataType._DATA_TYPE_DOUBLE:
				byteArray = new ByteArray(Double.parseDouble(value));
				break;
			case DataType._DATA_TYPE_STRING:
				byteArray = new ByteArray(value);
				break;
			case DataType._DATA_TYPE_LONG:
				byteArray = new ByteArray(Long.parseLong(value));
				break;
		}
		return byteArray;
	}
}
