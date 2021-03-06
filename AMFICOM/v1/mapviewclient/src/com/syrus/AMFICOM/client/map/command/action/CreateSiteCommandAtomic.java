/*-
 * $$Id: CreateSiteCommandAtomic.java,v 1.40 2006/04/14 12:04:07 arseniy Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;

import java.awt.Point;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * ?????????? ??????? ??????? ?? ?????. ???????????? ??? ???????? (drag/drop), ?
 * ????? point (? ???????? ???????????)
 * 
 * @version $Revision: 1.40 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateSiteCommandAtomic extends MapActionCommand {
	/**
	 * ??????????? ????
	 */
	SiteNode site;

	/** ??? ???????????? ???????? */
	SiteNodeType type;

	Map map;

	/**
	 * ???????? ?????, ? ??????? ????????? ????? ?????????????? ????
	 */
	Point point = null;

	/**
	 * ?????????????? ?????, ? ??????? ????????? ????? ?????????????? ????.
	 * ????? ?????????????????? ?? point
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
			Log.debugMessage("create site node of type " //$NON-NLS-1$
					+ this.type.getName() 
					+ " (" + this.type.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);
			if(!getLogicalNetLayer().getContext().getApplicationModel()
					.isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
				return;
			if(this.coordinatePoint == null) {
				this.coordinatePoint = this.logicalNetLayer.getConverter()
						.convertScreenToMap(this.point);
			}
			this.map = this.logicalNetLayer.getMapView().getMap();
			// ??????? ????? ????
			try {
				this.site = SiteNode.createInstance(
						LoginManager.getUserId(),
						this.coordinatePoint,
						this.type);
			} catch(CreateObjectException e) {
				Log.errorMessage(e);
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
			Log.errorMessage(e);
		}
	}

	@Override
	public void undo() {
		Log.errorMessage(NOT_IMPLEMENTED);

//		???? ????? ??? ????? ?? ????????
//		try {
//			StorableObjectPool.putStorableObject(this.site);
//			this.map.removeNode(this.site);
//		} catch(IllegalObjectEntityException e) {
//			Log.errorMessage(e);
//		}
	}

	@Override
	public void redo() {
		this.map.addNode(this.site);
		StorableObjectPool.delete(this.site.getId());
	}
}
