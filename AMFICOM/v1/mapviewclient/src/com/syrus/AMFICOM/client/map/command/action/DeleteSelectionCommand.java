/*-
 * $$Id: DeleteSelectionCommand.java,v 1.34 2005/09/30 16:08:37 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.VoidElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

/**
 * ������� ��������� �������� �����. ������� �������� ������ ������
 * (CommandBundle), ��������� ��������� ��������.
 * 
 * @version $Revision: 1.34 $, $Date: 2005/09/30 16:08:37 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class DeleteSelectionCommand extends MapActionCommandBundle {
	List<Scheme> schemes;
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
		final LinkedList<AbstractNode> nodesToDelete = new LinkedList<AbstractNode>();
		final LinkedList<NodeLink> nodeLinksToDelete = new LinkedList<NodeLink>();
		final LinkedList<PhysicalLink> linksToDelete = new LinkedList<PhysicalLink>();
		final LinkedList<CablePath> cablePathsToDelete = new LinkedList<CablePath>();

		final int showMode = this.logicalNetLayer.getMapState().getShowMode();

		for(MapElement mapElement : this.logicalNetLayer.getSelectedElements()) {
			if(mapElement instanceof AbstractNode) {
				nodesToDelete.add((AbstractNode) mapElement);
			}
			else if(mapElement instanceof NodeLink
					&& showMode == MapState.SHOW_NODE_LINK) {
				NodeLink nodeLink = (NodeLink) mapElement;
				PhysicalLink link = nodeLink.getPhysicalLink();
				if(!(link instanceof UnboundLink)) {
					nodeLinksToDelete.add(nodeLink);
				}
			}
			else if(mapElement instanceof PhysicalLink
					&& showMode == MapState.SHOW_PHYSICAL_LINK) {
				if(!(mapElement instanceof UnboundLink)) {
					linksToDelete.add((PhysicalLink) mapElement);
				}
			}
		}

		// ������� ������ ������ �������� ����������
		for(PhysicalLink physicalLink : linksToDelete) {
			//cannot delete INDOOR - it should be deletea automatically
			//when corresponding cableinlet is deleted
			if(physicalLink.getType().getSort().value() != PhysicalLinkTypeSort._INDOOR) {
				DeletePhysicalLinkCommandBundle command = 
					new DeletePhysicalLinkCommandBundle(physicalLink);
				command.setNetMapViewer(this.netMapViewer);
				add(command);
			}
		}

		// ������� ������ ������ �������� ����������
		for(NodeLink nodeLink : nodeLinksToDelete) {
			//cannot delete INDOOR - it should be deletea automatically
			//when corresponding cableinlet is deleted
			if(nodeLink.getPhysicalLink().getType().getSort().value() != PhysicalLinkTypeSort._INDOOR) {
				DeleteNodeLinkCommandBundle command = 
					new DeleteNodeLinkCommandBundle(nodeLink);
				command.setNetMapViewer(this.netMapViewer);
				add(command);
			}
		}

		// ������� ������ ������ �������� �����
		for(AbstractNode node : nodesToDelete) {
			DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(node);
			command.setNetMapViewer(this.netMapViewer);
			add(command);
			if(node instanceof SiteNode) {
				SiteNode siteNode = (SiteNode) node;
				for(SiteNode attachedSiteNode : siteNode.getAttachedSiteNodes()) {
					DeleteNodeCommandBundle command2 = new DeleteNodeCommandBundle(attachedSiteNode);
					command2.setNetMapViewer(this.netMapViewer);
					add(command2);
				}
			}
		}

		this.schemes = new LinkedList<Scheme>();

		// ������� ������ ������ �������� �����
		for(CablePath cablePath : cablePathsToDelete) {
			UnPlaceSchemeCableLinkCommand command = 
				new UnPlaceSchemeCableLinkCommand(cablePath);
			command.setNetMapViewer(this.netMapViewer);
			command.execute();
			this.schemes.add(cablePath.getSchemeCableLink().getParentScheme());
			setUndoable(false);
		}
	}

	/**
	 * ����� �������� �������� ������� ������� ����� � ���������� ���������� ��
	 * ���������� �����
	 */
	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " + "method call",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		// ��������� ��� ������� � ������
		super.execute();

		for(Scheme scheme : this.schemes) {
			this.logicalNetLayer.getMapViewController().scanPaths(scheme);
		}

		MapElement mapElement = VoidElement.getInstance(this.logicalNetLayer.getMapView());

		this.logicalNetLayer.setCurrentMapElement(mapElement);
	}

}
