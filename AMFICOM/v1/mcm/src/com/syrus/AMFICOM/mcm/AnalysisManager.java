package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.SetParameter;

public interface AnalysisManager {
	SetParameter[] analyse() throws AnalysisException;
}