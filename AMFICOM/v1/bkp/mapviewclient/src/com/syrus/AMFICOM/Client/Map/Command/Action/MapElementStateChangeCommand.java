/**
 * $Id: MapElementStateChangeCommand.java,v 1.5 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;

/**
 * атомарная команда изменения состояния элемента карты 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public final class MapElementStateChangeCommand extends MapActionCommand
{
	MapElement me;
	MapElementState initialState;
	MapElementState finalState;
	
	public MapElementStateChangeCommand(MapElement me, MapElementState initialState, MapElementState finalState)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.me = me;
		this.initialState = initialState;
		this.finalState = finalState;
	}
	
	public MapElement getElement()
	{
		return this.me;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		this.me.revert(this.finalState);
	}
	
	public void redo()
	{
		this.me.revert(this.finalState);
	}
	
	public void undo()
	{
		this.me.revert(this.initialState);
	}
}
