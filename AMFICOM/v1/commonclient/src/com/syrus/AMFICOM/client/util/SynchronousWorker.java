/*-
 * $Id: SynchronousWorker.java,v 1.1 2006/06/22 14:41:33 saa Exp $
 * 
 * Copyright � 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.util;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


/**
 * ����� ������������ ������������ ��������� AWT
 * ����������� ���������� "���������� ��������", �������� ��������
 * ������������, �� �� �������� ��������� ������.
 * ������ ����������� ��� ���������� ������� ��������, �������
 * ������ ���� ��������� �� ����������� �������������� � �������������.
 * <p>
 * �������� ����� ������������ ������ �� ������ AWT-EventQueue.
 * ������ ������ ��������� ��������� �����, ����������
 * "���������� ��������" � ������ {@link #construct()}. ������
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
 * �� ��������� ��������� ���� � {@link JProgressBar}'��, � AWT EventQueue
 * ������������ ������. ������ ������������ ������������ ��������� ����� �
 * �����, ��� ���������� ��������, �� ������ ��� ���������.
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
 * @version $Revision: 1.1 $, $Date: 2006/06/22 14:41:33 $
 * @module
 */
public abstract class SynchronousWorker<T> {
	private static final long TIMEOUT1 = 100; // 100 ms wait

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

	private String title = null;
	private String text = null;
	private boolean showProgressBar = true;

	/**
	 * @param executor ������������ {@link Executor}, � ������� ��������
	 * ����� ����������� ������ ���� null, ����� ��������� ������
	 * � �� �������������� ������.
	 * @param title ��������� ��� ���������� ������� ��������
	 * @param text ��������� ��������� ��� ���������� ������� ��������
	 * @param showProgressBar true, ����� ���������� {@link JProgressBar}
	 */
	public SynchronousWorker(Executor executor, String title, String text, boolean showProgressBar) {
		this(executor);
		this.title = title;
		this.text = text;
		this.showProgressBar = showProgressBar;
	}

//	/**
//	 * �� ���������� ������� {@link Executor}. ��������� ������
//	 * � �� �������������� ������.
//	 * <p>
//	 * ���� ����������� �����, �.�. �� ������� ������� ����� ������
//	 * "����� ������� executor". ����� ����� ������ ���� ���������,
//	 * ��� executor �� �����.
//	 */
//	public SynchronousWorker() {
//		this(null);
//	}

	/**
	 * @param executor ������������ {@link Executor}, � ������� ��������
	 * ����� ����������� ������ ���� null, ����� ��������� ������
	 * � �� �������������� ������.
	 * ������������� �������� {@link Executor}'� ����� ���� ������������
	 * ��� �������������.
	 */
	public SynchronousWorker(Executor executor) {
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
	 * ��� ������ ��� ����������� ������������ (��. {@link SynchronousWorker}
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
	public T execute() throws ExecutionException {
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
					this.lock.wait(TIMEOUT1);
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
			dlg = new JDialog();
		}

		Container area = new JPanel();
		if (this.text != null) {
			final JLabel label = new JLabel(this.text);
			label.setVisible(true);
			area.add(label);
		}

		if (this.showProgressBar) {
			final JProgressBar bar = new JProgressBar();
			bar.setIndeterminate(true);
			area.add(bar);
		}

		if (this.title != null) {
			dlg.setTitle(this.title);
		}

		area.setVisible(true);
		dlg.add(area);
		dlg.setModal(true);
		dlg.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		// these two lineas are copy-righted from bob's ProcessingDialog
		final Dimension screenSize =
			Toolkit.getDefaultToolkit().getScreenSize();
		dlg.pack();
		dlg.setLocation((screenSize.width - dlg.getWidth())/2,
				(screenSize.height - dlg.getHeight())/2);
		dlg.setVisible(true); // ������ ������� ���������� �� �������� ������
		dlg.dispose();
		if (!this.done) {
			throw new InternalError();
		}
		return getResult();
	}

	// this.done should be true by the moment
	private T getResult() throws ExecutionException {
		if (this.throwable == null) {
			return this.result;
		}
		throw new ExecutionException(this.throwable);
	}

	/**
	 * ����-������������
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 8; i++) {
			final int iFinal = i;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				try {
					Object result = new SynchronousWorker<Object>(
							null, "�������� " + iFinal, "���������...", true) {
						@Override
						public Object construct() {
							try {
								// ��������� ������ ������� �������� �������,
								// � ��������� - ���.
								Thread.sleep(iFinal/2 % 2 == 0 ? 300 : 20);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
//						throw new Error();
							return "Done " + iFinal;
						}}.execute();
						System.out.println(result);
				} catch (ExecutionException e) {
					// @todo Auto-generated catch block
					e.printStackTrace();
				}
				}
			});
		}
		testPerformance();
	}

	private static void testPerformance() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Starting measurements...");
				final Executor immediateExecutor = new Executor() {
					public void execute(Runnable command) {
						command.run();
					}
				};

				final ExecutorService singleThreadExecutor =
					Executors.newSingleThreadExecutor();

				// ��������� ��� ����������, ��������� �������� UI
				// (��������� ��� ���������, �� ����� ������������ ����������)
				measurePerformance(immediateExecutor);

				// ������������ �����������
				measurePerformance(singleThreadExecutor);

				// �������� ������ ��� ������ ������
				measurePerformance(null);

				singleThreadExecutor.shutdown();
			}
		});
	}
	/**
	 * should be invoked from AWT EQ only
	 */
	static void measurePerformance(final Executor executor) {
		try {
			final int count = 1000;
			long t0 = System.nanoTime();
			for (int i = 0; i < count; i++) {
				final int iFinal = i;
				new SynchronousWorker<Object>(executor) {
					@Override
					public Object construct() {
						return "Done testPerformance #" + iFinal;
					}
				}.execute();
			}
			long t1 = System.nanoTime();

			System.out.println("Average RTT: "
					+ (t1 - t0) / count / 1000 + " us for executor "
					+ (executor == null ? null : executor.getClass().getName()));
		} catch (ExecutionException e) {
			// @todo Auto-generated catch block
			e.printStackTrace();
		}
	}
}
