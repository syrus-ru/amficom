package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.resource.*;

public class ProtoElementPropsPanel extends JPanel
{
	JPanel compPanel;
	JPanel cl2Panel;
	JTextField mapProtoTextField = new JTextField();
	JButton chooseMapProtoButton = new JButton("...");
	JTextField nameTextField = new JTextField();
	JTextArea descriptionTextArea = new JTextArea();
	JTextField manufacturerTextField = new JTextField();
	JTextField ugoNameTextField = new JTextField();
	JButton ugoIconButton = new JButton();
	JButton assignEqtButton = new JButton();
	String undoType;
	String undoDescription;
	String undoManufacturer;
	String undoUgoName;
	SchemeProtoGroup undo_scheme_proto;

	SchemeProtoElement proto;
	SchemeProtoGroup scheme_proto;
	EquipmentType eqt;
	ApplicationContext aContext;

	public ProtoElementPropsPanel(ApplicationContext aContext)
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
		JPanel co1Panel = new JPanel(new BorderLayout());
		JPanel co2Panel = new JPanel(new BorderLayout());
		JPanel co3Panel = new JPanel(new BorderLayout());
		JPanel co4Panel = new JPanel(new BorderLayout());

		setLayout(new BorderLayout());
		add(classPanel, BorderLayout.NORTH);
		add(compPanel, BorderLayout.CENTER);

		JPanel clLabelPanel = new JPanel();
		clLabelPanel.setPreferredSize(new Dimension (60, 10));
		clLabelPanel.add(new JLabel("Группа"));

//		JPanel cIsKisLabelPanel = new JPanel();
//		cIsKisLabelPanel.setPreferredSize(new Dimension (60, 10));

		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setPreferredSize(new Dimension (60, 10));
		nameLabelPanel.add(new JLabel("Название"));

		JPanel descrLabelPanel = new JPanel();
		descrLabelPanel.setPreferredSize(new Dimension (60, 10));
		descrLabelPanel.add(new JLabel("Описание"));

		JPanel symbolLabelPanel = new JPanel();
		symbolLabelPanel.setPreferredSize(new Dimension (60, 10));
		symbolLabelPanel.add(new JLabel("Символ"));

		JPanel manLabelPanel = new JPanel();
		manLabelPanel.setPreferredSize(new Dimension (60, 10));
		manLabelPanel.add(new JLabel("Произв."));

//		isKisCheckBox.setText("Топологический компонент");
//		isKisCheckBox.setFocusPainted(false);
//		isKisCheckBox.addChangeListener(new MyChangeListener());

		ugoIconButton.setPreferredSize(new Dimension(22, 22));
		ugoIconButton.setBorder(BorderFactory.createEtchedBorder());
		ugoIconButton.setToolTipText("Выбор картинки");
		ugoIconButton.setFocusPainted(false);
		assignEqtButton.setPreferredSize(new Dimension(22, 22));
		assignEqtButton.setBorder(BorderFactory.createEtchedBorder());
		assignEqtButton.setToolTipText("Определить тип");
		assignEqtButton.setFocusPainted(false);

