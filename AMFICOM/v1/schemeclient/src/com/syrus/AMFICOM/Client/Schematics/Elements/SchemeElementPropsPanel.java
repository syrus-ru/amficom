package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeElement;

public class SchemeElementPropsPanel extends JPanel
{
	JPanel compPanel;
	JPanel cl2Panel;
	JTextField mapProtoTextField = new JTextField();
	JTextField nameTextField = new JTextField();
	JTextField titleTextField = new JTextField();
	JTextArea descriptionTextArea = new JTextArea();
	JTextField manufacturerTextField = new JTextField();
	JTextField ugoNameTextField = new JTextField();
	JButton ugoIconButton = new JButton();
	String undoType;
	String undoDescription;
	String undoUgoName;

	SchemeElement element;
	ApplicationContext aContext;

	public SchemeElementPropsPanel(ApplicationContext aContext)
	{
		this.aContext = aContext;
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
		JPanel classPanel = new JPanel(new BorderLayout());
		compPanel = new JPanel(new BorderLayout());

		JPanel cl1Panel = new JPanel(new BorderLayout());
		cl2Panel = new JPanel(new BorderLayout());
		JPanel co0Panel = new JPanel(new BorderLayout());
		JPanel co1Panel = new JPanel(new BorderLayout());
		JPanel co2Panel = new JPanel(new BorderLayout());
		JPanel co3Panel = new JPanel(new BorderLayout());
		JPanel co4Panel = new JPanel(new BorderLayout());

		setLayout(new BorderLayout());
		add(classPanel, BorderLayout.NORTH);
		add(compPanel, BorderLayout.CENTER);

		JPanel clLabelPanel = new JPanel();
		clLabelPanel.setPreferredSize(new Dimension (60, 10));
		clLabelPanel.add(new JLabel("������"));

//		JPanel cIsKisLabelPanel = new JPanel();
//		cIsKisLabelPanel.setPreferredSize(new Dimension (60, 10));

		JPanel titleLabelPanel = new JPanel();
		titleLabelPanel.setPreferredSize(new Dimension (60, 10));
		titleLabelPanel.add(new JLabel("��������"));

		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setPreferredSize(new Dimension (60, 10));
		nameLabelPanel.add(new JLabel("���"));

		JPanel descrLabelPanel = new JPanel();
		descrLabelPanel.setPreferredSize(new Dimension (60, 10));
		descrLabelPanel.add(new JLabel("��������"));

		JPanel symbolLabelPanel = new JPanel();
		symbolLabelPanel.setPreferredSize(new Dimension (60, 10));
		symbolLabelPanel.add(new JLabel("������"));

		JPanel manLabelPanel = new JPanel();
		manLabelPanel.setPreferredSize(new Dimension (60, 10));
		manLabelPanel.add(new JLabel("������."));

//		isKisCheckBox.setText("�������������� ���������");
//		isKisCheckBox.setFocusPainted(false);
//		isKisCheckBox.addChangeListener(new MyChangeListener());

		ugoIconButton.setPreferredSize(new Dimension(22, 22));
		ugoIconButton.setBorder(BorderFactory.createEtchedBorder());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().add(descriptionTextArea);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setAutoscrolls(true);
		descriptionTextArea.setWrapStyleWord(true);

		cl1Panel.add(clLabelPanel, BorderLayout.WEST);
		co0Panel.add(titleLabelPanel, BorderLayout.WEST);
		co1Panel.add(nameLabelPanel, BorderLayout.WEST);
		co2Panel.add(descrLabelPanel, BorderLayout.WEST);
		co3Panel.add(manLabelPanel, BorderLayout.WEST);
		co4Panel.add(symbolLabelPanel, BorderLayout.WEST);
		cl1Panel.add(mapProtoTextField, BorderLayout.CENTER);
		co0Panel.add(titleTextField, BorderLayout.CENTER);
		co1Panel.add(nameTextField, BorderLayout.CENTER);
		co2Panel.add(scrollPane, BorderLayout.CENTER);
		co3Panel.add(manufacturerTextField, BorderLayout.CENTER);
		co4Panel.add(ugoNameTextField, BorderLayout.CENTER);
		co4Panel.add(ugoIconButton, BorderLayout.EAST);

//		classPanel.add(cl1Panel, BorderLayout.CENTER);
		classPanel.add(co0Panel, BorderLayout.NORTH);
		classPanel.add(co1Panel, BorderLayout.SOUTH);
		compPanel.add(co3Panel, BorderLayout.NORTH);
		compPanel.add(co2Panel, BorderLayout.CENTER);
		//compPanel.add(co4Panel, BorderLayout.SOUTH);

		ugoIconButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				ugoIconButton_actionPerformed();
			}
		});

		titleTextField.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (element == null)
					return;
				element.setName(titleTextField.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		descriptionTextArea.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (element == null)
					return;
				element.setDescription(descriptionTextArea.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		ugoNameTextField.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (element == null)
					return;
				element.setLabel(ugoNameTextField.getText());
				aContext.getDispatcher().notify(new SchemeElementsEvent(element.getId(), element.getLabel(), SchemeElementsEvent.UGO_TEXT_UPDATE_EVENT));
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

		nameTextField.setEnabled(false);
	}

	public void setEditable(boolean b)
	{
		titleTextField.setEnabled(b);
		descriptionTextArea.setEnabled(b);
		manufacturerTextField.setEnabled(b);
		ugoNameTextField.setEnabled(b);
		ugoIconButton.setEnabled(b);
	}

	public void init(SchemeElement element, boolean show_is_kis)
	{
		this.element = element;

		if (show_is_kis)
			compPanel.add(cl2Panel, BorderLayout.SOUTH);

		this.mapProtoTextField.setText(""); //$NON-NLS-1$
		mapProtoTextField.setCaretPosition(0);

		mapProtoTextField.setEnabled(false);

		titleTextField.setText(element.getName());
		titleTextField.setCaretPosition(0);
		if (element.getScheme() != null)
			nameTextField.setText(element.getScheme().getName());
		nameTextField.setText(element.getName());
		nameTextField.setCaretPosition(0);

		descriptionTextArea.setText(element.getDescription());
		ugoNameTextField.setText(element.getLabel());
		ugoNameTextField.setCaretPosition(0);

		if (element != null)
		{
			EquipmentType eqt = element.getEquipmentType();
			if (eqt != null)
				manufacturerTextField.setText(eqt.getManufacturer());
			else
				manufacturerTextField.setText("");
			manufacturerTextField.setCaretPosition(0);
		}
		else
			manufacturerTextField.setText("");
		//manufacturerTextField.setEnabled(false);

		undoType = element.getName();
		undoDescription = element.getDescription();
		undoUgoName = element.getLabel();
		updateUI();
	}

	public void undo()
	{
		if (element != null)
		{
			element.setName(undoType);
			element.setDescription(undoDescription);
			element.setLabel(undoUgoName);
		}
	}

	public String getSchemeElementName()
	{
		return titleTextField.getText();
	}

	void ugoIconButton_actionPerformed()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new ChoosableFileFilter("gif", "Icon image"));
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			ImageIcon icon = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
			if (icon.getIconHeight() < 20 && icon.getIconWidth() < 20)
				ugoIconButton.setIcon(icon);
			else
				ugoIconButton.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
		}
	}
}

