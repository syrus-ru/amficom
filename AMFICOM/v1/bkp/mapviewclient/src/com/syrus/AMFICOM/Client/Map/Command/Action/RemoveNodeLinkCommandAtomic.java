/**
 * $Id: RemoveNodeLinkCommandAtomic.java,v 1.6 2005/03/01 15:37:25 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.NodeLink;

/**
 * удаление фрагмента линии связи из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/03/01 15:37:25 $
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

