/**
 * $Id: CreateCablePathCommandAtomic.java,v 1.6 2004/12/24 15:42:11 krupenn Exp $
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
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 * создание пути рпокладки кабеля, внесение его в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/24 15:42:11 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateCablePathCommandAtomic extends MapActionCommand
{
	/** кабельный путь */
	CablePath cp;
	
	/** кабель */
	SchemeCableLink scl;
	
	/** начальный узел */
	AbstractNode startNode;
	
	/** конечный узел */
	AbstractNode endNode;
	
	public CreateCablePathCommandAtomic(
			SchemeCableLink scl,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.scl = scl;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public CablePath getPath()
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
		
		try
		{
			cp = new CablePath(
					scl,
					IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE),
					startNode, 
					endNode, 
					logicalNetLayer.getMapView());
	
			logicalNetLayer.getMapView().addCablePath(cp);
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
		}
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

