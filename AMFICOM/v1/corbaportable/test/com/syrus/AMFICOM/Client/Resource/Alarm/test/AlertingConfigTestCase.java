/*
 * $Id: AlertingConfigTestCase.java,v 1.1 2004/06/24 10:57:36 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Alarm.test;

import com.syrus.AMFICOM.corba.portable.common.*;
import com.syrus.AMFICOM.corba.portable.reflect.*;
import junit.framework.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/24 10:57:36 $
 * @author $Author: bass $
 */
public class AlertingConfigTestCase extends TestCase {
	public AlertingConfigTestCase(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(AlertingConfigTestCase.class);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(AlertingConfigTestCase.class);
//		junit.swingui.TestRunner.run(AlertingConfigTestCase.class);
//		junit.textui.TestRunner.run(AlertingConfigTestCase.class);
	}

	public void testAlarmTypeImpl() throws DatabaseAccessException {
		System.out.println();
		String ids[] = AlarmTypeImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			AlarmTypeImpl alarmType = AlarmTypeImpl.AlarmTypeImpl(ids[i]);
			System.out.print(alarmType.getId() + '\t');
			System.out.print(alarmType.getCodename() + '\t');
			System.out.print(alarmType.getName() + '\t');
			System.out.println(alarmType.getDescription());
		}
	}

	public void testAlertingMessageTextImpl() throws DatabaseAccessException {
		System.out.println();
		String ids[] = AlertingMessageTextImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			AlertingMessageTextImpl alertingMessageText = AlertingMessageTextImpl.AlertingMessageTextImpl(ids[i]);
			System.out.print(alertingMessageText.getId() + '\t');
			System.out.print("" + alertingMessageText.getMessageTypeId() + '\t');
			System.out.println(alertingMessageText.getText());
		}
	}

	public void testAlertingMessageUserLinkImpl() throws DatabaseAccessException {
		System.out.println();
		String ids[] = AlertingMessageUserLinkImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			AlertingMessageUserLinkImpl alertingMessageUserLink = AlertingMessageUserLinkImpl.AlertingMessageUserLinkImpl(ids[i]);
			System.out.print(alertingMessageUserLink.getId() + '\t');
			System.out.print("" + alertingMessageUserLink.getAlertingMessageId() + '\t');
			System.out.print("" + alertingMessageUserLink.getAlertingTypeId() + '\t');
			System.out.print("" + alertingMessageUserLink.getSourceId() + '\t');
			System.out.println(alertingMessageUserLink.getUserId());
		}
	}

	public void testAlertingTypeImpl() throws DatabaseAccessException {
		System.out.println();
		Identifier ids[] = AlertingTypeImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			AlertingTypeImpl alertingTypeImpl = AlertingTypeImpl.AlertingTypeImpl(ids[i]);
			System.out.print("" + alertingTypeImpl.getId() + '\t');
			System.out.print(alertingTypeImpl.getCodename() + '\t');
			System.out.println(alertingTypeImpl.getName());
		}
	}

	public void testEventSourceImpl() throws DatabaseAccessException {
		System.out.println();
		String ids[] = EventSourceImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			EventSourceImpl eventSource = EventSourceImpl.EventSourceImpl(ids[i]);
			System.out.print(eventSource.getId() + '\t');
			System.out.print(eventSource.getObjectSourceName() + '\t');
			System.out.print(eventSource.getDescription() + '\t');
			System.out.print("" + eventSource.getObjectSourceId() + '\t');
			System.out.println(eventSource.getTypeId());
		}
	}

	public void testEventSourceImplMatchingToEventSourceTypeImpl() throws DatabaseAccessException {
		System.out.println();
		String ids[] = EventSourceTypeImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			EventSourceTypeImpl eventSourceType = EventSourceTypeImpl.EventSourceTypeImpl(ids[i]);
			System.out.print(eventSourceType.getId() + '\t');
			System.out.print(eventSourceType.getCodename() + '\t');
			System.out.print(eventSourceType.getName() + '\t');
			System.out.println(eventSourceType.getSourceTableName());
			String _ids[] = EventSourceImpl.getMatchingIds(eventSourceType);
			for (int j = 0; j < _ids.length; j ++) {
				EventSourceImpl eventSource = EventSourceImpl.EventSourceImpl(_ids[j]);
				System.out.print('\t' + eventSource.getId() + '\t');
				System.out.print('\t' + eventSource.getObjectSourceName() + '\t');
				System.out.print('\t' + eventSource.getDescription() + '\t');
				System.out.print("\t" + eventSource.getObjectSourceId() + '\t');
				System.out.println("\t" + eventSource.getTypeId());
			}
		}
	}

	public void testEventSourceTypeImpl() throws DatabaseAccessException {
		System.out.println();
		String ids[] = EventSourceTypeImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			EventSourceTypeImpl eventSourceType = EventSourceTypeImpl.EventSourceTypeImpl(ids[i]);
			System.out.print(eventSourceType.getId() + '\t');
			System.out.print(eventSourceType.getCodename() + '\t');
			System.out.print(eventSourceType.getName() + '\t');
			System.out.println(eventSourceType.getSourceTableName());
		}
	}

	public void testEventTypeImpl() throws DatabaseAccessException {
		System.out.println();
		String ids[] = EventTypeImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			EventTypeImpl eventType = EventTypeImpl.EventTypeImpl(ids[i]);
			System.out.print(eventType.getId() + '\t');
			System.out.print(eventType.getCodename() + '\t');
			System.out.println(eventType.getName());
		}
	}

	public void testMessageTypeImpl() throws DatabaseAccessException {
		System.out.println();
		String ids[] = MessageTypeImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			MessageTypeImpl messageTypeImpl = MessageTypeImpl.MessageTypeImpl(ids[i]);
			System.out.print(messageTypeImpl.getId() + '\t');
			System.out.print(messageTypeImpl.getCodename() + '\t');
			System.out.println(messageTypeImpl.getName());
		}
	}

	public void testUserImpl() throws DatabaseAccessException {
		System.out.println();
		Identifier ids[] = UserImpl.getIds();
		for (int i = 0; i < ids.length; i++) {
			UserImpl user = UserImpl.UserImpl(ids[i]);
			System.out.print("" + user.getId() + '\t');
			System.out.print(user.getLogin() + '\t');
			System.out.print(user.getName() + '\t');
			System.out.print(user.getOperationalId() + '\t');
			System.out.print(user.getOpertatorId() + '\t');
			System.out.print(user.getOrganizationId() + '\t');
			System.out.print(user.getSubscriberId() + '\t');
			System.out.println(user.getType());
		}
	}
}
