/*-
 * $$Id: CreateCollectorCommandAtomic.java,v 1.23 2005/10/30 15:20:31 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.util.Log;

/**
 * создание коллектора, внесение его в пул и на карту - 
 * атомарное действие
 *  
 * @version $Revision: 1.23 $, $Date: 2005/10/30 15:20:31 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateCollectorCommandAtomic extends MapActionCommand {
	/** коллектор */
	Collector collector;

	/** название */
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
		assert Log.debugMessage(
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
			assert Log.debugMessage(e, Level.SEVERE);
		}
	}

	@Override
	public void redo() {
		try {
			StorableObjectPool.putStorableObject(this.collector);
			this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removeCollector(
				this.collector);
		StorableObjectPool.delete(this.collector.getId());
	}
}

