package Moduls.Skladnik.ui.graphics;

import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author Ondřej Bleha
 */
public class ConsoleFrame extends JFrame{
    private JScrollPane jScrollPane1;
    private JTextPane console;
    
    public ConsoleFrame(JTextPane console){
        initComponents(console);
        
        this.setTitle("Skladník - console");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/ui/graphics/icons/console.png")));
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
    }
    
    
    private void initComponents(JTextPane console){
        jScrollPane1 = new javax.swing.JScrollPane();
        this.console = console;

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        this.console.setEditable(false);
        this.console.setBackground(new java.awt.Color(42, 42, 42));
        this.console.setFocusable(false);
        jScrollPane1.setViewportView(this.console);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
        );

        pack();
    }
}
