/**
 * $Id: HandPanCommand.java,v 1.5 2004/11/16 17:31:17 krupenn Exp $
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
 * ������� ������������ ������ ������ ����� "������".
 * �������� �������� - ��������� ������ ���������� � ������� MapState
 * � ���������� ������� ���� �����
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/11/16 17:31:17 $
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
		if(aModel.isSelected(MapApplicationModel.OPERATION_HAND_PAN))
		{
			logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
			logicalNetLayer.getMapState().setOperationMode(MapState.NO_OPERATION);
			logicalNetLayer.setCursor(Cursor.getDefaultCursor());

			aModel.setSelected(MapApplicationModel.OPERATION_HAND_PAN, false);
			aModel.fireModelChanged();
		}
		else
		if(!aModel.isSelected(MapApplicationModel.OPERATION_HAND_PAN))
		{
			aModel.setSelected(MapApplicationModel.OPERATION_HAND_PAN, true);

			aModel.setSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE, false);
			aModel.setSelected(MapApplicationModel.OPERATION_MOVE_TO_CENTER, false);
			aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT, false);
			aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_BOX, false);
			aModel.setSelected(MapApplicationModel.OPERATION_MOVE_FIXED, false);
			aModel.fireModelChanged();

			logicalNetLayer.getMapState().setOperationMode(MapState.MOVE_HAND);
			logicalNetLayer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}
}
