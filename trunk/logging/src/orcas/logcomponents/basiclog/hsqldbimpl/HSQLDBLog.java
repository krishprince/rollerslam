/*
 * HSQLDBLog.java
 *
 */

package orcas.logcomponents.basiclog.hsqldbimpl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import orcas.helpers.SerializationHelper;
import orcas.logcomponents.basiclog.AbstractLog;
import rollerslam.infrastructure.logging.AgentActionLogEntry;
import rollerslam.infrastructure.logging.EnvironmentStateLogEntry;
import rollerslam.infrastructure.logging.GoalUpdateLogEntry;
import rollerslam.infrastructure.logging.LogEntry;

/**
 *
 * @author Weslei
 */
public class HSQLDBLog extends AbstractLog {

    private String user = "sa";
    private String password = "";

    //create table string
    private String ctStr = "CREATE TABLE t_log (lid INTEGER IDENTITY, cycle INTEGER, agent_id INTEGER, dt_hour TIMESTAMP, winfo VARCHAR, msg VARCHAR, reason VARCHAR, ainfo VARCHAR, thelog VARCHAR)";

    private String insStr = "INSERT INTO t_log(cycle, agent_id, dt_hour, winfo, msg, reason, ainfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


    private Connection conn = null;

    public HSQLDBLog() {
    }


    protected void init(Properties logProperties) {
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();

            //current timestamp yearmonthday_hourminute
            DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmm");
            String ct = df.format(new Date());

            String url = "jdbc:hsqldb:" + logProperties.getProperty("db.url") + "testrollerslam_" + ct + ";shutdown=true";

            conn = DriverManager.getConnection(url, "sa", "");


            Statement st = conn.createStatement();

            st.executeUpdate(ctStr);
            
            st.close();

            System.out.println("DB Logger initialized. Logging to: rollerslam_" + ct);
            
            Runtime.getRuntime().addShutdownHook(new Thread(){
                public void run() {
                    HSQLDBLog.this.terminate();
                }
            });
            
        } catch (Exception ex) {
            System.out.println("Error setting up db. Details: " + ex);
        }
    }


    protected void doLog(Serializable message) {
        if (!(message instanceof LogEntry)) {
            throw new RuntimeException("This log implementation wasn't designed to log " + message.getClass());
        }
        try {
            PreparedStatement ps = conn.prepareStatement(insStr);

            LogEntry le = (LogEntry) message;

            ps.setInt(1, le.getCycle());
            ps.setInt(2, le.getAgentId());
            ps.setTimestamp(3, new java.sql.Timestamp(le.getTimestamp().getTime()));

            if (le instanceof EnvironmentStateLogEntry) {
                ps.setString(4, SerializationHelper.object2String(((EnvironmentStateLogEntry) le).getWorld()));
            } else {
                ps.setNull(4, Types.VARCHAR);
            }

            if (le instanceof AgentActionLogEntry) {
                ps.setString(5, SerializationHelper.object2String(((AgentActionLogEntry) le).getMessage()));
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            if (le instanceof GoalUpdateLogEntry) {
                ps.setString(6, ((GoalUpdateLogEntry) le).getReason());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }
            
            if (le.getAdditionalData() != null) {
                ps.setString(7, SerializationHelper.object2String(le.getAdditionalData()));
            } else {
                ps.setNull(7, Types.VARCHAR);
            }
            
            ps.setString(8, SerializationHelper.object2String(le));

            ps.executeUpdate();
            
            ps.close();
            
        } catch (Exception err) {
            System.out.println("Error on log operation. Details: " + err);
        }
    }

    public void terminate() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error closing DB connection. Details: " + ex);
        }
    }

    public static void main(String ... args) throws Exception {
        Class.forName("org.hsqldb.jdbcDriver").newInstance();
        Connection c = DriverManager.getConnection("jdbc:hsqldb:file:///c:/temp/testrollerslam_20070812_2316;");
        Statement s = c.createStatement();
        java.sql.ResultSet rs = s.executeQuery("SELECT * FROM t_log");
        while (rs.next()) {
            System.out.println("Found: " + rs.getString(1) + " - " 
                    + rs.getString(2) + " - " + rs.getString(3) + " - " 
                    + rs.getString(4) + " - " + rs.getString(5));
        }
        
    }
}
