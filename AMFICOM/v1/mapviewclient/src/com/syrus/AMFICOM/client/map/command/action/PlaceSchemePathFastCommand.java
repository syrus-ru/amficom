/*-
 * $Id: PlaceSchemePathFastCommand.java,v 1.1 2006/06/23 13:55:27 stas Exp $
 *
 * Copyright њ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class PlaceSchemePathFastCommand extends MapActionCommandBundle {
	SiteNode startNode = null;
	SiteNode endNode = null;

	MeasurementPath measurementPath = null;

	SchemePath schemePath = null;

	public PlaceSchemePathFastCommand(SchemePath path, SiteNode startNode, SiteNode endNode) {
		super();
		this.schemePath = path;
		this.startNode = startNode;
		this.endNode = endNode;
	}

	static long t0 = 0;
	@Override
	public void execute() {
		try {
			this.measurementPath = super.createMeasurementPath(this.schemePath, 
					this.startNode, this.endNode);

			// сама€ долга€ операци€, остальные по времени = 0
			this.measurementPath.sortPathElements();
			
			super.setUndoable(false);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			Log.errorMessage(e);
		}
	}
}
