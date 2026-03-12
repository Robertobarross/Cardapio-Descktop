package com.cardapio;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.cardapio.model.Food;

import java.util.List;

public class Main extends Application {

    private TableView<Food> table = new TableView<>();

    @Override
    public void start(Stage stage) {

        TableColumn<Food, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Food, String> colName = new TableColumn<>("Nome");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Food, Double> colPrice = new TableColumn<>("Preço");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().add(colId);
        table.getColumns().add(colName);
        table.getColumns().add(colPrice);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Button btn = new Button("Carregar Cardápio");

        btn.setOnAction(e -> {

            List<Food> foods = ApiService.getFoods();

            if (foods != null) {
                table.getItems().clear();
                table.getItems().addAll(foods);
            }

        });

        VBox root = new VBox(10);

        root.getChildren().add(btn);
        root.getChildren().add(table);

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Cardápio Desktop");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}