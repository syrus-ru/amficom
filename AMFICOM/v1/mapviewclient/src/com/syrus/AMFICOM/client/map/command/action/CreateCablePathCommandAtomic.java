/**
 * $Id: CreateCablePathCommandAtomic.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;

/**
 * создание физической линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
class CreateCablePathCommandAtomic extends MapActionCommand
{
	MapCablePathElement cp;
	
	SchemeCableLink scl;
	
	MapNodeElement startNode;
	MapNodeElement endNode;
	
	public CreateCablePathCommandAtomic(
			SchemeCableLink scl,
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.scl = scl;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public MapCablePathElement getPath()
	{
		return cp;
	}
	
	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		
		cp = new MapCablePathElement(
				scl,
				dataSource.GetUId( com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement.typ ),
				startNode, 
				endNode, 
				logicalNetLayer.getMapView());
		Pool.put(
				com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement.typ, 
				cp.getId(), 
				cp);

		logicalNetLayer.getMapView().addCablePath(cp);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().addCablePath(cp);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().removeCablePath(cp);
	}
}

