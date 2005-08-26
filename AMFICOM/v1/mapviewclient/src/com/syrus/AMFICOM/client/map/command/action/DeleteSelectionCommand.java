/**
 * $Id: DeleteSelectionCommand.java,v 1.28 2005/08/26 15:39:54 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.util.Log;

/**
 * ������� ��������� �������� �����. ������� �������� ������ ������
 * (CommandBundle), ��������� ��������� ��������.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.28 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclient
 */
public class DeleteSelectionCommand extends MapActionCommandBundle {
	/**
	 * ��� ��������� ����������� ���� ���� ��������� ������� �� ��������
	 * ��������� ��������. ���������� �������� �������������� ������ ��� ������
	 * execute()
	 */
	@Override
	public void setNetMapViewer(final NetMapViewer netMapViewer) {
		super.setNetMapViewer(netMapViewer);

		// ������� ��� ��������� �������� ������������ �� ���������� �� ��
		// ��������
		Iterator e;

		final LinkedList<AbstractNode> nodesToDelete = new LinkedList<AbstractNode>();
		final LinkedList<NodeLink> nodeLinksToDelete = new LinkedList<NodeLink>();
		final LinkedList<PhysicalLink> linksToDelete = new LinkedList<PhysicalLink>();
		final LinkedList cablePathsToDelete = new LinkedList();

		final int showMode = this.logicalNetLayer.getMapState().getShowMode();

		for(Iterator it = this.logicalNetLayer.getSelectedElements().iterator(); it .hasNext();) {
			MapElement me = (MapElement) it.next();
			if(me instanceof AbstractNode) {
				nodesToDelete.add((AbstractNode) me);
			}
			else if(me instanceof NodeLink
					&& showMode == MapState.SHOW_NODE_LINK) {
				NodeLink nodeLink = (NodeLink) me;
				PhysicalLink link = nodeLink.getPhysicalLink();
				if(!(link instanceof UnboundLink)) {
					nodeLinksToDelete.add(nodeLink);
				}
			}
			else if(me instanceof PhysicalLink
					&& showMode == MapState.SHOW_PHYSICAL_LINK) {
				if(!(me instanceof UnboundLink)) {
					linksToDelete.add((PhysicalLink) me);
				}
			}
		}
		// ������� ������ ������ �������� ����������
		e = linksToDelete.iterator();
		while(e.hasNext()) {
			DeletePhysicalLinkCommandBundle command = new DeletePhysicalLinkCommandBundle(
					(PhysicalLink) e.next());
			command.setNetMapViewer(this.netMapViewer);
			add(command);
		}

		// ������� ������ ������ �������� ����������
		e = nodeLinksToDelete.iterator();
		while(e.hasNext()) {
			DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(
					(NodeLink) e.next());
			command.setNetMapViewer(this.netMapViewer);
			add(command);
		}

		// ������� ������ ������ �������� �����
		e = nodesToDelete.iterator();
		while(e.hasNext()) {
			DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(
					(AbstractNode) e.next());
			command.setNetMapViewer(this.netMapViewer);
			add(command);
		}

		// ������� ������ ������ �������� �����
		e = cablePathsToDelete.iterator();
		while(e.hasNext()) {
			// removeCablePath((MapCablePathElement )e.next());
			UnPlaceSchemeCableLinkCommand command = new UnPlaceSchemeCableLinkCommand(
					(CablePath) e.next());
			command.setNetMapViewer(this.netMapViewer);
			command.execute();
		}
	}

	/**
	 * ����� �������� �������� ������� ������� ����� � ���������� ���������� ��
	 * ���������� �����
	 */
	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " + "method call", 
			Level.FINEST);

		// ��������� ��� ������� � ������
		super.execute();

		MapElement mapElement = VoidElement.getInstance(this.logicalNetLayer.getMapView());

		this.logicalNetLayer.setCurrentMapElement(mapElement);
	}

}
