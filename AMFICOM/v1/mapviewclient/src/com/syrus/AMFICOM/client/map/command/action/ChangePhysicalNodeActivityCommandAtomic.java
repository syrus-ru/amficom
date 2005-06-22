/**
 * $Id: ChangePhysicalNodeActivityCommandAtomic.java,v 1.10 2005/06/22 08:43:46 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.util.Log;

/**
 * Изменение активности топологического узла - атомарное действие
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/06/22 08:43:46 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

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

