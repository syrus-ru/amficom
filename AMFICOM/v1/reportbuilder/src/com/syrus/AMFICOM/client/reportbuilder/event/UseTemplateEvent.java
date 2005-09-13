/*
 * $Id: UseTemplateEvent.java,v 1.1 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.event;

import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * Используется в редакторе шаблонов отчётов при создании нового шаблона
 * отчёта.
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/09/13 12:23:11 $
 * @module reportbuilder_v1
 */
public class UseTemplateEvent extends ReportEvent{
	public UseTemplateEvent(Object source, ReportTemplate reportTemplate){
		super(source,TYPE,reportTemplate,null);
	}
	
	public ReportTemplate getReportTemplate(){
		return (ReportTemplate)this.getOldValue();
	}
}
