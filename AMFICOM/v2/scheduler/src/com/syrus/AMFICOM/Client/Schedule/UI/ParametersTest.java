/*
 * ParametersTestPanel.java
 * Created on 29.04.2004 11:44:40
 * 
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import com.syrus.AMFICOM.Client.Resource.Result.*;

/**
 * @author Vladimir Dolzhenko
 * 
 * provide interface for varius parameter test panel 
 * i.g. ReflectometryTestPanel
 */
public interface ParametersTest {
	//String COMMAND_SEND_TEST = "SendTest";
	String PARAMETER_ID_NAME = "testargument";
	public TestArgumentSet getParameters();	
	public String getPanelName();
	public void setTest(Test test);
	
}
