/*
 * $Id: ReportFlagEvent.java,v 1.1 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.event;

/**
 * Используется в редакторе шаблонов отчётов для передачи событий типа
 * извещение, где даже не важно откуда пришло событие.
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/09/03 12:42:20 $
 * @module reportbuilder_v1
 */
public class ReportFlagEvent extends ReportEvent{
	public static final String LABEL_CREATION_STARTED = "labelCreationStarted";
	public static final String LABEL_CREATION_CANCELED = "labelCreationCanceled";	
	public static final String IMAGE_CREATION_STARTED = "imageCreationStarted";
	public static final String IMAGE_CREATION_CANCELED = "imageCreationCanceled";	
	
	public static final String DELETE_OBJECT = "deleteObject";
	
	public static final String REPAINT_RENDERER = "repaintRenderer";
	
	public ReportFlagEvent(Object source, String eventType){
		super(source,TYPE,eventType,null);
	}
	
	public String getEventType(){
		return (String)this.getOldValue();
	}
}
