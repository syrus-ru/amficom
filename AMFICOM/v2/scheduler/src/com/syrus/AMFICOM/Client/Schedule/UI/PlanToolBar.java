package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Report.ReportBuilder;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

class PlanToolBar extends JPanel {

	private class FlashIcon implements Icon {

		private static final int	H		= TestLine.MINIMAL_WIDTH;
		private static final int	W		= TestLine.MINIMAL_WIDTH;

		private boolean				flash	= false;

		public void flash() {
			this.flash = !this.flash;
		}

		public int getIconHeight() {
			return H + 1;
		}

		public int getIconWidth() {
			return W + 1;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g;
			Color foregroundColor = g.getColor();
			g2d.setBackground(Color.lightGray);
			g2d.clearRect(x, y, W + 1, H + 1);
			g2d.setColor(this.flash ? TestLine.COLOR_SCHEDULED : TestLine.COLOR_UNRECOGNIZED);
			g2d.fillRect(x + 2, y + 2, W - 3, H - 3);
			g2d.draw3DRect(x, y, W, H, true);
			g.setColor(foregroundColor);
		}
	}

	static final int			H				= 22;
	private JButton				applyButton		= new JButton();

	private JButton				dateButton		= new JButton(UIStorage.CALENDAR_ICON);
	JSpinner					dateSpinner		= new DateSpinner();

	private JButton				nowButton		= new JButton(UIStorage.TIME_ICON);

	PlanPanel					panel;
	//private ApplicationContext	aContext;
	Dispatcher			dispatcher;
	AComboBox			scaleComboBox;
	JSpinner			timeSpinner		= new TimeSpinner();

	private JButton				zoomInButton	= new JButton();
	private JButton				zoomNoneButton	= new JButton();
	private JButton				zoomOutButton	= new JButton();

