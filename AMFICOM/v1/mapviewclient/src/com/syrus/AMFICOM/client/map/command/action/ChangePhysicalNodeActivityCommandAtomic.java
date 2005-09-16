/**
 * $Id: ChangePhysicalNodeActivityCommandAtomic.java,v 1.15 2005/09/16 14:53:32 krupenn Exp $
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

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.util.Log;

/**
 * Изменение активности топологического узла - атомарное действие
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/09/16 14:53:32 $
 * @module mapviewclient
 */
public class ChangePhysicalNodeActivityCommandAtomic extends MapActionCommand {
	/** узел */
	TopologicalNode node;

	/**
	 * новое состояние активности узла
	 */
	boolean active;

	TopologicalNodeController controller;

	public ChangePhysicalNodeActivityCommandAtomic(
			TopologicalNode node,
			boolean active) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = node;
		this.active = active;
	}

	@Override
	public void setLogicalNetLayer(LogicalNetLayer lnl) {
		super.setLogicalNetLayer(lnl);

		this.controller = (TopologicalNodeController) 
				lnl.getMapViewController().getController(this.node);
	}

	public TopologicalNode getNode() {
		return this.node;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | "  //$NON-NLS-1$
				+ " set " + this.node.getId() + " active(" + this.active + ")",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Level.FINEST);

		this.controller.setActive(this.node, this.active);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public void undo() {
		this.controller.setActive(this.node, !this.active);
	}
}
