package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ImagesDialog;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;

public class MapProtoElementPropsPanel extends JPanel
{
	private JTextField mapProtoNameTextField = new JTextField();
	private JTextArea groupDescrTextArea = new JTextArea();
	private JButton imageButton = new JButton("icon");
	private JCheckBox isKisCheckBox = new JCheckBox();
	String image_id = "pc";

	MapProtoElement map_proto = new MapProtoElement();
	ApplicationContext aContext;

	public MapProtoElementPropsPanel(ApplicationContext aContext)
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
		JPanel namePanel = new JPanel(new BorderLayout());
		JPanel descrPanel = new JPanel(new BorderLayout());
		JPanel isKisPanel = new JPanel(new BorderLayout());

		setLayout(new BorderLayout());
		add(namePanel, BorderLayout.NORTH);
		add(descrPanel, BorderLayout.CENTER);
		add(isKisPanel, BorderLayout.SOUTH);

		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setPreferredSize(new Dimension (60, 10));
		nameLabelPanel.add(new JLabel("Название"));
		JPanel descrLabelPanel = new JPanel();
		descrLabelPanel.setPreferredSize(new Dimension (60, 10));
		descrLabelPanel.add(new JLabel("Описание"));
		JPanel isKisLabelPanel = new JPanel();
		isKisLabelPanel.setPreferredSize(new Dimension (60, 10));

		isKisCheckBox.setText("Группа топологических компонентов");
		isKisCheckBox.setFocusPainted(false);

		namePanel.add(nameLabelPanel, BorderLayout.WEST);
		namePanel.add(mapProtoNameTextField, BorderLayout.CENTER);
		descrPanel.add(descrLabelPanel, BorderLayout.WEST);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getViewport().add(groupDescrTextArea);
		groupDescrTextArea.setLineWrap(true);
		groupDescrTextArea.setAutoscrolls(true);
		groupDescrTextArea.setWrapStyleWord(true);

		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new BorderLayout());
		imagePanel.add(imageButton, BorderLayout.NORTH);
		imagePanel.add(new JPanel(), BorderLayout.CENTER);

		descrPanel.add(scrollPane, BorderLayout.CENTER);
		descrPanel.add(imagePanel, BorderLayout.EAST);
		isKisPanel.add(isKisLabelPanel, BorderLayout.WEST);
		isKisPanel.add(isKisCheckBox, BorderLayout.CENTER);

		imageButton.setBorder(BorderFactory.createEtchedBorder());
		imageButton.setPreferredSize(new Dimension(54, 54));
		imageButton.setMaximumSize(new Dimension(54, 54));
		imageButton.setFocusPainted(false);
		imageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				imageButton_actionPerformed(ev);
			}
		});
	}

	public void setEditable(boolean b)
	{
		mapProtoNameTextField.setEnabled(b);
		groupDescrTextArea.setEnabled(b);
		imageButton.setEnabled(b);
		isKisCheckBox.setEnabled(b);
	}

	public void init(MapProtoElement mapproto, DataSourceInterface dataSource)
	{
		this.map_proto = mapproto;
		mapProtoNameTextField.setText(map_proto.getName());
		mapProtoNameTextField.setCaretPosition(0);
		groupDescrTextArea.setText(map_proto.description);
		isKisCheckBox.setSelected(map_proto.is_visual);
		image_id = (map_proto.getImageID().equals("") ? "pc" : map_proto.getImageID());
		map_proto.setImageID(image_id);

		ImageResource ir = ImageCatalogue.get(image_id);
		imageButton.setText("");
		if (ir != null)
			imageButton.setIcon(new ImageIcon(ir.getImage().getScaledInstance(
					50, 50, Image.SCALE_SMOOTH)));

		updateUI();
	}

	private void imageButton_actionPerformed(ActionEvent e)
	{
		ImagesDialog frame = new ImagesDialog(aContext);
		frame.setImageResource(ImageCatalogue.get(image_id));

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
			imageButton.setText("");
			imageButton.setIcon(new ImageIcon(ir.getImage().getScaledInstance(
					50,	50,	Image.SCALE_SMOOTH)));
			image_id = ir.getId();
		}
	}

	public MapProtoElement getMapProtoElement()
	{
		return map_proto;
	}

	public MapProtoElement createMapProtoElement()
	{
		if (mapProtoNameTextField.getText().equals(""))
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Не задано название группы.", "Ошибка", JOptionPane.OK_OPTION);
			return null;
		}

		MapProtoElement new_proto = new MapProtoElement();
		new_proto.id = aContext.getDataSourceInterface().GetUId(MapProtoElement.typ);
		new_proto.name = mapProtoNameTextField.getText();
		new_proto.description = groupDescrTextArea.getText();
		new_proto.domain_id = aContext.getSessionInterface().getDomainId();
		new_proto.is_visual = isKisCheckBox.isSelected();
		new_proto.setImageID(((image_id == null || image_id.equals("")) ? "pc" : image_id));

		Pool.put(MapProtoElement.typ, new_proto.getId(), new_proto);

		map_proto = new_proto;
		return new_proto;
	}
}

