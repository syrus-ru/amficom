/**
 * $Id: MapPopupMenu.java,v 1.26 2005/02/02 09:05:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceSelectionDialog;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateCollectorCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteSelectionCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.GenerateCablePathCablingCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.GenerateUnboundLinkCablingCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.InsertSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MapElementStateChangeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveCollectorCommandAtomic;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Props.MapPropertiesPane;
import com.syrus.AMFICOM.Client.Map.Props.MapPropsManager;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * Контекстное меню элемента карты
 * 
 * 
 * 
 * @version $Revision: 1.26 $, $Date: 2005/02/02 09:05:10 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public abstract class MapPopupMenu extends JPopupMenu
{
	protected LogicalNetLayer logicalNetLayer;
	
	protected Point point;

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

	public void setPoint(Point point)
	{
		this.point = point;
	}

	public Point getPoint()
	{
		return point;
	}

	public void showProperties(Object me)
	{
		ObjectResourcePropertiesPane prop = MapPropsManager.getPropsPane(me);
		if(prop == null)
			return;
		prop.setContext(logicalNetLayer.getContext());
		((MapPropertiesPane )prop).setLogicalNetLayer(logicalNetLayer);
		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModel.getString("Properties"), 
				true, 
				me,
				prop);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation(
				(screenSize.width - frameSize.width)/2, 
				(screenSize.height - frameSize.height)/2);
				
//		MapElementState mes = me.getState();
		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
//			MapElementState mes2 = me.getState();
//			if(! mes.equals(mes2))
			{
				Dispatcher disp = logicalNetLayer.getContext().getDispatcher();
				if(disp != null)
					disp.notify(new MapEvent(this, MapEvent.MAP_CHANGED));
			}
			
		}
	}

	public abstract void setElement(Object me);

	protected Collector selectCollector()
	{
		Collector collector = null;

		List list = logicalNetLayer.getMapView().getMap().getCollectors();
		
		ObjectResourceSelectionDialog dialog = new ObjectResourceSelectionDialog(list);
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == ObjectResourceSelectionDialog.RET_OK)
		{
			collector = (Collector )dialog.getSelected();
		}
		
		return collector;
	}
	
	protected SiteNodeType selectNodeProto()
	{
		SiteNodeType proto = null;

		List list = NodeTypeController.getTopologicalProtos(logicalNetLayer.getContext());

		ObjectResourceSelectionDialog dialog = new ObjectResourceSelectionDialog(list);
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == ObjectResourceSelectionDialog.RET_OK)
		{
			proto = (SiteNodeType)dialog.getSelected();
		}
		
		return proto;
	}

	protected SiteNode selectSiteNode()
	{
		SiteNode site = null;

		List list = new LinkedList();
		for(Iterator it = getLogicalNetLayer().getMapView().getMap().getSiteNodes().iterator(); it.hasNext();)
		{
			SiteNode s = (SiteNode)it.next();
			if(!( s instanceof UnboundNode))
				list.add(s);
		}
		
		ObjectResourceSelectionDialog dialog = new ObjectResourceSelectionDialog(list);
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == ObjectResourceSelectionDialog.RET_OK)
		{
			site = (SiteNode)dialog.getSelected();
		}
		return site;
	}

	protected PhysicalLink selectPhysicalLinkAt(UnboundLink unbound)
	{
		Map map = logicalNetLayer.getMapView().getMap();
		
		PhysicalLink link = null;
		
		AbstractNode node1 = unbound.getStartNode();
		AbstractNode node2 = unbound.getEndNode();

		List list = new LinkedList();

		// select physical links that connect same end nodes as link
		List list2 = map.getPhysicalLinksAt(node1);
		for(Iterator it = map.getPhysicalLinksAt(node2).iterator(); it.hasNext();)
		{
			PhysicalLink le = (PhysicalLink)it.next();
			if(! (le instanceof UnboundLink))
				if(list2.contains(le))
					list.add(le);
		}

		ObjectResourceSelectionDialog dialog = new ObjectResourceSelectionDialog(list);
			
		dialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = dialog.getSize();
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		dialog.show();

		if(dialog.getReturnCode() == ObjectResourceSelectionDialog.RET_OK)
		{
			link = (PhysicalLink)dialog.getSelected();
		}
		
		return link;
	}

	protected Collector createCollector()
	{
		String inputValue = JOptionPane.showInputDialog(
				Environment.getActiveWindow(), 
				"Введите имя коллектора", 
				"Коллектор1");
		if(inputValue != null)
		{
			CreateCollectorCommandAtomic command = new CreateCollectorCommandAtomic(inputValue);
			command.setLogicalNetLayer(logicalNetLayer);
			getLogicalNetLayer().getCommandList().add(command);
			getLogicalNetLayer().getCommandList().execute();
			
			return command.getCollector();
		}
		
		return null;
	}
	
	protected void addLinksToCollector(Collector collector, List links)
	{
		for(Iterator it = links.iterator(); it.hasNext();)
		{
			PhysicalLink mple = (PhysicalLink)it.next();

			addLinkToCollector(collector, mple);
		}
	}
	
	protected void addLinkToCollector(Collector collector, PhysicalLink mple)
	{
		Identifier creatorId = getLogicalNetLayer().getUserId();
			
		PhysicalLinkType collectorType = LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.COLLECTOR);

		Collector prevCollector = logicalNetLayer.getMapView().getMap().getCollector(mple);
		if(prevCollector != null)
			prevCollector.removePhysicalLink(mple);
	
		collector.addPhysicalLink(mple);

		MapElementState state = mple.getState();
		mple.setType(collectorType);

		MapElementStateChangeCommand command2 = new MapElementStateChangeCommand(mple, state, mple.getState());
		command2.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command2);
		getLogicalNetLayer().getCommandList().execute();
	}

	protected void removeLinksFromCollector(Collector collector, List links)
	{
		for(Iterator it = links.iterator(); it.hasNext();)
		{
			PhysicalLink mple = (PhysicalLink)it.next();

			removeLinkFromCollector(collector, mple);
		}
	}
	
	protected void removeLinkFromCollector(Collector collector, PhysicalLink mple)
	{
		Identifier creatorId = getLogicalNetLayer().getUserId();

		collector.removePhysicalLink(mple);

		MapElementState state = mple.getState();

		mple.setType(LinkTypeController.getPhysicalLinkType(creatorId, PhysicalLinkType.TUNNEL));

		MapElementStateChangeCommand command = new MapElementStateChangeCommand(mple, state, mple.getState());
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
		
		if(collector.getPhysicalLinks().size() == 0)
			removeCollector(collector);
	}
	
	protected void removeCollector(Collector collector)
	{
		RemoveCollectorCommandAtomic command = new RemoveCollectorCommandAtomic(collector);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
	}

	protected void removeMapElement(MapElement me)
	{
		getLogicalNetLayer().deselectAll();
		me.setSelected(true);
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
	}

	protected void insertSiteInPlaceOfANode(TopologicalNode node, SiteNodeType proto)
	{
		InsertSiteCommandBundle command = new InsertSiteCommandBundle(node, proto);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
	}

	protected void convertUnboundNodeToSite(UnboundNode unbound, SiteNodeType proto)
	{
		if(unbound.isRemoved())
			return;

		CreateSiteCommandAtomic command = new CreateSiteCommandAtomic(proto, unbound.getLocation());
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
		
		SiteNode site = command.getSite();

		BindUnboundNodeToSiteCommandBundle command2 = new BindUnboundNodeToSiteCommandBundle(unbound, site);
		command2.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command2);
		getLogicalNetLayer().getCommandList().execute();
		
		getLogicalNetLayer().repaint(false);
	}

	protected void generatePathCabling(CablePath path, SiteNodeType proto)
	{
		GenerateCablePathCablingCommandBundle command = 
				new GenerateCablePathCablingCommandBundle(path, proto);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint(false);
	}
	
	protected void convertUnboundLinkToPhysicalLink(UnboundLink unbound)
	{
		GenerateUnboundLinkCablingCommandBundle command = 
				new GenerateUnboundLinkCablingCommandBundle(unbound);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();

		getLogicalNetLayer().repaint(false);
	}
}
