/**
 * $Id: ChangePhysicalNodeActivityCommandAtomic.java,v 1.9 2005/06/06 12:20:29 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * Изменение активности топологического узла - атомарное действие
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/06/06 12:20:29 $
 * @module mapviewclient_v1
 */
public class ChangePhysicalNodeActivityCommandAtomic extends MapActionCommand
{
	/** узел */
	TopologicalNode node;
	
	/**
	 * новое состояние активности узла
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

