package me.shelepov;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class MP3Player extends Application {

    MediaPlayer mediaPlayer;
    Media media;
    int songNumber;
    ArrayList<File> songs = new ArrayList<>();
    Label label = new Label("No file selected");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Button openButton = new Button("Open");
        openButton.setOnAction(actionEvent -> openFile(stage));

        Button selectFolder = new Button("Select folder");
        selectFolder.setOnAction(actionEvent -> {
            songs.addAll(getFileList(stage));
            playSong();
        });
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(actionEvent -> pauseMedia());

        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(actionEvent -> playMedia());

        Button stopButton = new Button("Stop");
        stopButton.setOnAction(actionEvent -> mediaPlayer.stop());

        Button nextButton = new Button("Next");
        nextButton.setOnAction(actionEvent -> nextMedia());

        Button prevButton = new Button("Prev");
        prevButton.setOnAction(actionEvent -> previousMedia());

        BorderPane borderPane = new BorderPane();
        HBox buttons = new HBox(openButton, selectFolder, prevButton, pauseButton, resumeButton, stopButton, nextButton);
        borderPane.setTop(buttons);
        borderPane.setCenter(label);
        BorderPane.setAlignment(label, Pos.CENTER);

        Scene scene = new Scene(borderPane, 650, 200);
        stage.setScene(scene);
        stage.setTitle("MP3 Player");
        stage.show();
    }

    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select one or more files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (!isNull(files)) {
            songs.addAll(files);
            playSong();
        }
    }

    private String getFileName(File file) {
        String absolutePath = file.getAbsolutePath();
        String separator = File.separator;
        String[] split = absolutePath.split("\\" + separator);
        return split[split.length - 1];
    }

    public List<File> getFileList(Stage stage) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(stage);
            return (List<File>) FileUtils.listFiles(file.getAbsoluteFile(), new String[]{"mp3"}, true);
        } catch (java.lang.RuntimeException exc) {
            System.out.println("unable to read list of files");
        }
        return new ArrayList<>();
    }

    public void playMedia() {
        mediaPlayer.play();
    }

    public void pauseMedia() {
        mediaPlayer.pause();
    }

    public void resetMedia() {
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void previousMedia() {
        if (songNumber > 0) {
            songNumber--;
        } else {
            songNumber = songs.size() - 1;
        }
        playSong();
    }

    private void playSong() {
        if (!isNull(mediaPlayer)) mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        label.setText(songs.get(songNumber).getName());
        playMedia();
    }

    public void nextMedia() {
        if (songNumber < songs.size() - 1) {
            songNumber++;
        } else {
            songNumber = 0;
        }
        playSong();
    }
}
