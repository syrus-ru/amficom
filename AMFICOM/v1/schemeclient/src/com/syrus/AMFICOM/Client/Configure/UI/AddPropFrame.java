package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.DataType;

public class AddPropFrame extends JDialog
{
	public static final int OK = 1;
	public static final int CANCEL = 0;

	protected int res = CANCEL;
	protected CharacteristicTypeSort sort;
	protected CharacteristicType type;
	Object selected = LangModelConfig.getString("label_new_char");

	private ObjComboBox comboBox;
	private JPanel panel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JTextField nameField = new JTextField();
	private JTextArea descrArea = new JTextArea();
	private JLabel name = new JLabel();
	private JLabel descr = new JLabel();
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();
	ApplicationContext aContext;

	public AddPropFrame(Frame parent, String title, ApplicationContext aContext)
	{
		super(parent, title);
		this.aContext = aContext;

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		comboBox = new ObjComboBox(
					CharacteristicTypeController.getInstance(),
					new LinkedList(),
					CharacteristicTypeController.KEY_NAME);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(350, 170);

		setLocation((screenSize.width-frameSize.width)/2, (screenSize.height-frameSize.height)/2);
		setSize(frameSize);
		setResizable(false);
		setTitle(LangModelConfig.getString("label_char"));

		name.setText(LangModelConfig.getString("label_name"));
		descr.setText(LangModelConfig.getString("label_description"));

		nameField.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{}
			public void keyReleased(KeyEvent e)
			{
				okButton.setEnabled(!nameField.getText().equals(""));
			}
			public void keyTyped(KeyEvent e)
			{}
		});

		panel.setLayout(new BorderLayout());

		JPanel n_panel = new JPanel();
		n_panel.setLayout(new BorderLayout());
		name.setPreferredSize(new Dimension(65, 5));
		n_panel.add(name, BorderLayout.WEST);
		n_panel.add(nameField, BorderLayout.CENTER);

		JPanel s_panel = new JPanel();
		s_panel.setLayout(new BorderLayout());
		descr.setPreferredSize(new Dimension(65, 5));
		s_panel.add(descr, BorderLayout.WEST);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(descrArea);
		descrArea.setAutoscrolls(true);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		s_panel.add(scrollPane, BorderLayout.CENTER);

		panel.add(n_panel, BorderLayout.NORTH);
		panel.add(s_panel, BorderLayout.CENTER);

		okButton.setText(LangModelConfig.getString("buttonAdd"));
		okButton.setEnabled(false);
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				okButton_actionPerformed(e);
			}
		});

		cancelButton.setText(LangModelConfig.getString("buttonCancel"));
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed(e);
			}
		});

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		comboBox.addItem(LangModelConfig.getString("label_new_char"));
		comboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					comboBoxItemSelected();
				}
			}
		});

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(comboBox, BorderLayout.NORTH);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	void okButton_actionPerformed(ActionEvent e)
	{
		res = OK;
		if (selected.equals(LangModelConfig.getString("label_new_char")))
		{
			Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
			try {
				type = CharacteristicType.createInstance(
						userId,
						"",
						descrArea.getText(),
						DataType._DATA_TYPE_STRING,
						sort);
			}
			catch (CreateObjectException ex) {
				ex.printStackTrace();
			}
		}
		else
		{
			/*h = Pool.getMap(CharacteristicType.typ);
			for (Enumeration enum = h.elements(); enum.hasMoreElements();)
			{
				CharacteristicType type = (CharacteristicType)enum.nextElement();
				if (type.getName().equals(selected))

			}*/
		}

		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		dispose();
	}

	void comboBoxItemSelected()
	{
		selected = comboBox.getSelectedItem();
		boolean b = selected.equals(LangModelConfig.getString("label_new_char"));
		nameField.setEnabled(b);
		descrArea.setEnabled(b);
		okButton.setEnabled(!b);
		if (!b)
		{
			type = (CharacteristicType)selected;
			nameField.setText(type.getDescription());
		}
		else
		{
			type = null;
			nameField.setText("");
			descrArea.setText("");
		}
	}

	public int showDialog(CharacteristicTypeSort sort, List chars)
	{
		this.sort = sort;

		Identifier domainId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
		try {
			Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			DomainCondition condition = new DomainCondition(domain, ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE);
			List characteristicTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(
						 condition, true);
			for (Iterator it = characteristicTypes.iterator(); it.hasNext();)
			{
				CharacteristicType type = (CharacteristicType)it.next();
				if (type.getSort().equals(sort) && !chars.contains(type))
					comboBox.addItem(type);
			}
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
		setModal(true);
		setVisible(true);
		return res;
	}

	public String getSelectedName()
	{
		if (selected instanceof String)
			return (String)selected;
		else
			return ((CharacteristicType)selected).getDescription();
	}

	public CharacteristicType getSelectedType()
	{
		return type;
	}
}