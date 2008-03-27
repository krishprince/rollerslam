/**
 * 
 */
package rollerslam.tracing.gui.realization.service;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import rollerslam.tracing.gui.specification.service.TraceOutput;

/**
 * @author Rafael Oliveira
 *
 */
public class TraceOutputImpl extends JScrollPane implements TraceOutput{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3440214961783150981L;
	
    
	JTextArea output = new JTextArea(20, 10);
      
    public TraceOutputImpl() {
        setViewportView(output);
    }
    
    public void append(String text) {
    	output.append(text + "\n");
    	output.setCaretPosition( output.getDocument().getLength() );
    }
    
    public void clear() {
        setText("");
    }
    
    public void setText(String text) {
        output.setText(text);
        output.setCaretPosition( output.getDocument().getLength() );
    }

}
