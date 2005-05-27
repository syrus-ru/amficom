/**
 * $Id: MapPopupMenu.java,v 1.38 2005/05/27 15:14:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/
package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateCollectorCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteSelectionCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.GenerateCablePathCablingCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.GenerateUnboundLinkCablingCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.InsertSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MapElementStateChangeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveCollectorCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.Client.Map.Props.MapVisualManager;
import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.UI.dialogs.WrapperedComboChooserDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
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
 * @version $Revision: 1.38 $, $Date: 2005/05/27 15:14:57 $
 * @module mapviewclient_v1
 */
public abstract class MapPopupMenu extends JPopupMenu {
	protected LogicalNetLayer logicalNetLayer;

	protected Point point;

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer) {
		this.logicalNetLayer = logicalNetLayer;
	}

	public LogicalNetLayer getLogicalNetLayer() {
		return this.logicalNetLayer;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Point getPoint() {
		return this.point;
	}

	public void showProperties(Object me) {
		StorableObjectEditor prop = MapVisualManager.getVisualManager((MapElement )me).getGeneralPropertiesPanel();
		if(prop == null)
			return;
		EditorDialog dialog = new EditorDialog(
				LangModelGeneral.getString("Properties"), 
				true, 
				me,
				prop);

//		MapElementState mes = me.getState();
		dialog.setVisible(true);

		if(dialog.ifAccept()) {
// MapElementState mes2 = me.getState();
//			if(! mes.equals(mes2))
			{
				Dispatcher disp = this.logicalNetLayer.getContext().getDispatcher();
				if(disp != null)
					disp.firePropertyChange(new MapEvent(this, MapEvent.MAP_CHANGED));
			}
			
		}
	}

	public abstract void setElement(Object me);

	protected Collector selectCollector() {
		Collector collector = null;

		Collection list = this.logicalNetLayer.getMapView().getMap().getAllCollectors();
		
		WrapperedComboChooserDialog dialog = new WrapperedComboChooserDialog(list);
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == WrapperedComboChooserDialog.RET_OK) {
			collector = (Collector )dialog.getSelected();
		}
		
		return collector;
	}
	
	protected SiteNodeType selectNodeProto() {
		SiteNodeType proto = null;

		Collection list = NodeTypeController.getTopologicalNodeTypes();

		WrapperedComboChooserDialog dialog = new WrapperedComboChooserDialog(list);
		
		Iterator listIt = list.iterator();
		if (listIt.hasNext())
			dialog.setSelected(listIt.next());
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == WrapperedComboChooserDialog.RET_OK) {
			proto = (SiteNodeType )dialog.getSelected();
		}

		return proto;
	}

	protected SiteNode selectSiteNode() {
		SiteNode site = null;

		List list = new LinkedList();
		for(Iterator it = getLogicalNetLayer().getMapView().getMap()
				.getAllSiteNodes().iterator(); it.hasNext();) {
			SiteNode s = (SiteNode)it.next();
			if(!( s instanceof UnboundNode))
				list.add(s);
		}
		
		WrapperedComboChooserDialog dialog = new WrapperedComboChooserDialog(list);
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == WrapperedComboChooserDialog.RET_OK) {
			site = (SiteNode )dialog.getSelected();
		}
		return site;
	}

	protected PhysicalLink selectPhysicalLinkAt(UnboundLink unbound) {
		Map map = this.logicalNetLayer.getMapView().getMap();
		
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

		WrapperedComboChooserDialog dialog = new WrapperedComboChooserDialog(list);
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == WrapperedComboChooserDialog.RET_OK) {
			link = (PhysicalLink )dialog.getSelected();
		}

		return link;
	}

	protected Collector createCollector() {
		String inputValue = JOptionPane.showInputDialog(
				Environment.getActiveWindow(), 
				"Введите имя коллектора", 
				"Коллектор1");
		if(inputValue != null) {
			CreateCollectorCommandAtomic command = new CreateCollectorCommandAtomic(inputValue);
			command.setLogicalNetLayer(this.logicalNetLayer);
			getLogicalNetLayer().getCommandList().add(command);
			getLogicalNetLayer().getCommandList().execute();
			
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

		Collector prevCollector = this.logicalNetLayer.getMapView().getMap().getCollector(mple);
		if(prevCollector != null)
			prevCollector.removePhysicalLink(mple);
	
		collector.addPhysicalLink(mple);

		MapElementState state = mple.getState();
		mple.setType(collectorType);

		MapElementStateChangeCommand command2 = new MapElementStateChangeCommand(mple, state, mple.getState());
		command2.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command2);
		getLogicalNetLayer().getCommandList().execute();
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
		command.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
		
		if(collector.getPhysicalLinks().size() == 0)
			removeCollector(collector);
	}
	
	protected void removeCollector(Collector collector) {
		RemoveCollectorCommandAtomic command = new RemoveCollectorCommandAtomic(collector);
		command.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
	}

	protected void removeMapElement(MapElement me) {
		getLogicalNetLayer().deselectAll();
		getLogicalNetLayer().getMapView().getMap().setSelected(me, true);
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
	}

	protected void insertSiteInPlaceOfANode(TopologicalNode node, SiteNodeType proto) {
		InsertSiteCommandBundle command = new InsertSiteCommandBundle(node, proto);
		command.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
	}

	protected void convertUnboundNodeToSite(UnboundNode unbound, SiteNodeType proto) {
		if(unbound.isRemoved())
			return;

		CreateSiteCommandAtomic command = new CreateSiteCommandAtomic(proto, unbound.getLocation());
		command.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
		
		SiteNode site = command.getSite();

		BindUnboundNodeToSiteCommandBundle command2 = new BindUnboundNodeToSiteCommandBundle(unbound, site);
		command2.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command2);
		getLogicalNetLayer().getCommandList().execute();
		
		try {
			getLogicalNetLayer().repaint(false);
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
		command.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		try {
			getLogicalNetLayer().repaint(false);
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
		command.setLogicalNetLayer(this.logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		try {
			getLogicalNetLayer().repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
