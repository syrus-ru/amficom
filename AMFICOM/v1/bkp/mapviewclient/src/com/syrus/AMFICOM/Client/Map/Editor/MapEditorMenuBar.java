/**
 * $Id: MapEditorMenuBar.java,v 1.10 2005/02/07 16:09:26 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Панель меню модуля "Редактор топологических схем".
 * @version $Revision: 1.10 $, $Date: 2005/02/07 16:09:26 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorMenuBar extends JMenuBar
		implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenuItem menuSessionNew = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	JMenu menuMap = new JMenu();
	JMenuItem menuMapNew = new JMenuItem();
	JMenuItem menuMapOpen = new JMenuItem();
	JMenuItem menuMapClose = new JMenuItem();
	JMenuItem menuMapSave = new JMenuItem();
	JMenuItem menuMapSaveAs = new JMenuItem();
	JMenuItem menuMapExport = new JMenuItem();
	JMenuItem menuMapImport = new JMenuItem();


	JMenu menuMapView = new JMenu();
	JMenuItem menuMapViewNew = new JMenuItem();
	JMenuItem menuMapViewOpen = new JMenuItem();
	JMenuItem menuMapViewClose = new JMenuItem();
	JMenuItem menuMapViewSave = new JMenuItem();
	JMenuItem menuMapViewSaveAs = new JMenuItem();
	JMenuItem menuMapViewAddScheme = new JMenuItem();
	JMenuItem menuMapViewRemoveScheme = new JMenuItem();

	JMenu menuView = new JMenu();
	JMenuItem menuViewProto = new JMenuItem();
	JMenuItem menuViewAttributes = new JMenuItem();
	JMenuItem menuViewElements = new JMenuItem();
	JMenuItem menuViewSetup = new JMenuItem();
	JMenuItem menuViewMap = new JMenuItem();
	JMenuItem menuViewMapScheme = new JMenuItem();
	JMenuItem menuViewAll = new JMenuItem();
//	JMenuItem menuViewOptions = new JMenuItem();

	JMenu menuReport = new JMenu();
	JMenuItem menuReportCreate = new JMenuItem();

	JMenu menuHelp = new JMenu();
	JMenuItem menuHelpContents = new JMenuItem();
	JMenuItem menuHelpFind = new JMenuItem();
	JMenuItem menuHelpTips = new JMenuItem();
	JMenuItem menuHelpStart = new JMenuItem();
	JMenuItem menuHelpCourse = new JMenuItem();
	JMenuItem menuHelpHelp = new JMenuItem();
	JMenuItem menuHelpSupport = new JMenuItem();
	JMenuItem menuHelpLicense = new JMenuItem();
	JMenuItem menuHelpAbout = new JMenuItem();

	public MapEditorMenuBar()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public MapEditorMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit()
	{
		ActionListener actionAdapter =
			new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						MapEditorMenuBar.this.actionPerformed(e);
					}
				};

		this.menuSession.setText(LangModel.getString("menuSession"));
		this.menuSession.setName("menuSession");
		this.menuSessionNew.setText(LangModel.getString("menuSessionNew"));
		this.menuSessionNew.setName("menuSessionNew");
		this.menuSessionNew.addActionListener(actionAdapter);
		this.menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		this.menuSessionClose.setName("menuSessionClose");
		this.menuSessionClose.addActionListener(actionAdapter);
//		this.menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
//		this.menuSessionOptions.setName("menuSessionOptions");
//		this.menuSessionOptions.addActionListener(actionAdapter);
		this.menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		this.menuSessionConnection.setName("menuSessionConnection");
		this.menuSessionConnection.addActionListener(actionAdapter);
		this.menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		this.menuSessionChangePassword.setName("menuSessionChangePassword");
		this.menuSessionChangePassword.addActionListener(actionAdapter);
		this.menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		this.menuSessionDomain.setName("menuSessionDomain");
		this.menuSessionDomain.addActionListener(actionAdapter);
		this.menuExit.setText(LangModel.getString("menuExit"));
		this.menuExit.setName("menuExit");
		this.menuExit.addActionListener(actionAdapter);

		this.menuView.setText(LangModelMap.getString("menuView"));
		this.menuView.setName("menuView");
		this.menuViewProto.setText(LangModelMap.getString("menuViewProto"));
		this.menuViewProto.setName("menuViewProto");
		this.menuViewProto.addActionListener(actionAdapter);
		this.menuViewAttributes.setText(LangModelMap.getString("menuViewAttributes"));
		this.menuViewAttributes.setName("menuViewAttributes");
		this.menuViewAttributes.addActionListener(actionAdapter);
		this.menuViewElements.setText(LangModelMap.getString("menuViewElements"));
		this.menuViewElements.setName("menuViewElements");
		this.menuViewElements.addActionListener(actionAdapter);
		this.menuViewSetup.setText(LangModelMap.getString("menuViewSetup"));
		this.menuViewSetup.setName("menuViewSetup");
		this.menuViewSetup.addActionListener(actionAdapter);
		this.menuViewMap.setText(LangModelMap.getString("menuViewMap"));
		this.menuViewMap.setName("menuViewMap");
		this.menuViewMap.addActionListener(actionAdapter);
		this.menuViewMapScheme.setText(LangModelMap.getString("menuViewMapScheme"));
		this.menuViewMapScheme.setName("menuViewMapScheme");
		this.menuViewMapScheme.addActionListener(actionAdapter);
		this.menuViewAll.setText(LangModelMap.getString("menuViewAll"));
		this.menuViewAll.setName("menuViewAll");
		this.menuViewAll.addActionListener(actionAdapter);
//		this.menuViewOptions.setText(LangModelMap.getString("menuViewOptions"));
//		this.menuViewOptions.setName("menuViewOptions");
//		this.menuViewOptions.addActionListener(actionAdapter);

		this.menuMap.setText(LangModelMap.getString("menuMap"));
		this.menuMap.setName("menuMap");
		this.menuMapNew.setText(LangModelMap.getString("menuMapNew"));
		this.menuMapNew.setName("menuMapNew");
		this.menuMapNew.addActionListener(actionAdapter);
		this.menuMapOpen.setText(LangModelMap.getString("menuMapOpen"));
		this.menuMapOpen.setName("menuMapOpen");
		this.menuMapOpen.addActionListener(actionAdapter);
		this.menuMapClose.setText(LangModelMap.getString("menuMapClose"));
		this.menuMapClose.setName("menuMapClose");
		this.menuMapClose.addActionListener(actionAdapter);
		this.menuMapSave.setText(LangModelMap.getString("menuMapSave"));
		this.menuMapSave.setName("menuMapSave");
		this.menuMapSave.addActionListener(actionAdapter);
		this.menuMapSaveAs.setText(LangModelMap.getString("menuMapSaveAs"));
		this.menuMapSaveAs.setName("menuMapSaveAs");
		this.menuMapSaveAs.addActionListener(actionAdapter);
		this.menuMapExport.setText(LangModelMap.getString("menuMapExport"));
		this.menuMapExport.setName("menuMapExport");
		this.menuMapExport.addActionListener(actionAdapter);
		this.menuMapImport.setText(LangModelMap.getString("menuMapImport"));
		this.menuMapImport.setName("menuMapImport");
		this.menuMapImport.addActionListener(actionAdapter);

		this.menuMapView.setText(LangModelMap.getString("menuMapView"));
		this.menuMapView.setName("menuMapView");
		this.menuMapViewNew.setText(LangModelMap.getString("menuMapViewNew"));
		this.menuMapViewNew.setName("menuMapViewNew");
		this.menuMapViewNew.addActionListener(actionAdapter);
		this.menuMapViewOpen.setText(LangModelMap.getString("menuMapViewOpen"));
		this.menuMapViewOpen.setName("menuMapViewOpen");
		this.menuMapViewOpen.addActionListener(actionAdapter);
		this.menuMapViewClose.setText(LangModelMap.getString("menuMapViewClose"));
		this.menuMapViewClose.setName("menuMapViewClose");
		this.menuMapViewClose.addActionListener(actionAdapter);
		this.menuMapViewSave.setText(LangModelMap.getString("menuMapViewSave"));
		this.menuMapViewSave.setName("menuMapViewSave");
		this.menuMapViewSave.addActionListener(actionAdapter);
		this.menuMapViewSaveAs.setText(LangModelMap.getString("menuMapViewSaveAs"));
		this.menuMapViewSaveAs.setName("menuMapViewSaveAs");
		this.menuMapViewSaveAs.addActionListener(actionAdapter);
		this.menuMapViewAddScheme.setText(LangModelMap.getString("menuMapViewAddScheme"));
		this.menuMapViewAddScheme.setName("menuMapViewAddScheme");
		this.menuMapViewAddScheme.addActionListener(actionAdapter);
		this.menuMapViewRemoveScheme.setText(LangModelMap.getString("menuMapViewRemoveScheme"));
		this.menuMapViewRemoveScheme.setName("menuMapViewRemoveScheme");
		this.menuMapViewRemoveScheme.addActionListener(actionAdapter);

		this.menuReport.setText(LangModelReport.getString("label_report"));
		this.menuReport.setName("menuReport");
		this.menuReportCreate.setText(LangModelReport.getString("label_reportForTemplate"));
		this.menuReportCreate.setName("menuReportCreate");
		this.menuReportCreate.addActionListener(actionAdapter);
		this.menuReport.add(this.menuReportCreate);

		this.menuHelp.setText(LangModel.getString("menuHelp"));
		this.menuHelp.setName("menuHelp");
		this.menuHelpContents.setText(LangModel.getString("menuHelpContents"));
		this.menuHelpContents.setName("menuHelpContents");
		this.menuHelpContents.addActionListener(actionAdapter);
		this.menuHelpFind.setText(LangModel.getString("menuHelpFind"));
		this.menuHelpFind.setName("menuHelpFind");
		this.menuHelpFind.addActionListener(actionAdapter);
		this.menuHelpTips.setText(LangModel.getString("menuHelpTips"));
		this.menuHelpTips.setName("menuHelpTips");
		this.menuHelpTips.addActionListener(actionAdapter);
		this.menuHelpStart.setText(LangModel.getString("menuHelpStart"));
		this.menuHelpStart.setName("menuHelpStart");
		this.menuHelpStart.addActionListener(actionAdapter);
		this.menuHelpCourse.setText(LangModel.getString("menuHelpCourse"));
		this.menuHelpCourse.setName("menuHelpCourse");
		this.menuHelpCourse.addActionListener(actionAdapter);
		this.menuHelpHelp.setText(LangModel.getString("menuHelpHelp"));
		this.menuHelpHelp.setName("menuHelpHelp");
		this.menuHelpHelp.addActionListener(actionAdapter);
		this.menuHelpSupport.setText(LangModel.getString("menuHelpSupport"));
		this.menuHelpSupport.setName("menuHelpSupport");
		this.menuHelpSupport.addActionListener(actionAdapter);
		this.menuHelpLicense.setText(LangModel.getString("menuHelpLicense"));
		this.menuHelpLicense.setName("menuHelpLicense");
		this.menuHelpLicense.addActionListener(actionAdapter);
		this.menuHelpAbout.setText(LangModel.getString("menuHelpAbout"));
		this.menuHelpAbout.setName("menuHelpAbout");
		this.menuHelpAbout.addActionListener(actionAdapter);

		this.menuSession.add(this.menuSessionNew);
		this.menuSession.add(this.menuSessionClose);
		this.menuSession.add(this.menuSessionChangePassword);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuSessionConnection);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuSessionDomain);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuExit);

		this.menuView.add(this.menuViewProto);
		this.menuView.add(this.menuViewAttributes);
		this.menuView.add(this.menuViewElements);
		this.menuView.add(this.menuViewSetup);
		this.menuView.add(this.menuViewMap);
		this.menuView.add(this.menuViewMapScheme);
		this.menuView.addSeparator();
		this.menuView.add(this.menuViewAll);
 //   this.menuMap.add(this.menuMapOptions);

		this.menuMap.add(this.menuMapNew);
		this.menuMap.add(this.menuMapOpen);
		this.menuMap.addSeparator();
		this.menuMap.add(this.menuMapSave);
		this.menuMap.add(this.menuMapSaveAs);
		this.menuMap.add(this.menuMapClose);
		this.menuMap.addSeparator();
		this.menuMap.add(this.menuMapExport);
		this.menuMap.add(this.menuMapImport);


		this.menuMapView.add(this.menuMapViewNew);
		this.menuMapView.add(this.menuMapViewOpen);
		this.menuMapView.addSeparator();
		this.menuMapView.add(this.menuMapViewSave);
		this.menuMapView.add(this.menuMapViewSaveAs);
		this.menuMapView.add(this.menuMapViewClose);
		this.menuMapView.addSeparator();
		this.menuMapView.add(this.menuMapViewAddScheme);
		this.menuMapView.add(this.menuMapViewRemoveScheme);

//		this.menuHelp.add(this.menuHelpContents);
//		this.menuHelp.add(this.menuHelpFind);
//		this.menuHelp.add(this.menuHelpTips);
//		this.menuHelp.add(this.menuHelpStart);
//		this.menuHelp.add(this.menuHelpCourse);
//		this.menuHelp.addSeparator();
//		this.menuHelp.add(this.menuHelpHelp);
//		this.menuHelp.addSeparator();
//		this.menuHelp.add(this.menuHelpSupport);
//		this.menuHelp.add(this.menuHelpLicense);
//		this.menuHelp.addSeparator();
		this.menuHelp.add(this.menuHelpAbout);

		this.add(this.menuSession);
		this.add(this.menuMap);
		this.add(this.menuMapView);
		this.add(this.menuView);
		this.add(this.menuReport);    
		this.add(this.menuHelp);
	}

	public void setModel(ApplicationModel a)
	{
		this.aModel = a;
	}

	public ApplicationModel getModel()
	{
		return this.aModel;
	}

	public void modelChanged(String e[])
	{
		this.menuSession.setVisible(this.aModel.isVisible("menuSession"));
		this.menuSession.setEnabled(this.aModel.isEnabled("menuSession"));

		this.menuSessionNew.setVisible(this.aModel.isVisible("menuSessionNew"));
		this.menuSessionNew.setEnabled(this.aModel.isEnabled("menuSessionNew"));

		this.menuSessionClose.setVisible(this.aModel.isVisible("menuSessionClose"));
		this.menuSessionClose.setEnabled(this.aModel.isEnabled("menuSessionClose"));

		this.menuSessionConnection.setVisible(this.aModel.isVisible("menuSessionConnection"));
		this.menuSessionConnection.setEnabled(this.aModel.isEnabled("menuSessionConnection"));

		this.menuSessionChangePassword.setVisible(this.aModel.isVisible("menuSessionChangePassword"));
		this.menuSessionChangePassword.setEnabled(this.aModel.isEnabled("menuSessionChangePassword"));

		this.menuSessionDomain.setVisible(this.aModel.isVisible("menuSessionDomain"));
		this.menuSessionDomain.setEnabled(this.aModel.isEnabled("menuSessionDomain"));

		this.menuExit.setVisible(this.aModel.isVisible("menuExit"));
		this.menuExit.setEnabled(this.aModel.isEnabled("menuExit"));

		this.menuView.setVisible(this.aModel.isVisible("menuView"));
		this.menuView.setEnabled(this.aModel.isEnabled("menuView"));

		this.menuViewProto.setVisible(this.aModel.isVisible("menuViewProto"));
		this.menuViewProto.setEnabled(this.aModel.isEnabled("menuViewProto"));

		this.menuViewAttributes.setVisible(this.aModel.isVisible("menuViewAttributes"));
		this.menuViewAttributes.setEnabled(this.aModel.isEnabled("menuViewAttributes"));

		this.menuViewElements.setVisible(this.aModel.isVisible("menuViewElements"));
		this.menuViewElements.setEnabled(this.aModel.isEnabled("menuViewElements"));

		this.menuViewSetup.setVisible(this.aModel.isVisible("menuViewSetup"));
		this.menuViewSetup.setEnabled(this.aModel.isEnabled("menuViewSetup"));

		this.menuViewMap.setVisible(this.aModel.isVisible("menuViewMap"));
		this.menuViewMap.setEnabled(this.aModel.isEnabled("menuViewMap"));

		this.menuViewMapScheme.setVisible(this.aModel.isVisible("menuViewMapScheme"));
		this.menuViewMapScheme.setEnabled(this.aModel.isEnabled("menuViewMapScheme"));

		this.menuViewAll.setVisible(this.aModel.isVisible("menuViewAll"));
		this.menuViewAll.setEnabled(this.aModel.isEnabled("menuViewAll"));

//		this.menuViewOptions.setVisible(this.aModel.isVisible("menuViewOptions"));
//		this.menuViewOptions.setEnabled(this.aModel.isEnabled("menuViewOptions"));

		this.menuMap.setVisible(this.aModel.isVisible("menuMap"));
		this.menuMap.setEnabled(this.aModel.isEnabled("menuMap"));

		this.menuMapNew.setVisible(this.aModel.isVisible("menuMapNew"));
		this.menuMapNew.setEnabled(this.aModel.isEnabled("menuMapNew"));

		this.menuMapOpen.setVisible(this.aModel.isVisible("menuMapOpen"));
		this.menuMapOpen.setEnabled(this.aModel.isEnabled("menuMapOpen"));

		this.menuMapClose.setVisible(this.aModel.isVisible("menuMapClose"));
		this.menuMapClose.setEnabled(this.aModel.isEnabled("menuMapClose"));

		this.menuMapSave.setVisible(this.aModel.isVisible("menuMapSave"));
		this.menuMapSave.setEnabled(this.aModel.isEnabled("menuMapSave"));

		this.menuMapSaveAs.setVisible(this.aModel.isVisible("menuMapSaveAs"));
		this.menuMapSaveAs.setEnabled(this.aModel.isEnabled("menuMapSaveAs"));

		this.menuMapExport.setVisible(this.aModel.isVisible("menuMapExport"));
		this.menuMapExport.setEnabled(this.aModel.isEnabled("menuMapExport"));

		this.menuMapImport.setVisible(this.aModel.isVisible("menuMapImport"));
		this.menuMapImport.setEnabled(this.aModel.isEnabled("menuMapImport"));

		this.menuMapView.setVisible(this.aModel.isVisible("menuMapView"));
		this.menuMapView.setEnabled(this.aModel.isEnabled("menuMapView"));

		this.menuMapViewNew.setVisible(this.aModel.isVisible("menuMapViewNew"));
		this.menuMapViewNew.setEnabled(this.aModel.isEnabled("menuMapViewNew"));

		this.menuMapViewOpen.setVisible(this.aModel.isVisible("menuMapViewOpen"));
		this.menuMapViewOpen.setEnabled(this.aModel.isEnabled("menuMapViewOpen"));

		this.menuMapViewClose.setVisible(this.aModel.isVisible("menuMapViewClose"));
		this.menuMapViewClose.setEnabled(this.aModel.isEnabled("menuMapViewClose"));

		this.menuMapViewSave.setVisible(this.aModel.isVisible("menuMapViewSave"));
		this.menuMapViewSave.setEnabled(this.aModel.isEnabled("menuMapViewSave"));

		this.menuMapViewSaveAs.setVisible(this.aModel.isVisible("menuMapViewSaveAs"));
		this.menuMapViewSaveAs.setEnabled(this.aModel.isEnabled("menuMapViewSaveAs"));

		this.menuMapViewAddScheme.setVisible(this.aModel.isVisible("menuMapViewAddScheme"));
		this.menuMapViewAddScheme.setEnabled(this.aModel.isEnabled("menuMapViewAddScheme"));

		this.menuMapViewRemoveScheme.setVisible(this.aModel.isVisible("menuMapViewRemoveScheme"));
		this.menuMapViewRemoveScheme.setEnabled(this.aModel.isEnabled("menuMapViewRemoveScheme"));

		this.menuHelp.setVisible(this.aModel.isVisible("menuHelp"));
		this.menuHelp.setEnabled(this.aModel.isEnabled("menuHelp"));

		this.menuHelpContents.setVisible(this.aModel.isVisible("menuHelpContents"));
		this.menuHelpContents.setEnabled(this.aModel.isEnabled("menuHelpContents"));

		this.menuHelpFind.setVisible(this.aModel.isVisible("menuHelpFind"));
		this.menuHelpFind.setEnabled(this.aModel.isEnabled("menuHelpFind"));

		this.menuHelpTips.setVisible(this.aModel.isVisible("menuHelpTips"));
		this.menuHelpTips.setEnabled(this.aModel.isEnabled("menuHelpTips"));

		this.menuHelpStart.setVisible(this.aModel.isVisible("menuHelpStart"));
		this.menuHelpStart.setEnabled(this.aModel.isEnabled("menuHelpStart"));

		this.menuHelpCourse.setVisible(this.aModel.isVisible("menuHelpCourse"));
		this.menuHelpCourse.setEnabled(this.aModel.isEnabled("menuHelpCourse"));

		this.menuHelpHelp.setVisible(this.aModel.isVisible("menuHelpHelp"));
		this.menuHelpHelp.setEnabled(this.aModel.isEnabled("menuHelpHelp"));

		this.menuHelpSupport.setVisible(this.aModel.isVisible("menuHelpSupport"));
		this.menuHelpSupport.setEnabled(this.aModel.isEnabled("menuHelpSupport"));

		this.menuHelpLicense.setVisible(this.aModel.isVisible("menuHelpLicense"));
		this.menuHelpLicense.setEnabled(this.aModel.isEnabled("menuHelpLicense"));

		this.menuHelpAbout.setVisible(this.aModel.isVisible("menuHelpAbout"));
		this.menuHelpAbout.setEnabled(this.aModel.isEnabled("menuHelpAbout"));
	}

	public void actionPerformed(ActionEvent e)
	{
		if(this.aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = this.aModel.getCommand(s);
		command.execute();
	}
}

