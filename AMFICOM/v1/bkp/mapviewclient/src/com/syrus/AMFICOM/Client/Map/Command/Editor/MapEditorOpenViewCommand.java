/*
 * $Id: MapEditorOpenViewCommand.java,v 1.19 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ???????
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewOpenCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ????? MapEditorOpenViewCommand ???????????? ??? ???????? ?????????????? ????? ? ??????
 * "???????? ?????????????? ????". ?????????? ??????? MapOpenCommand, ? ???? 
 * ???????????? ?????? MapContext, ??????????? ???? ????? ? ????????????? ????
 * ? MapContext ?????????? ? ???? ?????
 * 
 * @version $Revision: 1.19 $, $Date: 2005/05/27 15:14:55 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see com.syrus.AMFICOM.Client.Map.Command.Map.MapOpenCommand
 */
public class MapEditorOpenViewCommand extends AbstractCommand {
	protected ApplicationContext aContext;

	protected JDesktopPane desktop;

	protected MapFrame mapFrame = null;

	protected MapView mapView = null;

	/**
	 * ? ?????? ?????????????? ?????????????? ???? ? ???????????? ????
	 * ??????????? ??????? MapContext ? ???? ?????????? ???????
	 */
	private boolean canDelete = true;

	private boolean checkSave = true;

	/**
	 * @param desktop ???? ?????? ???? ?????
	 * @param aContext ???????? ?????? "???????? ?????????????? ????"
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
