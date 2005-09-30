/*-
 * $$Id: MapNavigateCommand.java,v 1.6 2005/09/30 16:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationModel;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class MapNavigateCommand extends AbstractCommand {

	protected ApplicationModel aModel;

	protected Throwable exception = null;

	protected NetMapViewer netMapViewer;

	public MapNavigateCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.aModel = aModel;
	}

	public Throwable getException() {
		return this.exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
}
