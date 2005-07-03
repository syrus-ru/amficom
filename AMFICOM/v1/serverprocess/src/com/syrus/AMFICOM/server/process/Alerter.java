/*
 * $Id: Alerter.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import com.syrus.AMFICOM.corba.portable.alarm.Message;
import com.syrus.AMFICOM.corba.portable.client.*;
import com.syrus.AMFICOM.corba.portable.common.*;
import com.syrus.AMFICOM.corba.portable.reflect.AlertingType;
import com.syrus.AMFICOM.server.LogWriter;
import com.syrus.util.corba.JavaSoftORBUtil;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.omg.CORBA.SystemException;

/**
 * Alerter is a process responsible for event notification of clients.
 *
 * @bug When there's no clients listening for popup messages remained,
 *      reportPopupMessages() isn't invoked, and the last delivery thread
 *      remains unmapped (until at least one new valid client appears). WONTFIX.
 *
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @author $Author: bass $
 * @module serverprocess
 */
public final class Alerter extends Thread {
	/**
	 * The monitoring thread falls asleep for <code>TIME_INTERVAL</code>.
	 */
	private static final long TIME_INTERVAL = 4000L;

	private ProgressThread progressThread;

	private LogWriter logWriter;

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	
	/**
	 * The hashtable to store IOR-to-thread mappings.
	 */
	private Hashtable hashtable = new Hashtable();

	private final ThreadGroup popupMessageDeliveryThreadGroup = new ThreadGroup("PopupMessageDeliveryThreadGroup");

	private static final boolean DEBUG = false;

	/**
	 * The runtime state flag of this process.
	 * 
	 * @see #isRunning()
	 * @see #setRunning(boolean)
	 */
	private volatile boolean running = false;

	/**
	 * Constructs an <code>Alerter</code> with the specified
	 * <code>LogWriter</code>.
	 *
	 * @param logWriter the instance of <code>LogWriter</code> to log messages.
	 */
	public Alerter(LogWriter logWriter) {
		this.logWriter = logWriter;
	}

	/**
	 * Don't let anyone invoke this constructor directly.
	 */
	private Alerter() {
	}

	/**
	 * Stops the current process.
	 * 
	 * @see #run()
	 */
	public void stopRunning() {
		try {
			setRunning(false);
		} catch (IllegalStateException ise) {
			logWriter.log(ise.getLocalizedMessage());
			logWriter.log(SIMPLE_DATE_FORMAT.format(new Date(System.currentTimeMillis())) + ": Exiting...");
			return;
		}
		logWriter.log(SIMPLE_DATE_FORMAT.format(new Date(System.currentTimeMillis())) + ": Stopping Alerter...");
		try {
			progressThread.interrupt();
		} catch (NullPointerException npe) {
			/*
			 * progressThread not yet created.
			 */
			;
		}
	}

	/**
	 * @param running
	 * @throws IllegalStateException
	 */
	private synchronized void setRunning(boolean running) throws IllegalStateException {
		if (this.running && running)
			throw new IllegalStateException("Alerter already running.");
		else if (! (this.running || running))
			throw new IllegalStateException("Alerter already stopped.");
		this.running = running;
	}

	/**
	 * Getter for property <code>running</code>.
	 *
	 * @return the value of property <code>running</code>.
	 * @see #running
	 */
	private synchronized boolean isRunning() {
		return running;
	}

