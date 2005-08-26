/**
 * $Id: RemoveMeasurementPathCommandAtomic.java,v 1.18 2005/08/26 15:39:54 krupenn Exp $
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
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.util.Log;

/**
 * �������� �������������� ���� �� ����� - ��������� ��������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclient
 */
public class RemoveMeasurementPathCommandAtomic extends MapActionCommand {
	MeasurementPath measuremetnPath;

	public RemoveMeasurementPathCommandAtomic(MeasurementPath mp) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.measuremetnPath = mp;
	}

	public MeasurementPath getPath() {
		return this.measuremetnPath;
	}

	@Override
	public void execute() {
		Log.debugMessage(
				getClass().getName() + "::execute() | "
					+ "remove measurement path "
					+ this.measuremetnPath.getName()
					+ " (" + this.measuremetnPath.getId() + ")", 
				Level.FINEST);

		this.logicalNetLayer.getMapView().removeMeasurementPath(
				this.measuremetnPath);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().removeMeasurementPath(
				this.measuremetnPath);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().addMeasurementPath(
				this.measuremetnPath);
	}
}
