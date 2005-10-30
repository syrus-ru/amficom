/*-
 * $$Id: CreateMarkCommandAtomic.java,v 1.34 2005/10/30 14:48:55 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 *  оманда создани€ метки на линии
 * 
 * @version $Revision: 1.34 $, $Date: 2005/10/30 14:48:55 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateMarkCommandAtomic extends MapActionCommand {
	/**
	 * созданный элемент метки
	 */
	Mark mark;

	/**
	 * ¬ыбранный фрагмент линии
	 */
	PhysicalLink link;
	
	Map map;
	
	/**
	 * дистанци€ от начала линии, на которой создаетс€ метка
	 */
	double distance;
	
	/**
	 * точка, в которой создаетс€ новый топологический узел
	 */
	Point point;

	public CreateMarkCommandAtomic(
			PhysicalLink link,
			Point point) {
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.link = link;
		this.point = point;
	}

	@Override
	public void execute() {
		try {
			Log.debugMessage(
				getClass().getName() + "::execute() | "  //$NON-NLS-1$
					+ "create mark at link " + this.link.getName()  //$NON-NLS-1$
					+ " (" + this.link.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

			if ( !getLogicalNetLayer().getContext().getApplicationModel()
					.isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
				return;
			}
			MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
			this.map = this.logicalNetLayer.getMapView().getMap();
			this.link.sortNodeLinks();
			this.distance = 0.0;
			AbstractNode node = this.link.getStartNode();
			for(Iterator it = this.link.getNodeLinks().iterator(); it.hasNext();) {
				NodeLink nodeLink = (NodeLink)it.next();

				NodeLinkController nlc = (NodeLinkController)
					getLogicalNetLayer().getMapViewController().getController(nodeLink);

				if(nlc.isMouseOnElement(nodeLink, this.point)) {
					DoublePoint dpoint = converter.convertScreenToMap(this.point);
					this.distance += converter.distance(node.getLocation(), dpoint);
					break;
				}
				nlc.updateLengthLt(nodeLink);
				this.distance += nodeLink.getLengthLt();

				if(nodeLink.getStartNode().equals(node))
					node = nodeLink.getEndNode();
				else
					node = nodeLink.getStartNode();
			}
			try {
				this.mark = Mark.createInstance(
						LoginManager.getUserId(),
						this.link, 
						this.distance);
				this.mark.setName(I18N.getString(MapEditorResourceKeys.ENTITY_MARK));
			} catch (CreateObjectException e) {
				Log.debugMessage(e, Level.SEVERE);
			}
			this.map.addNode(this.mark);
			MarkController mc = (MarkController)
				getLogicalNetLayer().getMapViewController().getController(this.mark);
			mc.updateScaleCoefficient(this.mark);
			mc.moveToFromStartLt(this.mark, this.distance);
			// операци€ закончена - оповестить слушателей
			this.logicalNetLayer.setCurrentMapElement(this.mark);
			setResult(Command.RESULT_OK);
		} catch(Exception e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugMessage(e, Level.SEVERE);
		}
	}
	
	@Override
	public void undo() {
		try {
			StorableObjectPool.putStorableObject(this.mark);
			this.map.removeNode(this.mark);
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void redo() {
		this.map.addNode(this.mark);
		StorableObjectPool.delete(this.mark.getId());
	}
}
