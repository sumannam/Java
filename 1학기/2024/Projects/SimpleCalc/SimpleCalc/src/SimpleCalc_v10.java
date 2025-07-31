
import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SimpleCalc_v10 extends JFrame {
    JTextField t1, t2;
    JTextArea area;
    JButton cal, reset;
    JComboBox<String> cb;


    SimpleCalc_v10() {
        setTitle("계산기");

        setLayout(new BorderLayout(10, 10));
        showNorth();
        showCenter();
        showSouth();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 220);
        setVisible(true);
    }

    void showNorth() {
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel panel = new JPanel(new GridLayout(1, 4));


        t1 = new JTextField(5);
        t2 = new JTextField(5);
        // t2.setEnabled(false);

        cal = new JButton("계산");
        cal.setPreferredSize(new Dimension(70, 30));

        p1.add(t1);
        p2.add(t2);
        p3.add(cal);

        panel.add(p1);
        panel.add(p2);
        panel.add(p3);

        add(panel, BorderLayout.NORTH);

        ActionListener listener1 = e -> {
            if (e.getSource() == cal) {
                if (t1.getText().isEmpty())
                    area.setText("");
                else {
                    int num1 = Integer.parseInt(t1.getText());
                    int num2 = Integer.parseInt(t2.getText());
                    int result = num1 + num2;
                    area.setText("" + result);
                }
            }
            else {
                t1.setText("");
                t2.setText("");
                area.setText("");
            }
        };

        cal.addActionListener(listener1);
    }

    void showCenter() {
        JPanel panel = new JPanel();

        area = new JTextArea(30, 20);
        // area.setText("이 영역에 원의 넓이를\n계산하는 과정이 나타납니다.");
        area.setEditable(false);
        area.setForeground(Color.RED);

        panel.add(area);

        add(panel, BorderLayout.CENTER);
    }

    void showSouth() {
        // String[] color = { "red", "blue" };

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // cb = new JComboBox<String>(color);
        // reset = new JButton("리셋");

        // panel.add(cb);
        // panel.add(reset);
        add(panel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new SimpleCalc_v10();
    }
}