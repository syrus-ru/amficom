/*
 * $Id: ReportSendEventCommand.java,v 1.2 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.templatescheme;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;

public class ReportSendEventCommand extends AbstractCommand {
	ApplicationContext aContext;
	String eventType = null;

	public ReportSendEventCommand(ApplicationContext aContext,String eventType){
		this.aContext = aContext;
		this.eventType = eventType;
	}

	@Override
	public void execute() {
		if (this.eventType == null){
			this.result = RESULT_CANCEL;
			return;
		}
		
		this.aContext.getDispatcher().firePropertyChange(new ReportFlagEvent(this,this.eventType));
		this.result = RESULT_OK;
	}
}

