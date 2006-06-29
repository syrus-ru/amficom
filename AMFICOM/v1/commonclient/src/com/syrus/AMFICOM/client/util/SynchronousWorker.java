/*-
 * $Id: SynchronousWorker.java,v 1.2 2006/06/29 11:08:17 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.util;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.concurrent.Executor;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.model.AbstractMainFrame;


/**
 * Класс обеспечивает обработчикам сообщений AWT
 * возможность выполнения "длительных действий", блокируя действия
 * пользователя, но не блокируя отрисовку экрана.
 * <p>
 * Данный класс расширяет {@link AbstractSynchronousWorker}, определяя
 * поведение пользовательского интерфейса во время выполнения
 * "длительного действия".
 * <p>
 * Основные положение, наследованные от {@link AbstractSynchronousWorker}:
 * <ul>
 * <li> Данным сервисом можно пользоваться только из потока AWT-EventQueue;
 * <li> Клиент должен расширить настоящий класс, реализовав
 *   "длительное действие" в методе {@link #construct()};
 * <li> Метод {@link #construct()} не должен напрямую обращаться к
 *   уже отображенным ("realized") объектам Swing;
 * <li> Метод {@link #execute()} возвращает значение,
 *   которое вернул {@link #construct()};
 * </ul>
 * <p>
 * Дополнительно, данный сервис предоставляет дополнительные возможности
 * по изменению заголовка окна во время выполнения загрузки, выключению
 * indeterminate режима и отображению процентов.
 * В качестве родительского окна для диалога используется
 * {@link AbstractMainFrame#getActiveMainFrame()}.
 * 
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2006/06/29 11:08:17 $
 * @module
 */
public abstract class SynchronousWorker<T> extends AbstractSynchronousWorker<T>{

	String title = null;
	String text = null;
	private boolean showProgressBar = true;
	private JLabel label = null;
	private volatile JDialog dialog = null; // ссылка на родительский диалог
	private JPanel area = null;
	private JProgressBar bar = null;

	/**
	 * @param executor Используемый {@link Executor}, с помощью которого
	 * будет выполняться задача, либо null, чтобы выполнять задачу
	 * в ее индивидуальном потоке.
	 * @param title заголовок для модального диалога ожидания, nullable
	 * @param text текстовое сообщение для модального диалога ожидания, nullable
	 * @param showProgressBar true, чтобы отображать {@link JProgressBar}
	 */
	public SynchronousWorker(Executor executor, String title, String text, boolean showProgressBar) {
		this(executor);
		this.title = title;
		this.text = text;
		this.showProgressBar = showProgressBar;
	}

	/**
	 * @param executor Используемый {@link Executor}, с помощью которого
	 * будет выполняться задача либо null, чтобы выполнять задачу
	 * в ее индивидуальном потоке.
	 * Использование внешнего {@link Executor}'а может быть использовано
	 * для синхронизации.
	 */
	public SynchronousWorker(Executor executor) {
		super(executor);
	}

	@Override
	protected final Frame getParentFrame() {
		return AbstractMainFrame.getActiveMainFrame();
	}

	@Override
	protected final void prepareDialog(JDialog dlg2) {
		assert this.dialog != null; // иначе клиент нарушил API
		this.dialog = dlg2;
		this.area = new JPanel();
		this.label = new JLabel();

		updateDialog();

		this.label.setVisible(true);
		area.add(this.label);

		if (this.showProgressBar) {
			bar = new JProgressBar();
			bar.setIndeterminate(true);
			this.area.add(bar);
		}

		this.area.setVisible(true);
		this.dialog.add(this.area);
		this.dialog.setModal(true);
		this.dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.dialog.pack();
		this.dialog.setLocation(-1 - this.dialog.getWidth(), -1 - this.dialog.getHeight());
		// these two lines are copy-righted from bob's ProcessingDialog
		final Dimension screenSize =
			Toolkit.getDefaultToolkit().getScreenSize();
		this.dialog.setLocation((screenSize.width - this.dialog.getWidth())/2,
				(screenSize.height - this.dialog.getHeight())/2);
	}

	/**
	 * Метод может вызываться из {@link #construct()} для смены заголовка окна
	 * и текста, отображаемого в окне.
	 * 
	 * @param title новый заголовок либо null, чтобы оставить неизмененным
	 * @param text новый текст либо null, чтобы оставить неизмененным
	 */
	protected void setTitleAndText(final String title, final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (title != null) {
					SynchronousWorker.this.title = title.equals("") ? null : title;
				}
				if (text != null) {
					SynchronousWorker.this.text = text.equals("") ? null : text;
				}
				SynchronousWorker.this.updateDialog();
			}});
	}

	/**
	 * Если ProgressBar отображен, то выключает режим
	 * indeterminate и устанавливает ProgressBar в заданное положение.
	 * Если ProgressBar не отображен, ничего не делает.
	 * <p>
	 * Данная реализация игнорирует запросы, которые поступили
	 * до фактического отображения модального диалога.
	 * В будущем это, возможно, будет изменено.
	 * 
	 * @param percentage процент выполнения, число от 0 до 100.
	 */
	protected void setPercents(final int percentage) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SynchronousWorker.this.updatePercentage(percentage);
			}
		});
	}

	void updatePercentage(int percentage) {
		if (this.bar != null) {
			this.bar.setIndeterminate(false);
			this.bar.setValue(percentage);
		}
	}

	void updateDialog() {
		if (this.dialog == null) {
			return; // Диалоговое окно еще не настроено
		}

		// меняем заголовок
		this.dialog.setTitle(this.title);

		// меняем текст
		this.label.setText(this.text);
		dlg.pack();
	}
}
