package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.UI.DateSpinner;
import com.syrus.AMFICOM.Client.General.UI.TimeSpinner;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.JSpinner.DateEditor;

import com.syrus.AMFICOM.Client.General.Filter.FilterPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.FontMetrics;

import com.syrus.AMFICOM.filter.FilterExpressionInterface;

public class GeneralTimeFilterPanel extends FilterPanel
{
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();

	public DateSpinner loDateSpin = new DateSpinner();
	public TimeSpinner loTimeSpin = new TimeSpinner();
	public DateSpinner hiDateSpin = new DateSpinner();
	public TimeSpinner hiTimeSpin = new TimeSpinner();

	public GeneralTimeFilterPanel()
	{
		super();
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
		((DateEditor)loDateSpin.getEditor()).getTextField().addMouseListener(
			new java.awt.event.MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					spinDate_mousePressed(e,loDateSpin);
				}
			});

		((DateEditor)loTimeSpin.getEditor()).getTextField().addMouseListener(
			new java.awt.event.MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					spinDate_mousePressed(e,loTimeSpin);
				}
			});

		((DateEditor)hiDateSpin.getEditor()).getTextField().addMouseListener(
			new java.awt.event.MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					spinDate_mousePressed(e,hiDateSpin);
				}
			});

		((DateEditor)hiTimeSpin.getEditor()).getTextField().addMouseListener(
			new java.awt.event.MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					spinDate_mousePressed(e,hiTimeSpin);
				}
			});

		this.setLayout(new GridBagLayout());
		jLabel1.setText(LangModel.String("labelFrom"));
		jLabel2.setText(LangModel.String("labelTo"));

		this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
		this.add(loDateSpin, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
		this.add(loTimeSpin, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));

		this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
		this.add(hiDateSpin, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
		this.add(hiTimeSpin, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
//		this.setLayout(null);
	}

	public FilterExpressionInterface getExpression(String col_id, String col_name, boolean conditionsRequested)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime((Date)loDateSpin.getValue());

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime((Date)loTimeSpin.getValue());

		cal1.set(Calendar.HOUR_OF_DAY,cal2.get(Calendar.HOUR_OF_DAY));
		cal1.set(Calendar.MINUTE,cal2.get(Calendar.MINUTE));

		String fromDate = String.valueOf(cal1.getTime().getTime());


		cal1 = Calendar.getInstance();
		cal1.setTime((Date)hiDateSpin.getValue());

		cal2 = Calendar.getInstance();
		cal2.setTime((Date)hiTimeSpin.getValue());

		cal1.set(Calendar.HOUR_OF_DAY,cal2.HOUR_OF_DAY);
		cal1.set(Calendar.MINUTE,cal2.MINUTE);

		String toDate = String.valueOf(cal1.getTime().getTime());

		FilterExpression fexp = new FilterExpression();

		String expName = LangModel.String("labelFiltration") + " \'" + col_name + "\' ";
		if (conditionsRequested)
			expName += (LangModel.String("labelTimeOt") + " " + fromDate + " " +
							LangModel.String("labelTimeDo") + " " + toDate);

		fexp.setName(expName);
		fexp.setColumnName(col_name);

		Vector vec = new Vector();
		vec.add("time");
		vec.add(fromDate);
		vec.add(toDate);
		fexp.setVec(vec);

		fexp.setId(col_id);
		return fexp;
	}

	public void setExpression(FilterExpression expr)
	{
		Vector vec = expr.getVec();
		loDateSpin.setValue(new Date(Long.parseLong ((String) vec.elementAt(1))));
		loTimeSpin.setValue(new Date(Long.parseLong ((String) vec.elementAt(1))));
		hiDateSpin.setValue(new Date(Long.parseLong ((String) vec.elementAt(2))));
		hiTimeSpin.setValue(new Date(Long.parseLong ((String) vec.elementAt(2))));
	}

	void spinDate_mousePressed(MouseEvent e, TimeSpinner spin)
	{
		JFormattedTextField textField = ((DateEditor)spin.getEditor()).getTextField();
		FontMetrics fontMetrics = textField.getFontMetrics(textField.getFont());

		SimpleDateFormat sdf = null;
		if (spin instanceof DateSpinner)
			sdf = new SimpleDateFormat(DateSpinner.getPattern());
		else
			sdf = new SimpleDateFormat(TimeSpinner.getPattern());
		String fullString = sdf.format(spin.getValue());

		int x = e.getX();
		int xl = 0;
		for (int i = 0; i < fullString.length(); i++)
		{
			String stringToConsider = fullString.substring(0, i + 1);
			xl = fontMetrics.stringWidth(stringToConsider);
			if (xl > x)
			{
				if (spin instanceof DateSpinner)
				{
					int spaceNumber = 0;
					int from = 0;
					int to = 0;

					do
					{
						int temp = stringToConsider.indexOf("  ",from);
						if (temp != -1)
						{
							from = temp + 2;
							spaceNumber++;
						}
						else
						{
							to = fullString.indexOf("  ",from);
							if (to == -1)
								to = fullString.length();
							break;
						}
					}
					while (true);

					textField.select(from,to);
					SpinnerDateModel sModel = (SpinnerDateModel) spin.getModel();
					if (spaceNumber == 0)
						sModel.setCalendarField(Calendar.DAY_OF_MONTH);
					if (spaceNumber == 1)
						sModel.setCalendarField(Calendar.MONTH);
					if (spaceNumber == 2)
						sModel.setCalendarField(Calendar.YEAR);
					break;
				}
				else
				{
					SpinnerDateModel sModel = (SpinnerDateModel) spin.getModel();
					if (stringToConsider.indexOf(':') != -1)
					{
						textField.select(3,5);
						sModel.setCalendarField(Calendar.MINUTE);
					}
					else
					{
						textField.select(0,2);
						sModel.setCalendarField(Calendar.HOUR_OF_DAY);
					}
					break;
				}
			}
		}
	}
}
