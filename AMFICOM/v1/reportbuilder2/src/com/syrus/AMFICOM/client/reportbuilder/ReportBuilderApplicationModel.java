/*
 * $Id: ReportBuilderApplicationModel.java,v 1.3 2006/04/11 14:30:02 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;

public class ReportBuilderApplicationModel extends ApplicationModel
{
	public static final String	START							= "_start";
	public static final String	CANCEL							= "_cancel";
	
	public static final String	MENU_WINDOW_TREE				= "menuWindowTree";
	public static final String	MENU_WINDOW_TEMPLATE_SCHEME		= "menuWindowTemplateScheme";

	public static final String	MENU_TEMPLATE					= "menuTemplate";
	public static final String	MENU_REPORT_TEMPLATE_NEW		= "menuReportTemplateNew";
	public static final String	MENU_REPORT_TEMPLATE_SAVE		= "menuReportTemplateSave";
	public static final String	MENU_REPORT_TEMPLATE_SAVE_AS	= "menuReportTemplateSaveAs";	
	public static final String	MENU_REPORT_TEMPLATE_LOAD		= "menuReportTemplateLoad";
	public static final String	MENU_TEMPLATE_PARAMETERS		= "menuTemplateParameters";
	
	public static final String	MENU_INSERT_LABEL				= "menuInsertLabel";
	public static final String	MENU_INSERT_IMAGE				= "menuInsertImage";
	public static final String	MENU_DELETE_OBJECT				= "menuDeleteObject";
	public static final String	MENU_CHANGE_VIEW				= "menuChangeView";
	public static final String	MENU_SAVE_REPORT				= "menuSaveReport";	
	public static final String	MENU_PRINT_REPORT				= "menuPrintReport";	
	
	public ReportBuilderApplicationModel()
	{
		add(ApplicationModel.MENU_SESSION);
		add(ApplicationModel.MENU_SESSION_NEW);
		add(ApplicationModel.MENU_SESSION_CLOSE);
		add(ApplicationModel.MENU_SESSION_OPTIONS);
		add(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD);
		add(ApplicationModel.MENU_EXIT);
		
		add(ApplicationModel.MENU_HELP);
		add(ApplicationModel.MENU_HELP_ABOUT);

		add(MENU_TEMPLATE);
		add(MENU_TEMPLATE_PARAMETERS);
		
		add(ApplicationModel.MENU_VIEW);
		add(MENU_WINDOW_TREE);
		add(MENU_WINDOW_TEMPLATE_SCHEME);
		add(ApplicationModel.MENU_VIEW_ARRANGE);
		
		add(MENU_REPORT_TEMPLATE_NEW);
		add(MENU_REPORT_TEMPLATE_SAVE);
		add(MENU_REPORT_TEMPLATE_SAVE_AS);		
		add(MENU_REPORT_TEMPLATE_LOAD);
		
		//Доступность кнопки
		add(MENU_INSERT_LABEL);
		//Имя +START и +CANCEL - для команды нажатия/отжатия ToggleButton
		add(MENU_INSERT_LABEL + START);
		add(MENU_INSERT_LABEL + CANCEL);
		add(MENU_INSERT_IMAGE);		
		add(MENU_INSERT_IMAGE + START);
		add(MENU_INSERT_IMAGE + CANCEL);		
		add(MENU_DELETE_OBJECT);
		add(MENU_CHANGE_VIEW);
		add(MENU_SAVE_REPORT);
		add(MENU_PRINT_REPORT);		
		
		add(ApplicationModel.MENU_VIEW_ARRANGE);
		
		this.initUIConstats();
	}
	
	private void initUIConstats() {
		//Для внутреннего тулбара рендерера шаблона
		UIManager.put(ReportBuilderResourceKeys.ICON_REPORTBUILDER_MAIN, Toolkit
				.getDefaultToolkit().getImage("images/main/report_mini.gif"));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_INSERT_LABEL,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addtext.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_INSERT_IMAGE,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/graph_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_DELETE_OBJECT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/delete.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_VIEW_REPORT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/view_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_VIEW_TEMPLATE_SCHEME,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_templ_scheme.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_SAVE_REPORT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_PRINT_REPORT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/print_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		//Для главного тулбара		
		UIManager.put(
				ReportBuilderResourceKeys.ICON_NEW_TEMPLATE,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_SAVE_TEMPLATE,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_SAVE_AS_TEMPLATE,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/saveas.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_LOAD_TEMPLATE,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openfile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_IMPORT_TEMPLATES,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/load_template.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.ICON_EXPORT_TEMPLATES,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save_template.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		//Для отображаемых в дереве item'ов
		UIManager.put(
				ReportBuilderResourceKeys.TABLE_TEMPLATE_ELEMENT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/table_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.SCHEME_TEMPLATE_ELEMENT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.GRAPH_TEMPLATE_ELEMENT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/graph_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		
		//TODO Для комбинированных отчётов из редактора шаблонов (Ветвь
		//"Данные для отчётов")по мере необходимости здесь добавлять бандлы
		I18N.addResourceBundle("com.syrus.AMFICOM.Client.General.Lang.schematics");
		I18N.addResourceBundle("com.syrus.AMFICOM.client.resource.map");
		I18N.addResourceBundle("com.syrus.AMFICOM.Client.General.lang.scheduler");
		I18N.addResourceBundle("com.syrus.AMFICOM.client.resource.map");
		I18N.addResourceBundle("com.syrus.AMFICOM.resource.observer");
	}
}
