/*
 * $Id: ResourcedbInterface.java,v 1.3 2004/09/09 11:32:40 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server;

import com.syrus.AMFICOM.CORBA.Resource.ImageResource_Transferable;
import com.syrus.util.database.JdbcBlobManager;
import java.sql.*;
import java.util.*;

/**
 * All methods which don't accept {@link Connection} as their first argument
 * will be removed.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/09 11:32:40 $
 * @module servermisc_v1
 * @todo Dispose of duplicate string literals. 
 */
public final class ResourcedbInterface {
	private static final char SEPARATOR = '-';

	/**
	 * Declared public as
	 * {@link com.syrus.AMFICOM.server.object.AMFICOMdbInterface} uses
	 * these.
	 */
	public static final String DESC_IDS[] = {
		"accessport", "ACCESSPORTS", "",
		"accessporttype", "ACCESSPORTTYPES", "",
		"agent", "AGENTS", "",
		"alarm", "ALARMS", " where STATUS != 4",
		"analysis", "ANALYSIS", "",
		"analysistype", "ANALYSISTYPES", "",
		"cablelink", "CABLELINKS", "",
		"cablelinktype", "CABLETYPES", "",
		"cableport", "CABLEPORTS", "",
		"cableporttype", "CABLEPORTTYPES", "",
		"characteristictype", "CHARACTERISTICTYPES", "",
		"client", "CLIENTS", "",
		"criteriaset", "CRITERIASETS", "",
		"criteriatype", "CRITERIATYPES", "",
		"domain", "DOMAINS", "",
		"elementattributetype", "ELEMENTATTRIBUTETYPES", "",
		"equipment", "EQUIPMENTS", " where IS_KIS = 0",
		"equipmenttype", "EQUIPMENTTYPES", "",
		"evaluation", "EVALUATIONS", "",
		"evaluationtype", "EVALUATIONTYPES", "",
		"globalparametertype", "PARAMETERTYPES", "",
		"imageresource", "IMAGERESOURCES", " where source_string != 'scheme'",
		"ismmapcontext", "ISMMAPCONTEXTS", "",
		"kis", "EQUIPMENTS", " where IS_KIS = 1",
		"link", "LINKS", "",
		"linktype", "LINKTYPES", "",
		"mapcontext", "MAPCONTEXTS", "",
		"maplinkproto", "MAPLINKPROTOELEMENTS", "",
		"mappathproto", "MAPPATHPROTOELEMENTS", "",
		"mapprotogroup", "MAPPROTOGROUPS", "",
		"mapprotoelement", "MAPPROTOELEMENTS", "",
		"modeling", "MODELING", "",
		"modelingtype", "MODELINGTYPES", "",
		"monitoredelement", "MONITOREDELEMENTS", "",
		"operatorcategory", "CATEGORIES", "",
		"operatorgroup", "GROUPS", "",
		"operatorprofile", "OPERATORPROFILES", "",
		"path", "TRANSMISSIONPATHS", "",
		"pathtype", "PATHTYPES", "",
		"port", "PORTS", "",
		"porttype", "PORTTYPES", "",
		"proto", "SCHEMEPROTOELEMENTS", " where IS_TOP_LEVEL = 1",
		"reporttemplate", "REPORTTEMPLATES", "",
		"resultset", "RESULTSETS", "",
		"scheme", "SCHEMES", "",
		"server", "SERVERS", "",
		"test", "TESTS", " where DELETED is null",
		"testonetime", "TESTS", " where DELETED is null and TEMPORAL_TYPE = 0",
		"testsetup", "TESTSETUPS", "",
		"testtype", "TESTTYPES", ""
	};

