/*-
 * $Id: SynchronousWorker.java,v 1.2 2006/06/29 11:08:17 saa Exp $
 * 
 * Copyright � 2006 Syrus Systems.
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
 * ����� ������������ ������������ ��������� AWT
 * ����������� ���������� "���������� ��������", �������� ��������
 * ������������, �� �� �������� ��������� ������.
 * <p>
 * ������ ����� ��������� {@link AbstractSynchronousWorker}, ���������
 * ��������� ����������������� ���������� �� ����� ����������
 * "����������� ��������".
 * <p>
 * �������� ���������, ������������� �� {@link AbstractSynchronousWorker}:
 * <ul>
 * <li> ������ �������� ����� ������������ ������ �� ������ AWT-EventQueue;
 * <li> ������ ������ ��������� ��������� �����, ����������
 *   "���������� ��������" � ������ {@link #construct()};
 * <li> ����� {@link #construct()} �� ������ �������� ���������� �
 *   ��� ������������ ("realized") �������� Swing;
 * <li> ����� {@link #execute()} ���������� ��������,
 *   ������� ������ {@link #construct()};
 * </ul>
 * <p>
 * �������������, ������ ������ ������������� �������������� �����������
 * �� ��������� ��������� ���� �� ����� ���������� ��������, ����������
 * indeterminate ������ � ����������� ���������.
 * � �������� ������������� ���� ��� ������� ������������
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
	private volatile JDialog dialog = null; // ������ �� ������������ ������
	private JPanel area = null;
	private JProgressBar bar = null;

	/**
	 * @param executor ������������ {@link Executor}, � ������� ��������
	 * ����� ����������� ������, ���� null, ����� ��������� ������
	 * � �� �������������� ������.
	 * @param title ��������� ��� ���������� ������� ��������, nullable
	 * @param text ��������� ��������� ��� ���������� ������� ��������, nullable
	 * @param showProgressBar true, ����� ���������� {@link JProgressBar}
	 */
	public SynchronousWorker(Executor executor, String title, String text, boolean showProgressBar) {
		this(executor);
		this.title = title;
		this.text = text;
		this.showProgressBar = showProgressBar;
	}

	/**
	 * @param executor ������������ {@link Executor}, � ������� ��������
	 * ����� ����������� ������ ���� null, ����� ��������� ������
	 * � �� �������������� ������.
	 * ������������� �������� {@link Executor}'� ����� ���� ������������
	 * ��� �������������.
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
		assert this.dialog != null; // ����� ������ ������� API
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
	 * ����� ����� ���������� �� {@link #construct()} ��� ����� ��������� ����
	 * � ������, ������������� � ����.
	 * 
	 * @param title ����� ��������� ���� null, ����� �������� ������������
	 * @param text ����� ����� ���� null, ����� �������� ������������
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
	 * ���� ProgressBar ���������, �� ��������� �����
	 * indeterminate � ������������� ProgressBar � �������� ���������.
	 * ���� ProgressBar �� ���������, ������ �� ������.
	 * <p>
	 * ������ ���������� ���������� �������, ������� ���������
	 * �� ������������ ����������� ���������� �������.
	 * � ������� ���, ��������, ����� ��������.
	 * 
	 * @param percentage ������� ����������, ����� �� 0 �� 100.
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
			return; // ���������� ���� ��� �� ���������
		}

		// ������ ���������
		this.dialog.setTitle(this.title);

		// ������ �����
		this.label.setText(this.text);
		dlg.pack();
	}
}
