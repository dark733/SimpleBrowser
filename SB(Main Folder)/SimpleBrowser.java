import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;

public class SimpleBrowser extends JFrame {
    private JTextField addressBar;
    private JButton searchButton;
    private JEditorPane display;
    private JProgressBar progressBar;

    public SimpleBrowser() {
        super("Simple Browser");

        addressBar = new JTextField();
        addressBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                loadPage(event.getActionCommand());
            }
        });

        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                loadPage(addressBar.getText());
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(addressBar, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        display = new JEditorPane();
        display.setEditable(false);
        display.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent event) {
                if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    loadPage(event.getURL().toString());
                }
            }
        });
        add(new JScrollPane(display), BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(false); // Remove "%" text
        progressBar.setValue(-1); // Initial value set to -1
        Dimension prefSize = progressBar.getPreferredSize();
        prefSize.height = 30; // Increase height
        progressBar.setPreferredSize(prefSize);
        add(progressBar, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadPage(String url) {
        try {
            progressBar.setValue(-1); // Reset progress bar
            if (url.startsWith("file://")) {
                display.setPage(url);
                addressBar.setText(url);
            } else {
                display.setPage(new URL(url));
                addressBar.setText(url);
            }
            progressBar.setValue(100); // Set progress bar to 100% when loading is complete
            // Reset progress bar to 0% after a short delay (1000ms)
            Timer timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    progressBar.setValue(0);
                }
            });
            timer.setRepeats(false);
            timer.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading page: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SimpleBrowser();
            }
        });
    }
}
