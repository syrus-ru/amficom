/**
 * $Id: MapEditorMainFrame.java,v 1.44 2005/06/14 12:07:14 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.editor;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorCloseMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorCloseViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorNewMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorNewViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorOpenMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorOpenViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveMapAsCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveViewAsCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewAdditionalPropertiesCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewCharacteristicsCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewGeneralPropertiesCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapAllCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapControlsCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapViewNavigatorCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapWindowCommand;
import com.syrus.AMFICOM.client.map.command.map.CreateMapReportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapAddExternalNodeCommand;
import com.syrus.AMFICOM.client.map.command.map.MapAddMapCommand;
import com.syrus.AMFICOM.client.map.command.map.MapExportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapImportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapRemoveMapCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewAddSchemeCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewRemoveSchemeCommand;
import com.syrus.AMFICOM.client.map.ui.MapAdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.map.ui.MapCharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapGeneralPropertiesFrame;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.CloseAllInternalCommand;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.SchemeSampleData;

/**
 * Основное окно модуля Редактор топологической схемы
 * 
 * 
 * 
 * @version $Revision: 1.44 $, $Date: 2005/06/14 12:07:14 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public final class MapEditorMainFrame extends AbstractMainFrame {
	protected static String iniFileName = "Map.properties";

	private MapFrame mapFrame = null;
	
	public MapEditorMainFrame(ApplicationContext aContext)
	{
		super(
			aContext, 
			LangModelMap.getString("Map"), 
			new MapEditorMenuBar(aContext.getApplicationModel()), 
			new MapEditorToolBar());
			
		super.setWindowArranger(new MapEditorWindowArranger(this.desktopPane));

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		this.addComponentListener(new ComponentAdapter()
			{
				public void componentShown(ComponentEvent e)
				{
					thisComponentShown(e);
				}
			});

		this.desktopPane.setLayout(null);
	}

	protected void setDefaultModel (ApplicationModel aModel) {
		super.setDefaultModel(aModel);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, true);
	}

	public void disposeModule() {
		super.disposeModule();
		if(getMapFrame() != null)
			getMapFrame().saveConfig();
		setContext(null);
	}

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

		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, 
				new MapViewAddSchemeCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, 
				new MapViewRemoveSchemeCommand(
						this.desktopPane, 
						this.aContext));

		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW, 
				new MapEditorNewViewCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN, 
				new MapEditorOpenViewCommand(
						this.desktopPane, 
						this.aContext));
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
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_CONTROLS, 
				new ViewMapControlsCommand(
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
          
		CreateMapReportCommand rc = new CreateMapReportCommand(this.aContext);
		aModel.setCommand(MapEditorApplicationModel.ITEM_REPORT_CREATE, rc);

//		aModel.setCommand("menuReportOpen", new CreateMapReportCommand(this.aContext));

//		if (ClientSessionEnvironment.getInstance().sessionEstablished()) {
//            this.internalDispatcher.firePropertyChange(new ContextChangeEvent(
//					this,
//					ContextChangeEvent.SESSION_OPENED_EVENT), true);
//		}
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_FRAME_SHOWN, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_VIEW_SELECTED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_VIEW_CLOSED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEditorWindowArranger.EVENT_ARRANGE, this);				
				this.statusBar.removeDispatcher(this.aContext.getDispatcher());
			}

		if(aContext != null)
		{
			super.setContext(aContext);

			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_FRAME_SHOWN, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_SELECTED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_CLOSED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEditorWindowArranger.EVENT_ARRANGE, this);			
		}
	}

	public void propertyChange(PropertyChangeEvent pce)
	{
		if (		(pce.getPropertyName().equals(MapEditorWindowArranger.EVENT_ARRANGE))
				&& 	(pce.getSource() instanceof JDesktopPane)
				&&	(pce.getSource().equals(this.desktopPane)))
		{
			this.windowArranger.arrange();
		}
		if(pce.getPropertyName().equals(MapEvent.MAP_FRAME_SHOWN))
		{
			this.mapFrame = (MapFrame)pce.getSource();
		 }
		else
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_SELECTED))
		{
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT, true);

			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, true);

			aModel.fireModelChanged();
			setTitle(LangModelMap.getString("MapView") + ": " + ((MapView )pce.getSource()).getName());
		}
		else
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_CLOSED))
		{
			for(int i = 0; i < this.desktopPane.getComponents().length; i++)
			{
				Component comp = this.desktopPane.getComponent(i);
				if (comp instanceof MapFrame)
				{
					((MapFrame)comp).setVisible(false);
					((MapFrame)comp).setContext(null);
				}
				else if (comp instanceof MapGeneralPropertiesFrame)
					((MapGeneralPropertiesFrame)comp).setVisible(false);
				else if (comp instanceof MapAdditionalPropertiesFrame)
					((MapAdditionalPropertiesFrame)comp).setVisible(false);
				else if (comp instanceof MapCharacteristicPropertiesFrame)
					((MapCharacteristicPropertiesFrame)comp).setVisible(false);
			}
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT, false);

			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, false);

			aModel.fireModelChanged();
			setTitle(LangModelMap.getString("MapView"));
		}
	}

	public void setConnectionClosed() {
		super.setConnectionClosed();
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT, false);

		aModel.fireModelChanged();
	}

	public void setConnectionFailed() {
		super.setConnectionFailed();
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT, false);

		aModel.fireModelChanged();
	}

	public void setDomainSelected() {
		super.setDomainSelected();
		new CloseAllInternalCommand(this.desktopPane).execute();

		new ViewMapAllCommand(
				this.desktopPane,
				this.aContext,
				new MapMapEditorApplicationModelFactory()).execute();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT, true);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_IMPORT, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN, true);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_PROPERTIES, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_GENERAL, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_CONTROLS, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ALL, true);
    
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT_CREATE, true);

		aModel.fireModelChanged();

		try {
			SchemeSampleData.populate(
					LoginManager.getUserId(),
					LoginManager.getDomainId());
		} catch(DatabaseException e) {
			e.printStackTrace();
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
		} catch(IdentifierGenerationException e) {
			e.printStackTrace();
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
	}

	public void setSessionClosed() {
		ApplicationModel aModel = this.aContext.getApplicationModel();

		setDefaultModel(aModel);

		aModel.fireModelChanged();

		new CloseAllInternalCommand(this.desktopPane).execute();
	}

	void thisComponentShown(ComponentEvent e) {
//		initModule();
		this.desktopPane.setPreferredSize(this.desktopPane.getSize());
	}

	private MapFrame getMapFrame()
	{
		return this.mapFrame;
	}

}
