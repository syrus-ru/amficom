/**
 * $Id: MapEditorMenuBar.java,v 1.16 2005/06/06 12:20:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.editor;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Панель меню модуля "Редактор топологических схем".
 * @version $Revision: 1.16 $, $Date: 2005/06/06 12:20:33 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorMenuBar extends JMenuBar implements ApplicationModelListener {
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
	JMenuItem menuMapAddMap = new JMenuItem();
	JMenuItem menuMapRemoveMap = new JMenuItem();
	JMenuItem menuMapAddExternal = new JMenuItem();
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
	JMenuItem menuViewGeneral = new JMenuItem();
	JMenuItem menuViewAdditional = new JMenuItem();
	JMenuItem menuViewCharacteristics = new JMenuItem();
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

	public MapEditorMenuBar() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public MapEditorMenuBar(ApplicationModel aModel) {
		this();
		this.aModel = aModel;
	}

	private void jbInit() {
		ActionListener actionAdapter = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MapEditorMenuBar.this.actionPerformed(e);
			}
		};

		this.menuSession.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION));
		this.menuSession.setName(MapEditorApplicationModel.ITEM_SESSION);
		this.menuSessionNew.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION_NEW));
		this.menuSessionNew.setName(MapEditorApplicationModel.ITEM_SESSION_NEW);
		this.menuSessionNew.addActionListener(actionAdapter);
		this.menuSessionClose.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION_CLOSE));
		this.menuSessionClose.setName(MapEditorApplicationModel.ITEM_SESSION_CLOSE);
		this.menuSessionClose.addActionListener(actionAdapter);
//		this.menuSessionOptions.setText(LangModelGeneral.getString("menuSessionOptions"));
//		this.menuSessionOptions.setName("menuSessionOptions");
//		this.menuSessionOptions.addActionListener(actionAdapter);
		this.menuSessionConnection.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION_CONNECTION));
		this.menuSessionConnection.setName(MapEditorApplicationModel.ITEM_SESSION_CONNECTION);
		this.menuSessionConnection.addActionListener(actionAdapter);
		this.menuSessionChangePassword.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD));
		this.menuSessionChangePassword.setName(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD);
		this.menuSessionChangePassword.addActionListener(actionAdapter);
		this.menuSessionDomain.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION_DOMAIN));
		this.menuSessionDomain.setName(MapEditorApplicationModel.ITEM_SESSION_DOMAIN);
		this.menuSessionDomain.addActionListener(actionAdapter);
		this.menuExit.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION_EXIT));
		this.menuExit.setName(MapEditorApplicationModel.ITEM_SESSION_EXIT);
		this.menuExit.addActionListener(actionAdapter);

		this.menuView.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW));
		this.menuView.setName(MapEditorApplicationModel.ITEM_VIEW);
		this.menuViewGeneral.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_GENERAL));
		this.menuViewGeneral.setName(MapEditorApplicationModel.ITEM_VIEW_GENERAL);
		this.menuViewGeneral.addActionListener(actionAdapter);
		this.menuViewAdditional.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL));
		this.menuViewAdditional.setName(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL);
		this.menuViewAdditional.addActionListener(actionAdapter);
		this.menuViewCharacteristics.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS));
		this.menuViewCharacteristics.setName(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS);
		this.menuViewCharacteristics.addActionListener(actionAdapter);
		this.menuViewSetup.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));
		this.menuViewSetup.setName(MapEditorApplicationModel.ITEM_VIEW_CONTROLS);
		this.menuViewSetup.addActionListener(actionAdapter);
		this.menuViewMap.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_MAP));
		this.menuViewMap.setName(MapEditorApplicationModel.ITEM_VIEW_MAP);
		this.menuViewMap.addActionListener(actionAdapter);
		this.menuViewMapScheme.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR));
		this.menuViewMapScheme.setName(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR);
		this.menuViewMapScheme.addActionListener(actionAdapter);
		this.menuViewAll.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_ALL));
		this.menuViewAll.setName(MapEditorApplicationModel.ITEM_VIEW_ALL);
		this.menuViewAll.addActionListener(actionAdapter);
//		this.menuViewOptions.setText(LangModelMap.getString("menuViewOptions"));
//		this.menuViewOptions.setName("menuViewOptions");
//		this.menuViewOptions.addActionListener(actionAdapter);

		this.menuMap.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP));
		this.menuMap.setName(MapEditorApplicationModel.ITEM_MAP);
		this.menuMapNew.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_NEW));
		this.menuMapNew.setName(MapEditorApplicationModel.ITEM_MAP_NEW);
		this.menuMapNew.addActionListener(actionAdapter);
		this.menuMapOpen.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_OPEN));
		this.menuMapOpen.setName(MapEditorApplicationModel.ITEM_MAP_OPEN);
		this.menuMapOpen.addActionListener(actionAdapter);
		this.menuMapClose.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_CLOSE));
		this.menuMapClose.setName(MapEditorApplicationModel.ITEM_MAP_CLOSE);
		this.menuMapClose.addActionListener(actionAdapter);
		this.menuMapSave.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_SAVE));
		this.menuMapSave.setName(MapEditorApplicationModel.ITEM_MAP_SAVE);
		this.menuMapSave.addActionListener(actionAdapter);
		this.menuMapSaveAs.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_SAVE_AS));
		this.menuMapSaveAs.setName(MapEditorApplicationModel.ITEM_MAP_SAVE_AS);
		this.menuMapSaveAs.addActionListener(actionAdapter);
		this.menuMapAddMap.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		this.menuMapAddMap.setName(MapEditorApplicationModel.ITEM_MAP_ADD_MAP);
		this.menuMapAddMap.addActionListener(actionAdapter);
		this.menuMapRemoveMap.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		this.menuMapRemoveMap.setName(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP);
		this.menuMapRemoveMap.addActionListener(actionAdapter);
		this.menuMapAddExternal.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		this.menuMapAddExternal.setName(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL);
		this.menuMapAddExternal.addActionListener(actionAdapter);
		this.menuMapExport.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_EXPORT));
		this.menuMapExport.setName(MapEditorApplicationModel.ITEM_MAP_EXPORT);
		this.menuMapExport.addActionListener(actionAdapter);
		this.menuMapImport.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_IMPORT));
		this.menuMapImport.setName(MapEditorApplicationModel.ITEM_MAP_IMPORT);
		this.menuMapImport.addActionListener(actionAdapter);

		this.menuMapView.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW));
		this.menuMapView.setName(MapEditorApplicationModel.ITEM_MAP_VIEW);
		this.menuMapViewNew.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		this.menuMapViewNew.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW);
		this.menuMapViewNew.addActionListener(actionAdapter);
		this.menuMapViewOpen.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		this.menuMapViewOpen.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN);
		this.menuMapViewOpen.addActionListener(actionAdapter);
		this.menuMapViewClose.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE));
		this.menuMapViewClose.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE);
		this.menuMapViewClose.addActionListener(actionAdapter);
		this.menuMapViewSave.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		this.menuMapViewSave.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE);
		this.menuMapViewSave.addActionListener(actionAdapter);
		this.menuMapViewSaveAs.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS));
		this.menuMapViewSaveAs.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS);
		this.menuMapViewSaveAs.addActionListener(actionAdapter);
		this.menuMapViewAddScheme.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		this.menuMapViewAddScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME);
		this.menuMapViewAddScheme.addActionListener(actionAdapter);
		this.menuMapViewRemoveScheme.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
		this.menuMapViewRemoveScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME);
		this.menuMapViewRemoveScheme.addActionListener(actionAdapter);

//		this.menuReport.setText(LangModelGeneral.getString("label_report"));
		this.menuReport.setName(MapEditorApplicationModel.ITEM_REPORT);
//		this.menuReportCreate.setText(LangModelGeneral.getString("label_reportForTemplate"));
		this.menuReportCreate.setName(MapEditorApplicationModel.ITEM_REPORT_CREATE);
		this.menuReportCreate.addActionListener(actionAdapter);
		this.menuReport.add(this.menuReportCreate);

		this.menuHelp.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP));
		this.menuHelp.setName(MapEditorApplicationModel.ITEM_HELP);
//		this.menuHelpContents.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP_CONTENTS));
		this.menuHelpContents.setName(MapEditorApplicationModel.ITEM_HELP_CONTENTS);
		this.menuHelpContents.addActionListener(actionAdapter);
//		this.menuHelpFind.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP_FIND));
		this.menuHelpFind.setName(MapEditorApplicationModel.ITEM_HELP_FIND);
		this.menuHelpFind.addActionListener(actionAdapter);
//		this.menuHelpTips.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP_FIND));
		this.menuHelpTips.setName(MapEditorApplicationModel.ITEM_HELP_FIND);
		this.menuHelpTips.addActionListener(actionAdapter);
//		this.menuHelpStart.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP_START));
		this.menuHelpStart.setName(MapEditorApplicationModel.ITEM_HELP_START);
		this.menuHelpStart.addActionListener(actionAdapter);
//		this.menuHelpCourse.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP_COURSE));
		this.menuHelpCourse.setName(MapEditorApplicationModel.ITEM_HELP_COURSE);
		this.menuHelpCourse.addActionListener(actionAdapter);
//		this.menuHelpHelp.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP_HELP));
		this.menuHelpHelp.setName(MapEditorApplicationModel.ITEM_HELP_HELP);
		this.menuHelpHelp.addActionListener(actionAdapter);
//		this.menuHelpSupport.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP_SUPPORT));
		this.menuHelpSupport.setName(MapEditorApplicationModel.ITEM_HELP_SUPPORT);
		this.menuHelpSupport.addActionListener(actionAdapter);
//		this.menuHelpLicense.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP_LICENSE));
		this.menuHelpLicense.setName(MapEditorApplicationModel.ITEM_HELP_LICENSE);
		this.menuHelpLicense.addActionListener(actionAdapter);
		this.menuHelpAbout.setText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_HELP_ABOUT));
		this.menuHelpAbout.setName(MapEditorApplicationModel.ITEM_HELP_ABOUT);
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

		this.menuView.add(this.menuViewGeneral);
		this.menuView.add(this.menuViewAdditional);
		this.menuView.add(this.menuViewCharacteristics);
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
		this.menuMap.add(this.menuMapAddMap);
		this.menuMap.add(this.menuMapRemoveMap);
		this.menuMap.addSeparator();
		this.menuMap.add(this.menuMapAddExternal);
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

	public void setModel(ApplicationModel a) {
		this.aModel = a;
	}

	public ApplicationModel getModel() {
		return this.aModel;
	}

	public void modelChanged(String e) {
		modelChanged(new String[] { e
		});
	}

	public void modelChanged(String[] elementNames) {
		this.menuSession.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION));
		this.menuSession.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION));

		this.menuSessionNew.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION_NEW));
		this.menuSessionNew.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION_NEW));

		this.menuSessionClose.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION_CLOSE));
		this.menuSessionClose.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION_CLOSE));

		this.menuSessionConnection.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION_CONNECTION));
		this.menuSessionConnection.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION_CONNECTION));

		this.menuSessionChangePassword.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD));
		this.menuSessionChangePassword.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD));

		this.menuSessionDomain.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION_DOMAIN));
		this.menuSessionDomain.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION_DOMAIN));

		this.menuExit.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION_EXIT));
		this.menuExit.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION_EXIT));

		this.menuView.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW));
		this.menuView.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW));

		this.menuViewGeneral.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_GENERAL));
		this.menuViewGeneral.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_GENERAL));

		this.menuViewAdditional.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL));
		this.menuViewAdditional.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL));

		this.menuViewCharacteristics.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS));
		this.menuViewCharacteristics.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS));

		this.menuViewSetup.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));
		this.menuViewSetup.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));

		this.menuViewMap.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_MAP));
		this.menuViewMap.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP));

		this.menuViewMapScheme.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR));
		this.menuViewMapScheme.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR));

		this.menuViewAll.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_ALL));
		this.menuViewAll.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_ALL));

//		this.menuViewOptions.setVisible(this.aModel.isVisible("menuViewOptions"));
//		this.menuViewOptions.setEnabled(this.aModel.isEnabled("menuViewOptions"));

		this.menuMap.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP));
		this.menuMap.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP));

		this.menuMapNew.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_NEW));
		this.menuMapNew.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_NEW));

		this.menuMapOpen.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_OPEN));
		this.menuMapOpen.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN));

		this.menuMapClose.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_CLOSE));
		this.menuMapClose.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE));

		this.menuMapSave.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_SAVE));
		this.menuMapSave.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE));

		this.menuMapSaveAs.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_SAVE_AS));
		this.menuMapSaveAs.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS));

		this.menuMapAddMap.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		this.menuMapAddMap.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));

		this.menuMapRemoveMap.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		this.menuMapRemoveMap.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));

		this.menuMapAddExternal.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		this.menuMapAddExternal.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));

		this.menuMapExport.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_EXPORT));
		this.menuMapExport.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT));

		this.menuMapImport.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_IMPORT));
		this.menuMapImport.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_IMPORT));

		this.menuMapView.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW));
		this.menuMapView.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW));

		this.menuMapViewNew.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		this.menuMapViewNew.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));

		this.menuMapViewOpen.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		this.menuMapViewOpen.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));

		this.menuMapViewClose.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE));
		this.menuMapViewClose.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE));

		this.menuMapViewSave.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		this.menuMapViewSave.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));

		this.menuMapViewSaveAs.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS));
		this.menuMapViewSaveAs.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS));

		this.menuMapViewAddScheme.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		this.menuMapViewAddScheme.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));

		this.menuMapViewRemoveScheme.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
		this.menuMapViewRemoveScheme.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));

		this.menuHelp.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP));
		this.menuHelp.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP));

		this.menuHelpContents.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP_CONTENTS));
		this.menuHelpContents.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP_CONTENTS));

		this.menuHelpFind.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP_FIND));
		this.menuHelpFind.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP_FIND));

		this.menuHelpTips.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP_FIND));
		this.menuHelpTips.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP_FIND));

		this.menuHelpStart.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP_START));
		this.menuHelpStart.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP_START));

		this.menuHelpCourse.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP_COURSE));
		this.menuHelpCourse.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP_COURSE));

		this.menuHelpHelp.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP_HELP));
		this.menuHelpHelp.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP_HELP));

		this.menuHelpSupport.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP_SUPPORT));
		this.menuHelpSupport.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP_SUPPORT));

		this.menuHelpLicense.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP_LICENSE));
		this.menuHelpLicense.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP_LICENSE));

		this.menuHelpAbout.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_HELP_ABOUT));
		this.menuHelpAbout.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_HELP_ABOUT));
	}

	public void actionPerformed(ActionEvent e) {
		if(this.aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = this.aModel.getCommand(s);
		command.execute();
	}

}
