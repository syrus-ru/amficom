package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

public class ObjectResourceTreeNode extends DefaultMutableTreeNode
{
	boolean isFinal = false;
	boolean enable = true;
	Object obj;
	String name;
	public boolean expanded = false;
	Object parameter = null;
	ObjectResourceElementLabel label = null;
	
	boolean dragDropEnabled = false;

	public ObjectResourceTreeNode(Object obj, String name, boolean enable)
	{
		super (name, true);
		this.enable = enable;
		this.obj = obj;
		this.name = name;
	}

	public ObjectResourceTreeNode(Object obj, String name, boolean enable, boolean isFinal)
	{
		this(obj, name, enable);
		this.isFinal = isFinal;
	}

	public ObjectResourceTreeNode(Object obj, String name, boolean enable, ImageIcon ii)
	{
		this(obj, name, enable);
		label = new ObjectResourceElementLabel(ii, obj, name, enable);
	}

	public ObjectResourceTreeNode(Object obj, String name, boolean enable, ImageIcon ii, boolean isFinal)
	{
		this(obj, name, enable, ii);
		this.isFinal = isFinal;
	}

	public void setFinal(boolean isFinal)
	{
		this.isFinal = isFinal;
	}

	public boolean isFinal()
	{
		return isFinal;
	}

	public void setDragDropEnabled(boolean dragDropEnabled)
	{
		this.dragDropEnabled = dragDropEnabled;
	}

	public boolean isDragDropEnabled()
	{
		return dragDropEnabled;
	}

	public Object getObject()
	{
		return obj;
	}

	public String getName()
	{
		return name;
	}

	public void setParameter(Object p)
	{
		parameter = p;
	}

	public Object getParameter()
	{
		return parameter;
	}

	public Component getComponent()
	{
		return label;
	}
}