	/**
	 * The runnable body of the <code>Alerter</code> process.
	 */
	public void run() {
		try {
			setRunning(true);
		} catch (IllegalStateException ise) {
			logWriter.log(ise.getLocalizedMessage());
			logWriter.log(SIMPLE_DATE_FORMAT.format(new Date(System.currentTimeMillis())) + ": Exiting...");
			return;
		}
		logWriter.log(SIMPLE_DATE_FORMAT.format(new Date(System.currentTimeMillis())) + ": Starting Alerter...");

		Identifier alarmReceivers[];

		AlertingMessageHolder alertingMessageHolders[];

		while (isRunning()) {
			try {
				alarmReceivers = ServerProcessHelper.getLoggedUsers();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
				alarmReceivers = new Identifier[0];
			}
			for (int i = 0; i < alarmReceivers.length; i++) {
				Identifier userId = alarmReceivers[i];
				try {
					alertingMessageHolders = ServerProcessHelper.loadMessages(userId);
				} catch (SQLException sqle) {
					sqle.printStackTrace();
					alertingMessageHolders = new AlertingMessageHolder[0];
				}
				LinkedList eMailMessageList = new LinkedList();
				LinkedList faxMessageList = new LinkedList();
				LinkedList logMessageList = new LinkedList();
				LinkedList pagingMessageList = new LinkedList();
				LinkedList popupMessageList = new LinkedList();
				LinkedList smsMessageList = new LinkedList();
				LinkedList voiceMessageList = new LinkedList();
				for (int j = 0; j < alertingMessageHolders.length; j++) {
					AlertingMessageHolder alertingMessageHolder = alertingMessageHolders[j];
					String alertingTypeId = alertingMessageHolder.getAlertingTypeId().toString();
					Message message = alertingMessageHolder.getMessage();
					assert alertingMessageHolder.getUserId().equals(userId);
					if (alertingTypeId.equals(AlertingType.ID_EMAIL))
						eMailMessageList.addLast(message);
					else if (alertingTypeId.equals(AlertingType.ID_FAX))
						faxMessageList.addLast(message);
					else if (alertingTypeId.equals(AlertingType.ID_LOG))
						logMessageList.addLast(message);
					else if (alertingTypeId.equals(AlertingType.ID_PAGING))
						pagingMessageList.addLast(message);
					else if (alertingTypeId.equals(AlertingType.ID_POPUP))
						popupMessageList.addLast(message);
					else if (alertingTypeId.equals(AlertingType.ID_SMS))
						smsMessageList.addLast(message);
					else if (alertingTypeId.equals(AlertingType.ID_VOICE))
						voiceMessageList.addLast(message);
				}
				reportEMailMessages(userId, (Message[]) (eMailMessageList.toArray(new Message[eMailMessageList.size()])));
				reportFaxMessages(userId, (Message[]) (faxMessageList.toArray(new Message[faxMessageList.size()])));
				reportLogMessages(userId, (Message[]) (logMessageList.toArray(new Message[logMessageList.size()])));
				reportPagingMessages(userId, (Message[]) (pagingMessageList.toArray(new Message[pagingMessageList.size()])));
				reportPopupMessages(userId, (Message[]) (popupMessageList.toArray(new Message[popupMessageList.size()])));
				reportSMSMessages(userId, (Message[]) (smsMessageList.toArray(new Message[smsMessageList.size()])));
				reportVoiceMessages(userId, (Message[]) (voiceMessageList.toArray(new Message[voiceMessageList.size()])));
			}
			/**
			 * @todo Rewrite LogWriter to extend
			 *       java.io.PrintWriter and thus have methods
			 *       print() and println().
			 */
			if (DEBUG)
				logWriter.log("Falling asleep for " + TIME_INTERVAL + " millisecond(s)...");
//			progressThread = new ProgressThread(TIME_INTERVAL, logWriter);
			progressThread = new ProgressThread(TIME_INTERVAL);
			progressThread.start();
			try {
				progressThread.join();
			} catch (InterruptedException ie) {
				;
			}
			if (DEBUG)
				logWriter.log("Waking up...");
		}
		logWriter.log("Alerter finished...");
	}

