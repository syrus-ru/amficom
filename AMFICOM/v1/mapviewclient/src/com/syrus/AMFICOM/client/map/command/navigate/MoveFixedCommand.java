/**
 * $Id: MoveFixedCommand.java,v 1.5 2005/06/06 12:20:31 krupenn Exp $
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
import com.syrus.AMFICOM.client.model.MapApplicationModel;

/**
 * Команда включения/выключения масштабирования по выбранной области
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/06/06 12:20:31 $
 * @module mapviewclient_v1
 */
public class MoveFixedCommand extends MapNavigateCommand {
	public MoveFixedCommand(LogicalNetLayer logicalNetLayer) {
		super(logicalNetLayer);
	}

	public void execute() {
		if(this.aModel.isSelected(MapApplicationModel.OPERATION_MOVE_FIXED)) {
			this.logicalNetLayer.getMapState().setActionMode(
					MapState.NULL_ACTION_MODE);
			this.logicalNetLayer.getMapState().setOperationMode(
					MapState.NO_OPERATION);

			this.logicalNetLayer.setCursor(Cursor.getDefaultCursor());

			this.aModel.setSelected(
					MapApplicationModel.OPERATION_MOVE_FIXED,
					false);
			this.aModel.fireModelChanged();
		}
		else
			if(!this.aModel
					.isSelected(MapApplicationModel.OPERATION_MOVE_FIXED)) {
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MOVE_FIXED,
						true);

				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MEASURE_DISTANCE,
						false);
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
				this.aModel.fireModelChanged();

				this.logicalNetLayer.getMapState().setOperationMode(
						MapState.MOVE_FIXDIST);
				this.logicalNetLayer.setCursor(Cursor
						.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
	}
}
