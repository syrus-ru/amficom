/*
 * $Id: DRComponentMovedEvent.java,v 1.1 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.event;

import com.syrus.AMFICOM.client.report.DataRenderingComponent;

/**
 * ������������ � ��������� �������� ������� ��� ������ �������
 * ����������� ������ - ����� ����������� ������� ������������� ������
 * � ��������� ��������. ��������� ImageRenderingComponent ���������
 * DataRenderingComponent, ��������� ���������� � ��� ����, ���� ���
 * �������!XXX
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/09/03 12:42:20 $
 * @module reportbuilder_v1
 */
public class DRComponentMovedEvent extends ReportEvent{
	public DRComponentMovedEvent(Object source, DataRenderingComponent drComponent){
		super(source,TYPE,drComponent,null);
	}
	
	public DataRenderingComponent getDRComponentMoved(){
		return (DataRenderingComponent)this.getOldValue();
	}
}
