package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Filter.FilterDialog;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Report.ReportBuilder;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.Filter.TestFilter;
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

	static final int	H				= 22;
	private JButton		applyButton		= new JButton();

	private JButton		dateButton		= new JButton(UIStorage.CALENDAR_ICON);
	JSpinner			dateSpinner		= new DateSpinner();

	private JButton		nowButton		= new JButton(UIStorage.TIME_ICON);

	PlanPanel			panel;
	//private ApplicationContext aContext;
	Dispatcher			dispatcher;
	AComboBox			scaleComboBox;
	JSpinner			timeSpinner		= new TimeSpinner();

	FilterDialog		filterDialog;

	private JButton		filterButton	= new JButton();
	private JButton		zoomInButton	= new JButton();
	private JButton		zoomNoneButton	= new JButton();
	private JButton		zoomOutButton	= new JButton();

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
		this.scaleComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				AComboBox comboBox = (AComboBox) e.getSource();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					//System.out.println("comboBox.getSelectedIndex():" +
					// comboBox.getSelectedIndex());
					Calendar calDate = Calendar.getInstance();
					calDate.setTime((Date) PlanToolBar.this.dateSpinner.getModel().getValue());
					Calendar timeDate = Calendar.getInstance();
					timeDate.setTime((Date) PlanToolBar.this.timeSpinner.getModel().getValue());
					calDate.set(Calendar.HOUR_OF_DAY, timeDate.get(Calendar.HOUR_OF_DAY));
					calDate.set(Calendar.MINUTE, timeDate.get(Calendar.MINUTE));
					panel.setStartDate(calDate.getTime());
					panel.setScale(comboBox.getSelectedIndex());
					//panel.updateTests();
					panel.updateTestLines();
				}
			}
		});
		this.scaleComboBox.setSelectedItem(LangModelSchedule.getString("1 week"));

		this.dateSpinner.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				DateSpinner spinner = (DateSpinner) e.getSource();
				Calendar calDate = Calendar.getInstance();
				calDate.setTime((Date) spinner.getModel().getValue());
				Calendar timeDate = Calendar.getInstance();
				timeDate.setTime((Date) PlanToolBar.this.timeSpinner.getModel().getValue());
				calDate.set(Calendar.HOUR_OF_DAY, timeDate.get(Calendar.HOUR_OF_DAY));
				calDate.set(Calendar.MINUTE, timeDate.get(Calendar.MINUTE));
				panel.setStartDate(calDate.getTime());
				//panel.updateTests();
				panel.updateTestLines();
			}
		});

		this.timeSpinner.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				TimeSpinner spinner = (TimeSpinner) e.getSource();
				Calendar timeDate = Calendar.getInstance();
				timeDate.setTime((Date) spinner.getModel().getValue());
				Calendar calDate = Calendar.getInstance();
				calDate.setTime((Date) PlanToolBar.this.dateSpinner.getModel().getValue());
				calDate.set(Calendar.HOUR_OF_DAY, timeDate.get(Calendar.HOUR_OF_DAY));
				calDate.set(Calendar.MINUTE, timeDate.get(Calendar.MINUTE));
				panel.setStartDate(calDate.getTime());
				//panel.updateTests();
				panel.updateTestLines();
			}
		});

		this.dateButton.setMargin(UIStorage.INSET_NULL);
		this.dateButton.setFocusable(false);
		this.dateButton.setToolTipText(LangModelSchedule.getString("Calendar")); //$NON-NLS-1$
		this.dateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//showCalendar();
				Calendar cal = Calendar.getInstance();
				Date date = (Date) PlanToolBar.this.dateSpinner.getModel().getValue();
				cal.setTime(date);

				JDialog calendarDialog = CalendarUI
						.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
				calendarDialog.setSize(new Dimension(200, 200));
				calendarDialog.setResizable(false);
				calendarDialog.setLocation(new Point(PlanToolBar.this.dateSpinner.getLocationOnScreen().x - 35,
														PlanToolBar.this.dateSpinner.getLocationOnScreen().y + H));
				calendarDialog.setVisible(true);
				if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
					PlanToolBar.this.dateSpinner.getModel().setValue(cal.getTime());
			}
		});
		UIStorage.setRigidSize(this.zoomInButton, UIStorage.BUTTON_SIZE);
		this.zoomInButton.setFocusable(false);
		this.zoomInButton.setIcon(UIStorage.ZOOMIN_ICON);
		this.zoomInButton.setToolTipText(LangModelSchedule.getString("ZoomIn")); //$NON-NLS-1$
		this.zoomInButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(1.25);
			}
		});

		//UIStorage.setRigidSize(this.filterButton, UIStorage.BUTTON_SIZE);
		this.filterButton.setMargin(UIStorage.INSET_NULL);
		this.filterButton.setFocusable(false);
		this.filterButton.setIcon(UIStorage.FILTER_ICON);
		this.filterButton.setToolTipText(LangModelSchedule.getString("Filtration")); //$NON-NLS-1$
		this.filterButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ObjectResourceFilter filter = ((SchedulerModel) aContext.getApplicationModel()).getFilter();
				TestFilter orf = (TestFilter) filter.clone();
				
				/**
				 * FIXME recast without create new instance of FilterDialog
				 */
				//if (PlanToolBar.this.filterDialog == null) {
					PlanToolBar.this.filterDialog = new FilterDialog(orf, aContext);
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					Dimension frameSize = PlanToolBar.this.filterDialog.getSize();
					frameSize.width = 450;
					frameSize.height = frameSize.height + 20;
					PlanToolBar.this.filterDialog.setSize(frameSize);

					if (frameSize.height > screenSize.height)
						frameSize.height = screenSize.height;
					if (frameSize.width > screenSize.width)
						frameSize.width = screenSize.width;
					PlanToolBar.this.filterDialog.setLocation((screenSize.width - frameSize.width) / 2,
																(screenSize.height - frameSize.height) / 2);
				//}
				//else PlanToolBar.this.filterDialog.setF
				PlanToolBar.this.filterDialog.pack();

				PlanToolBar.this.filterDialog.setModal(true);
				PlanToolBar.this.filterDialog.setVisible(true);
				PlanToolBar.this.filterDialog.setFilter(orf);

				if (PlanToolBar.this.filterDialog.retcode == FilterDialog.RETURN_CODE_OK) {					
					((SchedulerModel) aContext.getApplicationModel()).setFilter(orf);
				}
			}
		});

		UIStorage.setRigidSize(this.zoomOutButton, UIStorage.BUTTON_SIZE);
		this.zoomOutButton.setFocusable(false);
		this.zoomOutButton.setIcon(UIStorage.ZOOMOUT_ICON);
		this.zoomOutButton.setToolTipText(LangModelSchedule.getString("ZoomOut")); //$NON-NLS-1$
		this.zoomOutButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(.8);
			}
		});
		UIStorage.setRigidSize(this.zoomNoneButton, UIStorage.BUTTON_SIZE);
		this.zoomNoneButton.setFocusable(false);
		this.zoomNoneButton.setIcon(UIStorage.NOZOOM_ICON);
		this.zoomNoneButton.setToolTipText(LangModelSchedule.getString("ZoomNone")); //$NON-NLS-1$
		this.zoomNoneButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale2Fit();
			}
		});

		box.add(new JLabel(LangModelSchedule.getString("Detalization"))); //$NON-NLS-1$
		box.add(Box.createHorizontalStrut(4));

		box.add(this.scaleComboBox);
		box.add(Box.createHorizontalStrut(10));
		box.add(new JLabel(LangModelSchedule.getString("Date"))); //$NON-NLS-1$
		box.add(Box.createHorizontalStrut(4));
		{
			Dimension d = new Dimension(137, H);
			UIStorage.setRigidSize(this.dateSpinner, d);
		}
		box.add(this.dateSpinner);
		box.add(this.dateButton);
		box.add(Box.createHorizontalStrut(10));
		box.add(new JLabel(LangModelSchedule.getString("Time"))); //$NON-NLS-1$
		box.add(Box.createHorizontalStrut(4));
		{
			Dimension d = new Dimension(55, H);
			UIStorage.setRigidSize(this.timeSpinner, d);
		}
		box.add(this.timeSpinner);
		this.nowButton.setMargin(UIStorage.INSET_NULL);
		box.add(this.nowButton);
		box.add(Box.createHorizontalStrut(10));
		box.add(this.applyButton);
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
			legendPanel.add(new JLabel(LangModelSchedule.getString("Alarm"), PlanToolBar
					.getColorIcon(TestLine.COLOR_ALARM), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Warning"), PlanToolBar
					.getColorIcon(TestLine.COLOR_WARNING), SwingConstants.LEFT));
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

		this.applyButton.setIcon(UIStorage.REFRESH_ICON);
		this.applyButton.setToolTipText(LangModelSchedule.getString("Apply")); //$NON-NLS-1$
		this.applyButton.setMargin(UIStorage.INSET_NULL);

		box.add(Box.createHorizontalGlue());
		box.add(this.filterButton);
		box.add(Box.createHorizontalGlue());
		box.add(this.zoomInButton);
		box.add(this.zoomOutButton);
		box.add(this.zoomNoneButton);
		add(box, gbc);

		this.nowButton.setFocusable(false);
		this.nowButton.setToolTipText(LangModelSchedule.getString("CurrentTime")); //$NON-NLS-1$
		this.nowButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.dateSpinner.setValue(new Date(System.currentTimeMillis()));
				PlanToolBar.this.timeSpinner.setValue(new Date(System.currentTimeMillis()));
			}
		});

		this.applyButton.setFocusable(false);
		this.applyButton.setToolTipText(LangModelSchedule.getString("Apply")); //$NON-NLS-1$
		this.applyButton.addActionListener(new ActionListener() {

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

						panel.setDate(date.getTime(), PlanToolBar.this.scaleComboBox.getSelectedIndex());
						PlanToolBar.this.dispatcher
								.notify(new OperationEvent(new Boolean(false), 0,
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