/*-
 * $$Id: RemoveCollectorCommandAtomic.java,v 1.16 2005/10/21 16:51:35 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.util.Log;

/**
 * удаление коллектора из карты - атомарное действие
 * 
 * @version $Revision: 1.16 $, $Date: 2005/10/21 16:51:35 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "remove collector " //$NON-NLS-1$
					+ this.collector.getName()
					+ " (" + this.collector.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		this.logicalNetLayer.getMapView().getMap().removeCollector(
				this.collector);
		StorableObjectPool.delete(this.collector.getId());
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().getMap().removeCollector(
				this.collector);
		StorableObjectPool.delete(this.collector.getId());
	}

	@Override
	public void undo() {
		try {
			StorableObjectPool.putStorableObject(this.collector);
			this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
		}
	}
}
