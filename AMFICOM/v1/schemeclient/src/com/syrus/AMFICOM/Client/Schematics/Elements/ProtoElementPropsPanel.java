package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ImagesDialog;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeDevice;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class ProtoElementPropsPanel extends JPanel
{
	JPanel compPanel;
	JPanel cl2Panel;
	private JTextField mapProtoTextField = new JTextField();
	private JButton chooseMapProtoButton = new JButton("...");
	private JTextField nameTextField = new JTextField();
	private JTextArea descriptionTextArea = new JTextArea();
	private JTextField manufacturerTextField = new JTextField();
	private JTextField ugoNameTextField = new JTextField();
	private JButton ugoIconButton = new JButton();
	private JButton assignEqtButton = new JButton();
	private String undoType;
	private String undoDescription;
	private String undoManufacturer;
	private String undoUgoName;
	MapProtoElement undo_map_proto;

	ProtoElement proto;
	MapProtoElement map_proto;
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
				eqt.name = nameTextField.getText();
				proto.name = nameTextField.getText();
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
				eqt.description = descriptionTextArea.getText();
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
				eqt.manufacturer = manufacturerTextField.getText();
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
				proto.label = ugoNameTextField.getText();
				aContext.getDispatcher().notify(new SchemeElementsEvent(proto.getId(), proto.label, SchemeElementsEvent.UGO_TEXT_UPDATE_EVENT));
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

	public void init(ProtoElement proto, DataSourceInterface dataSource, boolean show_is_kis)
	{
		this.proto = proto;
		this.map_proto = proto.map_proto;

		if (show_is_kis)
			compPanel.add(cl2Panel, BorderLayout.SOUTH);

		eqt = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);

		if (map_proto == null)
			mapProtoTextField.setText("");
		else
			mapProtoTextField.setText(map_proto.getName());
		mapProtoTextField.setCaretPosition(0);
		mapProtoTextField.setEnabled(false);

		nameTextField.setText(eqt.getName());
		descriptionTextArea.setText(eqt.description);
		manufacturerTextField.setText(eqt.manufacturer);
		ugoNameTextField.setText(proto.label);
		nameTextField.setCaretPosition(0);
		manufacturerTextField.setCaretPosition(0);
		ugoNameTextField.setCaretPosition(0);

		if (!proto.symbol_id.equals(""))
		{
			ImageResource ir = ImageCatalogue.get(proto.symbol_id);

			ImageIcon icon;
			if (ir != null)
				icon = new ImageIcon(ir.getImage());
			else
				icon = new ImageIcon(proto.symbol_id);

			if (icon != null)
			{
				if (icon.getIconHeight() < 20 && icon.getIconWidth() < 20)
					ugoIconButton.setIcon(icon);
				else
					ugoIconButton.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
			}
		}

		undo_map_proto = map_proto;
		undoType = eqt.getName();
		undoDescription = eqt.description;
		undoManufacturer = eqt.manufacturer;
		undoUgoName = proto.label;
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
		if (eqt != null)
		{
			eqt.name = undoType;
			eqt.description = undoDescription;
			eqt.manufacturer = undoManufacturer;
		}
		if (proto != null)
		{
			proto.label = undoUgoName;
			proto.map_proto = undo_map_proto;
		}
		map_proto = undo_map_proto;
	}

	public String getProtoName()
	{
		return nameTextField.getText();
	}

	public MapProtoElement getMapProtoElement ()
	{
		return map_proto;
	}

	void ugoIconButton_actionPerformed()
	{
		ImagesDialog frame = new ImagesDialog(aContext);
		if (!proto.symbol_id.equals(""))
			frame.setImageResource(ImageCatalogue.get(proto.symbol_id));

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
			proto.symbol_id = ir.getId();

			aContext.getDispatcher().notify(new SchemeElementsEvent(proto.getId(), icon, SchemeElementsEvent.UGO_ICON_UPDATE_EVENT));
		}
	}

	void assignEqtButton_actionPerformed()
	{
		EquipmentType eqt = new ChooseEqtDialog().init();
		if (eqt != null)
			proto.equipment_type_id = eqt.getId();

	}

	void chooseMapProtoButton_actionPerformed()
	{
		ChooseMapGroupDialog newMapProtoDialog = new ChooseMapGroupDialog(aContext);
		int ret = newMapProtoDialog.showDialog();
		if (ret == ChooseMapGroupDialog.OK)
		{
			map_proto = newMapProtoDialog.getSelectedElement();
			proto.map_proto = map_proto;
			mapProtoTextField.setText(map_proto.getName());
			mapProtoTextField.setCaretPosition(0);
		}
	}
}

class ChooseEqtDialog extends JDialog
{
	JButton ok = new JButton("OK");
	JButton cancel = new JButton("CANCEL");
	ObjectResourceListBox list = new ObjectResourceListBox();

	ChooseEqtDialog()
	{
		super (Environment.getActiveWindow());
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
		setSize(new Dimension(350, 400));
		setLocation(500, 250);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		JScrollPane scrollPane = new JScrollPane(list);

		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		ok.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				dispose();
			}
		});
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				list.removeSelectionInterval(list.getMinSelectionIndex(), list.getMaxSelectionIndex());
				dispose();
			}
		});

	}

	EquipmentType init()
	{
		list.setContents(EquipmentType.typ);
		setVisible(true);
		return (EquipmentType)list.getSelectedObjectResource();
	}

}
