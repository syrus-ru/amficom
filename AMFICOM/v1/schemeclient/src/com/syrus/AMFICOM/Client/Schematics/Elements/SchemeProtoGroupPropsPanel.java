package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
import com.syrus.AMFICOM.client_.general.ui_.ImagesDialog;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;

public class SchemeProtoGroupPropsPanel extends JPanel
{
	private JTextField mapProtoNameTextField = new JTextField();
	private JTextArea groupDescrTextArea = new JTextArea();
	private JButton imageButton = new JButton();
	private JCheckBox isKisCheckBox = new JCheckBox();
	BitmapImageResource image;

	SchemeProtoGroup scheme_proto;
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

		Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
		try {
			File f = new File("images/folder.gif");
			byte[] buf = new byte[(int)f.length()];
			FileInputStream fis = new FileInputStream(f);
			fis.read(buf);
			fis.close();
			image = BitmapImageResource.createInstance(user_id, "", buf);
		}
		catch (CreateObjectException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex) {
			ex.printStackTrace();
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
		nameLabelPanel.add(new JLabel("��������"));
		JPanel descrLabelPanel = new JPanel();
		descrLabelPanel.setPreferredSize(new Dimension (60, 10));
		descrLabelPanel.add(new JLabel("��������"));
		JPanel isKisLabelPanel = new JPanel();
		isKisLabelPanel.setPreferredSize(new Dimension (60, 10));

		isKisCheckBox.setText("������ �������������� �����������");
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

	public void init(SchemeProtoGroup scheme_proto)
	{
		this.scheme_proto = scheme_proto;
		mapProtoNameTextField.setText(scheme_proto.getName());
		mapProtoNameTextField.setCaretPosition(0);
		groupDescrTextArea.setText(scheme_proto.getDescription());
//		isKisCheckBox.setSelected(scheme_proto.is_visual);

		ImageIcon icon;
		if (scheme_proto.getSymbol() == null)
			icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
		else
			icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(scheme_proto.getSymbol().
					getImage()).getScaledInstance(50, 50, Image.SCALE_SMOOTH));

		imageButton.setIcon(icon);
		updateUI();
	}

	void imageButton_actionPerformed(ActionEvent e)
	{
		ImagesDialog frame = new ImagesDialog(aContext);
//		frame.setImageResource(image == null ? : image);

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
			imageButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(ir.getImage()).
					getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
			image = ir;
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
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "�� ������ �������� ������.", "������", JOptionPane.OK_OPTION);
			return;
		}

		scheme_proto.setName(mapProtoNameTextField.getText());
		scheme_proto.setDescription(groupDescrTextArea.getText());
		scheme_proto.setSymbol(image);
	}
}

