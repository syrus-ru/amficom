package com.syrus.AMFICOM.Client.Model;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.Client.General.UI.ATable;

public class TransData extends JInternalFrame {

	MyTableModel myTableModel = new MyTableModel();
	JScrollPane jScrollPane1 = new JScrollPane();
	ATable jTable1 = new ATable();
	JPanel jPanel1 = new JPanel(new GridLayout(2, 2));
	AComboBox jComboBox1 = new AComboBox(AComboBox.SMALL_FONT);
	AComboBox jComboBox2 = new AComboBox(AComboBox.SMALL_FONT);
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();

	public TransData() {
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

		jLabel1.setText("Система передачи:");
		jLabel2.setText("Длина волны:");
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		this.getContentPane().add(jPanel1, BorderLayout.NORTH);

		jPanel1.add(jLabel1);
		jPanel1.add(jLabel2);
		jPanel1.add(jComboBox1);
		jPanel1.add(jComboBox2);
		jScrollPane1.getViewport().add(jTable1);
		jScrollPane1.getViewport().setBackground(SystemColor.window);

		this.setTitle("Параметры системы передачи");

		jComboBox1.addItemListener(new java.awt.event.ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				jComboBox1_itemStateChanged(e);
			}
		});

								jComboBox2.addItemListener(new java.awt.event.ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				jComboBox2_itemStateChanged(e);
			}
		});

		jTable1.setModel(myTableModel);
		jTable1.getColumnModel().getColumn(0).setPreferredWidth(155);
		jTable1.getColumnModel().getColumn(1).setPreferredWidth(45);
		set_def_data();
	}
	void jComboBox1_itemStateChanged(ItemEvent e)
	 {
			if(jComboBox1.getSelectedItem() != null &&
				 jComboBox2.getSelectedItem() != null)
			set_data((String)(jComboBox1.getSelectedItem()),
							 (String)(jComboBox2.getSelectedItem()));
	 }

	void jComboBox2_itemStateChanged(ItemEvent e)
	 {
			if(jComboBox1.getSelectedItem() != null &&
				 jComboBox2.getSelectedItem() != null)
			set_data((String)(jComboBox1.getSelectedItem()),
							 (String)(jComboBox2.getSelectedItem()));
	 }

 void set_def_data()
 {
	 jComboBox1.addItem("STM-1");
	 jComboBox1.addItem("STM-4");
	 jComboBox1.addItem("STM-16");

	 jComboBox2.addItem("1310");
	 jComboBox2.addItem("1550");

	 String name = new String("STM-1");
	 String wl   = new String("1310");
	 set_data(name, wl);
 }

 void set_data(String name, String wl)
	{
		 if(name.equals("STM-1") && wl.equals("1310"))
		 {
		myTableModel.setValueAt( new String("155.52"), 0, 1);
		myTableModel.setValueAt( new String("<5"), 1, 1);
		myTableModel.setValueAt( new String("-5"), 2, 1);
		myTableModel.setValueAt( new String("-38"), 3, 1);
		myTableModel.setValueAt( new String("30"), 4, 1);
		myTableModel.setValueAt( new String("0.4"), 5, 1);
		myTableModel.setValueAt( new String("0.1 - 0.05"), 6, 1);
		myTableModel.setValueAt( new String("0.2 - 0.0"), 7, 1);
		myTableModel.setValueAt( new String("0.7 - 0.425"), 8, 1);
		myTableModel.setValueAt( new String("42.8 - 50.5"), 9, 1);
		 }
		 else
		 if(name.equals("STM-1") && wl.equals("1550"))
		 {
		myTableModel.setValueAt( new String("155.52"), 0, 1);
		myTableModel.setValueAt( new String("<1"), 1, 1);
		myTableModel.setValueAt( new String("-5"), 2, 1);
		myTableModel.setValueAt( new String("-40"), 3, 1);
		myTableModel.setValueAt( new String("32"), 4, 1);
		myTableModel.setValueAt( new String("0.25"), 5, 1);
		myTableModel.setValueAt( new String("0.1 - 0.05"), 6, 1);
		myTableModel.setValueAt( new String("0.2 - 0.0"), 7, 1);
		myTableModel.setValueAt( new String("0.55 - 0.275"), 8, 1);
		myTableModel.setValueAt( new String("48.1 - 116.3"), 9, 1);
		 }
		 else
		 if(name.equals("STM-4") && wl.equals("1310"))
		 {
		myTableModel.setValueAt( new String("622.08"), 0, 1);
		myTableModel.setValueAt( new String("4"), 1, 1);
		myTableModel.setValueAt( new String("-6"), 2, 1);
		myTableModel.setValueAt( new String("-37"), 3, 1);
		myTableModel.setValueAt( new String("28"), 4, 1);
		myTableModel.setValueAt( new String("0.4"), 5, 1);
		myTableModel.setValueAt( new String("0.1 - 0.05"), 6, 1);
		myTableModel.setValueAt( new String("0.2 - 0.0"), 7, 1);
		myTableModel.setValueAt( new String("0.7 - 0.425"), 8, 1);
		myTableModel.setValueAt( new String("48.0 - 65.8"), 9, 1);
		 }
		 else
		 if(name.equals("STM-4") && wl.equals("1550"))
		 {
		myTableModel.setValueAt( new String("622.08"), 0, 1);
		myTableModel.setValueAt( new String("0.3"), 1, 1);
		myTableModel.setValueAt( new String("-8"), 2, 1);
		myTableModel.setValueAt( new String("-38"), 3, 1);
		myTableModel.setValueAt( new String("27"), 4, 1);
		myTableModel.setValueAt( new String("0.25"), 5, 1);
		myTableModel.setValueAt( new String("0.1 - 0.05"), 6, 1);
		myTableModel.setValueAt( new String("0.2 - 0.0"), 7, 1);
		myTableModel.setValueAt( new String("0.55 - 0.275"), 8, 1);
		myTableModel.setValueAt( new String("49.0 - 98.1"), 9, 1);
		 }
		 else
		 if(name.equals("STM-16") && wl.equals("1310"))
		 {
		myTableModel.setValueAt( new String("2488.32"), 0, 1);
		myTableModel.setValueAt( new String("<1"), 1, 1);
		myTableModel.setValueAt( new String("-5"), 2, 1);
		myTableModel.setValueAt( new String("-18"), 3, 1);
		myTableModel.setValueAt( new String("10"), 4, 1);
		myTableModel.setValueAt( new String("0.4"), 5, 1);
		myTableModel.setValueAt( new String("0.1 - 0.05"), 6, 1);
		myTableModel.setValueAt( new String("0.2 - 0.0"), 7, 1);
		myTableModel.setValueAt( new String("0.7 - 0.425"), 8, 1);
		myTableModel.setValueAt( new String("14.2 - 23.5"), 9, 1);
		 }
		 else
//     if(name.equals("STM-16") && wl.equals(1310))
		 {
		myTableModel.setValueAt( new String("2488.32"), 0, 1);
		myTableModel.setValueAt( new String("0.25"), 1, 1);
		myTableModel.setValueAt( new String("-4"), 2, 1);
		myTableModel.setValueAt( new String("-27"), 3, 1);
		myTableModel.setValueAt( new String("20"), 4, 1);
		myTableModel.setValueAt( new String("0.25"), 5, 1);
		myTableModel.setValueAt( new String("0.1 - 0.05"), 6, 1);
		myTableModel.setValueAt( new String("0.2 - 0.0"), 7, 1);
		myTableModel.setValueAt( new String("0.55 - 0.275"), 8, 1);
		myTableModel.setValueAt( new String("36.2 - 72.7"), 9, 1);
		 }

	}


	class MyTableModel extends AbstractTableModel
	{
	String[] columnNames = { "" ,
																 ""};
	Object[][] data = {
		{"Скорость передачи", new String()},
		{"Ширина спектра", new String()},
		{"Уровень после разъема, дБм", new String()},
		{"Уровень приема, дБм", new String()},
		{"Максимум затухания с запасом 3 дБ, дБ", new String()},
		{"Коэффициент затухания волокна, дБ/км", new String()},
				{"Затухание термосоединения, дБ", new String()},
				{"Запас на потери из-за затухания, дБ/км", new String()},
				{"Коэффиент затухания магистрали, дБ/км", new String()},
				{"Длина ретрансляционного участка", new String()}

							 };
				public void clearTable()
				{
					data = new Object[][]{};
					super.fireTableDataChanged();
				}

				public int getColumnCount() {
						return columnNames.length;
				}

				public int getRowCount() {
						return data.length;
				}

				public String getColumnName(int col) {
						return columnNames[col];
				}

				public Object getValueAt(int row, int col) {
						return data[row][col];
				}

							public double getvalueat(int row, int col) {
				return ((Double)(data[row][col])).doubleValue();
				}

				public Class getColumnClass(int c) {
						return getValueAt(0, c).getClass();
				}

				public boolean isCellEditable(int row, int col) {
						if (col < 1) {
								return false;
						} else {
								return false;
						}
				}

				public void setValueAt(Object value, int row, int col) {
						data[row][col] = value;
						fireTableCellUpdated(row, col);
				}

	}

}


