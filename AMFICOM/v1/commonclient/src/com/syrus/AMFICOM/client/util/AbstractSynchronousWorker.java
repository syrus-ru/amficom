/*-
 * $Id: AbstractSynchronousWorker.java,v 1.1 2006/06/29 11:08:17 saa Exp $
 * 
 * Copyright � 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.util;

import java.awt.Frame;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;


/**
 * ����� ������������ ������������ ��������� AWT
 * ����������� ���������� "���������� ��������", �������� ��������
 * ������������, �� �� �������� ��������� ������.
 * ������ ����������� ��� ���������� ������� ��������, �������
 * ������ ���� ��������� �� ����������� �������������� � �������������.
 * <p>
 * �������� ����� ������������ ������ �� ������ AWT-EventQueue.
 * ������ ������ ��������� ��������� �����, ����������
 * "���������� ��������" � ������ {@link #construct()} � ���������
 * ���������� ����������� ���� � ������ {@link #prepareDialog(JDialog)}.
 * ������
 * ��������������� ������� {@link #execute()}, ������� �������� ����������
 * {@link #construct()} - ���� ����� �������� ����� {@link #Executor},
 * ���� � ��������� ������ � ���������� ���������� ��� ������.
 * <p>
 * ����� {@link #execute()} ������� ���������� {@link #construct()}
 * ��������� �������:
 * <ul>
 * <li> � ������� ���������� ������� �������� (~100 ��) UI ����������� ���������.
 * ��� ��������, ����� �� ���������� ������������ �������������� ������.
 * (� ���������, � �� ����, ��� ����������� ������������, �� ������ ���������
 * ���� � �� ������������ AWT EventQueue). (� ������ ������� - spurious wakeups
 * - ���� ���������� ������� ����� ��������������� �����������).
 * <li> ���� �� ��� ����� {@link #construct()} �� �����������,
 * �� ��������� ��������� ���� �� ���������� � ����������, �������������
 * ������� {@link #prepareDialog(JDialog)}, � AWT EventQueue
 * ������������ ������. ������ ������������ ������������ ��������� �����, ��
 * �����, ��� ���������� �������� � ����� ��������������.
 * </ul>
 * ����� �������, ������������ ������ �� ����� ������ �� ����������
 * ������ {@link #construct()}. (���� �����, � ��������� ������� ��������
 * ��������� ������ �������� ������� "Cancel").
 * <p>
 * ����� {@link #execute()} ���������� ��������,
 * ������� ������ {@link #construct()}.
 * <p>
 * ��� ���������� ������ {@link #construct()} ����� �� ��������, ���
 * � ��� ������ ���������� � ��� ������������ ("realized") �������� Swing,
 * �.�. ��, ��� �������, ����������� � ���� ������, ������ AWT EventQueue.
 * <p>
 * � ������� �� {@link #construct()}, {@link #prepareDialog(JDialog)}
 * ���������� ������������� � ������ AWT-EQ.
 * <p>
 * ��������� EventQueue ���������� ������ �� ����� ������ ���������� �������,
 * �� ���������, ��� �����-�� ������ ���������� ������� AWT ���� �������������
 * ���� ��������. � ����� ������ �������� ������ ��������� ����, ��� ���������.
 * ������� �������������� ����������� �����, � ����� �� ������������� ���
 * ������������� ��������� ����� {@link Executor}'�, �������� ����� ��������.
 * <p>
 * ������������� �������� {@link Executor}'� ����� ���� ������������
 * ��� ������������� ���������� �������� ����� Worker'� � ���-�� ���.
 * <p>
 * ��� �������� ������� ������, � ������ �������� ������ ������
 * (����� <code>null</code> {@link Executor})
 * ������� ����� ���������� execute() ��� ������ ������ construct() 
 * �� ������ P-IV 3GHz w/hyperThreading WinXP ���������� ����� 500 ���, �
 * �� Athlon64 2.5Ghz Debian Linux - 200 ���.
 * 
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2006/06/29 11:08:17 $
 * @module
 */
