package com.syrus.AMFICOM.Client.Map.Popup;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Props.MapPropertiesPane;
import com.syrus.AMFICOM.Client.Map.Props.MapPropsManager;
import com.syrus.AMFICOM.Client.Map.UI.MapElementLabel;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNodeType;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class ProtoPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	
	private SiteNodeType proto;
	
	private MapElementLabel lab;

	private static ProtoPopupMenu instance = new ProtoPopupMenu();

	private ProtoPopupMenu()
	{
		super();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ProtoPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.proto = (SiteNodeType )me;
	}

	public void setElementLabel(MapElementLabel lab)
	{
		this.lab = lab;
	}

	private void jbInit() 
	{
		removeMenuItem.setText(LangModelMap.getString("Delete"));
		removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeProto();
				}
			});
		propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
		this.add(removeMenuItem);
		this.addSeparator();
		this.add(propertiesMenuItem);
	}

	private void showProperties()
	{
		ObjectResourcePropertiesPane prop = MapPropsManager.getPropsPane(proto);
		if(prop == null)
			return;
		((MapPropertiesPane )prop).setLogicalNetLayer(logicalNetLayer);
		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModel.getString("Properties"), 
				true, 
				proto,
				prop);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation(
				(screenSize.width - frameSize.width)/2, 
				(screenSize.height - frameSize.height)/2);
				
		dialog.setVisible(true);

		lab.updateIcon();
	}

	private void removeProto()
	{
	}
}
