package firstDraft;

// These are the imports needed for the game

import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Arrays;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

// This the necessary declarations for any global variables or objects are made.
public class Main extends Application {

	public static Circle cir = new Circle(20);

	public static Circle othercir = new Circle(20);

	public static Rectangle leftrect = new Rectangle(20, 20);
	public static Rectangle rightrect = new Rectangle(20, 20);

	public static Random random = new Random();

	public static int option;
	public static int score = 0;
	public static double speed = 1.5;
	public static double increase = 0.75;

	static Pane root = new Pane();
	static Scene scene = new Scene(root, 400, 400);

	static Pane menuroot = new Pane();
	static Scene menu = new Scene(menuroot, 400, 400);
	static Text title = new Text("The Adventures of Bally Boi");
	static Text controls = new Text("Controls: Use Left and Right Arrows");
	static Text guide = new Text("Goal: Dodge the Blocks and Survive as Long as Possible");
	static Text scorestitle = new Text("Wall of Fame");
	static Button startbutton = new Button("Start");

	static Pane overroot = new Pane();
	static Scene over = new Scene(overroot, 400, 400);
	static Text gameover = new Text("GAME OVER");
	static Text yourscore = new Text("Your Score: " + score);
	static Text walloffame = new Text("Wall of Fame:");
	static Button restart = new Button("Restart");
	static Button mainmenu = new Button("Main Menu");
	
	public static Label score1;
	public static Label score2;
	public static Label score3;
	public static Label score4;
	public static Label score5;
	public static Label score6;
	public static Label score7;
	public static Label score8;
	public static Label score9;
	public static Label score10;

	public static long[] scoretable = new long[10];

	public static Label[] stringscores = new Label[10];

	public static JSONArray scoreData = new JSONArray();

	public static AnimationTimer timer;

	public static Label n1 = new Label("1");
	public static Label n2 = new Label("2");
	public static Label n3 = new Label("3");
	public static Label n4 = new Label("4");
	public static Label n5 = new Label("5");
	public static Label n6 = new Label("6");
	public static Label n7 = new Label("7");
	public static Label n8 = new Label("8");
	public static Label n9 = new Label("9");
	public static Label n10 = new Label("10");

	// This method contains a bunch of declarations and stuff that makes the program look nice.
	public static void initialize(Stage primaryStage) {

		// This makes the buttons interactive using the spacebar in addition to the mouse. 
		startbutton.setDefaultButton(true);
		restart.setDefaultButton(true);
		mainmenu.setDefaultButton(true);

		// This contols the 'Wall of Fame' text on the main screen.
		walloffame.setLayoutX(155);
		walloffame.setLayoutY(195);
		walloffame.setFont(Font.font("Specialty", 15));
		walloffame.setStyle("-fx-font-weight: bold");

		// This controls the title text on the main screen.
		title.setLayoutX(77);
		title.setLayoutY(20);
		title.setFont(Font.font("Specialty", 20));
		title.setStyle("-fx-font-weight: bold");

		// This controls the 'controls' text on the main screen.
		controls.setLayoutX(100);
		controls.setLayoutY(60);
		controls.setFont(Font.font("Specialty", 13));

		// This controls the 'guide' text on the main screen.
		guide.setLayoutX(45);
		guide.setLayoutY(85);
		guide.setFont(Font.font("Specialty", 13));

		// This controls the 'start' button on the main screen.
		startbutton.setLayoutX(165);
		startbutton.setLayoutY(105);
		startbutton.setPadding(new Insets(25, 25, 25, 25));

		// This adds all of the above objects to the root of the menu scene.
		menuroot.getChildren().addAll(startbutton, title, controls, guide, walloffame);
		menuroot.getChildren().addAll(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10);

		// This loop traverses the top ten scores array to create the necessary labels.
		for (int i = 0; i < 10; i++) {
			menuroot.getChildren().addAll(stringscores[i]);
		}

	}

