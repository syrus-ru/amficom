/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.21 2005/09/16 14:53:32 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 * создание прокладки измерительного пути
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.21 $, $Date: 2005/09/16 14:53:32 $
 * @module mapviewclient
 */
public class CreateMeasurementPathCommandAtomic extends MapActionCommand {
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
			AbstractNode endNode) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemePath = schemePath;
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public MeasurementPath getPath() {
		return this.measurementPath;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | "  //$NON-NLS-1$
				+ "create MeasurementPath for SchemePath "  //$NON-NLS-1$
				+ this.schemePath.getName() 
				+ " (" + this.schemePath.getId()  //$NON-NLS-1$
				+ ") with start at node " + this.startNode.getName()  //$NON-NLS-1$
				+ " (" + this.startNode.getId()  //$NON-NLS-1$
				+ ") and end at node " + this.endNode.getName()  //$NON-NLS-1$
				+ " (" + this.endNode.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);
		
		this.measurementPath = MeasurementPath.createInstance(
				this.schemePath,
				this.startNode, 
				this.endNode,
				this.logicalNetLayer.getMapView());

		this.logicalNetLayer.getMapView().addMeasurementPath(this.measurementPath);
		setResult(Command.RESULT_OK);
	}
}

