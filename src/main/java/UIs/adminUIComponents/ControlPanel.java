package UIs.adminUIComponents;

import UIs.AdminInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.BatchUpdateException;

public class ControlPanel {
    //Интерфейс для подключения к бд
    AdminInterface adminInterface;

    //Привязка основной панели
    public ControlPanel(AdminInterface adminInterface) {
        this.adminInterface = adminInterface;
    }

    //Сборка
    public JPanel controlPanelConfig() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(
                adminInterface.getMainFrame().getContentPane().getWidth() / 5,
                adminInterface.getMainFrame().getContentPane().getHeight() / 5));
        panel.setSize(adminInterface.getMainFrame().getContentPane().getWidth() / 5,
                adminInterface.getMainFrame().getContentPane().getHeight() / 5);
        panel.setLocation(0, 0);
        panel.setLayout(new GridLayout(2, 1));

        JButton btnStart = new JButton("Старт");
        btnStart.addActionListener(e -> {
            JDialog setup = new JDialog(adminInterface.getMainFrame(), "Настройка подключения", true);
            setup.setSize((int) (adminInterface.getScreenSize().getWidth() / 5),
                    (int) (adminInterface.getScreenSize().getHeight() / 5));
            setup.setPreferredSize(setup.getSize());
            setup.setLayout(new GridLayout(5, 1));

            String[] drivers = new String[]{"sqlite"};
            JComboBox<String> comboBox = new JComboBox<>(drivers);
            setup.add(comboBox);

            JTextField url = new JTextField();
            url.setText("URL");
            setup.add(url);

            JTextField name = new JTextField();
            name.setText("ИМЯ");
            setup.add(name);

            JTextField password = new JTextField();
            password.setText("ПАРОЛЬ");
            setup.add(password);

            JButton button = new JButton("ПОДКЛЮЧИТЬСЯ");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    adminInterface.getRequestOperator().createConnection(
                            name.getText(), password.getText(), url.getText(), (String) comboBox.getSelectedItem());
                    adminInterface.placeDataBaseListPanel();
                    adminInterface.update();
                    setup.dispose();
                }
            });
            setup.add(button);

            setup.setLocationRelativeTo(adminInterface.getMainFrame());
            setup.setVisible(true);
        });

        JButton btnStop = new JButton("Стоп");
//        btnStop.addActionListener(e -> SpringApplication.exit(adminInterface
//                .getSpring().context()));

        panel.add(btnStart);
        panel.add(btnStop);
        return panel;
    }
}
