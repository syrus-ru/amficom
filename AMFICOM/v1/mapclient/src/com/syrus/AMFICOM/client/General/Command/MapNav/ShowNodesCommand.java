package com.syrus.AMFICOM.Client.General.Command.MapNav;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.Map.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;

public class ShowNodesCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	AbstractButton button;
	ApplicationModel aModel;

	public ShowNodesCommand(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("button"))
			button = (AbstractButton )value;
		if(field.equals("applicationModel"))
			aModel = (ApplicationModel )value;
	}

	public Object clone()
	{
		Command com = new ShowNodesCommand(logicalNetLayer);
		com.setParameter("button", button);
		return com;
	}

	public void execute()
	{
		boolean showPhysicalNodeElement = false;
	
		if(aModel.isSelected("mapModeViewNodes"))
		{
			button.setIcon(new ImageIcon("images/nodes_invisible.gif"));
			aModel.setSelected("mapModeViewNodes", false);
			aModel.fireModelChanged("");
		}
		else
		if(!aModel.isSelected("mapModeViewNodes"))
		{
			button.setIcon(new ImageIcon("images/nodes_visible.gif"));
			aModel.setSelected("mapModeViewNodes", true);
			aModel.fireModelChanged("");
		}

		logicalNetLayer.getMapContext().setPhysicalNodeElementVisibility( aModel.isSelected("mapModeViewNodes"));
		logicalNetLayer.postDirtyEvent();
		logicalNetLayer.postPaintEvent();
	}
}