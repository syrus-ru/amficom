package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ImagesDialog;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;

public class SchemePropsPanel extends JPanel
{
	public JTextField schemeNameTextField = new JTextField();
	public JTextArea schemeDescrTextArea = new JTextArea();
	private JTextField ugoNameTextField = new JTextField();
	private JButton ugoIconButton = new JButton();
	public AComboBox schemeTypeComboBox = new AComboBox(scheme_type_names);

	Scheme scheme;
	ApplicationContext aContext;
	Dispatcher dispatcher;
	boolean show_ugo;

	static String[] scheme_type_names = new String[]
	{
		LangModelSchematics.String(Scheme.NETWORK),
		LangModelSchematics.String(Scheme.CABLESUBNETWORK),
		LangModelSchematics.String(Scheme.BUILDING),
//		Scheme.FLOOR,
//		Scheme.ROOM,
//		Scheme.RACK,
//		Scheme.BAY,
//		Scheme.CARDCAGE
	};

	static String[] scheme_types = new String[]
	{
		Scheme.NETWORK,
		Scheme.CABLESUBNETWORK,
		Scheme.BUILDING,
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
				scheme.name = schemeNameTextField.getText();
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
				scheme.description = schemeDescrTextArea.getText();
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
				scheme.label = ugoNameTextField.getText();
				dispatcher.notify(new SchemeElementsEvent(scheme.getId(), scheme.label, SchemeElementsEvent.UGO_TEXT_UPDATE_EVENT));
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
					scheme.scheme_type = scheme_types[schemeTypeComboBox.getSelectedIndex()];
				}
			}
		});
	}

	public void init(Scheme scheme)
	{
		this.scheme = scheme;
		schemeNameTextField.setText(scheme.getName());
		schemeNameTextField.setCaretPosition(0);
		schemeDescrTextArea.setText(scheme.description);
		ugoNameTextField.setText(scheme.label);
		ugoNameTextField.setCaretPosition(0);

		for (int i = 0; i < scheme_types.length; i++)
		{
			if (scheme_types[i].equals(scheme.scheme_type))
			{
				schemeTypeComboBox.setSelectedIndex(i);
				break;
			}
		}

//		scheme.scheme_type = (String)schemeTypeComboBox.getSelectedItem();

		if (!scheme.symbol_id.equals(""))
		{
			ImageResource ir = ImageCatalogue.get(scheme.symbol_id);

			ImageIcon icon;
			if (ir != null)
				icon = new ImageIcon(ir.getImage());
			else
				icon = new ImageIcon(scheme.symbol_id);

			if (icon != null)
			{
				if (icon.getIconHeight() < 20 && icon.getIconWidth() < 20)
					ugoIconButton.setIcon(icon);
				else
					ugoIconButton.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
			}
		}
		updateUI();
	}

	void ugoIconButton_actionPerformed()
	{
		ImagesDialog frame = new ImagesDialog(aContext);
		if (!scheme.symbol_id.equals(""))
			frame.setImageResource(ImageCatalogue.get(scheme.symbol_id));

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
			ImageResource ir = frame.getImageResource();
			ugoIconButton.setText("");
			ImageIcon icon = new ImageIcon(ir.getImage());
			if (icon.getIconWidth() > 20 || icon.getIconHeight() > 20)
				icon = new ImageIcon (icon.getImage().getScaledInstance(20,	20,	Image.SCALE_SMOOTH));
			ugoIconButton.setIcon(icon);
			scheme.symbol_id = ir.getId();

			dispatcher.notify(new SchemeElementsEvent(scheme.getId(), icon, SchemeElementsEvent.UGO_ICON_UPDATE_EVENT));
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

	public String getSchemeType()
	{
		return scheme_types[schemeTypeComboBox.getSelectedIndex()];
	}
}