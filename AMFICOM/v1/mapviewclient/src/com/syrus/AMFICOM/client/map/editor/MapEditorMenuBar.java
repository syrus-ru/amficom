/**
 * $Id: MapEditorMenuBar.java,v 1.5 2004/10/06 14:11:56 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;

import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

/**
 * Панель меню модуля "Редактор топологических схем" 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/10/06 14:11:56 $
 * @module
 * @author $Author: krupenn $
 * @see
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

	JMenu menuScheme = new JMenu();
	JMenuItem menuSchemeAddToView = new JMenuItem();
	JMenuItem menuSchemeRemoveFromView = new JMenuItem();

	JMenu menuMapView = new JMenu();
	JMenuItem menuMapViewNew = new JMenuItem();
	JMenuItem menuMapViewOpen = new JMenuItem();
	JMenuItem menuMapViewClose = new JMenuItem();
	JMenuItem menuMapViewSave = new JMenuItem();
	JMenuItem menuMapViewSaveAs = new JMenuItem();

	JMenu menuView = new JMenu();
	JMenuItem menuViewProto = new JMenuItem();
	JMenuItem menuViewAttributes = new JMenuItem();
	JMenuItem menuViewElements = new JMenuItem();
	JMenuItem menuViewSetup = new JMenuItem();
	JMenuItem menuViewMap = new JMenuItem();
	JMenuItem menuViewMapScheme = new JMenuItem();
	JMenuItem menuViewAll = new JMenuItem();
//	JMenuItem menuViewOptions = new JMenuItem();

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

		menuSession.setText(LangModel.getString("menuSession"));
		menuSession.setName("menuSession");
		menuSessionNew.setText(LangModel.getString("menuSessionNew"));
		menuSessionNew.setName("menuSessionNew");
		menuSessionNew.addActionListener(actionAdapter);
		menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		menuSessionClose.setName("menuSessionClose");
		menuSessionClose.addActionListener(actionAdapter);
//		menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
//		menuSessionOptions.setName("menuSessionOptions");
//		menuSessionOptions.addActionListener(actionAdapter);
		menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		menuSessionConnection.setName("menuSessionConnection");
		menuSessionConnection.addActionListener(actionAdapter);
		menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		menuSessionChangePassword.setName("menuSessionChangePassword");
		menuSessionChangePassword.addActionListener(actionAdapter);
		menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
		menuSessionDomain.addActionListener(actionAdapter);
		menuExit.setText(LangModel.getString("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

		menuView.setText(LangModelMap.getString("menuView"));
		menuView.setName("menuView");
		menuViewProto.setText(LangModelMap.getString("menuViewProto"));
		menuViewProto.setName("menuViewProto");
		menuViewProto.addActionListener(actionAdapter);
		menuViewAttributes.setText(LangModelMap.getString("menuViewAttributes"));
		menuViewAttributes.setName("menuViewAttributes");
		menuViewAttributes.addActionListener(actionAdapter);
		menuViewElements.setText(LangModelMap.getString("menuViewElements"));
		menuViewElements.setName("menuViewElements");
		menuViewElements.addActionListener(actionAdapter);
		menuViewSetup.setText(LangModelMap.getString("menuViewSetup"));
		menuViewSetup.setName("menuViewSetup");
		menuViewSetup.addActionListener(actionAdapter);
		menuViewMap.setText(LangModelMap.getString("menuViewMap"));
		menuViewMap.setName("menuViewMap");
		menuViewMap.addActionListener(actionAdapter);
		menuViewMapScheme.setText(LangModelMap.getString("menuViewMapScheme"));
		menuViewMapScheme.setName("menuViewMapScheme");
		menuViewMapScheme.addActionListener(actionAdapter);
		menuViewAll.setText(LangModelMap.getString("menuViewAll"));
		menuViewAll.setName("menuViewAll");
		menuViewAll.addActionListener(actionAdapter);
//		menuViewOptions.setText(LangModelMap.getString("menuViewOptions"));
//		menuViewOptions.setName("menuViewOptions");
//		menuViewOptions.addActionListener(actionAdapter);

		menuMap.setText(LangModelMap.getString("menuMap"));
		menuMap.setName("menuMap");
		menuMapNew.setText(LangModelMap.getString("menuMapNew"));
		menuMapNew.setName("menuMapNew");
		menuMapNew.addActionListener(actionAdapter);
		menuMapOpen.setText(LangModelMap.getString("menuMapOpen"));
		menuMapOpen.setName("menuMapOpen");
		menuMapOpen.addActionListener(actionAdapter);
		menuMapClose.setText(LangModelMap.getString("menuMapClose"));
		menuMapClose.setName("menuMapClose");
		menuMapClose.addActionListener(actionAdapter);
		menuMapSave.setText(LangModelMap.getString("menuMapSave"));
		menuMapSave.setName("menuMapSave");
		menuMapSave.addActionListener(actionAdapter);
		menuMapSaveAs.setText(LangModelMap.getString("menuMapSaveAs"));
		menuMapSaveAs.setName("menuMapSaveAs");
		menuMapSaveAs.addActionListener(actionAdapter);
		menuMapExport.setText(LangModelMap.getString("menuMapExport"));
		menuMapExport.setName("menuMapExport");
		menuMapExport.addActionListener(actionAdapter);
		menuMapImport.setText(LangModelMap.getString("menuMapImport"));
		menuMapImport.setName("menuMapImport");
		menuMapImport.addActionListener(actionAdapter);

		menuScheme.setText(LangModelMap.getString("menuScheme"));
		menuScheme.setName("menuScheme");
		menuSchemeAddToView.setText(LangModelMap.getString("menuSchemeAddToView"));
		menuSchemeAddToView.setName("menuSchemeAddToView");
		menuSchemeAddToView.addActionListener(actionAdapter);
		menuSchemeRemoveFromView.setText(LangModelMap.getString("menuSchemeRemoveFromView"));
		menuSchemeRemoveFromView.setName("menuSchemeRemoveFromView");
		menuSchemeRemoveFromView.addActionListener(actionAdapter);

		menuMapView.setText(LangModelMap.getString("menuMapView"));
		menuMapView.setName("menuMapView");
		menuMapViewNew.setText(LangModelMap.getString("menuMapViewNew"));
		menuMapViewNew.setName("menuMapViewNew");
		menuMapViewNew.addActionListener(actionAdapter);
		menuMapViewOpen.setText(LangModelMap.getString("menuMapViewOpen"));
		menuMapViewOpen.setName("menuMapViewOpen");
		menuMapViewOpen.addActionListener(actionAdapter);
		menuMapViewClose.setText(LangModelMap.getString("menuMapViewClose"));
		menuMapViewClose.setName("menuMapViewClose");
		menuMapViewClose.addActionListener(actionAdapter);
		menuMapViewSave.setText(LangModelMap.getString("menuMapViewSave"));
		menuMapViewSave.setName("menuMapViewSave");
		menuMapViewSave.addActionListener(actionAdapter);
		menuMapViewSaveAs.setText(LangModelMap.getString("menuMapViewSaveAs"));
		menuMapViewSaveAs.setName("menuMapViewSaveAs");
		menuMapViewSaveAs.addActionListener(actionAdapter);

		menuHelp.setText(LangModel.getString("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModel.getString("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModel.getString("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModel.getString("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpStart.setText(LangModel.getString("menuHelpStart"));
		menuHelpStart.setName("menuHelpStart");
		menuHelpStart.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModel.getString("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModel.getString("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpSupport.setText(LangModel.getString("menuHelpSupport"));
		menuHelpSupport.setName("menuHelpSupport");
		menuHelpSupport.addActionListener(actionAdapter);
		menuHelpLicense.setText(LangModel.getString("menuHelpLicense"));
		menuHelpLicense.setName("menuHelpLicense");
		menuHelpLicense.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModel.getString("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuView.add(menuViewProto);
		menuView.add(menuViewAttributes);
		menuView.add(menuViewElements);
		menuView.add(menuViewSetup);
		menuView.add(menuViewMap);
		menuView.add(menuViewMapScheme);
		menuView.addSeparator();
		menuView.add(menuViewAll);
 //   menuMap.add(menuMapOptions);

		menuMap.add(menuMapNew);
		menuMap.add(menuMapOpen);
		menuMap.addSeparator();
		menuMap.add(menuMapSave);
		menuMap.add(menuMapSaveAs);
		menuMap.add(menuMapClose);
		menuMap.addSeparator();
		menuMap.add(menuMapExport);
		menuMap.add(menuMapImport);

		menuScheme.add(menuSchemeAddToView);
		menuScheme.add(menuSchemeRemoveFromView);

		menuMapView.add(menuMapViewNew);
		menuMapView.add(menuMapViewOpen);
		menuMapView.addSeparator();
		menuMapView.add(menuMapViewSave);
		menuMapView.add(menuMapViewSaveAs);
		menuMapView.add(menuMapViewClose);

		menuHelp.add(menuHelpContents);
		menuHelp.add(menuHelpFind);
		menuHelp.add(menuHelpTips);
		menuHelp.add(menuHelpStart);
		menuHelp.add(menuHelpCourse);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpHelp);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpSupport);
		menuHelp.add(menuHelpLicense);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpAbout);

		this.add(menuSession);
		this.add(menuMap);
		this.add(menuMapView);
		this.add(menuScheme);
		this.add(menuView);
		this.add(menuHelp);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void modelChanged(String e[])
	{
		menuSession.setVisible(aModel.isVisible("menuSession"));
		menuSession.setEnabled(aModel.isEnabled("menuSession"));

		menuSessionNew.setVisible(aModel.isVisible("menuSessionNew"));
		menuSessionNew.setEnabled(aModel.isEnabled("menuSessionNew"));

		menuSessionClose.setVisible(aModel.isVisible("menuSessionClose"));
		menuSessionClose.setEnabled(aModel.isEnabled("menuSessionClose"));

		menuSessionConnection.setVisible(aModel.isVisible("menuSessionConnection"));
		menuSessionConnection.setEnabled(aModel.isEnabled("menuSessionConnection"));

		menuSessionChangePassword.setVisible(aModel.isVisible("menuSessionChangePassword"));
		menuSessionChangePassword.setEnabled(aModel.isEnabled("menuSessionChangePassword"));

		menuSessionDomain.setVisible(aModel.isVisible("menuSessionDomain"));
		menuSessionDomain.setEnabled(aModel.isEnabled("menuSessionDomain"));

		menuExit.setVisible(aModel.isVisible("menuExit"));
		menuExit.setEnabled(aModel.isEnabled("menuExit"));

		menuView.setVisible(aModel.isVisible("menuView"));
		menuView.setEnabled(aModel.isEnabled("menuView"));

		menuViewProto.setVisible(aModel.isVisible("menuViewProto"));
		menuViewProto.setEnabled(aModel.isEnabled("menuViewProto"));

		menuViewAttributes.setVisible(aModel.isVisible("menuViewAttributes"));
		menuViewAttributes.setEnabled(aModel.isEnabled("menuViewAttributes"));

		menuViewElements.setVisible(aModel.isVisible("menuViewElements"));
		menuViewElements.setEnabled(aModel.isEnabled("menuViewElements"));

		menuViewSetup.setVisible(aModel.isVisible("menuViewSetup"));
		menuViewSetup.setEnabled(aModel.isEnabled("menuViewSetup"));

		menuViewMap.setVisible(aModel.isVisible("menuViewMap"));
		menuViewMap.setEnabled(aModel.isEnabled("menuViewMap"));

		menuViewMapScheme.setVisible(aModel.isVisible("menuViewMapScheme"));
		menuViewMapScheme.setEnabled(aModel.isEnabled("menuViewMapScheme"));

		menuViewAll.setVisible(aModel.isVisible("menuViewAll"));
		menuViewAll.setEnabled(aModel.isEnabled("menuViewAll"));

//		menuViewOptions.setVisible(aModel.isVisible("menuViewOptions"));
//		menuViewOptions.setEnabled(aModel.isEnabled("menuViewOptions"));

		menuMap.setVisible(aModel.isVisible("menuMap"));
		menuMap.setEnabled(aModel.isEnabled("menuMap"));

		menuMapNew.setVisible(aModel.isVisible("menuMapNew"));
		menuMapNew.setEnabled(aModel.isEnabled("menuMapNew"));

		menuMapOpen.setVisible(aModel.isVisible("menuMapOpen"));
		menuMapOpen.setEnabled(aModel.isEnabled("menuMapOpen"));

		menuMapClose.setVisible(aModel.isVisible("menuMapClose"));
		menuMapClose.setEnabled(aModel.isEnabled("menuMapClose"));

		menuMapSave.setVisible(aModel.isVisible("menuMapSave"));
		menuMapSave.setEnabled(aModel.isEnabled("menuMapSave"));

		menuMapSaveAs.setVisible(aModel.isVisible("menuMapSaveAs"));
		menuMapSaveAs.setEnabled(aModel.isEnabled("menuMapSaveAs"));

		menuMapExport.setVisible(aModel.isVisible("menuMapExport"));
		menuMapExport.setEnabled(aModel.isEnabled("menuMapExport"));

		menuMapImport.setVisible(aModel.isVisible("menuMapImport"));
		menuMapImport.setEnabled(aModel.isEnabled("menuMapImport"));

		menuScheme.setVisible(aModel.isVisible("menuScheme"));
		menuScheme.setEnabled(aModel.isEnabled("menuScheme"));

		menuSchemeAddToView.setVisible(aModel.isVisible("menuSchemeAddToView"));
		menuSchemeAddToView.setEnabled(aModel.isEnabled("menuSchemeAddToView"));

		menuSchemeRemoveFromView.setVisible(aModel.isVisible("menuSchemeRemoveFromView"));
		menuSchemeRemoveFromView.setEnabled(aModel.isEnabled("menuSchemeRemoveFromView"));

		menuMapView.setVisible(aModel.isVisible("menuMapView"));
		menuMapView.setEnabled(aModel.isEnabled("menuMapView"));

		menuMapViewNew.setVisible(aModel.isVisible("menuMapViewNew"));
		menuMapViewNew.setEnabled(aModel.isEnabled("menuMapViewNew"));

		menuMapViewOpen.setVisible(aModel.isVisible("menuMapViewOpen"));
		menuMapViewOpen.setEnabled(aModel.isEnabled("menuMapViewOpen"));

		menuMapViewClose.setVisible(aModel.isVisible("menuMapViewClose"));
		menuMapViewClose.setEnabled(aModel.isEnabled("menuMapViewClose"));

		menuMapViewSave.setVisible(aModel.isVisible("menuMapViewSave"));
		menuMapViewSave.setEnabled(aModel.isEnabled("menuMapViewSave"));

		menuMapViewSaveAs.setVisible(aModel.isVisible("menuMapViewSaveAs"));
		menuMapViewSaveAs.setEnabled(aModel.isEnabled("menuMapViewSaveAs"));

		menuHelp.setVisible(aModel.isVisible("menuHelp"));
		menuHelp.setEnabled(aModel.isEnabled("menuHelp"));

		menuHelpContents.setVisible(aModel.isVisible("menuHelpContents"));
		menuHelpContents.setEnabled(aModel.isEnabled("menuHelpContents"));

		menuHelpFind.setVisible(aModel.isVisible("menuHelpFind"));
		menuHelpFind.setEnabled(aModel.isEnabled("menuHelpFind"));

		menuHelpTips.setVisible(aModel.isVisible("menuHelpTips"));
		menuHelpTips.setEnabled(aModel.isEnabled("menuHelpTips"));

		menuHelpStart.setVisible(aModel.isVisible("menuHelpStart"));
		menuHelpStart.setEnabled(aModel.isEnabled("menuHelpStart"));

		menuHelpCourse.setVisible(aModel.isVisible("menuHelpCourse"));
		menuHelpCourse.setEnabled(aModel.isEnabled("menuHelpCourse"));

		menuHelpHelp.setVisible(aModel.isVisible("menuHelpHelp"));
		menuHelpHelp.setEnabled(aModel.isEnabled("menuHelpHelp"));

		menuHelpSupport.setVisible(aModel.isVisible("menuHelpSupport"));
		menuHelpSupport.setEnabled(aModel.isEnabled("menuHelpSupport"));

		menuHelpLicense.setVisible(aModel.isVisible("menuHelpLicense"));
		menuHelpLicense.setEnabled(aModel.isEnabled("menuHelpLicense"));

		menuHelpAbout.setVisible(aModel.isVisible("menuHelpAbout"));
		menuHelpAbout.setEnabled(aModel.isEnabled("menuHelpAbout"));
	}

	public void actionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command.execute();
	}
}

