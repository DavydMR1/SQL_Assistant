package UIs.adminUIComponents;

import UIs.AdminInterface;

import javax.swing.*;
import java.awt.*;

public class OptionPanel {
    //Дополнительные инструменты
    AdminInterface adminInterface;
    //Привязка основной панели
    public OptionPanel(AdminInterface adminInterface) {
        this.adminInterface = adminInterface;
    }

    //Сборка
    public JPanel optionPanelConfig() {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(
                (int) (adminInterface.getMainFrame().getContentPane().getWidth() * 0.8),
                adminInterface.getMainFrame().getContentPane().getHeight() / 5));
        panel.setSize((int) (adminInterface.getMainFrame().getContentPane().getWidth() * 0.8),
                adminInterface.getMainFrame().getContentPane().getHeight() / 5);
        Dimension componentSize = new Dimension(panel.getWidth() / 5, panel.getHeight() / 2);

        if (adminInterface.getRequestOperator().checkConnection()) {
                JButton saveData = new JButton("Сохранить");
                saveData.addActionListener(e -> adminInterface.getRequestOperator().updateDB(adminInterface.getData()));
                saveData.setPreferredSize(componentSize);
                saveData.setSize(componentSize);
                panel.add(saveData);
                layout.putConstraint(SpringLayout.WEST, panel.getComponent(0), 0, SpringLayout.WEST, panel);
        }
        return panel;
    }
}