		chooseMapProtoButton.setPreferredSize(new Dimension(22, 22));
		chooseMapProtoButton.setBorder(BorderFactory.createEtchedBorder());
		chooseMapProtoButton.setToolTipText("Выбор группы");
		chooseMapProtoButton.setFocusPainted(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().add(descriptionTextArea);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setAutoscrolls(true);
		descriptionTextArea.setWrapStyleWord(true);

		cl1Panel.add(clLabelPanel, BorderLayout.WEST);
//		cl2Panel.add(cIsKisLabelPanel, BorderLayout.WEST);
		co1Panel.add(nameLabelPanel, BorderLayout.WEST);
		co2Panel.add(descrLabelPanel, BorderLayout.WEST);
		co3Panel.add(manLabelPanel, BorderLayout.WEST);
		co4Panel.add(symbolLabelPanel, BorderLayout.WEST);
		cl1Panel.add(mapProtoTextField, BorderLayout.CENTER);
//		cl2Panel.add(isKisCheckBox, BorderLayout.CENTER);
		co1Panel.add(nameTextField, BorderLayout.CENTER);
		co2Panel.add(scrollPane, BorderLayout.CENTER);
		co3Panel.add(manufacturerTextField, BorderLayout.CENTER);
		co4Panel.add(ugoNameTextField, BorderLayout.CENTER);
		co1Panel.add(assignEqtButton, BorderLayout.EAST);
		cl1Panel.add(chooseMapProtoButton, BorderLayout.EAST);
		co4Panel.add(ugoIconButton, BorderLayout.EAST);

		classPanel.add(cl1Panel, BorderLayout.CENTER);
		classPanel.add(co1Panel, BorderLayout.SOUTH);
		compPanel.add(co3Panel, BorderLayout.NORTH);
		compPanel.add(co2Panel, BorderLayout.CENTER);
		compPanel.add(co4Panel, BorderLayout.SOUTH);

		chooseMapProtoButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				chooseMapProtoButton_actionPerformed();
			}
		});
		ugoIconButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				ugoIconButton_actionPerformed();
			}
		});
		assignEqtButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				assignEqtButton_actionPerformed();
			}
		});
		nameTextField.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (eqt == null || proto == null)
					return;
				eqt.setName(nameTextField.getText());
				proto.name(nameTextField.getText());
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
				if (eqt == null)
					return;
				eqt.setDescription(descriptionTextArea.getText());
			}
			public void keyPressed(KeyEvent ae)
					{}
		});
		manufacturerTextField.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent ae)
					{ }
			public void keyReleased(KeyEvent ae)
			{
				if (eqt == null)
					return;
				eqt.setManufacturer(manufacturerTextField.getText());
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
				if (proto == null)
					return;
				proto.label(ugoNameTextField.getText());
				aContext.getDispatcher().notify(new SchemeElementsEvent(proto.id(), proto.label(), SchemeElementsEvent.UGO_TEXT_UPDATE_EVENT));
			}
			public void keyPressed(KeyEvent ae)
					{}
		});

	}

	public void setEditable(boolean b)
	{
		chooseMapProtoButton.setEnabled(b);
		nameTextField.setEnabled(b);
		descriptionTextArea.setEnabled(b);
		manufacturerTextField.setEnabled(b);
		ugoNameTextField.setEnabled(b);
		ugoIconButton.setEnabled(b);
		assignEqtButton.setEnabled(b);
	}

	public void init(SchemeProtoElement proto, boolean show_is_kis)
	{
		this.proto = proto;
		this.scheme_proto = proto.parent();

		if (show_is_kis)
			compPanel.add(cl2Panel, BorderLayout.SOUTH);

		eqt = proto.equipmentTypeImpl();

		if (scheme_proto == null)
			mapProtoTextField.setText("");
		else
			mapProtoTextField.setText(scheme_proto.name());
		mapProtoTextField.setCaretPosition(0);
		mapProtoTextField.setEnabled(false);

		nameTextField.setText(proto.name());
		descriptionTextArea.setText(eqt.getDescription());
		manufacturerTextField.setText(eqt.getManufacturer());
		ugoNameTextField.setText(proto.label());
		nameTextField.setCaretPosition(0);
		manufacturerTextField.setCaretPosition(0);
		ugoNameTextField.setCaretPosition(0);

		if (proto.symbol() != null)
		{
			BitmapImageResource ir = proto.symbolImpl();

			ImageIcon icon = null;
			if (ir != null)
				icon = new ImageIcon(ir.getImage());

			if (icon != null)
			{
				if (icon.getIconHeight() < 20 && icon.getIconWidth() < 20)
					ugoIconButton.setIcon(icon);
				else
					ugoIconButton.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
			}
		}

		undo_scheme_proto = scheme_proto;
		undoType = eqt.getName();
		undoDescription = eqt.getDescription();
		undoManufacturer = eqt.getManufacturer();
		undoUgoName = proto.label();
		updateUI();
	}

	boolean hasCablePort (SchemeProtoElement proto)
	{
		SchemeDevice[] devices = proto.devices();
		for (int i = 0; i < devices.length; i++)
		{
			if (devices[i].schemeCablePorts().length != 0)
				return true;
		}

		SchemeProtoElement[] protos = proto.protoElements();
		for (int i = 0; i < protos.length; i++)
		{
			if (hasCablePort(protos[i]))
				return true;
		}
		return false;
	}

	public void undo()
	{
		if (eqt != null)
		{
			eqt.setName(undoType);
			eqt.setDescription(undoDescription);
			eqt.setManufacturer(undoManufacturer);
		}
		if (proto != null)
		{
			proto.label(undoUgoName);
			proto.parent(undo_scheme_proto);
		}
		scheme_proto = undo_scheme_proto;
	}

	public String getProtoName()
	{
		return nameTextField.getText();
	}

	public SchemeProtoGroup getSchemeProtoGroup()
	{
		return scheme_proto;
	}

	void ugoIconButton_actionPerformed()
	{
		ImagesDialog frame = new ImagesDialog(aContext);
		if (proto.symbol() != null)
			frame.setImageResource(proto.symbolImpl());

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
			proto.symbolImpl(ir);

			aContext.getDispatcher().notify(new SchemeElementsEvent(proto.id(), icon, SchemeElementsEvent.UGO_ICON_UPDATE_EVENT));
		}
	}

	void assignEqtButton_actionPerformed()
	{
//		EquipmentType eqt = new ChooseEqtDialog().init();
//		if (eqt != null)
//			proto.equipmentTypeImpl(eqt);

	}

	void chooseMapProtoButton_actionPerformed()
	{
		ChooseMapGroupDialog newMapProtoDialog = new ChooseMapGroupDialog(aContext);
		int ret = newMapProtoDialog.showDialog();
		if (ret == ChooseMapGroupDialog.OK)
		{
			if (proto.parent() != null)
				Arrays.asList(proto.parent().schemeProtoElements()).remove(proto);
			scheme_proto = newMapProtoDialog.getSelectedElement();
			List protos = Arrays.asList(scheme_proto.schemeProtoGroups());
			if (!protos.contains(proto))
				protos.add(proto);
			proto.parent(scheme_proto);
			mapProtoTextField.setText(scheme_proto.name());
			mapProtoTextField.setCaretPosition(0);
		}
	}
}
