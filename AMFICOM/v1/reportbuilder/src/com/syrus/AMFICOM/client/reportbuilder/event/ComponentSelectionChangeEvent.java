/*
 * $Id: ComponentSelectionChangeEvent.java,v 1.1 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.event;

import com.syrus.AMFICOM.client.report.RenderingComponent;

/**
 * ������������ � ��������� �������� ������� ��� �������� �������
 * � ��������� �������.
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/09/03 12:42:20 $
 * @module reportbuilder_v1
 */
public class ComponentSelectionChangeEvent extends ReportEvent{
	public ComponentSelectionChangeEvent(
			Object source,
			RenderingComponent renderingComponent){
		super(source,TYPE,renderingComponent,null);
	}
	
	public RenderingComponent getRenderingComponent(){
		return (RenderingComponent)this.getOldValue();
	}
}
