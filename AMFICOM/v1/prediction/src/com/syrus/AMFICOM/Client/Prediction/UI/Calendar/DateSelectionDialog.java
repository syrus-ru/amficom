package com.syrus.AMFICOM.Client.Prediction.UI.Calendar;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import oracle.jdeveloper.layout.VerticalFlowLayout;

public class DateSelectionDialog  extends JDialog
{
	public static final int CANCEL = 0;
	public static final int OK = 1;

	public int retCode = CANCEL;

	private CalendarPanel calendarPanel = new CalendarPanel();
	private JPanel mainPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout = new VerticalFlowLayout();
	private JPanel buttonPanel = new JPanel();
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();
	private JLabel jLabel1 = new JLabel();

	public long getDate()
	{
		long time = calendarPanel.getSelectedTime();
		return time;
	}

	private DateSelectionDialog(Frame frame, String title, boolean modal)
	{
		super(frame, title, modal);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public DateSelectionDialog(JFrame parent, String title)
	{
		this(parent, title, true);
	}


	private void jbInit() throws Exception
	{
		mainPanel.setLayout(verticalFlowLayout);
		okButton.setMaximumSize(new Dimension(91, 27));
		okButton.setPreferredSize(new Dimension(90, 27));
		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				okButton_actionPerformed(e);
			}
		});
		cancelButton.setMinimumSize(new Dimension(90, 27));
		cancelButton.setPreferredSize(new Dimension(90, 27));
		cancelButton.setText("Отмена");
		cancelButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed(e);
			}
		});
		jLabel1.setText("         ");
		mainPanel.setMinimumSize(new Dimension(330, 188));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(calendarPanel, null);
		mainPanel.add(buttonPanel, null);
		buttonPanel.add(okButton, null);
		buttonPanel.add(jLabel1, null);
		buttonPanel.add(cancelButton, null);
		this.setSize(315, 210);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight() - 30) / 2);

		this.setResizable(false);
		this.setVisible(true);
	}

//-----------------------------------------------------------------------------
	void okButton_actionPerformed(ActionEvent e)
	{
		retCode = OK;
		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		dispose();
	}

}