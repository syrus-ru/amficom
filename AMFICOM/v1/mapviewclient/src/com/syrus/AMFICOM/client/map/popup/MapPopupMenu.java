/**
 * $Id: MapPopupMenu.java,v 1.9 2004/10/11 16:48:33 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceSelectionDialog;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateCollectorCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.DeleteSelectionCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.GenerateCablePathCablingCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.InsertSiteCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MapElementStateChangeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveCollectorCommandAtomic;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Props.MapPropsManager;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * Контекстное меню элемента карты
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/10/11 16:48:33 $
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

	public void showProperties(MapElement me)
	{
		ObjectResourcePropertiesPane prop = MapPropsManager.getPropsPane(me);
		if(prop == null)
			return;
		prop.setContext(logicalNetLayer.getContext());
		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModel.getString("Properties"), 
				true, 
				(ObjectResource )me,
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
		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
			Dispatcher disp = logicalNetLayer.getContext().getDispatcher();
			if(disp != null)
				disp.notify(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
	}

	public abstract void setMapElement(MapElement me);

	protected MapPipePathElement selectCollector()
	{
		MapPipePathElement collector = null;

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
			collector = (MapPipePathElement )dialog.getSelected();
		}
		
		return collector;
	}
	
	protected MapNodeProtoElement selectNodeProto()
	{
		MapNodeProtoElement proto = null;

		List list = logicalNetLayer.getTopologicalProtos();

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
			proto = (MapNodeProtoElement )dialog.getSelected();
		}
		
		return proto;
	}

	protected MapSiteNodeElement selectSiteNode()
	{
		MapSiteNodeElement site = null;

		List list = new ArrayList();
		for(Iterator it = getLogicalNetLayer().getMapView().getMap().getMapSiteNodeElements().iterator(); it.hasNext();)
		{
			MapSiteNodeElement s = (MapSiteNodeElement )it.next();
			if(!( s instanceof MapUnboundNodeElement))
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
			site = (MapSiteNodeElement )dialog.getSelected();
		}
		return site;
	}

	protected MapPipePathElement createCollector()
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
	
	protected void addLinksToCollector(MapPipePathElement collector, List links)
	{
		for(Iterator it = links.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )it.next();

			addLinkToCollector(collector, mple);
		}
	}
	
	protected void addLinkToCollector(MapPipePathElement collector, MapPhysicalLinkElement mple)
	{
		MapPipePathElement prevCollector = logicalNetLayer.getMapView().getMap().getCollector(mple);
		if(prevCollector != null)
			prevCollector.removeLink(mple);
	
		collector.addLink(mple);

		MapElementState state = mple.getState();
		mple.setMapProtoId(MapLinkProtoElement.COLLECTIOR);

		MapElementStateChangeCommand command2 = new MapElementStateChangeCommand(mple, state, mple.getState());
		command2.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command2);
		getLogicalNetLayer().getCommandList().execute();
	}

	protected void removeLinksFromCollector(MapPipePathElement collector, List links)
	{
		for(Iterator it = links.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )it.next();

			removeLinkFromCollector(collector, mple);
		}
	}
	
	protected void removeLinkFromCollector(MapPipePathElement collector, MapPhysicalLinkElement mple)
	{
		collector.removeLink(mple);

		MapElementState state = mple.getState();

		mple.setMapProtoId(MapLinkProtoElement.TUNNEL);

		MapElementStateChangeCommand command = new MapElementStateChangeCommand(mple, state, mple.getState());
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
		
		if(collector.getLinks().size() == 0)
			removeCollector(collector);
	}
	
	protected void removeCollector(MapPipePathElement collector)
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

	protected void insertSiteInPlaceOfANode(MapPhysicalNodeElement node, MapNodeProtoElement proto)
	{
		InsertSiteCommand command = new InsertSiteCommand(node, proto);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
	}

	protected void convertUnboundNodeToSite(MapUnboundNodeElement unbound, MapNodeProtoElement proto)
	{
		CreateSiteCommand command = new CreateSiteCommand(proto, unbound.getAnchor());
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
		
		MapSiteNodeElement site = command.getSite();

		BindUnboundNodeToSiteCommandBundle command2 = new BindUnboundNodeToSiteCommandBundle(unbound, site);
		command2.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command2);
		getLogicalNetLayer().getCommandList().execute();
		
		getLogicalNetLayer().repaint();
	}

	protected void generatePathCabling(MapCablePathElement path, MapNodeProtoElement proto)
	{
//		MapNodeProtoElement proto = (MapNodeProtoElement )Pool.get(
//				MapNodeProtoElement.typ, 
//				MapNodeProtoElement.WELL);

		GenerateCablePathCablingCommandBundle command = 
				new GenerateCablePathCablingCommandBundle(path, proto);
		command.setLogicalNetLayer(logicalNetLayer);
		getLogicalNetLayer().getCommandList().add(command);
		getLogicalNetLayer().getCommandList().execute();
	}
}
