package com.syrus.AMFICOM.Client.Configure.Map;

import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

public class MapElementLabel extends JLabel
		implements
			MouseListener,
			DragSourceListener,
			DragGestureListener,
			Transferable
{
	DragSource dragSource = null;
	protected int type;
	MapProtoElement sElement = null;
	boolean enable = false;

	protected Color defaultElementLineBorderColor = Color.gray;
	protected Color mouseEnterLineBorederColor = Color.blue;
	protected int mouseEnterLineBorederThickness = 3;

	Border normalBorder = new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.gray);
	Border selectedBorder = new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.red);
//    (new LineBorder(getDefaultElementLineBorderColor (), 1));
//    (new LineBorder(getMouseEnterLineBorederColor(), getMouseEnterLineBorederThickness() ));

	public MapElementLabel()
	{
	}

	public MapElementLabel(ImageIcon myIcon, MapProtoElement sEl)
	{
		sElement = sEl;
	
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
	
		this.setBorder(normalBorder);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setSize(myIcon.getIconWidth(),myIcon.getIconHeight());
		this.setIcon(myIcon);
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
		if (flavor.getHumanPresentableName()=="ElementLabel")
		{
//			System.out.println("The type is " + sElement.getType());
			return (Object) (sElement);
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor myDataFlavor = new DataFlavor(this.getClass(),"ElementLabel");
		System.out.println("myDataFlavor "+myDataFlavor.getHumanPresentableName());
		DataFlavor[] dfs = new DataFlavor[2];
		dfs[0] = myDataFlavor;
//	    dfs[1] = DataFlavor.plainTextFlavor;
		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();
		return dfs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		System.out.println("support myDataFlavor " + (flavor.getHumanPresentableName()=="ElementLabel"));
		return (flavor.getHumanPresentableName()=="ElementLabel");
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

	public void setEnableMapElemenetLabel(boolean b)
	{
		enable = b;
		setEnabled(b);
	}

	public boolean getEnableMapElemenetLabel()
	{
		return enable;
	}
}
