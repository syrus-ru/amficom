/**
 * $Id: MapActionCommand.java,v 1.4 2005/02/08 15:11:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;

/**
 *  
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class MapActionCommand extends VoidCommand
{
	protected static final int ACTION_NONE = 0;
	protected static final int ACTION_DRAW_NODE = 1;
	protected static final int ACTION_DRAW_LINE = 2;
	protected static final int ACTION_MOVE_SELECTION = 3;
	protected static final int ACTION_DELETE_SELECTION = 4;
	protected static final int ACTION_DROP_NODE = 5;
	protected static final int ACTION_DROP_LINE = 6;
	
	protected int action = ACTION_NONE;
	protected MapElement element = null;
	protected MapElementState elementState = null;

	LogicalNetLayer logicalNetLayer = null;
	ApplicationContext aContext = null;
	
	public MapActionCommand(int action)
	{
		this.action = action;
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}
	
	protected ApplicationContext getContext()
	{
		return this.aContext;
	}
	
	protected LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			this.logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationContext"))
			this.aContext = (ApplicationContext )value;
	}
}