	/**
	 * <code>SM_solution</code> is obsolete and will be removed soon.
	 */
	private static final String SEQ_IDS[] = {
		"accessport", "accprt", "ACCPRT_SEQ",
		"actionargument", "acarg", "AARG_SEQ",
		"actionparameter", "acpar", "APAR_SEQ",
		"agent", "agent", "ADMIN_SEQ",
		"alarm", "alrm", "ALRM_SEQ",
		"alarmrule", "alrm", "ALRM_SEQ",
		"alerting", "alrt", "ALRM_SEQ",
		"alertingmessage", "am", "ALRM_SEQ",
		"alertingmessageuser", "amu", "ALRM_SEQ",
		"analysis", "ani", "ANI_SEQ",
		"analysiscriteria", "ancri", "ATARG_SEQ",
		"analysistype", "anityp", "ANITYP_SEQ",
		"analysistypeargument", "atparg", "ATARG_SEQ",
		"analysistypecriteria", "atpcri", "ATARG_SEQ",
		"analysistypeparameter", "atppar", "ATARG_SEQ",
		"attribute", "attr", "ATTRIB_SEQ",
		"attributerule", "attrrul", "ATTRIB_SEQ",
		"cablelink", "clnk", "CABLE_SEQ",
		"cablelinkthread", "cth", "CABLE_SEQ",
		"cablelinktype", "clt", "LT_SEQ",
		"cablethread", "cth", "CABLE_SEQ",
		"cableport", "cprt", "CABLE_SEQ",
		"cableporttype", "CPRTT", "PRTT_SEQ",
		"characteristic", "char", "CHAR_SEQ",
		"client", "client", "ADMIN_SEQ",
		"comm_perm_attrib", "comattr", "COMATTR_SEQ",
		"criteria", "cri", "CRI_SEQ",
		"criteriaset", "criset", "CRISET_SEQ",
		"criteriasetmelink", "csmelnk", "CSMELNK_SEQ",
		"criteriatype", "crityp", "CRITYP_SEQ",
		"deviceport", "sdprt", "SCHPRO_SEQ",
		"domain", "domain", "DOMAIN_SEQ",
		"elementattribute", "eattr", "EATTRIB_SEQ",
		"elementcharacteristic", "echar", "ECHAR_SEQ",
		"equipment", "eq", "EQ_SEQ",
		"equipmenttype", "eqt", "EQT_SEQ",
		"etalon", "eta", "ETA_SEQ",
		"etalontypeparameter", "etatp", "ETATP_SEQ",
		"etalonparameter", "etap", "ETATP_SEQ",
		"evaluation", "eval", "EVAL_SEQ",
		"evaluationargument", "evarg", "ARG_SEQ",
		"evaluationtypeargument", "etparg", "ETARG_SEQ",
		"evaluationtypeparameter", "etppar", "ETARG_SEQ",
		"evaluationtypethreshold", "etpth", "ETARG_SEQ",
		"event", "sysev", "SYSEV_SEQ",
		"eventsource", "esrc", "SYSEVSRC_SEQ",
		"fieldreport", "fldrpt", "RPT_SEQ",
		"imageresource", "img", "IMG_SEQ",
		"ismmapcontext", "ismap", "ISMMAP_SEQ",
		"kis", "kis", "KIS_SEQ",
		"link", "link", "LINK_SEQ",
		"linktype", "lt", "LT_SEQ",
		"loggeduser", "sess", "LUSR_SEQ",
		"mapconnectionpointelement", "mapcp", "MAPCPE_SEQ",
		"mapcontext", "map", "MAP_SEQ",
		"mapelementlink", "mel", "MEL_SEQ",
		"mapequipmentelement", "mapeq", "MAPEQE_SEQ",
		"maplinkelement", "maplink", "MAPLE_SEQ",
		"mapkiselement", "mapkis", "MAPKISE_SEQ",
		"mapmarkelement", "mapmark", "MAPNODE_SEQ",
		"mapnodeelement", "mapnode", "MAPNODE_SEQ",
		"mapnodelinkelement", "mapnl", "MAPNLE_SEQ",
		"mappathelement", "mappath", "MAPPE_SEQ",
		"mappathlink", "mpl", "MPLINK_SEQ",
		"mapprotogroup", "mappg", "MAPPRO_SEQ",
		"mapprotoelement", "mappro", "MAPPRO_SEQ",
		"modeling", "mod", "ANI_SEQ",
		"monitoredelement", "mone", "MONE_SEQ",
		"monitoredelementattachment", "monea", "MONEA_SEQ",
		"operatorcategorylink", "opclink", "OPCLNK_SEQ",
		"operatorgroup", "grp", "GRP_SEQ",
		"operatorgrouplink", "opglink", "OPGLNK_SEQ",
		"operatorprofile", "oprf", "OPRF_SEQ",
		"parameter", "param", "PARAM_SEQ",
		"path", "path", "PATH_SEQ",
		"pathlink", "pathlnk", "PLNK_SEQ",
		"port", "port", "PORT_SEQ",
		"porttype", "prtt", "PRTT_SEQ",
		"proto", "sproto", "SCHPRO_SEQ",
		"reporttemplate", "rpt", "RPT_SEQ",
		"reporttemplatebox", "rptbox", "RPT_SEQ",
		"reporttemplatefield", "rptfld", "RPT_SEQ",
		"resourcequery", "resqry", "RESQRY_SEQ",
		"result", "result", "RES_SEQ",
		"resultparameter", "rpar", "RESPAR_SEQ",
		"resultset", "rset", "RSET_SEQ",
		"scheme", "sch", "SCHEME_SEQ",
		"schemecablelink", "schclnk", "SCHPRO_SEQ",
		"schemecableport", "schcprt", "SCHPRO_SEQ",
		"schemecablethread", "schcth", "SCHPRO_SEQ",
		"schemedevice", "schdev", "SCHPRO_SEQ",
		"schemeelement", "schel", "SCHEL_SEQ",
		"schemeelementlink", "schell", "SCHEL_SEQ",
		"schemelink", "schlnk", "SCHPRO_SEQ",
		"optimized_scheme_info", "soi", "SCHEME_SEQ",
		"schemepath", "schpath",  "SCHEL_SEQ",
		"schemepathlink", "spl",  "SCHEL_SEQ",
		"schemeport", "schprt", "SCHPRO_SEQ",
		"server", "server", "ADMIN_SEQ",
		"sm_solution", "sol", "SCHEME_SEQ",
		"SM_solution", "sol", "SCHEME_SEQ",
		"systemevent", "sysev", "SYSEV_SEQ",
		"systemeventsource", "esrc", "SYSEVSRC_SEQ",
		"sourceeventtyperule", "setrul", "SETRUL_SEQ",
		"test", "test", "TEST_SEQ",
		"testargument", "targ", "ARG_SEQ",
		"testargumentset", "tas", "ARG_SEQ",
		"testrequest", "treq", "TREQ_SEQ",
		"testport", "tstprt", "TSTPRT_SEQ",
		"testsetup", "tsetup", "TSETUP_SEQ",
		"testtimestamp", "ttmst", "TTMST_SEQ",
		"threshold", "thr", "THR_SEQ",
		"thresholdset", "thrset", "THRSET_SEQ",
		"user", "user", "USER_SEQ"
	};

