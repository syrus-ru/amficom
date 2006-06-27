/*
 * $Id: IdentifierPool.java,v 1.40.2.2 2006/06/27 15:34:00 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

import gnu.trove.TShortHashSet;
import gnu.trove.TShortObjectHashMap;
import gnu.trove.TShortObjectIterator;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.util.Fifo;
import com.syrus.util.FifoSaver;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.40.2.2 $, $Date: 2006/06/27 15:34:00 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class IdentifierPool {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int MAX_CAPACITY = 100;
	private static final double MIN_FILL_FACTOR = 0.2;
	private static final long MAX_TIME_WAIT = 3 * 60 * 1000; /* Maximim time to wait while identifiers are loading*/

	private static IdentifierFactory identifierFactory;
	private static int capacity;

	private static ExecutorService executorService;
	private static CORBAActionProcessor corbaActionProcessor;
	
	/**
	 * ����� �������� ���������������. ���� - ��� ��������. �������� - �������
	 * ���������������.
	 */
	private static TShortObjectHashMap idPoolMap;

	/**
	 * ����� ����� ���������, ��� ������� ��� ������� ����� ���������.
	 */
	private static TShortHashSet fillingFifoCodes;

	private IdentifierPool() {
		// empty private construcor
	}


	public static void init(final IdentifierFactory identifierFactory1) {
		init(identifierFactory1, DEFAULT_CAPACITY, getDefaultCORBAActionProcessor());
	}

	/**
	 * @param identifierFactory1
	 * @param corbaActionProcessor1
	 *        ���� <code>null</code>, �� ������������
	 *        {@link #getDefaultCORBAActionProcessor() ���������� �� ���������}.
	 */
	public static void init(final IdentifierFactory identifierFactory1, final CORBAActionProcessor corbaActionProcessor1) {
		init(identifierFactory1, DEFAULT_CAPACITY,
				corbaActionProcessor1 == null
						? getDefaultCORBAActionProcessor()
						: corbaActionProcessor1);
	}

	public static void init(final IdentifierFactory identifierFactory1, final int capacity1) {
		init(identifierFactory1, capacity1, getDefaultCORBAActionProcessor());
	}

	public static void init(final IdentifierFactory identifierFactory1,
			final int capacity1,
			final CORBAActionProcessor corbaActionProcessor1) {
		identifierFactory = identifierFactory1;
		capacity = (capacity1 <= MAX_CAPACITY) ? capacity1 : MAX_CAPACITY;
		executorService = Executors.newCachedThreadPool();
		corbaActionProcessor = corbaActionProcessor1;

		idPoolMap = new TShortObjectHashMap();
		fillingFifoCodes = new TShortHashSet();
	}

	/**
	 * <p>
	 * ������� ����� ������������� ��� �������� <code>entityCode</code>.
	 * <p>
	 * ���� ����� ������� ������� ��������������� ��� �������
	 * <code>entityCode</code> �� ����� {@link #idPoolMap}. ���� �����
	 * ������� ���, �� �� ������� ţ � �������� � �����.
	 * <p>
	 * ����� ���������� �������� �� ������� "��������ۣ������". ���������, ���
	 * ������� "����������", ���� ���������� �������� � ��� ������, ��� �����
	 * ����� �� ������� ���� �������, ������������ ����������
	 * {@link #MIN_FILL_FACTOR}.
	 * <p>
	 * ���� ������� �������� �� ������� ���� ���������������, �� ����� �������
	 * ���� �� ��� �� ������� � ���������� ���. � ��������� ������ �� ������ ���
	 * �������� <code>entityCode</code> ����������� ��������� ����� ���������
	 * {@link IdentifierLoader} (���� �� �� ��� ��� �� ������� ��� �����
	 * <code>entityCode</code>). ����� �ģ�, ���� � ������� �������� ���� ��
	 * ���� ��������� ������, �� �� ������, ��� {@link #MAX_TIME_WAIT}. ����
	 * ������� ���������, �� ������������ ���������� �� ������� �������������, �
	 * ��������� ������ �������� {@link IdentifierGenerationException}.
	 * 
	 * @param entityCode
	 *        ��� ��������, ��� ������� ���������� �������� ����� �������������.
	 * @return �����, ������� � ������������� �������������.
	 * @throws IdentifierGenerationException
	 *         ���� ���������� �������� ����� �������������.
	 */
	public static Identifier getGeneratedIdentifier(final short entityCode) throws IdentifierGenerationException {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		Fifo<Identifier> fifo;

		synchronized (idPoolMap) {
			fifo = (Fifo<Identifier>) idPoolMap.get(entityCode);
			if (fifo == null) {
				fifo = new Fifo<Identifier>(capacity);
				idPoolMap.put(entityCode, fifo);
			}
		}

		synchronized (fifo) {
			if (fifo.size() > MIN_FILL_FACTOR * fifo.capacity()) {
				return fifo.remove();
			}
			final long deadtime = System.currentTimeMillis() + MAX_TIME_WAIT;
			final long start = System.currentTimeMillis();
			for(;;) {
				long currentTime = System.currentTimeMillis();
				long toWait = deadtime - currentTime;
				if (toWait <= 0) {
					throw new IdentifierGenerationException("Cannot load identifiers for entity '"
							+ ObjectEntities.codeToString(entityCode) + "'; wait "
							+ (currentTime - start) + " ms (" + MAX_TIME_WAIT + ")");
				}

				synchronized (fillingFifoCodes) {
					if (!fillingFifoCodes.contains(entityCode)) {
						executorService.submit(new IdentifierLoader(fifo, entityCode));
						fillingFifoCodes.add(entityCode);
					}
				}

				if (!fifo.isEmpty()) {
					return fifo.remove();
				}

				try {
					fifo.wait(toWait);
				} catch (InterruptedException ie) {
					Log.errorMessage(ie);
				}
			}
		}
	}

	/**
	 * ����� ���������. ������������ ��� ���������� ������� ���������������
	 * {@link Fifo}.
	 */
	private static class IdentifierLoader implements Runnable {
		final Fifo<Identifier> fifo;
		final short entityCode;

		IdentifierLoader(final Fifo<Identifier> fifo, final short entityCode) {
			this.fifo = fifo;
			this.entityCode = entityCode;
		}

		@SuppressWarnings("synthetic-access")
		public void run() {
			final CORBAAction corbaAction = new CORBAAction() {
				@SuppressWarnings("synthetic-access")
				public void perform() {
					int numberToLoad;
					synchronized (IdentifierLoader.this.fifo) {
						numberToLoad = IdentifierLoader.this.fifo.capacity() - IdentifierLoader.this.fifo.size();
					}
					while (numberToLoad > 0) {
						final Set<Identifier> ids;
						try {
							ids = identifierFactory.getGeneratedIdentifierRange(IdentifierLoader.this.entityCode, numberToLoad);
						} catch (CommunicationException ce) {
							Log.errorMessage(ce);
							try {
								Thread.sleep(500);
							} catch (InterruptedException ie) {
								Log.errorMessage(ie);
							}
							break;
						} catch (IdentifierGenerationException ige) {
							Log.errorMessage(ige);
							break;
						} catch (IllegalObjectEntityException ioee) {
							Log.errorMessage(ioee);
							break;
						}

						synchronized (IdentifierLoader.this.fifo) {
							for (final Identifier id : ids) {
								IdentifierLoader.this.fifo.push(id);
							}
							IdentifierLoader.this.fifo.notifyAll();
							numberToLoad = IdentifierLoader.this.fifo.capacity() - IdentifierLoader.this.fifo.size();
						}
					}

				}
			};

			try {
				corbaActionProcessor.performAction(corbaAction);
			} catch (final ApplicationException ae) {
				Log.errorMessage(ae);
			}

			synchronized (this.fifo) {
				synchronized (fillingFifoCodes) {
					fillingFifoCodes.remove(IdentifierLoader.this.entityCode);
					this.fifo.notifyAll();
				}
			}
		}
	}

	private static CORBAActionProcessor getDefaultCORBAActionProcessor() {
		return new CORBAActionProcessor() {
			public void performAction(final CORBAAction action) throws ApplicationException {
				try {
					action.perform();
				} catch (final IdentifierGenerationException e) {
					throw e; 
				} catch (final AMFICOMRemoteException e) {
					throw new IdentifierGenerationException("Cannot obtain reference on identifier generator server", e);
				}
			}
		};
	}

	protected static void serialize() {
		synchronized (idPoolMap) {
			for (final TShortObjectIterator iterator = idPoolMap.iterator(); iterator.hasNext();) {
				iterator.advance();
				final short entityCode = iterator.key();
				final String entityName = ObjectEntities.codeToString(entityCode);
				final Fifo<?> fifo = (Fifo) iterator.value();
				FifoSaver.save(fifo, entityName);
			}
		}
	}

	protected static void deserialize() {
		final Map<String, Fifo<?>> fifoMap = FifoSaver.load(capacity);
		for (final String entityName : fifoMap.keySet()) {
			final short entityCode = ObjectEntities.stringToCode(entityName);
			idPoolMap.put(entityCode, fifoMap.get(entityName));
		}
	}
}
