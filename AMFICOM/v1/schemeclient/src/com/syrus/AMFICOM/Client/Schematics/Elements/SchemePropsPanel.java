package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;
import com.syrus.AMFICOM.resource.*;

public class SchemePropsPanel extends JPanel
{
	public JTextField schemeNameTextField = new JTextField();
	public JTextArea schemeDescrTextArea = new JTextArea();
	private JTextField ugoNameTextField = new JTextField();
	private JButton ugoIconButton = new JButton();
	public AComboBox schemeTypeComboBox = new AComboBox(schemeType_names);

	Scheme scheme;
	ApplicationContext aContext;
	Dispatcher dispatcher;
	boolean show_ugo;

	static String[] schemeType_names = new String[]
	{
		LangModelSchematics.getString("NETWORK"),
		LangModelSchematics.getString("CABLE_SUBNETWORK"),
		LangModelSchematics.getString("BUILDING"),
//		Scheme.FLOOR,
//		Scheme.ROOM,
//		Scheme.RACK,
//		Scheme.BAY,
//		Scheme.CARDCAGE
	};

	static Type[] schemeTypes = new Type[]
	{
		Type.NETWORK,
		Type.CABLE_SUBNETWORK,
		Type.BUILDING,
//		Scheme.FLOOR,
//		Scheme.ROOM,
//		Scheme.RACK,
//		Scheme.BAY,
//		Scheme.CARDCAGE
	};

	public SchemePropsPanel(ApplicationContext aContext, Dispatcher dispatcher)
	{
		this(aContext, dispatcher, true);
	}

	public SchemePropsPanel(ApplicationContext aContext, Dispatcher dispatcher, boolean show_ugo)
	{
		this.aContext = aContext;
		this.dispatcher = dispatcher;
		this.show_ugo = show_ugo;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setLayout(new BorderLayout());

		JPanel panel1 = new JPanel(new BorderLayout());
		JPanel panel21 = new JPanel(new BorderLayout());
		JPanel panel22 = new JPanel(new BorderLayout());
		JPanel panel3 = new JPanel(new BorderLayout());

		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setPreferredSize(new Dimension (60, 10));
		nameLabelPanel.add(new JLabel ("Название"));
		JPanel descrLabelPanel = new JPanel();
		descrLabelPanel.setPreferredSize(new Dimension (60, 10));
		descrLabelPanel.add(new JLabel ("Описание"));
		JPanel symbolLabelPanel = new JPanel();
		symbolLabelPanel.setPreferredSize(new Dimension (60, 10));
		symbolLabelPanel.add(new JLabel("Символ"));
		JPanel schemeTypeLabelPanel = new JPanel();
		schemeTypeLabelPanel.setPreferredSize(new Dimension (60, 10));
		schemeTypeLabelPanel.add(new JLabel("Тип"));

		panel1.add(nameLabelPanel, BorderLayout.WEST);
		panel1.add(schemeNameTextField, BorderLayout.CENTER);
		panel21.add(descrLabelPanel, BorderLayout.WEST);
		JScrollPane scroll = new JScrollPane(schemeDescrTextArea);
		schemeDescrTextArea.setWrapStyleWord(true);
		schemeDescrTextArea.setLineWrap(true);
		panel21.add(scroll, BorderLayout.CENTER);
		panel22.add(schemeTypeLabelPanel, BorderLayout.WEST);
		panel22.add(schemeTypeComboBox, BorderLayout.CENTER);

		ugoIconButton.setPreferredSize(new Dimension(22, 22));
		ugoIconButton.setBorder(BorderFactory.createEtchedBorder());
		ugoIconButton.setFocusPainted(false);
		panel3.add(ugoNameTextField, BorderLayout.CENTER);
		panel3.add(symbolLabelPanel, BorderLayout.WEST);
		panel3.add(ugoIconButton, BorderLayout.EAST);

		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.add(panel21, BorderLayout.CENTER);
		panel2.add(panel22, BorderLayout.SOUTH);
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.CENTER);
		if (show_ugo)
			add(panel3, BorderLayout.SOUTH);


		schemeNameTextField.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (scheme == null)
					return;
				scheme.name(schemeNameTextField.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		schemeDescrTextArea.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (scheme == null)
					return;
				scheme.description(schemeDescrTextArea.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

		ugoIconButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				ugoIconButton_actionPerformed();
			}
		});
		ugoNameTextField.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (scheme == null)
					return;
				scheme.label(ugoNameTextField.getText());
				dispatcher.notify(new SchemeElementsEvent(scheme, scheme.label(), SchemeElementsEvent.UGO_TEXT_UPDATE_EVENT));
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		schemeTypeComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (scheme == null)
					return;
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					scheme.type(schemeTypes[schemeTypeComboBox.getSelectedIndex()]);
				}
			}
		});
	}

	public void init(Scheme scheme)
	{
		this.scheme = scheme;
		schemeNameTextField.setText(scheme.name());
		schemeNameTextField.setCaretPosition(0);
		schemeDescrTextArea.setText(scheme.description());
		ugoNameTextField.setText(scheme.label());
		ugoNameTextField.setCaretPosition(0);

		for (int i = 0; i < schemeTypes.length; i++)
		{
			if (schemeTypes[i].equals(scheme.type()))
			{
				schemeTypeComboBox.setSelectedIndex(i);
				break;
			}
		}

//		scheme.schemeType = (String)schemeTypeComboBox.getSelectedItem();

		if (scheme.symbol() != null)
		{
			BitmapImageResource ir = scheme.symbolImpl();
			ImageIcon icon = new ImageIcon(ir.getImage());
			if (icon.getIconHeight() < 20 && icon.getIconWidth() < 20)
				ugoIconButton.setIcon(icon);
			else
				ugoIconButton.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		}
		updateUI();
	}

	void ugoIconButton_actionPerformed()
	{
		ImagesDialog frame = new ImagesDialog(aContext);
		if (scheme.symbol() != null)
			frame.setImageResource(scheme.symbolImpl());

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setModal(true);
		frame.setVisible(true);

		if(frame.ret_code == 1)
		{
			BitmapImageResource ir = frame.getImageResource();
			ugoIconButton.setText("");
			ImageIcon icon = new ImageIcon(ir.getImage());
			if (icon.getIconWidth() > 20 || icon.getIconHeight() > 20)
				icon = new ImageIcon (icon.getImage().getScaledInstance(20,	20,	Image.SCALE_SMOOTH));
			ugoIconButton.setIcon(icon);
			scheme.symbolImpl(ir);

			dispatcher.notify(new SchemeElementsEvent(scheme, icon, SchemeElementsEvent.UGO_ICON_UPDATE_EVENT));
		}
	}

	public void setEditable (boolean b)
	{
		schemeNameTextField.setEnabled(b);
		schemeDescrTextArea.setEnabled(b);
		schemeTypeComboBox.setEnabled(b);
		ugoIconButton.setEnabled(b);
		ugoNameTextField.setEnabled(b);
	}

	public String getSchemeName()
	{
		return schemeNameTextField.getText();
	}

	public String getSchemeDescription()
	{
		return schemeDescrTextArea.getText();
	}

	public Type getSchemeType()
	{
		return schemeTypes[schemeTypeComboBox.getSelectedIndex()];
	}
}