	private static final String COLUMN_NAME_IMG = "img";

	private static final String COLUMN_NAME_SOURCE_STRING = "source_string";

	private static final String TABLE_NAME_IMAGERESOURCES
		= "amficom.imageresources";

	private static final String ID_EQUALS = "id = '";

	private static final String SOURCE_STRING_FILE = "file";

	/**
	 * @see "ORA-01795"
	 */
	private static final int MAXIMUM_EXPRESSION_NUMBER = 1000;

	private ResourcedbInterface() {
	}

	/**
	 * @param conn the database connection to use.
	 */
	public static String getUid(final Connection conn, String type) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			String identifierBase = null;
			String sequenceName = null;
			for (int i = 0; i < SEQ_IDS.length; i += 3)
				if (SEQ_IDS[i].equals(type)) {
					identifierBase = SEQ_IDS[i + 1];
					sequenceName = SEQ_IDS[i + 2];
					break;
				}
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT amficom." + sequenceName + ".nextval FROM sys.dual");
			if (resultSet.next())
				return identifierBase + SEPARATOR + String.valueOf(resultSet.getInt("nextval"));
			else
				return type + System.currentTimeMillis();
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

	/**
	 * @param conn the database connection to use.
	 */
	public static ImageResource_Transferable getImage(final Connection conn, String imageResourceId) throws SQLException {
		try {
			Collection imageResourceIds = new LinkedList();
			imageResourceIds.add(imageResourceId);
			return (ImageResource_Transferable) (getImages(conn, imageResourceIds).iterator().next());
		} catch (NoSuchElementException nsee) {
			nsee.printStackTrace();
			return null;
		}
	}

	/**
	 * Implicitly issues <code>COMMIT</code> immediately after data is
	 * stored, which may cause some overhead. This behaviour should be
	 * changed for optimal performance.
	 *
	 * @param conn the database connection to use.
	 */
	public static void setImage(final Connection conn, String imageResourceId, byte data[]) throws SQLException {
		JdbcBlobManager.setData(conn, COLUMN_NAME_IMG, TABLE_NAME_IMAGERESOURCES, ID_EQUALS + imageResourceId + '\'', data);
	}

