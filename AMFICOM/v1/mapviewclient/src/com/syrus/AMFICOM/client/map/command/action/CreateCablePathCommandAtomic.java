/**
 * $Id: CreateCablePathCommandAtomic.java,v 1.16 2005/06/06 12:20:29 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * создание пути рпокладки кабеля, внесение его в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/06/06 12:20:29 $
 * @module mapviewclient_v1
 */
public class CreateCablePathCommandAtomic extends MapActionCommand
{
	/** кабельный путь */
	CablePath cablePath;
	
	/** кабель */
	SchemeCableLink schemeCableLink;
	
	/** начальный узел */
	AbstractNode startNode;
	
	/** конечный узел */
	AbstractNode endNode;
	
	public CreateCablePathCommandAtomic(
			SchemeCableLink schemeCableLink,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemeCableLink = schemeCableLink;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public CablePath getPath()
	{
		return this.cablePath;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		this.cablePath = com.syrus.AMFICOM.mapview.CablePath.createInstance(
				this.schemeCableLink,
				this.startNode, 
				this.endNode);

		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
		setResult(Command.RESULT_OK);
	}
	
	public void redo()
	{
		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
	}
}

