package com.syrus.AMFICOM.Client.Configure.UI;


import java.text.SimpleDateFormat;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.BorderFactory;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.DataSet;

import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.Network.CableLinkThread;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import java.awt.*;

public class CableLinkFibrePanel extends GeneralPanel
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	CableLink cl;

  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JPanel linksPanel = new JPanel();
	private JLabel linksTypeLabel = new JLabel();
	private JLabel linksMarkLabel = new JLabel();
	private JLabel linksNameLabel = new JLabel();
	private JLabel linksIdLabel = new JLabel();
	private ObjectResourceComboBox linksTypeBox = new ObjectResourceComboBox(LinkType.typ, true);
	private JTextField linksMarkField = new JTextField();
	private JTextField linksNameField = new JTextField();
	private JTextField linksIdField = new JTextField();
	private JPanel listPanel = new JPanel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private BorderLayout borderLayout1 = new BorderLayout();
	private ObjectResourceListBox LinksList = new ObjectResourceListBox(CableLinkThread.typ);

	public CableLinkFibrePanel()
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

	public CableLinkFibrePanel(CableLink cl)
	{
		this();
		setObjectResource(cl);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_fibers"));

		this.setLayout(gridBagLayout2);

		linksTypeLabel.setText(LangModelConfig.getString("label_type"));
		linksTypeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksMarkLabel.setText(LangModelConfig.getString("label_mark"));
		linksMarkLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksNameLabel.setText(LangModelConfig.getString("label_name"));
		linksNameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksIdLabel.setText(LangModelConfig.getString("label_id"));
		linksIdLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksIdField.setEnabled(false);
		listPanel.setLayout(borderLayout1);

		jScrollPane1.setPreferredSize(new Dimension(145, 125));
		jScrollPane1.setMinimumSize(new Dimension(145, 125));
//		jScrollPane1.setMaximumSize(new Dimension(145, 125));
//		jScrollPane1.setSize(new Dimension(145, 125));
		LinksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		LinksList.setBorder(BorderFactory.createLoweredBevelBorder());
		LinksList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					LinksList_valueChanged(e);
				}
			});

		jScrollPane1.getViewport();
		jScrollPane1.getViewport().add(LinksList, null);
		listPanel.add(jScrollPane1, BorderLayout.CENTER);

	 linksPanel.setLayout(gridBagLayout1);

	 linksPanel.add(linksNameLabel,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 linksPanel.add(linksMarkLabel,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 linksPanel.add(linksTypeLabel,            new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 linksPanel.add(linksIdLabel,     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	 linksPanel.add(linksNameField,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 linksPanel.add(linksMarkField,    new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 linksPanel.add(linksTypeBox,   new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 linksPanel.add(linksIdField,   new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));


	 this.add(listPanel,   new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(linksPanel,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
  }

	public ObjectResource getObjectResource()
	{
		return cl;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.cl = (CableLink )or;

		this.LinksList.setContents("");
		this.linksIdField.setText("");
		this.linksNameField.setText("");
		this.linksMarkField.setText("");
		this.linksTypeBox.setSelected("");

		if(cl != null)
		{
			DataSet ds = new DataSet(cl.threads.elements());
			ObjectResourceSorter sorter = CableLinkThread.getDefaultSorter();
			sorter.setDataSet(ds);
			ds = sorter.default_sort();
			this.LinksList.setContents(ds.elements());
		}
		return true;

	}

	public boolean modify()
	{
		try
		{
			CableLinkThread clt = (CableLinkThread )LinksList.getSelectedObjectResource();

			clt.id = this.linksIdField.getText();
			clt.name = this.linksNameField.getText();
			clt.mark = this.linksMarkLabel.getText();
			clt.link_type_id = (String )this.linksTypeBox.getSelected();
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	private void LinksList_valueChanged(ListSelectionEvent e)
	{
		if(e.getValueIsAdjusting())
			return;
		CableLinkThread clt = (CableLinkThread )LinksList.getSelectedObjectResource();
		if (clt != null)
		{
			this.linksIdField.setText(clt.getId());
			this.linksNameField.setText(clt.getName());
			this.linksMarkField.setText(clt.mark);
			this.linksTypeBox.setSelected(clt.link_type_id);
		}
	}
}