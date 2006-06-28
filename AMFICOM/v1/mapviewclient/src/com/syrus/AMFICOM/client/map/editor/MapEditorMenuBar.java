/*-
 * $$Id: MapEditorMenuBar.java,v 1.33 2006/06/23 14:09:12 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.editor;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;

/**
 * Панель меню модуля "Редактор топологических схем".
 * 
 * @version $Revision: 1.33 $, $Date: 2006/06/23 14:09:12 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorMenuBar extends AbstractMainMenuBar {

	private static final long serialVersionUID = -314315267016874348L;

	public MapEditorMenuBar(ApplicationModel applicationModel) {
		super(applicationModel);
	}

	@Override
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
		final JMenuItem menuMapViewValidate = new JMenuItem();
	
		final JMenu menuMapLibrary = new JMenu();
		final JMenuItem menuMapLibraryNew = new JMenuItem();
		final JMenuItem menuMapLibraryOpen = new JMenuItem();
		final JMenuItem menuMapLibrarySave = new JMenuItem();
		final JMenuItem menuMapLibrarySaveAs = new JMenuItem();
		final JMenuItem menuMapLibraryClose = new JMenuItem();
		final JMenuItem menuMapLibraryRemove = new JMenuItem();
		final JMenuItem menuMapLibraryExport = new JMenuItem();
		final JMenuItem menuMapLibraryImport = new JMenuItem();
		final JMenuItem menuMapLibraryNewSiteType = new JMenuItem();
		final JMenuItem menuMapLibraryNewLinkType = new JMenuItem();
		final JMenuItem menuMapLibraryRemoveSiteType = new JMenuItem();
		final JMenuItem menuMapLibraryRemoveLinkType = new JMenuItem();
	
		final JMenu menuView = new JMenu();
		final JMenuItem menuViewGeneral = new JMenuItem();
		final JMenuItem menuViewAdditional = new JMenuItem();
		final JMenuItem menuViewCharacteristics = new JMenuItem();
		final JMenuItem menuViewMapChooser = new JMenuItem();
		final JMenuItem menuViewMapLayers = new JMenuItem();
		final JMenuItem menuViewMap = new JMenuItem();
		final JMenuItem menuViewNavigator = new JMenuItem();
		final JMenuItem menuViewAll = new JMenuItem();
	
		final JMenu menuReport = new JMenu();
		final JMenuItem menuReportCreate = new JMenuItem();
	
		menuView.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW));
		menuView.setName(MapEditorApplicationModel.ITEM_VIEW);
		menuViewGeneral.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_GENERAL));
		menuViewGeneral.setName(MapEditorApplicationModel.ITEM_VIEW_GENERAL);
		menuViewGeneral.addActionListener(super.actionAdapter);
		menuViewAdditional.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL));
		menuViewAdditional.setName(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL);
		menuViewAdditional.addActionListener(super.actionAdapter);
		menuViewCharacteristics.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS));
		menuViewCharacteristics.setName(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS);
		menuViewCharacteristics.addActionListener(super.actionAdapter);
		menuViewMapChooser.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_MAP_CHOOSER));
		menuViewMapChooser.setName(MapEditorApplicationModel.ITEM_VIEW_MAP_CHOOSER);
		menuViewMapChooser.addActionListener(super.actionAdapter);
		menuViewMapLayers.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS));
		menuViewMapLayers.setName(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS);
		menuViewMapLayers.addActionListener(super.actionAdapter);
		menuViewMap.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_MAP));
		menuViewMap.setName(MapEditorApplicationModel.ITEM_VIEW_MAP);
		menuViewMap.addActionListener(super.actionAdapter);
		menuViewNavigator.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR));
		menuViewNavigator.setName(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR);
		menuViewNavigator.addActionListener(super.actionAdapter);
		menuViewAll.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_ALL));
		menuViewAll.setName(MapEditorApplicationModel.ITEM_VIEW_ALL);
		menuViewAll.addActionListener(super.actionAdapter);

		menuMap.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP));
		menuMap.setName(MapEditorApplicationModel.ITEM_MAP);
		menuMapNew.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_NEW));
		menuMapNew.setName(MapEditorApplicationModel.ITEM_MAP_NEW);
		menuMapNew.addActionListener(super.actionAdapter);
		menuMapOpen.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_OPEN));
		menuMapOpen.setName(MapEditorApplicationModel.ITEM_MAP_OPEN);
		menuMapOpen.addActionListener(super.actionAdapter);
		menuMapClose.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_CLOSE));
		menuMapClose.setName(MapEditorApplicationModel.ITEM_MAP_CLOSE);
		menuMapClose.addActionListener(super.actionAdapter);
		menuMapSave.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_SAVE));
		menuMapSave.setName(MapEditorApplicationModel.ITEM_MAP_SAVE);
		menuMapSave.addActionListener(super.actionAdapter);
		menuMapSaveAs.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_SAVE_AS));
		menuMapSaveAs.setName(MapEditorApplicationModel.ITEM_MAP_SAVE_AS);
		menuMapSaveAs.addActionListener(super.actionAdapter);
		menuMapAddMap.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		menuMapAddMap.setName(MapEditorApplicationModel.ITEM_MAP_ADD_MAP);
		menuMapAddMap.addActionListener(super.actionAdapter);
		menuMapRemoveMap.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		menuMapRemoveMap.setName(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP);
		menuMapRemoveMap.addActionListener(super.actionAdapter);
		menuMapAddExternal.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		menuMapAddExternal.setName(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL);
		menuMapAddExternal.addActionListener(super.actionAdapter);
		menuMapExport.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_EXPORT));
		menuMapExport.setName(MapEditorApplicationModel.ITEM_MAP_EXPORT);
		menuMapExport.addActionListener(super.actionAdapter);
		menuMapImport.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_IMPORT));
		menuMapImport.setName(MapEditorApplicationModel.ITEM_MAP_IMPORT);
		menuMapImport.addActionListener(super.actionAdapter);

		menuMapView.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW));
		menuMapView.setName(MapEditorApplicationModel.ITEM_MAP_VIEW);
		menuMapViewNew.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		menuMapViewNew.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW);
		menuMapViewNew.addActionListener(super.actionAdapter);
		menuMapViewOpen.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		menuMapViewOpen.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN);
		menuMapViewOpen.addActionListener(super.actionAdapter);
		menuMapViewClose.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE));
		menuMapViewClose.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE);
		menuMapViewClose.addActionListener(super.actionAdapter);
		menuMapViewSave.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		menuMapViewSave.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE);
		menuMapViewSave.addActionListener(super.actionAdapter);
		menuMapViewSaveAs.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS));
		menuMapViewSaveAs.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS);
		menuMapViewSaveAs.addActionListener(super.actionAdapter);
		menuMapViewAddScheme.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		menuMapViewAddScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME);
		menuMapViewAddScheme.addActionListener(super.actionAdapter);
		menuMapViewRemoveScheme.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
		menuMapViewRemoveScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME);
		menuMapViewRemoveScheme.addActionListener(super.actionAdapter);
		menuMapViewValidate.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_VALIDATE));
		menuMapViewValidate.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_VALIDATE);
		menuMapViewValidate.addActionListener(super.actionAdapter);
		
		menuMapLibrary.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY));
		menuMapLibrary.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY);
		menuMapLibraryNew.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW));
		menuMapLibraryNew.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW);
		menuMapLibraryNew.addActionListener(super.actionAdapter);
		menuMapLibraryOpen.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_OPEN));
		menuMapLibraryOpen.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_OPEN);
		menuMapLibraryOpen.addActionListener(super.actionAdapter);
		menuMapLibrarySave.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE));
		menuMapLibrarySave.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE);
		menuMapLibrarySave.addActionListener(super.actionAdapter);
		menuMapLibrarySaveAs.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS));
		menuMapLibrarySaveAs.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS);
		menuMapLibrarySaveAs.addActionListener(super.actionAdapter);
		menuMapLibraryClose.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_CLOSE));
		menuMapLibraryClose.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_CLOSE);
		menuMapLibraryClose.addActionListener(super.actionAdapter);
		menuMapLibraryRemove.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE));
		menuMapLibraryRemove.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE);
		menuMapLibraryRemove.addActionListener(super.actionAdapter);
		menuMapLibraryExport.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_EXPORT));
		menuMapLibraryExport.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_EXPORT);
		menuMapLibraryExport.addActionListener(super.actionAdapter);
		menuMapLibraryImport.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_IMPORT));
		menuMapLibraryImport.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_IMPORT);
		menuMapLibraryImport.addActionListener(super.actionAdapter);
		menuMapLibraryNewSiteType.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE));
		menuMapLibraryNewSiteType.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE);
		menuMapLibraryNewSiteType.addActionListener(super.actionAdapter);
		menuMapLibraryNewLinkType.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE));
		menuMapLibraryNewLinkType.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE);
		menuMapLibraryNewLinkType.addActionListener(super.actionAdapter);
		menuMapLibraryRemoveSiteType.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE));
		menuMapLibraryRemoveSiteType.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE);
		menuMapLibraryRemoveSiteType.addActionListener(super.actionAdapter);
		menuMapLibraryRemoveLinkType.setText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE));
		menuMapLibraryRemoveLinkType.setName(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE);
		menuMapLibraryRemoveLinkType.addActionListener(super.actionAdapter);
		
		menuReport.setText(I18N.getString(MapEditorApplicationModel.ITEM_REPORT));
		menuReport.setName(MapEditorApplicationModel.ITEM_REPORT);
		menuReportCreate.setText(I18N.getString(MapEditorApplicationModel.ITEM_REPORT_CREATE));
		menuReportCreate.setName(MapEditorApplicationModel.ITEM_REPORT_CREATE);
		menuReportCreate.addActionListener(super.actionAdapter);
		menuReport.add(menuReportCreate);

		menuView.add(menuViewMapChooser);
		menuView.add(menuViewMapLayers);
		menuView.addSeparator();
		menuView.add(menuViewGeneral);
		menuView.add(menuViewAdditional);
		menuView.add(menuViewCharacteristics);
		menuView.add(menuViewMap);
		menuView.add(menuViewNavigator);
		menuView.addSeparator();
		menuView.add(menuViewAll);

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
		menuMapView.addSeparator();
		menuMapView.add(menuMapViewValidate);
		
		menuMapLibrary.add(menuMapLibraryNew);
		menuMapLibrary.add(menuMapLibraryOpen);
		menuMapLibrary.add(menuMapLibrarySave);
		menuMapLibrary.add(menuMapLibrarySaveAs);
		menuMapLibrary.add(menuMapLibraryClose);
		menuMapLibrary.addSeparator();
		menuMapLibrary.add(menuMapLibraryRemove);
		menuMapLibrary.addSeparator();
		menuMapLibrary.add(menuMapLibraryExport);
		menuMapLibrary.add(menuMapLibraryImport);
		menuMapLibrary.addSeparator();
		menuMapLibrary.add(menuMapLibraryNewSiteType);
		menuMapLibrary.add(menuMapLibraryRemoveSiteType);
		menuMapLibrary.addSeparator();
		menuMapLibrary.add(menuMapLibraryNewLinkType);
		menuMapLibrary.add(menuMapLibraryRemoveLinkType);

		add(menuMap);
		add(menuMapView);
		add(menuMapLibrary);
		add(menuReport);    
		add(menuView);

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(String elementName) {
				this.modelChanged();
			}

			public void modelChanged(String[] elementNames) {
				this.modelChanged();
			}

			private void modelChanged() {
				final ApplicationModel aModel = MapEditorMenuBar.this.getApplicationModel();
				menuView.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW));
				menuView.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW));
		
				menuViewGeneral.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_GENERAL));
				menuViewGeneral.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_GENERAL));
		
				menuViewAdditional.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL));
				menuViewAdditional.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL));
		
				menuViewCharacteristics.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS));
				menuViewCharacteristics.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS));
		
				menuViewMapChooser.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_MAP_CHOOSER));
				menuViewMapChooser.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP_CHOOSER));
		
				menuViewMapLayers.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS));
				menuViewMapLayers.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS));
		
				menuViewMap.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_MAP));
				menuViewMap.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP));
		
				menuViewNavigator.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR));
				menuViewNavigator.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR));
		
				menuViewAll.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_ALL));
				menuViewAll.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_ALL));
		
				menuMap.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP));
				menuMap.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP));
		
				menuMapNew.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_NEW));
				menuMapNew.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_NEW));
		
				menuMapOpen.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_OPEN));
				menuMapOpen.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN));
		
				menuMapClose.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_CLOSE));
				menuMapClose.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE));
		
				menuMapSave.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_SAVE));
				menuMapSave.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE));
		
				menuMapSaveAs.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_SAVE_AS));
				menuMapSaveAs.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS));
		
				menuMapAddMap.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
				menuMapAddMap.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		
				menuMapRemoveMap.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
				menuMapRemoveMap.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		
				menuMapAddExternal.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
				menuMapAddExternal.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		
				menuMapExport.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_EXPORT));
				menuMapExport.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT));
		
				menuMapImport.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_IMPORT));
				menuMapImport.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_IMPORT));
		
				menuMapView.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW));
				menuMapView.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW));
		
				menuMapViewNew.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
				menuMapViewNew.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		
				menuMapViewOpen.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
				menuMapViewOpen.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		
				menuMapViewClose.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE));
				menuMapViewClose.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE));
		
				menuMapViewSave.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
				menuMapViewSave.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		
				menuMapViewSaveAs.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS));
				menuMapViewSaveAs.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS));
		
				menuMapViewAddScheme.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
				menuMapViewAddScheme.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		
				menuMapViewRemoveScheme.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
				menuMapViewRemoveScheme.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
				
				menuMapViewValidate.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_VALIDATE));
				menuMapViewValidate.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_VALIDATE));

				menuMapLibrary.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY));
				menuMapLibrary.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY));

				menuMapLibraryNew.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW));
				menuMapLibraryNew.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW));
				
				menuMapLibraryOpen.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_OPEN));
				menuMapLibraryOpen.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_OPEN));
				
				menuMapLibrarySave.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE));
				menuMapLibrarySave.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE));
				
				menuMapLibrarySaveAs.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS));
				menuMapLibrarySaveAs.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS));
				
				menuMapLibraryClose.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_CLOSE));
				menuMapLibraryClose.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_CLOSE));
				
				menuMapLibraryRemove.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE));
				menuMapLibraryRemove.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE));
				
				menuMapLibraryExport.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_EXPORT));
				menuMapLibraryExport.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_EXPORT));
				
				menuMapLibraryImport.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_IMPORT));
				menuMapLibraryImport.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_IMPORT));
				
				menuMapLibraryNewSiteType.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE));
				menuMapLibraryNewSiteType.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE));
				
				menuMapLibraryNewLinkType.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE));
				menuMapLibraryNewLinkType.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE));
				
				menuMapLibraryRemoveSiteType.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE));
				menuMapLibraryRemoveSiteType.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE));
				
				menuMapLibraryRemoveLinkType.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE));
				menuMapLibraryRemoveLinkType.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE));
				
				menuReport.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_REPORT));
				menuReport.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_REPORT));

				menuReportCreate.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_REPORT_CREATE));
				menuReportCreate.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_REPORT_CREATE));
			}
		});

	}

}
