package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.UgoPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class MapSiteBindPanel extends JPanel implements ObjectResourcePropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	MapSiteNodeElement site;
	private JLabel titleLabel = new JLabel();
	private ObjectResourceListBox elementsList = new ObjectResourceListBox();

	private JPanel jPanel1 = new JPanel();
	private JButton bindButton = new JButton();
	private JButton unbindButton = new JButton();

	private UgoPanel schemePanel = new UgoPanel(null);

	public MapSiteBindPanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void jbInit()
	{
		this.setLayout(gridBagLayout1);
		this.setName(LangModelMap.getString("SiteBinding"));
		titleLabel.setText(LangModelMap.getString("SiteBinding"));

		elementsList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					showElement((SchemeElement )elementsList.getSelectedObjectResource());
				}
			});

		bindButton.setText("Привязать");
		bindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ObjectResource or = elementsList.getSelectedObjectResource();
//					bind(or);
				}
			});
		unbindButton.setText("Отвязать");
		unbindButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ObjectResource or = elementsList.getSelectedObjectResource();
					unbind(or);
				}
			});
		jPanel1.add(bindButton, null);
		jPanel1.add(unbindButton, null);

		schemePanel.getGraph().setGraphEditable(false);

		this.add(titleLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, null, 0, 0));
		this.add(elementsList, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 100, 150));
		this.add(Box.createVerticalGlue(), com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, null, 10, 150));
		this.add(schemePanel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
//		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, null, 0, 0));
		this.add(jPanel1, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
	}

	public ObjectResource getObjectResource()
	{
		return null;
	}

	public void unbind(ObjectResource or)
	{
		SchemeElement se = (SchemeElement )or;
		se.siteId = "";
		elementsList.remove(se);
	}

	public void showElement(SchemeElement se)
	{
//		schemePanel.openSchemeElement(se);
	}

	public void setObjectResource(ObjectResource objectResource)
	{
		elementsList.removeAll();
		site = (MapSiteNodeElement )objectResource;
		if(site == null)
		{
			elementsList.setEnabled(false);
			schemePanel.getGraph().removeAll();
		}
		else
		{
			elementsList.setEnabled(true);
			List list = Pool.getList(SchemeElement.typ);
			if(list != null)
			{
				for(Iterator it = list.iterator(); it.hasNext();)
				{
					SchemeElement se = (SchemeElement )it.next();
					if(se.siteId.equals(site.getId()))
						elementsList.add(se);
				}
			}
		}
	}

	public void setContext(ApplicationContext aContext)
	{
//		schemePanel.setContext(aContext);
	}

	public boolean modify()
	{
		try 
		{
			return true;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
		
		return false;
	}

	public boolean create()
	{
		return false;
	}

	public boolean delete()
	{
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean save()
	{
		return false;
	}

	public boolean cancel()
	{
		return false;
	}
}
