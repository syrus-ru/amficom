/*
 * $Id: AnalysisDadaraParameterTypes.java,v 1.1 2005/04/12 12:37:46 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package test.com.syrus.AMFICOM.general;

import junit.framework.Test;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/12 12:37:46 $
 * @author $Author: bob $
 * @module tools
 */
public class AnalysisDadaraParameterTypes extends GeneralTestCase {

	public AnalysisDadaraParameterTypes(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = AnalysisDadaraParameterTypes.class;
		junit.awtui.TestRunner.run(clazz);
		// junit.swingui.TestRunner.run(clazz);
		// junit.textui.TestRunner.run(clazz);

	}

	public static Test suite() {
		return suiteWrapper(AnalysisDadaraParameterTypes.class);
	}

	public void testCreateParameterTypes() throws ApplicationException {
		{
			ParameterType parameterType = ParameterType.createInstance(creatorId, ParameterTypeCodenames.DADARA_MTAE,
				"ModelTraceAndEvents for analysis results", "ModelTraceAndEvents",
				DataType.DATA_TYPE_RAW);
			GeneralStorableObjectPool.putStorableObject(parameterType);
		}
		{
			ParameterType parameterType = ParameterType.createInstance(creatorId,
				ParameterTypeCodenames.DADARA_ETALON_MTM, "ModeltraceMananger for etalon+thresholds",
				"ModeltraceMananger", DataType.DATA_TYPE_RAW);
			GeneralStorableObjectPool.putStorableObject(parameterType);
		}
		{
			ParameterType parameterType = ParameterType.createInstance(creatorId,
				ParameterTypeCodenames.DADARA_MIN_TRACE_LEVEL, "Minimal trace level", "Minimal trace level",
				DataType.DATA_TYPE_DOUBLE);
			GeneralStorableObjectPool.putStorableObject(parameterType);
		}

		{
			ParameterType parameterType = ParameterType.createInstance(creatorId,
				ParameterTypeCodenames.DADARA_NOISE_FACTOR, "Analysis noise factor", "Noise factor", DataType.DATA_TYPE_DOUBLE);
			GeneralStorableObjectPool.putStorableObject(parameterType);
		}

		{
			ParameterType parameterType = ParameterType.createInstance(creatorId, ParameterTypeCodenames.MIN_EVENT,
				"Analysis event detection threshold", "Event threshold", DataType.DATA_TYPE_DOUBLE);
			GeneralStorableObjectPool.putStorableObject(parameterType);
		}
		{
			ParameterType parameterType = ParameterType.createInstance(creatorId, ParameterTypeCodenames.MIN_SPLICE,
				"Analysis splice detection threshold", "Splice threshold", DataType.DATA_TYPE_DOUBLE);
			GeneralStorableObjectPool.putStorableObject(parameterType);
		}

		{
			ParameterType parameterType = ParameterType.createInstance(creatorId, ParameterTypeCodenames.MIN_CONNECTOR,
				"Analysis connector detection threshold", "Connector threshold", DataType.DATA_TYPE_DOUBLE);
			GeneralStorableObjectPool.putStorableObject(parameterType);
		}
	}
}
