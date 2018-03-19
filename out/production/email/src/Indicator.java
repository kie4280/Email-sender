import javax.swing.*;
import java.awt.event.*;

public class Indicator extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JProgressBar progressBar1;
    public JLabel label1;

    public Indicator() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);
        setSize(200, 150);
        setLocationRelativeTo(null);
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
// call onCancel() when cross is clicked
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
// add your code here if necessary
        Core.cancel = true;
        dispose();
    }

    public void update(int done, int all) {

        progressBar1.setValue((int) (done * 100 / all));
        label1.setText(done + " / " + all);
    }
    /*public static void main(String[] args) {
        Indicator indicator = new Indicator();
        indicator.update();
        indicator.setVisible(true);
    }*/

}
