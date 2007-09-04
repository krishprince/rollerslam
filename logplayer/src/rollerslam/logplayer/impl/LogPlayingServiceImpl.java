/*
 * LogPlayingImpl.java
 *
 * Created on 14/08/2007, 15:12:26
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.logplayer.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import orcas.helpers.SerializationHelper;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.logging.LogEntry;
import rollerslam.logplayer.LogPlayingService;

/**
 *
 * @author Weslei
 */
public class LogPlayingServiceImpl implements LogPlayingService {

    private Integer currentCycle;
    private Integer totalCycles;
    private Connection conn;
    private String user = "sa";
    private String password = "";
    
    private String readLogForAgentSQL = "SELECT thelog FROM t_log WHERE agent_id = ? AND cycle = ?";

    public LogPlayingServiceImpl() {
    }

    public void load(String simDescription) {
        currentCycle = 0;
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            String url = "jdbc:hsqldb:" + simDescription + ";shutdown=true";
            conn = conn = DriverManager.getConnection(url, "sa", "");
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    LogPlayingServiceImpl.this.terminate();
                }
            });

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT max(cycle) FROM t_log");
            rs.next();
            int temp = rs.getInt(1);
            if (temp < 2) {
                throw new RuntimeException("This is not a valid simulation database.");
            }
            totalCycles = temp;
            st.close();
        } catch (Exception err) {
            throw new RuntimeException("Error loading simDescription. Details: " + err, err);
        }
    }

    public Integer getTotalCycles() {
        return totalCycles;
    }

    public Integer getCurrentCycle() {
        return currentCycle;
    }

    public void setCurrentCycle(Integer actualCycle) {
        this.currentCycle = actualCycle;
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

    public LogEntry getLogForAgent(Integer agId) {
        LogEntry le = null;
        try {
            if (!conn.isClosed()) {
                PreparedStatement ps = conn.prepareStatement(readLogForAgentSQL);
                ps.setInt(1, agId);
                ps.setInt(2, currentCycle);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    le = (LogEntry)SerializationHelper.string2Object(rs.getString(1));
                }

                ps.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error reading object. Details: " + ex, ex);
        }
        return le;
    }

    public static void main(String[] args) {
    }
}
