/**
 * $Id: MoveFixedDistanceCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

import java.awt.Point;

/**
 * Команда позволяет перемещать топологический узел вокруг другого
 * топологического узла, связанного с ним фрагментом линии, при сохранении
 * длины фрагмента
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveFixedDistanceCommand extends MoveSelectionCommandBundle
{

	public MoveFixedDistanceCommand(Point point)
	{
		super(point);
	}

	public MoveFixedDistanceCommand(LogicalNetLayer logicalNetLayer)
	{
		super(logicalNetLayer);
	}
}
