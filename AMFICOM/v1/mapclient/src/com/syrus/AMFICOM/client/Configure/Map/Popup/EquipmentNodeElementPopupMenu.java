package com.syrus.AMFICOM.Client.Configure.Map.Popup;


import com.syrus.AMFICOM.Client.Configure.Map.UI.MapElementPropertiesDialog;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.MapEquipmentNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class EquipmentNodeElementPopupMenu 
		extends JPopupMenu 
		implements MyPopupMenu
{
  JMenuItem menuDelete = new JMenuItem();
  JMenuItem menuSelect = new JMenuItem();
  JMenuItem menuCopy = new JMenuItem();
  JMenuItem menuScheme = new JMenuItem();
  JMenuItem menuElement = new JMenuItem();
  JMenuItem menuProperties = new JMenuItem();

  JFrame mainFrame;
  MapNodeElement myNode;
  ApplicationContext aContext;
  String scheme_id = "";
  String element_id = "";

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

  public EquipmentNodeElementPopupMenu(JFrame myFrame, MapNodeElement node)
  {
    super();
    try
    {
      mainFrame = myFrame;
      myNode = node;
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
	try
	{
		MapEquipmentNodeElement en = (MapEquipmentNodeElement )node;
		if(en.element_id != null)
		{
			SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, en.element_id);
			if(se != null)
			{
				if(se.scheme_id != null)
					if(!se.scheme_id.equals(""))
					{
						scheme_id = se.scheme_id;
						this.add(menuScheme);
					}
				if(se.element_ids != null)
					if(!se.element_ids.isEmpty())
					{
						element_id = se.getId();
						this.add(menuElement);
					}
			}
		}
	}
	catch(Exception ex)
	{
	}
	
  }

  private void jbInit() throws Exception
  {
      menuDelete = new JMenuItem(LangModelMap.Text("EquipmentElementPopupMenuDelete"));
      menuSelect = new JMenuItem(LangModelMap.Text("EquipmentElementPopupMenuChoose"));
      menuCopy = new JMenuItem(LangModelMap.Text("EquipmentElementPopupMenuCopy"));
      menuScheme = new JMenuItem(LangModelMap.Text("EquipmentElementPopupMenuScheme"));
      menuElement = new JMenuItem(LangModelMap.Text("EquipmentElementPopupMenuElement"));
      menuProperties = new JMenuItem(LangModelMap.Text("EquipmentElementPopupMenuProperties"));

		menuDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					menuDelete_actionPerformed(e);
				}
			});
		menuCopy.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					menuCopy_actionPerformed(e);
				}
			});
		menuScheme.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					menuScheme_actionPerformed(e);
				}
			});
		menuElement.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					menuElement_actionPerformed(e);
				}
			});
    menuProperties.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
					menuProperties_actionPerformed(e);
      }
    });
//    this.add(menuDelete);
//    this.add(menuSelect);
//    this.add(menuCopy);
//    this.add(menuScheme);
    this.add(menuProperties);
  }

	void menuProperties_actionPerformed(ActionEvent e)
	{

		MapElementPropertiesDialog dialog = new MapElementPropertiesDialog(
			mainFrame, 
			aContext, 
			"Hello",
			true, 
			myNode);

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		dialog.show();
	}

	private void menuCopy_actionPerformed(ActionEvent e)
	{
		try
		{
			MapEquipmentNodeElement mene = (MapEquipmentNodeElement )myNode;
			SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, mene.element_id);
			ElementAttribute ea = (ElementAttribute )se.attributes.get("optimizerAttribute");
			ea.setValue("mandatory");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
//		myNode.optimizerAttribute = ;
	}

	private void menuScheme_actionPerformed(ActionEvent e)
	{
		Environment.the_dispatcher.notify(new OperationEvent(scheme_id, 0, "mapaddschemeevent"));
		if(myNode.getAlarmState())
		{
			for(int i = 0; i < myNode.getMapContext().markers.size(); i++)
			try 
			{
				MapAlarmMarker mar = (MapAlarmMarker )myNode.getMapContext().markers.get(i);
				aContext.getDispatcher().notify(new SchemeElementsEvent(
					this,
					mar.link_id, 
					SchemeElementsEvent.CREATE_ALARMED_LINK_EVENT));
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			} 
		}
	}

	private void menuElement_actionPerformed(ActionEvent e)
	{
		Environment.the_dispatcher.notify(new OperationEvent(element_id, 0, "mapaddschemeelementevent"));
		if(myNode.getAlarmState())
		{
			for(int i = 0; i < myNode.getMapContext().markers.size(); i++)
			try 
			{
				MapAlarmMarker mar = (MapAlarmMarker )myNode.getMapContext().markers.get(i);
				aContext.getDispatcher().notify(new SchemeElementsEvent(
					this,
					mar.link_id, 
					SchemeElementsEvent.CREATE_ALARMED_LINK_EVENT));
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			} 
		}
	}

	private void menuDelete_actionPerformed(ActionEvent e)
	{
		try
		{
			MapEquipmentNodeElement mene = (MapEquipmentNodeElement )myNode;
			SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, mene.element_id);
			ElementAttribute ea = (ElementAttribute )se.attributes.get("optimizerAttribute");
			ea.setValue("restricted");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
//		myNode.optimizerAttribute = "restricted";
	}

}