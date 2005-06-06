/*
 * $Id: MapEditorOpenViewCommand.java,v 1.21 2005/06/06 12:57:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewOpenCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс MapEditorOpenViewCommand используется для открытия топологической схемы в модуле
 * "Редактор топологических схем". Вызывается команда MapOpenCommand, и если 
 * пользователь выбрал MapContext, открывается окно карты и сопутствующие окна
 * и MapContext передается в окно карты
 * 
 * @version $Revision: 1.21 $, $Date: 2005/06/06 12:57:01 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see com.syrus.AMFICOM.client.map.command.map.MapOpenCommand
 */
public class MapEditorOpenViewCommand extends AbstractCommand {
	protected ApplicationContext aContext;

	protected JDesktopPane desktop;

	protected MapFrame mapFrame = null;

	protected MapView mapView = null;

	/**
	 * в модуле редактирования топологических схем у пользователя есть
	 * возможность удалять MapContext в окне управления схемами
	 */
	private boolean canDelete = true;

	private boolean checkSave = true;

	/**
	 * @param desktop куда класть окно карты
	 * @param aContext Контекст модуля "Редактор топологических схем"
	 */
	public MapEditorOpenViewCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(this.mapFrame != null) {
			if(isCheckSave()) {
				if(!this.mapFrame.checkCanCloseMap())
					return;
				if(!this.mapFrame.checkCanCloseMapView())
					return;
			}
		}

		MapViewOpenCommand moc = new MapViewOpenCommand(
				this.desktop,
				this.aContext);
		moc.setCanDelete(isCanDelete());
		moc.execute();

		if(moc.getResult() == Command.RESULT_OK) {
			this.mapView = moc.getMapView();

			this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
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

			try {
				this.mapFrame.setMapView(this.mapView);
				ViewGeneralPropertiesCommand propCommand = new ViewGeneralPropertiesCommand(
						this.desktop,
						this.aContext);
				propCommand.execute();
				ViewAdditionalPropertiesCommand addCommand = new ViewAdditionalPropertiesCommand(
						this.desktop,
						this.aContext);
				addCommand.execute();
			} catch(MapConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(MapDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public boolean isCanDelete() {
		return this.canDelete;
	}

	public void setCheckSave(boolean checkSave) {
		this.checkSave = checkSave;
	}

	public boolean isCheckSave() {
		return this.checkSave;
	}

}
