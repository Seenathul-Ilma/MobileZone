package lk.ijse.gdse71.mobilezone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppInitializer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/StartingPage.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Mobile Zone");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/MobileZoneIcon.png")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // Launch the JavaFX application
    }

    /*@Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoadingScreenView.fxml"))));
        stage.show();

        Task<Scene> loadingTask = new Task<>() {
            @Override
            protected Scene call() throws Exception {
                FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/MainLayout.fxml"));
                return new Scene(fxmlLoader.load());
            }
        };

        loadingTask.setOnSucceeded(event -> {
            Scene value = loadingTask.getValue();

            stage.setTitle("Mobile Zone");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/background.jpg")));
            stage.setMaximized(true);

            stage.setScene(value);
        });

        loadingTask.setOnFailed(event -> {
            System.err.println("Failed to load the main layout."); // Print error message
        });

        new Thread(loadingTask).start();
    }*/
}
