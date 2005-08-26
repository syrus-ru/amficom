/**
 * $Id: CreateCablePathCommandAtomic.java,v 1.21 2005/08/26 15:39:54 krupenn Exp $
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
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * создание пути рпокладки кабеля, внесение его в пул и на карту - атомарное
 * действие
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.21 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclient
 */
public class CreateCablePathCommandAtomic extends MapActionCommand {
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
			AbstractNode endNode) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemeCableLink = schemeCableLink;
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public CablePath getPath() {
		return this.cablePath;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " 
				+ "create CablePath for SchemeCableLink " 
				+ this.schemeCableLink.getName() 
				+ " (" + this.schemeCableLink.getId() 
				+ ") with start at node " + this.startNode.getName() 
				+ " (" + this.startNode.getId() 
				+ ") and end at node " + this.endNode.getName() 
				+ " (" + this.endNode.getId() + ")", 
			Level.FINEST);
		
		this.cablePath = com.syrus.AMFICOM.mapview.CablePath.createInstance(
				this.schemeCableLink,
				this.startNode,
				this.endNode);

		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
	}
}

