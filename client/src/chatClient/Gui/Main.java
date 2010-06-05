package chatClient.Gui;

import chatClient.Client;
import chatClient.Config;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import java.awt.event.*;

public class Main extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTabbedPane tabbedPane1;
    private JTextField textField1;
    private JTextPane textPane1;
    private JTextField ipAddressTextField;
    private JTextField portTextField;
    private JButton buttonConnect;
    private JButton buttonDisconnect;

    private final DefaultStyledDocument doc;
    private StyleContext sc;
    private Client clientThread;


    public Main() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        sc = new StyleContext();
        doc = new DefaultStyledDocument(sc);
        textPane1.setDocument(doc);

        try {
            clientThread = new Client(this);
        }
        catch(Exception e) {
            addText(e.toString());
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        
        buttonConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientThread.setIp(ipAddressTextField.getText());
                clientThread.setPort(Integer.parseInt(portTextField.getText()));
                clientThread.start();
                System.out.println("After Client thread");
            }
        });
        buttonDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public void addText(final String text) {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                try {
                    doc.insertString(0, text + "\n", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        try {
                            doc.insertString(0, text + "\n", null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch(Exception e) {

        }
    }

    public void markConnected() {
        ipAddressTextField.setEnabled(false);
        portTextField.setEnabled(false);
        buttonConnect.setEnabled(false);
        buttonDisconnect.setEnabled(true);
    }

    public void markDisconnected() {
        ipAddressTextField.setEnabled(true);
        portTextField.setEnabled(true);
        buttonConnect.setEnabled(true);
        buttonDisconnect.setEnabled(false);
    }
}