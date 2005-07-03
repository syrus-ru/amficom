/*
 * ParametersTestPanel.java
 * Created on 29.04.2004 11:54:37
 * 
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import javax.swing.JPanel;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.configuration.MonitoredElement;

/**
 * @author Vladimir Dolzhenko
 */
public abstract class ParametersTestPanel extends JPanel implements
		ParametersTest {
	protected ApplicationContext		aContext;
	
	public ParametersTestPanel(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public abstract void setMonitoredElement(MonitoredElement me);
}
