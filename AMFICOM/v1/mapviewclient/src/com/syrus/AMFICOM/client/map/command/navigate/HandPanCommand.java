/**
 * $Id: HandPanCommand.java,v 1.11 2005/06/16 10:57:20 krupenn Exp $ 
 * Syrus Systems 
 * Научно-технический центр 
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный 
 * Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import java.awt.Cursor;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModel;

/**
 * Команда переключения режима сдвика карты "лапкой". основное действие -
 * изменение модели приложения и объекта MapState в логическом сетевом слое
 * карты
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/06/16 10:57:20 $
 * @module mpviewclient_v1
 */
public class HandPanCommand extends MapNavigateCommand {
	public HandPanCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	public void execute() {
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		if(this.aModel.isSelected(MapApplicationModel.OPERATION_HAND_PAN)) {
			logicalNetLayer.getMapState().setActionMode(
					MapState.NULL_ACTION_MODE);
			logicalNetLayer.getMapState().setOperationMode(
					MapState.NO_OPERATION);
			this.netMapViewer.setCursor(Cursor.getDefaultCursor());

			this.aModel.setSelected(
					MapApplicationModel.OPERATION_HAND_PAN,
					false);
			this.aModel.fireModelChanged();
		}
		else
			if(!this.aModel.isSelected(MapApplicationModel.OPERATION_HAND_PAN)) {
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_HAND_PAN,
						true);

				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MEASURE_DISTANCE,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MOVE_TO_CENTER,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_ZOOM_TO_POINT,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_ZOOM_BOX,
						false);
				this.aModel.setSelected(
						MapApplicationModel.OPERATION_MOVE_FIXED,
						false);
				this.aModel.fireModelChanged();

				logicalNetLayer.getMapState().setOperationMode(
						MapState.MOVE_HAND);
				this.netMapViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
	}
}
