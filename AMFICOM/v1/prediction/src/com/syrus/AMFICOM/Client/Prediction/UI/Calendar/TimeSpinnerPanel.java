package com.syrus.AMFICOM.Client.Prediction.UI.Calendar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

public class TimeSpinnerPanel extends JPanel
{
	String spin_pressed = "left";

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	SimpleDateFormat sdf_ = new SimpleDateFormat("HH:mm:ss");

	boolean editing = false;
	Calendar cal = Calendar.getInstance();
	SpinnerDateModel sdm1 = new SpinnerDateModel();
	public JSpinner jSpin1 = new JSpinner(sdm1);
	JFormattedTextField jTextField1;
	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout2 = new BorderLayout();


	public TimeSpinnerPanel()
	{

		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
		}
	}



	private void jbInit() throws Exception
	{
		javax.swing.JSpinner.DateEditor de1 = (javax.swing.JSpinner.DateEditor )jSpin1.getEditor();
		jTextField1 = de1.getTextField();
		jTextField1.setEditable(true);
		DateFormatter formatter = new DateFormatter(sdf);
		DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
		DateFormatter formatter_ = new DateFormatter(sdf_);
		DefaultFormatterFactory factory_ = new DefaultFormatterFactory(formatter_);
		jTextField1.setFormatterFactory(factory);

		cal = Calendar.getInstance();
		jTextField1.setValue(cal.getTime());
		jSpin1.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jSpin1_stateChanged(e);
			}
		});
		jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Spin1_mousePressed(e);
			}
		});

		this.setLayout(borderLayout2);
		this.add(jSpin1, BorderLayout.NORTH);

		this.setPreferredSize(new Dimension(175, 31));
		this.setMinimumSize(new Dimension(100, 30));
	}







	private void jSpin1_stateChanged(ChangeEvent e) {
		Date date = (Date )jSpin1.getValue();
	}

	public long getSelectedTime()
	{
		Date date = (Date )jSpin1.getValue();
		return date.getTime();
	}



	void Spin1_mousePressed(MouseEvent e) {
		spin_pressed = "left";
		FontMetrics fm = jTextField1.getFontMetrics(jTextField1.getFont());
		String temp = sdf.format(jSpin1.getValue());
		int x = e.getX();
		int xl = 0;
		for (int i = 0; i < temp.length(); i++)
		{
			xl = fm.stringWidth(temp.substring(0, i+1));
			if (xl > x)
			{
				if ( i >= 0 && i < 5)
				{
					sdm1.setCalendarField(Calendar.YEAR);
					jTextField1.select(0,4);
					break;
				}
				else if ( i >= 5 && i < 8)
				{
					sdm1.setCalendarField(Calendar.MONTH);
					jTextField1.select(5,7);
					break;
				}
				else if ( i >= 8 && i < 11)
				{
					sdm1.setCalendarField(Calendar.DAY_OF_MONTH);
					jTextField1.select(8,10);
					break;
				}
				else if ( i >= 11 && i < 14)
				{
					sdm1.setCalendarField(Calendar.HOUR_OF_DAY);
					jTextField1.select(11,13);
					break;
				}
				else if ( i >= 14 && i < 17)
				{
					sdm1.setCalendarField(Calendar.MINUTE);
					jTextField1.select(14,16);
					break;
				}
				else
				{
					sdm1.setCalendarField(Calendar.SECOND);
					jTextField1.select(17,19);
					break;
				}
			}
		}
	}

}

