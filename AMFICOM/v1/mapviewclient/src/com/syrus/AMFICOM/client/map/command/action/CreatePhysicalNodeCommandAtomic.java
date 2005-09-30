/*-
 * $$Id: CreatePhysicalNodeCommandAtomic.java,v 1.29 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * �������� ��������������� ����, �������� ��� � ��� � �� ����� - ���������
 * ��������
 * 
 * @version $Revision: 1.29 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreatePhysicalNodeCommandAtomic extends MapActionCommand {
	/** ����������� �������������� ���� */
	TopologicalNode node;

	/** �������������� ���������� ���� */
	DoublePoint point;

	PhysicalLink physicalLink;

	public CreatePhysicalNodeCommandAtomic(
			PhysicalLink physicalLink,
			DoublePoint point) {
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.point = point;
		this.physicalLink = physicalLink;
	}

	public TopologicalNode getNode() {
		return this.node;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "create topological node on link " //$NON-NLS-1$
				+ this.physicalLink.getName() 
				+ " (" + this.physicalLink.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		try {
			this.node = TopologicalNode.createInstance(
					LoginManager.getUserId(),
					LangModelMap.getString(MapEditorResourceKeys.NONAME),
					MapEditorResourceKeys.EMPTY_STRING,
					this.point);

			TopologicalNodeController tnc = (TopologicalNodeController) getLogicalNetLayer()
					.getMapViewController().getController(this.node);

			// �� ��������� ������������� ���� �� �������
			tnc.setActive(this.node, false);
			// ���������� ����������� ��� ��������������� �����������
			// � ������������ � ������� ��������� ����������� �����
			tnc.updateScaleCoefficient(this.node);

			this.logicalNetLayer.getMapView().getMap().addNode(this.node);
			setResult(Command.RESULT_OK);
		} catch(CreateObjectException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().getMap().addNode(this.node);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
	}
}
