/**
 * $Id: ChangePhysicalNodeActivityCommandAtomic.java,v 1.4 2004/12/22 16:38:39 krupenn Exp $
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
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * Изменение активности топологического узла - атомарное действие
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/22 16:38:39 $
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
	
	public ChangePhysicalNodeActivityCommandAtomic(
			TopologicalNode mpne, 
			boolean active)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = mpne;
		this.active = active;
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
		node.setActive(active);
	}
	
	public void redo()
	{
		execute();
	}
	
	public void undo()
	{
		node.setActive(!active);
	}
}

