package com.syrus.AMFICOM.mcm;

import java.util.Map;
import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.util.Log;

public abstract class AnalysisManager {
	private static final String CODENAME_ANALYSIS_TYPE_DADARA = "dadara";

	private static Map analysisTypes;

	static {
		analysisTypes = new Hashtable(1);
		addAnalysisType(CODENAME_ANALYSIS_TYPE_DADARA);
	}

	public static Result analyse(Result measurementResult,
												Analysis analysis,
												Set etalon) throws AnalysisException {
		Identifier analysisTypeId = analysis.getTypeId();
		AnalysisType analysisType = (AnalysisType)analysisTypes.get(analysisTypeId);
		if (analysisType != null) {
			String analysisTypeCodename = analysisType.getCodename();
			SetParameter[] arParameters = null;
			if (analysisTypeCodename.equals(CODENAME_ANALYSIS_TYPE_DADARA)) {
				DadaraAnalysisManager dadaraAnalysisManager = new DadaraAnalysisManager(measurementResult,
																																								analysis,
																																								etalon);
				arParameters = dadaraAnalysisManager.analyse();
			}
			else
				throw new AnalysisException("Analysis for codename '" + analysisTypeCodename + "' not implemented");

			Result result = null; 
			try {
				analysis.createResult(MeasurementControlModule.getNewIdentifier(ObjectEntities.RESULT_ENTITY),
															MeasurementControlModule.iAm.getUserId(),
															measurementResult.getMeasurement(),
															AlarmLevel.ALARM_LEVEL_NONE,
															arParameters);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
				result = null;
			}
			return result;
		}
		throw new AnalysisException("Cannot find analysis of type '" + analysisTypeId + "'");
	}

  public abstract SetParameter[] analyse() throws AnalysisException;

	private static void addAnalysisType(String codename) {
		try {
			AnalysisType analysisType = AnalysisTypeDatabase.retrieveForCodename(codename);
			Identifier analysisTypeId = analysisType.getId();
			if (!analysisTypes.containsKey(analysisTypeId))
				analysisTypes.put(analysisTypeId, analysisType);
			else
				Log.errorMessage("Analysis type of codename '" + codename + "' and id '" + analysisTypeId.toString() + "' already added to map");
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
	}
}