package com.cardapio;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import com.cardapio.model.Food;

import java.util.List;

public class Main extends Application {

    private TableView<Food> table = new TableView<>();

    @Override
    public void start(Stage stage) {

        // BUSCA
        TextField searchField = new TextField();
        searchField.setPromptText("Pesquisar comida...");

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {

            List<Food> foods = ApiService.getFoods();

            List<Food> filtered = foods.stream()
                    .filter(f -> f.getName().toLowerCase()
                    .contains(newValue.toLowerCase()))
                    .toList();

            table.getItems().setAll(filtered);

        });

        // CAMPOS
        TextField nameField = new TextField();
        nameField.setPromptText("Cadastrar comida");

        TextField priceField = new TextField();
        priceField.setPromptText("Preço");

        // BOTÃO ADICIONAR
        Button addButton = new Button("Adicionar");

        addButton.setOnAction(e -> {

            try {

                String name = nameField.getText();
                Double price = Double.parseDouble(priceField.getText());

                Food food = new Food();
                food.setName(name);
                food.setPrice(price);

                ApiService.createFood(food);

                loadFoods();

                nameField.clear();
                priceField.clear();

            } catch (Exception ex) {

                System.out.println("Erro ao adicionar comida");

            }

        });

        // COLUNAS DA TABELA
        TableColumn<Food, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Food, String> colName = new TableColumn<>("Nome");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Food, Double> colPrice = new TableColumn<>("Preço");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // COLUNA EDITAR
        TableColumn<Food, Void> editCol = new TableColumn<>("Editar");

        editCol.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("Editar");

            {
                btn.setOnAction(e -> {

                    Food food = getTableView().getItems().get(getIndex());

                    Stage editStage = new Stage();
                    editStage.setTitle("Editar comida");

                    TextField nameEdit = new TextField(food.getName());
                    TextField priceEdit = new TextField(food.getPrice().toString());

                    Button saveBtn = new Button("Salvar");

                    saveBtn.setOnAction(ev -> {

                        try{

                            food.setName(nameEdit.getText());
                            food.setPrice(Double.parseDouble(priceEdit.getText()));

                            ApiService.updateFood(food);

                            loadFoods();

                            editStage.close();

                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                    });

                    VBox layout = new VBox(
                            10,
                            new Label("Nome"),
                            nameEdit,
                            new Label("Preço"),
                            priceEdit,
                            saveBtn
                    );

                    layout.setPadding(new Insets(20));

                    Scene scene = new Scene(layout,300,200);

                    editStage.setScene(scene);
                    editStage.show();

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty){

                super.updateItem(item, empty);

                if(empty){
                    setGraphic(null);
                }else{
                    setGraphic(btn);
                }

            }

        });

        // COLUNA EXCLUIR
        TableColumn<Food, Void> deleteCol = new TableColumn<>("Excluir");

        deleteCol.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("Excluir");

            {
                btn.setOnAction(e -> {

                    Food food = getTableView().getItems().get(getIndex());

                    ApiService.deleteFood(food.getId());

                    loadFoods();

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty){

                super.updateItem(item, empty);

                if(empty){
                    setGraphic(null);
                }else{
                    setGraphic(btn);
                }

            }

        });

        // ADICIONAR COLUNAS
        table.getColumns().add(colId);
        table.getColumns().add(colName);
        table.getColumns().add(colPrice);
        table.getColumns().add(editCol);
        table.getColumns().add(deleteCol);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // BOTÃO CARREGAR
        Button loadButton = new Button("Carregar Cardápio");
        loadButton.setOnAction(e -> loadFoods());

        // LAYOUT
        VBox root = new VBox(
                10,
                new Label("Pesquisar"),
                searchField,
                new Label("Nome"),
                nameField,
                new Label("Preço"),
                priceField,
                addButton,
                loadButton,
                table
        );

        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 700, 500);

        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Cardápio Desktop");
        stage.setScene(scene);
        stage.show();
    }

    private void loadFoods() {

        List<Food> foods = ApiService.getFoods();

        if (foods != null) {
            table.getItems().setAll(foods);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}