/**
 * $Id: RemoveCablePathCommandAtomic.java,v 1.19 2005/09/16 14:53:33 krupenn Exp $
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
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.util.Log;

/**
 * �������� ���������� ���� �� ����� - ��������� ��������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/09/16 14:53:33 $
 * @module mapviewclient
 */
public class RemoveCablePathCommandAtomic extends MapActionCommand {
	CablePath cablePath;

	public RemoveCablePathCommandAtomic(CablePath cp) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.cablePath = cp;
	}

	public CablePath getPath() {
		return this.cablePath;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "remove cable path " //$NON-NLS-1$
				+ this.cablePath.getName()
				+ " (" + this.cablePath.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
	}
}
