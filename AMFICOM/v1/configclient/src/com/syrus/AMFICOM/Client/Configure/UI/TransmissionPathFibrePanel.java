package com.syrus.AMFICOM.Client.Configure.UI;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTablePane;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.ISM.AccessPort;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Dimension;

import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import oracle.jdeveloper.layout.XYConstraints;

public class TransmissionPathFibrePanel extends GeneralPanel
{
	TransmissionPath tp;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
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
	private ObjectResourceComboBox portBox = new ObjectResourceComboBox(AccessPort.typ, true);
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

	public TransmissionPathFibrePanel(TransmissionPath tp)
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
				new DataSet());
	}

	public ObjectResource getObjectResource()
	{
		return tp;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.tp = (TransmissionPath )or;

		if(tp != null)
		{
			LinksTable.setContents(new DataSet(tp.links.iterator()));
		}
		else
		{
			LinksTable.setContents(new DataSet());
		}
	}

	public boolean modify()
	{
		return true;
	}
}