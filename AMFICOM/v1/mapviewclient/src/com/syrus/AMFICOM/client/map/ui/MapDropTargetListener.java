/**
 * $Id: MapDropTargetListener.java,v 1.40 2005/09/19 15:37:44 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * Обработчик событий drag/drop в окне карты 
 * 
 * 
 * 
 * @version $Revision: 1.40 $, $Date: 2005/09/19 15:37:44 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public final class MapDropTargetListener implements DropTargetListener
{
	NetMapViewer netMapViewer;

	public MapDropTargetListener(NetMapViewer netMapViewer)
	{
		this.netMapViewer = netMapViewer;
	}

	//Здесь мы получаем объект который пользователь переносит с панели
	public void drop(DropTargetDropEvent dtde)
	{
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		Object or = null;

		Point point = dtde.getLocation();

		if ( logicalNetLayer.getMapView() != null)
		{
			DataFlavor[] df = dtde.getCurrentDataFlavors();
			Transferable transferable = dtde.getTransferable();
			for(int i = 0; i < df.length; i++) {
				try
				{
					if (df[i].getHumanPresentableName().equals("ElementLabel")) //$NON-NLS-1$
					{
						Identifier id = (Identifier )transferable.getTransferData(df[(i)]);
						SiteNodeType mpe = StorableObjectPool.getStorableObject(id, false);
	
						mapElementDropped(mpe, point);
					}
					else
					if (df[i].getHumanPresentableName().equals("IconedTreeUI.object")) //$NON-NLS-1$
					{
						ArrayList items = (ArrayList)transferable.getTransferData(df[i]);
						for(Iterator iter = items.iterator(); iter.hasNext();) {
							or = iter.next();
							
							if(or instanceof SiteNodeType) {
								SiteNodeType snt = (SiteNodeType)or;
								Identifier id = snt.getId();
								SiteNodeType mpe = StorableObjectPool.getStorableObject(id, true);
								mapElementDropped(mpe, point);
							} if(or instanceof SchemeElement) {
								SchemeElement se = (SchemeElement )or;
								Identifier id = se.getId();
								SchemeElement sereal = StorableObjectPool.getStorableObject(id, true);
								schemeElementDropped(sereal, point);
							}
							else if(or instanceof SchemeCableLink) {
								SchemeCableLink scl = (SchemeCableLink )or;
								Identifier id = scl.getId();
								SchemeCableLink sclreal = StorableObjectPool.getStorableObject(id, true);
								schemeCableLinkDropped(sclreal);
							}
							else if(or instanceof Scheme) {
								Scheme sc = (Scheme )or;
								Identifier id = sc.getId();
								Scheme screal = StorableObjectPool.getStorableObject(id, true);
								SchemeElement sereal = screal.getParentSchemeElement();
								schemeElementDropped(sereal, point);
							}
						}
					}
					else
					{
						dtde.rejectDrop();
						return;
					}
	
					dtde.acceptDrop(DnDConstants.ACTION_MOVE);
					dtde.getDropTargetContext().dropComplete(true);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	protected void mapElementDropped(SiteNodeType nodeType, Point point)
	{
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		CreateSiteCommandAtomic cmd = new CreateSiteCommandAtomic(nodeType, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(cmd);
		logicalNetLayer.getCommandList().execute();
		logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
	}

	protected void schemeElementDropped(SchemeElement schemeElement, Point point)
	{
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		MapView mapView = logicalNetLayer.getMapView();
		Map map = mapView.getMap();
		SiteNode site = mapView.findElement(schemeElement);
		if(site != null)
		{
			if(site instanceof UnboundNode)
			{
				try {
					logicalNetLayer.deselectAll();
					map.setSelected(site, true);
					Point pt = logicalNetLayer.getConverter().convertMapToScreen(site.getLocation());
					MoveSelectionCommandBundle cmd = new MoveSelectionCommandBundle(pt);
					cmd.setNetMapViewer(this.netMapViewer);
					cmd.setParameter(MoveSelectionCommandBundle.END_POINT, point);
					logicalNetLayer.getCommandList().add(cmd);
					logicalNetLayer.getCommandList().execute();
					logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
				} catch(MapConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(MapDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				map.setSelected(site, true);
				logicalNetLayer.sendSelectionChangeEvent();
			}
		}
		else
		{
			PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(schemeElement, point);
			cmd.setNetMapViewer(this.netMapViewer);
			logicalNetLayer.getCommandList().add(cmd);
			logicalNetLayer.getCommandList().execute();
			logicalNetLayer.getCommandList().flush();
			logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);

			logicalNetLayer.getMapViewController().scanCables(schemeElement.getParentScheme());
		}
	}

	protected void schemeCableLinkDropped(SchemeCableLink schemeCableLink)
	{
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		MapView mapView = logicalNetLayer.getMapView();
		Map map = mapView.getMap();
		CablePath cablePath = mapView.findCablePath(schemeCableLink);
		if(cablePath != null)
		{
			map.setSelected(cablePath, true);
			logicalNetLayer.sendSelectionChangeEvent();
		}
		else
		{
			SiteNode startNode = logicalNetLayer.getMapView().getStartNode(schemeCableLink);
			SiteNode endNode = logicalNetLayer.getMapView().getEndNode(schemeCableLink);
	
			if(startNode == null || endNode == null)
			{
				JOptionPane.showMessageDialog(
					Environment.getActiveWindow(), 
					LangModelMap.getString("Error.UnableToPlaceCable"), //$NON-NLS-1$
					LangModelMap.getString("Error.PlaceNodesFirst"),  //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(schemeCableLink);
			cmd.setNetMapViewer(this.netMapViewer);
			logicalNetLayer.getCommandList().add(cmd);
			logicalNetLayer.getCommandList().execute();
			logicalNetLayer.getCommandList().execute();
			logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);

			logicalNetLayer.getMapViewController().scanPaths(schemeCableLink.getParentScheme());
		}
	}
/*					
		else
		if(or instanceof SchemePath)
		{
			SchemePath sp = (SchemePath )or;
			logicalNetLayer.getMapView().placeElement(sp);
		}
*/

	public void dragEnter(DropTargetDragEvent dtde)
	{//empty
	}

	public void dragExit(DropTargetEvent dte)
	{//empty
	}

	public void dragOver(DropTargetDragEvent dtde)
	{//empty
	}

	public void dropActionChanged(DropTargetDragEvent dtde)
	{//empty
	}

}
