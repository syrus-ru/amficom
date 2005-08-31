/*
 * $Id: ReportBuilderApplicationModel.java,v 1.1 2005/08/31 10:04:26 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class ReportBuilderApplicationModel extends ApplicationModel
{
	public ReportBuilderApplicationModel()
	{
		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionDomain");
		add("menuSessionSave");
		add("menuSessionUndo");
		add("menuExit");

		add("menuBar");
		add("toolBar");
		add("statusBar");

		add("menuWindow");
		add("menuWindowTree");
		add("menuWindowTemplateScheme");
		
		add("menuReportTemplateNew");
		add("menuReportTemplateSave");
		add("menuReportTemplateLoad");
		add("menuReportTemplateExport");
		add("menuReportTemplateImport");
		
		add(ApplicationModel.MENU_VIEW_ARRANGE);
		
		this.initUIConstats();
	}
	
	private void initUIConstats() {
		//TODO Сделать:-)
//		UIManager.put(SchemeResourceKeys.ICON_SCHEMATICS, Toolkit
//				.getDefaultToolkit().getImage("images/main/schematics_mini.gif"));
//		UIManager.put(SchemeResourceKeys.ICON_COMPONENTS, Toolkit
//				.getDefaultToolkit().getImage("images/main/components_mini.gif"));
//		
//		UIManager.put(SchemeResourceKeys.ICON_CATALOG, new ImageIcon(Toolkit
//				.getDefaultToolkit().getImage("images/folder.gif").getScaledInstance(
//						16, 16, Image.SCALE_SMOOTH)));
//		UIManager.put(SchemeResourceKeys.ICON_SCHEME, new ImageIcon(Toolkit
//				.getDefaultToolkit().getImage("images/scheme.gif").getScaledInstance(
//						16, 16, Image.SCALE_SMOOTH)));
//		UIManager.put(SchemeResourceKeys.ICON_NEW, new ImageIcon(Toolkit
//				.getDefaultToolkit().getImage("images/new.gif").getScaledInstance(16,
//						16, Image.SCALE_SMOOTH)));
//		UIManager.put(SchemeResourceKeys.ICON_SAVE, new ImageIcon(Toolkit
//				.getDefaultToolkit().getImage("images/save.gif").getScaledInstance(16,
//						16, Image.SCALE_SMOOTH)));
//		
//		UIManager.put(SchemeResourceKeys.COLOR_PORT_COMMON, new Color(200, 255, 200));
//		UIManager.put(SchemeResourceKeys.COLOR_PORT_NO_LINK, new Color(255, 255, 200));
//		UIManager.put(SchemeResourceKeys.COLOR_PORT_NO_TYPE, new Color(255, 200, 200));
//		UIManager.put(SchemeResourceKeys.COLOR_PORT_TERMAL, new Color(200, 200, 255));
	}
}
