/*
 * $Id: ApplicationContext.java,v 1.4 2005/05/05 11:04:47 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;

/**
 * @author $Author: bob $
 * @version $Revision: 1.4 $, $Date: 2005/05/05 11:04:47 $
 * @module generalclient_v1
 */
public final class ApplicationContext
{
	
	private ApplicationModel applicationModel;
	
	private Dispatcher dispatcher;


	public void setApplicationModel(ApplicationModel applicationModel)
	{
		this.applicationModel = applicationModel;
	}


	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public ApplicationModel getApplicationModel()
	{
		return applicationModel;
	}

	public Dispatcher getDispatcher()
	{
		return dispatcher;
	}
}
