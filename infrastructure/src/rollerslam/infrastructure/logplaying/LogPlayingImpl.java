/*
 * LogPlayingImpl.java
 *
 * Created on 14/08/2007, 15:12:26
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.logplaying;

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

/**
 *
 * @author Weslei
 */
public class LogPlayingImpl implements LogPlayingService {

    private Integer currentCycle;
    private Integer totalCycles;
    private Connection conn;
    private String user = "sa";
    private String password = "";


    private String readWorldInCycle = "SELECT winfo FROM t_log WHERE cycle = ? AND NOT winfo IS NULL";
    private String readAdditionalDataInCycle = "SELECT ainfo FROM t_log WHERE cycle = ? AND NOT ainfo IS NULL";
    private String readMessagesInCycle = "SELECT msg FROM t_log WHERE cycle = ? AND NOT msg IS NULL";

    public LogPlayingImpl() {
    }

    public void load(String simDescription) {
        currentCycle = 0;
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            String url = "jdbc:hsqldb:" + simDescription + ";shutdown=true";
            conn = conn = DriverManager.getConnection(url, "sa", "");
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    LogPlayingImpl.this.terminate();
                }
            });

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT count(*) FROM t_log");
            rs.next();
            totalCycles = rs.getInt(1);
            st.close();

            System.out.println("read " + totalCycles + " registers");
        } catch (Exception err) {
            throw new RuntimeException("Error loading simDescription. Details: " + err);
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

    public World getWorld() {
        World w = null;
        try {
            PreparedStatement ps = conn.prepareStatement(readWorldInCycle);
            ps.setInt(1, this.currentCycle);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("No world info for the cycle " + currentCycle);
            }
            w = (World) SerializationHelper.string2Object(rs.getString(1));
        } catch (Exception ex) {
            throw new RuntimeException("Error reading object. Details: " + ex);
        }
        return w;
    }

    public List<Message> getMessages() {
        List<Message> ms = new ArrayList<Message>();
        try {
            PreparedStatement ps = conn.prepareStatement(readMessagesInCycle);
            ps.setInt(1, this.currentCycle);

            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ms.add((Message)SerializationHelper.string2Object(rs.getString(1)));
            }
            
        } catch (Exception ex) {
            throw new RuntimeException("Error reading object. Details: " + ex);
        }
        return ms;
    }

    public Object getAdditionalData() {
        Object o = null;
        try {
            PreparedStatement ps = conn.prepareStatement(readAdditionalDataInCycle);
            ps.setInt(1, this.currentCycle);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("No additional data info for the cycle " + currentCycle);
            }
            o = SerializationHelper.string2Object(rs.getString(1));
        } catch (Exception ex) {
            throw new RuntimeException("Error reading object. Details: " + ex);
        }
        return o;
    }


    public static void main(String[] args) {
        LogPlayingImpl lpi = new LogPlayingImpl();
        lpi.load("file:///c:/temp/testrollerslam_20070814_1613");
        lpi.setCurrentCycle(3);
        System.out.println(((String[]) lpi.getAdditionalData())[1]);
    }
}
