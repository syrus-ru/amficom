/**
 * $Id: MapElementStateChangeCommand.java,v 1.12 2005/08/17 14:14:16 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.util.Log;

/**
 * атомарная команда изменения состояния элемента карты 
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.12 $, $Date: 2005/08/17 14:14:16 $
 * @module mapviewclient
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
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
		this.me.revert(this.finalState);
		setResult(Command.RESULT_OK);
	}
	
	@Override
	public void redo()
	{
		this.me.revert(this.finalState);
	}
	
	@Override
	public void undo()
	{
		this.me.revert(this.initialState);
	}
}
