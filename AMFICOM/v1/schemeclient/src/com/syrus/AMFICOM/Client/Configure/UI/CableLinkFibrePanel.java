package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.Arrays;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.CableThreadController;
import com.syrus.AMFICOM.scheme.corba.*;

public class CableLinkFibrePanel extends GeneralPanel
{
	SchemeCableLink link;

	private JPanel linksPanel = new JPanel();
	private JLabel linksTypeLabel = new JLabel();
	private JLabel linksMarkLabel = new JLabel();
	private JLabel linksNameLabel = new JLabel();
	private JLabel linksIdLabel = new JLabel();
	private ObjComboBox linksTypeBox;
	private JTextField linksMarkField = new JTextField();
	private JTextField linksNameField = new JTextField();
	private JTextField linksIdField = new JTextField();
	private JPanel listPanel = new JPanel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	ObjList threadsList = new ObjList(CableThreadController.getInstance(), CableThreadController.KEY_NAME);

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
		setObject(link);
	}

	private void jbInit() throws Exception
	{
		Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
				getAccessIdentifier().domain_id);
		Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
				domain_id, true);
		DomainCondition condition = new DomainCondition(domain, ObjectEntities.LINKTYPE_ENTITY_CODE);
		linksTypeBox = new ObjComboBox(
				LinkTypeController.getInstance(),
				ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true),
				LinkTypeController.KEY_NAME);

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
		threadsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		threadsList.setBorder(BorderFactory.createLoweredBevelBorder());
		threadsList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				threadsList_valueChanged(e);
			}
		});

		jScrollPane1.getViewport();
		jScrollPane1.getViewport().add(threadsList, null);
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

	public Object getObject()
	{
		return link;
	}

	public void setObject(Object or)
	{
		link = (SchemeCableLink) or;

		linksIdField.setText("");
		linksNameField.setText("");
		linksMarkField.setText("");

		threadsList.removeAll();
		threadsList.addElements(Arrays.asList(link.schemeCableThreads()));
	}

	public boolean modify()
	{
		try
		{
			SchemeCableThread clt = (SchemeCableThread)threadsList.getSelectedValue();
			clt.name(linksNameField.getText());
		}
		catch (Exception ex)
		{
			return false;
		}
		return true;
	}

	private void threadsList_valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
		SchemeCableThread clt = (SchemeCableThread)threadsList.getSelectedValue();

		linksIdField.setText(clt.id().identifierString());
		linksNameField.setText(clt.name());

		LinkType type = clt.cableThreadTypeImpl().getLinkType();
		linksTypeBox.setSelectedItem(type);
		linksMarkField.setText(clt.cableThreadTypeImpl().getDescription());
	}
}