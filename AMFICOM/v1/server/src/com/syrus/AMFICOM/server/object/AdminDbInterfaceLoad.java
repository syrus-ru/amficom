/*
 * $Id: AdminDbInterfaceLoad.java,v 1.1.2.1 2004/10/18 15:31:41 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.object;

import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.server.*;
import java.sql.*;
import java.util.*;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2004/10/18 15:31:41 $
 * @author $Author: bass $
 * @module server_v1
 * @todo Rewrite per-id loaders to take advantage of prepared statements.
 */
final class AdminDbInterfaceLoad implements SqlConstants {
	private AdminDbInterfaceLoad() {
	}

	static void loadServers(final Connection conn, final ServerSeq_TransferableHolder serverSeq, final String serverIds[]) throws SQLException {
		Collection servers = new LinkedList();
		for (int i = 0; i < serverIds.length; i++)
			servers.add(loadServer(conn, serverIds[i]));
		serverSeq.value = (Server_Transferable[]) servers.toArray(new Server_Transferable[servers.size()]);
	}

	private static Server_Transferable loadServer(final Connection conn, final String id) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			StringBuffer sql = new StringBuffer(KEYWORD_SELECT);
			sql.append(COLUMN_NAME);
			sql.append(COMMA);
			sql.append(COLUMN_CONTACT);
			sql.append(COMMA);
			sql.append(COLUMN_LICENSE_ID);
			sql.append(COMMA);
			sql.append(COLUMN_VERSION);
			sql.append(COMMA);
			sql.append(COLUMN_LOCATION);
			sql.append(COMMA);
			sql.append(COLUMN_HOSTNAME);
			sql.append(COMMA);
			sql.append(COLUMN_CREATED);
			sql.append(COMMA);
			sql.append(COLUMN_MODIFIED);
			sql.append(KEYWORD_FROM);
			sql.append(TABLE_SERVERS);
			sql.append(KEYWORD_WHERE);
			sql.append(COLUMN_ID);
			sql.append(EQUALS);
			sql.append(APOSTROPHE);
			sql.append(id);
			sql.append(APOSTROPHE);
			if (DEBUG)
				System.out.println(sql);

			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql.toString());
			resultSet.next();
			
			return new Server_Transferable(id,
				SqlUtilities.getNonNullString(resultSet, COLUMN_NAME),
				SqlUtilities.getNonNullString(resultSet, COLUMN_VERSION),
				SqlUtilities.getNonNullString(resultSet, COLUMN_LICENSE_ID),
				SqlUtilities.getNonNullString(resultSet, COLUMN_LOCATION),
				SqlUtilities.getNonNullString(resultSet, COLUMN_CONTACT),
				SqlUtilities.getNonNullString(resultSet, COLUMN_HOSTNAME),
				SqlUtilities.getNonNullDateAsMillis(resultSet, COLUMN_CREATED),
				SqlUtilities.getNonNullDateAsMillis(resultSet, COLUMN_MODIFIED),
				0);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} finally {
				if (stmt != null)
					stmt.close();
			}
		}
	}

	static void loadServers(final Connection conn, final ServerSeq_TransferableHolder serverSeq) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			StringBuffer sql = new StringBuffer(KEYWORD_SELECT);
			sql.append(COLUMN_ID);
			sql.append(COMMA);
			sql.append(COLUMN_NAME);
			sql.append(COMMA);
			sql.append(COLUMN_CONTACT);
			sql.append(COMMA);
			sql.append(COLUMN_LICENSE_ID);
			sql.append(COMMA);
			sql.append(COLUMN_VERSION);
			sql.append(COMMA);
			sql.append(COLUMN_LOCATION);
			sql.append(COMMA);
			sql.append(COLUMN_HOSTNAME);
			sql.append(COMMA);
			sql.append(COLUMN_CREATED);
			sql.append(COMMA);
			sql.append(COLUMN_MODIFIED);
			sql.append(KEYWORD_FROM);
			sql.append(TABLE_SERVERS);
			if (DEBUG)
				System.out.println(sql);
				
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql.toString());

			Collection servers = new LinkedList();
			while (resultSet.next())
				servers.add(new Server_Transferable(resultSet.getString(COLUMN_ID),
					SqlUtilities.getNonNullString(resultSet, COLUMN_NAME),
					SqlUtilities.getNonNullString(resultSet, COLUMN_VERSION),
					SqlUtilities.getNonNullString(resultSet, COLUMN_LICENSE_ID),
					SqlUtilities.getNonNullString(resultSet, COLUMN_LOCATION),
					SqlUtilities.getNonNullString(resultSet, COLUMN_CONTACT),
					SqlUtilities.getNonNullString(resultSet, COLUMN_HOSTNAME),
					SqlUtilities.getNonNullDateAsMillis(resultSet, COLUMN_CREATED),
					SqlUtilities.getNonNullDateAsMillis(resultSet, COLUMN_MODIFIED),
					0));
			serverSeq.value = (Server_Transferable[]) servers.toArray(new Server_Transferable[servers.size()]);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} finally {
				if (stmt != null)
					stmt.close();
			}
		}
	}

	static void loadClients(final Connection conn, final ClientSeq_TransferableHolder clientSeq, final String clientIds[]) throws SQLException {
		Collection clients = new LinkedList();
		for (int i = 0; i < clientIds.length; i++)
			clients.add(loadClient(conn, clientIds[i]));
		clientSeq.value = (Client_Transferable[]) clients.toArray(new Client_Transferable[clients.size()]);
	}

	private static Client_Transferable loadClient(final Connection conn, final String id) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			StringBuffer sql = new StringBuffer(KEYWORD_SELECT);
			sql.append(COLUMN_NAME);
			sql.append(COMMA);
			sql.append(COLUMN_CONTACT);
			sql.append(COMMA);
			sql.append(COLUMN_LICENSE_ID);
			sql.append(COMMA);
			sql.append(COLUMN_VERSION);
			sql.append(COMMA);
			sql.append(COLUMN_LOCATION);
			sql.append(COMMA);
			sql.append(COLUMN_HOSTNAME);
			sql.append(COMMA);
			sql.append(COLUMN_CREATED);
			sql.append(COMMA);
			sql.append(COLUMN_MODIFIED);
			sql.append(KEYWORD_FROM);
			sql.append(TABLE_CLIENTS);
			sql.append(KEYWORD_WHERE);
			sql.append(COLUMN_ID);
			sql.append(EQUALS);
			sql.append(APOSTROPHE);
			sql.append(id);
			sql.append(APOSTROPHE);
			if (DEBUG)
				System.out.println(sql);

			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql.toString());
			resultSet.next();

			return new Client_Transferable(id,
				SqlUtilities.getNonNullString(resultSet, COLUMN_NAME),
				SqlUtilities.getNonNullString(resultSet, COLUMN_VERSION),
				SqlUtilities.getNonNullString(resultSet, COLUMN_LICENSE_ID),
				SqlUtilities.getNonNullString(resultSet, COLUMN_LOCATION),
				SqlUtilities.getNonNullString(resultSet, COLUMN_CONTACT),
				SqlUtilities.getNonNullString(resultSet, COLUMN_HOSTNAME),
				SqlUtilities.getNonNullDateAsMillis(resultSet, COLUMN_CREATED),
				SqlUtilities.getNonNullDateAsMillis(resultSet, COLUMN_MODIFIED));
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} finally {
				if (stmt != null)
					stmt.close();
			}
		}
	}

	static void loadClients(final Connection conn, final ClientSeq_TransferableHolder clientSeq) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			StringBuffer sql = new StringBuffer(KEYWORD_SELECT);
			sql.append(COLUMN_ID);
			sql.append(COMMA);
			sql.append(COLUMN_NAME);
			sql.append(COMMA);
			sql.append(COLUMN_CONTACT);
			sql.append(COMMA);
			sql.append(COLUMN_LICENSE_ID);
			sql.append(COMMA);
			sql.append(COLUMN_VERSION);
			sql.append(COMMA);
			sql.append(COLUMN_LOCATION);
			sql.append(COMMA);
			sql.append(COLUMN_HOSTNAME);
			sql.append(COMMA);
			sql.append(COLUMN_CREATED);
			sql.append(COMMA);
			sql.append(COLUMN_MODIFIED);
			sql.append(KEYWORD_FROM);
			sql.append(TABLE_CLIENTS);
			if (DEBUG)
				System.out.println(sql);
				
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql.toString());

			Collection clients = new LinkedList();
			while (resultSet.next())
				clients.add(new Client_Transferable(resultSet.getString(COLUMN_ID),
					SqlUtilities.getNonNullString(resultSet, COLUMN_NAME),
					SqlUtilities.getNonNullString(resultSet, COLUMN_VERSION),
					SqlUtilities.getNonNullString(resultSet, COLUMN_LICENSE_ID),
					SqlUtilities.getNonNullString(resultSet, COLUMN_LOCATION),
					SqlUtilities.getNonNullString(resultSet, COLUMN_CONTACT),
					SqlUtilities.getNonNullString(resultSet, COLUMN_HOSTNAME),
					SqlUtilities.getNonNullDateAsMillis(resultSet, COLUMN_CREATED),
					SqlUtilities.getNonNullDateAsMillis(resultSet, COLUMN_MODIFIED)));
			clientSeq.value = (Client_Transferable[]) clients.toArray(new Client_Transferable[clients.size()]);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} finally {
				if (stmt != null)
					stmt.close();
			}
		}
	}

	static void loadAgents(final Connection conn, final AgentSeq_TransferableHolder agentSeq, final String agentIds[]) throws SQLException {
		Collection agents = new LinkedList();
		for (int i = 0; i < agentIds.length; i++)
			agents.add(loadAgent(conn, agentIds[i]));
		agentSeq.value = (Agent_Transferable[]) agents.toArray(new Agent_Transferable[agents.size()]);
	}

	private static Agent_Transferable loadAgent(final Connection conn, final String id) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			StringBuffer sql = new StringBuffer(KEYWORD_SELECT);
			sql.append(COLUMN_NAME);
			sql.append(COMMA);
			sql.append(COLUMN_CONTACT);
			sql.append(COMMA);
			sql.append(COLUMN_LICENSE_ID);
			sql.append(COMMA);
			sql.append(COLUMN_VERSION);
			sql.append(COMMA);
			sql.append(COLUMN_LOCATION);
			sql.append(COMMA);
			sql.append(COLUMN_HOSTNAME);
			sql.append(COMMA);
			sql.append(COLUMN_CREATED);
			sql.append(COMMA);
			sql.append(COLUMN_MODIFIED);
			sql.append(KEYWORD_FROM);
			sql.append(TABLE_AGENTS);
			sql.append(KEYWORD_WHERE);
			sql.append(COLUMN_ID);
			sql.append(EQUALS);
			sql.append(APOSTROPHE);
			sql.append(id);
			sql.append(APOSTROPHE);
			if (DEBUG)
				System.out.println(sql);

			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql.toString());
			resultSet.next();

			return new Agent_Transferable(id,
				SqlUtilities.getNonNullString(resultSet, COLUMN_NAME),
				SqlUtilities.getNonNullString(resultSet, COLUMN_VERSION),
				SqlUtilities.getNonNullString(resultSet, COLUMN_LICENSE_ID),
				SqlUtilities.getNonNullString(resultSet, COLUMN_LOCATION),
				SqlUtilities.getNonNullString(resultSet, COLUMN_CONTACT),
				SqlUtilities.getNonNullString(resultSet, COLUMN_HOSTNAME),
				resultSet.getDate(COLUMN_CREATED).getTime(),
				resultSet.getDate(COLUMN_MODIFIED).getTime());
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} finally {
				if (stmt != null)
					stmt.close();
			}
		}
	}

	static void loadAgents(final Connection conn, final AgentSeq_TransferableHolder agentSeq) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			StringBuffer sql = new StringBuffer(KEYWORD_SELECT);
			sql.append(COLUMN_ID);
			sql.append(COMMA);
			sql.append(COLUMN_NAME);
			sql.append(COMMA);
			sql.append(COLUMN_CONTACT);
			sql.append(COMMA);
			sql.append(COLUMN_LICENSE_ID);
			sql.append(COMMA);
			sql.append(COLUMN_VERSION);
			sql.append(COMMA);
			sql.append(COLUMN_LOCATION);
			sql.append(COMMA);
			sql.append(COLUMN_HOSTNAME);
			sql.append(COMMA);
			sql.append(COLUMN_CREATED);
			sql.append(COMMA);
			sql.append(COLUMN_MODIFIED);
			sql.append(KEYWORD_FROM);
			sql.append(TABLE_AGENTS);
			if (DEBUG)
				System.out.println(sql);
				
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql.toString());

			Collection agents = new LinkedList();
			while (resultSet.next())
				agents.add(new Agent_Transferable(resultSet.getString(COLUMN_ID),
					SqlUtilities.getNonNullString(resultSet, COLUMN_NAME),
					SqlUtilities.getNonNullString(resultSet, COLUMN_VERSION),
					SqlUtilities.getNonNullString(resultSet, COLUMN_LICENSE_ID),
					SqlUtilities.getNonNullString(resultSet, COLUMN_LOCATION),
					SqlUtilities.getNonNullString(resultSet, COLUMN_CONTACT),
					SqlUtilities.getNonNullString(resultSet, COLUMN_HOSTNAME),
					resultSet.getDate(COLUMN_CREATED).getTime(),
					resultSet.getDate(COLUMN_MODIFIED).getTime()));
			agentSeq.value = (Agent_Transferable[]) agents.toArray(new Agent_Transferable[agents.size()]);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} finally {
				if (stmt != null)
					stmt.close();
			}
		}
	}
}
