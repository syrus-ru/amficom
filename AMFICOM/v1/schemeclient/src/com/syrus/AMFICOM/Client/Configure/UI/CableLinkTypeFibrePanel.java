package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;

public class CableLinkTypeFibrePanel extends GeneralPanel
{
	protected CableLinkType clt;

	JPanel linksPanel = new JPanel();
	JLabel linksTypeLabel = new JLabel();
	JLabel linksMarkLabel = new JLabel();
	JLabel linksNameLabel = new JLabel();
	JLabel linksNumberLabel = new JLabel();
	JLabel linksIdLabel = new JLabel();
	ObjComboBox linksTypeBox;
	JTextField linksMarkField = new JTextField();
	JTextField linksNameField = new JTextField();
	JTextField linksNumberField = new JTextField();
	JButton linksNumberButton = new JButton();
	JPanel listPanel = new JPanel();
	JScrollPane jScrollPane1 = new JScrollPane();
	ObjList threadsList = new ObjList(CableThreadTypeController.getInstance(), StorableObjectWrapper.COLUMN_NAME);

	protected CableLinkTypeFibrePanel()
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

	protected CableLinkTypeFibrePanel(LinkType clt)
	{
		this();
		setObject(clt);
	}

	private void jbInit() throws Exception
	{
		EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINKTYPE_ENTITY_CODE);

		linksTypeBox = new ObjComboBox(
				LinkTypeController.getInstance(),
				ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true),
				StorableObjectWrapper.COLUMN_NAME);
		setName(LangModelConfig.getString("label_fibers"));

		this.setLayout(new GridBagLayout());

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
		linksNumberButton.addActionListener(new LinkNumberActionListener());

		linksIdLabel.setText(LangModelConfig.getString("label_id"));
		linksIdLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
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

		this.add(listPanel,   new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(linksPanel,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	class LinkNumberActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				int num = Integer.parseInt(linksNumberField.getText());
				if (num < 0)
					throw new NumberFormatException();

				int old_num = clt.getCableThreadTypes().size();
				if (num > old_num)
				{
//					LinkType link_type = null;
//					String codename = "";
//					if (old_num > 0)
//					{
//						CableThreadType ctt = (CableThreadType)clt.getCableThreadTypes().get(0);
//						link_type = ctt.getLinkType();
//						codename = ctt.getCodename();
//					}
					LinkType link_type = (LinkType)linksTypeBox.getSelectedItem();
					if (link_type == null)
						return;
					String codename = link_type.getCodename();
					if (codename == null)
						codename = "";

					Identifier user_id = new Identifier(((RISDSessionInfo)aContext.
							getSessionInterface()).getAccessIdentifier().user_id);
					try {
						List newCableThreadTypes = new ArrayList(num);
						newCableThreadTypes.addAll(clt.getCableThreadTypes());

						for (int i = old_num; i < num; i++) {
							CableThreadType newctt = CableThreadType.createInstance(
									user_id,
									codename,
									"",
									"fiber" + (i + 1),
									Color.BLACK.getRGB(),
									link_type);
							newCableThreadTypes.add(newctt);
						}
						clt.setCableThreadTypes(newCableThreadTypes);
					}
					catch (CreateObjectException ex) {
						ex.printStackTrace();
					}
				}
				else
				{
					List toDelete = new LinkedList();
					for (int i = old_num-1; i >= num; i--)
					{
						//CableTypeThread ctt = (CableTypeThread)clt.cable_threads.get(i);
						CableThreadType ctt = (CableThreadType)clt.getCableThreadTypes().get(i);
						toDelete.add(ctt.getId());
						clt.getCableThreadTypes().remove(ctt);
					}
					try {
						ConfigurationStorableObjectPool.delete(toDelete);
					}
					catch (ApplicationException ex) {
						ex.printStackTrace();
					}
				}
				setObject(clt);
			}
			catch (NumberFormatException ex)
			{
				linksNumberField.setText(String.valueOf(clt.getCableThreadTypes().size()));
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

	public Object getObject()
	{
		return clt;
	}

	public void setObject(Object or)
	{
		this.clt = (CableLinkType)or;

		this.threadsList.removeAll();
		this.linksNameField.setText("");
		this.linksNumberField.setText(String.valueOf(clt.getCableThreadTypes().size()));
		this.linksMarkField.setText("");
//		this.linksTypeBox.setSelectedItem(null);

		if(clt != null)
			threadsList.addElements(clt.getCableThreadTypes());
	}

	public boolean modify()
	{
		try
		{
			CableThreadType ctt = (CableThreadType)threadsList.getSelectedValue();
			if (clt != null)
			{
				ctt.setName(linksNameField.getText());
				ctt.setDescription(this.linksMarkLabel.getText());
				ctt.setLinkType((LinkType)this.linksTypeBox.getSelectedItem());
			}
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	void threadsList_valueChanged(ListSelectionEvent e)
	{
		if(e.getValueIsAdjusting())
			return;

		CableThreadType ctt = (CableThreadType)this.threadsList.getSelectedValue();
		if (ctt != null)
		{
			this.linksNameField.setText(ctt.getName());
			this.linksMarkField.setText(ctt.getDescription());
			this.linksTypeBox.setSelectedItem(ctt.getLinkType());
		}
	}
}