public abstract class AbstractSynchronousWorker<T> {
	private static final long DEFAULT_TIMEOUT1 = 100L; // 100 ms wait

	private static final Executor NEW_THREAD_EXECUTOR = new Executor() {
		public void execute(Runnable command) {
			new Thread(command).start();
		}
	};

	final Object lock = new Object();

	private final Executor executor;
	private Runnable task; // if null, the task is done and result calculated

	boolean done;
	JDialog dlg; // if not null, should be set unvisible when done
	T result;
	Throwable throwable;

	/**
	 * @param executor ������������ {@link Executor}, � ������� ��������
	 * ����� ����������� ������ ���� null, ����� ��������� ������
	 * � �� �������������� ������.
	 * ������������� �������� {@link Executor}'� ����� ���� ������������
	 * ��� �������������.
	 */
	public AbstractSynchronousWorker(Executor executor) {
		this.executor = executor == null ? NEW_THREAD_EXECUTOR : executor;
		this.done = false;
		this.task = new Runnable() {
			public void run() {
				final T value;
				try {
					value = construct();
				} catch (Throwable t) {
					// ���������� � �������
					setResults(null, t);
					return;
				}
				// ���������� ����������
				setResults(value, null);
			}
		};
	}

	void setResults(T result, Throwable t) {
		synchronized (this.lock) {
			// ����� ������� wait(lock) ������ ������, ����� lock �����������,
			// � setResult ������ �����������.

			if (this.done) {
				throw new IllegalStateException("Already complete");
			}
			this.result = result;
			this.throwable = t;
			this.done = true;

			this.lock.notify();

			// ��������� ���� �������� ��������� ������
			if (this.dlg != null) {
				// ��� ������� ��������� ���������� ����.
				// �������� ��� ��� ��� ��� - ��� �� ���� �����.
				// �� �� �����, ��� ��� ����� ���������� ��������,
				// � ��� AWT-EQ ������ ��������� ��� ����� ������.
				SwingUtilities.invokeLater(new Runnable(){
					public void run() {
						if (!dlg.isVisible()) {
							throw new InternalError();
						}
						// ��� ����� ���� ��������� ������ ����� ����, ���
						// ������ ����� ������� - � ������, ��� ������� ������.
						dlg.setVisible(false);
					}});
			}
		}

	}

	/**
	 * ��������� ���������� ������ {@link #construct()}, ���������� ���
	 * ���������� ����� ������ � ���������� ��������, ������� ���� �����
	 * ������.
	 * <p>
	 * ���� ����� ����� �������� ������ �� ����������� ������� AWT � ������
	 * ���� ��� �� ����� ����� <code>this</code>.
	 * <p>
	 * �� ����� �������� ���������� {@link #construct()} �����������
	 * ��� ������ ��� ����������� ������������ (��. {@link AbstractSynchronousWorker}
	 * � ���������
	 * {@link #SynchronousWorker(Executor, String, String, boolean)}).
	 * 
	 * @return ��������, ������� ������ ����� {@link #construct()}
	 * @throws IllegalStateException ����� ������ �� � ������ AWT Event Queue
	 * @throws IllegalStateException ��������� ����� ������
	 * @throws ExecutionException ���������� {@link #construct()}
	 *    ����������� �����������. � ������� ������ ������� ����� Throwable,
	 *    ��, ��������, � ������� ����� �������� ������ Exception.
	 *    �� ������ ������ ������������� ������ ��, ��� ������� ��, ���
	 *    ��������� ��� {@link #construct()}.
	 */
	public final T execute() throws ExecutionException {
		final long delay = getDelay();
		final Frame dlgParentFrame = getParentFrame();

//		long t0 = System.nanoTime();
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("Not event dispatching thread");
		}
//		long t1 = System.nanoTime();

