/*
 * ParametersTestPanel.java
 * Created on 29.04.2004 11:54:37
 * 
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import javax.swing.JPanel;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * @author Vladimir Dolzhenko
 */
public abstract class ParametersTestPanel extends JPanel implements
		ParametersTest {
	protected final ApplicationContext	aContext;
	
	public ParametersTestPanel(final ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public abstract void setMonitoredElement(final MonitoredElement me);
	
	public abstract void setEnableEditing(final boolean enable);
}
