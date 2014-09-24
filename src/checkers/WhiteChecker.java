package checkers;

import javax.swing.ImageIcon; 

/**
 * Represent a white piece, which means that the piece moves upwards.
 */
public class WhiteChecker extends Checker {
   
   public WhiteChecker(Coordinate c) {
      checkerState = new NormalStateWhite();
      position = c;
      value = WHITE_VALUE_NORMAL;
      stringRep = "O";
   }
   
   
   public int getColor() {
      return WHITE;
   }
  
  
   public ImageIcon getIcon() {
      if (value == WHITE_VALUE_NORMAL)
         //return CheckersApplet.getWhiteCheckerIcon();
         return new ImageIcon("images/white.gif");
      else
         //return CheckersApplet.getWhiteKingCheckerIcon();
         return new ImageIcon("images/whiteKing.gif");
   }

   
   public void makeKing() {
      checkerState = new KingState();
      value = WHITE_VALUE_KING;
      stringRep = "W";
   }
   
   
   public boolean kingRow() {
      return ( (position.get() >= 1) && (position.get() <= 4) );
   }
   
   
   public boolean isKing() {
      return (value == WHITE_VALUE_KING);
   }
   
   
   public Checker copy() {
      Checker newChecker = new WhiteChecker(position);
      if (value == WHITE_VALUE_KING)
         newChecker.makeKing();
      return newChecker;
   }
}