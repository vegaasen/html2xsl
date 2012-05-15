package com.telenor.xsl.elements.gui.view;

import com.telenor.xsl.elements.gui.model.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.telenor.xsl.elements.util.DocumentUtilities.verifyNotNull;

/**
 * Simple GUI for the application.
 *
 * Please note: I _totally_ suck in Swing GUI programming, as I'm mainly a web-developer. If you find some strange stuff
 * in here, please let me know, and I'll try to reflect on the suggestions submitted!
 *
 * @author vegaasen
 * @since 1.0-SNAPSHOT
 * @version 0.01
 */
public class ParserGui extends JFrame implements ActionListener {

    private static final int DEFAULT_GRID_WIDTH = 1;
    private static final Insets DEFAULT_INSETS = new Insets(5, 5, 5, 5);
    
    private Size w_size;
    private String w_title;

    public ParserGui() {}

    public void openGui() {
        setSize(getW_size().getWidth(), getW_size().getHeight());
        setTitle(getW_title());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setResizable(true);
        
        generateTopArea_comp(this);

        //pack();
        setVisible(true);
    }

    private void generateTopArea_comp(final Container container) {
        if(verifyNotNull(container)) {
            JLabel lblFileLocation, lblUrlLocation, lblHeaderDetails;
            JTextField txtFileLocation, txtUrlLocation;
            GridBagConstraints gbConstraints = new GridBagConstraints();

            gbConstraints.gridx = 0;
            gbConstraints.gridy = 0;
            gbConstraints.gridwidth = DEFAULT_GRID_WIDTH;
            gbConstraints.fill = GridBagConstraints.NONE;
            gbConstraints.insets = DEFAULT_INSETS;
            lblFileLocation = new JLabel("From file:");
            container.add(lblFileLocation, gbConstraints);

            gbConstraints.gridx = 1;
            gbConstraints.gridy = 0;
            gbConstraints.gridwidth = 1;
            gbConstraints.fill = GridBagConstraints.BOTH;
            gbConstraints.insets = DEFAULT_INSETS;
            txtFileLocation = new JTextField("C:\\", 10);
            container.add(txtFileLocation, gbConstraints);

            gbConstraints.gridx = 2;
            gbConstraints.gridy = 0;
            gbConstraints.gridwidth = DEFAULT_GRID_WIDTH;
            gbConstraints.fill = GridBagConstraints.NONE;
            gbConstraints.insets = DEFAULT_INSETS;
            lblUrlLocation = new JLabel("From url:");
            container.add(lblUrlLocation, gbConstraints);

            gbConstraints.gridx = 3;
            gbConstraints.gridy = 0;
            gbConstraints.gridwidth = 1;
            gbConstraints.fill = GridBagConstraints.BOTH;
            gbConstraints.insets = DEFAULT_INSETS;
            txtUrlLocation = new JTextField("http://", 10);
            container.add(txtUrlLocation, gbConstraints);

            
        }
    }

    private static void generateInputOutputArea_comp(final Container container) {

    }

    private static void generateBottomArea_comp(final Container container) {
        
    }

    public void actionPerformed(ActionEvent e) {}

    public Size getW_size() {
        return w_size;
    }

    public void setW_size(Size w_size) {
        this.w_size = w_size;
    }

    public String getW_title() {
        return w_title;
    }

    public void setW_title(String w_title) {
        this.w_title = w_title;
    }
}
