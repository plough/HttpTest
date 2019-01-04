import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by plough on 2019/1/3.
 */
class ControlPane extends JPanel {
    private JTextField urlText;
    private DisplayPane displayPane;
    private JButton requestOnceButton;
    private JButton requestLoopButton;
    private JButton stopLoopButton;
    private Thread loopThread;



    ControlPane(DisplayPane displayPane) {
        assert displayPane != null;

        this.displayPane = displayPane;
        initComponents();
        initListeners();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(20, 50, 5, 0));
        this.add(createUrlPane(), BorderLayout.NORTH);
        this.add(createButtonPane(), BorderLayout.CENTER);
    }

    private JPanel createUrlPane() {
        JPanel urlPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        urlPane.setBorder(new EmptyBorder(0, 10, 0, 0));

        JLabel urlLabel = new JLabel("请求url：");
//        urlLabel.setBounds(10,20,80,25);
        urlPane.add(urlLabel);

        urlText = new JTextField(30);
        urlText.setText("http://httpbin.org/get?a=1&b=2");
//        urlText.setBounds(100,20,500,25);
        urlPane.add(urlText);

        return urlPane;
    }

    private JPanel createButtonPane() {
        JPanel btnPane = new JPanel(new BorderLayout());

        btnPane.add(createOncePane(), BorderLayout.NORTH);
        btnPane.add(createLoopPane(), BorderLayout.CENTER);

        return btnPane;
    }

    private JPanel createOncePane() {
        JPanel oncePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        oncePane.setAlignmentX(FlowLayout.LEFT);
        // 创建请求按钮
        requestOnceButton = new JButton("发送一次请求");
//        requestOnceButton.setBorder(new EmptyBorder(0, 0, 0, 50));
        oncePane.add(requestOnceButton);
        return oncePane;
    }

    private JPanel createLoopPane() {
        JPanel loopPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        requestLoopButton = new JButton("循环发送请求");
        JLabel tip = new JLabel("每秒一次，直到服务器返回值异常");
        loopPane.add(requestLoopButton);
        loopPane.add(tip);

        stopLoopButton = new JButton("停止循环请求");
        loopPane.add(stopLoopButton);
        return loopPane;
    }

    private void initListeners() {
        requestOnceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearRes();
                doHttpGet(urlText.getText());
            }
        });

        requestLoopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loopThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        displayPane.initCounter();
                        while (displayPane.isNormalState()) {
                            clearRes();
                            doHttpGet(urlText.getText());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                                break;
                            }
                            displayPane.addCounter();
                        }
                    }
                });
                loopThread.start();

            }
        });

        stopLoopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loopThread.interrupt();
            }
        });
    }

    private void doHttpGet(String httpUrl) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(httpUrl);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseContent = EntityUtils.toString(entity, "UTF-8");
            log(response.getStatusLine().toString(), responseContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void clearRes() {
        displayPane.logSwing("", "");
    }

    private void log(String header, String content) {
        logConsole(header, content);
        displayPane.logSwing(header, content);
    }

    private void logConsole(String header, String content) {
        System.out.println(header);
        System.out.println(content);
    }
}
