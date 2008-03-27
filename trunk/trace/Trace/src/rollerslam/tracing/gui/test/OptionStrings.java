package rollerslam.tracing.gui.test;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
 
/**
 * @author Rafael Oliveira
 *
 */
public class OptionStrings
{
    private JPanel mainPanel = new JPanel();
    private ButtonGroup buttonGroup = new ButtonGroup();
 
    private OptionStrings(String[] optionStrings)
    {
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setLayout(new GridLayout(0, 1, 0, 10));
 
        for (String string : optionStrings)
        {
            JRadioButton jrButton = new JRadioButton(string);
            jrButton.setActionCommand(string);
            buttonGroup.add(jrButton);
            mainPanel.add(jrButton);
        }
    }
 
    private JPanel getMainPanel()
    {
        return mainPanel;
    }
    
    private String getSelectedString()
    {
        ButtonModel buttonModel = buttonGroup.getSelection();
        if (buttonModel != null)
        {
            return buttonModel.getActionCommand();
        }
        return null;
    }
    
    /**
     * allow user to select amongst an array of String
     * @param optionStrings: Strings placed in JRadioButton panel.  Becomes choices
     * @return the optionString selected or null if none selected 
     */
    public static String showChoices(String[] optionStrings)
    {
        String selectedString = null;
        OptionStrings oStrings = new OptionStrings(optionStrings);
        int result = JOptionPane.showConfirmDialog(null, oStrings.getMainPanel(), "Choose An Option",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
        {
            selectedString = oStrings.getSelectedString();
        }
        return selectedString;
    }
}
