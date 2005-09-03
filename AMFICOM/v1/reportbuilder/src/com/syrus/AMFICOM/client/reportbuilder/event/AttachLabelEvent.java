/*
 * $Id: AttachLabelEvent.java,v 1.1 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.event;

import com.syrus.AMFICOM.client.report.AttachedTextComponent;

/**
 * Используется в редакторе шаблонов отчётов в процессе привязки надписи
 * к объекту.
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/09/03 12:42:20 $
 * @module reportbuilder_v1
 */
public class AttachLabelEvent extends ReportEvent{
	public AttachLabelEvent(
			Object source,
			AttachedTextComponent textComponent,
			String attachingType){
		super(source,TYPE,textComponent,attachingType);
	}
	
	public AttachedTextComponent getTextComponentToAttach(){
		return (AttachedTextComponent)this.getOldValue();
	}
	public String getTextAttachingType(){
		return (String)this.getNewValue();
	}
}
