/*
 * ParametersTestPanel.java
 * Created on 29.04.2004 11:44:40
 * 
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.Test;

/**
 * @author Vladimir Dolzhenko
 * 
 * provide interface for varius parameter test panel i.g. ReflectometryTestPanel
 */
public interface ParametersTest {

	//String COMMAND_SEND_TEST = "SendTest";
	String	PARAMETER_ID_NAME	= "testargument";	//$NON-NLS-1$

	Set getSet();
	
	void setSet(Set set);
	
	void setMeasurementSetup(MeasurementSetup measurementSetup);

	String getPanelName();

	void setTest(Test test);
	
	void unregisterDispatcher();
}
