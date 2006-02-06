/*
 * ParametersTestPanel.java
 * Created on 29.04.2004 11:44:40
 * 
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.measurement.MeasurementSetup;

/**
 * @author Vladimir Dolzhenko
 * 
 * provide interface for varius parameter test panel i.g. ReflectometryTestPanel
 */
public interface ParametersTest {
	
	MeasurementSetup getMeasurementSetup() throws CreateObjectException;
	
	void setMeasurementSetup(final MeasurementSetup measurementSetup); 
	
}
