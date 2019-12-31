package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
//import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
//import javafx.fxml.FXMLLoader;

/**
 * @author fokofotso
 *
 */

public class MainApp  extends Application {

	private static int round = 1;
	private int count;

	private ArrayList<Integer> player1;
	private ArrayList<Integer> player2;

	private VBox root;
	private Scene scene;
	//private GridPane gridpane;
	private Label label;
	private HBox hbox1;
	private HBox hbox2;
	private HBox hbox3;
	private HBox hbox4;
	private ArrayList<Button> bt;
	private Button bt1;	
	private Button bt2;
	private Button bt3;	
	private Button bt4;
	private Button bt5;	
	private Button bt6;
	private Button bt7;	
	private Button bt8;
	private Button bt9;	
	private Button btRePlay;
	private Button btPlayComputer;	
	private Button btLevel;
	//private Button bt2;
	private boolean playComputer;
	private Random rand;
	private List<Color> colors;
	private Color[] playersColors;
	private List<String> colorsNames;
	private List<Integer> ids;
	private int wins1;
	private int wins2;
	private int level;
	//private TextField textField;
	//@Override
	public void start(Stage primaryStage) {
		try {
			initialition();
			primaryStage.setTitle("Tic-Tac-Toe");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.centerOnScreen();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private class ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {	
			Button button = ((Button)e.getSource());
			if(count%2==1 && !playComputer) {
				handlePlay(button ,player1, player2, playersColors[0]);		
			} else if(count%2==0 && !playComputer) {				
				handlePlay(button ,player2, player1, playersColors[1]);
			} else if(playComputer) {	
				handlePlay(button, player1, player2, playersColors[0]);				  
			}				
		}

		private void computerPlay(Button button, ArrayList<Integer> player, 
				ArrayList<Integer> cmputerPlayer, Color color, int max) {
			//System.out.println("computer rount at: " + count);
			int n = computerChoiceButton(player, cmputerPlayer, max);
			if(n==-1) {
				endGame(Color.GREY);
				return;
			}			
			bt.get(n).setBackground(
					new Background(new BackgroundFill(color, null, null)));
			cmputerPlayer.add(Integer.valueOf(bt.get(n).getId()));
			bt.get(n).setText(bt.get(n).getId());	
			updateCount();
			if(count>=5) {
				if(checkWinner(cmputerPlayer)) {
					endGame(color);
					return;
				}
			}										
		}

		private void handlePlay(Button button, ArrayList<Integer> player, ArrayList<Integer> otherPlayer,
				Color color) {		
			if(!player.contains(Integer.parseInt(button.getId())) && 
					!otherPlayer.contains(Integer.parseInt(button.getId()))) {
				button.setBackground(
						new Background(new BackgroundFill(color, null, null)));
				player.add(Integer.parseInt(button.getId()));
				button.setText(button.getId());	
				updateCount();
				if(count>=5) {				
					if(checkWinner(player)) {
						endGame(color);	
						return;
					}
				}				
				if(playComputer) {
					computerPlay(button, player1, player2, playersColors[1], 8);
				}
			}		
		}

		private int computerChoiceButton(ArrayList<Integer> player11, ArrayList<Integer> player22, int max) {
			if(count>max) {				
				return -1;
			}
			int n = rand.nextInt(9);
			//System.out.println("level: " + level);
			if(level>0) {
				int id = stragieTicTacToe(player11, player22);
				n = ids.indexOf(id);
				//System.out.println("n of strategie: " + n);
			} 						    
			while(player11.contains(Integer.valueOf(bt.get(n).getId())) 
					|| player22.contains(Integer.valueOf(bt.get(n).getId()))) {			
				n = rand.nextInt(9);						 
			}		
			return n;		
		}

	}

	public boolean checkWinner(ArrayList<Integer> player) {
		//System.out.println("count: " + count);
		//player.forEach(e->System.out.println(e));
		if(player.containsAll(Arrays.asList(2,7,6))){
			return true;
		}
		if(player.containsAll(Arrays.asList(9,5,1))){
			return true;
		}
		if(player.containsAll(Arrays.asList(4,3,8))){
			return true;
		}
		if(player.containsAll(Arrays.asList(2,9,4))) {
			return true;
		}
		if(player.containsAll(Arrays.asList(7,5,3))) {
			return true;
		}
		if(player.containsAll(Arrays.asList(6,1,8))) {
			return true;
		}
		if(player.containsAll(Arrays.asList(2,5,8))) {
			return true;
		}
		if(player.containsAll(Arrays.asList(6,5,4))) {
			return true;
		}
		if(count==9) {		
			endGame(Color.GREY);
		}
		//System.out.println(false);
		return false;    
	}

	public int stragieTicTacToe(ArrayList<Integer> player, ArrayList<Integer> computerPlayer) {
		if(count==1) {
			return  player.contains(5)?rand.nextInt(9)+1:5;
		}
		if(count < 4) {
			int n = strategie(player, computerPlayer);
			if(n!=-1) {
				return n;
			}else {
				n=strategie(computerPlayer, player);
				return n!=-1?n:rand.nextInt(9)+1;
			}
		}else {
			int n = strategie(computerPlayer, player);
			if(n!=-1) {
				return n;
			}else {
				n=strategie(player, computerPlayer);
				return n!=-1?n:rand.nextInt(9)+1;
			}
		}
	}

	private int strategie(ArrayList<Integer> player, ArrayList<Integer> computerPlayer) {
		int n = -1;
		List<Integer> allPlay = new ArrayList<Integer>();
		if(level>1) {
			player.forEach(e->allPlay.add(e));
			computerPlayer.forEach(e->allPlay.add(e));
		}
		if(player.containsAll(Arrays.asList(9,1)) && !allPlay.contains(5)){
			return 5;
		}
		if(player.containsAll(Arrays.asList(2,7)) && !allPlay.contains(6)){
			return 6;
		}
		if(player.containsAll(Arrays.asList(7,6)) && !allPlay.contains(2)){
			return 2;
		}
		if(player.containsAll(Arrays.asList(2,6)) && !allPlay.contains(7)){
			return 7;
		}
		if(player.containsAll(Arrays.asList(9,5)) && !allPlay.contains(1)){
			return 1;
		}
		if(player.containsAll(Arrays.asList(5,1)) && !allPlay.contains(9)){
			return 9;
		}
		if(player.containsAll(Arrays.asList(4,3)) && !allPlay.contains(8)){
			return 8;
		}
		if(player.containsAll(Arrays.asList(4,8)) && !allPlay.contains(3)){
			return 3;
		}
		if(player.containsAll(Arrays.asList(3,8)) && !allPlay.contains(4)){
			return 4;
		}
		if(player.containsAll(Arrays.asList(9,4)) && !allPlay.contains(2)) {
			return 2;
		}
		if(player.containsAll(Arrays.asList(2,4)) && !allPlay.contains(9)) {
			return 9;
		}
		if(player.containsAll(Arrays.asList(2,9)) && !allPlay.contains(4)) {
			return 4;
		}
		if(player.containsAll(Arrays.asList(5,3)) && !allPlay.contains(7)) {
			return 7;
		}
		if(player.containsAll(Arrays.asList(7,3)) && !allPlay.contains(5)) {
			return 5;
		}
		if(player.containsAll(Arrays.asList(7,5)) && !allPlay.contains(3)) {
			return 3;
		}
		if(player.containsAll(Arrays.asList(1,8)) && !allPlay.contains(6)) {
			return 6;
		}
		if(player.containsAll(Arrays.asList(6,8)) && !allPlay.contains(1)) {
			return 1;
		}
		if(player.containsAll(Arrays.asList(6,1)) && !allPlay.contains(8)) {
			return 8;
		}
		if(player.containsAll(Arrays.asList(5,8)) && !allPlay.contains(2)) {
			return 2;
		}
		if(player.containsAll(Arrays.asList(2,8)) && !allPlay.contains(5)) {
			return 5;
		}
		if(player.containsAll(Arrays.asList(2,5)) && !allPlay.contains(8)) {
			return 8;
		}
		if(player.containsAll(Arrays.asList(5,4)) && !allPlay.contains(6)) {
			return 6;
		}
		if(player.containsAll(Arrays.asList(6,4)) && !allPlay.contains(5)) {
			return 5;
		}
		if(player.containsAll(Arrays.asList(6,5)) && !allPlay.contains(4)) {
			return 4;
		}
		return n;
	}

	public void endGame(Color color) {
		round++;
		String c1 = colorsNames.get(colors.indexOf(playersColors[0]));
		String c2 = colorsNames.get(colors.indexOf(playersColors[1]));
		if(color==playersColors[0] ) {
			++wins1;
			label.setText("GAME OVER!!! THE WINNER IS "+ c1 + "."
					+ " Score: " +c1 +" "+ wins1 + " : " + c2 +" "+ wins2);			   
		}else if(color==playersColors[1]){
			++wins2;
			label.setText("GAME OVER!!! THE WINNER IS "+ c2 + "."
					+ " Score: " +c1 +" "+ wins1 + " : " + c2 +" "+ wins2);
		}else if(color==Color.GREY){
			label.setText("GAME OVER!!! IT IS DRAW."
					+ " Score: " +c1 +" "+ wins1 + " : " + c2 +" "+ wins2);
		}
		initPlayer();
		for(int i=0; i<9; i++){	
			bt.get(i).setDisable(true);	
		}	    		
	}

	public void replay() {
		initPlayer();
		label.setText("Current round: "+ round);		
		for(int i=0; i<9; i++){
			Button button = bt.get(i);
			button.setDisable(false);
			button.setBackground( new Background(
					new BackgroundFill(javafx.scene.paint.Color.LIGHTGRAY, null, null)));	    	
		}
	}

	private void initPlayer() {
		if(count>0) {
			player1.clear();
			player2.clear();
			count=0;
		}

	}

	private void updateCount() {
		++count;	
	}

	private void initialition() {
		count = 0;
		playComputer = true;
		bt = new ArrayList<Button>();
		root = new VBox(10);
		root.setAlignment(Pos.CENTER);
		//gridpane = new GridPane();
		bt1 = new Button(); bt.add(bt1);		 
		bt2 = new Button(); bt.add(bt2);
		bt3 = new Button();	bt.add(bt3);
		bt4 = new Button(); bt.add(bt4);
		bt5 = new Button();	bt.add(bt5);
		bt6 = new Button(); bt.add(bt6);
		bt7 = new Button(); bt.add(bt7);
		bt8 = new Button(); bt.add(bt8);
		bt9 = new Button(); bt.add(bt9);	    

		ids = Arrays.asList(2, 7, 6, 9, 5, 1, 4, 3, 8);

		for(int i=0; i<9; i++){		    	
			Button button = bt.get(i);
			//gridpane.add(button, j, i); 
			button.setPadding(new Insets(50));
			button.setOnAction(new ButtonHandler());
			button.setId(ids.get(i).toString());
			button.setMaxWidth(50);
			button.setBackground( new Background(
					new BackgroundFill(javafx.scene.paint.Color.LIGHTGRAY, null, null)));	    			    		    					
		}
		hbox1 = new HBox(10);
		hbox1.setAlignment(Pos.BOTTOM_CENTER);
		hbox1.getChildren().addAll(bt1, bt2, bt3);
		hbox2 = new HBox(10);
		hbox2.setAlignment(Pos.BOTTOM_CENTER);
		hbox2.getChildren().addAll(bt4, bt5, bt6);
		hbox3 = new HBox(10);
		hbox3.setAlignment(Pos.BOTTOM_CENTER);
		hbox3.getChildren().addAll(bt7, bt8, bt9);

		//gridpane.setAlignment(Pos.TOP_CENTER);
		label = new Label("Current round: "+ round + ". You "
				+ "play with Computer: "+ playComputer );
		//label.setAlignment(Pos.CENTER);
		btRePlay = new Button("Re-Play");
		btRePlay.setPadding(new Insets(20));
		btRePlay.setOnAction(e -> {
			replay();
			playComputer = false;
		});

		//textField = new TextField("1");
		//textField.
		level =0;
		int l =level+1;
		btLevel = new Button("Level "+ l);
		btLevel.setPadding(new Insets(20));	
		btLevel.setOnAction(e -> {
			level = (++level)%3;
			int ll =level+1;
			btLevel.setText("Level "+ ll);
		});
		btPlayComputer = new Button("Play with Computer");
		btPlayComputer.setPadding(new Insets(20));
		btPlayComputer.setOnAction(e -> {
			replay();
			playComputer = true;
			label.setText("Current round: "+ round + ". You "
					+ "play with Computer: "+ playComputer);
		});
		hbox4 = new HBox(10);
		hbox4.setAlignment(Pos.BOTTOM_CENTER);
		hbox4.getChildren().addAll(btRePlay, btLevel, btPlayComputer);
		root.getChildren().addAll(hbox1, hbox2, hbox3, label, hbox4);
		scene = new Scene(root,400,500);
		player1  = new ArrayList<Integer>(5);
		player2  = new ArrayList<Integer>(5);
		rand = new Random();
		colors = Arrays.asList(Color.BLUE, Color.BLACK, Color.ORANGE, Color.GREEN, Color.YELLOW,
				Color.DEEPPINK, Color.GREENYELLOW, Color.CHOCOLATE, Color.LIGHTGREEN);
		colorsNames = Arrays.asList("BLUE", "BLACK", "ORANGE", "GREEN", "YELLOW",
				"DEEPPINK", "GREENYELLOW", "CHOCOLATE", "LIGHTGREEN");
		playersColors = getPlayerColor();
	}

	private Color[] getPlayerColor() {
		Color[] pc = new Color[2];
		int l = colors.size();
		int n = rand.nextInt(l);
		int m = rand.nextInt(l);
		while(n==m) {
			m = rand.nextInt(l);
		}
		pc[0]=colors.get(n);
		pc[1]=colors.get(m);
		return pc;
	}

	public static void main(String[] args) {
		Application.launch(args);;
	}
}
