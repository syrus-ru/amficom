/*
 * $Id: ReportBuilderApplicationModel.java,v 1.8 2005/09/16 13:26:30 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class ReportBuilderApplicationModel extends ApplicationModel
{
	public static final String	START							= "_start";
	public static final String	CANCEL							= "_cancel";
	
	public static final String	MENU_WINDOW						= "menuWindow";
	public static final String	MENU_WINDOW_TREE				= "menuWindowTree";
	public static final String	MENU_WINDOW_TEMPLATE_SCHEME		= "menuWindowTemplateScheme";

	public static final String	MENU_TEMPLATE					= "menuTemplate";
	public static final String	MENU_TEMPLATE_PARAMETERS		= "menuTemplateParameters";
	
	public static final String	MENU_REPORT_TEMPLATE_NEW		= "menuReportTemplateNew";
	public static final String	MENU_REPORT_TEMPLATE_SAVE		= "menuReportTemplateSave";
	public static final String	MENU_REPORT_TEMPLATE_SAVE_AS	= "menuReportTemplateSaveAs";	
	public static final String	MENU_REPORT_TEMPLATE_LOAD		= "menuReportTemplateLoad";
	public static final String	MENU_REPORT_TEMPLATE_EXPORT		= "menuReportTemplateExport";
	public static final String	MENU_REPORT_TEMPLATE_IMPORT		= "menuReportTemplateImport";	
	
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
		add(ApplicationModel.MENU_SESSION_DOMAIN);
		add(ApplicationModel.MENU_EXIT);

		add(MENU_TEMPLATE);
		add(MENU_TEMPLATE_PARAMETERS);
		
		add(MENU_WINDOW);
		add(MENU_WINDOW_TREE);
		add(MENU_WINDOW_TEMPLATE_SCHEME);
		
		add(MENU_REPORT_TEMPLATE_NEW);
		add(MENU_REPORT_TEMPLATE_SAVE);
		add(MENU_REPORT_TEMPLATE_SAVE_AS);		
		add(MENU_REPORT_TEMPLATE_LOAD);
		add(MENU_REPORT_TEMPLATE_EXPORT);
		add(MENU_REPORT_TEMPLATE_IMPORT);

		//����������� ������
		add(MENU_INSERT_LABEL);
		//��� +START � +CANCEL - ��� ������� �������/������� ToggleButton
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
		//��� ����������� ������� ��������� �������
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
		//��� �������� �������		
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
		//��� ������������ � ������ item'��
		UIManager.put(
				ReportBuilderResourceKeys.TABLE_TEMPLATE_ELEMENT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/table_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.SCHEME_TEMPLATE_ELEMENT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(
				ReportBuilderResourceKeys.GRAPH_TEMPLATE_ELEMENT,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/graph_report.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	}
}
