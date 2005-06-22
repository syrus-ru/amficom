/**
 * $Id: MapModeCommand.java,v 1.17 2005/06/22 08:43:48 krupenn Exp $ 
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный 
 * Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import java.util.Iterator;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.mapview.MeasurementPath;

/**
 * Команда переключения режима работы с картой - режимы фрагмента, линии, пути
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/06/22 08:43:48 $
 * @module mapviewclient_v1
 */
public class MapModeCommand extends MapNavigateCommand {
	String modeString;

	int mode;

	public MapModeCommand(
			ApplicationModel aModel, 
			NetMapViewer netMapViewer,
			String modeString,
			int mode) {
		super(aModel, netMapViewer);
		this.modeString = modeString;
		this.mode = mode;
	}

	public void execute() {
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		if(this.aModel.isEnabled(this.modeString)) {
			{
				this.aModel.setSelected(
						MapApplicationModel.MODE_NODE_LINK,
						false);
				this.aModel.setSelected(MapApplicationModel.MODE_LINK, false);
				this.aModel.setSelected(
						MapApplicationModel.MODE_CABLE_PATH,
						false);
				this.aModel.setSelected(MapApplicationModel.MODE_PATH, false);

				this.aModel.setSelected(this.modeString, true);

				if(this.modeString.equals(MapApplicationModel.MODE_PATH)) {
					for(Iterator it = logicalNetLayer.getMapView().getMeasurementPaths().iterator(); it.hasNext();) {
						MeasurementPath mpath = (MeasurementPath )it.next();
						mpath.sortPathElements();
					}
				}

				this.aModel.fireModelChanged();

				logicalNetLayer.getMapState().setShowMode(this.mode);
				try {
					this.netMapViewer.repaint(false);
				} catch(MapConnectionException e) {
					setException(e);
					setResult(Command.RESULT_NO);
					e.printStackTrace();
				} catch(MapDataException e) {
					setException(e);
					setResult(Command.RESULT_NO);
					e.printStackTrace();
				}
			}
		}
	}
}
