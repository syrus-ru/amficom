/**
 * $Id: MapRemoveMapCommand.java,v 1.10 2005/08/19 15:43:32 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.command.map;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ������ �� ���� ��������� ����� 
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/08/19 15:43:32 $
 * @module mapviewclient
 */
public class MapRemoveMapCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	public MapRemoveMapCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null)
			return;

		MapView mapView = mapFrame.getMapView();

		if(mapView == null)
			return;

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapOpening")));

		MapTableController mapTableController = MapTableController.getInstance();

		Map map = (Map )WrapperedTableChooserDialog.showChooserDialog(
				LangModelMap.getString("Map"),
				mapView.getMap().getMaps(),
				mapTableController,
				mapTableController.getKeysArray(),
				true);

		if(map == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		mapView.getMap().removeMap(map);
		mapFrame.getContext().getDispatcher().firePropertyChange(
				new MapEvent(this, MapEvent.MAP_VIEW_CHANGED, mapFrame.getMapView()));
		mapFrame.getContext().getDispatcher().firePropertyChange(
				new MapEvent(this, MapEvent.UPDATE_SELECTION));
		mapFrame.getContext().getDispatcher().firePropertyChange(
				new MapEvent(this, MapEvent.SELECTION_CHANGED, mapFrame.getMapView().getMap().getSelectedElements()));

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

}
