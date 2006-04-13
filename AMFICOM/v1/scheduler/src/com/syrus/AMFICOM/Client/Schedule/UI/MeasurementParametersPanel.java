/*
 * ParametersTestPanel.java
 * Created on 29.04.2004 11:44:40
 * 
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * ������ � ����������� ���������.
 * ��������� JPanel, ����� ţ ����� ���� ���������� ��� ������.
 * 
 * @author $Author: saa $
 * @author �� ����������
 * @version $Revision: 1.1.2.2 $, $Date: 2006/04/13 09:39:03 $
 * @module
 */
public abstract class MeasurementParametersPanel extends JPanel {
	/**
	 * ������ �� ������
	 */
	protected SchedulerModel		schedulerModel;

	protected final void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.schedulerModel =
			(SchedulerModel)applicationContext.getApplicationModel();
	}

	/**
	 * �������� ������� ��������� ��������� ��������� � ���� ������� ���������
	 * �� ������� �� � ������� ���� ������.
	 * 
	 * @return ������� ��������� ��������� ���������
	 * @throws CreateObjectException
	 */
	public abstract ActionTemplate<Measurement> getMeasurementTemplate()
	throws CreateObjectException;

	/**
	 * ���������� ������� ��������� ��������� �������� �������
	 * ������� ���������. ��� ������������� ������� MonitoredElement.
	 * 
	 * @param template ������ ������ ���������, not null
	 */
	public abstract void setMeasurementTemplate(final ActionTemplate<Measurement> template);

	/**
	 * ���������/��������� ��������� ��������� GUI
	 */
	public abstract void setEnableEditing(boolean b);

//	/**
//	 * ���� �� ���� �������, ����� ����� ����� ��� ����� � ����� �� ��� �����
//	 */
//	protected TestParametersPanel	testParametersPanel;
//	protected final void setTestParametersPanel(
//			final TestParametersPanel testParametersPanel) {
//		this.testParametersPanel = testParametersPanel;
//	}

	/**
	 * ������������� ������� ����� ������������.
	 * ������������ ���������� ����� ������ - ����������, ����� ������
	 * ���������� ��� �������� ����� �����.
	 * ���������� ������ ������������ �����, �����
	 * ����� {@link #getMeasurementTemplate()} ��������� ������,
	 * ���������� ��� ��������� ����� �����.
	 * 
	 */
	public abstract void setMonitoredElement(MonitoredElement me);
}
