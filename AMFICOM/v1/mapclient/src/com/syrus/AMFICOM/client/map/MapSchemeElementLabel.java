package com.syrus.AMFICOM.Client.Map;

import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

//A0A
public class MapSchemeElementLabel extends JLabel
		implements 
//			MouseListener,
			DragSourceListener,
			DragGestureListener
//			Transferable
{
	DragSource dragSource = null;
	protected int type;
	ObjectResource sElement = null;
	boolean enable = true;

	protected MapSchemeElementLabel()
	{
	}

	public MapSchemeElementLabel(ImageIcon myIcon, ObjectResource sEl)
	{
		sElement = sEl;
	
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
	
		this.setText(sEl.getName());
		this.setIcon(myIcon);
		this.setEnabled(enable);
		this.setOpaque(false);
	}

	public void dragDropEnd(DragSourceDropEvent dsde)
	{
//	System.out.println("dragDropEnd");
	}

	public void dragEnter(DragSourceDragEvent dsde)
	{
//	System.out.println("dragEnter");
	}

	public void dragExit(DragSourceEvent dse)
	{
//	System.out.println("dragExit");
	}

	public void dragOver(DragSourceDragEvent dsde)
	{
//	System.out.println("dragOver");
	}

	public void dropActionChanged(DragSourceDragEvent dsde)
	{
///	System.out.println("dropActionChanged");
	}

	public void dragGestureRecognized( DragGestureEvent event)
	{
//	System.out.println("dragGestureRecognized");
		if ( enable )
		{
//	System.out.println("enabled " + enable);
			if(sElement instanceof SchemeElement)
				dragSource.startDrag (event, DragSource.DefaultMoveDrop, (SchemeElement )sElement, this);
			else
			if(sElement instanceof SchemeCableLink)
				dragSource.startDrag (event, DragSource.DefaultMoveDrop, (SchemeCableLink )sElement, this);
			else
			if(sElement instanceof SchemePath)
				dragSource.startDrag (event, DragSource.DefaultMoveDrop, (SchemePath )sElement, this);
		}
	}

}
