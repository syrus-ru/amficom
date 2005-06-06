/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.15 2005/06/06 12:20:30 krupenn Exp $
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
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * создание прокладки измерительного пути 
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/06/06 12:20:30 $
 * @module mapviewclient_v1
 */
public class CreateMeasurementPathCommandAtomic extends MapActionCommand
{
	/** создаваемый измерительный путь */
	MeasurementPath measurementPath;
	
	/** схемный путь */
	SchemePath schemePath;
	
	/** начальный узел */
	AbstractNode startNode;
	
	/** конечный узел */
	AbstractNode endNode;
	
	public CreateMeasurementPathCommandAtomic(
			SchemePath schemePath,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemePath = schemePath;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public MeasurementPath getPath()
	{
		return this.measurementPath;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		this.measurementPath = MeasurementPath.createInstance(
				this.schemePath,
				this.startNode, 
				this.endNode,
				this.logicalNetLayer.getMapView());

		this.logicalNetLayer.getMapView().addMeasurementPath(this.measurementPath);
		setResult(Command.RESULT_OK);
	}
}

