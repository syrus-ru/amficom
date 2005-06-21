/**
 * $Id: MapPopupMenu.java,v 1.44 2005/06/21 12:48:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/
package com.syrus.AMFICOM.client.map.popup;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.UI.dialogs.WrapperedComboChooserDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.CreateCollectorCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.DeleteSelectionCommand;
import com.syrus.AMFICOM.client.map.command.action.GenerateCablePathCablingCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.GenerateUnboundLinkCablingCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.InsertSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.MapElementStateChangeCommand;
import com.syrus.AMFICOM.client.map.command.action.RemoveCollectorCommandAtomic;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.props.MapVisualManager;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;

/**
 * Контекстное меню элемента карты
 * @author $Author: krupenn $
 * @version $Revision: 1.44 $, $Date: 2005/06/21 12:48:47 $
 * @module mapviewclient_v1
 */
public abstract class MapPopupMenu extends JPopupMenu {

	protected Point point;
	protected NetMapViewer netMapViewer;

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Point getPoint() {
		return this.point;
	}

	public void showProperties(Object me) {
		StorableObjectEditor prop = MapVisualManager.getVisualManager(me).getGeneralPropertiesPanel();
		if(prop == null)
			return;
//		MapElementState mes = me.getState();
		if(EditorDialog.showEditorDialog(
				LangModelGeneral.getString("Properties"), 
				me,
				prop)) {
// MapElementState mes2 = me.getState();
//			if(! mes.equals(mes2))
			{
				Dispatcher disp = this.netMapViewer.getLogicalNetLayer().getContext().getDispatcher();
				if(disp != null)
					disp.firePropertyChange(new MapEvent(this, MapEvent.MAP_CHANGED));
			}
			
		}
	}

	public abstract void setElement(Object me);

	protected Collector selectCollector() {
		Collection list = this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getAllCollectors();
		return (Collector )WrapperedComboChooserDialog.showChooserDialog(list);
	}
	
	protected SiteNodeType selectNodeProto() {
		Object toSelect = null;
		Collection list = NodeTypeController.getTopologicalNodeTypes();
		Iterator listIt = list.iterator();
		if (listIt.hasNext())
			toSelect = listIt.next();

		return (SiteNodeType )WrapperedComboChooserDialog.showChooserDialog(list, toSelect);
	}

	protected SiteNode selectSiteNode() {
		SiteNode site = null;

		List list = new LinkedList();
		for(Iterator it = this.netMapViewer.getLogicalNetLayer().getMapView().getMap()
				.getAllSiteNodes().iterator(); it.hasNext();) {
			SiteNode s = (SiteNode)it.next();
			if(!( s instanceof UnboundNode))
				list.add(s);
		}
		
		return (SiteNode )WrapperedComboChooserDialog.showChooserDialog(list);
	}

	protected PhysicalLink selectPhysicalLinkAt(UnboundLink unbound) {
		Map map = this.netMapViewer.getLogicalNetLayer().getMapView().getMap();
		
		PhysicalLink link = null;
		
		AbstractNode node1 = unbound.getStartNode();
		AbstractNode node2 = unbound.getEndNode();

		List list = new LinkedList();

		// select physical links that connect same end nodes as link
		Collection list2 = map.getPhysicalLinksAt(node1);
		for(Iterator it = map.getPhysicalLinksAt(node2).iterator(); it.hasNext();) {
			PhysicalLink le = (PhysicalLink)it.next();
			if(! (le instanceof UnboundLink))
				if(list2.contains(le))
					list.add(le);
		}

		return (PhysicalLink )WrapperedComboChooserDialog.showChooserDialog(list);
	}

	protected Collector createCollector() {
		String inputValue = JOptionPane.showInputDialog(
				Environment.getActiveWindow(), 
				"Введите имя коллектора", 
				"Коллектор1");
		if(inputValue != null) {
			CreateCollectorCommandAtomic command = new CreateCollectorCommandAtomic(inputValue);
			command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
			this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
			this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
			
			return command.getCollector();
		}
		
		return null;
	}
	
	protected void addLinksToCollector(Collector collector, Set links) {
		for(Iterator it = links.iterator(); it.hasNext();) {
			PhysicalLink mple = (PhysicalLink )it.next();

			addLinkToCollector(collector, mple);
		}
	}

	protected void addLinkToCollector(Collector collector, PhysicalLink mple) {
		PhysicalLinkType collectorType = LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_COLLECTOR);

		Collector prevCollector = this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(mple);
		if(prevCollector != null)
			prevCollector.removePhysicalLink(mple);
	
		collector.addPhysicalLink(mple);

		MapElementState state = mple.getState();
		mple.setType(collectorType);

		MapElementStateChangeCommand command2 = new MapElementStateChangeCommand(mple, state, mple.getState());
		command2.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command2);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
	}

	protected void removeLinksFromCollector(Collector collector, Set links) {
		for(Iterator it = links.iterator(); it.hasNext();) {
			PhysicalLink mple = (PhysicalLink )it.next();

			removeLinkFromCollector(collector, mple);
		}
	}

	protected void removeLinkFromCollector(
			Collector collector,
			PhysicalLink mple) {
		collector.removePhysicalLink(mple);

		MapElementState state = mple.getState();

		mple.setType(LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_TUNNEL));

		MapElementStateChangeCommand command = new MapElementStateChangeCommand(mple, state, mple.getState());
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
		
		if(collector.getPhysicalLinks().size() == 0)
			removeCollector(collector);
	}
	
	protected void removeCollector(Collector collector) {
		RemoveCollectorCommandAtomic command = new RemoveCollectorCommandAtomic(collector);
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
	}

	protected void removeMapElement(MapElement me) {
		this.netMapViewer.getLogicalNetLayer().deselectAll();
		this.netMapViewer.getLogicalNetLayer().getMapView().getMap().setSelected(me, true);
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
	}

	protected void insertSiteInPlaceOfANode(TopologicalNode node, SiteNodeType proto) {
		InsertSiteCommandBundle command = new InsertSiteCommandBundle(node, proto);
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
	}

	protected void convertUnboundNodeToSite(UnboundNode unbound, SiteNodeType proto) {
		if(unbound.isRemoved())
			return;

		CreateSiteCommandAtomic command = new CreateSiteCommandAtomic(proto, unbound.getLocation());
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
		
		SiteNode site = command.getSite();

		BindUnboundNodeToSiteCommandBundle command2 = new BindUnboundNodeToSiteCommandBundle(unbound, site);
		command2.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command2);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
		
		try {
			this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void generatePathCabling(CablePath path, SiteNodeType proto) {
		GenerateCablePathCablingCommandBundle command = 
				new GenerateCablePathCablingCommandBundle(path, proto);
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();

		try {
			this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void convertUnboundLinkToPhysicalLink(UnboundLink unbound) {
		GenerateUnboundLinkCablingCommandBundle command = 
				new GenerateUnboundLinkCablingCommandBundle(unbound);
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();

		try {
			this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
