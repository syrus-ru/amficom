/*-
 * $$Id: CreateMarkCommandAtomic.java,v 1.40 2006/04/14 12:04:07 arseniy Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;

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
 * ??????? ???????? ????? ?? ?????
 * 
 * @version $Revision: 1.40 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateMarkCommandAtomic extends MapActionCommand {
	/**
	 * ????????? ??????? ?????
	 */
	Mark mark;

	/**
	 * ????????? ???????? ?????
	 */
	PhysicalLink link;
	
	Map map;
	
	/**
	 * ????????? ?? ?????? ?????, ?? ??????? ????????? ?????
	 */
	double distance;
	
	/**
	 * ?????, ? ??????? ????????? ????? ?????????????? ????
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
			Log.debugMessage("create mark at link " + this.link.getName()  //$NON-NLS-1$
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
				Log.errorMessage(e);
			}
			this.map.addNode(this.mark);
			MarkController mc = (MarkController)
				getLogicalNetLayer().getMapViewController().getController(this.mark);
			mc.updateScaleCoefficient(this.mark);
			mc.moveToFromStartLt(this.mark, this.distance);
			// ???????? ????????? - ?????????? ??????????
			this.logicalNetLayer.setCurrentMapElement(this.mark);
			setResult(Command.RESULT_OK);
		} catch(Exception e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.errorMessage(e);
		}
	}
	
	@Override
	public void undo() {
		Log.errorMessage(NOT_IMPLEMENTED);

//		???? ????? ??? ????? ?? ????????
//		try {
//			StorableObjectPool.putStorableObject(this.mark);
//			this.map.removeNode(this.mark);
//		} catch(IllegalObjectEntityException e) {
//			Log.errorMessage(e);
//		}
	}
	
	@Override
	public void redo() {
		this.map.addNode(this.mark);
		StorableObjectPool.delete(this.mark.getId());
	}
}
