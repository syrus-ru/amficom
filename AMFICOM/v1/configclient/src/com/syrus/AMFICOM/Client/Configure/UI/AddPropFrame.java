package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CharacteristicType;

public class AddPropFrame extends JDialog
{
	public static final int OK = 1;
	public static final int CANCEL = 0;

	protected int res = CANCEL;
	protected String ch_class;
	String selected = LangModelConfig.getString("label_new_char");
	Map h_named = new HashMap();
	CharacteristicType type = new CharacteristicType();

	private JComboBox jComboBox1 = new JComboBox();
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

		jComboBox1.addItem(LangModelConfig.getString("label_new_char"));
		jComboBox1.addItemListener(new ItemListener()
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
		this.getContentPane().add(jComboBox1, BorderLayout.NORTH);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	void okButton_actionPerformed(ActionEvent e)
	{
		res = OK;
		if (selected.equals(LangModelConfig.getString("label_new_char")))
		{
			type.name = nameField.getText();
			type.description = descrArea.getText();
			type.ch_class = ch_class;
			type.id = String.valueOf(aContext.getDataSourceInterface().GetUId(CharacteristicType.typ));
			Pool.put(CharacteristicType.typ, type.getId(), type);
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
		selected = (String)jComboBox1.getSelectedItem();
		boolean b = selected.equals(LangModelConfig.getString("label_new_char"));
		nameField.setEnabled(b);
		descrArea.setEnabled(b);
		okButton.setEnabled(!b);
		if (!b)
		{
			nameField.setText(((CharacteristicType)h_named.get(selected)).getName());
			descrArea.setText(((CharacteristicType)h_named.get(selected)).description);
			type = (CharacteristicType)h_named.get(selected);
		}
		else
		{
			type = new CharacteristicType();
			nameField.setText("");
			descrArea.setText("");
		}
	}

	public int showDialog(String ch_class, List chars)
	{
		this.ch_class = ch_class;
		Map h = Pool.getMap(CharacteristicType.typ);

		if (h != null)
		{
			Map used = new HashMap(chars.size());
			for (ListIterator it = chars.listIterator(); it.hasNext();)
			{
				List el = (List)it.next();
				used.put(el.get(0), el);
			}

			for (Iterator it = h.values().iterator(); it.hasNext();)
			{
				CharacteristicType type = (CharacteristicType)it.next();
				if (type.ch_class.equals(ch_class) && !used.containsKey(type.getName()))
					jComboBox1.addItem(type.getName());
				h_named.put(type.getName(), type);
			}
		}
		setModal(true);
		setVisible(true);
		return res;
	}

	public String getSelectedName()
	{
		return selected;
	}

	public CharacteristicType getSelectedType()
	{
		return type;
	}
}