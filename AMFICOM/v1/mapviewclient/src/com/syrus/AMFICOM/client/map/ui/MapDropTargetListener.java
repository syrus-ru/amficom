/**
 * $Id: MapDropTargetListener.java,v 1.10 2005/01/14 15:03:13 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

/**
 * Обработчик событий drag/drop в окне карты 
 * 
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2005/01/14 15:03:13 $
 * @module
 * @author $Author: krupenn $
 * @see
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

		if ( logicalNetLayer.getMapView() != null)
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
					or = (Object )transferable.getTransferData(df[0]);

					if(or instanceof SchemeElement)
					{
						SchemeElement se = (SchemeElement )or;

						schemeElementDropped(se, point);
					}
					else
					if(or instanceof SchemeCableLink)
					{
						SchemeCableLink scl = (SchemeCableLink )or;
						schemeCableLinkDropped(scl, point);
					}
				}
				else
				{
					dtde.rejectDrop();
					return;
				}

				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				dtde.getDropTargetContext().dropComplete(true);
				logicalNetLayer.repaint(false);

				logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	protected void mapElementDropped(SiteNodeType mpe, Point point)
	{
		CreateSiteCommandAtomic cmd = new CreateSiteCommandAtomic(mpe, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(cmd);
		logicalNetLayer.getCommandList().execute();
	}

	protected void schemeElementDropped(SchemeElement se, Point point)
	{
		SiteNode site = logicalNetLayer.getMapView().findElement(se);
		if(site != null)
		{
			if(site instanceof UnboundNode)
			{
				logicalNetLayer.deselectAll();
				site.setSelected(true);
				Point pt = logicalNetLayer.convertMapToScreen(site.getLocation());
				MoveSelectionCommandBundle cmd = new MoveSelectionCommandBundle(pt);
				cmd.setLogicalNetLayer(logicalNetLayer);
				cmd.setParameter(MoveSelectionCommandBundle.END_POINT, point);
				logicalNetLayer.getCommandList().add(cmd);
				logicalNetLayer.getCommandList().execute();
			}
			else
			{
				site.setSelected(true);
			}
		}
		else
		{
			PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(se, point);
			cmd.setLogicalNetLayer(logicalNetLayer);
			logicalNetLayer.getCommandList().add(cmd);
			logicalNetLayer.getCommandList().execute();
		}
	}

	protected void schemeCableLinkDropped(SchemeCableLink scl, Point point)
	{
		CablePath cp = logicalNetLayer.getMapView().findCablePath(scl);
		if(cp != null)
		{
			cp.setSelected(true);
		}
		else
		{
			SiteNode[] mne = logicalNetLayer.getMapView().getSideNodes(scl);
	
			if(mne[0] == null || mne[1] == null)
			{
				JOptionPane.showMessageDialog(
					Environment.getActiveWindow(), 
					"Unable to place scheme cable link",
					"Place nodes first!", 
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
			cmd.setLogicalNetLayer(logicalNetLayer);
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
	{
	}

	public void dragExit(DropTargetEvent dte)
	{
	}

	public void dragOver(DropTargetDragEvent dtde)
	{
	}

	public void dropActionChanged(DropTargetDragEvent dtde)
	{
	}

}
