/**
 * $Id: ChangePhysicalNodeActivityCommandAtomic.java,v 1.8 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.TopologicalNodeController;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * ��������� ���������� ��������������� ���� - ��������� ��������
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class ChangePhysicalNodeActivityCommandAtomic extends MapActionCommand
{
	/** ���� */
	TopologicalNode node;
	
	/**
	 * ����� ��������� ���������� ����
	 */
	boolean active;

	TopologicalNodeController controller;
	
	public ChangePhysicalNodeActivityCommandAtomic(
			TopologicalNode mpne, 
			boolean active)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = mpne;
		this.active = active;
	}
	
	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		super.setLogicalNetLayer(lnl);

		this.controller = (TopologicalNodeController )
			lnl.getMapViewController().getController(this.node);
	}
	
	public TopologicalNode getNode()
	{
		return this.node;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		this.controller.setActive(this.node, this.active);
		setResult(Command.RESULT_OK);
	}
	
	public void redo()
	{
		execute();
	}
	
	public void undo()
	{
		this.controller.setActive(this.node, !this.active);
	}
}
