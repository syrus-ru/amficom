package com.syrus.AMFICOM.server.measurement;

import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import oracle.jdbc.driver.OracleResultSet;
import com.syrus.AMFICOM.CORBA.Survey.ClientParameter_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Parameter_Transferable;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.Log;

public class Argument {
  private String id;
  private String type_id;
  private String holder_id;
  private byte[] value;
  private String holder_sort;

  public Argument(String id, String holder_sort) throws SQLException {
    this.id = id;
    this.holder_sort = holder_sort;

    Statement st = DatabaseConnection.getConnection().createStatement();
    String table_name = this.holder_sort + "arguments";
    String holder_column_name = this.holder_sort + "_id";
    String query = "SELECT type_id, value, " + holder_column_name + " holder_id"
      + " FROM amficom." + table_name
      + " WHERE id = '" + this.id + "'";
    Log.debugMessage("Argument.init | Trying: " + query, Log.DEBUGLEVEL05);
    OracleResultSet rs = (OracleResultSet)st.executeQuery(query);
    if (!rs.next()) {
      String mesg = "No record in " + table_name + " with id '" + this.id + "'";
      throw new SQLException(mesg);
    }
    this.type_id = rs.getString("type_id");
    this.value = ByteArrayDatabase.toByteArray(rs.getBLOB("value"));
    this.holder_id = rs.getString("holder_id");
		rs.close();
		st.close();
  }

  public Argument(ClientParameter_Transferable parameter_t,
                  String holder_id,
                  String holder_sort) throws Exception {
    this(holder_sort,
         parameter_t.id,
         parameter_t.type_id,
         holder_id,
         parameter_t.value);
    this.save();
  }
/*
  public Argument(Parameter_Transferable parameter_t,
                  String holder_id,
                  String holder_sort) throws SQLException {
    this(holder_sort,
         ResourcedbInterface.getUId(holder_sort + "argument"),
         parameter_t.type_id,
         holder_id,
         parameter_t.value);
    this.save();
  }
*/
  private Argument(String holder_sort,
                   String id,
                   String type_id,
                   String holder_id,
                   byte[] value) {
    this.holder_sort = holder_sort;
    this.id = id;
    this.type_id = type_id;
    this.holder_id = holder_id;
    this.value = value;
  }

  public void save() throws Exception {
    Connection conn = DatabaseConnection.getConnection();
    Statement st = conn.createStatement();
    String table_name = this.holder_sort + "arguments";
    String holder_column_name = this.holder_sort + "_id";
    String update = "INSERT INTO amficom." + table_name
      + " (id, type_id, value, " + holder_column_name + ")"
      + " VALUES ('" + this.id + "', '" + this.type_id + "', EMPTY_BLOB(), '" + this.holder_id + "')";
    Log.debugMessage("Argument.save | Trying: " + update, Log.DEBUGLEVEL05);
    st.executeUpdate(update);
		st.close();
    ByteArrayDatabase bdb  = new ByteArrayDatabase(this.value);
    bdb.saveAsBlob(conn, table_name, "value", "id = '" + this.id + "'");
  }

  public String getId() {
    return this.id;
  }

  public String getTypeId() {
    return this.type_id;
  }

  public String getHolderId() {
    return this.holder_id;
  }

  public byte[] getValue() {
    return this.value;
  }

  public ClientParameter_Transferable getClientTransferable() throws SQLException {
    ParameterType parameterType = new ParameterType(this.type_id, this.holder_sort, "argument");
    return new ClientParameter_Transferable(this.id,
                                            this.type_id,
                                            this.value,
                                            parameterType.getCodename(),
                                            parameterType.getParameterTypeId());
  }

  public Parameter_Transferable getTransferable() throws SQLException {
    ParameterType parameterType = new ParameterType(this.type_id, this.holder_sort, "argument");
    return new Parameter_Transferable(parameterType.getCodename(),
                                      this.type_id,
                                      this.value);
  }

	protected void update(ClientParameter_Transferable parameter_t) throws Exception {
		if (parameter_t.id.compareTo(this.id) != 0)
			return;

		this.value = parameter_t.value;

		Connection conn = DatabaseConnection.getConnection();
    String table_name = this.holder_sort + "arguments";
    ByteArrayDatabase badb = new ByteArrayDatabase(this.value);
		badb.saveAsBlob(conn, table_name, "value", "id = '" + this.id + "'");
	}

  public static Argument[] retrieveArguments(String holder_id, String holder_sort) throws SQLException {
    Statement st = DatabaseConnection.getConnection().createStatement();
    String table_name = holder_sort + "arguments";
    String holder_column_name = holder_sort + "_id";
    String query = "SELECT id, type_id, value"
      + " FROM amficom." + table_name
      + " WHERE " + holder_column_name + " = '" + holder_id + "'";
    Log.debugMessage("Argument.retrieveArguments | Trying: " + query, Log.DEBUGLEVEL05);
    OracleResultSet rs = (OracleResultSet)st.executeQuery(query);
    LinkedList lla = new LinkedList();
    while (rs.next())
      lla.add(new Argument(holder_sort,
                           rs.getString("id"),
                           rs.getString("type_id"),
                           holder_id,
                           ByteArrayDatabase.toByteArray(rs.getBLOB("value"))));
		rs.close();
		st.close();
    Log.debugMessage("Argument.retrieveArguments | Retrieved " + lla.size() + " " + holder_sort + " arguments for " + holder_column_name + " = '" + holder_id + "'", Log.DEBUGLEVEL05);
    return (Argument[])lla.toArray(new Argument[lla.size()]);
  }
}
