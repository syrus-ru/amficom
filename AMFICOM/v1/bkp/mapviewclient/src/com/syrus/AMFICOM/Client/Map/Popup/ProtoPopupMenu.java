package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Props.SiteNodeTypePanel;
import com.syrus.AMFICOM.Client.Map.UI.MapElementLabel;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.map.SiteNodeType;

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
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeProto();
				}
			});
		this.propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		this.propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
		this.add(this.removeMenuItem);
		this.addSeparator();
		this.add(this.propertiesMenuItem);
	}

	void showProperties()
	{
		SiteNodeTypePanel prop = SiteNodeTypePanel.getInstance();
		if(prop == null)
			return;
		prop.setLogicalNetLayer(this.logicalNetLayer);
		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModel.getString("Properties"), 
				true, 
				this.proto,
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

		this.lab.updateIcon();
	}

	void removeProto()
	{//empty
	}
}
