package checkers.gui;

import checkers.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MainFrame extends JFrame {

   // GUI fields.
   private JMenuBar menuBar = new JMenuBar();
   private JMenu checkersMenu = new JMenu("Options");
   private JMenu player1LevelMenu = new JMenu("Player1 Level");
   private JMenu player2LevelMenu = new JMenu("Player2 Level");
   private JMenu player1StrategyMenu = new JMenu("Player1 Strategy");
   private JMenu player2StrategyMenu = new JMenu("Player2 Strategy");
   private JMenu gameTypeMenu = new JMenu("Game Type");

   private JRadioButtonMenuItem player1Level1;
   private JRadioButtonMenuItem player1Level2;
   private JRadioButtonMenuItem player1Level3;
   private JRadioButtonMenuItem player2Level1;
   private JRadioButtonMenuItem player2Level2;
   private JRadioButtonMenuItem player2Level3;
   private JRadioButtonMenuItem uservsMachine;
   private JRadioButtonMenuItem machinevsMachine;
   private JRadioButtonMenuItem player1Strategy1;
   private JRadioButtonMenuItem player1Strategy2;
   private JRadioButtonMenuItem player1Strategy3;
   private JRadioButtonMenuItem player2Strategy1;
   private JRadioButtonMenuItem player2Strategy2;
   private JRadioButtonMenuItem player2Strategy3;
   
   
   private ButtonGroup player1LevelGroup;
   private ButtonGroup player2LevelGroup;
   private ButtonGroup player1StrategyGroup;
   private ButtonGroup player2StrategyGroup;
   private ButtonGroup gameTypeGroup;
   
   private JPanel mainPanel = new JPanel();
   private JTextArea outputPanel = new JTextArea(3,20);
   private Cursor thinkCursor = new Cursor(Cursor.WAIT_CURSOR);
   private Cursor moveCursor = new Cursor(Cursor.DEFAULT_CURSOR);
   String output = "";  
   
   // Levels.
   private static final int LEVEL1 = 3;
   private static final int LEVEL2 = 5;
   private static final int LEVEL3 = 8;
   private int thinkDepth1 = LEVEL1; 
   private int thinkDepth2 = LEVEL3;// Default.
   
   // States.
   private static final int FROM = 1;  
   private static final int TO = 2;
   private static final int FROM_MULTIPLE = 3;
   private static final int TO_MULTIPLE = 4;
   private static final int COMPUTER_THINKS = 5;
   private static final int NOT_STARTED = 6;
   private int state = NOT_STARTED; 
   

   //algorithms
   private static final int MINIMAX=1;
   private static final int MINIMAXAB=2;
   private static final int RANDOM=3;
   private int strategyP1=MINIMAXAB;//the default should be MINIMAXAB
   private int strategyP2=RANDOM; 
   
   //Game Type
   private static final int USER_VS_MACHINE=1;
   private static final int MACHINE_VS_MACHINE=2;
   private int gameType = USER_VS_MACHINE;//the default should be UvsM
   private int currentTurn = Checker.WHITE;

   private int nextTurn(int currentTurn){
      if(currentTurn==Checker.WHITE)
            return Checker.BLACK;
      else
            return Checker.WHITE;
   }
   
   private Checker multipleJumpsChecker = null;
   private int player1Color = Checker.WHITE;
   private int player2Color = Checker.BLACK;
   private Coordinate from;
   private Board board = new Board();
   private static boolean seenStartDialog = false;
   
   
   // ********** GUI ************
   private void initGui() {
      Toolkit toolkit = getToolkit();
      setTitle("Checkers");
      Dimension size = toolkit.getScreenSize();
      setBounds(size.width/4, size.height/4, 400, 470);
      setResizable(true);
      setJMenuBar(menuBar);

      player1LevelMenu.setEnabled(false);
      player1StrategyMenu.setEnabled(false);
      
      menuBar.add(checkersMenu);
      checkersMenu.add(new StartAction("New game"));
      checkersMenu.add(new ResetAction("Reset board"));
     
      // Level radio buttons.
      checkersMenu.add(player1LevelMenu);   
      player1Level1 = new JRadioButtonMenuItem("Level 1", thinkDepth1 == LEVEL1);
      player1Level2 = new JRadioButtonMenuItem("Level 2", thinkDepth1== LEVEL2);
      player1Level3 = new JRadioButtonMenuItem("Level 3", thinkDepth1== LEVEL3);
      
      checkersMenu.add(player2LevelMenu);
      player2Level1 = new JRadioButtonMenuItem("Level 1", thinkDepth2== LEVEL1);
      player2Level2 = new JRadioButtonMenuItem("Level 2", thinkDepth2== LEVEL2);
      player2Level3 = new JRadioButtonMenuItem("Level 3", thinkDepth2== LEVEL3);
      
      
      player1Level1.addActionListener(new Player1LevelListener(LEVEL1));
      player1Level2.addActionListener(new Player1LevelListener(LEVEL2));
      player1Level3.addActionListener(new Player1LevelListener(LEVEL3));
      player1LevelGroup = new ButtonGroup();
      player1LevelGroup.add(player1Level1);
      player1LevelGroup.add(player1Level2);
      player1LevelGroup.add(player1Level3);
      player1LevelMenu.add(player1Level1);
      player1LevelMenu.add(player1Level2);
      player1LevelMenu.add(player1Level3);
      
      player2Level1.addActionListener(new Player2LevelListener(LEVEL1));
      player2Level2.addActionListener(new Player2LevelListener(LEVEL2));
      player2Level3.addActionListener(new Player2LevelListener(LEVEL3));
      player2LevelGroup = new ButtonGroup();
      player2LevelGroup.add(player2Level1);
      player2LevelGroup.add(player2Level2);
      player2LevelGroup.add(player2Level3);
      player2LevelMenu.add(player2Level1);
      player2LevelMenu.add(player2Level2);
      player2LevelMenu.add(player2Level3);
      
      // strategy radio buttons.
      checkersMenu.add(player1StrategyMenu);   
      player1Strategy1 = new JRadioButtonMenuItem("Minimax", strategyP1== MINIMAX);
      player1Strategy2 = new JRadioButtonMenuItem("MinimaxAB", strategyP1== MINIMAXAB);
      player1Strategy3 = new JRadioButtonMenuItem("Random", strategyP1== RANDOM);
      
      checkersMenu.add(player2StrategyMenu);
      player2Strategy1 = new JRadioButtonMenuItem("Minimax", strategyP2== MINIMAX);
      player2Strategy2 = new JRadioButtonMenuItem("MinimaxAB", strategyP2== MINIMAXAB);
      player2Strategy3 = new JRadioButtonMenuItem("Random", strategyP2== RANDOM);
      
      
      player1Strategy1.addActionListener(new Player1StrategyListener(MINIMAX));
      player1Strategy2.addActionListener(new Player1StrategyListener(MINIMAXAB));
      player1Strategy3.addActionListener(new Player1StrategyListener(RANDOM));
      player1StrategyGroup = new ButtonGroup();
      player1StrategyGroup.add(player1Strategy1);
      player1StrategyGroup.add(player1Strategy2);
      player1StrategyGroup.add(player1Strategy3);
      player1StrategyMenu.add(player1Strategy1);
      player1StrategyMenu.add(player1Strategy2);
      player1StrategyMenu.add(player1Strategy3);
      
      player2Strategy1.addActionListener(new Player2StrategyListener(MINIMAX));
      player2Strategy2.addActionListener(new Player2StrategyListener(MINIMAXAB));
      player2Strategy3.addActionListener(new Player2StrategyListener(RANDOM));
      player2StrategyGroup = new ButtonGroup();
      player2StrategyGroup.add(player2Strategy1);
      player2StrategyGroup.add(player2Strategy2);
      player2StrategyGroup.add(player2Strategy3);
      player2StrategyMenu.add(player2Strategy1);
      player2StrategyMenu.add(player2Strategy2);
      player2StrategyMenu.add(player2Strategy3);
      
      checkersMenu.add(gameTypeMenu);
      uservsMachine = new JRadioButtonMenuItem("User vs Machine",gameType==USER_VS_MACHINE);
      machinevsMachine = new JRadioButtonMenuItem("Machine vs Machine",gameType==MACHINE_VS_MACHINE);
      uservsMachine.addActionListener(new GameTypeListener(USER_VS_MACHINE));
      machinevsMachine.addActionListener(new GameTypeListener(MACHINE_VS_MACHINE));
      gameTypeGroup = new ButtonGroup();
      gameTypeGroup.add(uservsMachine);
      gameTypeGroup.add(machinevsMachine);
      gameTypeMenu.add(uservsMachine);
      gameTypeMenu.add(machinevsMachine);
      
      
      
      // The rest of the buttons.
      
      //checkersMenu.add(new AboutAction("About Checkers"));
      //checkersMenu.add(new RulesAction("Rules of Checkers", this));
      //checkersMenu.add(new ExitAction("Exit"));
      /*exitButton.setBounds(300,250,100,30);
      exitButton.addActionListener(new ExitAction("Exit"));
      mainPanel.add(exitButton);*/
      mainPanel.setLayout(new GridLayout(8,8,0,0));
      createGrid();
      outputPanel.setEditable(false);
      outputPanel.setBackground(Color.lightGray);
      JScrollPane scrollOutputPane = new JScrollPane(outputPanel);
      scrollOutputPane.setVerticalScrollBarPolicy(
                       JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      getContentPane().add(mainPanel, BorderLayout.CENTER);
      getContentPane().add(scrollOutputPane, BorderLayout.SOUTH);
      enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      
      
      //SOME TEST
      System.out.println("player1:"+player1Color+"  "+thinkDepth1);
      System.out.println("player2:"+player2Color+"  "+thinkDepth2);
   }
   
   
   protected void processWindowEvent(WindowEvent e) {
      if (e.getID() == WindowEvent.WINDOW_CLOSING) {
         dispose();
         System.exit(0);
      }
      super.processWindowEvent(e);
   }
        
   
   private void createGrid() {      
      for (int y = 0; y < 8; y++)
         for (int x = 0; x < 8; x++)
            if (y % 2 == 0)
               if (x % 2 == 0) 
                  mainPanel.add(new CheckerLabel());
               else 
                  mainPanel.add(
                        new CheckerButton(findCoordinateEvenRow(x,y), this));
            else 
               if (x % 2 == 0)
                  mainPanel.add(
                        new CheckerButton(findCoordinateOddRow(x,y), this));
               else 
                  mainPanel.add(new CheckerLabel());
   }
   
   
   // Paints the board (model) on the gui (view). 
   private void updateGrid() {
      JButton button = null;
      for (int i = 1; i < 33; i++) {
         button = (JButton) mainPanel.getComponent(findSquare(i));
         if (board.getChecker(new Coordinate(i)) != null) {
            button.setText("");           
            button.setIcon(board.getChecker(new Coordinate(i)).getIcon());
         }
         else {
            button.setText("" + i);
            button.setIcon(null);
         }
      }
   }
   
  
   private void outputText(String s) {
      output = "\n> " + s;
      //output = "> " + s + "\n" + output;
      outputPanel.setText(output);
   }


   
   
   // ********** Functionality ***********
   
   public MainFrame() {
      initGui();
      board.initialize();
      updateGrid();
   }


   public static void main(String args[]) {
      MainFrame m = new MainFrame();
      m.setVisible(true);
   }
   
   
   /**
    * Performs an action depending on the state of the game. It gets the action
    * from the CheckerButton class.
    */
   public void handleButtonEvents(Coordinate c) {
	  System.out.println("handleButtonEvents !!!!"+currentTurn);
	  if (gameType == USER_VS_MACHINE){
		 if (state == NOT_STARTED)
		         outputText("Please choose begin game fra the menu.");
		 else
			 if (state == COMPUTER_THINKS)
				 outputText("You cannot move while the computer is thinking.");
	         else 
	            switch(state) {
	               case FROM:
	                  from = c;
	                  state = TO;
	                  break;
	               case TO:
	                  moveUser(from, c);
	                  break;
	               case FROM_MULTIPLE:
	                  from = c;
	                  state = TO_MULTIPLE;
	                  break;
	               case TO_MULTIPLE:
	                  multipleJumps(from, c);
	                  break;
	            }        
	  }else{
		  if (state == COMPUTER_THINKS)
            outputText("You cannot move while the computer is thinking.");
		  else
			  computerMovesGeneral(currentTurn);
		  state = FROM;
	  }
   }
   public void computerMovesGeneral(int currentTurn){
	   int strategy;
	   if (currentTurn == player1Color)
		   strategy = strategyP1;
	   else
		   strategy = strategyP2;
	   
	   if (strategy == MINIMAX)
		  computerMoves(currentTurn);
	   else if (strategy == MINIMAXAB)
		  computerMovesAB(currentTurn);
	   else
		  computerMovesRandom(currentTurn);
   }
   public void computerMovesGeneral(){
	   computerMovesGeneral(Checker.BLACK);
   }
      
   
   // The computer thinks.... USING MINIMAX WITHOUT ALPHA-BETA PRUNING
   public void computerMoves(int currentTurn) {
      MoveList validMoves = Rules.findAllValidMoves(board, currentTurn);
      if (validMoves.size() == 0) {
         JOptionPane.showMessageDialog((Component)this, "\nCongratualations!" +
         "You win\n", "Checkers", JOptionPane.INFORMATION_MESSAGE);
         outputText("Congratulations. You win.");
         state = NOT_STARTED;
      }
      else {
    	  int thinkDepth;
    	  if (currentTurn==player1Color)
    		  thinkDepth=thinkDepth1;
    	  else
    		  thinkDepth=thinkDepth2;
         board.getHistory().reset();
         setCursor(thinkCursor);
         Board comBoard = Rules.minimax(board, thinkDepth, currentTurn);
         setCursor(moveCursor);                                          
         Move move = comBoard.getHistory().first();
         board = Rules.executeMove(move, board);
         updateGrid();
         MoveIterator iterator = board.getHistory().getIterator();
         String moves = "";
         while (iterator.hasNext()) {
            moves = moves + iterator.next();
            if (iterator.hasNext()) moves = moves + " , ";
         }      
         outputText("Computer moves: "+currentTurn + moves);
         state = FROM;
         this.currentTurn = nextTurn(currentTurn);
         validMoves = Rules.findAllValidMoves(board, this.currentTurn);
         if (validMoves.size() == 0) {
        	 String mensj;
        	 if (nextTurn(this.currentTurn) == player1Color)
        		 mensj = "White";
        	 else
        		 mensj = "Black";
        	mensj="Player "+mensj+
        			" Wins!! ThinkDepth:" + thinkDepth; 
            JOptionPane.showMessageDialog((Component)this, mensj,
            		"Checkers", JOptionPane.INFORMATION_MESSAGE);
            outputText(mensj);
            state = NOT_STARTED;
         }
      }           
   }  
   
   // The computer thinks.... USING ALPHA-BETA PRUNING
   public void computerMovesAB(int currentTurn) {
	      MoveList validMoves = Rules.findAllValidMoves(board, currentTurn);
	      if (validMoves.size() == 0) {
	         JOptionPane.showMessageDialog((Component)this, "\nCongratualations!" +
	         "You win\n", "Checkers", JOptionPane.INFORMATION_MESSAGE);
	         outputText("Congratulations. You win.");
	         state = NOT_STARTED;
	      }
	      else {
	    	  int thinkDepth;
	    	  if (currentTurn==player1Color)
	    		  thinkDepth=thinkDepth1;
	    	  else
	    		  thinkDepth=thinkDepth2;
	    	  
	         board.getHistory().reset();
	         setCursor(thinkCursor);
	         Board comBoard = Rules.minimaxAB(board, thinkDepth, currentTurn, 
	                                          Rules.minusInfinityBoard(),
	                                          Rules.plusInfinityBoard());
	         setCursor(moveCursor);                                          
	         Move move = comBoard.getHistory().first();
	         board = Rules.executeMove(move, board);
	         updateGrid();
	         MoveIterator iterator = board.getHistory().getIterator();
	         String moves = "";
	         while (iterator.hasNext()) {
	            moves = moves + iterator.next();
	            if (iterator.hasNext()) moves = moves + " , ";
	         }      
	         outputText("Computer moves: " +currentTurn+ moves);
	         state = FROM;
	         this.currentTurn = nextTurn(currentTurn);
	         validMoves = Rules.findAllValidMoves(board, this.currentTurn);
	         if (validMoves.size() == 0) {
	        	 String mensj;
	        	 if (nextTurn(this.currentTurn) == player1Color)
	        		 mensj = "White";
	        	 else
	        		 mensj = "Black";
	        	mensj="Player "+mensj+
	        			" Wins!! ThinkDepth:" + thinkDepth; 
	            JOptionPane.showMessageDialog((Component)this, mensj,
	            		"Checkers", JOptionPane.INFORMATION_MESSAGE);
	            outputText(mensj);
	            state = NOT_STARTED;
	         }
	      }           
	   }
// The computer thinks.... NEW STATE RANDOMLY SELECTED
   public void computerMovesRandom(int currentTurn) {
	      MoveList validMoves = Rules.findAllValidMoves(board, currentTurn);
	      if (validMoves.size() == 0) {
	         JOptionPane.showMessageDialog((Component)this, "\nCongratualations!" +
	         "You win\n", "Checkers", JOptionPane.INFORMATION_MESSAGE);
	         outputText("Congratulations. You win.");
	         state = NOT_STARTED;
	      }
	      else {
	    	 Random rand = new Random();
		     Move newMove=validMoves.get(rand.nextInt(validMoves.size()));
	         CheckerState checker = null;
	         if (currentTurn == Checker.BLACK){
	        	 checker = new NormalStateBlack();
	         }else{
	        	 checker = new NormalStateWhite();
	         }
	         MoveList newMoveList = new MoveList();
	         checker.findValidJumps(newMove.getChecker(), board, newMoveList);
	         MoveIterator iterator = newMoveList.getIterator();
	         if (newMoveList.size() == 0){
		         board.getHistory().reset();               
		         board = Rules.executeMove(newMove, board);
		         updateGrid();
	         }
	         String moves = "";
	         while (iterator.hasNext()) {
	        	 newMove = iterator.next();
	            moves = moves + newMove;
	            board = Rules.executeMove(newMove, board);
	            updateGrid();
	            if (iterator.hasNext()) moves = moves + " , ";
	         }      
	         outputText("Computer moves: "+currentTurn + moves);
	         state = FROM;
	         this.currentTurn = nextTurn(currentTurn);
	         validMoves = Rules.findAllValidMoves(board, this.currentTurn);
	         if (validMoves.size() == 0) {
	        	 int thinkDepth;
		    	  if (currentTurn==player1Color)
		    		  thinkDepth=thinkDepth1;
		    	  else
		    		  thinkDepth=thinkDepth2;
		    	  
	        	 String mensj;
	        	 if (nextTurn(this.currentTurn) == player1Color)
	        		 mensj = "White";
	        	 else
	        		 mensj = "Black";
	        	mensj="Player "+mensj+
	        			" Wins!! ThinkDepth:" + thinkDepth; 
	            JOptionPane.showMessageDialog((Component)this, mensj,
	            		"Checkers", JOptionPane.INFORMATION_MESSAGE);
	            outputText(mensj);
	            state = NOT_STARTED;
	         }
	      }           
	   }

   
   /**
    * A move is validated. If it is a normal move the method checks that there 
    * is no mandatory jumps to be made. If the move is a jump the method 
    * checks if a multiple jump is possible.
    */
   public void moveUser(Coordinate from, Coordinate to) {
	      Move move = validateUserMove(from, to);
	      if (move == null) {
	         outputText("Invalid move.");
	         state = FROM;
	      }
	      else {
	         if (move.isJump()) {
	            board = Rules.executeUserJump(move, board);
	            outputText("You move: " + move);
	            updateGrid();
	            multipleJumpsChecker = board.getChecker(move.getDestination());
	            if (mandatoryJump(multipleJumpsChecker, board)) {
	               outputText("A multiple jump must be completed.");
	               state = FROM_MULTIPLE;
	            }
	            else {
	               state = COMPUTER_THINKS;
	               this.currentTurn = nextTurn(this.currentTurn);
	               computerMovesGeneral();
	            }       
	         }
	         else {   // Normal move.
	            if (Rules.existJump(board, player1Color)) {
	               outputText("Invalid move. If you can jump, you must.");
	               state = FROM;
	            }
	            else {
	               board = Rules.executeMove(move, board);
	               outputText("You move: " + move);
	               updateGrid();
	               state = COMPUTER_THINKS;
	               this.currentTurn = nextTurn(this.currentTurn);
	               computerMovesGeneral();
	            }             
	         }
	      }
	   }
   
   
   // Returns true if checker can make a jump.
   private boolean mandatoryJump(Checker checker, Board board) {
      MoveList movelist = new MoveList();
      checker.findValidJumps(movelist, board);
      if (movelist.size() != 0)
         return true;
      else
         return false;
   }
   
   
   /**
    * Checks that the entered move is a jump and that the multiple jumps is 
    * performed with the same checker.
    */
   private void multipleJumps(Coordinate from, Coordinate to) {  
      Move move = validateUserMove(from, to);
      if (move == null) {
         outputText("Invalid move.");
         state = FROM_MULTIPLE;
      }
      else {
         if (!move.isJump()) {
            outputText("Invalid move. Must be a jump.");
            state = FROM_MULTIPLE;
         }
         else {
            if (!multipleJumpsChecker.getPosition().equals(from)) {
               outputText("Invalid move. Must be a jump with the same checker.");
               state = FROM_MULTIPLE;
            }   
            else {
               board = Rules.executeUserJump(move, board);
               outputText("You move: " + move);
               updateGrid();
               multipleJumpsChecker = board.getChecker(move.getDestination());
               if (mandatoryJump(multipleJumpsChecker, board)) {
                  outputText("A multiple jump must be completed.");
                  state = FROM_MULTIPLE;
               }
               else {
                  state = COMPUTER_THINKS;
                  this.currentTurn = nextTurn(this.currentTurn);
                  computerMovesGeneral();
               }   
            }    
         }
      }
   }

      
   // Returns a valid move entered by the user otherwise null.
   public Move validateUserMove(Coordinate from, Coordinate to) {
      Move move = null;   
      Checker checker = board.getChecker(from);
      if (checker != null) {
         if (player1Color == Checker.WHITE) {   
            if (checker.getColor() == Checker.WHITE) {  
               if (checker.getValue() == Checker.WHITE_VALUE_KING) {
                  if (Math.abs(from.row() - to.row()) == 1) {
                     if (Rules.validKingMove(from, to, board))
                        move = new MoveNormal(checker, to);
                  }
                  else
                     if (Rules.validKingJump(from, to, board))
                        move = new MoveJump(checker, to);
               }           
               else {  // Normal white checker. 
                  if (from.row() - to.row() == 1) { 
                     if (Rules.validWhiteMove(from, to, board)) 
                        move = new MoveNormal(checker, to);
                  }
                  else
                     if (Rules.validWhiteJump(from, to, board))
                        move = new MoveJump(checker, to);       
               }
            }
         }
         else {   // User is black.
            if (checker.getColor() == Checker.BLACK) {  
               if (checker.getValue() == Checker.BLACK_VALUE_KING) {
                  if (Math.abs(from.row() - to.row()) == 1) {
                     if (Rules.validKingMove(from, to, board)) 
                        move = new MoveNormal(checker, to);
                  }
                  else
                     if (Rules.validKingJump(from, to, board)) 
                        move = new MoveJump(checker, to);
               }           
               else {   // Normal black checker.
                  if (to.row() - from.row() == 1) {
                     if (Rules.validBlackMove(from, to, board))
                        move = new MoveNormal(checker, to);
                  }
                  else
                     if (Rules.validBlackJump(from, to, board)) 
                        move = new MoveJump(checker, to);
               }
            }
         }
      }
      return move;
   }
   
   
   // Transform a coordinate from the Board grid (1-32) to the GUI grid (0-63).
   private int findSquare(int c) {
      int row = new Coordinate(c).row();
      if (row % 2 == 0)
         return (c * 2) - 1;
      else
         return (c * 2) - 2; 
   }
   
   
   // Returns the board coordinate (1-32) from the square coordinates (0-7,0-7).
   // The square coordinate must be at an even row.
   private int findCoordinateEvenRow(int x, int y) {
      return ((int) Math.ceil(x/2.0)) + (y * 4);   
   }
   
   
   private int findCoordinateOddRow(int x, int y) {
      return ((int) Math.ceil(x/2.0)) + (y * 4) + 1;   
   }
   
   
   // *****************************************************
   // Inner classes for menu items.
   
   class StartAction extends AbstractAction {   
      StartAction(String name) {
         super(name);
      }
      
      public void actionPerformed(ActionEvent e) {
         player1LevelMenu.setEnabled(false);
         player2LevelMenu.setEnabled(false);

         gameTypeMenu.setEnabled(false);
         board.initialize();
         updateGrid();
         if (! seenStartDialog) {
            JOptionPane.showMessageDialog((Component)e.getSource(), "To move a " +
            "piece click with the mouse on the square that the piece occupy. " +
            "Then click on the empty square that you want to move to.", "Checkers", 
            JOptionPane.INFORMATION_MESSAGE);
            seenStartDialog = true;  
         }
         if(gameType==MACHINE_VS_MACHINE){
            currentTurn = Checker.WHITE;
            computerMovesGeneral(currentTurn);
         }
         else {
            outputText("Please make the first move");
            state = FROM;
         }     
      }
   }
   
   
   class ResetAction extends AbstractAction {   
      ResetAction(String name) {
         super(name);
      }
      
      public void actionPerformed(ActionEvent e) {
         player1LevelMenu.setEnabled(true);
         player2LevelMenu.setEnabled(true);
         board.initialize();
         updateGrid();
         state = NOT_STARTED;
      }
   }
   
   class Player1LevelListener implements ActionListener  {
      private int level;
    
      Player1LevelListener(int l) {
         level = l;
      }
    
      public void actionPerformed(ActionEvent e) {
         thinkDepth1= level;
      }
   }
   
   class Player2LevelListener implements ActionListener  {
      private int level;
    
      Player2LevelListener(int l) {
         level = l;
      }
    
      public void actionPerformed(ActionEvent e) {
         thinkDepth2 = level;
      }
   }
   
   class Player1StrategyListener implements ActionListener  {
      private int strategy;
    
      Player1StrategyListener(int s) {
         strategy = s;
      }
    
      public void actionPerformed(ActionEvent e) {
         strategyP1= strategy;
      }
   }
   
   class Player2StrategyListener implements ActionListener  {
      private int strategy;
    
      Player2StrategyListener(int s) {
         strategy = s;
      }
    
      public void actionPerformed(ActionEvent e) {
         strategyP2= strategy;
      }
   }
   
   
   
   class GameTypeListener implements ActionListener  {
      private int tg;
     
      GameTypeListener(int t) {
         tg =t;
      }
    
      public void actionPerformed(ActionEvent e) {
         if(tg==2){
            
             player1LevelMenu.setEnabled(true);
             player2LevelMenu.setEnabled(true);
             player1StrategyMenu.setEnabled(true);
             player2StrategyMenu.setEnabled(true);
         }else{
             player1LevelMenu.setEnabled(false);
             player2LevelMenu.setEnabled(true);

         }
         gameType = tg;
      }
   }
   
}