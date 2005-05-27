/*
 * $Id: MapEditorOpenMapCommand.java,v 1.17 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapOpenCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapGeneralPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTreeFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс MapEditorOpenMapCommand используется для открытия топологической схемы в модуле
 * "Редактор топологических схем". Вызывается команда MapOpenCommand, и если 
 * пользователь выбрал MapContext, открывается окно карты и сопутствующие окна
 * и MapContext передается в окно карты
 * 
 * @version $Revision: 1.17 $, $Date: 2005/05/27 15:14:55 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapOpenCommand
 */
public class MapEditorOpenMapCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	MapFrame mapFrame = null;

	MapGeneralPropertiesFrame propFrame = null;

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

	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(this.mapFrame != null) {
			if(!this.mapFrame.checkCanCloseMap())
				return;
			if(!this.mapFrame.checkCanCloseMapView())
				return;
		}

		MapOpenCommand mapOpenCommand = new MapOpenCommand(
				this.desktop,
				this.aContext);
		// в модуле редактирования топологических схем у пользователя есть
		// возможность удалять MapContext в окне управления схемами
		mapOpenCommand.setCanDelete(true);
		mapOpenCommand.execute();

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

			MapViewNewCommand cmd = new MapViewNewCommand(
					this.map,
					this.aContext);
			cmd.execute();

			this.mapView = cmd.getMapView();

			try {
				this.mapView.setCenter(this.mapFrame.getMapViewer()
						.getLogicalNetLayer().getCenter());
				this.mapView.setScale(this.mapFrame.getMapViewer()
						.getLogicalNetLayer().getScale());
				this.mapFrame.setMapView(this.mapView);
				ViewGeneralPropertiesCommand propCommand = new ViewGeneralPropertiesCommand(
						this.desktop,
						this.aContext);
				propCommand.execute();
				this.propFrame = propCommand.frame;
				ViewMapViewNavigatorCommand elementsCommand = new ViewMapViewNavigatorCommand(
						this.desktop,
						this.aContext);
				elementsCommand.execute();
				this.treeFrame = elementsCommand.treeFrame;
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public MapFrame getMapFrame() {
		return this.mapFrame;
	}

	public MapGeneralPropertiesFrame getPropertiesFrame() {
		return this.propFrame;
	}

	public MapViewTreeFrame getTreeFrame() {
		return this.treeFrame;
	}

}
