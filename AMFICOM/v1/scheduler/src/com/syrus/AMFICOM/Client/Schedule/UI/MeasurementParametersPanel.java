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
 * Панель с параметрами измерения.
 * Расширяет JPanel, чтобы её можно было отобразить как панель.
 * 
 * @author $Author: saa $
 * @author не установлен
 * @version $Revision: 1.1.2.2 $, $Date: 2006/04/13 09:39:03 $
 * @module
 */
public abstract class MeasurementParametersPanel extends JPanel {
	/**
	 * Ссылка на модель
	 */
	protected SchedulerModel		schedulerModel;

	protected final void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.schedulerModel =
			(SchedulerModel)applicationContext.getApplicationModel();
	}

	/**
	 * Оформить текущие выбранные параметры измерения в виде шаблона измерения
	 * по текущей ЛТ и вернуть этот шаблон.
	 * 
	 * @return текущие выбранные параметры измерения
	 * @throws CreateObjectException
	 */
	public abstract ActionTemplate<Measurement> getMeasurementTemplate()
	throws CreateObjectException;

	/**
	 * Установить текущие параметры измерения согласно данному
	 * шаблону измерения. При необходимости сменить MonitoredElement.
	 * 
	 * @param template данный шаблон измерения, not null
	 */
	public abstract void setMeasurementTemplate(final ActionTemplate<Measurement> template);

	/**
	 * Разрешить/запретить изменение элементов GUI
	 */
	public abstract void setEnableEditing(boolean b);

//	/**
//	 * Пока не могу сказать, зачем будет нужна эта штука и будет ли она нужна
//	 */
//	protected TestParametersPanel	testParametersPanel;
//	protected final void setTestParametersPanel(
//			final TestParametersPanel testParametersPanel) {
//		this.testParametersPanel = testParametersPanel;
//	}

	/**
	 * Устанавливает текущую линию тестирования.
	 * Единственное назначение этого метода - обеспечить, чтобы шаблон
	 * создавался для заданной извне линии.
	 * Реализация должна обеспечивать также, чтобы
	 * метод {@link #getMeasurementTemplate()} возвращал шаблон,
	 * приемлемый для выбранной здесь линии.
	 * 
	 */
	public abstract void setMonitoredElement(MonitoredElement me);
}
