package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public interface EvaluationManager {

	SetParameter[] evaluate() throws EvaluationException;

	AlarmLevel getAlarmLevel();
}
