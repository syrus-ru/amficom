/**
 * $Id: MapElementLabel.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;

import java.awt.Color;
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
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * Визуальный компонент на панели компонентов, с помощью которых создаются
 * узла (посредством drag/drop)
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapElementLabel extends JLabel
		implements
			MouseListener,
			DragSourceListener,
			DragGestureListener,
			Transferable
{
	DragSource dragSource = null;
	protected int type;
	MapNodeProtoElement sElement = null;
	boolean enable = false;

	protected Color defaultElementLineBorderColor = Color.gray;
	protected Color mouseEnterLineBorederColor = Color.blue;
	protected int mouseEnterLineBorederThickness = 3;

	Border normalBorder = new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.gray);
	Border selectedBorder = new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.red);

	public MapElementLabel(ImageIcon icon, MapNodeProtoElement sEl)
	{
		sElement = sEl;
	
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
	
		this.setBorder(normalBorder);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setSize(icon.getIconWidth(),icon.getIconHeight());
		this.setIcon(icon);
		this.addMouseListener(this);
		this.setEnabled(enable);
	}

	public void dragDropEnd(DragSourceDropEvent dsde)
	{
	}

	public void dragEnter(DragSourceDragEvent dsde)
	{
	}

	public void dragExit(DragSourceEvent dse)
	{
	}

	public void dragOver(DragSourceDragEvent dsde)
	{
	}

	public void dropActionChanged(DragSourceDragEvent dsde)
	{
	}

	public void dragGestureRecognized( DragGestureEvent event)
	{
		if (enable)
			dragSource.startDrag (event, DragSource.DefaultMoveDrop, sElement, this);
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
		this.setBorder(selectedBorder);
//		Point p = this.getLocation();
//		this.setLocation(p.x - 1, p.y - 1);
	}

	public void mouseExited(MouseEvent e)
	{
		this.setBorder(normalBorder);
//		Point p = this.getLocation();
//		this.setLocation(p.x + 1, p.y + 1);
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public Object getTransferData(DataFlavor flavor)
	{
		if (flavor.getHumanPresentableName() == "ElementLabel")
		{
//			System.out.println("The type is " + sElement.getType());
			return sElement;
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor dataFlavor = new DataFlavor(this.getClass(), "ElementLabel");
		System.out.println("dataFlavor " + dataFlavor.getHumanPresentableName());
		DataFlavor[] dfs = new DataFlavor[2];
		dfs[0] = dataFlavor;
//	    dfs[1] = DataFlavor.plainTextFlavor;
		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();
		return dfs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		System.out.println("support DataFlavor " + (flavor.getHumanPresentableName() == "ElementLabel"));
		return (flavor.getHumanPresentableName() == "ElementLabel");
	}

	public Color getDefaultElementLineBorderColor ()
	{
		return defaultElementLineBorderColor;
	}

	public void setDefaultElementLineBorderColor (Color color)
	{
		defaultElementLineBorderColor = color;
	}

	public Color getMouseEnterLineBorederColor ()
	{
		return mouseEnterLineBorederColor;
	}

	public void setMouseEnterLineBorederColor (Color color)
	{
		mouseEnterLineBorederColor = color;
	}

	public int getMouseEnterLineBorederThickness ()
	{
		return mouseEnterLineBorederThickness;
	}

	public void setMouseEnterLineBorederThickness (int size)
	{
		mouseEnterLineBorederThickness = size;
	}

	public void setEnabled(boolean b)
	{
		enable = b;
		super.setEnabled(b);
	}

	public boolean getEnabled()
	{
		return enable;
	}
}
