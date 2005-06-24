/**
 * $Id: MapDropTargetListener.java,v 1.24 2005/06/24 13:04:51 krupenn Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * Обработчик событий drag/drop в окне карты 
 * 
 * 
 * 
 * @version $Revision: 1.24 $, $Date: 2005/06/24 13:04:51 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
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
			try
			{
				if (df[0].getHumanPresentableName().equals("ElementLabel"))
				{
//					SiteNodeType mpe = (SiteNodeType)transferable.getTransferData(df[(0)]);
					Identifier id = (Identifier )transferable.getTransferData(df[(0)]);
					SiteNodeType mpe = (SiteNodeType)StorableObjectPool.getStorableObject(id, false);

					mapElementDropped(mpe, point);
				}
				else
				if (df[0].getHumanPresentableName().equals("SchemeElementLabel"))
				{
					or = transferable.getTransferData(df[0]);

					if(or instanceof SchemeElement)
					{
						SchemeElement se = (SchemeElement )or;

						schemeElementDropped(se, point);
					}
					else
					if(or instanceof SchemeCableLink)
					{
						SchemeCableLink scl = (SchemeCableLink )or;
						schemeCableLinkDropped(scl);
					}
				}
				else
				{
					dtde.rejectDrop();
					return;
				}

				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				dtde.getDropTargetContext().dropComplete(true);
				logicalNetLayer.sendMapEvent(MapEvent.NEED_REPAINT);

//				this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
			}
			catch(Exception e)
			{
				e.printStackTrace();
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
			}
		}
		else
		{
			PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(schemeElement, point);
			cmd.setNetMapViewer(this.netMapViewer);
			logicalNetLayer.getCommandList().add(cmd);
			logicalNetLayer.getCommandList().execute();
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
		}
		else
		{
			SiteNode startNode = logicalNetLayer.getMapView().getStartNode(schemeCableLink);
			SiteNode endNode = logicalNetLayer.getMapView().getEndNode(schemeCableLink);
	
			if(startNode == null || endNode == null)
			{
				JOptionPane.showMessageDialog(
					Environment.getActiveWindow(), 
					"Unable to place scheme cable link",
					"Place nodes first!", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(schemeCableLink);
			cmd.setNetMapViewer(this.netMapViewer);
			logicalNetLayer.getCommandList().add(cmd);
			logicalNetLayer.getCommandList().execute();
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
