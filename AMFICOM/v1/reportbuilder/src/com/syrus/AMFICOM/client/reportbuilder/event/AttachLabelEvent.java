/*
 * $Id: AttachLabelEvent.java,v 1.2 2005/10/05 09:39:37 peskovsky Exp $
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
 * @author $Author: peskovsky $
 * @version $Revision: 1.2 $, $Date: 2005/10/05 09:39:37 $
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
