/*
 * $Id: ReportEvent.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.1.1.1 $, $Date: 2005/12/02 11:37:17 $
 * @module reportbuilder_v1
 */
public abstract class ReportEvent extends PropertyChangeEvent{
	public static final String TYPE = ReportEvent.class.getName();
	
	public ReportEvent(Object source, String propertyName, Object oldValue, Object newValue) {
		super(source, propertyName, oldValue, newValue);
		// Empty
	}
}
