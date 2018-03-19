import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Gui extends JFrame {
    private JPasswordField passwordField1;
    private JTextField textField1;
    private JButton enterButton;
    private JComboBox comboBox1;
    private JPanel panel1;
    private JTextField textField2;
    private JButton browseButton;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel panel2;
    private JCheckBox SSLEnabled;
    private JCheckBox TLSEnabled;
    JFileChooser chooser = new JFileChooser();
    Indicator indicator = new Indicator();
    private boolean others = false;

    public Gui() {
        super("auto emailsender");
        Container c = getContentPane();
        Gui gui = this;
        c.add(panel1);
        c.setVisible(true);
        panel2.setVisible(false);
        passwordField1.setEchoChar('*');
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        String[] smtpoptions = {"gmail", "yahoo", "outlook", "other"};


        for (int i = 0; i < smtpoptions.length; i++) {
            comboBox1.addItem(smtpoptions[i]);
        }
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = chooser.showOpenDialog(Gui.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    textField2.setText(file.toString());
                }
            }
        });
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Core.user = textField1.getText();
                Core.password = String.copyValueOf(passwordField1.getPassword());
                Core.file = textField2.getText();
                if (comboBox1.getSelectedIndex() == 3) {
                    Emailsender.SSLEnable = SSLEnabled.isSelected();
                    Emailsender.TLSEnable = TLSEnabled.isSelected();
                }
                setEnterButtonEnable(false);
                if (others) {
                    Core.smtp = textField3.getText();
                    Core.port = textField4.getText();
                    System.out.println(Core.smtp + "   " + Core.port);
                }
                if (Core.user.isEmpty()) {
                    textField1.setText("cannot be empty !");
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            textField1.setText("");
                            setEnterButtonEnable(true);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
                if (Core.password.isEmpty()) {
                    passwordField1.setText("cannot be empty !");
                    passwordField1.setEchoChar((char) 0);
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            passwordField1.setText("");
                            passwordField1.setEchoChar('*');
                            setEnterButtonEnable(true);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
                if (Core.file.isEmpty()) {
                    textField2.setText("cannot be empty !");
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            textField2.setText("");
                            setEnterButtonEnable(true);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
                if (!Core.user.isEmpty() && !Core.password.isEmpty() && !Core.file.isEmpty()) {

                    Runnable r1 = new Runnable() {
                        @Override
                        public void run() {
                            Core core = new Core(gui, indicator);
                            core.send();
                        }
                    };
                    Thread t1 = new Thread(r1, "CoreThread");
                    t1.start();
                }
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel2.setVisible(false);
                setSize(400, 200);
                others = false;
                switch (comboBox1.getSelectedIndex()) {
                    case 0:
                        Core.smtp = "smtp.gmail.com";
                        Core.port = "587";
                        Emailsender.SSLEnable = false;
                        Emailsender.TLSEnable = true;
                        break;
                    case 1:
                        Core.smtp = "smtp.mail.yahoo.com";
                        Core.port = "587";
                        Emailsender.SSLEnable = false;
                        Emailsender.TLSEnable = true;
                        break;
                    case 2:
                        Core.smtp = "smtp-mail.outlook.com";
                        Core.port = "587";
                        Emailsender.SSLEnable = false;
                        Emailsender.TLSEnable = true;
                        break;
                    case 3:
                        setSize(400, 230);
                        panel2.setVisible(true);
                        others = true;
                        break;
                }
            }

        });
    }

    public void setEnterButton(String text) {
        enterButton.setText(text);
    }

    public void setTextField2(String text) {
        textField2.setText(text);
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField2.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void setEnterButtonEnable(boolean enable) {
        if (enable) {
            enterButton.setEnabled(true);
        } else {
            enterButton.setEnabled(false);
        }
    }


}
