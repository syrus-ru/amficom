package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.CableLinkThread;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class CableLinkFibrePanel extends GeneralPanel
{
	SchemeCableLink link;

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

	public CableLinkFibrePanel(SchemeCableLink link)
	{
		this();
		setObjectResource(link);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_fibers"));

		this.setLayout(new GridBagLayout());

		linksTypeLabel.setText(LangModelConfig.getString("label_type"));
		linksTypeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksMarkLabel.setText(LangModelConfig.getString("label_mark"));
		linksMarkLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksNameLabel.setText(LangModelConfig.getString("label_name"));
		linksNameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksIdLabel.setText(LangModelConfig.getString("label_id"));
		linksIdLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		linksIdField.setEnabled(false);
		listPanel.setLayout(new BorderLayout());

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

		linksPanel.setLayout(new GridBagLayout());

		linksPanel.add(linksNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		linksPanel.add(linksMarkLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		linksPanel.add(linksTypeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if (Environment.isDebugMode())
			linksPanel.add(linksIdLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
					, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		linksPanel.add(linksNameField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		linksPanel.add(linksMarkField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		linksPanel.add(linksTypeBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		if (Environment.isDebugMode())
			linksPanel.add(linksIdField, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
					, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		this.add(listPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
				, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(linksPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	public ObjectResource getObjectResource()
	{
		return link;
	}

	public void setObjectResource(ObjectResource or)
	{
		link = (SchemeCableLink) or;

		LinksList.setContents("");
		linksIdField.setText("");
		linksNameField.setText("");
		linksMarkField.setText("");
		linksTypeBox.setSelected("");

		ObjectResourceSorter sorter = SchemeCableThread.getDefaultSorter();
		sorter.setDataSet(link.cableThreads);
		LinksList.setContents(sorter.default_sort());
	}

	public boolean modify()
	{
		try
		{
			SchemeCableThread clt = (SchemeCableThread) LinksList.getSelectedObjectResource();

			clt.id = this.linksIdField.getText();
			clt.name = this.linksNameField.getText();
			clt.linkTypeId = (String)this.linksTypeBox.getSelected();

			CableLinkThread thread = (CableLinkThread)Pool.get(CableLinkThread.typ, clt.threadId);
			if (thread != null)
				thread.mark = this.linksMarkLabel.getText();
		}
		catch (Exception ex)
		{
			return false;
		}
		return true;
	}

	private void LinksList_valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
		SchemeCableThread clt = (SchemeCableThread)LinksList.getSelectedObjectResource();
		linksIdField.setText(clt.getId());
		linksNameField.setText(clt.getName());
		linksTypeBox.setSelected(clt.linkTypeId);

		CableLinkThread thread = (CableLinkThread)Pool.get(CableLinkThread.typ, clt.threadId);
		if (thread != null)
			linksMarkField.setText(thread.mark);
	}
}