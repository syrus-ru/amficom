package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

public class ObjectResourceTreeNode extends DefaultMutableTreeNode
{
	public boolean expanded = false;

	boolean isFinal = false;

	private boolean enable = true;
	private Object obj;
	private String name;
	private Object parameter = null;

	private JPanel renderer = new JPanel();
	private ObjectResourceElementLabel label = null;

	boolean dragDropEnabled = false;

	public ObjectResourceTreeNode(Object obj, String name, boolean enable)
	{
		super (name, true);
		this.enable = enable;
		this.obj = obj;
		this.name = name;

		label = new ObjectResourceElementLabel(null, obj, name, enable);
		renderer.setLayout(new BorderLayout());
		renderer.add(label,BorderLayout.CENTER);
	}

	public ObjectResourceTreeNode(Object obj, String name, boolean enable, boolean isFinal)
	{
		this(obj, name, enable);
		this.isFinal = isFinal;
	}

	public ObjectResourceTreeNode(Object obj, String name, boolean enable, ImageIcon ii)
	{
		super (name, true);
		this.enable = enable;
		this.obj = obj;
		this.name = name;

		label = new ObjectResourceElementLabel(ii, obj, name, enable);
		renderer.setLayout(new BorderLayout());
		renderer.add(label,BorderLayout.CENTER);
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

	public JComponent getComponent()
	{
		return renderer;
	}

	public void setComponent(JPanel comp)
	{
	  renderer = comp;
	}

	public void setForeground(Color fc)
	{
		label.setForeground(fc);
	}

	public void setBackground(Color bc)
	{
		renderer.setBackground(bc);
	}

	public void setIcon(ImageIcon icon)
	{
		label.setIcon(icon);
	}

	ObjectResourceElementLabel getElementLabel()
	{
		return label;
	}
}
