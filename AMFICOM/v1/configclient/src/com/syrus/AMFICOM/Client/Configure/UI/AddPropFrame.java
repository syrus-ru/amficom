package com.syrus.AMFICOM.Client.Configure.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CharacteristicType;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AddPropFrame extends JDialog
{
	public static final int OK = 1;
	public static final int CANCEL = 0;

	protected int res = CANCEL;
	protected String ch_class;
	String selected = LangModelConfig.getString("label_new_char");
	Hashtable h;
	Hashtable h_named = new Hashtable();
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
			/*h = Pool.getHash(CharacteristicType.typ);
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

	public int showDialog(String ch_class, Vector chars)
	{
		this.ch_class = ch_class;
		h = Pool.getHash(CharacteristicType.typ);

		Hashtable used = new Hashtable(chars.size());
		for (int i = 0; i < chars.size(); i++)
		{
			Vector el = (Vector)chars.get(i);
			used.put(el.get(0), el);
		}
		if (h != null)
		{
			for (Enumeration enum = h.elements(); enum.hasMoreElements();)
			{
				CharacteristicType type = (CharacteristicType)enum.nextElement();
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