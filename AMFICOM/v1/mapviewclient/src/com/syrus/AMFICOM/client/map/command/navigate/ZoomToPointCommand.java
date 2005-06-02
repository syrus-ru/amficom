/**
 * $Id: ZoomToPointCommand.java,v 1.9 2005/02/18 12:19:45 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;

/**
 * Команда включения/выкллючения режима приближения точки
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/02/18 12:19:45 $
 * @module mapviewclient_v1
 */
public class ZoomToPointCommand extends MapNavigateCommand
{
	public ZoomToPointCommand(LogicalNetLayer logicalNetLayer)
	{
		super(logicalNetLayer);
	}

	public void execute()
	{
		if(this.aModel.isSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT))
		{
			this.logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
			this.logicalNetLayer.getMapState().setOperationMode(MapState.NO_OPERATION);
			this.logicalNetLayer.setCursor(Cursor.getDefaultCursor());

			this.aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT, false);
			this.aModel.fireModelChanged();
		}
		else
		if(!this.aModel.isSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT))
		{
			this.aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT, true);

			this.aModel.setSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE, false);
			this.aModel.setSelected(MapApplicationModel.OPERATION_MOVE_TO_CENTER, false);
			this.aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_BOX, false);
			this.aModel.setSelected(MapApplicationModel.OPERATION_HAND_PAN, false);
			this.aModel.setSelected(MapApplicationModel.OPERATION_MOVE_FIXED, false);
			this.aModel.fireModelChanged();

			this.logicalNetLayer.getMapState().setOperationMode(MapState.ZOOM_TO_POINT);
			this.logicalNetLayer.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
}
