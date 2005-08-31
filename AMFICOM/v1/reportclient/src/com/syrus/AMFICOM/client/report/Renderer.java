/*
 * $Id: Renderer.java,v 1.1 2005/08/31 10:32:55 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

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
	 * @param fromAnotherModule
	 */
	public abstract void buildFromTemplate(ReportTemplate template,boolean fromAnotherModule)
		throws CreateReportException;
}
