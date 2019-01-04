import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * Created by plough on 2019/1/3.
 */
class DisplayPane extends JPanel {
    private JLabel resHeaderText;
    private JTextArea resContentText;
    private boolean normalState = true;
    private JLabel counterLabel;
    private JLabel maxWaitingSecondsLabel;
    private int maxWaitingSeconds = 0;
    private int counter = 0; // 连续请求次数

    DisplayPane() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.add(createShowHeaderPane(), BorderLayout.NORTH);
        this.add(createShowContentPane(), BorderLayout.CENTER);
    }

    private JPanel createShowHeaderPane() {
        JPanel headerPane = new JPanel();

        maxWaitingSecondsLabel = new JLabel("最长等待时间：" + maxWaitingSeconds + "s");
        headerPane.add(maxWaitingSecondsLabel);

        counterLabel = new JLabel("已连续发送" + counter + "次请求");
        counterLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        headerPane.add(counterLabel);
        counterLabel.setVisible(false);



        final JLabel resHeaderLabel = new JLabel("服务器响应头：");
        headerPane.add(resHeaderLabel);

        resHeaderText = new JLabel();
        headerPane.add(resHeaderText);

        return headerPane;
    }

    private JPanel createShowContentPane() {
        JPanel contentPane = new JPanel();

        final JLabel resContentLabel = new JLabel("服务器返回内容：");
//        resContentLabel.setBounds(10,220,120,25);
        contentPane.add(resContentLabel);


        resContentText = new JTextArea(25, 50);
        resContentText.setLineWrap(true);
//        resContentText.setBounds(120,220,465,225);
        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setBounds(120,220,465,225);
        scrollPane.setViewportView(resContentText);
        contentPane.add(scrollPane);

        return contentPane;
    }

    void addCounter() {
        counter ++;
        counterLabel.setText("已连续发送" + counter + "次请求");
        if (!counterLabel.isVisible()) {
            counterLabel.setVisible(true);
        }

        maxWaitingSecondsLabel.setText("最长等待时间：" + maxWaitingSeconds + "s");
    }

    void initCounter() {
        counter = 0;
        if (counterLabel.isVisible()) {
            counterLabel.setVisible(false);
        }
    }

    void updateMaxWaitingSeconds(int waitingSeconds) {
        if (waitingSeconds > maxWaitingSeconds) {
            this.maxWaitingSeconds = waitingSeconds;
        }
    }


    void logSwing(String header, String content) {
        this.resHeaderText.setText(header);
        this.resContentText.setText(content);

        checkException(header);
    }

    boolean isNormalState() {
        return normalState;
    }

    private void checkException(String header) {
        normalState = header.contains("200 OK");
        this.resHeaderText.setForeground(normalState ? Color.BLACK : Color.RED);
    }
}
