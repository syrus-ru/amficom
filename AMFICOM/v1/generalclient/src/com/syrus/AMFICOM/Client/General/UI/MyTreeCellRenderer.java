package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.Resource.ObjectHierarchy;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Component;

import java.lang.reflect.Field;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer 
{
	public MyTreeCellRenderer()
	{
		super();
		closedIcon = null;
		openIcon = null;
		leafIcon = null;
	}

	public Component getTreeCellRendererComponent(
			JTree tree, 
			Object value,
			boolean selected, 
			boolean expanded, 
			boolean leaf, 
			int row, 
			boolean hasFocus)
	{
		Object obj = ((DefaultMutableTreeNode) value).getUserObject();

		String text;
		if(obj instanceof String)
//			text = (String )obj;
			text = ObjectHierarchy.getNodeName((String )obj);
		else
		if(obj instanceof ObjectResource)
		{
			try
			{
//				Class cl = obj.getClass();
//				Field nameField = cl.getField("name");
//				text = nameField.toString();
				text = ((ObjectResource )obj).getName();
			}
			catch(Exception e)
			{
				text = "noname";
			}
		}
		else
			text = "error";

//		System.out.println("tree renderer for " + obj.toString() + " == " + text);

		return super.getTreeCellRendererComponent(
				tree,
				text,
				selected, 
				expanded, 
				leaf, 
				row, 
				hasFocus);
  }	
}