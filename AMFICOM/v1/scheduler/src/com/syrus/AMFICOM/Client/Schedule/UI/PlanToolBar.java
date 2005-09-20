
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.ProcessingDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.filter.UI.CalendarUI;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.newFilter.DateSpinner;
import com.syrus.AMFICOM.newFilter.TimeSpinner;

class PlanToolBar {

	private static final long	serialVersionUID	= -1251916980092015668L;

	private class FlashIcon implements Icon {

		private boolean	flash	= false;

		public void flash() {
			this.flash = !this.flash;
		}

		public int getIconHeight() {
			return PlanToolBar.this.h + 1;
		}

		public int getIconWidth() {
			return PlanToolBar.this.w + 1;
		}

		public void paintIcon(	Component c,
								Graphics g,
								int x,
								int y) {
			Graphics2D g2d = (Graphics2D) g;
			Color foregroundColor = g.getColor();
			g2d.setBackground(Color.lightGray);
			g2d.clearRect(x, y, PlanToolBar.this.w + 1, PlanToolBar.this.h + 1);
			g2d.setColor(this.flash ? SchedulerModel.COLOR_SCHEDULED : SchedulerModel.COLOR_UNRECOGNIZED);
			g2d.fillRect(x + 2, y + 2, PlanToolBar.this.w - 3, PlanToolBar.this.h - 3);
			g2d.draw3DRect(x, y, PlanToolBar.this.w, PlanToolBar.this.h, true);
			g.setColor(foregroundColor);
		}
	}

	int				w;
	int				h;
	private JButton	applyButton		= new JButton();

	private JButton	dateButton		= new JButton(UIStorage.CALENDAR_ICON);
	JSpinner		dateSpinner		= new DateSpinner();

	private JButton	nowButton		= new JButton(UIStorage.TIME_ICON);

	PlanPanel		panel;
	// private ApplicationContext aContext;
	Dispatcher		dispatcher;
	AComboBox		scaleComboBox;
	JSpinner		timeSpinner		= new TimeSpinner();

//	FilterDialog	filterDialog;

	private JButton	filterButton	= new JButton();
	private JButton	zoomInButton	= new JButton();
	private JButton	zoomNoneButton	= new JButton();
	private JButton	zoomOutButton	= new JButton();

	JToolBar		toolBar;

	public PlanToolBar(final ApplicationContext aContext, final PlanPanel panel) {
		if (aContext != null) {
			this.dispatcher = aContext.getDispatcher();
		}
		final SchedulerModel schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.panel = panel;

		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);

		final Font font2 = this.toolBar.getFont();
		final FontMetrics fontMetrics = this.toolBar.getFontMetrics(font2);
		this.w = fontMetrics.charWidth('W');
		this.h = this.w;

