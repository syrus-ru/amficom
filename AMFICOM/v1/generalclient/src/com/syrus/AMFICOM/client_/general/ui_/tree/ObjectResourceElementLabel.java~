package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;

import javax.swing.*;


public class ObjectResourceElementLabel extends JLabel
		implements
			DragSourceListener,
			DragGestureListener
{
	DragSource dragSource = null;
	protected int type;
	Object or = null;
	String name = "";
	ImageIcon myIcon = null;
	boolean enable = true;

	protected ObjectResourceElementLabel()
	{
	}

	public ObjectResourceElementLabel(ImageIcon myIcon, Object or, String name, boolean enable)
	{
		this.enable = enable;
		this.or = or;
		this.myIcon = myIcon;

		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);

		this.setText(name);

		if (myIcon != null)
			this.setIcon(myIcon);

		this.setEnabled(enable);
		this.setOpaque(false);
	}

	public Image getImage()
	{
		if (myIcon == null)
			return null;

		return myIcon.getImage();
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
		if ( enable )
		{
			try
			{
				Transferable tr = (Transferable )or;
				dragSource.startDrag (event, DragSource.DefaultMoveDrop, tr, this);
//				System.out.println("start dragging " + tr);
//        		event.startDrag(DragSource.DefaultMoveDrop, tr);
			}
			catch (Exception ex)
			{
				System.out.println("OR is not Transferable");
			}
		}
	}
}
