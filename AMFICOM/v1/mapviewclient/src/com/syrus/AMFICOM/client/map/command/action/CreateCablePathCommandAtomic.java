/**
 * $Id: CreateCablePathCommandAtomic.java,v 1.22 2005/09/16 14:53:32 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * �������� ���� ��������� ������, �������� ��� � ��� � �� ����� - ���������
 * ��������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.22 $, $Date: 2005/09/16 14:53:32 $
 * @module mapviewclient
 */
public class CreateCablePathCommandAtomic extends MapActionCommand {
	/** ��������� ���� */
	CablePath cablePath;

	/** ������ */
	SchemeCableLink schemeCableLink;

	/** ��������� ���� */
	AbstractNode startNode;

	/** �������� ���� */
	AbstractNode endNode;

	public CreateCablePathCommandAtomic(
			SchemeCableLink schemeCableLink,
			AbstractNode startNode,
			AbstractNode endNode) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemeCableLink = schemeCableLink;
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public CablePath getPath() {
		return this.cablePath;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | "  //$NON-NLS-1$
				+ "create CablePath for SchemeCableLink "  //$NON-NLS-1$
				+ this.schemeCableLink.getName() 
				+ " (" + this.schemeCableLink.getId()  //$NON-NLS-1$
				+ ") with start at node " + this.startNode.getName()  //$NON-NLS-1$
				+ " (" + this.startNode.getId()  //$NON-NLS-1$
				+ ") and end at node " + this.endNode.getName()  //$NON-NLS-1$
				+ " (" + this.endNode.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);
		
		this.cablePath = com.syrus.AMFICOM.mapview.CablePath.createInstance(
				this.schemeCableLink,
				this.startNode,
				this.endNode);

		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
	}
}

