/*
 * $Id: ReportEvent.java,v 1.4 2005/09/01 14:21:40 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.event;

import java.beans.PropertyChangeEvent;

/**
 * Используется в редакторе шаблонов отчётов для передачи событий типа
 * извещение, где даже не важно откуда пришло событие.
 * @author $Author: peskovsky $
 * @version $Revision: 1.4 $, $Date: 2005/09/01 14:21:40 $
 * @module reportbuilder_v1
 */
public class ReportEvent extends PropertyChangeEvent{
	public static final String TYPE = ReportEvent.class.getName();
	
	public static final String LABEL_CREATION_STARTED = "labelCreationStarted";
	public static final String LABEL_CREATION_CANCELED = "labelCreationCanceled";	
	public static final String IMAGE_CREATION_STARTED = "imageCreationStarted";
	public static final String IMAGE_CREATION_CANCELED = "imageCreationCanceled";	
	
	public ReportEvent(Object source, String eventType){
		super(source,TYPE,eventType,null);
	}
	
	public String getEventType(){
		return (String)this.getOldValue();
	}
}
