/**
 * $Id: MoveToCenterCommand.java,v 1.11 2005/06/16 10:57:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import java.awt.Cursor;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModel;

/**
 * Командавключения/выключения режима центрирования по точке 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/06/16 10:57:20 $
 * @module mapviewclient_v1
 */
public class MoveToCenterCommand extends MapNavigateCommand {
	public MoveToCenterCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	public void execute() {
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		if(this.aModel.isSelected(MapApplicationModel.OPERATION_MOVE_TO_CENTER)) {
			logicalNetLayer.getMapState().setActionMode(
					MapState.NULL_ACTION_MODE);
			logicalNetLayer.getMapState().setOperationMode(
					MapState.NO_OPERATION);
			this.netMapViewer.setCursor(Cursor.getDefaultCursor());

			this.aModel.setSelected(
					MapApplicationModel.OPERATION_MOVE_TO_CENTER,
					false);
			this.aModel.fireModelChanged();
		}
		else
			if(!this.aModel
					.isSelected(MapApplicationModel.OPERATION_MOVE_TO_CENTER)) {
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MOVE_TO_CENTER,
						true);

				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MEASURE_DISTANCE,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_ZOOM_TO_POINT,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_ZOOM_BOX,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_HAND_PAN,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MOVE_FIXED,
						false);
				this.aModel.fireModelChanged();

				logicalNetLayer.getMapState().setOperationMode(
						MapState.MOVE_TO_CENTER);
				this.netMapViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
	}
}