	/**
	 * Implicitly issues <code>COMMIT</code> immediately after data is
	 * stored, which may cause some overhead. This behaviour should be
	 * changed for optimal performance.
	 *
	 * @param conn the database connection to use.
	 */
	public static String setImage(final Connection conn, byte data[]) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			String imageResourceId = getUid(conn, "imageresource");
			stmt = conn.createStatement();
			stmt.executeUpdate("INSERT INTO amficom.imageresources (id, name, codename, filename, source_string, img) VALUES ('" + imageResourceId + "', 'bytes', 'bytes', '', 'bytes', empty_blob())");
			setImage(conn, imageResourceId, data);
			return imageResourceId;
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

	/**
	 * <p>Each entry of <code>imageResourceSeq</code> must have:
	 * <ul>
	 * <li>a valid {@link ImageResource_Transferable#id id} field;</li>
	 * <li>a valid
	 * {@link ImageResource_Transferable#source_string source_string} field,
	 * namely one of:<ul>
	 * 	<li>{@link #SOURCE_STRING_FILE &quot;file&quot;};</li>
	 *      <li><code>&quot;bytes&quot;</code>;</li>
	 *      <li><code>&quot;scheme&quot;</code>;</li>
	 * </ul></li>
	 * <li>if <code>source_string</code> is either
	 * <code>&quot;bytes&quot;</code> or <code>&quot;scheme&quot;</code>,
	 * {@link ImageResource_Transferable#image image} field must be
	 * non-null.</li>
	 * </ul>
	 * All other fields may remain uninitalized.</p>
	 * <p>This method doesn't change <code>autoCommit</code> state of the
	 * underlying database connection. It's desired for best performance
	 * that this property be <code>false</code>.</p>
	 *
	 * @param conn the database connection to use.
	 * @param imageResourceSeq an array of images to be saved.
	 * @throws SQLException if a database error occurs.
	 */
	public static void setImages(final Connection conn, ImageResource_Transferable imageResourceSeq[]) throws SQLException {
		if (imageResourceSeq == null)
			return;

		int imageResourceSeqLength = imageResourceSeq.length;

		if (imageResourceSeqLength == 0)
			return;

		StringBuffer sql1 = new StringBuffer("SELECT id FROM ");
		sql1.append(TABLE_NAME_IMAGERESOURCES);
		sql1.append(" WHERE id ");

		StringBuffer sql2 = new StringBuffer("INSERT INTO ");
		sql2.append(TABLE_NAME_IMAGERESOURCES);
		sql2.append(" (id, name, codename, filename, ");
		sql2.append(COLUMN_NAME_SOURCE_STRING);
		sql2.append(", ");
		sql2.append(COLUMN_NAME_IMG);
		sql2.append(") VALUES (");
		if (imageResourceSeqLength == 1) {
			ImageResource_Transferable imageResource = imageResourceSeq[0];

			sql1.append("= '");
			sql1.append(imageResource.id);
			sql1.append('\'');

			sql2.append('\'');
			sql2.append(imageResource.id);
			sql2.append("', '");
			sql2.append(imageResource.source_string);
			sql2.append("', '");
			sql2.append(imageResource.source_string);
			sql2.append("', '");
			sql2.append(imageResource.filename == null ? "" : imageResource.filename);
			sql2.append("', '");
			sql2.append(imageResource.source_string);
			sql2.append('\'');
			sql2.append(", empty_blob())");

			Statement stmt1 = null;
			Statement stmt2 = null;
			ResultSet resultSet = null;
			try {
				stmt1 = conn.createStatement();
				stmt2 = conn.createStatement();
				resultSet = stmt1.executeQuery(sql1.toString());
				if (!resultSet.next())
					stmt2.executeUpdate(sql2.toString());
				if (!imageResource.source_string.equals(SOURCE_STRING_FILE))
					JdbcBlobManager.setData(conn, COLUMN_NAME_IMG, TABLE_NAME_IMAGERESOURCES, ID_EQUALS + imageResource.id + '\'', false, imageResource.image);
				conn.commit();
			} finally {
				try {
					if (resultSet != null)
						resultSet.close();
				} finally {
					try {
						if (stmt1 != null)
							stmt1.close();
					} finally {
						if (stmt2 != null)
							stmt2.close();
					}
				}
			}
		} else {
			sql1.append("= ?");

			sql2.append("?, ?, ?, ?, ?");
			sql2.append(", empty_blob())");

			PreparedStatement stmt1 = null;
			PreparedStatement stmt2 = null;
			try {
				stmt1 = conn.prepareStatement(sql1.toString());
				stmt2 = conn.prepareStatement(sql2.toString());
				for (int i = 0; i < imageResourceSeqLength; i++) {
					ImageResource_Transferable imageResource = imageResourceSeq[i];
					stmt1.setString(1, imageResource.id);
					ResultSet resultSet = null;
					try {
						resultSet = stmt1.executeQuery();
						if (!resultSet.next()) {
							stmt2.setString(1, imageResource.id);
							stmt2.setString(2, imageResource.source_string);
							stmt2.setString(3, imageResource.source_string);
							stmt2.setString(4, imageResource.filename);
							stmt2.setString(5, imageResource.source_string);
							stmt2.executeUpdate();
						}
						if (!imageResource.source_string.equals(SOURCE_STRING_FILE))
							JdbcBlobManager.setData(conn, COLUMN_NAME_IMG, TABLE_NAME_IMAGERESOURCES, ID_EQUALS + imageResource.id + '\'', false, imageResource.image);
					} finally {
						if (resultSet != null)
							resultSet.close();
					}
				}
				conn.commit();
			} finally {
				try {
					if (stmt1 != null)
						stmt1.close();
				} finally {
					if (stmt2 != null)
						stmt2.close();
				}
			}
		}
	}

