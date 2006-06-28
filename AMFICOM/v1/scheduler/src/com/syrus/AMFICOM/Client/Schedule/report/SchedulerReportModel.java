/*-
 * $Id: SchedulerReportModel.java,v 1.2 2006/04/03 10:31:04 bass Exp $
 *
 * Copyright © 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.report;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.TableModelVerticalDivider;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.TableDataStorableElement;

/**
 * @author Peter Peskovsky
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/04/03 10:31:04 $
 * @module scheduler
 */
public final class SchedulerReportModel extends ReportModel {
	// Названия отчётов для карты
	/**
	 * График тестов
	 */ 
	public static String TESTS_GRAPHIC = "testsGraphic";
	/**
	 * Параметры теста
	 */ 
	public static String TEST_PARAMETERS = "testParameters";
	/**
	 * Список тестов
	 */ 
	public static String TESTS_LIST = "testsList";
	
	@Override
	public ReportType getReportKind(String reportName){
		ReportType result = ReportType.TABLE;
		if (reportName.equals(TESTS_GRAPHIC))
			result = ReportType.GRAPH;
		return result;
	}
	
	@Override
	public RenderingComponent createReport(
			final AbstractDataStorableElement dataStorableElement,
			final Object data,
			final ApplicationContext aContext)
	throws CreateReportException{
		RenderingComponent result = null;
		String reportName = dataStorableElement.getReportName();
		String modelClassName = dataStorableElement.getModelClassName();		
		
		if (reportName.equals(TEST_PARAMETERS)) {
			if (!(data instanceof Identifier))
				throw new CreateReportException(
						reportName,
						modelClassName,
						CreateReportException.WRONG_DATA_TO_INSTALL);
			
			Identifier testId = (Identifier)data;
			if (testId.getMajor() == ObjectEntities.TEST_CODE){
				try {
					Test test = StorableObjectPool.getStorableObject(testId,true); 
					result = TestReport.createReport(
							test,
							(TableDataStorableElement) dataStorableElement);
				} catch (final CreateReportException cre) {
					// noone seems to care
				} catch (final ApplicationException ae) {
					// noone seems to care
				}
			}
		}
		else if (reportName.equals(TESTS_LIST)) {
			if (!(data instanceof AbstractTableModel))
				throw new CreateReportException(
						reportName,
						modelClassName,
						CreateReportException.WRONG_DATA_TO_INSTALL);
			result = TableModelVerticalDivider.createReport(
					(AbstractTableModel) data,
					(TableDataStorableElement) dataStorableElement);
		}
		
		if (result == null)
			throw new CreateReportException(
					reportName,
					modelClassName,
					CreateReportException.WRONG_DATA_TO_INSTALL);
		
		return result;
	}
	
	@Override
	public String getReportElementName(String reportName) {
		String langReportName = null;
		if (	reportName.equals(TESTS_GRAPHIC)
			||	reportName.equals(TEST_PARAMETERS)
			||	reportName.equals(TESTS_LIST))
			langReportName = I18N.getString("report.Modules.Scheduler." + reportName);
			
		return langReportName;
	}

	@Override
	public String getName() {
		return DestinationModules.SCHEDULER;
	}

	@Override
	public Collection<String> getTemplateElementNames() {
		Collection<String> result = new ArrayList<String>();

		result.add(TESTS_GRAPHIC);
		result.add(TEST_PARAMETERS);
		result.add(TESTS_LIST);		
		
		return result;
	}
}
