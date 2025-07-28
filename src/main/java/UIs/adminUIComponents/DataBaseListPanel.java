package UIs.adminUIComponents;

import UIs.AdminInterface;
import models.DBNames;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DataBaseListPanel {
    //Интервейс для выбора бд
    private final AdminInterface adminInterface;

    private Dimension componentSize;

    //Привязка основной панели
    public DataBaseListPanel(AdminInterface adminInterface) {
        this.adminInterface = adminInterface;
    }

    //Основная сборка
    public JScrollPane dataBaseListPanelConfig() {
        JScrollPane pane = new JScrollPane();
        pane.setPreferredSize(new Dimension(adminInterface.getMainFrame()
                .getContentPane().getWidth() / 5, (int) (adminInterface
                .getMainFrame().getContentPane().getHeight() * 0.8)));
        pane.setSize(adminInterface.getMainFrame().getContentPane().getWidth() / 5,
                (int) (adminInterface.getMainFrame().getContentPane().getHeight() * 0.8));

        if (adminInterface.getRequestOperator().checkConnection()) {

            JPanel panel = new JPanel(new CardLayout());

            DBNames dbNames = adminInterface.getRequestOperator().requestDBNames();

            componentSize = new Dimension(pane.getWidth(),
                    pane.getHeight() / 10);

            panel.add("Таблицы (Tables)", makeCard(dbNames.getTables()));
            panel.add("Виды (Views)", makeCard(dbNames.getViews()));
            panel.add("Индексы (Indexes)", makeCard(dbNames.getIndexes()));
            panel.add("Последовательности (Sequences)", makeCard(dbNames.getSequences()));

            pane.setViewportView(panel);

            JComboBox<String> comboBox = new JComboBox<>(
                    new String[] {"Таблицы (Tables)", "Виды (Views)", "Индексы (Indexes)",
                            "Последовательности (Sequences)"});

            comboBox.setEditable(false);
            comboBox.setPreferredSize(componentSize);
            comboBox.setSize(componentSize);
            comboBox.addItemListener(e -> {
                CardLayout layout = (CardLayout) (panel.getLayout());
                layout.show(panel, (String)e.getItem());
            });
            pane.setColumnHeaderView(comboBox);
        }
        return pane;
    }

    //Сборка списков таблиц
    private JPanel makeCard(List<String> data) {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        for (int i = 0; i < data.size(); i++)  {
            JButton button = new JButton(data.get(i));
            button.setPreferredSize(componentSize);
            button.setSize(componentSize);
            button.addActionListener(e -> {
                adminInterface.configureData(button.getText());
                adminInterface.placeDataBasePanel();
                adminInterface.placeOptionPanel();
                adminInterface.update();
            });
            panel.add(button);
            if (i == 0) {
                layout.putConstraint(SpringLayout.NORTH, panel.getComponent(0), 0, SpringLayout.NORTH, panel);
            }
            else {
                layout.putConstraint(SpringLayout.NORTH, panel.getComponent(i),
                        0, SpringLayout.SOUTH, panel.getComponent(i - 1));
            }
        }
        return panel;
    }
}
