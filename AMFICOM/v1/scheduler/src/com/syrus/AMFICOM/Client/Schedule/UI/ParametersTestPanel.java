/*
 * ParametersTestPanel.java
 * Created on 29.04.2004 11:54:37
 * 
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * @author Vladimir Dolzhenko
 */
public abstract class ParametersTestPanel extends JPanel implements
		ParametersTest {
	protected ApplicationContext	aContext;
	protected TestParametersPanel	testParametersPanel;
	protected SchedulerModel		schedulerModel;

	
	protected final void setApplicationContext(final ApplicationContext applicationContext) {
		this.aContext = applicationContext;
		this.schedulerModel = (SchedulerModel) this.aContext.getApplicationModel();
	}
	
	protected final void setTestParametersPanel(final TestParametersPanel testParametersPanel) {
		this.testParametersPanel = testParametersPanel;
	}
	
	public abstract void setMonitoredElement(final MonitoredElement me);
	
	public abstract void setEnableEditing(final boolean enable);
}
