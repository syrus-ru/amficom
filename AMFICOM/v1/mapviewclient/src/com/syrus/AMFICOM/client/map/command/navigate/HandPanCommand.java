/**
 * $Id: HandPanCommand.java,v 1.7 2005/02/08 15:11:10 krupenn Exp $
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
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/08 15:11:10 $
 * @module mpviewclient_v1
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
			this.logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			this.aModel = (ApplicationModel )value;
	}

	public void execute()
	{
		if(this.aModel.isSelected(MapApplicationModel.OPERATION_HAND_PAN))
		{
			this.logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
			this.logicalNetLayer.getMapState().setOperationMode(MapState.NO_OPERATION);
			this.logicalNetLayer.setCursor(Cursor.getDefaultCursor());

			this.aModel.setSelected(MapApplicationModel.OPERATION_HAND_PAN, false);
			this.aModel.fireModelChanged();
		}
		else
		if(!this.aModel.isSelected(MapApplicationModel.OPERATION_HAND_PAN))
		{
			this.aModel.setSelected(MapApplicationModel.OPERATION_HAND_PAN, true);

			this.aModel.setSelected(MapApplicationModel.OPERATION_MEASURE_DISTANCE, false);
			this.aModel.setSelected(MapApplicationModel.OPERATION_MOVE_TO_CENTER, false);
			this.aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_TO_POINT, false);
			this.aModel.setSelected(MapApplicationModel.OPERATION_ZOOM_BOX, false);
			this.aModel.setSelected(MapApplicationModel.OPERATION_MOVE_FIXED, false);
			this.aModel.fireModelChanged();

			this.logicalNetLayer.getMapState().setOperationMode(MapState.MOVE_HAND);
			this.logicalNetLayer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}
}