	// This method submits the game score to a JSON file.
	// Andrew helped a bit with this method.
	public static void submitScore() {
		try {

			// This loop checks where in the top ten scores the current score ranks. The
			// current score will take precedence over any that are lower than it, but if it
			// is lower than the tenth-highest score, then it is ignored.
			for (int i = 0; i < 10 && score > scoretable[i]; i++) {

				// If the current score is greater than only the tenth-highest score, it will
				// just overwrite that one.
				if (i == 0) {

					scoretable[i] = score;
				}

				// If the current score is greater than two or more of the current top ten
				// scores, then this loop will create a temporary variable that bumps each of
				// the other scores down one spot.
				else {

					long temp = scoretable[i];

					scoretable[i] = scoretable[i - 1];
					scoretable[i - 1] = temp;

				}
			}

			// This part of the code actually writes the new top ten scores into a JSON array.
			// These are the necessary declarations. They aren't made above because they don't
			// need to have global scope.
			FileWriter writer = new FileWriter("src/firstDraft/highscores.json");
			JSONArray newscoretables = new JSONArray();

			// This just traverses the 'scoretable' array from above and maps its values to the
			// corresponding spot in the JSON file.
			for (int i = 0; i < 10; i++) {
				JSONObject newScore = new JSONObject();
				newScore.put("score", scoretable[i]);

				newscoretables.add(newScore);
			}

			// This part writes the JSON array 'newscoretables' as a string in the JSON file.
			// It also flushes (clears) and closes the writer, just to be safe.
			writer.write(newscoretables.toJSONString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This method returns the top 10 scores in our JSON file and creates first a long array
	// to sort the numbers, then converts them to a String array so that they can be written
	// to Labels.
	// Andrew helped a bit with this method.
	public static void fetchScores() {

		try {

			FileReader reader = new FileReader("src/firstDraft/highscores.json");

			JSONParser parsepatel = new JSONParser();

			scoreData = (JSONArray) parsepatel.parse(reader);
			reader.close();

			// This for loop traverses the JSON Object and assigns each value to the corresponding
			// spot in the long array 'scoretable'.
			for (int i = 0; i < 10; i++) {

				scoretable[i] = (long) ((JSONObject) scoreData.get(i)).get("score");

			}

			// This sorts the long array scoretable in ascending order (hence the '9-1' reference
			// in our for loop below).
			Arrays.sort(scoretable);

			// This loop flips the long array 'scoretable' and maps it to a String array so that
			// I can assign the values to Labels.
			for (int i = 0; i < 10; i++) {
				stringscores[9 - i].setText(String.valueOf(scoretable[i]));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// This method creates the scoreboard that you see on the Main Menu scene.
	public static void makeAScoreboard() {

		stringscores[0] = new Label("");
		stringscores[0].resize(200, 45);
		stringscores[0].setLayoutX(100);
		stringscores[0].setLayoutY(205);

		stringscores[1] = new Label("");
		stringscores[1].resize(200, 45);
		stringscores[1].setLayoutX(100);
		stringscores[1].setLayoutY(250);

		stringscores[2] = new Label("");
		stringscores[2].resize(200, 45);
		stringscores[2].setLayoutX(100);
		stringscores[2].setLayoutY(295);

		stringscores[3] = new Label("");
		stringscores[3].resize(200, 45);
		stringscores[3].setLayoutX(100);
		stringscores[3].setLayoutY(340);

		stringscores[4] = new Label("");
		stringscores[4].resize(200, 45);
		stringscores[4].setLayoutX(100);
		stringscores[4].setLayoutY(385);

		stringscores[5] = new Label("");
		stringscores[5].resize(200, 45);
		stringscores[5].setLayoutX(300);
		stringscores[5].setLayoutY(205);

		stringscores[6] = new Label("");
		stringscores[6].resize(200, 45);
		stringscores[6].setLayoutX(300);
		stringscores[6].setLayoutY(250);

		stringscores[7] = new Label("");
		stringscores[7].resize(200, 45);
		stringscores[7].setLayoutX(300);
		stringscores[7].setLayoutY(295);

		stringscores[8] = new Label("");
		stringscores[8].resize(200, 45);
		stringscores[8].setLayoutX(300);
		stringscores[8].setLayoutY(340);

		stringscores[9] = new Label("");
		stringscores[9].resize(200, 45);
		stringscores[9].setLayoutX(300);
		stringscores[9].setLayoutY(385);
	}

	// This method has a random number generator that chooses between five possible cases for
	// the obstacles that will spawn at the top of the screen.
	public static void decision() {

		option = random.nextInt(5);

		// Case 1
		if (option == 0) {

			leftrect.setWidth(325);

			leftrect.setX(0);
			leftrect.setY(0);
			leftrect.setFill(Color.DARKRED);
			rightrect.setFill(Color.DARKRED);

		}

		// Case 2
		if (option == 1) {

			leftrect.setWidth(200);
			rightrect.setWidth(200);

			leftrect.setX(100);
			leftrect.setY(0);
			leftrect.setFill(Color.DARKRED);

			rightrect.setX(200);
			rightrect.setY(0);
			rightrect.setFill(Color.DARKRED);

		}

		// Case 3
		if (option == 2) {

			leftrect.setWidth(150);
			rightrect.setWidth(100);

			leftrect.setX(0);
			leftrect.setY(0);
			leftrect.setFill(Color.DARKRED);

			rightrect.setX(300);
			rightrect.setY(0);
			rightrect.setFill(Color.DARKRED);

		}

		// Case 4
		if (option == 3) {

			leftrect.setWidth(225);
			rightrect.setWidth(150);

			leftrect.setX(0);
			leftrect.setY(0);
			leftrect.setFill(Color.DARKRED);

			rightrect.setX(325);
			rightrect.setY(0);
			rightrect.setFill(Color.DARKRED);

		}

		// Case 5
		if (option == 4) {

			leftrect.setWidth(150);
			rightrect.setWidth(175);

			leftrect.setX(0);
			leftrect.setY(0);
			leftrect.setFill(Color.DARKRED);

			rightrect.setX(225);
			rightrect.setY(0);
			rightrect.setFill(Color.DARKRED);
		}
	}

	// This method handes the collision detection. The first two loops basically assign the
	// circle 'check' to whichever circle is on the screen, and the next two involve the actual
	// collision events.
	public static void crash(Stage primaryStage) {

		Circle check = new Circle();

		// If the first circle is on the screen, then ignore the second circle.
		if (cir.getCenterX() >= 0 && cir.getCenterX() <= 400) {
			check = cir;
		}

		// If the second circle is on the screen, then ignore the first circle.
		else if (othercir.getCenterX() >= 0 && othercir.getCenterX() <= 400) {
			check = othercir;
		}

		// If the toggled circle touches the right rectangle of the obstacle, then execute the
		// code.
		if (check.getBoundsInParent().intersects(rightrect.getBoundsInParent())) {
			timer.stop();
			submitScore();
			fetchScores();
			primaryStage.setScene(over);
			death(primaryStage);

			// If the toggled circle touches the left rectangle of the obstacle, then execute the
			// code.
		} else if (check.getBoundsInParent().intersects(leftrect.getBoundsInParent())) {
			timer.stop();
			submitScore();
			fetchScores();
			primaryStage.setScene(over);
			death(primaryStage);
		}
	}

	// This method handles what happens after the collision event. It sets the objects on the
	// 'over' scene that was set in the last method, and contains the EventHandlersfor the
	// two buttons that appear on the 'over' scene.
	public static void death(Stage primaryStage) {

		gameover.setLayoutX(150);
		gameover.setLayoutY(20);
		gameover.setFont(Font.font("Specialty", 20));
		gameover.setStyle("-fx-font-weight: bold");

		yourscore.setLayoutX(150);
		yourscore.setLayoutY(40);
		yourscore.setText("Your Score: " + score);
		yourscore.setFont(Font.font("Specialty", 16));

		restart.setLayoutX(80);
		restart.setLayoutY(165);
		restart.setPadding(new Insets(15, 15, 15, 15));

		mainmenu.setLayoutX(230);
		mainmenu.setLayoutY(165);
		mainmenu.setPadding(new Insets(15, 15, 15, 15));

		// When you push the 'restart' button, it will update the top ten scoreboard, reset the
		// score counter and all the obstacles, set the cirle in the middle again, and then run
		// the 'decision' method. Finally, it will set the scene to be the main game scene, and
		// unpause the AnimationTimer.
		restart.setOnAction(e -> {

			fetchScores();

			score = 0;

			rightrect.setX(0);
			rightrect.setY(0);

			leftrect.setX(0);
			leftrect.setY(0);

			cir.setCenterX(200);
			cir.setCenterY(380);

			othercir.setCenterX(cir.getCenterX() - 400);
			othercir.setCenterY(380);

			speed = 1.5;
			increase = 0.75;

			primaryStage.setScene(scene);
			decision();
			timer.start();
		});

		// When you push the 'main menu' button, it will update the top ten scoreboard, reset the
		// score counter and all the obstacles, set the cirle in the middle again, and then run
		// the 'decision' method. The difference between this button and the 'restart' button is
		// that this will set the scene to the Main Menu, and will also display the top ten
		// scoreboard.
		mainmenu.setOnAction(e -> {

			fetchScores();

			score = 0;

			rightrect.setX(0);
			rightrect.setY(0);

			leftrect.setX(0);
			leftrect.setY(0);

			cir.setCenterX(200);
			cir.setCenterY(380);

			othercir.setCenterX(cir.getCenterX() - 400);
			othercir.setCenterY(380);

			speed = 1.5;
			increase = 0.75;

			primaryStage.setScene(menu);
			decision();
		});

		// Regardless of which button is pushed, the objects on the 'Game Over' scene are reset.
		overroot.getChildren().removeAll(gameover, yourscore, restart, mainmenu);
		overroot.getChildren().addAll(gameover, yourscore, restart, mainmenu);
	}

	// This method creates the score rank numbers (1, 2, 3, etc).
	public static void scoreranks() {
		n1.resize(200, 45);
		n1.setLayoutX(10);
		n1.setLayoutY(205);
		n1.setStyle("-fx-font-weight: bold");

		n2.resize(200, 45);
		n2.setLayoutX(10);
		n2.setLayoutY(250);
		n2.setStyle("-fx-font-weight: bold");

		n3.resize(200, 45);
		n3.setLayoutX(10);
		n3.setLayoutY(295);
		n3.setStyle("-fx-font-weight: bold");
		
		n4.resize(200, 45);
		n4.setLayoutX(10);
		n4.setLayoutY(340);
		n4.setStyle("-fx-font-weight: bold");
		
		n5.resize(200, 45);
		n5.setLayoutX(10);
		n5.setLayoutY(385);
		n5.setStyle("-fx-font-weight: bold");
		
		n6.resize(200, 45);
		n6.setLayoutX(210);
		n6.setLayoutY(205);
		n6.setStyle("-fx-font-weight: bold");
		
		n7.resize(200, 45);
		n7.setLayoutX(210);
		n7.setLayoutY(250);
		n7.setStyle("-fx-font-weight: bold");
		
		n8.resize(200, 45);
		n8.setLayoutX(210);
		n8.setLayoutY(295);
		n8.setStyle("-fx-font-weight: bold");
		
		n9.resize(200, 45);
		n9.setLayoutX(210);
		n9.setLayoutY(340);
		n9.setStyle("-fx-font-weight: bold");
		
		n10.resize(200, 45);
		n10.setLayoutX(210);
		n10.setLayoutY(385);
		n10.setStyle("-fx-font-weight: bold");
	}
	@Override

	// This is where most of the action goes down.
	public void start(Stage primaryStage) {
		try {

			// This block creates the scoreboard, adds the top ten scores to it, runs the above
			// 'initialize' method, and chooses which obstacle case it will run.
			makeAScoreboard();
			fetchScores();
			initialize(primaryStage);
			decision();
			scoreranks();
			
			// When you press the start button, it changes the scene to the main game scene
			// and starts the AnimationTimer.
			startbutton.setOnAction(e -> {
				primaryStage.setScene(scene);
				timer.start();
			});

			// This creates the scoreboard text field in the top left of the screen.
			Text scoreboard = new Text();

			// This is the AnimationTimer that was mentioned above. It handles most of the
			// movement code for the obstacles, and also controls the speed of the obstacles.
			timer = new AnimationTimer() {

				@Override
				// This is the part of the AnimationTimer that handles what is actually
				// happening on the screen.
				public void handle(long now) {

					// This calls the 'crash' method to make sure that the ball hasn't hit
					// either of the obstacles.
					crash(primaryStage);

					// This increments the speed of the two rectangles based on the 'speed'
					// variable (see below)
					leftrect.setY(leftrect.getY() + speed);
					rightrect.setY(rightrect.getY() + speed);

					// This block sets the position, fill colour, font and text of the
					// scoreboard on the main game screen. It also increments the score.
					scoreboard.setX(10);
					scoreboard.setY(15);
					scoreboard.setFill(Color.BLACK);
					scoreboard.setFont(Font.font("Calibri", 20));
					scoreboard.setText(Integer.toString(score));
					score++;

					// This if statement handles what happens when the obstacle reaches the
					// bottom of the screen. When this happens, it increases the velocity of
					// the obstacles, but also decreases the incrementer to avoid rapid exponential
					// growth. It also calls the 'decision' method to choose a new obstacle.
					if (leftrect.getY() >= 450) {

						decision();
						if (increase > 0.5) {
							speed += increase;
							increase -= 0.05;
						}
					}

				}

			};

			// This do/while loop sets the initial coordinates and fill colour of the
			// circle. I
			// used a do/while loop because I only wanted to run through the assignment
			// once.
			do {
				cir.setCenterX(200);
				cir.setCenterY(380);
				cir.setFill(Color.GREEN);
			} while (false);

			// This do/while loop sets the initial coordinates and fill colour of the other
			// circle. I used a do/while loop because I only wanted to run through the
			// assignment
			// once.
			do {
				othercir.setCenterX(cir.getCenterX() - 400);
				othercir.setCenterY(380);
				othercir.setFill(Color.GREEN);
			} while (false);

			// This group consists of the two rectangular obstacles that make up each stage
			// of the
			// game. I used a level because I wanted the rectangles to move perfectly in
			// sync with
			// each other.
			Group level = new Group();
			level.getChildren().addAll(leftrect, rightrect);

			// This command adds the two circles from above, the 'level' group and the
			// scoreboard
			// to the main game scene.
			root.getChildren().addAll(cir, othercir, level, scoreboard);

			// This EventHandler is where the magic happens.
			final EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
				public void handle(final KeyEvent keyEvent) {

					// This switch moves the ball ten pixels to the left or to the right, depending
					// on which key is pressed.
					switch (keyEvent.getCode()) {

					// If the user presses the left arrow key, then the ball moves ten pixels to
					// the left. The other circle moves in parallel to the first one.
					case LEFT: {

						cir.setCenterX(cir.getCenterX() - 10);

						othercir.setCenterX(othercir.getCenterX() - 10);

						break;
					}

					// If the user presses the right arrow key, then the ball moves ten pixels to
					// the right. The other circle moves in parallel to the first one.
					case RIGHT: {

						cir.setCenterX(cir.getCenterX() + 10);

						othercir.setCenterX(othercir.getCenterX() + 10);

						break;
					}

					}
					
					// This series of if statements ensures that the balls loop around the
					// screen when one goes off the edge, so that the player can wrap around
					// the screen as much as they would like in either direction.
					
					// If the center of the main cirle reaches X = 20, then the other circle is
					// set 400 pixels to the right in preparation of the player moving off the
					// edge of the screen.
					if (cir.getCenterX() == 20) {

						othercir.setCenterX(cir.getCenterX() + 400);

					// If the center of the other cirle reaches X = 20, then the main circle is
					// set 400 pixels to the right in preparation of the player moving off the
					// edge of the screen.
					} else if (othercir.getCenterX() == 20) {

						cir.setCenterX(othercir.getCenterX() + 400);
					}

					// If the center of the main cirle reaches X = 380, then the other circle is
					// set 400 pixels to the left in preparation of the player moving off the
					// edge of the screen.
					if (cir.getCenterX() == 380) {

						othercir.setCenterX(cir.getCenterX() - 400);

					// If the center of the other cirle reaches X = 380, then the main circle is
					// set 400 pixels to the left in preparation of the player moving off the
					// edge of the screen.
					} else if (othercir.getCenterX() == 380) {

						cir.setCenterX(othercir.getCenterX() - 400);
					}

				}
			};

			scene.setOnKeyPressed(keyEventHandler);

			primaryStage.setScene(menu);
			primaryStage.show();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
