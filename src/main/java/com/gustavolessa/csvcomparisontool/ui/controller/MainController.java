package com.gustavolessa.csvcomparisontool.ui.controller;

import com.gustavolessa.csvcomparisontool.services.AppRunner;
import com.gustavolessa.csvcomparisontool.services.ArgsHandler;
import com.gustavolessa.csvcomparisontool.services.FileTypeFilter;
import com.gustavolessa.csvcomparisontool.ui.view.MainWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController extends JFrame {

    private final ArgsHandler argsHandler;

    private final AppRunner appRunner;

    private final MainWindow view;

    @Autowired
    public MainController(ArgsHandler argsHandler, AppRunner appRunner) {

        super("CSV Comparison Tool");
        this.argsHandler = argsHandler;
        this.appRunner = appRunner;

        this.view = new MainWindow() {
            @Override
            protected void keysFocusGained() {
                if (getKeyColumnsTextField().getText().equals(MainWindow.keysPlaceholder)) {
                    getKeyColumnsTextField().setText("");
                    getKeyColumnsTextField().setForeground(Color.BLACK);
                }
            }

            @Override
            protected void keysFocusLost() {
                if (getKeyColumnsTextField().getText().isEmpty()) {
                    getKeyColumnsTextField().setForeground(Color.GRAY);
                    getKeyColumnsTextField().setText(MainWindow.keysPlaceholder);
                }
            }

            @Override
            protected void runClicked() {
                readFields();
                String savedFile = appRunner.startFromUI();
                if (!savedFile.isEmpty())
                    showAlert("Success! Output file: " + savedFile);
                else
                    showAlert("Failed!");
            }

            @Override
            protected void chooseFolderClicked() {
                chooseFolder();
            }

            @Override
            protected void chooseFileClicked() {
                chooseFile();
            }

        };
        view.getKeyColumnsTextField().setForeground(Color.GRAY);
        view.getKeyColumnsTextField().setText(MainWindow.keysPlaceholder);
        this.setContentPane(view.$$$getRootComponent$$$());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.pack();
    }

    private void showAlert(String s) {
        JOptionPane.showMessageDialog(this, s);
    }


    private void readFields() {
        String systemColumnId = view.getSystemColumnTextField().getText();
        List<String> datasetOptions = Arrays.asList(view.getSystemOptionsTextField().getText().split(","));
        List<String> columnsToCompare = Arrays.asList(view.getColumnsToCompareTextField().getText().split(","));
        List<List<String>> keyColumns = new ArrayList<>();
        List<String> keys = Arrays.asList(view.getKeyColumnsTextField().getText().split("-"));
        keys.forEach(k -> {
            k.trim();
            if (!k.isEmpty()) keyColumns.add(Arrays.asList(k.split(",")));
        });
//        keyColumns.add(Arrays.asList(view.getKeyColumnsTextField().getText().split(",")));
//        keyColumns.add(Arrays.asList(view.getKeyColumnsTextField2().getText().split(",")));
        String dest = view.getDestinationTextField().getText();
        String src = view.getSourceTextField().getText();

        argsHandler.setSystemColumnId(systemColumnId);
        argsHandler.setDatasetOptions(datasetOptions);
        argsHandler.setColumnsToCompare(columnsToCompare);
        argsHandler.setKeyColumns(keyColumns);
        argsHandler.setDest(dest);
        argsHandler.setSrc(src);
    }

    private void chooseFolder() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                int returnValue = jfc.showOpenDialog(null);
        int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            view.getDestinationTextField().setText(selectedFile.getAbsolutePath());
            System.out.println(selectedFile.getAbsolutePath());
        }
    }

    private void chooseFile() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.addChoosableFileFilter(new FileTypeFilter(".csv", "Comma-Separated Values"));
        jfc.setAcceptAllFileFilterUsed(false);
        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            view.getSourceTextField().setText(selectedFile.getAbsolutePath());
            System.out.println(selectedFile.getAbsolutePath());
        }
    }

}
