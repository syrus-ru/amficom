/**
 * $Id: MapDropTargetListener.java,v 1.16 2005/02/28 16:18:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

/**
 * Обработчик событий drag/drop в окне карты 
 * 
 * 
 * 
 * @version $Revision: 1.16 $, $Date: 2005/02/28 16:18:18 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapDropTargetListener implements DropTargetListener
{
	LogicalNetLayer logicalNetLayer;

	public MapDropTargetListener(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	//Здесь мы получаем объект который пользователь переносит с панели
	public void drop(DropTargetDropEvent dtde)
	{
		Object or = null;

		Point point = dtde.getLocation();

		if ( this.logicalNetLayer.getMapView() != null)
		{
			DataFlavor[] df = dtde.getCurrentDataFlavors();
			Transferable transferable = dtde.getTransferable();
			try
			{
				if (df[0].getHumanPresentableName().equals("ElementLabel"))
				{
					SiteNodeType mpe = (SiteNodeType)transferable.getTransferData(df[(0)]);

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
				this.logicalNetLayer.repaint(false);

				this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	protected void mapElementDropped(SiteNodeType nodeType, Point point)
	{
		CreateSiteCommandAtomic cmd = new CreateSiteCommandAtomic(nodeType, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		this.logicalNetLayer.getCommandList().add(cmd);
		this.logicalNetLayer.getCommandList().execute();
	}

	protected void schemeElementDropped(SchemeElement schemeElement, Point point)
	{
		SiteNode site = this.logicalNetLayer.getMapView().findElement(schemeElement);
		if(site != null)
		{
			if(site instanceof UnboundNode)
			{
				try {
					this.logicalNetLayer.deselectAll();
					site.setSelected(true);
					Point pt = this.logicalNetLayer.convertMapToScreen(site.getLocation());
					MoveSelectionCommandBundle cmd = new MoveSelectionCommandBundle(pt);
					cmd.setLogicalNetLayer(this.logicalNetLayer);
					cmd.setParameter(MoveSelectionCommandBundle.END_POINT, point);
					this.logicalNetLayer.getCommandList().add(cmd);
					this.logicalNetLayer.getCommandList().execute();
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
				site.setSelected(true);
			}
		}
		else
		{
			PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(schemeElement, point);
			cmd.setLogicalNetLayer(this.logicalNetLayer);
			this.logicalNetLayer.getCommandList().add(cmd);
			this.logicalNetLayer.getCommandList().execute();
		}
	}

	protected void schemeCableLinkDropped(SchemeCableLink schemeCableLink)
	{
		CablePath cp = this.logicalNetLayer.getMapView().findCablePath(schemeCableLink);
		if(cp != null)
		{
			cp.setSelected(true);
		}
		else
		{
			SiteNode startNode = this.logicalNetLayer.getMapView().getStartNode(schemeCableLink);
			SiteNode endNode = this.logicalNetLayer.getMapView().getEndNode(schemeCableLink);
	
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
			cmd.setLogicalNetLayer(this.logicalNetLayer);
			this.logicalNetLayer.getCommandList().add(cmd);
			this.logicalNetLayer.getCommandList().execute();
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
