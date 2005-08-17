/**
 * $Id: HandPanCommand.java,v 1.12 2005/08/17 14:14:18 arseniy Exp $ 
 * Syrus Systems 
 * ������-����������� ����� 
 * ������: ������� ������������������ ������������������� 
 * ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import java.awt.Cursor;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModel;

/**
 * ������� ������������ ������ ������ ����� "������". �������� �������� -
 * ��������� ������ ���������� � ������� MapState � ���������� ������� ����
 * �����
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.12 $, $Date: 2005/08/17 14:14:18 $
 * @module mpviewclient_v1
 */
public class HandPanCommand extends MapNavigateCommand {
	public HandPanCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	@Override
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
