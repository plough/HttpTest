import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by plough on 2019/1/3.
 */
public class HttpTestPane extends JPanel {
//    private DisplayPane displayPane;


    public HttpTestPane() {
        initComponents();
    }
    
    private void initComponents() {
        this.setLayout(new BorderLayout());

        DisplayPane displayPane = new DisplayPane();
        ControlPane controlPane = new ControlPane(displayPane);

        this.add(controlPane, BorderLayout.NORTH);
        this.add(displayPane, BorderLayout.CENTER);
    }
}
