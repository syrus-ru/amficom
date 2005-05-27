/**
 * $Id: MeasureDistanceCommand.java,v 1.7 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;

/**
 * Команда включения/выключения масштабирования по выбранной области
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/05/27 15:14:56 $
 * @module mapviewclient_v1
 */
public class MeasureDistanceCommand extends MapNavigateCommand {
	public MeasureDistanceCommand(LogicalNetLayer logicalNetLayer) {
		super(logicalNetLayer);
	}

	public void execute() {
		if(this.aModel
				.isSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE)) {
			this.logicalNetLayer.getMapState().setActionMode(
					MapState.NULL_ACTION_MODE);
			this.logicalNetLayer.getMapState().setOperationMode(
					MapState.NO_OPERATION);
			this.logicalNetLayer.setCursor(Cursor.getDefaultCursor());

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

				this.logicalNetLayer.getMapState().setOperationMode(
						MapState.MEASURE_DISTANCE);
				this.logicalNetLayer.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
	}
}
