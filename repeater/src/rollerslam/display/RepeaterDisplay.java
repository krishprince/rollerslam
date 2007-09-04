package rollerslam.display;

import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.JOptionPane;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.display.Display;
import rollerslam.infrastructure.server.DisplayRegistryServer;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.repeater.server.DisplayRegistryObserver;
import rollerslam.repeater.server.RepeaterDisplayRegistryServer;
import rollerslam.repeater.server.RepeaterServer;

/**
 * Essa classe representa um display, mas as mensagens enviadas pela simulação
 * são reencaminhadas para o conjunto de displays registrados pelo repetidor
 *
 * @author Pablo
 */
public class RepeaterDisplay implements Display, DisplayRegistryObserver {

    private DisplayRegistryServer displayRegistry;
    private Vector<DisplayProcessor> processors = new Vector<DisplayProcessor>();

    private class DisplayProcessor implements Runnable {

        private Vector<Message> messages = new Vector<Message>();

        private Display display;

        public DisplayProcessor(Display d) {
            this.display = d;
        }

        public void addMessage(Message m) {
            synchronized (messages) {
                messages.add(m);
                messages.notifyAll();
            }
        }

        public void run() {
            boolean onError = false;

            while (!onError) {
                while (messages.isEmpty()) {
                    synchronized (messages) {
                        try {
                            messages.wait();
                        } catch (Exception e) {
                        }
                    }
                }

                synchronized (messages) {
                    for (Message m : messages) {
                        try {
                            display.update(m);
                        } catch (Exception e) {
                            if (PrintTrace.TracePrint) {
                                e.printStackTrace();
                            }
                            onError = true;
                            break;
                        }
                    }

                    messages.clear();
                }
            }

            if (onError) {
                try {
                    displayRegistry.unregister(display);
                } catch (RemoteException e) {
                    if (PrintTrace.TracePrint) {
                        e.printStackTrace();
                    }
                }
                synchronized (processors) {
                    processors.remove(this);
                }
            }
        }

        public Display getDisplay() {
            return display;
        }
    }

    public RepeaterDisplay(RepeaterDisplayRegistryServer displayRegistry) {
        this.displayRegistry = displayRegistry;
        displayRegistry.setObserver(this);
    }

    /**
     * @see rollerslam.infrastructure.server.Display#update(Message m)
     */
    public void update(Message m) throws RemoteException {
        synchronized (processors) {
            for (DisplayProcessor processor : processors) {
                processor.addMessage(m);
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        RepeaterServer server = RepeaterServer.getInstance();
        try {
            String host = null;

            if (args.length > 0) {
                host = args[0];
            } else {
                host = JOptionPane.showInputDialog("Simulation:", "AUTO");
            }
            if ("AUTO".equalsIgnoreCase(host)) {
                host = null;
            }
            server.init(host);
        } catch (RemoteException e) {
            if (PrintTrace.TracePrint) {
                e.printStackTrace();
            }
        }
    }

    public void notifyRegistered(Display d) {
        DisplayProcessor dp = new DisplayProcessor(d);
        processors.add(dp);
        new Thread(dp).start();
    }

    public void notifyUnregistered(Display d) {
        DisplayProcessor proc = null;
        for (DisplayProcessor processor : processors) {
            if (processor.getDisplay().equals(d)) {
                proc = processor;
            }
        }

        if (proc != null) {
            processors.remove(proc);
        }
    }
}