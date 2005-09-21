
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

import javax.swing.AbstractAction;
import javax.swing.Action;
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
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
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

final class PlanToolBar {

	private static final long	serialVersionUID	= -1251916980092015668L;

	private final class FlashIcon implements Icon {

		private boolean	flash	= false;

		public void flash() {
			this.flash = !this.flash;
		}

		public int getIconHeight() {
			return PlanToolBar.this.w + 1;
		}

		public int getIconWidth() {
			return PlanToolBar.this.w + 1;
		}

		public void paintIcon(	final Component c,
		                      	final Graphics g,
		                      	final int x,
		                      	final int y) {
			final Graphics2D g2d = (Graphics2D) g;
			final Color foregroundColor = g.getColor();
			g2d.setColor(this.flash ? SchedulerModel.COLOR_SCHEDULED : SchedulerModel.COLOR_UNRECOGNIZED);
			g2d.fillRect(x + 2, y + 2, PlanToolBar.this.w - 3, PlanToolBar.this.w - 3);
			g2d.draw3DRect(x, y, PlanToolBar.this.w, PlanToolBar.this.w, true);
			g.setColor(foregroundColor);
		}
	}

	int				w;

	JSpinner		dateSpinner		= new DateSpinner();

	PlanPanel		panel;
	Dispatcher		dispatcher;
	AComboBox		scaleComboBox;
	JSpinner		timeSpinner		= new TimeSpinner();

	JToolBar		toolBar;
	SchedulerModel	schedulerModel;

	public PlanToolBar(final ApplicationContext aContext, final PlanPanel panel) {
		if (aContext != null) {
			this.dispatcher = aContext.getDispatcher();
		}
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.panel = panel;

		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);

		final Font font = this.toolBar.getFont();
		final FontMetrics fontMetrics = this.toolBar.getFontMetrics(font);
		this.w = fontMetrics.charWidth('W');

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

		this.toolBar.add(new JLabel(LangModelSchedule.getString("Text.Plan.Toolbar.Scope") + ':')); //$NON-NLS-1$
		CommonUIUtilities.fixHorizontalSize(this.scaleComboBox);
		this.toolBar.add(this.scaleComboBox);
		this.toolBar.addSeparator();
		this.toolBar.add(new JLabel(LangModelSchedule.getString("Text.Plan.Toolbar.Date") + ':')); //$NON-NLS-1$
		
		CommonUIUtilities.fixHorizontalSize(this.dateSpinner);
		this.toolBar.add(this.dateSpinner);
		{
			
			final JButton button = this.toolBar.add(this.createShowCalendarAction());
			button.setFocusable(false);
			button.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		}
		this.toolBar.addSeparator();
		this.toolBar.add(new JLabel(LangModelSchedule.getString("Text.Plan.Toolbar.Time") + ':')); //$NON-NLS-1$

		CommonUIUtilities.fixHorizontalSize(this.timeSpinner);
		this.toolBar.add(this.timeSpinner);

		{
			final JButton button = this.toolBar.add(this.createCurrentDateAction());
			button.setFocusable(false);
			button.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		}

		this.toolBar.addSeparator();
		
		{
			final JButton button = this.toolBar.add(this.createApplyAction());
			button.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			button.setFocusable(false);
		}
		