	public PlanToolBar(final ApplicationContext aContext, final PlanPanel panel) {
		//this.aContext = aContext;		
		if (aContext != null) 
			this.dispatcher = aContext.getDispatcher();		
		this.panel = panel;
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		Box box = new Box(BoxLayout.X_AXIS);

		String[] scales = new String[PlanPanel.SCALES.length];
		for (int i = 0; i < scales.length; i++)
			scales[i] = new String(LangModelSchedule.getString(PlanPanel.SCALES[i]));
		this.scaleComboBox = new AComboBox(scales);
		this.scaleComboBox.setSelectedItem(LangModelSchedule.getString("1 week"));
		panel.setScale(PlanToolBar.this.scaleComboBox.getSelectedIndex());
		panel.updateTests();
		this.dateButton.setMargin(UIStorage.INSET_NULL);
		this.dateButton.setFocusable(false);
		this.dateButton.setToolTipText(LangModelSchedule.getString("Calendar")); //$NON-NLS-1$
		this.dateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//showCalendar();
				Calendar cal = Calendar.getInstance();
				Date date = (Date) dateSpinner.getModel().getValue();
				cal.setTime(date);

				JDialog calendarDialog = CalendarUI
						.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
				calendarDialog.setSize(new Dimension(200, 200));
				calendarDialog.setResizable(false);
				calendarDialog.setLocation(new Point(dateSpinner.getLocationOnScreen().x - 35, dateSpinner
						.getLocationOnScreen().y
						+ H));
				calendarDialog.setVisible(true);
				if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
					dateSpinner.getModel().setValue(cal.getTime());
			}
		});
		UIStorage.setRigidSize(zoomInButton, UIStorage.BUTTON_SIZE);
		zoomInButton.setFocusable(false);
		zoomInButton.setIcon(UIStorage.ZOOMIN_ICON);
		zoomInButton.setToolTipText(LangModelSchedule.getString("ZoomIn")); //$NON-NLS-1$
		zoomInButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(1.25);
			}
		});
		UIStorage.setRigidSize(zoomOutButton, UIStorage.BUTTON_SIZE);
		zoomOutButton.setFocusable(false);
		zoomOutButton.setIcon(UIStorage.ZOOMOUT_ICON);
		zoomOutButton.setToolTipText(LangModelSchedule.getString("ZoomOut")); //$NON-NLS-1$
		zoomOutButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(.8);
			}
		});
		UIStorage.setRigidSize(zoomNoneButton, UIStorage.BUTTON_SIZE);
		zoomNoneButton.setFocusable(false);
		zoomNoneButton.setIcon(UIStorage.NOZOOM_ICON);
		zoomNoneButton.setToolTipText(LangModelSchedule.getString("ZoomNone")); //$NON-NLS-1$
		zoomNoneButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale2Fit();
			}
		});

		box.add(new JLabel(LangModelSchedule.getString("Detalization"))); //$NON-NLS-1$
		box.add(Box.createHorizontalStrut(4));

		box.add(scaleComboBox);
		box.add(Box.createHorizontalStrut(10));
		box.add(new JLabel(LangModelSchedule.getString("Date"))); //$NON-NLS-1$
		box.add(Box.createHorizontalStrut(4));
		{
			Dimension d = new Dimension(137, H);
			UIStorage.setRigidSize(dateSpinner, d);
		}
		box.add(dateSpinner);
		box.add(dateButton);
		box.add(Box.createHorizontalStrut(10));
		box.add(new JLabel(LangModelSchedule.getString("Time"))); //$NON-NLS-1$
		box.add(Box.createHorizontalStrut(4));
		{
			Dimension d = new Dimension(55, H);
			UIStorage.setRigidSize(timeSpinner, d);
		}
		box.add(timeSpinner);
		nowButton.setMargin(UIStorage.INSET_NULL);
		box.add(nowButton);
		box.add(Box.createHorizontalStrut(10));
		box.add(applyButton);
		JButton legendButton = new JButton(LangModelSchedule.getString("Legend"));
		legendButton.setToolTipText(LangModelSchedule.getString("Legend"));
		legendButton.setMargin(UIStorage.INSET_NULL);
		box.add(Box.createHorizontalStrut(10));
		box.add(legendButton);
		{

			final JDialog dialog = new JDialog();
			dialog.setTitle(LangModelSchedule.getString("Legend"));
			JPanel legendPanel = new JPanel(new GridLayout(0, 1));
			legendPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			final FlashIcon flashIcon = new FlashIcon();
			final JLabel flashLabel = new JLabel(LangModelSchedule.getString("Not_saved"), flashIcon,
													SwingConstants.LEFT);
			javax.swing.Timer timer = new javax.swing.Timer(TestLine.TIME_OUT, new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					flashIcon.flash();
					flashLabel.repaint();
					flashLabel.revalidate();
				}
			});

			timer.start();

			legendPanel.add(flashLabel);
			legendPanel.add(new JLabel(LangModelSchedule.getString("Scheduled"), PlanToolBar
					.getColorIcon(TestLine.COLOR_SCHEDULED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Done"), PlanToolBar
					.getColorIcon(TestLine.COLOR_COMPLETED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Running"), PlanToolBar
					.getColorIcon(TestLine.COLOR_PROCCESSING), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Aborted"), PlanToolBar
					.getColorIcon(TestLine.COLOR_ABORDED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Unrecognized"), PlanToolBar
					.getColorIcon(TestLine.COLOR_UNRECOGNIZED), SwingConstants.LEFT));
			dialog.getContentPane().add(legendPanel);
			dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			dialog.pack();
			legendButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					dialog.setLocationRelativeTo(button);
					Point l = dialog.getLocation();
					dialog.setLocation(l.x + dialog.getWidth() / 2, l.y + dialog.getHeight() / 2);
					dialog.setVisible(!dialog.isVisible());
				}
			});
		}

		applyButton.setIcon(UIStorage.REFRESH_ICON);
		applyButton.setToolTipText(LangModelSchedule.getString("Apply")); //$NON-NLS-1$
		applyButton.setMargin(UIStorage.INSET_NULL);

		box.add(Box.createHorizontalGlue());
		box.add(zoomInButton);
		box.add(zoomOutButton);
		box.add(zoomNoneButton);
		add(box, gbc);

		nowButton.setFocusable(false);
		nowButton.setToolTipText(LangModelSchedule.getString("CurrentTime")); //$NON-NLS-1$
		nowButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dateSpinner.setValue(new Date(System.currentTimeMillis()));
				timeSpinner.setValue(new Date(System.currentTimeMillis()));
			}
		});

		applyButton.setFocusable(false);
		applyButton.setToolTipText(LangModelSchedule.getString("Apply")); //$NON-NLS-1$
		applyButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				aContext.getDispatcher().notify(
												new OperationEvent(new Boolean(true), 0,
																	SchedulerModel.COMMAND_CHANGE_STATUSBAR_STATE));
				ReportBuilder.invokeAsynchronously(new Runnable() {

					public void run() {
						PlanToolBar.this.dispatcher.notify(new OperationEvent(new Boolean(false), 0,
																SchedulerModel.COMMAND_COMMIT_CHANGES));
						Calendar date = Calendar.getInstance();
						date.setTime((Date) PlanToolBar.this.dateSpinner.getValue());
						Calendar time = Calendar.getInstance();
						time.setTime((Date) PlanToolBar.this.timeSpinner.getValue());

						date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
						date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

						panel.updateDate(date.getTime(), PlanToolBar.this.scaleComboBox.getSelectedIndex());
						PlanToolBar.this.dispatcher.notify(new OperationEvent(new Boolean(false), 0,
																SchedulerModel.COMMAND_CHANGE_STATUSBAR_STATE));
					}
				}, LangModelSchedule.getString("Updating_tests_from_BD"));
			}
		});		
		
	}

	public static Icon getColorIcon(Color color) {
		int x = 0;
		int y = 0;
		int w = TestLine.MINIMAL_WIDTH;
		int h = TestLine.MINIMAL_WIDTH;
		BufferedImage img = new BufferedImage(w + 1, h + 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setBackground(Color.lightGray);
		g2d.clearRect(x, y, w + 1, h + 1);
		g2d.setColor(color);
		g2d.fillRect(x + 2, y + 2, w - 3, h - 3);
		g2d.draw3DRect(x, y, w, h, true);
		Icon icon = new ImageIcon(img);
		return icon;
	}
}