/**
 * $Id: MapEditorMenuBar.java,v 1.18 2005/06/14 12:07:14 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.editor;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * Панель меню модуля "Редактор топологических схем".
 * @version $Revision: 1.18 $, $Date: 2005/06/14 12:07:14 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorMenuBar extends AbstractMainMenuBar {

	public MapEditorMenuBar(ApplicationModel applicationModel) {
		super(applicationModel);
	}

	protected void addMenuItems() {

		final JMenu menuMap = new JMenu();
		final JMenuItem menuMapNew = new JMenuItem();
		final JMenuItem menuMapOpen = new JMenuItem();
		final JMenuItem menuMapClose = new JMenuItem();
		final JMenuItem menuMapSave = new JMenuItem();
		final JMenuItem menuMapSaveAs = new JMenuItem();
		final JMenuItem menuMapAddMap = new JMenuItem();
		final JMenuItem menuMapRemoveMap = new JMenuItem();
		final JMenuItem menuMapAddExternal = new JMenuItem();
		final JMenuItem menuMapExport = new JMenuItem();
		final JMenuItem menuMapImport = new JMenuItem();
	
	
		final JMenu menuMapView = new JMenu();
		final JMenuItem menuMapViewNew = new JMenuItem();
		final JMenuItem menuMapViewOpen = new JMenuItem();
		final JMenuItem menuMapViewClose = new JMenuItem();
		final JMenuItem menuMapViewSave = new JMenuItem();
		final JMenuItem menuMapViewSaveAs = new JMenuItem();
		final JMenuItem menuMapViewAddScheme = new JMenuItem();
		final JMenuItem menuMapViewRemoveScheme = new JMenuItem();
	
		final JMenu menuView = new JMenu();
		final JMenuItem menuViewGeneral = new JMenuItem();
		final JMenuItem menuViewAdditional = new JMenuItem();
		final JMenuItem menuViewCharacteristics = new JMenuItem();
		final JMenuItem menuViewSetup = new JMenuItem();
		final JMenuItem menuViewMap = new JMenuItem();
		final JMenuItem menuViewMapScheme = new JMenuItem();
		final JMenuItem menuViewAll = new JMenuItem();
	//	final JMenuItem menuViewOptions = new JMenuItem();
	
		final JMenu menuReport = new JMenu();
		final JMenuItem menuReportCreate = new JMenuItem();
	
		menuView.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW));
		menuView.setName(MapEditorApplicationModel.ITEM_VIEW);
		menuViewGeneral.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_GENERAL));
		menuViewGeneral.setName(MapEditorApplicationModel.ITEM_VIEW_GENERAL);
		menuViewGeneral.addActionListener(super.actionAdapter);
		menuViewAdditional.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL));
		menuViewAdditional.setName(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL);
		menuViewAdditional.addActionListener(super.actionAdapter);
		menuViewCharacteristics.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS));
		menuViewCharacteristics.setName(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS);
		menuViewCharacteristics.addActionListener(super.actionAdapter);
		menuViewSetup.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));
		menuViewSetup.setName(MapEditorApplicationModel.ITEM_VIEW_CONTROLS);
		menuViewSetup.addActionListener(super.actionAdapter);
		menuViewMap.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_MAP));
		menuViewMap.setName(MapEditorApplicationModel.ITEM_VIEW_MAP);
		menuViewMap.addActionListener(super.actionAdapter);
		menuViewMapScheme.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR));
		menuViewMapScheme.setName(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR);
		menuViewMapScheme.addActionListener(super.actionAdapter);
		menuViewAll.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_ALL));
		menuViewAll.setName(MapEditorApplicationModel.ITEM_VIEW_ALL);
		menuViewAll.addActionListener(super.actionAdapter);
//		menuViewOptions.setText(LangModelMap.getString("menuViewOptions"));
//		menuViewOptions.setName("menuViewOptions");
//		menuViewOptions.addActionListener(super.actionAdapter);

		menuMap.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP));
		menuMap.setName(MapEditorApplicationModel.ITEM_MAP);
		menuMapNew.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_NEW));
		menuMapNew.setName(MapEditorApplicationModel.ITEM_MAP_NEW);
		menuMapNew.addActionListener(super.actionAdapter);
		menuMapOpen.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_OPEN));
		menuMapOpen.setName(MapEditorApplicationModel.ITEM_MAP_OPEN);
		menuMapOpen.addActionListener(super.actionAdapter);
		menuMapClose.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_CLOSE));
		menuMapClose.setName(MapEditorApplicationModel.ITEM_MAP_CLOSE);
		menuMapClose.addActionListener(super.actionAdapter);
		menuMapSave.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_SAVE));
		menuMapSave.setName(MapEditorApplicationModel.ITEM_MAP_SAVE);
		menuMapSave.addActionListener(super.actionAdapter);
		menuMapSaveAs.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_SAVE_AS));
		menuMapSaveAs.setName(MapEditorApplicationModel.ITEM_MAP_SAVE_AS);
		menuMapSaveAs.addActionListener(super.actionAdapter);
		menuMapAddMap.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		menuMapAddMap.setName(MapEditorApplicationModel.ITEM_MAP_ADD_MAP);
		menuMapAddMap.addActionListener(super.actionAdapter);
		menuMapRemoveMap.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		menuMapRemoveMap.setName(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP);
		menuMapRemoveMap.addActionListener(super.actionAdapter);
		menuMapAddExternal.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		menuMapAddExternal.setName(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL);
		menuMapAddExternal.addActionListener(super.actionAdapter);
		menuMapExport.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_EXPORT));
		menuMapExport.setName(MapEditorApplicationModel.ITEM_MAP_EXPORT);
		menuMapExport.addActionListener(super.actionAdapter);
		menuMapImport.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_IMPORT));
		menuMapImport.setName(MapEditorApplicationModel.ITEM_MAP_IMPORT);
		menuMapImport.addActionListener(super.actionAdapter);

		menuMapView.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW));
		menuMapView.setName(MapEditorApplicationModel.ITEM_MAP_VIEW);
		menuMapViewNew.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		menuMapViewNew.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW);
		menuMapViewNew.addActionListener(super.actionAdapter);
		menuMapViewOpen.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		menuMapViewOpen.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN);
		menuMapViewOpen.addActionListener(super.actionAdapter);
		menuMapViewClose.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE));
		menuMapViewClose.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE);
		menuMapViewClose.addActionListener(super.actionAdapter);
		menuMapViewSave.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		menuMapViewSave.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE);
		menuMapViewSave.addActionListener(super.actionAdapter);
		menuMapViewSaveAs.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS));
		menuMapViewSaveAs.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS);
		menuMapViewSaveAs.addActionListener(super.actionAdapter);
		menuMapViewAddScheme.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		menuMapViewAddScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME);
		menuMapViewAddScheme.addActionListener(super.actionAdapter);
		menuMapViewRemoveScheme.setText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
		menuMapViewRemoveScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME);
		menuMapViewRemoveScheme.addActionListener(super.actionAdapter);

//		menuReport.setText(LangModelGeneral.getString("label_report"));
		menuReport.setName(MapEditorApplicationModel.ITEM_REPORT);
//		menuReportCreate.setText(LangModelGeneral.getString("label_reportForTemplate"));
		menuReportCreate.setName(MapEditorApplicationModel.ITEM_REPORT_CREATE);
		menuReportCreate.addActionListener(super.actionAdapter);
		menuReport.add(menuReportCreate);

		menuView.add(menuViewGeneral);
		menuView.add(menuViewAdditional);
		menuView.add(menuViewCharacteristics);
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
		menuMap.add(menuMapAddMap);
		menuMap.add(menuMapRemoveMap);
		menuMap.addSeparator();
		menuMap.add(menuMapAddExternal);
		menuMap.addSeparator();
		menuMap.add(menuMapExport);
		menuMap.add(menuMapImport);

		menuMapView.add(menuMapViewNew);
		menuMapView.add(menuMapViewOpen);
		menuMapView.addSeparator();
		menuMapView.add(menuMapViewSave);
		menuMapView.add(menuMapViewSaveAs);
		menuMapView.add(menuMapViewClose);
		menuMapView.addSeparator();
		menuMapView.add(menuMapViewAddScheme);
		menuMapView.add(menuMapViewRemoveScheme);

		add(menuMap);
		add(menuMapView);
		add(menuView);
		add(menuReport);    

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(String elementName) {
				this.modelChanged();
			}

			public void modelChanged(String[] elementNames) {
				this.modelChanged();
			}

			private void modelChanged() {
				menuView.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_VIEW));
				menuView.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW));
		
				menuViewGeneral.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_GENERAL));
				menuViewGeneral.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_GENERAL));
		
				menuViewAdditional.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL));
				menuViewAdditional.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL));
		
				menuViewCharacteristics.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS));
				menuViewCharacteristics.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS));
		
				menuViewSetup.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));
				menuViewSetup.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));
		
				menuViewMap.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_MAP));
				menuViewMap.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP));
		
				menuViewMapScheme.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR));
				menuViewMapScheme.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR));
		
				menuViewAll.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_ALL));
				menuViewAll.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_ALL));
		
		//		menuViewOptions.setVisible(aModel.isVisible("menuViewOptions"));
		//		menuViewOptions.setEnabled(aModel.isEnabled("menuViewOptions"));
		
				menuMap.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP));
				menuMap.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP));
		
				menuMapNew.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_NEW));
				menuMapNew.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_NEW));
		
				menuMapOpen.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_OPEN));
				menuMapOpen.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN));
		
				menuMapClose.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_CLOSE));
				menuMapClose.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE));
		
				menuMapSave.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_SAVE));
				menuMapSave.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE));
		
				menuMapSaveAs.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_SAVE_AS));
				menuMapSaveAs.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS));
		
				menuMapAddMap.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
				menuMapAddMap.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		
				menuMapRemoveMap.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
				menuMapRemoveMap.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		
				menuMapAddExternal.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
				menuMapAddExternal.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		
				menuMapExport.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_EXPORT));
				menuMapExport.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT));
		
				menuMapImport.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_IMPORT));
				menuMapImport.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_IMPORT));
		
				menuMapView.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW));
				menuMapView.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW));
		
				menuMapViewNew.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
				menuMapViewNew.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		
				menuMapViewOpen.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
				menuMapViewOpen.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		
				menuMapViewClose.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE));
				menuMapViewClose.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE));
		
				menuMapViewSave.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
				menuMapViewSave.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		
				menuMapViewSaveAs.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS));
				menuMapViewSaveAs.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS));
		
				menuMapViewAddScheme.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
				menuMapViewAddScheme.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		
				menuMapViewRemoveScheme.setVisible(MapEditorMenuBar.this.applicationModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
				menuMapViewRemoveScheme.setEnabled(MapEditorMenuBar.this.applicationModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
			}
		});

	}

}
