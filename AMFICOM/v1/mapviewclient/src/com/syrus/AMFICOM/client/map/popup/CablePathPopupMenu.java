package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Props.MapCablePathPane;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;

public class CablePathPopupMenu extends MapPopupMenu 
{
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem propertiesMenuItem = new JMenuItem();
	private JMenuItem bindMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();
	
	private CablePath path;

	private static CablePathPopupMenu instance = new CablePathPopupMenu();

	private CablePathPopupMenu()
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
	
	public static CablePathPopupMenu getInstance()
	{
		return instance;
	}
	
	public void setElement(Object me)
	{
		this.path = (CablePath)me;
		
		boolean canGenerate = false;
		for(Iterator it = this.path.getLinks().iterator(); it.hasNext();)
		{
			Object link = it.next();
			if(link instanceof UnboundLink)
				canGenerate = true;
		}
		this.generateMenuItem.setVisible(canGenerate);
	}

	private void jbInit() 
	{
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeCablePath();
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
		this.bindMenuItem.setText(LangModelMap.getString("Bind"));
		this.bindMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					bind();
				}
			});
		this.generateMenuItem.setText(LangModelMap.getString("GenerateCabling"));
		this.generateMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					generateCabling();
				}
			});
		this.add(this.removeMenuItem);
		this.add(this.bindMenuItem);
		this.add(this.generateMenuItem);
		this.addSeparator();
		this.add(this.propertiesMenuItem);
	}

	void showProperties()
	{
		super.showProperties(this.path);
	}

	void removeCablePath()
	{
		super.removeMapElement(this.path);

		getLogicalNetLayer().repaint(false);
	}

	void bind()
	{
		MapCablePathPane prop = (MapCablePathPane )MapCablePathPane.getInstance();
		prop.setContext(this.logicalNetLayer.getContext());
		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModel.getString("Properties"), 
				true, 
				this.path,
				prop);

		prop.showBindPanel();

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

		if ( dialog.ifAccept())
		{// think about repainting?..
		}
	}

	void generateCabling()
	{
		SiteNodeType proto = super.selectNodeProto();
		if(proto != null)
		{
			super.generatePathCabling(this.path, proto);
			getLogicalNetLayer().repaint(false);
		}
	}
	
}
