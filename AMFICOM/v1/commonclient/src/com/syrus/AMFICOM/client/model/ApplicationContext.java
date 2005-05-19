/*
 * $Id: ApplicationContext.java,v 1.1 2005/05/19 14:06:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.Dispatcher;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:41 $
 * @module commonclient_v1
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
