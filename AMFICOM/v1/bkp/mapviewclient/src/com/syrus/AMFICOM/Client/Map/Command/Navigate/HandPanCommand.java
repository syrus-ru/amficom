/**
 * $Id: HandPanCommand.java,v 1.9 2005/05/27 15:14:56 krupenn Exp $ 
 * Syrus Systems 
 * ������-����������� ����� 
 * ������: ������� ������������������ ������������������� 
 * ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;

/**
 * ������� ������������ ������ ������ ����� "������". �������� �������� -
 * ��������� ������ ���������� � ������� MapState � ���������� ������� ����
 * �����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/05/27 15:14:56 $
 * @module mpviewclient_v1
 */
public class HandPanCommand extends MapNavigateCommand {
	public HandPanCommand(LogicalNetLayer logicalNetLayer) {
		super(logicalNetLayer);
	}

	public void execute() {
		if(this.aModel.isSelected(MapApplicationModel.OPERATION_HAND_PAN)) {
			this.logicalNetLayer.getMapState().setActionMode(
					MapState.NULL_ACTION_MODE);
			this.logicalNetLayer.getMapState().setOperationMode(
					MapState.NO_OPERATION);
			this.logicalNetLayer.setCursor(Cursor.getDefaultCursor());

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

				this.logicalNetLayer.getMapState().setOperationMode(
						MapState.MOVE_HAND);
				this.logicalNetLayer.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
	}
}