		synchronized (this.lock) {
//			long t2 = System.nanoTime();
			// ��������� �������� � ������� ������
			if (this.done) {
				throw new IllegalStateException("Already executed");
			}
			this.executor.execute(this.task);
//			long t3 = System.nanoTime();

			// wait for a while (not prone to spurious wakeups)
			try {
				// �� ������ executor'�, ��� ������������ ��� ��������
				// ����� � ����� ������ - ����� ������
				if (!this.done) {
					this.lock.wait(delay);
				}
			} catch (InterruptedException e) {
				// ignore interrupt,
				// anyway we will not return until the task is complete.
			}
//			long t4 = System.nanoTime();

			/*
			 * �������� ��� ������ ��������:
			 * 1. ��������� ���������� setResult().
			 *   ����� thread == null � ��������� ����.
			 * 2. �� ��������� - synchronized(lock) � setResult() �� �������.
			 *   ����� setResult() ��� �������, �� �� �� �������� �� ������ 
			 *   ������ �� synchronized(lock).
			 */

			// task finished?
			if (this.done) {
//				long t5 = System.nanoTime();
//				System.out.println("deltas/us:"
//						+ " check " + (t1-t0)/1000
//						+ " lock " + (t2-t1)/1000
//						+ " start " + (t3-t2)/1000
//						+ " wait " + (t4-t3)/1000
//						+ " xxx " + (t5-t4)/1000
//						);
				// ����� 1.
				return getResult();
			}
			// ����� 2.
			// show GUI dialog
			// NB: ������ dlg ������ �� ������������ lock
			dlg = new JDialog(dlgParentFrame);
		}

		prepareDialog(dlg);
		dlg.setVisible(true); // ������ ������� ���������� �� �������� ������
		dlg.dispose();
		if (!this.done) {
			throw new InternalError();
		}
		return getResult();
	}

	/**
	 * ����� ���������� ������ ����, ������� ������ ���� ������������ ���
	 * ������������ ���������� ������� ���� null.
	 * ����� ������������ ��� ��������������� ������������.
	 * ����� ���������� �� �������� ����������� ���� � ����� ����������
	 * ���� � ��� ������, ���� ���������� ���� ��� � �� ����� �������.
	 * <p>
	 * ������ ���������� ���������� null.
	 * 
	 * @return ������ �� ������������ ���� ��� ������������
	 * ���������� ������� ���� null.
	 */
	protected Frame getParentFrame() {
		return null;
	}

	// this.done should be true by the moment
	private T getResult() throws ExecutionException {
		if (this.throwable == null) {
			return this.result;
		}
		throw new ExecutionException(this.throwable);
	}

	/**
	 * �������������� ���� �����, ���������� ����������� ����������
	 * ��������. �������, ��� �� ����� ������ �� ���������� ������,
	 * � ������ �� �� ������ �������� ����������������� � �������
	 * ������������ (realized) ����������� Swing.
	 * ��� ������������� �������� ������ � GUI ����� ������������
	 * ������������ �������� ��� {@link SwingUtilities#invokeLater(Runnable)}.
	 * 
	 * @return ������������ ��������, may be null. ��� ����� �������� �
	 * ���������� ����� � �������� �������� {@link #execute()}.
	 * @throws Exception ����� ������
	 */
	public abstract T construct() throws Exception;

	/**
	 * ������� ���������� ����.
	 * ���������� � ������ AWT-EQ � ��� ������,
	 * ����� ������� ������� � ������ �����������
	 * ����. ���� {@link #construct()} ��������� ������, �� ���� �����
	 * ����� ������ �� ���������.
	 * <p>
	 * ���������� �� ������ �������� setVisible() - ��� ������ ���
	 * {@link AbstractSynchronousWorker}.
	 * <p>
	 * ������� ��������, ��� ������������� ���������� ����,
	 * ������ �� ������� ���������� ����� ������, �� ������ ��������
	 * ���������� �� ������, � ������� ����������� construct().
	 * 
	 * @param dlg2 ������ ��� ��������� ���������� ����
	 */
	protected abstract void prepareDialog(JDialog dlg2);

	/**
	 * �������������� ���� �����, ����� ������ �������� � �������������
	 * ����� ������� ����������� ����.
	 * <p> ��� ���������� ���������� �������� �� ��������� ~100 ms.
	 * 
	 * @return �������� � ��, >= 0.
	 */
	protected long getDelay() {
		return DEFAULT_TIMEOUT1;
	}
}
