/**
 * $Id: HandPanCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
 * Команда переключения режима сдвика карты "лапкой".
 * основное действие - изменение модели приложения и объекта MapState
 * в логическом сетевом слое карты
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class HandPanCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public HandPanCommand(LogicalNetLayer logicalNetLayer)
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
		return new HandPanCommand(logicalNetLayer);
	}

	public void execute()
	{
		if(aModel.isSelected("mapActionHandPan"))
		{
			logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
			logicalNetLayer.getMapState().setOperationMode(MapState.NO_OPERATION);
			logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			aModel.setSelected("mapActionHandPan", false);
			aModel.fireModelChanged("");
		}
		else
		if(!aModel.isSelected("mapActionHandPan"))
		{
			aModel.setSelected("mapActionHandPan", true);

			aModel.setSelected("mapActionMoveToCenter", false);
			aModel.setSelected("mapActionZoomToPoint", false);
			aModel.setSelected("mapActionZoomBox", false);
			aModel.fireModelChanged("");

			logicalNetLayer.getMapState().setOperationMode(MapState.MOVE_HAND);
			logicalNetLayer.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}
}
