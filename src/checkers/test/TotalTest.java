package checkers.test;

import junit.framework.*;

public class TotalTest extends TestCase {

   public TotalTest(String name) {
      super(name);
   }
  
   public static Test suite() {
      TestSuite suite = new TestSuite();      
      suite.addTestSuite(checkers.test.BoardTest.class);
      suite.addTestSuite(checkers.test.CoordinateTest.class);
      suite.addTestSuite(checkers.test.RulesTest.class);
      suite.addTestSuite(checkers.test.MoveListTest.class);
      suite.addTestSuite(checkers.test.BoardListTest.class);
      suite.addTestSuite(checkers.test.CheckersGameTest.class);
      return suite;
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(TotalTest.suite());      
   }
}