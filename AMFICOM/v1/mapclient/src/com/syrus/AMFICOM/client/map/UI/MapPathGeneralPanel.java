package com.syrus.AMFICOM.Client.Configure.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertyTablePane;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.text.SimpleDateFormat;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import oracle.jdeveloper.layout.VerticalFlowLayout;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class MapPathGeneralPanel extends GeneralPanel 
{
	MapTransmissionPathElement mappath;
	
//	JScrollPane charScrollPane = new JScrollPane();
//	JTable charTable = new JTable();

//	PropertyTableModel tableModel = new PropertyTableModel();
	JLabel jLabel6 = new JLabel();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel4 = new JLabel();
	ObjectResourceComboBox pathComboBox = new ObjectResourceComboBox("schemepath");
	ObjectResourceComboBox typeBox = new ObjectResourceComboBox("mappathproto");
	JTextField idField = new JTextField();
	JTextField nameField = new JTextField();
	private JPanel attributesPanel = new JPanel();
	ObjectResourcePropertyTablePane charPane = new ObjectResourcePropertyTablePane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	public MapPathGeneralPanel()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		charPane.initialize(new String[] {"Атрибут", "Значение"}, null);
	}

	public MapPathGeneralPanel(MapTransmissionPathElement mappath)
	{
		this();
		setObjectResource(mappath);
	}

	private void jbInit() throws Exception
	{
//		setName("Путь тестирования");

		this.setLayout(gridBagLayout1);
		jLabel6.setText("Элемент схемы");
		jLabel6.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel1.setText("Тип");
		jLabel1.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel2.setText("Идентификатор");
		jLabel2.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel4.setText("Название");
		jLabel4.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		pathComboBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					pathComboBox_itemStateChanged(e);
				}
			});
		idField.setEnabled(false);

		this.add(jLabel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel6, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(jLabel2, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(nameField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(typeBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(pathComboBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idField, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

//		attributesPanel.add(charPane, null);
//		this.add(attributesPanel, BorderLayout.CENTER);
	}

	public ObjectResource getObjectResource()
	{
		return mappath;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.mappath = (MapTransmissionPathElement )or;

		charPane.setSelected(or);

		if(mappath == null)
		{
			typeBox.setSelected("");
			pathComboBox.setSelected("");
			nameField.setText("");
			idField.setText("");
			return true;
		}

		typeBox.setSelected(mappath.type_id);
		pathComboBox.setSelected(mappath.PATH_ID);
		nameField.setText(mappath.getName());
		idField.setText(mappath.getId());
		return true;
	}

	public boolean modify()
	{
		mappath.type_id = typeBox.getSelectedId();
		mappath.PATH_ID = pathComboBox.getSelectedId();
		mappath.name = nameField.getText();
		mappath.setId(idField.getText());
		return true;
	}

	void buttonToCatalogue_actionPerformed(ActionEvent e)
	{
		modify();
/*
		Command command = new MapISMToCatalogueCommand(aContext, mappath);
		command.setParameter("local_address", localAddressField.getText());
		command.execute();
*/
	}

	void pathComboBox_itemStateChanged(ItemEvent e)
	{

	}
}