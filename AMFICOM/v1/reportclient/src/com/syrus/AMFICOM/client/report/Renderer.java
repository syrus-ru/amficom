/*
 * $Id: Renderer.java,v 1.2 2005/09/01 14:21:22 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.util.Map;

import javax.swing.JPanel;

import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * Отображает визульные компоненты схемы шаблона отчёта или самого
 * отчёта.
 */
public abstract class Renderer extends JPanel{
	/**
	 * Строит набор визульных компонент для элементов отображения и располагает их
	 * на "листе"
	 * @param template
	 */
	public abstract void buildFromTemplate(
			ReportTemplate template,
			Map<String, Object> data)
		throws CreateReportException;
}
