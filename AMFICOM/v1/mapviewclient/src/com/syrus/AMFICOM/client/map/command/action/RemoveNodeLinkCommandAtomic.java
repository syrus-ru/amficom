/**
 * $Id: RemoveNodeLinkCommandAtomic.java,v 1.9 2005/06/22 08:43:47 krupenn Exp $
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
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.util.Log;

/**
 * удаление фрагмента линии связи из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/06/22 08:43:47 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

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

