package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeDevice;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SchemeElementPropsPanel extends JPanel
{
	JPanel compPanel;
	JPanel cl2Panel;
	private JTextField mapProtoTextField = new JTextField();
	private JTextField nameTextField = new JTextField();
	private JTextField titleTextField = new JTextField();
	private JTextArea descriptionTextArea = new JTextArea();
	private JTextField manufacturerTextField = new JTextField();
	private JTextField ugoNameTextField = new JTextField();
	private JButton ugoIconButton = new JButton();
	private String undoType;
	private String undoDescription;
	private String undoUgoName;

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
		clLabelPanel.add(new JLabel("Группа"));

//		JPanel cIsKisLabelPanel = new JPanel();
//		cIsKisLabelPanel.setPreferredSize(new Dimension (60, 10));

		JPanel titleLabelPanel = new JPanel();
		titleLabelPanel.setPreferredSize(new Dimension (60, 10));
		titleLabelPanel.add(new JLabel("Название"));

		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setPreferredSize(new Dimension (60, 10));
		nameLabelPanel.add(new JLabel("Тип"));

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
				element.name = titleTextField.getText();
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
				element.description = descriptionTextArea.getText();
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
				element.ugo_text = ugoNameTextField.getText();
				aContext.getDispatcher().notify(new SchemeElementsEvent(element.getId(), element.ugo_text, SchemeElementsEvent.UGO_TEXT_UPDATE_EVENT));
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

	public void init(SchemeElement element, DataSourceInterface dataSource, boolean show_is_kis)
	{
		this.element = element;

		if (show_is_kis)
			compPanel.add(cl2Panel, BorderLayout.SOUTH);

		ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, element.proto_element_id);
		if (p != null && p.map_proto != null)
			mapProtoTextField.setText(p.map_proto.getName());
		else
			mapProtoTextField.setText("");
		mapProtoTextField.setCaretPosition(0);

		mapProtoTextField.setEnabled(false);

		titleTextField.setText(element.getName());
		titleTextField.setCaretPosition(0);
		if (!element.proto_element_id.equals(""))
			nameTextField.setText(Pool.getName(ProtoElement.typ, element.proto_element_id));
		else if (!element.scheme_id.equals(""))
			nameTextField.setText(Pool.getName(Scheme.typ, element.scheme_id));
		nameTextField.setCaretPosition(0);

		descriptionTextArea.setText(element.description);
		ugoNameTextField.setText(element.ugo_text);
		ugoNameTextField.setCaretPosition(0);

		if (p != null)
		{
			EquipmentType eqt = (EquipmentType)Pool.get(EquipmentType.typ, p.equipment_type_id);
			if (eqt != null)
				manufacturerTextField.setText(eqt.manufacturer);
			else
				manufacturerTextField.setText("");
			manufacturerTextField.setCaretPosition(0);
		}
		else
			manufacturerTextField.setText("");
		//manufacturerTextField.setEnabled(false);

		undoType = element.getName();
		undoDescription = element.description;
		undoUgoName = element.ugo_text;
		updateUI();
	}

	boolean hasCablePort (ProtoElement proto)
	{
		for (int i = 0; i < proto.devices.size(); i++)
		{
			SchemeDevice dev = (SchemeDevice)proto.devices.get(i);
			if (!dev.cableports.isEmpty())
				return true;
		}

		for (int i = 0; i < proto.protoelement_ids.size(); i++)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)proto.protoelement_ids.get(i));
			if (hasCablePort(p))
				return true;
		}
		return false;
	}

	public void undo()
	{
		if (element != null)
		{
			element.name = undoType;
			element.description = undoDescription;
			element.ugo_text = undoUgoName;
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

