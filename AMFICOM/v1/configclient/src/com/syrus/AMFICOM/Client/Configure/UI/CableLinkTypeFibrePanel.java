package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
//import com.syrus.AMFICOM.Client.Resource.Network.CableLinkThread;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;

public class CableLinkTypeFibrePanel extends GeneralPanel
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	CableLinkType clt;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JPanel linksPanel = new JPanel();
	private JLabel linksTypeLabel = new JLabel();
	private JLabel linksMarkLabel = new JLabel();
	private JLabel linksNameLabel = new JLabel();
	private JLabel linksNumberLabel = new JLabel();
	private JLabel linksIdLabel = new JLabel();
	private ObjectResourceComboBox linksTypeBox = new ObjectResourceComboBox(LinkType.typ, true);
	private JTextField linksMarkField = new JTextField();
	private JTextField linksNameField = new JTextField();
	private JTextField linksIdField = new JTextField();
	private JTextField linksNumberField = new JTextField();
	private JButton linksNumberButton = new JButton();
	private JPanel listPanel = new JPanel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private BorderLayout borderLayout1 = new BorderLayout();
	private ObjectResourceListBox LinksList = new ObjectResourceListBox(CableTypeThread.typ);

	public CableLinkTypeFibrePanel()
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

	public CableLinkTypeFibrePanel(CableLinkType clt)
	{
		this();
		setObjectResource(clt);
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
		linksNumberLabel.setText(LangModelConfig.getString("label_linksnumber"));
		linksNumberLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksNumberButton.setText(LangModelConfig.getString("label_linksnumberbutton"));
		linksNumberButton.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksNumberButton.addActionListener(new MyActionListener());

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
		linksPanel.add(linksNumberLabel,          new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
		 linksPanel.add(linksIdLabel,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		linksPanel.add(linksNameField,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		linksPanel.add(linksMarkField,    new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		linksPanel.add(linksTypeBox,   new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(linksNumberField, BorderLayout.CENTER);
		p1.add(linksNumberButton, BorderLayout.EAST);
		linksPanel.add(p1,   new GridBagConstraints(1, 3, 1, 1, 0.8, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			linksPanel.add(linksIdField,   new GridBagConstraints(1, 45, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));


		this.add(listPanel,   new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(linksPanel,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	class MyActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				int num = Integer.parseInt(linksNumberField.getText());
				if (num < 0)
					throw new NumberFormatException();

				int old_num = clt.cable_threads.size();
				if (num > old_num)
				{
					String link_type_id = "";
					if (old_num > 0)
					{
						CableTypeThread ctt = (CableTypeThread)clt.cable_threads.get(0);
						link_type_id = ctt.link_type_id;
					}
					for (int i = old_num; i < num; i++)
					{
						CableTypeThread newctt = new CableTypeThread();
						newctt.id = aContext.getDataSourceInterface().GetUId(CableTypeThread.typ);
						newctt.name = "fibrered" + (i + 1);
						newctt.link_type_id = (old_num == 0) ? "": link_type_id;
						//newctt.characteristics = ctt.characteristics.clone();
						clt.cable_threads.add(newctt);
					}
				}
				else
				{
					for (int i = old_num-1; i >= num; i--)
					{
						//CableTypeThread ctt = (CableTypeThread)clt.cable_threads.get(i);
						clt.cable_threads.remove(i);
					}
				}
				setObjectResource(clt);

			}
			catch (NumberFormatException ex)
			{
				linksNumberField.setText(String.valueOf(clt.cable_threads.size()));
			}
		}
/*
		private int parseName(String name)
		{
			if (name.length() == 0)
				return 0;
			char[] ch = new char[name.length()];
			name.getChars(0, name.length(), ch, 0);
			int i = ch.length - 1;
			while (Character.isDigit(ch[i]))
				i--;
			try
			{
				return Integer.parseInt(name.subSequence(i+1, name.length()));
			}
			catch (NumberFormatException ex)
			{
				return 0;
			}
		}*/
	}

	public ObjectResource getObjectResource()
	{
		return clt;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.clt = (CableLinkType)or;

		this.LinksList.setContents("");
		this.linksIdField.setText("");
		this.linksNameField.setText("");
		this.linksNumberField.setText(String.valueOf(clt.cable_threads.size()));
		this.linksMarkField.setText("");
		this.linksTypeBox.setSelected("");

		if(clt != null)
		{
			DataSet ds = new DataSet(clt.cable_threads.iterator());
			ObjectResourceSorter sorter = CableTypeThread.getDefaultSorter();
			sorter.setDataSet(ds);
			LinksList.setContents(sorter.default_sort());
		}
	}

	public boolean modify()
	{
		try
		{
			CableTypeThread clt = (CableTypeThread)LinksList.getSelectedObjectResource();

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

		CableTypeThread clt = (CableTypeThread)LinksList.getSelectedObjectResource();
		if (clt != null)
		{
			this.linksIdField.setText(clt.getId());
			this.linksNameField.setText(clt.getName());
			this.linksMarkField.setText(clt.mark);
			this.linksTypeBox.setSelected(clt.link_type_id);
		}
	}
}
