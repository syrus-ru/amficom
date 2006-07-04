/*-
 * $Id: StatusBar.java,v 1.13 2006/07/04 14:19:38 saa Exp $
 *
 * Copyright ╘ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2006/07/04 14:19:38 $
 * @author $Author: saa $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class StatusBar implements PropertyChangeListener {

	public static final String FIELD_PREFIX = "field";
	public static final String FIELD_DOMAIN = "domain";
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_SERVER = "server";
	public static final String FIELD_SESSION = "session";
	public static final String FIELD_USER = "user";
	public static final String FIELD_TIME = "time";

	private class TableHeaderCellRenderer extends DefaultTableCellRenderer {

		private static final long	serialVersionUID	= 1416487175210085862L;

		public TableHeaderCellRenderer() {
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setBorder(border);
		}

		@Override
		public Component getTableCellRendererComponent(final JTable jtable,
				final Object value,
				final boolean isSelected,
				final boolean hasFocus,
				final int row,
				final int column) {
			if (jtable != null) {
				final JTableHeader header = jtable.getTableHeader();
				if (header != null) {
					this.setForeground(header.getForeground());
					this.setBackground(header.getBackground());
					this.setFont(header.getFont());
				}
			}

			if (value instanceof Component) {
				return (Component) value;
			} else {
				this.setText((value == null) ? "" : value.toString());
			}
			// this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			return this;
		}

	}

	private static class ProgressState {
		String message;
		boolean determinate;
		int percentage;
		int nextPercents() {
			if (!this.determinate) {
				this.percentage = (this.percentage + 2) % 100;
			}
			return this.percentage;
		}
		ProgressState(String message) {
			this.message = message;
			this.determinate = false;
			this.percentage = 0;
		}
	}

	private JPanel panel;
	private DefaultTableColumnModel model;
	private DefaultTableModel tableModel;
	private JTable table;

	final Border border = BorderFactory.createBevelBorder(
			BevelBorder.LOWERED,
			Color.WHITE,
			Color.WHITE,
			new Color(142, 142, 142),
			new Color(99, 99, 99));


	private Timer timeTimer;

	// ProgressBar control fields

	/**
	 * Вложенные ProgressBar'ы. Самый новый имеет индекс 0.
	 */
	private List<ProgressState> progressStates = new LinkedList<ProgressState>();
	/**
	 * Текст, лежащий в виртуальном поле STATUS. Отображается, когда исчезает
	 * последний ProgressBar.
	 */
	private String statusText;
	/**
	 * Собственно полоска ProgressBar'а. Она одна, но ее состояние управляется
	 * текущим ProgressState'ом.
	 */
	private JProgressBar progressBar;
	private JLabel progressLabel;
	/**
	 * Панель с ProgressBar'ом. Обновление поля в TableHeader происходит
	 * повторной установкой этой панели в TableHeader.
	 */
	private JPanel progressPanel;
	/**
	 * Таймер автоматического обновления ProgressBar'а. Используется только
	 * в indeterminate режиме.
	 */
	private Timer progressBarTimer1;

	public StatusBar() {
		this.createModels();
		this.createUI();
	}

	private void createModels() {
		this.tableModel = new DefaultTableModel(1, 1);
		this.model = new DefaultTableColumnModel();
	}

	private void createUI() {
		this.table = new JTable(this.tableModel, this.model);
		this.table.setRowHeight(1);

		this.table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 4968210937248459683L;

			@Override
			public Component getTableCellRendererComponent(final JTable jtable,
					final Object value,
					final boolean isSelected,
					final boolean hasFocus,
					final int row,
					final int column) {
				super.getTableCellRendererComponent(jtable, value, isSelected, hasFocus, row, column);
				this.setBorder(null);
				return this;
			}
		});

		final JTableHeader tableHeader = this.table.getTableHeader();
		tableHeader.setDefaultRenderer(new TableHeaderCellRenderer());

		this.panel = new JPanel(new GridBagLayout());
		// this.panel.setBorder(BorderFactory.createEtchedBorder());

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		this.panel.add(tableHeader, gbc);
		this.panel.add(this.table, gbc);
	}


	public JPanel getPanel() {
		return this.panel;
	}

	public void add(final String fieldId) {
		this.add(-1, fieldId);
	}

	public void add(final int index, final String fieldId) {
		assert fieldId != null : ErrorMessages.NON_NULL_EXPECTED;
		final TableColumn column = new TableColumn();
		column.setHeaderValue(" " + fieldId);
		column.setIdentifier(fieldId);
		this.model.addColumn(column);
		final int index2 = this.model.getColumnIndex(fieldId);
		if (index >= 0) {
			this.model.moveColumn(index2, index);
		}

		if (fieldId.equals(FIELD_TIME)) {
			this.addTimerField(fieldId);
		}
	}

	public void remove(final String fieldId) {
		assert fieldId != null : ErrorMessages.NON_NULL_EXPECTED;

		// XXX: при удалении поля с ProgressBar'ом надо бы удалить и сам ProgressBar
		for (int i = 0; i < this.model.getColumnCount(); i++) {
			final TableColumn column = this.model.getColumn(i);
			if (fieldId.equals(column.getIdentifier())) {
				this.model.removeColumn(column);
				break;
			}
		}

	}

	void setValue(final String fieldId, final Object value) {
		final JTableHeader tableHeader = this.table.getTableHeader();
		TableColumn column = null;
		int index;
		for (index = 0; index < this.model.getColumnCount(); index++) {
			column = this.model.getColumn(index);
			Object identifier = column.getIdentifier();
			if (fieldId.equals(identifier)) {
				break;
			}
		}
		if (column != null) {
			column.setHeaderValue(value);
			final Rectangle headerRect = tableHeader.getHeaderRect(index);
			if (headerRect != null) {
				final Component componentAt = tableHeader.getComponentAt(headerRect.x, headerRect.y);
				if (componentAt != null) {
					componentAt.repaint();
				}
			}
		} else {
			Log.debugMessage("StatusBar field not found: " + fieldId, Level.WARNING);
		}
	}

	private void setText0(final String fieldId, final String text) {
		this.setValue(fieldId, text);
	}

	public void setWidth(final String fieldId, final int width) {
		this.table.getColumn(fieldId).setPreferredWidth(width);
	}

	private void addTimerField(final String fieldId) {

		if (this.timeTimer == null) {
			this.timeTimer = new Timer(1000, new ActionListener() {

				public void actionPerformed(final ActionEvent e) {
					final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.HOURS_MINUTES_SECONDS_DATE_FORMAT);
					StatusBar.this.setText(fieldId, sdf.format(new Date()));
				}
			});

			this.timeTimer.start();
		}
	}

	public void removeDispatcher(final Dispatcher dispatcher) {
		if (dispatcher != null) {
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_MESSAGE, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_USER, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_SERVER, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_SESSION, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_DOMAIN, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_BEGIN, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_END, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_PERCENTS, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_TEXT, this);
		}
	}

	public void addDispatcher(final Dispatcher dispatcher) {
		if (dispatcher != null) {
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_MESSAGE, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_USER, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_SERVER, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_SESSION, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_DOMAIN, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_BEGIN, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_END, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_PERCENTS, this);
			dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_TEXT, this);
		}
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		final String propertyName = evt.getPropertyName();
		if (evt instanceof StatusMessageEvent) {
			final StatusMessageEvent sme = (StatusMessageEvent) evt;
			if (propertyName.equals(StatusMessageEvent.STATUS_MESSAGE)) {
				this.setStatusText(sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_SESSION)) {
				this.setText0(FIELD_SESSION, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_SERVER)) {
				this.setText0(FIELD_SERVER, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_USER)) {
				this.setText0(FIELD_USER, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_DOMAIN)) {
				this.setText0(FIELD_DOMAIN, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_PROGRESS_BEGIN)) {
				 this.beginProgressBar(sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_PROGRESS_END)) {
				 this.endProgressBar();
			} else if (propertyName.equals(StatusMessageEvent.STATUS_PROGRESS_PERCENTS)) {
				 this.setProgressBarPercents(sme.getInteger());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_PROGRESS_TEXT)) {
				 this.setProgressBarText(sme.getText());
			}
		}
	}

	/**
	 * Устанавливает значение виртуального текстового поля.
	 * Термин "виртуальный" означает, что некоторые поля
	 * не обязательно непосредственно отображаются на экране:
	 * поле статуса может быть закрыто ProgressBar'ом.
	 * 
	 * @param fieldId id поля
	 * @param text текст
	 */
	public void setText(final String fieldId, final String text) {
		if (fieldId.equals(FIELD_STATUS)) {
			this.setStatusText(text);
		} else {
			this.setValue(fieldId, text);
		}
	}

	// ProgressBar stuff

	ProgressState getCurrentProgressState() {
		return this.progressStates.get(this.progressStates.size() - 1);
	}

	boolean isProgressBarOn() {
		return !this.progressStates.isEmpty();
	}

	private void setStatusText(String text) {
		this.statusText = text;
		if (!isProgressBarOn()) {
			this.setText0(FIELD_STATUS, text);
		}
	}

	private void beginProgressBar(final String text) {
		if (!this.isProgressBarOn()) {
			this.progressBar = new JProgressBar();
			this.progressPanel = new JPanel();
			this.progressLabel = new JLabel();
			this.progressLabel.setFont(this.table.getTableHeader().getFont());

//			this.progressBar.setBorder(BorderFactory.createEmptyBorder());
			this.progressPanel.setBorder(border);
			this.progressPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

			this.progressPanel.add(this.progressBar);
			this.progressPanel.add(progressLabel);
		}
		this.progressStates.add(0, new ProgressState(text));
		updateProgressPanel();
//		this.progressBar0.setString(text);
	}

	private void endProgressBar() {
		if (!this.isProgressBarOn()) {
			throw new IllegalStateException();
		}
		this.progressStates.remove(0);
		updateProgressPanel();
		if (!this.isProgressBarOn()) {
			// remove progress bar
			this.progressPanel = null;
			this.progressLabel = null;
			this.progressBar = null;
			this.setText0(FIELD_STATUS, this.statusText);
		}
	}

	private void setProgressBarPercents(int percents) {
		if (!this.isProgressBarOn()) {
			throw new IllegalStateException("No progress bar active");
		}
		final ProgressState currentState = getCurrentProgressState();
		currentState.determinate = true;
		currentState.percentage = percents;
		updateProgressPanel();
	}

	private void setProgressBarText(String text) {
		if (!this.isProgressBarOn()) {
			throw new IllegalStateException("No progress bar active");
		}
		final ProgressState currentState = getCurrentProgressState();
		currentState.message = text;
		updateProgressPanel();
	}

	private void updateProgressPanel() {
		// нужен ли таймер в текущем состоянии?
		boolean needTimer = isProgressBarOn() && !getCurrentProgressState().determinate;
		if (needTimer && this.progressBarTimer1 == null) {
			this.progressBarTimer1 = new Timer(100, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateProgressBar();
				}
			});
			this.progressBarTimer1.setRepeats(true);
			this.progressBarTimer1.start();
		} else if (!needTimer && this.progressBarTimer1 != null) {
			this.progressBarTimer1.stop();
			this.progressBarTimer1 = null;
		}

		// устанавливаем текст (для progressBar, но не для поля)
		if (isProgressBarOn()) {
			final String string = getCurrentProgressState().message;
//			this.progressBar0.setString(getCurrentProgressState().message);
			this.progressLabel.setText(string == null ? null : " " + string);
		}

		// немедленно отображаем изменения
		updateProgressBar();
	}

	void updateProgressBar() {
		if (!isProgressBarOn()) {
			return;
		}

		final ProgressState currentState = getCurrentProgressState();
		final JTableHeader tHeader = this.table.getTableHeader();

		this.progressBar.setValue(currentState.nextPercents());
		this.progressBar.setStringPainted(currentState.determinate);

		this.progressBar.setFont(tHeader.getFont()); // adjust font

		// размер придется выставить таким, чтобы вписываться в таблицу,
		// см. проблемы BasicProgressBarUI#getPreferredSize().
		// XXX: корректируем размер при каждом обновлении
		this.progressBar.setPreferredSize(new Dimension(
				this.progressBar.getPreferredSize().width,
				tHeader.getFontMetrics(tHeader.getFont()).getHeight()));

		setValue(FIELD_STATUS, this.progressPanel);
	}
}
