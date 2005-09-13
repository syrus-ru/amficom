/*
 * $Id: ReportQuickViewEvent.java,v 1.1 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.event;

/**
 * Используется в редакторе шаблонов отчётов для быстрого просмотра
 * отчёта-элемента дерева.
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/09/13 12:23:11 $
 * @module reportbuilder_v1
 */
public class ReportQuickViewEvent extends ReportEvent{
	public ReportQuickViewEvent(Object source, Object reportObject){
		super(source,TYPE,reportObject,null);
	}
	
	public Object getReportObject(){
		return this.getOldValue();
	}
}