		this.toolBar.addSeparator();
		{
			final JButton button = this.toolBar.add(this.createLegendAction());
			button.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		}

		
		this.toolBar.add(Box.createHorizontalGlue());
		for(final Action action : this.createZoomActions()) {
			final JButton button = this.toolBar.add(action);
			button.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			button.setFocusable(false);
		}
	}

	@SuppressWarnings("serial")
	private Action createShowCalendarAction() {
		final Action showCalendar = new AbstractAction("", UIStorage.CALENDAR_ICON) {
			public void actionPerformed(ActionEvent e) {
				final Calendar cal = Calendar.getInstance();
				final Date date = (Date) PlanToolBar.this.dateSpinner.getModel().getValue();
				cal.setTime(date);

				final JDialog calendarDialog = CalendarUI
						.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
				calendarDialog.setLocation(new Point(PlanToolBar.this.dateSpinner.getLocationOnScreen().x - 35,
														PlanToolBar.this.dateSpinner.getLocationOnScreen().y
																+ PlanToolBar.this.w));
				calendarDialog.setVisible(true);				
				
				if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK) {
					PlanToolBar.this.dateSpinner.getModel().setValue(cal.getTime());
				}		
			}
		};
		showCalendar.putValue(Action.SHORT_DESCRIPTION, 
			LangModelSchedule.getString("Text.Plan.Toolbar.Calendar"));
		return showCalendar;
	}	

	@SuppressWarnings("serial")
	private Action createCurrentDateAction() {
		final Action currentDate = new AbstractAction("", UIStorage.TIME_ICON) {
			public void actionPerformed(ActionEvent e) {
				final Date date = new Date();
				PlanToolBar.this.dateSpinner.setValue(date);
				PlanToolBar.this.timeSpinner.setValue(date);				
			}
		};
		currentDate.putValue(Action.SHORT_DESCRIPTION, 
			LangModelSchedule.getString("Text.Plan.Toolbar.CurrentTime"));
		return currentDate;
	}

	@SuppressWarnings("serial")
	private Action createApplyAction() {
		final Action apply = new AbstractAction("", UIStorage.REFRESH_ICON) {
			public void actionPerformed(final ActionEvent e) {
				final JButton button = (JButton) e.getSource();
				button.setEnabled(false);
				new ProcessingDialog(new Runnable() {

					public void run() {
						try {
							PlanToolBar.this.dispatcher
							.firePropertyChange(new StatusMessageEvent(PlanToolBar.this, StatusMessageEvent.STATUS_PROGRESS_BAR, true));
							PlanToolBar.this.schedulerModel.commitChanges();
							
							Calendar date = Calendar.getInstance();
							date.setTime((Date) PlanToolBar.this.dateSpinner.getValue());
							Calendar time = Calendar.getInstance();
							time.setTime((Date) PlanToolBar.this.timeSpinner.getValue());

							date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
							date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

							PlanToolBar.this.panel.setDate(date.getTime(), PlanToolBar.this.scaleComboBox.getSelectedIndex());
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
		};
		apply.putValue(Action.SHORT_DESCRIPTION, LangModelSchedule.getString("Text.Plan.Toolbar.Apply")); 
		return apply;
	}
	
	@SuppressWarnings("serial")
	private Action[] createZoomActions() {
		// Zoom actions 		
		final Action[] zoomActions = new Action[3];
		zoomActions[0] = new AbstractAction("", UIStorage.ZOOMIN_ICON){
			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(1.25);				
			}
		};
		zoomActions[0].putValue(Action.SHORT_DESCRIPTION, 
			LangModelSchedule.getString("Text.Plan.Toolbar.ZoomIn"));
		zoomActions[0].putValue(Action.MNEMONIC_KEY, 
			Integer.valueOf(KeyEvent.VK_PLUS));
		zoomActions[0].putValue(Action.ACCELERATOR_KEY, 
			KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.ALT_MASK));

		zoomActions[1] = new AbstractAction("", UIStorage.ZOOMOUT_ICON){
			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale(.8);				
			}
		};
		zoomActions[1].putValue(Action.SHORT_DESCRIPTION, 
			LangModelSchedule.getString("Text.Plan.Toolbar.ZoomOut"));
		zoomActions[1].putValue(Action.MNEMONIC_KEY, 
			Integer.valueOf(KeyEvent.VK_MINUS));
		zoomActions[1].putValue(Action.ACCELERATOR_KEY, 
			KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.ALT_MASK));
		
		zoomActions[2] = new AbstractAction("", UIStorage.NOZOOM_ICON){
			public void actionPerformed(ActionEvent e) {
				PlanToolBar.this.panel.updateScale2Fit();				
			}
		};
		zoomActions[2].putValue(Action.SHORT_DESCRIPTION, 
			LangModelSchedule.getString("Text.Plan.Toolbar.ActualSize"));
		zoomActions[2].putValue(Action.MNEMONIC_KEY, 
			Integer.valueOf(KeyEvent.VK_ASTERISK));
		zoomActions[2].putValue(Action.ACCELERATOR_KEY, 
			KeyStroke.getKeyStroke(KeyEvent.VK_ASTERISK, ActionEvent.ALT_MASK));

		return zoomActions;
	}
	
	private Icon getColorIcon(final Color color) {
		final BufferedImage img = new BufferedImage(this.w + 1, this.w + 1, BufferedImage.TYPE_INT_ARGB_PRE);
		final Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setColor(color);
		g2d.fillRect(2, 2, this.w - 3, this.w - 3);
		g2d.draw3DRect(0, 0, this.w, this.w, true);
		return new ImageIcon(img);
	}
	
	@SuppressWarnings("serial")
	private Action createLegendAction() {
		final JDialog dialog = new JDialog();
		
		final Action apply = new AbstractAction(
			LangModelSchedule.getString("Text.Plan.Toolbar.Legend")) {
			public void actionPerformed(final ActionEvent e) {
				final JButton button = (JButton) e.getSource();
				dialog.setLocationRelativeTo(button);
				dialog.setVisible(true);
			}
		};
		
		dialog.setTitle(LangModelSchedule.getString("Text.Plan.Toolbar.Legend"));
		JPanel legendPanel = new JPanel(new GridLayout(0, 1));
		legendPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		final FlashIcon flashIcon = new FlashIcon();
		final JLabel flashLabel = new JLabel(LangModelSchedule.getString("Text.Test.Status.NotSaved"), flashIcon,
												SwingConstants.LEFT);
		final Timer timer = new Timer(PlanPanel.TIME_OUT, new ActionListener() {

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

		return apply;
	
	}

	public JToolBar getToolBar() {
		return this.toolBar;
	}


}
