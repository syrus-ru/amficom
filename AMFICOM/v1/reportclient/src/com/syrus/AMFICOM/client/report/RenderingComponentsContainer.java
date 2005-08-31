/*
 * $Id: RenderingComponentsContainer.java,v 1.1 2005/08/31 10:32:54 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.report.ReportTemplate;
/**
 * Структура для хранения элементов отображения.
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/08/31 10:32:54 $
 * @module reportclient_v1
 */
public class RenderingComponentsContainer {
	private ReportTemplate reportTemplate = null;

	private final List<RenderingComponent> renderingComponents =
		new ArrayList<RenderingComponent>();
	
//	public final List<DataRenderingComponent> dataRenderingComponents =
//		new ArrayList<DataRenderingComponent>();
//	
//	public final List<AttachedTextComponent> textComponents =
//		new ArrayList<AttachedTextComponent>();
//	
//	public final List<ImageRenderingComponent> imageComponents =
//		new ArrayList<ImageRenderingComponent>();

	public ReportTemplate getReportTemplate() {
		return this.reportTemplate;
	}
	
	public RenderingComponentsContainer(ReportTemplate reportTemplate){
		this.reportTemplate = reportTemplate;
	}

	public List<RenderingComponent> getRenderingComponents() {
		return this.renderingComponents;
	}
	
	public void addRenderingComponent(RenderingComponent component){
		this.renderingComponents.add(component);
	}
}
