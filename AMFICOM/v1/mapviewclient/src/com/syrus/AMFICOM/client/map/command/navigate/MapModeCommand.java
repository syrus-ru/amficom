/*-
 * $$Id: MapModeCommand.java,v 1.24 2005/10/18 07:21:12 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.mapview.MeasurementPath;

/**
 * ������� ������������ ������ ������ � ������ - ������ ���������, �����, ����
 * 
 * @version $Revision: 1.24 $, $Date: 2005/10/18 07:21:12 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
						try {
							mpath.sortPathElements();
						} catch(ApplicationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
				} catch(ApplicationException e) {
					setException(e);
					setResult(Command.RESULT_NO);
					e.printStackTrace();
				}
			}
		}
	}
}
