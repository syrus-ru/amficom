/*
 * $Id: ServerProcessHelper.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import com.syrus.AMFICOM.corba.portable.alarm.*;
import com.syrus.AMFICOM.corba.portable.common.*;
import com.syrus.AMFICOM.server.prefs.JDBCConnectionManager;
import com.syrus.AMFICOM.server.process.mail.*;
import com.syrus.AMFICOM.server.process.sms.SMSProviderFactory;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @author $Author: bass $
 * @module serverprocess
 */
final class ServerProcessHelper {
	private static Connection conn;
	private static PreparedStatement alertingStmt1;
	private static PreparedStatement alertingStmt2;
	private static PreparedStatement alertingMessageTextStmt1;
	private static PreparedStatement alertingMessageUserLinkStmt1;
	private static PreparedStatement alertingMessageUserLinkStmt2;
	private static PreparedStatement evaluationStmt1;
	private static PreparedStatement eventStmt1;
	private static PreparedStatement eventSourceStmt1;
	private static PreparedStatement eventSourceTypeStmt1;
	private static PreparedStatement iorStmt1;
	private static PreparedStatement loggedUserStmt1;
	private static PreparedStatement loggedUserStmt2;
	private static PreparedStatement loggedUserStmt3;
	private static PreparedStatement loggedUserStmt4;
	private static PreparedStatement monitoredElementStmt1;
	private static PreparedStatement operatorProfileStmt1;
	private static PreparedStatement operatorProfileStmt2;
	private static PreparedStatement resultStmt1;
	private static PreparedStatement transmissionPathStmt1;

