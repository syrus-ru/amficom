/*-
 * $$Id: ZoomOutCommand.java,v 1.14 2006/02/15 11:12:44 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.util.Log;

/**
 * Команда "Отдалить вид со стандартным коэффициентом"
 *  
 * @version $Revision: 1.14 $, $Date: 2006/02/15 11:12:44 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ZoomOutCommand extends MapNavigateCommand {
	public ZoomOutCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	@Override
	public void execute() {
		try {
			this.netMapViewer.zoomOut();
		} catch(MapConnectionException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.errorMessage(e);
		} catch(MapDataException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.errorMessage(e);
		}
	}
}