		final String[] scales = new String[PlanPanel.SCALES.length];
		for (int i = 0; i < scales.length; i++) {
			scales[i] = new String(LangModelSchedule.getString(PlanPanel.SCALES[i]));
		}
		this.scaleComboBox = new AComboBox(scales);
		this.scaleComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				AComboBox comboBox = (AComboBox) e.getSource();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					final Calendar calDate = Calendar.getInstance();
					calDate.setTime((Date) PlanToolBar.this.dateSpinner.getModel().getValue());
					final Calendar timeDate = Calendar.getInstance();
					timeDate.setTime((Date) PlanToolBar.this.timeSpinner.getModel().getValue());
					calDate.set(Calendar.HOUR_OF_DAY, timeDate.get(Calendar.HOUR_OF_DAY));
					calDate.set(Calendar.MINUTE, timeDate.get(Calendar.MINUTE));
					panel.setStartDate(calDate.getTime());
					panel.setScale(comboBox.getSelectedIndex());
					panel.updateTestLinesTimeRegion();
				}
			}
		});
		this.scaleComboBox.setSelectedItem(LangModelSchedule.getString(PlanPanel.SCALES[PlanPanel.SCALES.length - 2]));

		
		final ChangeListener timeListener = new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				final Calendar calDate = Calendar.getInstance();
				calDate.setTime((Date) PlanToolBar.this.dateSpinner.getModel().getValue());
				final Calendar timeDate = Calendar.getInstance();
				timeDate.setTime((Date) PlanToolBar.this.timeSpinner.getModel().getValue());
				calDate.set(Calendar.HOUR_OF_DAY, timeDate.get(Calendar.HOUR_OF_DAY));
				calDate.set(Calendar.MINUTE, timeDate.get(Calendar.MINUTE));
				panel.setStartDate(calDate.getTime());
				panel.updateTestLinesTimeRegion();
			}
		};
		
		this.dateSpinner.addChangeListener(timeListener);

		this.timeSpinner.addChangeListener(timeListener);

		this.dateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.dateButton.setFocusable(false);
		this.dateButton.setToolTipText(LangModelSchedule.getString("Text.Plan.Toolbar.Calendar")); //$NON-NLS-1$
		this.dateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Calendar cal = Calendar.getInstance();
				final Date date = (Date) PlanToolBar.this.dateSpinner.getModel().getValue();
				cal.setTime(date);

				final JDialog calendarDialog = CalendarUI
						.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
				calendarDialog.setLocation(new Point(PlanToolBar.this.dateSpinner.getLocationOnScreen().x - 35,
														PlanToolBar.this.dateSpinner.getLocationOnScreen().y
																+ PlanToolBar.this.h));
				calendarDialog.setVisible(true);				
				
				if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK) {
					PlanToolBar.this.dateSpinner.getModel().setValue(cal.getTime());
				}
			}
		});
		
		this.zoomInButton.setMnemonic(Integer.valueOf(KeyEvent.VK_PLUS));
		this.zoomInButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.zoomInButton.setFocusable(false);
		this.zoomInButton.setIcon(UIStorage.ZOOMIN_ICON);
		this.zoomInButton.setToolTipText(LangModelSchedule.getString("Text.Plan.Toolbar.ZoomIn")); //$NON-NLS-1$
		this.zoomInButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(1.25);
			}
		});

		this.filterButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.filterButton.setFocusable(false);
		this.filterButton.setIcon(UIStorage.FILTER_ICON);
		this.filterButton.setToolTipText(LangModelSchedule.getString("Text.Plan.Toolbar.Filtration")); //$NON-NLS-1$		

		this.zoomOutButton.setMnemonic(Integer.valueOf(KeyEvent.VK_MINUS));
		this.zoomOutButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.zoomOutButton.setFocusable(false);
		this.zoomOutButton.setIcon(UIStorage.ZOOMOUT_ICON);
		this.zoomOutButton.setToolTipText(LangModelSchedule.getString("Text.Plan.Toolbar.ZoomOut")); //$NON-NLS-1$
		this.zoomOutButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(.8);
			}
		});
		
		this.zoomNoneButton.setMnemonic(Integer.valueOf(KeyEvent.VK_ASTERISK));
		this.zoomNoneButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.zoomNoneButton.setFocusable(false);
		this.zoomNoneButton.setIcon(UIStorage.NOZOOM_ICON);
		this.zoomNoneButton.setToolTipText(LangModelSchedule.getString("Text.Plan.Toolbar.ActualSize")); //$NON-NLS-1$
		this.zoomNoneButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale2Fit();
			}
		});

		this.toolBar.add(new JLabel(LangModelSchedule.getString("Text.Plan.Toolbar.Scope") + ':')); //$NON-NLS-1$
		CommonUIUtilities.fixHorizontalSize(this.scaleComboBox);
		this.toolBar.add(this.scaleComboBox);
		this.toolBar.addSeparator();
		this.toolBar.add(new JLabel(LangModelSchedule.getString("Text.Plan.Toolbar.Date") + ':')); //$NON-NLS-1$
		
		CommonUIUtilities.fixHorizontalSize(this.dateSpinner);
		this.toolBar.add(this.dateSpinner);
		this.toolBar.add(this.dateButton);
		this.toolBar.addSeparator();
		this.toolBar.add(new JLabel(LangModelSchedule.getString("Text.Plan.Toolbar.Time") + ':')); //$NON-NLS-1$

		CommonUIUtilities.fixHorizontalSize(this.timeSpinner);
		this.toolBar.add(this.timeSpinner);

		this.nowButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.toolBar.add(this.nowButton);
		this.toolBar.addSeparator();
		this.toolBar.add(this.applyButton);
		JButton legendButton = new JButton(LangModelSchedule.getString("Text.Plan.Toolbar.Legend"));
		legendButton.setToolTipText(LangModelSchedule.getString("Text.Plan.Toolbar.Legend"));
		legendButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.toolBar.addSeparator();
		this.toolBar.add(legendButton);
		{

			final JDialog dialog = new JDialog();
			dialog.setTitle(LangModelSchedule.getString("Text.Plan.Toolbar.Legend"));
			JPanel legendPanel = new JPanel(new GridLayout(0, 1));
			legendPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			final FlashIcon flashIcon = new FlashIcon();
			final JLabel flashLabel = new JLabel(LangModelSchedule.getString("Text.Test.Status.NotSaved"), flashIcon,
													SwingConstants.LEFT);
			javax.swing.Timer timer = new javax.swing.Timer(PlanPanel.TIME_OUT, new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					flashIcon.flash();
					flashLabel.repaint();
					flashLabel.revalidate();
				}
			});

			timer.start();

			legendPanel.add(flashLabel);
			legendPanel.add(new JLabel(LangModelSchedule.getString("Text.Test.Status.Scheduled"), this
					.getColorIcon(SchedulerModel.COLOR_SCHEDULED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Text.Test.Status.Completed"), this
					.getColorIcon(SchedulerModel.COLOR_COMPLETED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Text.Test.Status.Processing"), this
					.getColorIcon(SchedulerModel.COLOR_PROCCESSING), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Text.Test.Status.Aborted"), this
					.getColorIcon(SchedulerModel.COLOR_ABORDED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Text.Test.Status.Stopped"), this
				.getColorIcon(SchedulerModel.COLOR_STOPPED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Text.Test.Status.Alarm"), this
					.getColorIcon(SchedulerModel.COLOR_ALARM), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Text.Test.Status.Warning"), this
					.getColorIcon(SchedulerModel.COLOR_WARNING), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Text.Test.Status.Unrecognized"), this
					.getColorIcon(SchedulerModel.COLOR_UNRECOGNIZED), SwingConstants.LEFT));
			dialog.getContentPane().add(legendPanel);
			dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			dialog.pack();
			dialog.addFocusListener(new FocusListener() {

				public void focusGained(FocusEvent e) {
					// nothing

				}

				public void focusLost(FocusEvent e) {
					dialog.setVisible(!dialog.isVisible());

				}
			});
			legendButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					dialog.setLocationRelativeTo(button);
					Point l = dialog.getLocation();
					dialog.setLocation(l.x + dialog.getWidth() / 2, l.y + dialog.getHeight() / 2);
					dialog.setVisible(true);
				}
			});
		}

		this.applyButton.setIcon(UIStorage.REFRESH_ICON);
		this.applyButton.setToolTipText(LangModelSchedule.getString("Text.Plan.Toolbar.Apply")); //$NON-NLS-1$
		this.applyButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));

		/**
		 * @TODO add when TestFilter've been updated
		 */
		// box.add(this.filterButton);
		this.toolBar.add(Box.createHorizontalGlue());
		this.toolBar.add(this.zoomInButton);
		this.toolBar.add(this.zoomOutButton);
		this.toolBar.add(this.zoomNoneButton);

		this.nowButton.setFocusable(false);
		this.nowButton.setToolTipText(LangModelSchedule.getString("Text.Plan.Toolbar.CurrentTime")); //$NON-NLS-1$
		this.nowButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Date date = new Date(System.currentTimeMillis());
				PlanToolBar.this.dateSpinner.setValue(date);
				PlanToolBar.this.timeSpinner.setValue(date);
			}
		});

		this.applyButton.setFocusable(false);
		this.applyButton.setToolTipText(LangModelSchedule.getString("Text.Plan.Toolbar.PerformAndAcquireTests")); //$NON-NLS-1$
		this.applyButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final JButton button = (JButton) e.getSource();
				button.setEnabled(false);
				new ProcessingDialog(new Runnable() {

					public void run() {
						try {
							PlanToolBar.this.dispatcher
							.firePropertyChange(new StatusMessageEvent(PlanToolBar.this, StatusMessageEvent.STATUS_PROGRESS_BAR, true));
							schedulerModel.commitChanges();
							
							Calendar date = Calendar.getInstance();
							date.setTime((Date) PlanToolBar.this.dateSpinner.getValue());
							Calendar time = Calendar.getInstance();
							time.setTime((Date) PlanToolBar.this.timeSpinner.getValue());

							date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
							date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

							panel.setDate(date.getTime(), PlanToolBar.this.scaleComboBox.getSelectedIndex());
							PlanToolBar.this.dispatcher
									.firePropertyChange(new StatusMessageEvent(PlanToolBar.this, StatusMessageEvent.STATUS_PROGRESS_BAR, false));

							button.setEnabled(true);
						} catch (final ApplicationException e1) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelSchedule.getString("Error.CannotRefreshTests"),
								LangModelGeneral.getString("Error"),
								JOptionPane.OK_OPTION);
							return;
						}
					}
				}, LangModelSchedule.getString("StatusMessage.UpdatingTests"));
			}
		});		
	}

	public JToolBar getToolBar() {
		return this.toolBar;
	}

	public Icon getColorIcon(Color color) {
		int x = 0;
		int y = 0;
		BufferedImage img = new BufferedImage(this.w + 1, this.h + 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setBackground(Color.lightGray);
		g2d.clearRect(x, y, this.w + 1, this.h + 1);
		g2d.setColor(color);
		g2d.fillRect(x + 2, y + 2, this.w - 3, this.h - 3);
		g2d.draw3DRect(x, y, this.w, this.h, true);
		Icon icon = new ImageIcon(img);
		return icon;
	}
}
