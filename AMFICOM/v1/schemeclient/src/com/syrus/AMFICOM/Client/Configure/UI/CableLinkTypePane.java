package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.LinkTypeSort;
import com.syrus.AMFICOM.general.*;
import oracle.jdeveloper.layout.XYConstraints;

public class CableLinkTypePane extends JPanel implements ObjectResourcePropertiesPane
{
	private ApplicationContext aContext;
	protected CableLinkType linkType;
	private static ObjectResourcePropertiesPane instance;

	private AbstractLinkTypeGeneralPanel gPanel = new AbstractLinkTypeGeneralPanel();
	private CableLinkTypeFibrePanel fPanel = new CableLinkTypeFibrePanel();
	private CableLinkTypeCharacteristicsPanel chPanel = new CableLinkTypeCharacteristicsPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JButton saveButton = new JButton();
	private JPanel buttonsPanel = new JPanel();

	protected CableLinkTypePane()
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

	protected CableLinkTypePane(LinkType l)
	{
		this();
		setObject(l);
	}

	public static ObjectResourcePropertiesPane getInstance()
	{
		if (instance == null)
			instance = new CableLinkTypePane();
		return instance;
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(SwingConstants.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(fPanel.getName(), fPanel);
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
		this.linkType = (CableLinkType)or;

		gPanel.setObject(linkType);
		fPanel.setObject(linkType);
		chPanel.setObject(linkType);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		fPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if (gPanel.modify() &&
				fPanel.modify() &&
				chPanel.modify())
			return true;
		return false;
	}

	public boolean save()
	{
		if(modify())
		{
			try {
				ConfigurationStorableObjectPool.putStorableObject(linkType);
				return true;
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
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
		PopupNameFrame dialog = new PopupNameFrame(Environment.getActiveWindow(), "Новый тип");
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
				CableLinkType new_type = CableLinkType.createInstance(
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
