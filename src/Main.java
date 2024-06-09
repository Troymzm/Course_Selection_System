import com.student.view.LoginView;

import javax.swing.*;

/**
 * @author mzm
 * @version 1.0
 */
public class Main { public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new LoginView().setVisible(true);
        }
    });}
}
