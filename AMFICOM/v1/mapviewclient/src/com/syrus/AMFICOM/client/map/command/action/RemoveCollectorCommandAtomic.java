/**
 * $Id: RemoveCollectorCommandAtomic.java,v 1.13 2005/08/26 15:39:54 krupenn Exp $
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
import com.syrus.AMFICOM.map.Collector;
import com.syrus.util.Log;

/**
 * �������� ���������� �� ����� - ��������� ��������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclient
 */
public class RemoveCollectorCommandAtomic extends MapActionCommand {
	Collector collector;

	public RemoveCollectorCommandAtomic(Collector collector) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.collector = collector;
	}

	public Collector getCollector() {
		return this.collector;
	}

	@Override
	public void execute() {
		Log.debugMessage(
				getClass().getName() + "::execute() | "
					+ "remove collector "
					+ this.collector.getName()
					+ " (" + this.collector.getId() + ")", 
				Level.FINEST);

		this.logicalNetLayer.getMapView().getMap().removeCollector(
				this.collector);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().getMap().removeCollector(
				this.collector);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
	}
}
