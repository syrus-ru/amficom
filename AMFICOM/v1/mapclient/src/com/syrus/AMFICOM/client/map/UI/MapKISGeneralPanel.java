package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.Map.MapKISNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class MapKISGeneralPanel extends GeneralPanel 
{
	MapKISNodeElement mapkis;

	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel4 = new JLabel();
	JLabel jLabel5 = new JLabel();
	JLabel jLabel7 = new JLabel();
	JTextField nameField = new JTextField();
	JTextField idField = new JTextField();
	JTextField createdField = new JTextField();
	ObjectResourceComboBox typeComboBox = new ObjectResourceComboBox("mapkisproto");
	ObjectResourceComboBox kisComboBox = new ObjectResourceComboBox(SchemeElement.typ, true);
	JTextArea descriptionArea = new JTextArea();
	private JPanel jPanel1 = new JPanel();
	private XYLayout xYLayout1 = new XYLayout();
	
	public MapKISGeneralPanel()
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
	}

	public MapKISGeneralPanel(MapKISNodeElement mapkis)
	{
		this();
		setObjectResource(mapkis);
	}

	private void jbInit() throws Exception
	{
		setName("Общие");
	
//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));

		jLabel1.setText("Название");
		jLabel2.setText("Идентификатор");
		jLabel3.setText("Время создания");
		jLabel4.setText("Тип");
		jLabel5.setText("Элемент");
		jLabel7.setText("Примечания");
		jPanel1.setLayout(xYLayout1);

		this.add(jPanel1, new XYConstraints(195, 180, -1, -1));
		jPanel1.add(jLabel1, new XYConstraints(5, 10, 105, 20));
		jPanel1.add(jLabel2, new XYConstraints(5, 35, 105, 20));
		jPanel1.add(jLabel3, new XYConstraints(5, 60, 105, 20));
		jPanel1.add(jLabel4, new XYConstraints(5, 85, 105, 20));
		jPanel1.add(jLabel5, new XYConstraints(5, 110, 105, 20));
		jPanel1.add(jLabel7, new XYConstraints(5, 135, 105, 20));

		jPanel1.add(nameField, new XYConstraints(150, 10, 305, 20));
		jPanel1.add(idField, new XYConstraints(150, 35, 305, 20));
		jPanel1.add(createdField, new XYConstraints(150, 60, 305, 20));
		jPanel1.add(typeComboBox, new XYConstraints(150, 85, 305, 20));
		jPanel1.add(kisComboBox, new XYConstraints(150, 110, 305, 20));
		jPanel1.add(descriptionArea, new XYConstraints(5, 160, 455, 145));

		kisComboBox.setEnabled(false);
		idField.setEnabled(false);
		createdField.setEnabled(false);
	}

	public ObjectResource getObjectResource()
	{
		return mapkis;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

		this.mapkis = (MapKISNodeElement )or;

//		System.out.println("set prop pane to " + mapkis.name);

		if(mapkis.element_id != null && !mapkis.element_id.equals(""))
			kisComboBox.setSelected(mapkis.element_id);
		else
			kisComboBox.setSelected("");

		nameField.setText(mapkis.getName());
		idField.setText(mapkis.getId());
//		createdField.setText(sdf.format(new Date(mapkis.crea);
		typeComboBox.setSelected(mapkis.type_id);
		descriptionArea.setText(mapkis.description);
		return true;
	}

	public boolean modify()
	{
		mapkis.name = nameField.getText();

		MapProtoElement mpe = (MapProtoElement )typeComboBox.getSelected();
		mapkis.type_id = mpe.getId();

		mapkis.description = descriptionArea.getText();
		return true;
	}

}