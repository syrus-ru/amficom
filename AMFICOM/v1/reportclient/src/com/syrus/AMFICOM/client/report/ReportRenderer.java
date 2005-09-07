/*
 * $Id: ReportRenderer.java,v 1.4 2005/09/07 14:26:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * Реализует отчёт по шаблону
 * @author $Author: peskovsky $
 * @version $Revision: 1.4 $, $Date: 2005/09/07 14:26:10 $
 * @module reportclient_v1
 */
public class ReportRenderer extends JPanel{
	private static final long serialVersionUID = 6316228563298763509L;

	private ReportTemplate reportTemplate = null;
	
	public void setReportTemplate (ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}
	
	public void setData(Map<String, Object> data) throws CreateReportException {
		if (this.reportTemplate == null)
			throw new AssertionError("Report template is not set!");
		
		for (DataStorableElement dsElement : this.reportTemplate.getDataStorableElements()) {
			String dsElementLangName = LangModelReport.getString(dsElement.getReportName());
			String modelName = dsElement.getModelClassName();
			
			ReportModel model = ReportModelPool.getModel(modelName);
			if (model == null)
				throw new CreateReportException (
						dsElementLangName,
						CreateReportException.reportModelIsAbsent);
			
			Object dsElementData = data.get(dsElementLangName);
			if (dsElementData == null)
				throw new CreateReportException (
						dsElementLangName,
						CreateReportException.noDataToInstall);
			
			RenderingComponent component = model.createReport(
					dsElement,
					dsElementData);
			
			this.add((JComponent)component);
		}

		for (AttachedTextStorableElement label : this.reportTemplate.getTextStorableElements()) {
			AttachedTextComponent component = new AttachedTextComponent(label);
			this.add(component);
		}

		for (ImageStorableElement imageElement : this.reportTemplate.getImageStorableElements()) {
			ImageRenderingComponent component = new ImageRenderingComponent(
					imageElement,
					imageElement.getImage());
			this.add(component);
		}
		ReportLayout layout = new ReportLayout();
		layout.dolayout(this.getRenderingComponents(),this.reportTemplate);
	}
	
	public List<RenderingComponent> getRenderingComponents() {
		List<RenderingComponent> components = new ArrayList<RenderingComponent>();
		for (int i = 0; i < this.getComponentCount(); i++)
			components.add((RenderingComponent)this.getComponent(i));
		return components;
	}
}
