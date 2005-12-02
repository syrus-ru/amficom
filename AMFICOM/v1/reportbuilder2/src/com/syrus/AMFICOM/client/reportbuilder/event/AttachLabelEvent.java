/*
 * $Id: AttachLabelEvent.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.event;

import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.report.TextAttachingType;

/**
 * Используется в редакторе шаблонов отчётов в процессе привязки надписи
 * к объекту.
 * @author $Author: bass $
 * @version $Revision: 1.1.1.1 $, $Date: 2005/12/02 11:37:17 $
 * @module reportbuilder_v1
 */
public class AttachLabelEvent extends ReportEvent{
	public AttachLabelEvent(
			Object source,
			AttachedTextComponent textComponent,
			TextAttachingType attachingType){
		super(source,TYPE,textComponent,attachingType);
	}
	
	public AttachedTextComponent getTextComponentToAttach(){
		return (AttachedTextComponent)this.getOldValue();
	}
	public TextAttachingType getTextAttachingType(){
		return (TextAttachingType)this.getNewValue();
	}
}
