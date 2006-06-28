/*-
 * $Id: SynchronousWorker.java,v 1.1 2006/06/22 14:41:33 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
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
 * Класс обеспечивает обработчикам сообщений AWT
 * возможность выполнения "длительных действий", блокируя действия
 * пользователя, но не блокируя отрисовку экрана.
 * Сервис задумывался для выполнения сетевых операций, которые
 * должны быть завершены до продолжения взаимодействия с пользователем.
 * <p>
 * Сервисом можно пользоваться только из потока AWT-EventQueue.
 * Клиент должен расширить настоящий класс, реализовав
 * "длительное действие" в методе {@link #construct()}. Сервис
 * предоставляется методом {@link #execute()}, который вызывает выполнение
 * {@link #construct()} - либо через заданный извне {@link #Executor},
 * либо в отдельном потоке и дожидается завершения его работы.
 * <p>
 * Метод {@link #execute()} ожидает завершения {@link #construct()}
 * следующим образом:
 * <ul>
 * <li> В течение небольшого времени ожидания (~100 мс) UI блокируется полностью.
 * Это делается, чтобы не беспокоить пользователя выскакивающими окнами.
 * (К сожалению, я не знаю, как блокировать пользователя, не выводя модальное
 * окно и не останавливая AWT EventQueue). (В редких случаях - spurious wakeups
 * - этот промежуток времени может самопроизвольно сокращаться).
 * <li> Если за это время {@link #construct()} не завершается,
 * то создается модальное окно с {@link JProgressBar}'ом, и AWT EventQueue
 * возобновляет работу. Теперь пользователь заблокирован модальным окном и
 * видит, что приложение работает, но просит его подождать.
 * </ul>
 * Таким образом, пользователь ничего не может делать до завершения
 * метода {@link #construct()}. (Быть может, в следующих версиях появится
 * поддержка отмены операции кнопкой "Cancel").
 * <p>
 * Метод {@link #execute()} возвращает значение,
 * которое вернул {@link #construct()}.
 * <p>
 * При реализации метода {@link #construct()} прошу не забывать, что
 * в нем нельзя обращаться к уже отображенным ("realized") объектам Swing,
 * т.к. он, как правило, запускается в ином потоке, нежели AWT EventQueue.
 * <p>
 * Поскольку EventQueue продолжает работу во время показа модального диалога,
 * не исключено, что какой-то другой обработчик событий AWT тоже воспользуется
 * этим сервисом. В таком случае появится второе модальное окно, это нормально.
 * Вопросы взаимодействия выполняемых задач, а также их синхронизации при
 * использовании заданного извне {@link Executor}'а, решаются самим клиентом.
 * <p>
 * Использование внешнего {@link Executor}'а может быть использовано
 * для синхронизации исполнения действий этого Worker'а с чем-то еще.
 * <p>
 * Как показали пробные замеры, в режиме создания нового потока
 * (задан <code>null</code> {@link Executor})
 * среднее время выполнения execute() при пустом методе construct() 
 * на машине P-IV 3GHz w/hyperThreading WinXP составляет около 500 мкс, а
 * на Athlon64 2.5Ghz Debian Linux - 200 мкс.
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
	 * @param executor Используемый {@link Executor}, с помощью которого
	 * будет выполняться задача либо null, чтобы выполнять задачу
	 * в ее индивидуальном потоке.
	 * @param title заголовок для модального диалога ожидания
	 * @param text текстовое сообщение для модального диалога ожидания
	 * @param showProgressBar true, чтобы отображать {@link JProgressBar}
	 */
	public SynchronousWorker(Executor executor, String title, String text, boolean showProgressBar) {
		this(executor);
		this.title = title;
		this.text = text;
		this.showProgressBar = showProgressBar;
	}

