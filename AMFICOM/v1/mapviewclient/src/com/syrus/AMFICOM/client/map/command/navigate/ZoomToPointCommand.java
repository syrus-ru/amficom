/**
 * $Id: ZoomToPointCommand.java,v 1.5 2004/11/16 17:31:17 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;

import java.awt.Cursor;

/**
 * ������� ���������/����������� ������ ����������� �����
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/11/16 17:31:17 $
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
		if(aModel.isSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT))
		{
			logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
			logicalNetLayer.getMapState().setOperationMode(MapState.NO_OPERATION);
			logicalNetLayer.setCursor(Cursor.getDefaultCursor());

			aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT, false);
			aModel.fireModelChanged();
		}
		else
		if(!aModel.isSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT))
		{
			aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT, true);

			aModel.setSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE, false);
			aModel.setSelected(MapApplicationModel.OPERATION_MOVE_TO_CENTER, false);
			aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_BOX, false);
			aModel.setSelected(MapApplicationModel.OPERATION_HAND_PAN, false);
			aModel.setSelected(MapApplicationModel.OPERATION_MOVE_FIXED, false);
			aModel.fireModelChanged();

			logicalNetLayer.getMapState().setOperationMode(MapState.ZOOM_TO_POINT);
			logicalNetLayer.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
}