	/**
	 * @todo Rewrite this method to allow it to run asynchronously.
	 * @param userId
	 * @param messageSeq
	 */
	private void reportEMailMessages(Identifier userId, Message messageSeq[]) {
		try {
			if (messageSeq.length == 0)
				return;
		} catch (NullPointerException npe) {
			return;
		}
		try {
			for (int i = 0; i < messageSeq.length; i++) {
				Message message = messageSeq[i];
				ServerProcessHelper.sendEMailMessage(message, userId);
				ServerProcessHelper.setAlerted(message.getAlertingId());
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (MessageDeliveryFailedException mdfe) {
			mdfe.printStackTrace();
		}
	}

	/**
	 * @param userId
	 * @param messageSeq
	 */
	private void reportFaxMessages(Identifier userId, Message messageSeq[]) {
		try {
			if (messageSeq.length == 0)
				return;
		} catch (NullPointerException npe) {
			return;
		}
		try {
			for (int i = 0; i < messageSeq.length; i++) {
				Message message = messageSeq[i];
				/**
				 * @todo Implement this method
				 */
				ServerProcessHelper.setAlerted(message.getAlertingId());
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * @param userId
	 * @param messageSeq
	 */
	private void reportLogMessages(Identifier userId, Message messageSeq[]) {
		try {
			if (messageSeq.length == 0)
				return;
		} catch (NullPointerException npe) {
			return;
		}
		try {
			for (int i = 0; i < messageSeq.length; i++) {
				Message message = messageSeq[i];
				/**
				 * @todo Implement this method
				 */
				ServerProcessHelper.setAlerted(message.getAlertingId());
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * @param userId
	 * @param messageSeq
	 */
	private void reportPagingMessages(Identifier userId, Message messageSeq[]) {
		try {
			if (messageSeq.length == 0)
				return;
		} catch (NullPointerException npe) {
			return;
		}
		try {
			for (int i = 0; i < messageSeq.length; i++) {
				Message message = messageSeq[i];
				/**
				 * @todo Implement this method
				 */
				ServerProcessHelper.setAlerted(message.getAlertingId());
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * Reports a sequence of messages to multiple clients run (or client CORBA
	 * objects created) by the user with the specified user id.
	 *
	 * @param userId
	 * @param messageSeq
	 */
	private void reportPopupMessages(Identifier userId, Message messageSeq[]) {
		try {
			if (messageSeq.length == 0)
				return;
		} catch (NullPointerException npe) {
			return;
		}
		String iors[];
		/*
		 * Fetch IORs.
		 */
		try {
			iors = ServerProcessHelper.getIORsForLoggedUser(userId);
		} catch (SQLException sqle) {
			iors = new String[0];
			logWriter.log("SQLException while fetching IORs for user: "  + userId + '.');
			sqle.printStackTrace();
		}
		/*
		 * Perform sanity check -- remove unused IORs (and unmap their
		 * corresponding threads).
		 */
		synchronized (hashtable) {
			Enumeration keys = hashtable.keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				boolean mustBeRemoved = true;
				for (int i = 0; i < iors.length; i++)
					if (key.equals(iors[i])) {
						mustBeRemoved = false;
						break;
					}
				if (mustBeRemoved)
					((PopupMessageDeliveryThread) (hashtable.get(key))).setMapped(false);
			}
		}
		/*
		 * Deliver popup messages.
		 */
		for (int i = 0; i < iors.length; i++) {
			try {
				((PopupMessageDeliveryThread) (hashtable.get(iors[i]))).enqueueMessageSeq(messageSeq);
			} catch (NullPointerException npe) {
				/*
				 * Thread is not yet created.
				 */
				try {
					(new PopupMessageDeliveryThread(iors[i])).enqueueMessageSeq(messageSeq);
				} catch (NullPointerException _npe) {
					logWriter.log("NullPointerException while creating a new thread. Probably IOR is null.");
					_npe.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param userId
	 * @param messageSeq
	 */
	private void reportSMSMessages(Identifier userId, Message messageSeq[]) {
		try {
			if (messageSeq.length == 0)
				return;
		} catch (NullPointerException npe) {
			return;
		}
		try {
			for (int i = 0; i < messageSeq.length; i++) {
				Message message = messageSeq[i];
				ServerProcessHelper.sendSMSMessage(message, userId);
				ServerProcessHelper.setAlerted(message.getAlertingId());
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (MessageDeliveryFailedException mdfe) {
			mdfe.printStackTrace();
		}
	}

	/**
	 * @param userId
	 * @param messageSeq
	 */
	private void reportVoiceMessages(Identifier userId, Message messageSeq[]) {
		try {
			if (messageSeq.length == 0)
				return;
		} catch (NullPointerException npe) {
			return;
		}
		try {
			for (int i = 0; i < messageSeq.length; i++) {
				Message message = messageSeq[i];
				/**
				 * @todo Implement this method
				 */
				ServerProcessHelper.setAlerted(message.getAlertingId());
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
	 * @author $Author: bass $
	 * @module serverprocess
	 */
	private static class MessageDeliveryThread extends Thread {
		private MessageDeliveryThread() {
		}

		/**
		 * @param group
		 * @param name
		 */
		private MessageDeliveryThread(ThreadGroup group, String name) {
			super(group, name);
		}

		/**
		 * @param millis
		 */
		public static void sleep(long millis) {
			try {
				/*
				 * Clear interrupted status of the thread.
				 */
				interrupted();
				Thread.sleep(millis);
			} catch (InterruptedException ie) {
				;
			}
		}
	}
	
	/**
	 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
	 * @author $Author: bass $
	 * @module serverprocess
	 */
	private class PopupMessageDeliveryThread extends MessageDeliveryThread {
		/**
		 * Object reference this thread is associated with.
		 */
		private String ior;

		/**
		 * Cache to store incoming popup messages.
		 */
		private LinkedList inbox = new LinkedList();

		/**
		 * Cache to store sent popup messages.
		 */
		private LinkedList outbox = new LinkedList();

		/**
		 * Maximum size of sent messages cache.
		 */
		private static final int MAX_OUTBOX_SIZE = 10;

		/**
		 * A flag that signals whether or not interrupt() can be inviked on this
		 * thread.
		 */
		private volatile boolean canBeInterrupted = false;

		/**
		 * This constructor should never be used.
		 *
		 * @throws UnsupportedOperationException whenever invoked.
		 */
		private PopupMessageDeliveryThread() {
			throw new UnsupportedOperationException();
		}

		/**
		 * @param ior an object reference this thread is mapped to. If
		 *        <code>null</code>, a <code>NullPointerException</code> is
		 *        thrown.
		 */
		PopupMessageDeliveryThread(String ior) {
			super(popupMessageDeliveryThreadGroup, ior);
			this.ior = ior;
			setMapped(true);
			setPriority(MIN_PRIORITY);
		}

		public void run() {
			if (DEBUG)
				logWriter.log("Thread count @ thread entry: " + getThreadGroup().activeCount());
			while (true) {
				try {
					/*
					 * Aurora's Orb.init() seems to get blocked on either
					 * wait(), join() or sleep(). As a result, when we invoke
					 * interrupt() on a running thread, we receive either a
					 * CORBA.SystemException or an InterruptedException.
					 */
					Client client = ClientHelper.narrow(JavaSoftORBUtil.getInstance().getORB().string_to_object(ior));
					while (true) {
						Message message;
						synchronized (inbox) {
							message = (Message) (inbox.removeFirst());
						}
						synchronized (outbox) {
							if (outbox.contains(message))
								/*
								 * Message already delivered.
								 */
								continue;
						}
						try {
							client.reportPopupMessages(new Message[]{message});
							synchronized (outbox) {
								/*
								 * Since delivery succeded, move message to outbox
								 * and trim outbox if necessary.
								 */
								outbox.addLast(message);
								trimOutbox(MAX_OUTBOX_SIZE);
							}
							/*
							 * Mark messages delivered.
							 */
							try {
								ServerProcessHelper.setAlerted(message.getAlertingId());
							} catch (SQLException sqle) {
								logWriter.log(Thread.currentThread().getName() +  ":\n\tSQLException while marking messages delivered.");
							} catch (Exception e) {
								logWriter.log(Thread.currentThread().getName() + ":\n\t" + e.getLocalizedMessage());
							}
						} catch (SystemException se) {
							logWriter.log(Thread.currentThread().getName() + ":\n\tSystemException while delivering a popup message. Either object not present (while still not deleted by UserTracer) or CORBA transport problem.");
						} catch (MessageDeliveryFailedException mdfe) {
							logWriter.log(Thread.currentThread().getName() + ":\n\tMessageDeliveryFailedException while delivering a popup message; message: \"" + mdfe.getLocalizedMessage() + "\".");
						} catch (Exception e) {
							logWriter.log(Thread.currentThread().getName() + ":\n\t" + e.getLocalizedMessage());
						}
					}
				} catch (SystemException se) {
					logWriter.log("SystemException while resolving IOR to CORBA object.");
					canBeInterrupted = true;
					sleep(TIME_INTERVAL);
					canBeInterrupted = false;
				} catch (NoSuchElementException nsee) {
					/*
					 * If inbox is empty and unmap request has arrived, then
					 * cease execution...
					 */
					if (! isMapped()) {
						if (DEBUG)
							logWriter.log("Cleaning up outbox...");
						trimOutbox(0);
						if (DEBUG)
							logWriter.log("Thread count @ thread exit: " + getThreadGroup().activeCount());
						return;
					}
					/*
					 * ... else sleep until interrupted.
					 */
					canBeInterrupted = true;
					sleep(Long.MAX_VALUE);
					canBeInterrupted = false;
				}
			}
		}

		/**
		 * Getter for property <code>mapped</code>. This method is thread-safe,
		 * though not declared as synchronized.
		 *
		 * @return <code>true</code> if thread pool contains this thread,
		 *         <code>false</code> otherwise.
		 */
		private boolean isMapped() {
			return hashtable.containsKey(ior);
		}

		/**
		 * Adds this thread to or removes it from thread pool. Once the thread
		 * is added to the pool, it is started.
		 *
		 * @param mapped the new value of mapped flag of this thread.
		 * @throws IllegalStateException if invoked twice in succession with the same argument.
		 */
		private void setMapped(boolean mapped) {
			synchronized (hashtable) {
				if (mapped && (! isMapped())) {
					if (DEBUG)
						logWriter.log("Mapping and starting: " + toString());
					hashtable.put(ior, this);
					if (isAlive())
						throw new IllegalStateException("Thread has been already started before it is mapped.");
					else
						start();
				} else if ((! mapped) && isMapped()) {
					if (DEBUG)
						logWriter.log("Unmapping and stopping: " + toString());
					hashtable.remove(ior);
					interrupt();
				} else
					throw new IllegalStateException("Attempt to invoke setMapped(boolean) twice in succession with the same argument.");
			}
		}

		/**
		 * Sequentiallly invokes
		 * {@link #enqueueMessage(Message, boolean)
		 * enqueueMessage(Message, boolean)} for each array
		 * element.
		 *
		 * @param messageSeq
		 * @see #enqueueMessage(Message, boolean)
		 */
		private void enqueueMessageSeq(Message messageSeq[]) {
			for (int i = 0; i < (messageSeq.length - 1); i++)
				enqueueMessage(messageSeq[i], false);
			try {
				enqueueMessage(messageSeq[messageSeq.length - 1], true);
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				/*
				 * Thrown when a zero-length array is passed as argument.
				 */
				;
			}
		}

		/**
		 * Puts a message into inbox (i. e. schedules it for delivery). When a
		 * new message is put into inbox, no check is done whether another copy
		 * of the message is already present there (currently, &quot;copy&quot;
		 * means that not only <code>id</code> fields are equal, but so are
		 * <code>event_id</code> and <code>text</code> fields). There're two
		 * reasons for this:
		 * <ul>
		 *     <li>when delivery thread pops a message from inbox in order to
		 *     deliver it, it tries to deliver the message <i>only unless</i>
		 *     another message copy is already present in outbox (i. e. unless
		 *     this message has been already successfully delivered);</li>
		 *
		 *     <li>unlike outbox (which has fixed size), inbox may grow
		 *     <i>very</i> large, thus walking through the entire inbox may be
		 *     a <i>lengthy</i> operation.</li>
		 * </ul>
		 *
		 * @param message
		 * @param lastMessage signals that this message is the last (or only)
		 *        one in sequence added to inbox, and thread must be
		 *        interrupted.
		 * @see #inbox
		 * @see #outbox
		 * @see #MAX_OUTBOX_SIZE
		 */
		private void enqueueMessage(Message message, boolean lastMessage) {
			/*
			 * Рекурсия -- мать рекурсии ;-)
			 */
//			enqueueMessageSeq(new Message[]{message});
			synchronized (inbox) {
				inbox.add(message);
			}
			if (lastMessage)
				interrupt();
		}

		/**
		 * Trims <code>outbox</code> to <code>maximumAllowedSize</code>.
		 * Thread-safe.
		 *
		 * @param maximumAllowedSize the maximum allowed size of
		 *        <code>outbox</code>. If outbox size is less than this value,
		 *        no action is taken.
		 */
		void trimOutbox(int maximumAllowedSize) {
			synchronized (outbox) {
				try {
					while (true) {
						if (outbox.size() > maximumAllowedSize)
							outbox.removeFirst();
						else
							break;
					}
				} catch (NoSuchElementException nsee) {
					;
				}
			}
		}

		public void interrupt() {
			if (canBeInterrupted)
				super.interrupt();
		}
	}
}
