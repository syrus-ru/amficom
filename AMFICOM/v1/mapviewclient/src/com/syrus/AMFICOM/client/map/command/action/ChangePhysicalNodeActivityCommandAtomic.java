/**
 * $Id: ChangePhysicalNodeActivityCommandAtomic.java,v 1.2 2004/09/21 14:59:20 krupenn Exp $
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
 * @version $Revision: 1.2 $, $Date: 2004/09/21 14:59:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ChangePhysicalNodeActivityCommandAtomic extends MapActionCommand
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

