/*-
 * $$Id: MapEditorMainFrame.java,v 1.69 2005/10/31 15:29:31 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.editor;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.UIDefaults;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorCloseLibraryCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorCloseMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorCloseViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorNewLibraryCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorNewLinkTypeCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorNewMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorNewSiteTypeCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorNewViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorOpenLibraryCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorOpenMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorOpenViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorRemoveLibraryCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorRemoveLinkTypeCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorRemoveSiteTypeCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveLibraryAsCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveLibraryCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveMapAsCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveViewAsCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewAdditionalPropertiesCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewCharacteristicsCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewGeneralPropertiesCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapAllCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapChooserCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapLayersCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapViewNavigatorCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapWindowCommand;
import com.syrus.AMFICOM.client.map.command.map.CreateMapReportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapAddExternalNodeCommand;
import com.syrus.AMFICOM.client.map.command.map.MapAddMapCommand;
import com.syrus.AMFICOM.client.map.command.map.MapExportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapImportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapLibraryExportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapLibraryImportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapRemoveMapCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewAddSchemeCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewRemoveSchemeCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.CloseAllInternalCommand;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Основное окно модуля Редактор топологической схемы
 * 
 * @version $Revision: 1.69 $, $Date: 2005/10/31 15:29:31 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapEditorMainFrame extends AbstractMainFrame {
	private static final long serialVersionUID = 3420855179151985089L;

	private MapFrame mapFrame = null;
	
	UIDefaults frames;

	public MapEditorMainFrame(ApplicationContext aContext) {
		super(
			aContext, 
			I18N.getString(MapEditorResourceKeys.TITLE_MAP_EDITOR),
			new MapEditorMenuBar(aContext.getApplicationModel()), 
			new MapEditorToolBar());
			
		super.setWindowArranger(new MapEditorWindowArranger(this.desktopPane));

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentShown(ComponentEvent e) {
					thisComponentShown(e);
				}
			});

		this.desktopPane.setLayout(null);
	}

	@Override
	protected void setDefaultModel (ApplicationModel aModel) {
		super.setDefaultModel(aModel);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, true);
	}

	@Override
	public void disposeModule() {
		if(getMapFrame() != null)
			getMapFrame().saveConfig();
		setContext(null);
		super.disposeModule();
	}

	@Override
	protected void initModule() {
		super.initModule();

		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_NEW, 
				new MapEditorNewMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_OPEN, 
				new MapEditorOpenMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_CLOSE, 
				new MapEditorCloseMapCommand(
						this.desktopPane, 
						this.dispatcher));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_SAVE, 
				new MapEditorSaveMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, 
				new MapEditorSaveMapAsCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_ADD_MAP, 
				new MapAddMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP, 
				new MapRemoveMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL, 
				new MapAddExternalNodeCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_EXPORT, 
				new MapExportCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_IMPORT, 
				new MapImportCommand(
						this.desktopPane, 
						this.aContext));

		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW, 
				new MapEditorNewViewCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN, 
				new MapEditorOpenViewCommand(
						this.desktopPane, 
						this.aContext,
						new MapMapEditorApplicationModelFactory()));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, 
				new MapEditorCloseViewCommand(
						this.desktopPane, 
						this.dispatcher));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, 
				new MapEditorSaveViewCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, 
				new MapEditorSaveViewAsCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, 
				new MapViewAddSchemeCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, 
				new MapViewRemoveSchemeCommand(
						this.desktopPane, 
						this.aContext));

		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW, 
				new MapEditorNewLibraryCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_OPEN, 
				new MapEditorOpenLibraryCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_CLOSE, 
				new MapEditorCloseLibraryCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE, 
				new MapEditorRemoveLibraryCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE, 
				new MapEditorSaveLibraryCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS, 
				new MapEditorSaveLibraryAsCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_EXPORT, 
				new MapLibraryExportCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_IMPORT, 
				new MapLibraryImportCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE, 
				new MapEditorNewSiteTypeCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE, 
				new MapEditorRemoveSiteTypeCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE, 
				new MapEditorNewLinkTypeCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE, 
				new MapEditorRemoveLinkTypeCommand(
						this.desktopPane, 
						this.aContext));

		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_GENERAL, 
				new ViewGeneralPropertiesCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL, 
				new ViewAdditionalPropertiesCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS, 
				new ViewCharacteristicsCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_MAP_CHOOSER, 
				new ViewMapChooserCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS, 
				new ViewMapLayersCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_MAP, 
				new ViewMapWindowCommand(
						this.desktopPane, 
						this.aContext, 
						new MapMapEditorApplicationModelFactory()));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR, 
				new ViewMapViewNavigatorCommand(
						this.desktopPane, 
						this.aContext ));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_ALL, 
				new ViewMapAllCommand(
						this.desktopPane,
						this.aContext,
						new MapMapEditorApplicationModelFactory()));
          
		aModel.setCommand(MapEditorApplicationModel.ITEM_REPORT_CREATE, 
				new CreateMapReportCommand(this.desktopPane, this.aContext));
	}

	@Override
	public void setContext(ApplicationContext aContext) {
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null) {
				this.aContext.getDispatcher().removePropertyChangeListener(
						MapEvent.MAP_EVENT_TYPE,
						this);
				this.aContext.getDispatcher().removePropertyChangeListener(
						MapEditorWindowArranger.EVENT_ARRANGE,
						this);
				this.statusBar.removeDispatcher(this.aContext.getDispatcher());
			}

		if(aContext != null) {
			super.setContext(aContext);

			aContext.getDispatcher().addPropertyChangeListener(
					MapEvent.MAP_EVENT_TYPE,
					this);
			aContext.getDispatcher().addPropertyChangeListener(
					MapEditorWindowArranger.EVENT_ARRANGE,
					this);
			this.statusBar.addDispatcher(this.aContext.getDispatcher());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		super.propertyChange(pce);
		if ((pce.getPropertyName().equals(MapEditorWindowArranger.EVENT_ARRANGE))
				&&	(pce.getSource().equals(this.desktopPane))) {
			this.windowArranger.arrange();
		}
		if(pce.getPropertyName().equals(MapEvent.MAP_EVENT_TYPE)) {
			MapEvent mapEvent = (MapEvent )pce;
			String mapEventType = mapEvent.getMapEventType();

			if(mapEventType.equals(MapEvent.MAP_FRAME_SHOWN)) {
				this.mapFrame = (MapFrame)pce.getNewValue();
			 }
			else if(mapEventType.equals(MapEvent.MAP_VIEW_SELECTED)) {
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_MAP, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL, true);
	
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, true);

				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_EXPORT, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_IMPORT, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_OPEN, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_CLOSE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE, true);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS, true);
				
				aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT_CREATE, true);

				aModel.fireModelChanged();
				setTitle(I18N.getString(MapEditorResourceKeys.TITLE_MAP_VIEW) + ": " + ((MapView )pce.getNewValue()).getName()); //$NON-NLS-1$
			}
			else if(mapEventType.equals(MapEvent.MAP_VIEW_CLOSED)) {
				for(int i = 0; i < this.desktopPane.getComponents().length; i++) {
					Component comp = this.desktopPane.getComponent(i);
					if(comp instanceof MapFrame) {
						((MapFrame) comp).setVisible(false);
//						((MapFrame) comp).setContext(null);
					} else if(comp instanceof GeneralPropertiesFrame) {
						((GeneralPropertiesFrame) comp).setVisible(false);
					} else if(comp instanceof AdditionalPropertiesFrame) {
						((AdditionalPropertiesFrame) comp).setVisible(false);
					} else if(comp instanceof CharacteristicPropertiesFrame) {
						((CharacteristicPropertiesFrame) comp).setVisible(false);
					}
				}
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_MAP, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP, false);
	
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, false);
	
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_EXPORT, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_IMPORT, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_OPEN, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_CLOSE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE, false);
				aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS, false);
				
				aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT_CREATE, false);

				aModel.fireModelChanged();
				setTitle(I18N.getString(MapEditorResourceKeys.TITLE_MAP_VIEW));
			}
		}
	}

	void thisComponentShown(@SuppressWarnings("unused") ComponentEvent e) {
//		initModule();
		this.desktopPane.setPreferredSize(this.desktopPane.getSize());
	}

	private MapFrame getMapFrame() {
		return this.mapFrame;
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			if (this.mapFrame != null && this.mapFrame.checkChangesPresent()) {
				return;
			}
		}
		super.processWindowEvent(e);
	}

	@Override
	public void loggedIn() {
		new CloseAllInternalCommand(this.desktopPane).execute();

		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_ENTER)) {
			return;
		}
		
		new ViewMapAllCommand(
				this.desktopPane,
				this.aContext,
				new MapMapEditorApplicationModelFactory()).execute();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT, true);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_IMPORT, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL, true);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_EXPORT, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_IMPORT, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_LINK_TYPE, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_NEW_SITE_TYPE, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_OPEN, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_CLOSE, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_LINK_TYPE, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_REMOVE_SITE_TYPE, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS, true);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_PROPERTIES, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_GENERAL, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP_CHOOSER, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ALL, true);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT_CREATE, true);

		aModel.fireModelChanged();

//		try {
//			SchemeSampleData.populate(
//					LoginManager.getUserId(),
//					LoginManager.getDomainId());
//		} catch(DatabaseException e) {
//			e.printStackTrace();
//		} catch(IllegalObjectEntityException e) {
//			e.printStackTrace();
//		} catch(IdentifierGenerationException e) {
//			e.printStackTrace();
//		} catch(ApplicationException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void loggedOut() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		setDefaultModel(aModel);
		aModel.fireModelChanged();
		new CloseAllInternalCommand(this.desktopPane).execute();
	}
}
