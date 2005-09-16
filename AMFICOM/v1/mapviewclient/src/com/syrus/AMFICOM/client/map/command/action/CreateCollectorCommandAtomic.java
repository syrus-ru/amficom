/**
 * $Id: CreateCollectorCommandAtomic.java,v 1.18 2005/09/16 14:53:32 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.util.Log;

/**
 * �������� ����������, �������� ��� � ��� � �� ����� - 
 * ��������� �������� 
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/09/16 14:53:32 $
 * @module mapviewclient
 */
public class CreateCollectorCommandAtomic extends MapActionCommand {
	/** ��������� */
	Collector collector;

	/** �������� */
	String name;

	public CreateCollectorCommandAtomic(String name) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.name = name;
	}

	public Collector getCollector() {
		return this.collector;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | "  //$NON-NLS-1$
				+ "create collector " + this.name,  //$NON-NLS-1$
			Level.FINEST);
		
		try {
			this.collector = Collector.createInstance(
					LoginManager.getUserId(),
					this.logicalNetLayer.getMapView().getMap(),
					this.name,
					""); //$NON-NLS-1$
			this.logicalNetLayer.getMapView().getMap().addCollector(
					this.collector);
			setResult(Command.RESULT_OK);
		} catch(CreateObjectException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removeCollector(
				this.collector);
	}
}

