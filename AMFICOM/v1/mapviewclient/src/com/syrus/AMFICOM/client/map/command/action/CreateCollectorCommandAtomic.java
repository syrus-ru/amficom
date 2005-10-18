/*-
 * $$Id: CreateCollectorCommandAtomic.java,v 1.20 2005/10/18 07:21:12 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
 *  
 * @version $Revision: 1.20 $, $Date: 2005/10/18 07:21:12 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
			Log.debugException(e, Level.SEVERE);
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