	/**
	 * @param conn the database connection to use.
	 */
	public static Collection getImages(final Connection conn) throws SQLException {
		return getImages(conn, Integer.MAX_VALUE);
	}

	/**
	 * @param conn the database connection to use.
	 */
	public static Collection getImages(final Connection conn, final int fetchSize) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			Collection imageResources = new LinkedList();

			stmt = conn.createStatement();

			StringBuffer sql = new StringBuffer("SELECT id, name, ");
			sql.append(COLUMN_NAME_SOURCE_STRING);
			sql.append(", filename, ");
			sql.append(COLUMN_NAME_IMG);
			sql.append(" FROM ");
			sql.append(TABLE_NAME_IMAGERESOURCES);

			int i = 0;
			resultSet = stmt.executeQuery(sql.toString());
			while (resultSet.next() && (i++ < fetchSize)) {
				String sourceString = resultSet.getString(COLUMN_NAME_SOURCE_STRING);
				ImageResource_Transferable imageResource = new ImageResource_Transferable(resultSet.getString("id"), resultSet.getString("name"), sourceString, resultSet.getString("filename"), new byte[0]); 
					if (!sourceString.equals(SOURCE_STRING_FILE))
						imageResource.image = JdbcBlobManager.getData(resultSet.getBlob(COLUMN_NAME_IMG));
					imageResources.add(imageResource);
			}
			return imageResources;
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

	/**
	 * Returns a collection (actually a {@link LinkedList}) of images
	 * fetched by their identifiers. It is assumed that
	 * <code>imageResourceIds</code> contains no duplicate elements.
	 *
	 * @param conn the database connection to use.
	 * @param imageResourceIds identifiers of images to be fetched.
	 * @return a collection of images.
	 * @throws SQLException if a database error occurs.
	 */
	public static Collection getImages(final Connection conn, final Collection imageResourceIds) throws SQLException {
		if (imageResourceIds == null)
			return new LinkedList();

		int imageResourceIdsLength = imageResourceIds.size();

		if (imageResourceIdsLength == 0)
			return new LinkedList();

		StringBuffer sql = new StringBuffer("SELECT id, name, ");
		sql.append(COLUMN_NAME_SOURCE_STRING);
		sql.append(", filename, ");
		sql.append(COLUMN_NAME_IMG);
		sql.append(" FROM ");
		sql.append(TABLE_NAME_IMAGERESOURCES);
		sql.append(" WHERE id ");
		if (imageResourceIdsLength == 1) {
			sql.append("= '");
			sql.append((String) (imageResourceIds.iterator().next())); 
			sql.append('\'');
		} else if (imageResourceIdsLength <= MAXIMUM_EXPRESSION_NUMBER) {
			sql.append("IN ('");
			int i = 1;
			for (Iterator iterator = imageResourceIds.iterator(); iterator.hasNext(); i++) {
				sql.append(iterator.next());
				if (i < imageResourceIdsLength)
					sql.append("', '");
				else
					sql.append("')");
			}
		} else {
			Collection c1 = new LinkedList();
			Collection c2 = new LinkedList();
			int i = 0;
			for (Iterator iterator = imageResourceIds.iterator(); iterator.hasNext(); i++) {
				if (i < MAXIMUM_EXPRESSION_NUMBER)
					c1.add(iterator.next());
				else
					c2.add(iterator.next());
			}
			
			Collection imageResources = getImages(conn, c1);
			imageResources.addAll(getImages(conn, c2));
			return imageResources;
		}

		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql.toString());
			Collection imageResources = new LinkedList();
			while (resultSet.next()) {
				String sourceString = resultSet.getString(COLUMN_NAME_SOURCE_STRING);
				ImageResource_Transferable imageResource = new ImageResource_Transferable(resultSet.getString("id"), resultSet.getString("name"), sourceString, resultSet.getString("filename"), new byte[0]); 
				if (!sourceString.equals(SOURCE_STRING_FILE))
					imageResource.image = JdbcBlobManager.getData(resultSet.getBlob(COLUMN_NAME_IMG));
				imageResources.add(imageResource);
			}
			return imageResources;
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

