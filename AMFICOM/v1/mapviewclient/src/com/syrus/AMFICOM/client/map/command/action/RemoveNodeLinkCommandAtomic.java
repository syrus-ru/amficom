/**
 * $Id: RemoveNodeLinkCommandAtomic.java,v 1.7 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.NodeLink;

/**
 * удаление фрагмента линии связи из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class RemoveNodeLinkCommandAtomic extends MapActionCommand
{
	NodeLink nodeLink;
	
	public RemoveNodeLinkCommandAtomic(NodeLink nodeLink)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.nodeLink = nodeLink;
	}
	
	public NodeLink getNodeLink()
	{
		return this.nodeLink;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		this.logicalNetLayer.getMapView().getMap().removeNodeLink(this.nodeLink);
		setResult(Command.RESULT_OK);
	}
	
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().removeNodeLink(this.nodeLink);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().addNodeLink(this.nodeLink);
	}
}

