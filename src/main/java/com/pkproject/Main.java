package com.pkproject;

import com.pkproject.client.Client;
import com.pkproject.objects.Player;
import com.pkproject.objects.Question;
import com.pkproject.objects.StateGame;
import com.pkproject.settings.Settings;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private VBox root;
    private Stage stage;
    public Client client;
    public StateGame stateGame;
    private List<Question> questions;
    private List<Player> players;
    private boolean goNextQuestion;
    private int i = 0;

    //main buttons
    private Button buttonN;
    private Button buttonM;

    //question buttons
    private Button question;
    private Button answerA;
    private Button answerB;
    private Button answerC;
    private Button answerD;


    private ObservableList<PieChart.Data> pieChartData;
    private PieChart pieChart;
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public StateGame getStateGame() {
        return stateGame;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public void start(Stage stage) {
        //Preparing ObservbleList object
        pieChartData = FXCollections.observableArrayList();

        //Creating a Pie chart
        pieChart = new PieChart(pieChartData);

        //Setting the title of the Pie chart
        pieChart.setTitle("User Answer");

        //setting the direction to arrange the data
        pieChart.setClockwise(true);

        //Setting the length of the label line
        pieChart.setLabelLineLength(50);

        //Setting the labels of the pie chart visible
        pieChart.setLabelsVisible(true);

        //Setting the start angle of the pie chart
        pieChart.setStartAngle(180);

        initButtons();

        stateGame = new StateGame();
        questions = new ArrayList<Question>();
        client = new Client(this);

        if(client.connectWithServer()) {
            if(client.turnOnGame()) {
                System.out.println("connect with server");
                this.stage = stage;
                initUI(stage);
            }
        } else {
            System.out.println("error client");
        }
    }

    public void initButtons() {
        question = new Button("");
        answerA = new Button("");
        answerB = new Button("");
        answerC = new Button("");
        answerD = new Button("");

        buttonN = new Button("start quiz click n   ");
        buttonM = new Button("show result click m");

        enableDisableQuestionButtons(false);
    }

    public void enableDisableQuestionButtons(boolean visible) {
        question.setVisible(visible);
        answerA.setVisible(visible);
        answerB.setVisible(visible);
        answerC.setVisible(visible);
        answerD.setVisible(visible);
    }


    private void enableDisableButtonMain(boolean visible) {
        buttonM.setVisible(visible);
        buttonN.setVisible(visible);
    }

    private void initUI(Stage stage) {
        root = new VBox();
        root.getChildren().addAll(buttonN, buttonM, question, answerA, answerB, answerC, answerD, pieChart);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        scene.setOnKeyPressed(keyPressed);
        scene.setOnKeyReleased(keyReleased);
        //start game
        stage.show();
    }

    public void nextQuestion() {
        if(stateGame.startQuiz) {
            Question q = questions.get(i);
            question.setText("Question: " + q.getQuestion());
            answerA .setText("Answer A" + q.getAnswer()[0]);
            answerB.setText("Answer B" + q.getAnswer()[1]);
            answerC.setText("Answer C" + q.getAnswer()[2]);
            answerD.setText("Answer D" + q.getAnswer()[3]);
            stateGame.startQuiz = false;
            return;
        }
        else if(i == Settings.AMOUNTQUESTION) {
            stateGame.running = false;
            enableDisableQuestionButtons(false);
            enableDisableButtonMain(true);
            client.sendResult();
            stateGame.endGame = true;
            stateGame.running = false;
            return;
        }
        else {
            i++;
            Question q = questions.get(i);
            question.setText("Question: " + q.getQuestion());
            answerA.setText("Answer A" + q.getAnswer()[0]);
            answerB.setText("Answer B" + q.getAnswer()[1]);
            answerC.setText("Answer C" + q.getAnswer()[2]);
            answerD.setText("Answer D" + q.getAnswer()[3]);
        }
    }
    public void showResult() {
        client.showAllResult();
    }

    private EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {

        public void handle(KeyEvent event) {
          switch (event.getCode()) {
                //control one player
                case Z: //answer a
                    if(stateGame.running && !stateGame.endGame)  questions.get(i).setUserAnswer('a'); nextQuestion();
                    break;
                case X: //answer b
                    if(stateGame.running && !stateGame.endGame)  questions.get(i).setUserAnswer('b'); nextQuestion();
                    break;
                case C: //answer c
                    if(stateGame.running && !stateGame.endGame)  questions.get(i).setUserAnswer('c'); nextQuestion();
                    break;
                case V: //answer d
                    if(stateGame.running && !stateGame.endGame)  questions.get(i).setUserAnswer('d'); nextQuestion();
                    break;
                case N: //start quiz
                    if(stateGame.startQuiz) {
                        enableDisableQuestionButtons(true);
                        enableDisableButtonMain(false);
                        stateGame.running = true;
                        nextQuestion();
                    }
                     break;
                case M: //check all result if you end quiz
                    if(stateGame.endGame) {
                        pieChartData.clear();
                        client.showAllResult();
                        while(players == null); //loop, because we have to wait for a response from the server
                        System.out.println(players.size());
                        enableDisableButtonMain(false);
                        for(int i = 0; i < players.size(); i++) {
                            pieChartData.add(new PieChart.Data(players.get(i).getNameOfPlayer() + " Result: " +  players.get(i).getResult(),players.get(i).getResult()));
                        }

                    }
                     break;
                case R: //return with result
                    if(!stateGame.running && stateGame.endGame) {
                        enableDisableButtonMain(true);
                    }
                    break;
              case ENTER: //return with result
                  if(stateGame.running && stateGame.endGame) {
                      enableDisableButtonMain(true);
                  }
                  break;
            }
        }

    };

    private EventHandler<KeyEvent> keyReleased = new EventHandler<KeyEvent>() {

        public void handle(KeyEvent event) {
            switch (event.getCode()) {
               // case W: testPlayers[0].moveElementY(-Settings.VECOLITYPLAYERY); break;
               // case UP: testPlayers[1].moveElementY(-Settings.VECOLITYPLAYERY);break;
            }
        }
    };

    public static void main(String[] args) {
        launch(args);


    }
}
