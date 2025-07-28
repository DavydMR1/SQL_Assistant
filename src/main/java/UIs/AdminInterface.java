package UIs;

import UIs.adminUIComponents.ControlPanel;
import UIs.adminUIComponents.DataBaseListPanel;
import UIs.adminUIComponents.DataBasePanel;
import UIs.adminUIComponents.OptionPanel;
import lombok.Getter;
import lombok.Setter;
import models.Table;
import operators.RequestOperator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AdminInterface {
    //Основная рабочая панель
    @Getter
    @Setter
    private RequestOperator requestOperator;

    @Getter
    private final JFrame mainFrame = new JFrame();
    private final SpringLayout layout = new SpringLayout();

    @Getter
    private final List<String> dataTypes = new ArrayList<>();
    @Getter
    private Table data = new Table();
    @Getter
    private Dimension screenSize = new Dimension();

    private final ControlPanel controlPanel = new ControlPanel(this);
    private final DataBaseListPanel dataBaseListPanel = new DataBaseListPanel(this);
    private final DataBasePanel dataBasePanel = new DataBasePanel(this);
    private final OptionPanel optionPanel = new OptionPanel(this);

    //Конфигуратор типов данных
    //(Списко типов данных временный)
    public AdminInterface() {
        for (JDBCType type : JDBCType.values()) {
            dataTypes.add(type.getName());
        }
    }

    //Основной сборщик
    //(Ломается при повторном вызове)
    public void configureAdminInterface() {
        mainFrameConfig();
        placeControlPanel();
        placeDataBaseListPanel();
        placeDataBasePanel();
        placeOptionPanel();
        update();
//        mainFrame.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                placeControlPanel();
//                placeDataBaseListPanel();
//                placeDataBasePanel();
//                placeOptionPanel();
//                update();
//            }
//        });
    }

    //Сборщик окна
    //(Измение размера временно отключено из-за ошибок)
    private void mainFrameConfig() {
        mainFrame.setLocation(0,0);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(layout);

        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setAlwaysOnTop(false);

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setPreferredSize(new Dimension((int) screenSize.getWidth() / 2,
                (int) screenSize.getHeight() / 2));
        mainFrame.setMaximumSize(screenSize);
        mainFrame.setMinimumSize(new Dimension((int) screenSize.getWidth() / 4,
                (int) screenSize.getHeight() / 4));
        mainFrame.setSize((int) screenSize.getWidth() / 2,
                (int) screenSize.getHeight() / 2);
        update();
    }

    //От этого и до след. комментария - конфигураторы панелей
    public void placeControlPanel() {
        if (mainFrame.getContentPane().getComponentCount() > 0) {
            mainFrame.getContentPane().remove(0);
        }
        mainFrame.add(controlPanel.controlPanelConfig(), 0);
        layout.putConstraint(SpringLayout.WEST, mainFrame.getContentPane().getComponent(0),
                0, SpringLayout.WEST, mainFrame.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, mainFrame.getContentPane().getComponent(0),
                0, SpringLayout.NORTH, mainFrame.getContentPane());
    }

    public void placeDataBaseListPanel() {
        if (mainFrame.getContentPane().getComponentCount() > 1) {
            mainFrame.getContentPane().remove(1);
        }
        mainFrame.add(dataBaseListPanel.dataBaseListPanelConfig(), 1);
        layout.putConstraint(SpringLayout.WEST, mainFrame.getContentPane().getComponent(1),
                0, SpringLayout.WEST, mainFrame.getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, mainFrame.getContentPane().getComponent(1),
                0, SpringLayout.SOUTH, mainFrame.getContentPane());
    }

    public void placeDataBasePanel() {
        if (mainFrame.getContentPane().getComponentCount() > 2) {
            mainFrame.getContentPane().remove(2);
        }
        mainFrame.add(dataBasePanel.dataBasePanelConfig(), 2);
        layout.putConstraint(SpringLayout.EAST, mainFrame.getContentPane().getComponent(2),
                0, SpringLayout.EAST, mainFrame.getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, mainFrame.getContentPane().getComponent(2),
                0, SpringLayout.SOUTH, mainFrame.getContentPane());
    }

    public void placeOptionPanel() {
        if (mainFrame.getContentPane().getComponentCount() > 3) {
            mainFrame.getContentPane().remove(3);
        }
        mainFrame.add(optionPanel.optionPanelConfig(), 3);
        layout.putConstraint(SpringLayout.EAST, mainFrame.getContentPane().getComponent(3),
                0, SpringLayout.EAST, mainFrame.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, mainFrame.getContentPane().getComponent(3),
                0, SpringLayout.NORTH, mainFrame.getContentPane());
    }

    //Перерисовщик
    public void update() {
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.pack();
    }

    //Поиск типов данных
    public String findType(String type) {
        for (String dt : dataTypes) {
            if (Objects.equals(dt, type)) {
                return dt;
            }
        }
        return "NULL";
    }

    //запрос таблицы
    public void configureData(String title) {
        data = requestOperator.getDB(title);
    }
}
