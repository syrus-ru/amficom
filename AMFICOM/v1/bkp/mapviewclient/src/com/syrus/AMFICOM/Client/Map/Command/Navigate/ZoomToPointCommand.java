/**
 * $Id: ZoomToPointCommand.java,v 1.3 2004/10/19 10:41:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;

import java.awt.Cursor;

/**
 * Команда включения/выкллючения режима приближения точки
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 10:41:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ZoomToPointCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public ZoomToPointCommand(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			aModel = (ApplicationModel )value;
	}

	public Object clone()
	{
		return new ZoomToPointCommand(logicalNetLayer);
	}

	public void execute()
	{
		if(aModel.isSelected("mapActionZoomToPoint"))
		{
			logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
			logicalNetLayer.getMapState().setOperationMode(MapState.NO_OPERATION);
			logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			aModel.setSelected("mapActionZoomToPoint", false);
			aModel.fireModelChanged("");
		}
		else
		if(!aModel.isSelected("mapActionZoomToPoint"))
		{
			aModel.setSelected("mapActionZoomToPoint", true);

			aModel.setSelected("mapActionMeasureDistance", false);
			aModel.setSelected("mapActionMoveToCenter", false);
			aModel.setSelected("mapActionZoomBox", false);
			aModel.setSelected("mapActionHandPan", false);
			aModel.fireModelChanged("");

			logicalNetLayer.getMapState().setOperationMode(MapState.ZOOM_TO_POINT);
			logicalNetLayer.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
}