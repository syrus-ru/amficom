package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.IdentifierPool;
import oracle.jdeveloper.layout.XYConstraints;

public class LinkTypePane extends JPanel implements ObjectResourcePropertiesPane
{
	private ApplicationContext aContext;
	protected LinkType linkType;
	private static ObjectResourcePropertiesPane instance;

	private AbstractLinkTypeGeneralPanel gPanel = new AbstractLinkTypeGeneralPanel();
	private LinkTypeCharacteristicsPanel chPanel = new LinkTypeCharacteristicsPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JButton saveButton = new JButton();
	private JPanel buttonsPanel = new JPanel();

	protected LinkTypePane()
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

	protected LinkTypePane(LinkType l)
	{
		this();
		setObject(l);
	}

	public static ObjectResourcePropertiesPane getInstance()
	{
		if (instance == null)
			instance = new LinkTypePane();
		return instance;
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(SwingConstants.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(chPanel.getName(), chPanel);

		saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		buttonsPanel.add(saveButton, new XYConstraints(200, 487, -1, -1));
	}

	public Object getObject()
	{
		return linkType;
	}

	public void setObject(Object or)
	{
		this.linkType = (LinkType)or;

		gPanel.setObject(linkType);
		chPanel.setObject(linkType);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if (gPanel.modify() &&
				chPanel.modify())
			return true;
		return false;
	}

	public boolean save()
	{
		if (modify()) {
			if (chPanel.save()) {
				try {
					ConfigurationStorableObjectPool.putStorableObject(linkType);
					ConfigurationStorableObjectPool.flush(true);
					return true;
				} catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
		}
		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelConfig.getString("err_incorrect_data_input"));
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean cancel()
	{
		return false;
	}

	public boolean delete()
	{
		return false;
	}

	public boolean create()
	{
		PopupNameFrame dialog = new PopupNameFrame(Environment.getActiveWindow(), "����� ���");
		dialog.setSize(dialog.preferredSize);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - dialog.getPreferredSize().width) / 2,
											 (screenSize.height - dialog.getPreferredSize().height) / 2);
		dialog.setVisible(true);

		if (dialog.getStatus() == PopupNameFrame.OK && !dialog.getName().equals(""))
		{
			String name = dialog.getName();
			Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
			try {
				LinkType new_type = LinkType.createInstance(
						user_id,
						"",
						"",
						name,
						LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER,
						"",
						"",
						IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE));

				setObject(new_type);
				return true;
			}
			catch (IllegalObjectEntityException ex) {
				ex.printStackTrace();
			}
			catch (CreateObjectException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
	}
}
