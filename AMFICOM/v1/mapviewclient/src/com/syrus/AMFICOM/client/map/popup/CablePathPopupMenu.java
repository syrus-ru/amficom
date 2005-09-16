package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.props.MapVisualManager;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
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
	
	@Override
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
		this.removeMenuItem.setText(LangModelMap.getString("Delete")); //$NON-NLS-1$
		this.removeMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					removeCablePath();
				}
			});
		this.propertiesMenuItem.setText(LangModelMap.getString("Properties")); //$NON-NLS-1$
		this.propertiesMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperties();
				}
			});
		this.bindMenuItem.setText(LangModelMap.getString("Bind")); //$NON-NLS-1$
		this.bindMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					bind();
				}
			});
		this.generateMenuItem.setText(LangModelMap.getString("GenerateCabling")); //$NON-NLS-1$
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
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}

	void bind()
	{
		StorableObjectEditor prop = MapVisualManager.getVisualManager(this.path).getAdditionalPropertiesPanel();
		if(EditorDialog.showEditorDialog(
				LangModelGeneral.getString("Properties"),  //$NON-NLS-1$
				this.path,
				prop))
		{
			// think about repainting?..
		}
	}

	void generateCabling()
	{
		SiteNodeType proto = super.selectSiteNodeType();
		if(proto != null)
		{
			super.generatePathCabling(this.path, proto);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
	
}
