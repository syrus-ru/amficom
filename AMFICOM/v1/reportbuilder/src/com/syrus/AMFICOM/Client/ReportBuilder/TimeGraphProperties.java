package com.syrus.AMFICOM.Client.ReportBuilder;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionEvent;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import oracle.jdeveloper.layout.XYLayout;
import oracle.jdeveloper.layout.XYConstraints;

import org.jfree.data.time.Hour;
import org.jfree.data.time.Day;
import org.jfree.data.time.Week;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.Year;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
/**
 * <p>Title: </p>
 * <p>Description: Диалог для выбора величины периода квантования
 * для временных графиков</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class TimeGraphProperties extends JDialog
{
	static Class interval_value = null;

	private JPanel jPanel1 = new JPanel();
	private XYLayout xYLayout1 = new XYLayout();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	private JLabel jLabel1 = new JLabel();
	private JButton jButton1 = new JButton();
	private JButton jButton2 = new JButton();
	private AComboBox jComboBox1 = new AComboBox();

	public TimeGraphProperties(Frame frame, String title, boolean modal) {
	 super(frame, title, modal);
	 try {
		jbInit();
		setControlsData();
		pack();
	 }
	 catch(Exception ex) {
		ex.printStackTrace();
	 }
	}

	public TimeGraphProperties() {
	 this(null, "", false);
	}
	private void jbInit() throws Exception
	{
	 this.setResizable(false);
	 this.setSize(new Dimension(286,100));

	 jLabel1.setText(LangModelReport.String("label_Intervalvalue"));
	 jButton1.setText(LangModelReport.String("label_input"));
	 jButton1.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
			jButton1_actionPerformed(e);
		}
	 });
	 jButton2.setText(LangModelReport.String("label_cancel"));
	 jButton2.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
			jButton2_actionPerformed(e);
		}
	 });

	 jPanel1.setLayout(xYLayout1);
	 jPanel1.add(jLabel1,       new XYConstraints(4, 10, -1, -1));
	 jPanel1.add(jButton1,  new XYConstraints(21, 71, 101, 25));
	 jPanel1.add(jButton2,   new XYConstraints(150, 72, 101, 25));
	 jPanel1.add(jComboBox1,  new XYConstraints(42, 33, 188, 26));
	 this.getContentPane().add(jPanel1, BorderLayout.CENTER);
	}

	void setControlsData(){
	 jComboBox1.addItem(LangModelReport.String("label_hour"));
	 jComboBox1.addItem(LangModelReport.String("label_day"));
	 jComboBox1.addItem(LangModelReport.String("label_week"));
	 jComboBox1.addItem(LangModelReport.String("label_month"));
	 jComboBox1.addItem(LangModelReport.String("label_quarter"));
	 jComboBox1.addItem(LangModelReport.String("label_year"));
	}

	void jButton2_actionPerformed(ActionEvent e) {
	 this.dispose();
	}

	void jButton1_actionPerformed(ActionEvent e) {
	 if (jComboBox1.getSelectedItem().equals(LangModelReport.String("label_hour")))
		this.interval_value =  Hour.class;
	 if (jComboBox1.getSelectedItem().equals(LangModelReport.String("label_day")))
		this.interval_value =  Day.class;
	 if (jComboBox1.getSelectedItem().equals(LangModelReport.String("label_week")))
		this.interval_value =  Week.class;
	 if (jComboBox1.getSelectedItem().equals(LangModelReport.String("label_month")))
		this.interval_value =  Month.class;
	 if (jComboBox1.getSelectedItem().equals(LangModelReport.String("label_quarter")))
		this.interval_value =  Quarter.class;
	 if (jComboBox1.getSelectedItem().equals(LangModelReport.String("label_year")))
		this.interval_value =  Year.class;

	 this.dispose();
	}

}