	/**
	 * @todo Introduce init() method. 
	 */
	static {
		try {
			conn = JDBCConnectionManager.getConn();

			alertingStmt1 = conn.prepareStatement("SELECT id AS alertingId, alerting_message_user_id AS alertingMessageUserLinkId, event_id AS eventId FROM amficom.alertings WHERE alerted IS NULL", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			alertingStmt2 = conn.prepareStatement("UPDATE amficom.alertings SET alerted = SYSDATE WHERE id = ?");
			alertingMessageTextStmt1 = conn.prepareStatement("SELECT message_type_id AS messageTypeId, text FROM amficom.alertingmessagetexts WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			alertingMessageUserLinkStmt1 = conn.prepareStatement("SELECT alerting_message_id AS alertingMessageTextId, alerting_type_id AS alertingTypeId, source_id AS eventSourceId, user_id AS userId FROM amficom.alertingmessageuserlinks WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			alertingMessageUserLinkStmt2 = conn.prepareStatement("SELECT alerting_message_id AS alertingMessageTextId, alerting_type_id AS alertingTypeId, source_id AS eventSourceId FROM amficom.alertingmessageuserlinks WHERE id = ? AND user_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			evaluationStmt1 = conn.prepareStatement("SELECT monitored_element_id AS monitoredElementId FROM amficom.evaluations WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			eventStmt1 = conn.prepareStatement("SELECT descriptor AS resultId, source_id AS eventSourceId FROM amficom.events WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			eventSourceStmt1 = conn.prepareStatement("SELECT type_id AS eventSourceTypeId, object_source_id AS objectSourceId, object_source_name AS eventSourceName, description AS eventSourceDescription FROM amficom.eventsources WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			eventSourceTypeStmt1 = conn.prepareStatement("SELECT source_table_name AS sourceTableName FROM amficom.eventsourcetypes WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			iorStmt1 = conn.prepareStatement("SELECT ior FROM amficom.iors WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			loggedUserStmt1 = conn.prepareStatement("SELECT UNIQUE user_id AS userId FROM amficom.loggedusers WHERE alarm_receiver = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			loggedUserStmt2 = conn.prepareStatement("SELECT UNIQUE user_id AS userId FROM amficom.loggedusers", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			loggedUserStmt3 = conn.prepareStatement("SELECT id AS loggedUserId FROM amficom.loggedusers WHERE user_id = ? AND alarm_receiver = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			loggedUserStmt4 = conn.prepareStatement("SELECT id AS loggedUserId FROM amficom.loggedusers WHERE user_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			monitoredElementStmt1 = conn.prepareStatement("SELECT element_type AS monitoredElementType, path_id AS transmissionPathId FROM amficom.monitoredelements WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			resultStmt1 = conn.prepareStatement("SELECT elementary_start_time AS eventDate, evaluation_id AS evaluationId, result_type AS resultType FROM amficom.results WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			transmissionPathStmt1 = conn.prepareStatement("SELECT name AS transmissionPathName, description AS transmissionPathDescription, kis_id AS objectSourceId FROM amficom.transmissionpaths WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			operatorProfileStmt1 = conn.prepareStatement("SELECT name, e_mail AS eMail FROM amficom.operatorprofiles WHERE user_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			operatorProfileStmt2 = conn.prepareStatement("SELECT sms_number AS smsNumber FROM amficom.operatorprofiles WHERE user_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	private ServerProcessHelper() {
	}

	/**
	 * Returns an array of users logged in, configured for receiving messages.
	 *
	 * @return an array of users logged in.
	 * @throws SQLException
	 */
	static Identifier[] getAlarmReceivers() throws SQLException {
		ResultSet loggedUserResultSet = null;
		try {
			loggedUserStmt1.setInt(1, 1);
			loggedUserResultSet = loggedUserStmt1.executeQuery();
			List userIds = new LinkedList();
			while (loggedUserResultSet.next())
				userIds.add(new IdentifierImpl(loggedUserResultSet.getString("userId")));
			return (Identifier[]) userIds.toArray(new Identifier[userIds.size()]);
		} finally {
			try {
				loggedUserResultSet.close();
			} catch (NullPointerException npe) {
				;
			}
		}
	}

	/**
	 * Returns an array of users logged in.
	 * 
	 * @return an array of users logged in.
	 * @throws SQLException
	 */
	static Identifier[] getLoggedUsers() throws SQLException {
		ResultSet loggedUserResultSet = null;
		try {
			loggedUserResultSet = loggedUserStmt2.executeQuery();
			List userIds = new LinkedList();
			while (loggedUserResultSet.next())
				userIds.add(new IdentifierImpl(loggedUserResultSet.getString("userId")));
			return (Identifier[]) userIds.toArray(new Identifier[userIds.size()]);
		} finally {
			try {
				loggedUserResultSet.close();
			} catch (NullPointerException npe) {
				;
			}
		}
	}

	/**
	 * @throws SQLException
	 */
	static String[] getIORsForAlarmReceiver(Identifier userId) throws SQLException {
		ResultSet loggedUserResultSet = null;
		try {
			loggedUserStmt3.setString(1, userId.toString());
			loggedUserStmt3.setInt(2, 1);
			loggedUserResultSet = loggedUserStmt3.executeQuery();
			return getIORs(loggedUserResultSet);
		} finally {
			try {
				loggedUserResultSet.close();
			} catch (NullPointerException npe) {
				;
			}
		}
	}

	/**
	 * @exception SQLException
	 */
	static String[] getIORsForLoggedUser(Identifier userId) throws SQLException {
		ResultSet loggedUserResultSet = null;
		try {
			loggedUserStmt4.setString(1, userId.toString());
			loggedUserResultSet = loggedUserStmt4.executeQuery();
			return getIORs(loggedUserResultSet);
		} finally {
			try {
				loggedUserResultSet.close();
			} catch (NullPointerException npe) {
				;
			}
		}
	}

	/**
	 * @throws SQLException
	 */
	private static String[] getIORs(ResultSet loggedUserResultSet) throws SQLException {
		List iors = new LinkedList();
		Blob ior;
		while (loggedUserResultSet.next()) {
			ResultSet iorResultSet = null;
			try {
				iorStmt1.setString(1, loggedUserResultSet.getString("loggedUserId"));
				iorResultSet = iorStmt1.executeQuery();
				iorResultSet.absolute(1);
				ior = iorResultSet.getBlob("ior");
				long length = ior.length();
				/*
				 * This check is redundant as this way (by using getBlob()) we
				 * can obtain only the first 4000 bytes (and IOR length rarely
				 * exceeds 400 bytes).
				 */
				if (length > Integer.MAX_VALUE)
					length = Integer.MAX_VALUE;
				byte b[] = ior.getBytes(1L, (int) length);
				try {
					/*
					 * We do not perform any IOR validation -- one is
					 * automatically made when ORB.string_to_object() is
					 * invoked.
					 */
					iors.add(new String(b, "US-ASCII"));
				} catch (UnsupportedEncodingException uee) {
					iors.add(new String(b));
				}
			} finally {
				try {
					iorResultSet.close();
				} catch (NullPointerException npe) {
					;
				}
			}
		}
		return (String[]) (iors.toArray(new String[iors.size()]));
	}

	/**
	 * @param userId the user identifier. If <code>null</code>, messages for all
	 *        users are returned.
	 * @throws SQLException
	 * @todo Change messagetypes to match java logging constants.
	 * @todo Add filtering based on loglevel.
	 * @todo UI: Mail-Client!
	 */
	static AlertingMessageHolder[] loadMessages(Identifier userId) throws SQLException {
		LinkedList alertingMessageHolderList = new LinkedList();
		ResultSet alertingResultSet1 = null;
		try {
			alertingResultSet1 = alertingStmt1.executeQuery();
			while (alertingResultSet1.next()) {
				/*
				 * Information that will be returned. 
				 */
				String alertingTypeId;
				String alertingId;
				String messageTypeId;
				String eventId;
				Timestamp eventDate = null;
				String primaryEventSourceName;
				String secondaryEventSourceName = null;
				String eventSourceDescription;
				String transmissionPathName = null;
				String transmissionPathDescription = null;
				String text;

				/*
				 * Result sets that will be used.
				 */
				PreparedStatement stmt = null;
				ResultSet resultSet = null; 
				ResultSet alertingMessageTextResultSet1 = null;
				ResultSet alertingMessageUserLinkResultSet1 = null;
				ResultSet evaluationResultSet1 = null;
				ResultSet eventResultSet1 = null;
				ResultSet eventSourceResultSet1 = null;
				ResultSet eventSourceTypeResultSet1 = null;
				ResultSet monitoredElementResultSet1 = null;
				ResultSet resultResultSet1 = null;
				ResultSet transmissionPathResultSet1 = null;

				alertingId = alertingResultSet1.getString("alertingId");
				String alertingMessageUserLinkId = alertingResultSet1.getString("alertingMessageUserLinkId");
				eventId = alertingResultSet1.getString("eventId");
				try {
					if (userId == null) {
						alertingMessageUserLinkStmt1.setString(1, alertingMessageUserLinkId);
						alertingMessageUserLinkResultSet1 = alertingMessageUserLinkStmt1.executeQuery();
					} else {
						alertingMessageUserLinkStmt2.setString(1, alertingMessageUserLinkId);
						alertingMessageUserLinkStmt2.setString(2, userId.toString());
						alertingMessageUserLinkResultSet1 = alertingMessageUserLinkStmt2.executeQuery();
					}
					/*
					 * This ResultSet will contain 0 or 1 row.
					 */
					if (alertingMessageUserLinkResultSet1.next()) {
						String alertingMessageTextId = alertingMessageUserLinkResultSet1.getString("alertingMessageTextId");
						alertingTypeId = alertingMessageUserLinkResultSet1.getString("alertingTypeId");
						String secondaryEventSourceId = alertingMessageUserLinkResultSet1.getString("eventSourceId");
						if (userId == null)
							userId = new IdentifierImpl(alertingMessageUserLinkResultSet1.getString("userId"));
						alertingMessageTextStmt1.setString(1, alertingMessageTextId);
						alertingMessageTextResultSet1 = alertingMessageTextStmt1.executeQuery();
						/*
						 * alertingMessageTextId is always correct.
						 */
						alertingMessageTextResultSet1.absolute(1);

						messageTypeId = alertingMessageTextResultSet1.getString("messageTypeId");
						text = alertingMessageTextResultSet1.getString("text");
						eventStmt1.setString(1, eventId);
						eventResultSet1 = eventStmt1.executeQuery();
						/*
						 * eventId is always correct.
						 */
						eventResultSet1.absolute(1);
						String primaryEventSourceId = eventResultSet1.getString("eventSourceId");
						String resultId = eventResultSet1.getString("resultId");
						assert primaryEventSourceId.equals(secondaryEventSourceId): "primaryEventSourceId = \"" + primaryEventSourceId + "\"; secondaryEventSourceId = \"" + secondaryEventSourceId + "\"";
						eventSourceStmt1.setString(1, primaryEventSourceId);
						eventSourceResultSet1 = eventSourceStmt1.executeQuery();
						/*
						 * eventSourceId is always correct.
						 */
						eventSourceResultSet1.absolute(1);
						String eventSourceTypeId = eventSourceResultSet1.getString("eventSourceTypeId");
						String objectSourceId = eventSourceResultSet1.getString("objectSourceId");
						primaryEventSourceName = eventSourceResultSet1.getString("eventSourceName");
						eventSourceDescription = eventSourceResultSet1.getString("eventSourceDescription");
						if ((objectSourceId != null) && (objectSourceId.length() != 0)) {
							eventSourceTypeStmt1.setString(1, eventSourceTypeId);
							eventSourceTypeResultSet1 = eventSourceTypeStmt1.executeQuery();
							/*
							 * eventSourceTypeId is always correct.
							 */
							eventSourceTypeResultSet1.absolute(1);
							stmt = conn.prepareStatement("SELECT name AS eventSourceName FROM amficom." + eventSourceTypeResultSet1.getString("sourceTableName").toLowerCase() + " WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
							stmt.setString(1, objectSourceId);
							resultSet = stmt.executeQuery();
							/*
							 * As objectSourceId may be invalid (due to the absence of database
							 * constraints), this ResultSet will contain 0 or 1 row.
							 */
							if (resultSet.next())
								secondaryEventSourceName = resultSet.getString("eventSourceName");
						}
						if ((resultId != null) && (resultId.length() != 0)) {
							resultStmt1.setString(1, resultId);
							resultResultSet1 = resultStmt1.executeQuery();
							/*
							 * As resultId may be invalid (due to the absence of database
							 * constraints), this ResultSet will contain 0 or 1 row.
							 */
							if (resultResultSet1.next()) {
								eventDate = resultResultSet1.getTimestamp("eventDate");
								String evaluationId = resultResultSet1.getString("evaluationId");
								String resultType = resultResultSet1.getString("resultType");
								/*
								 * evaluationId is always correct if non-null.
								 */
								if ((resultType.equals("evaluation")) && (evaluationId != null)) {
									evaluationStmt1.setString(1, evaluationId);
									evaluationResultSet1 = evaluationStmt1.executeQuery();
									evaluationResultSet1.absolute(1);
									String monitoredElementId = evaluationResultSet1.getString("monitoredElementId");
									if ((monitoredElementId != null) && (monitoredElementId.length() != 0)) {
										monitoredElementStmt1.setString(1, monitoredElementId);
										monitoredElementResultSet1 = monitoredElementStmt1.executeQuery();
										/*
										 * As monitoredElementId may be invalid (due to the absence of database
										 * constraints), this ResultSet will contain 0 or 1 row.
										 */
										if (monitoredElementResultSet1.next()) {
											String transmissionPathId = monitoredElementResultSet1.getString("transmissionPathId");
											/*
											 * transmissionPathId is always correct if non-null.
											 */
											if ((monitoredElementResultSet1.getString("monitoredElementType").equals("path")) && (transmissionPathId != null)) {
												transmissionPathStmt1.setString(1, transmissionPathId);
												transmissionPathResultSet1 = transmissionPathStmt1.executeQuery();
												transmissionPathResultSet1.absolute(1);
												transmissionPathName = transmissionPathResultSet1.getString("transmissionPathName");
												transmissionPathDescription = transmissionPathResultSet1.getString("transmissionPathDescription");
												assert transmissionPathResultSet1.getString("objectSourceId").equals(objectSourceId): "Event source identifier fields are different in EVENTSOURCES (or EQUIPMENTS) and TRANSMISSIONPATHS";
											}
										}
									}
								}
							}
						}
					} else
						continue;
				} finally {
					try {
						try {
							stmt.close();
						} finally {
							try {
								resultSet.close();
							} finally {
								try {
									alertingMessageTextResultSet1.close();
								} finally {
									try {
										alertingMessageUserLinkResultSet1.close();
									} finally {
										try {
											evaluationResultSet1.close();
										} finally {
											try {
												eventResultSet1.close();
											} finally {
												try {
													eventSourceResultSet1.close();
												} finally {
													try {
														eventSourceTypeResultSet1.close();
													} finally {
														try {
															monitoredElementResultSet1.close();
														} finally {
															try {
																resultResultSet1.close();
															} finally {
																transmissionPathResultSet1.close();
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					} catch (NullPointerException npe) {
						;
					}
				}
				MessageImpl message = new MessageImpl();
				
				message.setAlertingId(new IdentifierImpl(alertingId));
				message.setMessageTypeId(new IdentifierImpl(messageTypeId));
				message.setEventId(new IdentifierImpl(eventId));
				message.setEventDate(eventDate);
				message.setEventSourceName(
					((primaryEventSourceName == null)
					? ((secondaryEventSourceName == null)
						? null
						: secondaryEventSourceName)
					: ((secondaryEventSourceName == null)
						? primaryEventSourceName
						: (primaryEventSourceName + "; " + secondaryEventSourceName))));
				message.setEventSourceDescription(eventSourceDescription);
				message.setTransmissionPathName(transmissionPathName);
				message.setTransmissionPathDescription(transmissionPathDescription);
				message.setText(text);

				alertingMessageHolderList.addLast(new AlertingMessageHolder(
					message,
					new IdentifierImpl(alertingTypeId),
					userId));
			}
		} finally {
			try {
				alertingResultSet1.close();
			} catch (NullPointerException npe) {
				;
			}
		}
		return (AlertingMessageHolder[]) (alertingMessageHolderList.toArray(
			new AlertingMessageHolder[alertingMessageHolderList.size()]));
	}

	/**
	 * Marks selected alerting as delivered.
	 * @param alertingId the corresponding primary key value in
	 *        AMFICOM.ALERTINGS.
	 * @throws SQLException
	 */
	static void setAlerted(Identifier alertingId) throws SQLException {
		setAlerted(alertingId, new PrintWriter(new NullWriter()));
	}

	/**
	 * Marks selected alerting as delivered.
	 * @param alertingId the corresponding primary key value in
	 *        AMFICOM.ALERTINGS.
	 * @throws SQLException
	 */
	static void setAlerted(Identifier alertingId, PrintWriter out) throws SQLException {
		boolean autoCommit = conn.getAutoCommit();
		conn.setAutoCommit(true);
		alertingStmt2.setString(1, alertingId.toString());
		out.println("ServerProcessHelper.setAlerted(): " + alertingStmt2.executeUpdate() + " row(s) affected...");
		conn.setAutoCommit(autoCommit);
	}

	/**
	 * @throws SQLException
	 * @throws MessageDeliveryFailedException
	 */
	static void sendEMailMessage(Message message, Identifier userId) throws SQLException, MessageDeliveryFailedException {
		sendEMailMessage(message, userId, new PrintWriter(new NullWriter()));
	}

	/**
	 * @throws SQLException
	 * @throws MessageDeliveryFailedException
	 */
	static void sendEMailMessage(Message message, Identifier userId, PrintWriter out) throws SQLException, MessageDeliveryFailedException {
		ResultSet operatorProfileResultSet = null;
		try {
			operatorProfileStmt1.setString(1, userId.toString());
			operatorProfileResultSet = operatorProfileStmt1.executeQuery();
			if (operatorProfileResultSet.next()) {
				String name = operatorProfileResultSet.getString("name");
				String eMail = operatorProfileResultSet.getString("eMail");
				try {
					if (name.length() == 0)
						name = null;
				} catch (NullPointerException npe) {
					;
				}
				try {
					MailIdentity to = new MailIdentity(name, eMail);
					String subject = "System Event Notification";
					String body = "Alerting Id: " + message.getAlertingId() + '\n'
						+ "Message Type Id: " + message.getMessageTypeId() + '\n'
						+ "Event Id: " + message.getEventId() + '\n'
						+ "Event Date: " + new Date(message.getEventDate()) + '\n'
						+ "Event Source Name: " + message.getEventSourceName() + '\n'
						+ "Event Source Description: " + message.getEventSourceDescription() + '\n'
						+ "Transmission Path Name: " + message.getTransmissionPathName() + '\n'
						+ "Transmission Path Description: " + message.getTransmissionPathDescription() + '\n'
						+ "Text: " + message.getText() + '\n';
					(new SimpleMailer()).sendMail(to, subject, body);
				} catch (IllegalArgumentException iae) {
					throw new MessageDeliveryFailedException("No valid e-mail address found.");
				} catch (IOException ioe) {
					throw new MessageDeliveryFailedException("Errors while sending an e-mail message");
				}
			} else
				throw new MessageDeliveryFailedException("No suitable operator profile found.");
		} finally {
			try {
				operatorProfileResultSet.close();
			} catch (NullPointerException npe) {
				;
			}
		}
	}

	/**
	 * @throws SQLException
	 * @throws MessageDeliveryFailedException
	 */
	static void sendSMSMessage(Message message, Identifier userId) throws SQLException, MessageDeliveryFailedException {
		sendSMSMessage(message, userId, new PrintWriter(new NullWriter()));
	}

	/**
	 * @throws SQLException
	 * @throws MessageDeliveryFailedException
	 */
	static void sendSMSMessage(Message message, Identifier userId, PrintWriter out) throws SQLException, MessageDeliveryFailedException {
		ResultSet operatorProfileResultSet = null;
		try {
			operatorProfileStmt2.setString(1, userId.toString());
			operatorProfileResultSet = operatorProfileStmt2.executeQuery();
			if (operatorProfileResultSet.next()) {
				String smsNumber = operatorProfileResultSet.getString("smsNumber");
				try {
					if (smsNumber.length() == 0)
						smsNumber = null;
				} catch (NullPointerException npe) {
					;
				}
				/**
				 * @todo Split the original message to 70- or 160-character
				 *       pieces based on content and send as multiple SMS
				 *       messages.
				 * @todo Handle Russian correctly: smsmail.ru has changed the
				 *       interface.
				 */
				/*
				 * SMS originator can be only 11 characters long.
				 */
				SMSProviderFactory.getDefaultSMSProvider().sendMessage("SystemEvent", smsNumber, message.getText());
			} else
				throw new MessageDeliveryFailedException("No suitable operator profile found.");
		} finally {
			try {
				operatorProfileResultSet.close();
			} catch (NullPointerException npe) {
				;
			}
		}
	}

	private static class NullWriter extends Writer {
		public void close() throws IOException {
		}

		public void flush() throws IOException {
		}

		public void write(char[] cbuf, int off, int len) throws IOException {
		}
	}
}
