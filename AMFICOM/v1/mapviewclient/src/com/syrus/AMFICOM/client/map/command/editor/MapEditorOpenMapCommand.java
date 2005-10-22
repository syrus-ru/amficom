/*-
 * $$Id: MapEditorOpenMapCommand.java,v 1.32 2005/10/22 15:00:35 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapNewCommand;
import com.syrus.AMFICOM.client.map.command.map.MapOpenCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewNewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapViewTreeFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс MapEditorOpenMapCommand используется для открытия топологической схемы в модуле
 * "Редактор топологических схем". Вызывается команда MapOpenCommand, и если 
 * пользователь выбрал MapContext, открывается окно карты и сопутствующие окна
 * и MapContext передается в окно карты
 * 
 * @version $Revision: 1.32 $, $Date: 2005/10/22 15:00:35 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 * @see MapOpenCommand
 */
public class MapEditorOpenMapCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	MapFrame mapFrame = null;

	GeneralPropertiesFrame propFrame = null;

	MapViewTreeFrame treeFrame = null;

	Map map = null;

	MapView mapView = null;

	/**
	 * @param desktop куда класть окно карты
	 * @param aContext Контекст модуля "Редактор топологических схем"
	 */
	public MapEditorOpenMapCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(this.mapFrame != null && this.mapFrame.isVisible()) {
			if(this.mapFrame.checkChangesPresent()) {
				return;
			}
		}

		MapOpenCommand mapOpenCommand = new MapOpenCommand(
				this.desktop,
				this.aContext);
		// в модуле редактирования топологических схем у пользователя есть
		// возможность удалять MapContext в окне управления схемами
		mapOpenCommand.setCanDelete(true);
		mapOpenCommand.execute();

		if(this.mapFrame != null) {
			Map openedMap = this.mapFrame.getMap();
			try {
				if(StorableObjectPool.getStorableObject(openedMap.getId(), false) == null
						&& mapOpenCommand.getResult() != Command.RESULT_OK) {
					// current mapFrame map was deleted
					MapNewCommand cmd = new MapNewCommand(this.mapFrame.getContext());
					cmd.execute();
					Map emptyMap = cmd.getMap();
					MapViewNewCommand cmd2 = new MapViewNewCommand(emptyMap, this.mapFrame.getContext());
					cmd2.execute();
					MapView emptyMapView = cmd2.getMapView();
					this.mapFrame.setMapView(emptyMapView);
				}
			} catch(ApplicationException e) {
				e.printStackTrace();
			} catch(MapException e) {
				e.printStackTrace();
			}
		}
		
		if(mapOpenCommand.getResult() == Command.RESULT_OK) {
			this.map = mapOpenCommand.getMap();

			if(this.mapFrame == null) {
				ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(
						this.desktop,
						this.aContext,
						new MapMapEditorApplicationModelFactory());

				mapCommand.execute();
				this.mapFrame = mapCommand.mapFrame;
			}

			if(this.mapFrame == null)
				return;

			this.mapFrame.setVisible(true);
			
			MapViewNewCommand cmd = new MapViewNewCommand(
					this.map,
					this.aContext);
			cmd.execute();

			this.mapView = cmd.getMapView();

			try {
				this.mapView.setCenter(this.mapFrame.getMapViewer()
						.getLogicalNetLayer().getMapContext().getCenter());
				this.mapView.setScale(this.mapFrame.getMapViewer()
						.getLogicalNetLayer().getMapContext().getScale());
				this.mapFrame.setMapView(this.mapView);
				ViewGeneralPropertiesCommand propCommand = new ViewGeneralPropertiesCommand(
						this.desktop,
						this.aContext);
				propCommand.execute();
				this.propFrame = propCommand.frame;
				new ViewAdditionalPropertiesCommand(this.desktop, this.aContext).execute();
				new ViewCharacteristicsCommand(this.desktop, this.aContext).execute();
				ViewMapViewNavigatorCommand elementsCommand = new ViewMapViewNavigatorCommand(
						this.desktop,
						this.aContext);
				elementsCommand.execute();
				this.treeFrame = elementsCommand.treeFrame;
			} catch(MapException e) {
				this.mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
				e.printStackTrace();
			}
		}
	}

	public MapFrame getMapFrame() {
		return this.mapFrame;
	}

	public GeneralPropertiesFrame getPropertiesFrame() {
		return this.propFrame;
	}

	public MapViewTreeFrame getTreeFrame() {
		return this.treeFrame;
	}

}
