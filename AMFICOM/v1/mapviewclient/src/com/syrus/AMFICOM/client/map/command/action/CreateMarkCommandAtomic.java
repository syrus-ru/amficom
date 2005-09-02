/**
 * $Id: CreateMarkCommandAtomic.java,v 1.27 2005/09/02 16:46:36 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
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
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * ������� �������� ����� �� �����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.27 $, $Date: 2005/09/02 16:46:36 $
 * @module mapviewclient
 */
public class CreateMarkCommandAtomic extends MapActionCommand {
	/**
	 * ��������� ������� �����
	 */
	Mark mark;

	/**
	 * ��������� �������� �����
	 */
	PhysicalLink link;
	
	Map map;
	
	/**
	 * ��������� �� ������ �����, �� ������� ��������� �����
	 */
	double distance;
	
	/**
	 * �����, � ������� ��������� ����� �������������� ����
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
				getClass().getName() + "::execute() | " 
					+ "create mark at link " + this.link.getName() 
					+ " (" + this.link.getId() + ")", 
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
				this.mark.setName(LangModelMap.getString("mark"));
			} catch (CreateObjectException e) {
				e.printStackTrace();
			}
			this.map.addNode(this.mark);
			MarkController mc = (MarkController)
				getLogicalNetLayer().getMapViewController().getController(this.mark);
			mc.updateScaleCoefficient(this.mark);
			mc.moveToFromStartLt(this.mark, this.distance);
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.setCurrentMapElement(this.mark);
			setResult(Command.RESULT_OK);
		} catch(Exception e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
	@Override
	public void undo() {
		this.map.removeNode(this.mark);
	}
	
	@Override
	public void redo() {
		this.map.addNode(this.mark);
	}
}
