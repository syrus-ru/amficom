/**
 * $Id: ChangePhysicalNodeActivityCommandAtomic.java,v 1.5 2005/01/12 14:23:19 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.TopologicalNodeController;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * Изменение активности топологического узла - атомарное действие
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/01/12 14:23:19 $
 * @module
 * @author $Author: krupenn $
 * @see
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

		controller = (TopologicalNodeController )
			lnl.getMapViewController().getController(node);
	}
	
	public TopologicalNode getNode()
	{
		return node;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		controller.setActive(node, active);
	}
	
	public void redo()
	{
		execute();
	}
	
	public void undo()
	{
		controller.setActive(node, !active);
	}
}

