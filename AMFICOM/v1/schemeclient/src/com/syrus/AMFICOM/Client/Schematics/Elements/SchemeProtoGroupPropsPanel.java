package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.ImagesDialog;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class SchemeProtoGroupPropsPanel extends JPanel
{
	private JTextField mapProtoNameTextField = new JTextField();
	private JTextArea groupDescrTextArea = new JTextArea();
	private JButton imageButton = new JButton();
	private JCheckBox isKisCheckBox = new JCheckBox();
	String image_id = "";

	SchemeProtoGroup scheme_proto = new SchemeProtoGroup();
	ApplicationContext aContext;

	public SchemeProtoGroupPropsPanel(ApplicationContext aContext)
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

	public void init(SchemeProtoGroup scheme_proto, DataSourceInterface dataSource)
	{
		this.scheme_proto = scheme_proto;
		mapProtoNameTextField.setText(scheme_proto.getName());
		mapProtoNameTextField.setCaretPosition(0);
		groupDescrTextArea.setText(scheme_proto.description);
//		isKisCheckBox.setSelected(scheme_proto.is_visual);

		ImageIcon icon;
		if (scheme_proto.getImageID().equals(""))
			icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
		else
			icon = new ImageIcon(ImageCatalogue.get(scheme_proto.getImageID()).getImage()
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH));

		imageButton.setIcon(icon);
		updateUI();
	}

	private void imageButton_actionPerformed(ActionEvent e)
	{
		ImagesDialog frame = new ImagesDialog(aContext);
		frame.setImageResource(ImageCatalogue.get(image_id.equals("") ? "pc" : image_id));

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
			imageButton.setIcon(new ImageIcon(ir.getImage().getScaledInstance(
					50,	50,	Image.SCALE_SMOOTH)));
			image_id = ir.getId();
		}
	}

	public SchemeProtoGroup getSchemeProtoGroup()
	{
		updateSchemeProtoGroup();
		return scheme_proto;
	}

	public void updateSchemeProtoGroup()
	{
		if (mapProtoNameTextField.getText().equals(""))
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Не задано название группы.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		scheme_proto.name = mapProtoNameTextField.getText();
		scheme_proto.description = groupDescrTextArea.getText();
		scheme_proto.setImageID(image_id);
	}
}

