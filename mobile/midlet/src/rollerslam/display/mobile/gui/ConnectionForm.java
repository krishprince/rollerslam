package rollerslam.display.mobile.gui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import rollerslam.display.mobile.gui.mvc.Controller;
import rollerslam.display.mobile.gui.mvc.Model;
import rollerslam.display.mobile.gui.mvc.View;
import rollerslam.infrastructure.server.PrintTrace;

public class ConnectionForm extends Form implements CommandListener, View {

	private TextField hostField = new TextField("Host:", "localhost", 255, TextField.ANY);
	private Command   connectCommand = new Command("Connect", Command.OK, 1);
	private Display   display;
	
	private Controller controller;
	private Model      model;
	
	public ConnectionForm(Display d, Controller c, Model m) {
		super("Rollerslam");
		this.display = d;		
		this.controller = c;
		this.model = m;
		
		append(hostField);
		addCommand(connectCommand);
		
		this.setCommandListener(this);
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if (arg0 == connectCommand) {
			try {
				controller.connect(hostField.getString());
				MobileCanvas canvas = new MobileCanvas();
				canvas.setModel(model);
				display.setCurrent(canvas);
			} catch (Exception e) {
				display.setCurrent(new Alert("Error", e.getMessage(), null, AlertType.ERROR), this);
				if (PrintTrace.TracePrint){
					e.printStackTrace();
				}
			}
		}
	}

}
