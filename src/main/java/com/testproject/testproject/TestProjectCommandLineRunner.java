package com.testproject.testproject;

import com.testproject.testproject.ui.view.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class TestProjectCommandLineRunner implements CommandLineRunner {
    private final MainView mainView;

    @Autowired
    public TestProjectCommandLineRunner(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void run(String... args) throws Exception {
        //Загрузка графического интерфейса
        EventQueue.invokeLater(() -> mainView.setVisible(true));
    }
}
