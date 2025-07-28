package UIs.adminUIComponents;

import UIs.AdminInterface;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBasePanel {
    //Интерфейс для таблицы
    private final AdminInterface adminInterface;

    private Dimension componentSize = new Dimension();

    //Привязка основной панели
    public DataBasePanel(AdminInterface adminInterface) {
        this.adminInterface = adminInterface;
    }

    //Основная сборка
    public JScrollPane dataBasePanelConfig() {
        JScrollPane pane = new JScrollPane();
        pane.setPreferredSize(new Dimension (
                (int) (adminInterface.getMainFrame().getContentPane().getWidth() * 0.8),
                (int) (adminInterface.getMainFrame().getContentPane().getHeight() * 0.8)));
        pane.setSize((int) (adminInterface.getMainFrame().getContentPane().getWidth() * 0.8),
                (int) (adminInterface.getMainFrame().getContentPane().getHeight() * 0.8));

        componentSize = new Dimension(pane.getWidth() / 5, pane.getHeight() / 10);

        if (adminInterface.getRequestOperator().checkConnection()) {
                pane.setViewportView(mainPanel());
                pane.setColumnHeaderView(columnOptions());
                pane.setRowHeaderView(rowOptions());
        }
        return pane;
    }

    //Сборка таблицы
    private JPanel mainPanel() {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setSize((int) (componentSize.getWidth() * adminInterface.getData().getDataTypes().size()),
                (int) (componentSize.getHeight() * adminInterface.getData().getTableData().size()));
        panel.setPreferredSize(panel.getSize());

        for (int i = 0; i < adminInterface.getData().getTableData().size(); i++) {
            for (int j = 0; j < adminInterface.getData().getDataTypes().size(); j++) {
                JFormattedTextField field;
                field = new JFormattedTextField(String.valueOf(adminInterface
                        .getData().getTableData().get(i).get(j)));
                field.getDocument().addDocumentListener(tableDocumentListenerMaker(field, j, i));
                field.setPreferredSize(componentSize);
                field.setSize(componentSize);
                panel.add(field);

                if (j == 0) {
                    if (i == 0) {
                        layout.putConstraint(SpringLayout.NORTH, panel.getComponent(0),
                                0, SpringLayout.NORTH, panel);
                        layout.putConstraint(SpringLayout.WEST, panel.getComponent(0),
                                0, SpringLayout.WEST, panel);
                    }
                    else {
                        layout.putConstraint(SpringLayout.NORTH,
                                panel.getComponent(i * adminInterface.getData().getDataTypes().size()),
                                0, SpringLayout.SOUTH,
                                panel.getComponent((i - 1) * adminInterface.getData().getDataTypes().size()));
                    }
                }
                else {
                    layout.putConstraint(SpringLayout.WEST,
                            panel.getComponent(i * adminInterface.getData().getDataTypes().size() + j),
                            0, SpringLayout.EAST,
                            panel.getComponent(i * adminInterface.getData().getDataTypes().size() + j - 1));

                    layout.putConstraint(SpringLayout.VERTICAL_CENTER,
                            panel.getComponent(i * adminInterface.getData().getDataTypes().size() + j),
                            0, SpringLayout.VERTICAL_CENTER,
                            panel.getComponent(i * adminInterface.getData().getDataTypes().size() + j - 1));
                }
//                if (i == 0) {
//                    if (j == 0) {
//                        layout.putConstraint(SpringLayout.NORTH, panel.getComponent(0),
//                                0, SpringLayout.NORTH, panel);
//                        layout.putConstraint(SpringLayout.WEST, panel.getComponent(0),
//                                0, SpringLayout.WEST, panel);
//                    }
//                    else {
//                        layout.putConstraint(SpringLayout.WEST, panel.getComponent(j),
//                                0, SpringLayout.EAST, panel.getComponent(j - 1));
//                    }
//                }
//                else {
//                    layout.putConstraint(SpringLayout.NORTH,
//                            panel.getComponent(i * adminInterface.getData().getDataTypes().size() + j),
//                            0, SpringLayout.SOUTH,
//                            panel.getComponent(i * (adminInterface.getData().getDataTypes().size() - 1) + j));
//                    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
//                            panel.getComponent(i * adminInterface.getData().getDataTypes().size() + j),
//                            0, SpringLayout.HORIZONTAL_CENTER,
//                            panel.getComponent(i * (adminInterface.getData().getDataTypes().size() - 1) + j));
//                }
            }
        }
        return panel;
    }

    //Сборка инструментов бля работы с колонками
    private JPanel columnOptions() {
        String[] dataTypeDesc = adminInterface.getDataTypes().toArray(
                s -> new String[adminInterface.getDataTypes().size()]);
        int compCount = 4;

        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setSize((int) componentSize.getWidth()
                * adminInterface.getData().getRowTitles().size(), (int) (componentSize.getHeight() * 2.5));
        panel.setPreferredSize(panel.getSize());

        panel.add(makeAddColumnButton(0, new Dimension(
                (int) (componentSize.getWidth() / 4), (int) (componentSize.getHeight() / 2))));
        layout.putConstraint(SpringLayout.WEST, panel.getComponent(0), 0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, panel.getComponent(0), 0, SpringLayout.NORTH, panel);

        for (int i = 0; i < adminInterface.getData().getDataTypes().size(); i++) {
            JButton button = new JButton("-");
            button.setSize((int) (componentSize.getWidth() / 2), (int) (componentSize.getHeight() / 2));
            button.setPreferredSize(button.getSize());
            button.setMargin(new Insets(0, 0, 0, 0));
            int finalI = i;
            button.addActionListener(e -> {
                adminInterface.getData().removeColumn(finalI);
                adminInterface.placeDataBasePanel();
                adminInterface.update();
            });
            panel.add(button);
            if (i == 0) {
                layout.putConstraint(SpringLayout.WEST, panel.getComponent(1),
                        0, SpringLayout.EAST, panel.getComponent(0));
            }
            else {
                layout.putConstraint(SpringLayout.WEST, panel.getComponent(i * compCount + 1),
                        0, SpringLayout.EAST, panel.getComponent(i * compCount - 2));
            }

            if (i == adminInterface.getData().getDataTypes().size() - 1) {
                panel.add(makeAddColumnButton(i + 1, new Dimension((int) (componentSize.getWidth() / 4),
                        (int) (componentSize.getHeight() / 2))));
            }
            else {
                panel.add(makeAddColumnButton(i + 1, new Dimension((int) (componentSize.getWidth() / 2),
                        (int) (componentSize.getHeight() / 2))));
            }
            layout.putConstraint(SpringLayout.WEST, panel.getComponent(i * compCount + 2),
                    0, SpringLayout.EAST, panel.getComponent(i * compCount + 1));

            JComboBox<String> comboBox = new JComboBox<>(dataTypeDesc);
            comboBox.setPreferredSize(componentSize);
            comboBox.setSize(componentSize);
            comboBox.setSelectedItem(adminInterface.findType(adminInterface
                    .getData().getDataTypes().get(i)));
            comboBox.addActionListener(typeActionListenerMaker(comboBox, i));
            panel.add(comboBox);
            layout.putConstraint(SpringLayout.NORTH, panel.getComponent(i * compCount + 3),
                    0, SpringLayout.SOUTH, panel.getComponent(i * compCount + 1));
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, panel.getComponent(i * compCount + 3),
                    0, SpringLayout.HORIZONTAL_CENTER, panel.getComponent(i * compCount + 1));

            JFormattedTextField field;
            field = new JFormattedTextField(adminInterface.getData().getRowTitles().get(i));
            field.getDocument().addDocumentListener(titleDocumentListenerMaker(field, i));
            field.setPreferredSize(componentSize);
            field.setSize(componentSize);
            field.setMargin(new Insets(0, 0, 0, 0));
            panel.add(field);
            layout.putConstraint(SpringLayout.NORTH, panel.getComponent(i * compCount + 4),
                    0, SpringLayout.SOUTH, panel.getComponent(i * compCount + 3));
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, panel.getComponent(i * compCount + 4),
                    0, SpringLayout.HORIZONTAL_CENTER, panel.getComponent(i * compCount + 3));
        }

        return panel;
    }

    //Сборщик кнопки вынесен из-за разных настроек
    private JButton makeAddColumnButton(int column, Dimension size) {
        JButton button = new JButton("+");
        button.setPreferredSize(size);
        button.setSize(size);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.addActionListener(e -> {
            adminInterface.getData().addColumn(column);
            adminInterface.placeDataBasePanel();
            adminInterface.update();
        });

        return button;
    }

    //Сборка инструментов бля работы со строками
    private JPanel rowOptions() {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setSize((int) (componentSize.getHeight() / 2),
                (int) (componentSize.getHeight() * (adminInterface.getData().getTableData().size() + 2)));
        panel.setPreferredSize(panel.getSize());

        panel.add(makeAddRowButton(adminInterface.getData().getTableData().size(), new Dimension(
                (int) (componentSize.getHeight() / 2), (int) (componentSize.getHeight() / 4))));
        layout.putConstraint(SpringLayout.WEST, panel.getComponent(0),
                0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, panel.getComponent(0),
                0, SpringLayout.NORTH, panel);

        for (int i = 0; i < adminInterface.getData().getTableData().size(); i++) {
            JButton button = new JButton("-");
            button.setPreferredSize(new Dimension(
                    (int) (componentSize.getHeight() / 2), (int) componentSize.getHeight() / 2));
            button.setSize((int) (componentSize.getHeight() / 2), (int) componentSize.getHeight() / 2);
            button.setMargin(new Insets(0, 0, 0, 0));
            int finalI = i;
            button.addActionListener(e -> {
                adminInterface.getData().getTableData().remove(finalI);
                adminInterface.placeDataBasePanel();
                adminInterface.update();
            });
            panel.add(button);
            layout.putConstraint(SpringLayout.NORTH, panel.getComponent(i * 2 + 1),
                    0, SpringLayout.SOUTH, panel.getComponent(i * 2));

            if (i == adminInterface.getData().getTableData().size() - 1) {
                panel.add(makeAddRowButton(i, new Dimension(
                        (int) (componentSize.getHeight() / 2), (int) (componentSize.getHeight() / 4))));
            }
            else {
                panel.add(makeAddRowButton(i, new Dimension(
                        (int) (componentSize.getHeight() / 2), (int) (componentSize.getHeight() / 2))));
            }
            layout.putConstraint(SpringLayout.NORTH, panel.getComponent(i * 2 + 2),
                    0, SpringLayout.SOUTH, panel.getComponent(i * 2 + 1));
        }
        return panel;
    }


    //Сборщик кнопки вынесен из-за разных настроек
    private JButton makeAddRowButton(int row, Dimension size) {

        JButton button = new JButton("+");
        button.setPreferredSize(new Dimension(size));
        button.setSize(size);
        button.setMargin(new Insets(0, 0, 0, 0));

        List<Object> tr = new ArrayList<>();
        for (String s : adminInterface.getData().getDataTypes()) {
            if (Objects.equals(s, "DATE")) {
                tr.add("1111-11-11");
            }
            else {
                tr.add(null);
            }
        }

        button.addActionListener(e -> {
            adminInterface.getData().getTableData().add(row, tr);
            adminInterface.placeDataBasePanel();
            adminInterface.update();
        });

        return button;
    }

    //Всё, что ниже нужно бля обновления Table в реальном времени
    //(Наверное надо сдо сделать обновление только на момент отправки запроса)
    private ActionListener typeActionListenerMaker(JComboBox<String> box, int x) {
        return e -> updateTypes((String) box.getSelectedItem(), x);
    }

    private DocumentListener titleDocumentListenerMaker(JFormattedTextField field, int x) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTitles(field.getText(), x);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTitles(field.getText(), x);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTitles(field.getText(), x);
            }
        };
    }

    private DocumentListener tableDocumentListenerMaker(JFormattedTextField field, int x, int y) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTable(field.getText(), x, y);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTable(field.getText(), x, y);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTable(field.getText(), x, y);
            }
        };
    }

    private void updateTypes(String text, int x) {
        adminInterface.getData().getDataTypes().set(x, adminInterface.findType(text));
    }

    private void updateTitles(String text, int x) {
        adminInterface.getData().getRowTitles().set(x, text);
    }

    private void updateTable(String text, int x, int y) {
        adminInterface.getData().getTableData().get(y).set(x, text);
    }
}
