/**
 * $Id: ChangePhysicalNodeActivityCommandAtomic.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;

/**
 * Изменение активности топологического узла - атомарное действие
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
class ChangePhysicalNodeActivityCommandAtomic extends MapActionCommand
{
	MapPhysicalNodeElement node;
	boolean active;
	
	public ChangePhysicalNodeActivityCommandAtomic(MapPhysicalNodeElement mpne, boolean active)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = mpne;
		this.active = active;
	}
	
	public MapPhysicalNodeElement getNode()
	{
		return node;
	}
	
	public void execute()
	{
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

