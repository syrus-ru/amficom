package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class CatalogElementsPanel extends JPanel
{
	private ObjectResourceListBox elementsList = new ObjectResourceListBox();
	private ObjectResourceListBox catalogList = new ObjectResourceListBox();
	private JTextField catalogElementNameField = new JTextField();
	private JLabel componentNameLabel = new JLabel();

	ObjectResource schemeElement_selected;
	Hashtable selected_ors = new Hashtable();
	private boolean skip = false;
	ApplicationContext aContext;

	public CatalogElementsPanel(ApplicationContext aContext)
	{
		this.aContext = aContext;
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		setLayout(new BorderLayout());

//		catalogElementNameField.setBackground(Color.lightGray);
		componentNameLabel.setText("Название элемента в каталоге:");

		JPanel catalogPanel = new JPanel(new BorderLayout());
		JScrollPane catalogScrollPane = new JScrollPane(catalogList);
		catalogPanel.add(new JLabel("Элементы в каталоге:"), BorderLayout.NORTH);
		catalogPanel.add(catalogScrollPane, BorderLayout.CENTER);

		JPanel elementsPanel = new JPanel(new BorderLayout());
		JScrollPane elementsScrollPane = new JScrollPane(elementsList);
		elementsPanel.add(new JLabel("Элементы на схеме:"), BorderLayout.NORTH);
		elementsPanel.add(elementsScrollPane, BorderLayout.CENTER);

		elementsList.setAutoscrolls(true);

		JPanel main = new JPanel (new GridLayout());
		main.add(elementsPanel);
		main.add(catalogPanel);

		JPanel catalogElementNamePanel = new JPanel(new BorderLayout());
		catalogElementNamePanel.add(componentNameLabel, BorderLayout.NORTH);
		catalogElementNamePanel.add(catalogElementNameField, BorderLayout.CENTER);

		add(catalogElementNamePanel, BorderLayout.NORTH);
		add(main, BorderLayout.CENTER);

		elementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		elementsList.addMouseListener(new MouseListener()
		{
			public void mouseEntered(MouseEvent e)
					{}
			public void mouseExited(MouseEvent e)
					{}
			public void mousePressed(MouseEvent e)
					{}
			public void mouseReleased(MouseEvent e)
					{}
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() != MouseEvent.BUTTON1)
				{
					if (schemeElement_selected != null)
					{
						//catalogElementNameField.setBackground(SystemColor.window);
						componentNameLabel.setText("Название элемента в каталоге: (новый)");
						catalogElementNameField.setText(schemeElement_selected.getName());
						catalogElementNameField.setCaretPosition(0);
						selected_ors.put(schemeElement_selected.getId(), schemeElement_selected.getName());
						skip = true;
						catalogList.removeSelectionInterval(0, catalogList.getMaxSelectionIndex());
						skip = false;
					}
				}
			}
		});
		elementsList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent event)
			{
				if (event.getValueIsAdjusting())
					return;
				if (elementsList.getSelected() != null)
					elementsList_selected();
			}
		});
		catalogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		catalogList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent event)
			{
				if (event.getValueIsAdjusting() || skip)
					return;
				catalogList_selected();
			}
		});

		catalogElementNameField.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent event)
					{}
			public void keyReleased(KeyEvent event)
			{
				catalogElementNameField_keyReleased();
			}
			public void keyTyped(KeyEvent event)
					{}
		});
	}

	public void init(DataSet data)
	{
		ObjectResourceSorter sorter = new ObjectResourceTypSorter();
		sorter.setDataSet(data);
		java.util.List ds = sorter.sort("typ", ObjectResourceSorter.SORT_ASCENDING);
		elementsList.setContents(ds);

		for (Iterator it = ds.iterator(); it.hasNext(); )
		{
			ObjectResource obj = (ObjectResource)it.next();
			Object setting_obj = "";

			if (obj instanceof SchemeElement)
			{
				if (!((SchemeElement)obj).equipment_id.equals(""))
					setting_obj = (ObjectResource)Pool.get("kisequipment", ((SchemeElement)obj).equipment_id);
				else
					setting_obj = ((SchemeElement)obj).getName();
			}
			if (obj instanceof SchemeLink)
			{
				if (!((SchemeLink)obj).link_id.equals(""))
					setting_obj = (ObjectResource)Pool.get(Link.typ, ((SchemeLink)obj).link_id);
				else
					setting_obj = ((SchemeLink)obj).getName();
			}
			if (obj instanceof SchemeCableLink)
			{
				if (!((SchemeCableLink)obj).cable_link_id.equals(""))
					setting_obj = (ObjectResource)Pool.get(CableLink.typ, ((SchemeCableLink)obj).cable_link_id);
				else
					setting_obj = ((SchemeCableLink)obj).getName();
			}
			if (obj instanceof SchemePath)
			{
				if (!((SchemePath)obj).path_id.equals(""))
					setting_obj = (ObjectResource)Pool.get(TransmissionPath.typ, ((SchemePath)obj).path_id);
				else
					setting_obj = ((SchemePath)obj).getName();
			}
			selected_ors.put(obj.getId(), setting_obj == null ? "" : setting_obj);
		}
	}

	void elementsList_selected()
	{
		if (schemeElement_selected == null || !schemeElement_selected.getTyp().equals(elementsList.getSelectedObjectResource().getTyp()))
		{
			schemeElement_selected = elementsList.getSelectedObjectResource();
			Map ds = new HashMap();
			catalogElementNameField.setText("");

			if (schemeElement_selected instanceof SchemeElement)
				ds = Pool.getHash("kisequipment");
			else if (schemeElement_selected instanceof SchemeLink)
				ds = Pool.getHash(Link.typ);
			else if (schemeElement_selected instanceof SchemeCableLink)
				ds = Pool.getHash(CableLink.typ);
			else if (schemeElement_selected instanceof SchemePath)
				ds = Pool.getHash(TransmissionPath.typ);

			ObjectResourceFilter filter = new ObjectResourceDomainFilter(aContext.getSessionInterface().getDomainId());
			ObjectResourceSorter sorter = new ObjectResourceNameSorter();
			sorter.setDataSet(filter.filter(ds));

			skip = true;
			catalogList.setContents(sorter.sort("name", ObjectResourceSorter.SORT_ASCENDING));
			skip = false;
		}
		schemeElement_selected = elementsList.getSelectedObjectResource();

		Object c_el = selected_ors.get(schemeElement_selected.getId());
		if (c_el instanceof ObjectResource)
			catalogList.setSelected(c_el);
		else if (c_el instanceof String)
		{
			if (c_el.equals(""))
				componentNameLabel.setText("Название элемента в каталоге:");
			else
				componentNameLabel.setText("Название элемента в каталоге: (новый)");
			catalogElementNameField.setText((String)c_el);
			catalogElementNameField.setCaretPosition(0);
			skip = true;
			catalogList.removeSelectionInterval(0, catalogList.getMaxSelectionIndex());
			skip = false;
		}
	}

	void catalogList_selected()
	{
		//catalogElementNameField.setBackground(Color.lightGray);
		componentNameLabel.setText("Название элемента в каталоге:");
		if (catalogList.getSelected() == null)
		{
			catalogElementNameField.setText("");
			selected_ors.put(schemeElement_selected.getId(), "");
		}
		else
		{
			ObjectResource catalogElement_selected = catalogList.getSelectedObjectResource();
			catalogElementNameField.setText(catalogElement_selected.getName());
			catalogElementNameField.setCaretPosition(0);
			selected_ors.put(schemeElement_selected.getId(), catalogElement_selected);
		}
	}

	void catalogElementNameField_keyReleased()
	{
		if (catalogElementNameField.getText().equals(""))
			//catalogElementNameField.setBackground(Color.lightGray);
		componentNameLabel.setText("Название элемента в каталоге:");
		else
			//catalogElementNameField.setBackground(SystemColor.window);
		componentNameLabel.setText("Название элемента в каталоге: (новый)");
		skip = true;
		catalogList.removeSelectionInterval(catalogList.getSelectedIndex(), catalogList.getSelectedIndex());
		skip = false;
		selected_ors.put(schemeElement_selected.getId(), catalogElementNameField.getText());
	}
}

class ObjectResourceTypSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"typ", "string"}
		};

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}

	public String getString(ObjectResource or, String column)
	{
		return or.getTyp() + or.getName();
	}

	public long getLong(ObjectResource or, String column)
	{
		return 0;
	}

}

