package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.ArrayList;

import java.awt.Dimension;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import oracle.jdeveloper.layout.XYConstraints;

public class TransmissionPathFibrePanel extends GeneralPanel
{
	SchemePath path;

	JLabel equipLabel = new JLabel();
	JLabel idLabel = new JLabel();
	JLabel local_addLabel1 = new JLabel();
	JLabel nameLabel = new JLabel();
	JTextField idField = new JTextField();
	JTextField nameField = new JTextField();
	JTextField localAdressField = new JTextField();
	ObjectResourceComboBox equipBox = new ObjectResourceComboBox(KIS.typ, true);
	private JTextArea descTextArea = new JTextArea();
	private JLabel descLabel = new JLabel();
	private ObjectResourceComboBox portBox = new ObjectResourceComboBox(MeasurementPort.typ, true);
	private JLabel portLabel = new JLabel();
	private JLabel local_addLabel2 = new JLabel();
	private JTextField ModifyField = new JTextField();
	private JLabel modifyLabel1 = new JLabel();
	private JLabel linksLabel = new JLabel();
//	private JTable LinksTable = new JTable();
	private JLabel modifyLabel2 = new JLabel();
	ObjectResourceTablePane LinksTable = new ObjectResourceTablePane();
	private JButton saveButton = new JButton();

	public TransmissionPathFibrePanel()
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

	public TransmissionPathFibrePanel(SchemePath tp)
	{
		this();
		setObjectResource(tp);
	}

	private void jbInit() throws Exception
	{
		setName("Список волокон");

		this.setPreferredSize(new Dimension(510, 410));
		this.setMaximumSize(new Dimension(510, 410));
		this.setMinimumSize(new Dimension(510, 410));

		LinksTable.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add(LinksTable,    new XYConstraints(2, 35, 455, 120));
		LinksTable.initialize(
				new StubDisplayModel(
					new String[] {"thread", "cable"},
					new String[] {"Волокно", "Кабель"}),
				new ArrayList());
	}

	public ObjectResource getObjectResource()
	{
		return path;
	}

	public void setObjectResource(ObjectResource or)
	{
		path = (SchemePath)or;

		LinksTable.setContents(path.links);
	}

	public boolean modify()
	{
		return true;
	}
}