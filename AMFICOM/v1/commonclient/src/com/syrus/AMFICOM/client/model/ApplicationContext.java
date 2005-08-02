/*
 * $Id: ApplicationContext.java,v 1.2 2005/08/02 13:03:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.Dispatcher;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/02 13:03:22 $
 * @module commonclient
 */
public final class ApplicationContext {

	private ApplicationModel	applicationModel;

	private Dispatcher			dispatcher;

	public void setApplicationModel(ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public ApplicationModel getApplicationModel() {
		return this.applicationModel;
	}

	public Dispatcher getDispatcher() {
		return this.dispatcher;
	}
}
