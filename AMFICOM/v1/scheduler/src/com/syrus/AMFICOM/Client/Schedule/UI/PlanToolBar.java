
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
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
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
		// this.aContext = aContext;
		if (aContext != null)
			this.dispatcher = aContext.getDispatcher();
		final SchedulerModel schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.panel = panel;

		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);

		Font font2 = this.toolBar.getFont();
		FontMetrics fontMetrics = this.toolBar.getFontMetrics(font2);
		this.w = fontMetrics.charWidth('W');
		this.h = this.w;

		String[] scales = new String[PlanPanel.SCALES.length];
		for (int i = 0; i < scales.length; i++)
			scales[i] = new String(LangModelSchedule.getString(PlanPanel.SCALES[i]));
		this.scaleComboBox = new AComboBox(scales);
		this.scaleComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				AComboBox comboBox = (AComboBox) e.getSource();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// System.out.println("comboBox.getSelectedIndex():" +
					// comboBox.getSelectedIndex());
					Calendar calDate = Calendar.getInstance();
					calDate.setTime((Date) PlanToolBar.this.dateSpinner.getModel().getValue());
					Calendar timeDate = Calendar.getInstance();
					timeDate.setTime((Date) PlanToolBar.this.timeSpinner.getModel().getValue());
					calDate.set(Calendar.HOUR_OF_DAY, timeDate.get(Calendar.HOUR_OF_DAY));
					calDate.set(Calendar.MINUTE, timeDate.get(Calendar.MINUTE));
					panel.setStartDate(calDate.getTime());
					panel.setScale(comboBox.getSelectedIndex());
					// panel.updateTests();
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
				// panel.updateTests();
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
				// panel.updateTests();
				panel.updateTestLines();
			}
		});

		this.dateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.dateButton.setFocusable(false);
		this.dateButton.setToolTipText(LangModelSchedule.getString("Calendar")); //$NON-NLS-1$
		this.dateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// showCalendar();
				Calendar cal = Calendar.getInstance();
				Date date = (Date) PlanToolBar.this.dateSpinner.getModel().getValue();
				cal.setTime(date);

				final JDialog calendarDialog = CalendarUI
						.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
				calendarDialog.pack();
				calendarDialog.setResizable(false);
				calendarDialog.setLocation(new Point(PlanToolBar.this.dateSpinner.getLocationOnScreen().x - 35,
														PlanToolBar.this.dateSpinner.getLocationOnScreen().y
																+ PlanToolBar.this.h));
				calendarDialog.setVisible(true);				
				
				if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
					PlanToolBar.this.dateSpinner.getModel().setValue(cal.getTime());
			}
		});
		this.zoomInButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.zoomInButton.setFocusable(false);
		this.zoomInButton.setIcon(UIStorage.ZOOMIN_ICON);
		this.zoomInButton.setToolTipText(LangModelSchedule.getString("ZoomIn")); //$NON-NLS-1$
		this.zoomInButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(1.25);
			}
		});

		this.filterButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.filterButton.setFocusable(false);
		this.filterButton.setIcon(UIStorage.FILTER_ICON);
		this.filterButton.setToolTipText(LangModelSchedule.getString("Filtration")); //$NON-NLS-1$
		// this.filterButton.addActionListener(new ActionListener() {
		//
		// public void actionPerformed(ActionEvent e) {
		// ObjectResourceFilter filter = ((SchedulerModel)
		// aContext.getApplicationModel()).getFilter();
		// TestFilter orf = (TestFilter) filter.clone();
		//				
		// /**
		// * FIXME recast without create new instance of FilterDialog
		// */
		// //if (PlanToolBar.this.filterDialog == null) {
		// PlanToolBar.this.filterDialog = new FilterDialog(orf, aContext);
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// Dimension frameSize = PlanToolBar.this.filterDialog.getSize();
		// frameSize.width = 450;
		// frameSize.height = frameSize.height + 20;
		// PlanToolBar.this.filterDialog.setSize(frameSize);
		//
		// if (frameSize.height > screenSize.height)
		// frameSize.height = screenSize.height;
		// if (frameSize.width > screenSize.width)
		// frameSize.width = screenSize.width;
		// PlanToolBar.this.filterDialog.setLocation((screenSize.width -
		// frameSize.width) / 2,
		// (screenSize.height - frameSize.height) / 2);
		// //}
		// //else PlanToolBar.this.filterDialog.setF
		// PlanToolBar.this.filterDialog.pack();
		//
		// PlanToolBar.this.filterDialog.setModal(true);
		// PlanToolBar.this.filterDialog.setVisible(true);
		// PlanToolBar.this.filterDialog.setFilter(orf);
		//
		// if (PlanToolBar.this.filterDialog.retcode ==
		// FilterDialog.RETURN_CODE_OK) {
		// ((SchedulerModel) aContext.getApplicationModel()).setFilter(orf);
		// }
		// }
		// });

		this.zoomOutButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.zoomOutButton.setFocusable(false);
		this.zoomOutButton.setIcon(UIStorage.ZOOMOUT_ICON);
		this.zoomOutButton.setToolTipText(LangModelSchedule.getString("ZoomOut")); //$NON-NLS-1$
		this.zoomOutButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(.8);
			}
		});
		this.zoomNoneButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.zoomNoneButton.setFocusable(false);
		this.zoomNoneButton.setIcon(UIStorage.NOZOOM_ICON);
		this.zoomNoneButton.setToolTipText(LangModelSchedule.getString("ZoomNone")); //$NON-NLS-1$
		this.zoomNoneButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale2Fit();
			}
		});

		this.toolBar.add(new JLabel(LangModelSchedule.getString("Detalization") + ':')); //$NON-NLS-1$
		CommonUIUtilities.fixHorizontalSize(this.scaleComboBox);
		this.toolBar.add(this.scaleComboBox);
		this.toolBar.addSeparator();
		this.toolBar.add(new JLabel(LangModelSchedule.getString("Date") + ':')); //$NON-NLS-1$
		
		CommonUIUtilities.fixHorizontalSize(this.dateSpinner);
		this.toolBar.add(this.dateSpinner);
		this.toolBar.add(this.dateButton);
		this.toolBar.addSeparator();
		this.toolBar.add(new JLabel(LangModelSchedule.getString("Time") + ':')); //$NON-NLS-1$

		CommonUIUtilities.fixHorizontalSize(this.timeSpinner);
		this.toolBar.add(this.timeSpinner);

		this.nowButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.toolBar.add(this.nowButton);
		this.toolBar.addSeparator();
		this.toolBar.add(this.applyButton);
		JButton legendButton = new JButton(LangModelSchedule.getString("Legend"));
		legendButton.setToolTipText(LangModelSchedule.getString("Legend"));
		legendButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.toolBar.addSeparator();
		this.toolBar.add(legendButton);
		{

			final JDialog dialog = new JDialog();
			dialog.setTitle(LangModelSchedule.getString("Legend"));
			JPanel legendPanel = new JPanel(new GridLayout(0, 1));
			legendPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			final FlashIcon flashIcon = new FlashIcon();
			final JLabel flashLabel = new JLabel(LangModelSchedule.getString("Not_saved"), flashIcon,
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
			legendPanel.add(new JLabel(LangModelSchedule.getString("Scheduled"), this
					.getColorIcon(SchedulerModel.COLOR_SCHEDULED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Completed"), this
					.getColorIcon(SchedulerModel.COLOR_COMPLETED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Processing"), this
					.getColorIcon(SchedulerModel.COLOR_PROCCESSING), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Aborted"), this
					.getColorIcon(SchedulerModel.COLOR_ABORDED), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Alarm"), this
					.getColorIcon(SchedulerModel.COLOR_ALARM), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Warning"), this
					.getColorIcon(SchedulerModel.COLOR_WARNING), SwingConstants.LEFT));
			legendPanel.add(new JLabel(LangModelSchedule.getString("Unrecognized"), this
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
		this.applyButton.setToolTipText(LangModelSchedule.getString("Apply")); //$NON-NLS-1$
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
		this.nowButton.setToolTipText(LangModelSchedule.getString("CurrentTime")); //$NON-NLS-1$
		this.nowButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.dateSpinner.setValue(new Date(System.currentTimeMillis()));
				PlanToolBar.this.timeSpinner.setValue(new Date(System.currentTimeMillis()));
			}
		});

		this.applyButton.setFocusable(false);
		this.applyButton.setToolTipText(LangModelSchedule.getString("Save test to DB, update tests from DB")); //$NON-NLS-1$
		this.applyButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				CommonUIUtilities.invokeAsynchronously(new Runnable() {

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

						} catch (ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(PlanToolBar.this.toolBar, e1);
						} catch (Exception e1) {
							AbstractMainFrame.showErrorMessage(PlanToolBar.this.toolBar, e1);
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
