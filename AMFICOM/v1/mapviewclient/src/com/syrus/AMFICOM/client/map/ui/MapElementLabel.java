/**
 * $Id: MapElementLabel.java,v 1.10 2005/03/10 17:45:37 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.Popup.ProtoPopupMenu;
import com.syrus.AMFICOM.map.SiteNodeType;

import java.awt.Color;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * Визуальный компонент на панели компонентов, с помощью которых создаются
 * узла (посредством drag/drop)
 * 
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2005/03/10 17:45:37 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapElementLabel extends JLabel
		implements
			MouseListener,
			DragSourceListener,
			DragGestureListener,
			Transferable
{
	public static final String DATA_FLAVOUR = "ElementLabel";

	DragSource dragSource = null;
	protected int type;
	SiteNodeType proto = null;
	NodeTypeController ntc;
	boolean enable = false;

	protected Color defaultElementLineBorderColor = Color.gray;
	protected Color mouseEnterLineBorederColor = Color.blue;
	protected int mouseEnterLineBorederThickness = 3;

	Border normalBorder = new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.gray);
	Border selectedBorder = new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.red);

	static final int ELEMENT_DIMENSION = 30;

	public MapElementLabel(SiteNodeType proto)
	{
		this.proto = proto;
		this.ntc = (NodeTypeController )NodeTypeController.getInstance();
//		this.ntc = (NodeTypeController )MapFrame
//			.getMapMainFrame()
//				.getMapViewer()
//					.getLogicalNetLayer()
//						.getMapViewController()
//							.getController(proto);
		
		updateIcon();
	
		this.dragSource = new DragSource();
		this.dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
	
		this.setBorder(this.normalBorder);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.addMouseListener(this);
		this.setEnabled(this.enable);
	}

	public void updateIcon()
	{
		ImageIcon icon = new ImageIcon(
				MapPropertiesManager.getImage(this.proto.getImageId()).getScaledInstance(
				ELEMENT_DIMENSION, 
				ELEMENT_DIMENSION, 
				Image.SCALE_SMOOTH));
		this.setSize(icon.getIconWidth(),icon.getIconHeight());
		this.setIcon(icon);
		this.updateUI();
	}

	public void dragDropEnd(DragSourceDropEvent dsde)
	{//empty
	}

	public void dragEnter(DragSourceDragEvent dsde)
	{//empty
	}

	public void dragExit(DragSourceEvent dse)
	{//empty
	}

	public void dragOver(DragSourceDragEvent dsde)
	{//empty
	}

	public void dropActionChanged(DragSourceDragEvent dsde)
	{//empty
	}

	public void dragGestureRecognized( DragGestureEvent event)
	{
		if (this.enable)
			this.dragSource.startDrag (event, DragSource.DefaultMoveDrop, this, this);
	}

	public void mouseClicked(MouseEvent e)
	{
		if(SwingUtilities.isRightMouseButton(e))
		{
			ProtoPopupMenu menu = ProtoPopupMenu.getInstance();
			menu.setElementLabel(this);
			menu.setElement(this.proto);
			menu.show(this, e.getPoint().x, e.getPoint().y);
		}
	}

	public void mouseEntered(MouseEvent e)
	{
		this.setBorder(this.selectedBorder);
//		Point p = this.getLocation();
//		this.setLocation(p.x - 1, p.y - 1);
	}

	public void mouseExited(MouseEvent e)
	{
		this.setBorder(this.normalBorder);
//		Point p = this.getLocation();
//		this.setLocation(p.x + 1, p.y + 1);
	}

	public void mousePressed(MouseEvent e)
	{//empty
	}

	public void mouseReleased(MouseEvent e)
	{//empty
	}

	public Object getTransferData(DataFlavor flavor)
	{
		if (flavor.getHumanPresentableName().equals(DATA_FLAVOUR))
		{
//			System.out.println("The type is " + sElement.getType());
			return this.proto.getId();
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor dataFlavor = new DataFlavor(this.getClass(), DATA_FLAVOUR);
		DataFlavor[] dfs = new DataFlavor[2];
		dfs[0] = dataFlavor;
//	    dfs[1] = DataFlavor.plainTextFlavor;
		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();
		return dfs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return (flavor.getHumanPresentableName().equals(DATA_FLAVOUR));
	}

	public void setEnabled(boolean b)
	{
		this.enable = b;
		super.setEnabled(b);
	}

	public boolean getEnabled()
	{
		return this.enable;
	}
}