	/**
	 * Does the same as {@link #getImages(Connection, Collection)}, using a different
	 * (less tricky) SQL query. This method is slightly slower (24 seconds
	 * vs. 18 seconds for a 1000-element table), but is not removed as it
	 * uses a more standard approach.
	 * 
	 * @param conn the database connection to use.
	 * @param imageResourceIds identifiers of images to be fetched.
	 * @return a collection of images.
	 * @throws SQLException if a database error occurs.
	 */
	public static Collection getImagesStd(final Connection conn, final Collection imageResourceIds) throws SQLException {
		Collection imageResources = new LinkedList();

		if (imageResourceIds == null)
			return imageResources;

		int imageResourceIdsLength = imageResourceIds.size();

		if (imageResourceIdsLength == 0)
			return imageResources;

		StringBuffer sql = new StringBuffer("SELECT id, name, ");
		sql.append(COLUMN_NAME_SOURCE_STRING);
		sql.append(", filename, ");
		sql.append(COLUMN_NAME_IMG);
		sql.append(" FROM ");
		sql.append(TABLE_NAME_IMAGERESOURCES);
		sql.append(" WHERE id ");
		if (imageResourceIdsLength == 1) {
			sql.append("= '");
			sql.append((String) (imageResourceIds.iterator().next()));
			sql.append('\'');
			Statement stmt = null;
			ResultSet resultSet = null;
			try {
				stmt = conn.createStatement();
				resultSet = stmt.executeQuery(sql.toString());
				if (resultSet.next()) {
					String sourceString = resultSet.getString(COLUMN_NAME_SOURCE_STRING);
					ImageResource_Transferable imageResource = new ImageResource_Transferable(resultSet.getString("id"), resultSet.getString("name"), sourceString, resultSet.getString("filename"), new byte[0]); 
					if (!sourceString.equals(SOURCE_STRING_FILE))
						imageResource.image = JdbcBlobManager.getData(resultSet.getBlob(COLUMN_NAME_IMG));
					imageResources.add(imageResource);
				}
				return imageResources;
			} finally {
				try {
					if (resultSet != null)
						resultSet.close();
				} finally {
					if (stmt != null)
						stmt.close();
				}
			}
		} else {
			sql.append("= ?");
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql.toString());
				for (Iterator iterator = imageResourceIds.iterator(); iterator.hasNext();) {
					stmt.setString(1, (String) iterator.next());
					ResultSet resultSet = null;
					try {
						resultSet = stmt.executeQuery();
						if (resultSet.next()) {
							String sourceString = resultSet.getString(COLUMN_NAME_SOURCE_STRING);
							ImageResource_Transferable imageResource = new ImageResource_Transferable(resultSet.getString("id"), resultSet.getString("name"), sourceString, resultSet.getString("filename"), new byte[0]); 
							if (!sourceString.equals(SOURCE_STRING_FILE))
								imageResource.image = JdbcBlobManager.getData(resultSet.getBlob(COLUMN_NAME_IMG));
							imageResources.add(imageResource);
						}
					} finally {
						if (resultSet != null)
							resultSet.close();
					}
				}
				return imageResources;
			} finally {
				if (stmt != null)
					stmt.close();
			}
		}
	}
}
