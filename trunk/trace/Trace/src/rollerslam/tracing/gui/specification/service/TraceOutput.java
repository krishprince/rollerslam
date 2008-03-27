/**
 * 
 */
package rollerslam.tracing.gui.specification.service;

/**
 * @author Rafael Oliveira
 *
 */
public interface TraceOutput {
	
    public void append(String text);    
    public void clear();    
    public void setText(String text);

}
