package com.syrus.AMFICOM.Client.Configure.Map;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import oracle.jdeveloper.layout.*;
import java.awt.event.*;

import javax.swing.filechooser.*;

import com.ofx.base.SxEnvironment;
import com.ofx.repository.*;
import oracle.jdeveloper.layout.XYConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class MapOptionsDialog extends JDialog 
{

	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	XYLayout xYLayout1 = new XYLayout();
	JLabel jLabel2 = new JLabel();
	JComboBox typeComboBox = new JComboBox();
	JPanel jPanel2 = new JPanel();
	XYLayout xYLayout2 = new XYLayout();
	JButton selectButton = new JButton();
	JTextField pathField = new JTextField();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JComboBox mapComboBox = new JComboBox();
	JButton okButton = new JButton();
	JButton cancelButton = new JButton();

	public int ret_code = 0;
	public static final int OK_CODE = 1;
	public static final int CANCEL_CODE = 2;

	public String path = "";
	public String map = "";
	private JPanel jPanel3 = new JPanel();
	private JPanel jPanel4 = new JPanel();

	public MapOptionsDialog()
	{
		this(null, "Параметры картографического отображения", false);
	}

	public MapOptionsDialog(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
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
		this.setResizable(false);
		this.setSize(new Dimension(500, 450));
		this.getContentPane().setLayout(borderLayout1);
		jPanel1.setLayout(xYLayout1);
		jPanel1.setMinimumSize(new Dimension(385, 270));
		jPanel1.setPreferredSize(new Dimension(385, 270));
		jLabel2.setText("Тип");
		jPanel2.setLayout(xYLayout2);
		jPanel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		selectButton.setText("выбрать");
		selectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					selectButton_actionPerformed(e);
				}
			});
		pathField.setText("");
		jLabel1.setText("Карта");
		jLabel3.setText("Вид");
		okButton.setText("Выбрать");
		okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					okButton_actionPerformed(e);
				}
			});
		cancelButton.setText("Отменить");
		cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					cancelButton_actionPerformed(e);
				}
			});
		jPanel3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		jPanel4.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		jPanel2.add(jLabel1, new XYConstraints(20, 20, 60, 20));
		jPanel2.add(selectButton, new XYConstraints(380, 20, 90, 20));
		jPanel2.add(pathField, new XYConstraints(70, 20, 295, 20));
		jPanel2.add(jLabel3, new XYConstraints(20, 50, 60, 20));
		jPanel2.add(mapComboBox, new XYConstraints(70, 50, 295, 20));

		jPanel1.add(jPanel4, new XYConstraints(380, 150, 105, 225));
		jPanel1.add(jLabel2, new XYConstraints(25, 20, 110, 20));
		jPanel1.add(typeComboBox, new XYConstraints(75, 20, 245, 20));
		jPanel1.add(jPanel2, new XYConstraints(5, 50, 480, 90));
		jPanel1.add(okButton, new XYConstraints(5, 380, 90, 20));
		jPanel1.add(cancelButton, new XYConstraints(105, 380, 90, 20));
		jPanel1.add(jPanel3, new XYConstraints(5, 150, 365, 225));
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);

		typeComboBox.setModel(new DefaultComboBoxModel(new String[] {"SpatialFX"} ));
	}

	public void setContents(String path, String view)
	{
		pathField.setText(path);
		updateMapList(path);
		mapComboBox.setSelectedItem(view);
	}

	void selectButton_actionPerformed(ActionEvent e)
	{
		JFileChooser chooser = new JFileChooser("d:/");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			pathField.setText("file://localhost/" + chooser.getSelectedFile().getAbsolutePath().replace('\\', '/'));
			System.out.println("check map " + pathField.getText());
			updateMapList(pathField.getText());
		}
	}

	protected void updateMapList(String path)
	{
//		String s[];
		Vector vec = new Vector();
		try 
		{
			if(MapManager.openSession(path, "userName", "password"))
			{
/*
				SxEnvironment.singleton().getQuery().openSession(
					"xfile",
					path,
					"userName",
					"password");
*/
				for(Enumeration en = SxMap.objectIDs(SxEnvironment.singleton().getQuery());en.hasMoreElements();)
					vec.add((String)en.nextElement());
//				s = new String[vec.size()];
//				vec.copyInto(s);
			}

			mapComboBox.setModel(new DefaultComboBoxModel(vec));
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}

	void okButton_actionPerformed(ActionEvent e)
	{
		path = pathField.getText();
		map = (String )mapComboBox.getSelectedItem();
		ret_code = 1;
		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		ret_code = 2;
		dispose();
	}
}