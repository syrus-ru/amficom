/**
 * $Id: MapModeCommand.java,v 1.21 2005/09/14 10:34:35 krupenn Exp $ 
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ ������������������� 
 * ���������������� �������� ���������� �����������
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
 * ������� ������������ ������ ������ � ������ - ������ ���������, �����, ����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.21 $, $Date: 2005/09/14 10:34:35 $
 * @module mapviewclient
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

	@Override
	public void execute() {
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		if(this.aModel.isEnabled(this.modeString)) {
			{
				this.aModel.setSelected(MapApplicationModel.MODE_NODE_LINK, false);
				this.aModel.setSelected(MapApplicationModel.MODE_LINK, false);
				this.aModel.setSelected(MapApplicationModel.MODE_CABLE_PATH, false);
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
					this.netMapViewer.getLogicalNetLayer().calculateVisualElements();
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
