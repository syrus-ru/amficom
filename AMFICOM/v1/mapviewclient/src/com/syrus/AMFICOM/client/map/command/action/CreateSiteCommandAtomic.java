/**
 * $Id: CreateSiteCommandAtomic.java,v 1.28 2005/08/26 15:39:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * Разместить сетевой элемент на карте. используется при переносе (drag/drop), в
 * точке point (в экранных координатах)
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.28 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclient
 */
public class CreateSiteCommandAtomic extends MapActionCommand {
	/**
	 * создаваемый узел
	 */
	SiteNode site;

	/** тип создаваемого элемента */
	SiteNodeType type;

	Map map;

	/**
	 * экранная точка, в которой создается новый топологический узел
	 */
	Point point = null;

	/**
	 * географическая точка, в которой создается новый топологический узел.
	 * может инициализироваться по point
	 */
	DoublePoint coordinatePoint = null;

	public CreateSiteCommandAtomic(SiteNodeType proto, DoublePoint dpoint) {
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.type = proto;
		this.coordinatePoint = dpoint;
	}

	public CreateSiteCommandAtomic(SiteNodeType proto, Point point) {
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.type = proto;
		this.point = point;
	}

	public SiteNode getSite() {
		return this.site;
	}

	@Override
	public void execute() {
		try {
			Log.debugMessage(
				getClass().getName() + "::execute() | "
					+ "create site node of type "
					+ this.type.getName() 
					+ " (" + this.type.getId() + ")", 
				Level.FINEST);
			if(!getLogicalNetLayer().getContext().getApplicationModel()
					.isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
				return;
			if(this.coordinatePoint == null)
				this.coordinatePoint = this.logicalNetLayer.getConverter()
						.convertScreenToMap(this.point);
			this.map = this.logicalNetLayer.getMapView().getMap();
			// создать новый узел
			try {
				this.site = SiteNode.createInstance(
						LoginManager.getUserId(),
						this.coordinatePoint,
						this.type);
			} catch(CreateObjectException e) {
				e.printStackTrace();
			}
			SiteNodeController snc = (SiteNodeController) getLogicalNetLayer()
					.getMapViewController().getController(this.site);
			snc.updateScaleCoefficient(this.site);
			this.map.addNode(this.site);

			this.logicalNetLayer.setCurrentMapElement(this.site);
			setResult(Command.RESULT_OK);
		} catch(Exception e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

	@Override
	public void undo() {
		this.map.removeNode(this.site);
	}

	@Override
	public void redo() {
		this.map.addNode(this.site);
	}
}
