package Project.client.views;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ContainerEvent;


import Project.client.ClientUtils;
import Project.client.ICardControls;
import Project.common.MyLogger;
import javax.swing.text.Highlighter;
import javax.swing.text.DefaultHighlighter;

public class UserListPanel extends JPanel {
    JPanel userListArea;
    JPanel wrapper;
    private static MyLogger logger = MyLogger.getLogger(UserListPanel.class.getName());

    public UserListPanel(ICardControls controls) {
        super(new BorderLayout(10, 10));
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        // wraps a viewport to provide scroll capabilities
        JScrollPane scroll = new JScrollPane(content);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        // scroll.setBorder(BorderFactory.createEmptyBorder());
        // no need to add content specifically because scroll wraps it

        userListArea = content;
        this.wrapper = wrapper;
        wrapper.add(scroll);
        this.add(wrapper, BorderLayout.CENTER);

        userListArea.addContainerListener(new ContainerListener() {

            @Override
            public void componentAdded(ContainerEvent e) {
                if (userListArea.isVisible()) {
                    userListArea.revalidate();
                    userListArea.repaint();
                }
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                if (userListArea.isVisible()) {
                    userListArea.revalidate();
                    userListArea.repaint();
                }
            }

        });
    }
    
    


    protected void resizeUserListItems() {
        for (Component p : userListArea.getComponents()) {
            if (p.isVisible()) {
                p.setPreferredSize(
                        new Dimension(wrapper.getWidth(), ClientUtils.calcHeightForText(this,
                                ((JEditorPane) p).getText(), wrapper.getWidth())));
                p.setMaximumSize(p.getPreferredSize());
            }
        }
        userListArea.revalidate();
        userListArea.repaint();
    }

    protected void addUserListItem(long clientId, String clientName) {
        logger.info("Adding user to list: " + clientName);
        JPanel content = userListArea;
        logger.info("Userlist: " + content.getSize());
        

        /* 
        for (Component p : userListArea.getComponents()) {
             
            JEditorPane children = (JEditorPane)p;
            String s = children.getText();
            if(s.equals(clientName)) {
                clientName = "<b>" + clientName + "</b>"; 
            }
        }
        */
        JEditorPane textContainer = new JEditorPane("text/html", clientName);
        //textContainer.setHighlighter(highlighter);
        
        textContainer.setName(clientId + "");
        // sizes the panel to attempt to take up the width of the container
        // and expand in height based on word wrapping
        textContainer.setLayout(null);
        textContainer.setPreferredSize(
                new Dimension(content.getWidth(), ClientUtils.calcHeightForText(this, clientName, content.getWidth())));
        textContainer.setMaximumSize(textContainer.getPreferredSize());
        textContainer.setEditable(false);
        // remove background and border (comment these out to see what it looks like
        // otherwise)
        ClientUtils.clearBackground(textContainer);
        // add to container
        content.add(textContainer);
    }

    protected void removeUserListItem(long clientId) {
        logger.info("removing user list item for id " + clientId);
        Component[] cs = userListArea.getComponents();
        for (Component c : cs) {
            if (c.getName().equals(clientId + "")) {
                userListArea.remove(c);
                break;
            }
        }
    }

    protected void clearUserList() {
        Component[] cs = userListArea.getComponents();
        for (Component c : cs) {
            userListArea.remove(c);
        }
    }
}