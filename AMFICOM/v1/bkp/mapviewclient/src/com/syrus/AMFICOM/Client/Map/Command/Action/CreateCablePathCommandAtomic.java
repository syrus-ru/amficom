/**
 * $Id: CreateCablePathCommandAtomic.java,v 1.4 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 * создание пути рпокладки кабеля, внесение его в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateCablePathCommandAtomic extends MapActionCommand
{
	/** кабельный путь */
	MapCablePathElement cp;
	
	/** кабель */
	SchemeCableLink scl;
	
	/** начальный узел */
	MapNodeElement startNode;
	
	/** конечный узел */
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		DataSourceInterface dataSource = aContext.getDataSource();
		
		cp = new MapCablePathElement(
				scl,
				dataSource.GetUId( MapCablePathElement.typ ),
				startNode, 
				endNode, 
				logicalNetLayer.getMapView());
		Pool.put(MapCablePathElement.typ, cp.getId(), cp);

		logicalNetLayer.getMapView().addCablePath(cp);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().addCablePath(cp);
		Pool.put(MapCablePathElement.typ, cp.getId(), cp);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().removeCablePath(cp);
		Pool.remove(MapCablePathElement.typ, cp.getId());
	}
}

