/**
 * $Id: MeasureDistanceCommand.java,v 1.11 2005/08/17 14:14:19 arseniy Exp $
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
 * Команда включения/выключения масштабирования по выбранной области
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/08/17 14:14:19 $
 * @module mapviewclient
 */
public class MeasureDistanceCommand extends MapNavigateCommand {
	public MeasureDistanceCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	@Override
	public void execute() {
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		if(this.aModel
				.isSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE)) {
			logicalNetLayer.getMapState().setActionMode(
					MapState.NULL_ACTION_MODE);
			logicalNetLayer.getMapState().setOperationMode(
					MapState.NO_OPERATION);
			this.netMapViewer.setCursor(Cursor.getDefaultCursor());

			this.aModel.setSelected(
					MapApplicationModel.OPERATION_MEASURE_DISTANCE,
					false);
			this.aModel.fireModelChanged();
		}
		else
			if(!this.aModel
					.isSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE)) {
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MEASURE_DISTANCE,
						true);

				this.aModel.setSelected(
						MapApplicationModel.OPERATION_ZOOM_BOX,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MOVE_TO_CENTER,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_ZOOM_TO_POINT,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_HAND_PAN,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MOVE_FIXED,
						false);
				this.aModel.fireModelChanged();

				logicalNetLayer.getMapState().setOperationMode(
						MapState.MEASURE_DISTANCE);
				this.netMapViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
	}
}