//	/**
//	 * Не используем внешний {@link Executor}. Выполняем задачу
//	 * в ее индивидуальном потоке.
//	 * <p>
//	 * Этот конструктор убран, т.к. он вызывал слишком много ошибок
//	 * "забыл указать executor". Пусть лучше клиент явно указывает,
//	 * что executor не нужен.
//	 */
//	public SynchronousWorker() {
//		this(null);
//	}

	/**
	 * @param executor Используемый {@link Executor}, с помощью которого
	 * будет выполняться задача либо null, чтобы выполнять задачу
	 * в ее индивидуальном потоке.
	 * Использование внешнего {@link Executor}'а может быть использовано
	 * для синхронизации.
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
					// завершение с ошибкой
					setResults(null, t);
					return;
				}
				// нормальное завершение
				setResults(value, null);
			}
		};
	}

	/**
	 * Переопределите этот метод, реализовав необходимые длительные
	 * действия. Помните, что он будет вызван из отдельного потока,
	 * а потому он не должен напрямую взаимодействовать с данными
	 * отображенных (realized) компонентов Swing.
	 * При необходимости передать данные в GUI можно использовать
	 * возвращаемое значение или {@link SwingUtilities#invokeLater(Runnable)}.
	 * 
	 * @return Произвольное значение, may be null. Оно будет передано в
	 * вызывающий поток в качестве возврата {@link #execute()}.
	 * @throws Exception любые ошибки
	 */
	public abstract T construct() throws Exception;

	void setResults(T result, Throwable t) {
		synchronized (this.lock) {
			// здесь никаких wait(lock) делать нельзя, иначе lock освободится,
			// и setResult станет неатомарным.

			if (this.done) {
				throw new IllegalStateException("Already complete");
			}
			this.result = result;
			this.throwable = t;
			this.done = true;

			this.lock.notify();

			// проверяем фазу ожидания основного потока
			if (this.dlg != null) {
				// Уже создано модальное диалоговое окно.
				// Показано оно или еще нет - нам не дано знать.
				// Но мы знаем, что оно будет непременно показано,
				// и что AWT-EQ начнет двигаться уже после показа.
				SwingUtilities.invokeLater(new Runnable(){
					public void run() {
						if (!dlg.isVisible()) {
							throw new InternalError();
						}
						// это может быть выполнено только после того, как
						// диалог будет показан - а значит, оно спрячет диалог.
						dlg.setVisible(false);
					}});
			}
		}

	}

	/**
	 * Запускает выполнение метода {@link #construct()}, дожидается его
	 * завершения этого метода и возвращает значение, которое этот метод
	 * вернул.
	 * <p>
	 * Этот метод можно вызывать только из обработчика событий AWT и только
	 * один раз за время жизни <code>this</code>.
	 * <p>
	 * Во время ожидания завершения {@link #construct()} принимаются
	 * все усилия для развлечения пользователя (см. {@link SynchronousWorker}
	 * и параметры
	 * {@link #SynchronousWorker(Executor, String, String, boolean)}).
	 * 
	 * @return значение, которое вернул метод {@link #construct()}
	 * @throws IllegalStateException метод вызван не в потоке AWT Event Queue
	 * @throws IllegalStateException повторный вызов метода
	 * @throws ExecutionException выполнение {@link #construct()}
	 *    завершилось исключением. В текущей версии ловится любой Throwable,
	 *    но, возможно, в будущем будет ловиться только Exception.
	 *    На данный момент гарантировано только то, что ловится то, что
	 *    объявлено для {@link #construct()}.
	 */
	public T execute() throws ExecutionException {
//		long t0 = System.nanoTime();
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("Not event dispatching thread");
		}
//		long t1 = System.nanoTime();

		synchronized (this.lock) {
//			long t2 = System.nanoTime();
			// запускаем действие в рабочем потоке
			if (this.done) {
				throw new IllegalStateException("Already executed");
			}
			this.executor.execute(this.task);
//			long t3 = System.nanoTime();

			// wait for a while (not prone to spurious wakeups)
			try {
				// на случай executor'а, уже выполнившего все действия
				// прямо в нашем потоке - ждать нечего
				if (!this.done) {
					this.lock.wait(TIMEOUT1);
				}
			} catch (InterruptedException e) {
				// ignore interrupt,
				// anyway we will not return until the task is complete.
			}
//			long t4 = System.nanoTime();

			/*
			 * Возможны два исхода ожидания:
			 * 1. дождались завершения setResult().
			 *   Тогда thread == null и результат есть.
			 * 2. не дождались - synchronized(lock) в setResult() не начался.
			 *   Тогда setResult() еще впереди, но он не начнется до нашего 
			 *   выхода из synchronized(lock).
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
				// исход 1.
				return getResult();
			}
			// исход 2.
			// show GUI dialog
			// NB: объект dlg создан до освобождения lock
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
		dlg.setVisible(true); // отмена диалога произойдет из рабочего потока
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
	 * Тест-демонстрация
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 8; i++) {
			final int iFinal = i;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				try {
					Object result = new SynchronousWorker<Object>(
							null, "Ожидание " + iFinal, "Подождите...", true) {
						@Override
						public Object construct() {
							try {
								// некоторые задачи вызовут всплытие диалога,
								// а некоторые - нет.
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

				// выполняем все немедленно, полностью блокируя UI
				// (приведено для сравнения, не имеет практической значимости)
				measurePerformance(immediateExecutor);

				// однопоточный исполнитель
				measurePerformance(singleThreadExecutor);

				// создание каждый раз нового потока
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
