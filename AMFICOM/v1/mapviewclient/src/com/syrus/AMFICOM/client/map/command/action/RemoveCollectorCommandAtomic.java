/*-
 * $$Id: RemoveCollectorCommandAtomic.java,v 1.21 2006/04/14 12:04:07 arseniy Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.util.Log;

/**
 * удаление коллектора из карты - атомарное действие
 * 
 * @version $Revision: 1.21 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
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
		Log.debugMessage("remove collector " //$NON-NLS-1$
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
		Log.errorMessage(NOT_IMPLEMENTED);

//		Этот метод всё равно не работает
//		try {
//			StorableObjectPool.putStorableObject(this.collector);
//			this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
//		} catch(IllegalObjectEntityException e) {
//			Log.errorMessage(e);
//		}
	}
}
