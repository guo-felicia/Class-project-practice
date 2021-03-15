import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library
import javalib.worldcanvas.WorldCanvas;

import java.awt.Color; // general colors (as triples of red,green,blue values)

interface ITree {
  // draws the ITree to a picture
  WorldImage draw();

  // determine whether any of the twigs in the tree (either stems or branches) are
  // pointing downward rather than upward.
  boolean isDrooping();

  // produces a Branch using the given arguments, with this tree on the left and
  // the given tree on the right
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree);

  // helper1: convert the angle from the given left/right angle
  ITree combinHelp(double angle);

  // returns the width of the tree
  double getWidth();

  // helper2 compare the width of left and right tree
  // return the maximum number of width
  double getLeftMax(double leftmost, double leftcurrent);

  // get the max distance from the tree to the origin
  double getRightMax(double rightmost, double rightcurrent);
}

class Leaf implements ITree {
  // represents the radius of the leaf
  int size;
  // the color to draw it
  Color color;

  Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }

  /* Template:
   * Fields:
   * this.size -- int
   * this.color -- color
   * 
   * Methods:
   * this.draw() -- WorldImage
   * this.isDrooping() -- boolean
   * this.combine(int leftLength, int rightLength, 
   * double leftTheta, double rightTheta,ITree otherTree) -- ITree
   * this.combineHelp(double angle) -- ITree
   * this.getWidh() -- double
   * this.getLeftMax() -- double
   * this.getRightMax() -- double
   * 
   */

  public WorldImage draw() {
    return new CircleImage(this.size, OutlineMode.SOLID, this.color);
  }

  public boolean isDrooping() {
    return false;
  }

  public ITree combinHelp(double angle) {
    return this;
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this,
        otherTree.combinHelp((rightTheta)));
  }

  public double getWidth() {
    return this.getRightMax(0, 0) - this.getLeftMax(0, 0);
  }

  public double getLeftMax(double leftmost, double leftcurrent) {
    double lc = -this.size + leftcurrent;

    if (lc <= leftmost) {
      if (lc <= 0) {
        return lc;
      }
      else {
        return 0;
      }
    }
    else {
      return leftmost;
    }
  }

  public double getRightMax(double rightmost, double rightcurrent) {
    double rc = this.size + rightcurrent;
    if (rc >= rightmost) {
      return rc;
    }
    else {
      return rightmost;
    }
  }
}

class Stem implements ITree {
  // How long this stick is
  int length;
  // The angle (in degrees) of this stem, relative to the +x axis
  double theta;
  // The rest of the tree
  ITree tree;

  Stem(int length, double theta, ITree tree) {
    this.length = length;
    this.theta = theta;
    this.tree = tree;
  }
  
  //In Stem
  /* Template:
   * Fields:
   * this.length -- int
   * this.theta -- double
   * this.tree -- ITree
   * 
   * Methods:
   * this.posnX() -- double
   * this.posnY() -- double
   * this.getPosn() -- Posn
   * this.drawLeftLine() -- WorldImage
   * this.drawRightLine() -- WorldImage
   * this.draw() -- WorldImage
   * this.isDrooping() -- boolean
   * this.combine(int leftLength, int rightLength, 
   * double leftTheta, double rightTheta,ITree otherTree) -- ITree
   * this.combineHelp(double angle) -- ITree
   * this.getLeftWidth() -- double
   * this.getRightWidth() -- double
   * this.getWidh() -- double
   * this.getLeftMax() -- double
   * this.getRightMax() -- double
   * 
   * Methods of fields:
   * this.tree.draw() -- WorldImage
   * this.tree.combinHelp(angle) -- ITree
   * this.tree.getWidthMax() -- double
   */

  public double posnX() {
    double a = Math.PI - (this.theta * Math.PI / 180);
    return this.length * Math.cos(a);
  }

  public double posnY() {
    double a = Math.PI - (this.theta * Math.PI / 180);
    return this.length * Math.sin(a);
  }

  // helper get Posn:
  public Posn getPosn() {
    return new Posn((int) this.posnX(), (int) this.posnY());
  }

  // helper: draw a line pointing to left:
  public WorldImage drawLeftLine() {
    if (this.theta >= 90 && this.theta < 180) {
      return new LineImage(this.getPosn(), Color.GRAY).movePinhole(-(this.posnX() / 2),
          -(this.posnY() / 2));
    }
    else {
      return new LineImage(this.getPosn(), Color.GRAY).movePinhole(-(this.posnX() / 2),
          (this.posnY() / 2));
    }
  }

  public WorldImage drawRightLine() {
    if (this.theta < 90) {
      return new LineImage(this.getPosn(), Color.GRAY).movePinhole((this.posnX() / 2),
          -1 * this.posnY() / 2);
    }
    else {
      return new LineImage(this.getPosn(), Color.GRAY).movePinhole((this.posnX() / 2),
          (this.posnY() / 2));
    }
  }

  public WorldImage draw() {
    if (this.theta >= 90 && this.theta <= 270) {
      return new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE, this.tree.draw(), 0, 0,
          this.drawLeftLine()).movePinhole(-1 * this.posnX(), -1 * this.posnY());
    }
    else {
      return new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE, this.tree.draw(), 0, 0,
          this.drawRightLine()).movePinhole(-1 * this.posnX(), -1 * this.posnY());
    }
  }

  public boolean isDrooping() {
    return this.theta > 180 || this.tree.isDrooping();
  }

  public ITree combinHelp(double angle) {
    return new Stem(this.length, this.theta + angle, this.tree.combinHelp(angle));
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    double l = leftTheta - 90;
    double r = -(90 - rightTheta);
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.combinHelp(l),
        otherTree.combinHelp(r));
  }

  public double getWidth() {
    return this.getRightMax(0, 0) - this.getLeftMax(0, 0);
  }

  public double getLeftMax(double leftmost, double leftcurrent) {
    double a = this.theta * Math.PI / 180;
    double stemLength = this.length * Math.cos(a);
    double lc = stemLength + leftcurrent;
    if (lc <= leftmost) {
      return this.tree.getLeftMax(lc, lc);
    }
    else {
      return this.tree.getLeftMax(leftmost, lc);
    }
  }

  public double getRightMax(double rightmost, double rightcurrent) {
    double a = this.theta * Math.PI / 180;
    double stemLength = this.length * Math.cos(a);
    double rc = stemLength + rightcurrent;
    if (rc >= rightmost) {
      return this.tree.getRightMax(rc, rc);
    }
    else {
      return this.tree.getRightMax(rightmost, rc);
    }
  }
}

class Branch implements ITree {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;

  Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left,
      ITree right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftTheta = leftTheta;
    this.rightTheta = rightTheta;
    this.left = left;
    this.right = right;
  }
  
  //In Branch

  /* Template:
   * Fields:
   * this.length -- int
   * this.theta -- double
   * this.tree -- ITree
   * 
   * Methods:
   * this.posnLX() -- double
   * this.posnLY() -- double
   * this.posnRX() -- double
   * this.posnRY() -- double
   * this.getLPosn() -- Posn
   * this.getRPosn() -- Posn
   * this.drawLeftLine() -- WorldImage
   * this.drawRightLine() -- WorldImage
   * this.draw() -- WorldImage
   * this.isDrooping() -- boolean
   * this.combine(int leftLength, int rightLength, 
   * double leftTheta, double rightTheta,ITree otherTree) -- ITree
   * this.combineHelp(double angle) -- ITree
   * this.getLeftWidth() -- double
   * this.getRightWidth() -- doublethis.getWidth() -- double
   * this.getLeftMax() -- double
   * this.getRightMax() -- double
   * 
   * Methods of fields:
   * this.left.isDrooping() -- boolean
   * this.right.isDrooping() -- boolean
   * this.left.combinHelp(angle) -- ITree
   * this.right.combinHelp(angle) --ITree
   * this.left.getLeftMax() -- double
   * this.right.getRightMax() -- double
   */

  public double posnLX() {
    double a = Math.PI - (this.leftTheta * Math.PI / 180);
    return this.leftLength * Math.cos(a);
  }

  public double posnLY() {
    double a = Math.PI - (this.leftTheta * Math.PI / 180);
    return this.leftLength * Math.sin(a);
  }

  public double posnRX() {
    double a = Math.PI - (this.rightLength * Math.PI / 180);
    return this.rightLength * Math.cos(a);
  }

  public double posnRY() {
    double a = Math.PI - (this.rightLength * Math.PI / 180);
    return this.rightLength * Math.sin(a);
  }

  public Posn getLPosn() {
    return new Posn((int) this.posnLX(), (int) this.posnLY());
  }

  public Posn getRPosn() {
    return new Posn((int) this.posnRX(), (int) this.posnRY());
  }

  // helper: draw a line pointing to left:

  public WorldImage drawLeftLine() {
    if (this.leftTheta >= 90 && this.leftTheta < 180) {
      return new LineImage(this.getLPosn(), Color.GRAY).movePinhole(-(this.posnLX() / 2),
          -(this.posnLY() / 2));
    }
    else {
      return new LineImage(this.getLPosn(), Color.GRAY).movePinhole(-(this.posnLX() / 2),
          (this.posnLY() / 2));
    }
  }

  public WorldImage drawRightLine() {
    if (this.rightTheta < 90) {
      return new LineImage(this.getRPosn(), Color.GRAY)
          .movePinhole((this.posnRX() / 2 + this.rightLength), -(this.posnRY() / 2));
    }
    else {
      return new LineImage(this.getRPosn(), Color.GRAY).movePinhole((this.posnRX() / 2),
          (this.posnRY() / 2));
    }
  }

  public WorldImage draw() {
    WorldImage left = new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
        this.left.draw(), 0, 0, this.drawLeftLine());
    WorldImage right = new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
        this.right.draw(), 0, 0, this.drawRightLine());

    return new OverlayImage(left.movePinhole(-this.posnLX(), -this.posnLY()),
        right.movePinhole(-this.posnLX(), -this.posnLY()));
  }

  public boolean isDrooping() {
    return this.leftTheta > 180 || this.rightTheta > 180 || this.left.isDrooping()
        || this.right.isDrooping();
  }

  public ITree combinHelp(double angle) {
    return new Branch(this.leftLength, this.rightLength, this.leftTheta + angle,
        this.rightTheta + angle, this.left.combinHelp(angle), this.right.combinHelp(angle));
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    double l = leftTheta - 90;
    double r = -(90 - rightTheta);
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.combinHelp(l),
        otherTree.combinHelp(r));
  }

  public double getWidth() {
    return this.getRightMax(0, 0) - this.getLeftMax(0, 0);
  }

  public double getLeftMax(double leftmost, double leftcurrent) {
    double a = this.leftTheta * Math.PI / 180;
    double stemLength = this.leftLength * Math.cos(a);
    double lc = stemLength + leftcurrent;
    if (lc <= leftmost) {
      return this.left.getLeftMax(lc, lc);
    }
    else {
      return this.left.getLeftMax(leftmost, lc);
    }
  }

  public double getRightMax(double rightmost, double rightcurrent) {
    double a = this.rightTheta * Math.PI / 180;
    double stemLength = this.rightLength * Math.cos(a);
    double rc = stemLength + rightcurrent;
    if (rc >= rightmost) {
      return this.right.getRightMax(rc, rc);
    }
    else {
      return this.right.getRightMax(rightmost, rc);
    }
  }
}

class ExamplesTrees {
  ExamplesTrees() {
  }

  ITree l1 = new Leaf(5, Color.BLACK);
  ITree l2 = new Leaf(8, Color.PINK);
  ITree s1 = new Stem(20, 60, l1);
  ITree s2 = new Stem(25, 135, l2);
  ITree news2 = new Stem(25, 190, l2);
  ITree tree1 = new Branch(30, 30, 135, 40, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));
  ITree tree2 = new Branch(30, 30, 115, 65, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));
  ITree tree3 = new Branch(50, 50, 315, 65, new Leaf(35, Color.GREEN), new Leaf(18, Color.ORANGE));
  ITree tree1v = new Branch(30, 30, 195, 100, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));
  ITree tree1v2 = new Branch(30, 30, 155, 60, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));
  ITree tree2v = new Branch(30, 30, 55, 5, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));
  ITree tree2v2 = new Branch(30, 30, 195, 145, new Leaf(15, Color.GREEN),
      new Leaf(8, Color.ORANGE));
  ITree tree2v3 = new Branch(30, 30, 95, 45, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));
  ITree s3 = new Stem(40, 90, tree1);
  ITree s4 = new Stem(50, 90, tree2);
  ITree news4 = new Stem(50, 170, tree2v2);
  ITree s5 = new Stem(35, 195, l2);// draw check
  ITree s6 = new Stem(50, 285, tree1);// do we need to check ???
  ITree s7 = new Stem(50, 285, s5);
  ITree news6 = new Stem(50, 305, tree1v2);

  // Examples of combine
  ITree cobl1 = new Branch(10, 20, 120, 60, l1, l2);
  ITree cobl2 = new Branch(30, 25, 100, 80, l1, news4);
  ITree cobl3 = new Branch(15, 30, 135, 55, l2, news2);
  ITree cobl4 = new Branch(10, 25, 110, 70, news6, tree2v3);
  ITree b1 = tree1.combine(40, 50, 150, 30, tree2);
  ITree b1Check = new Branch(40, 50, 150, 30, tree1v, tree2v);

  // Posn for lines:
  double x = 20 * Math.cos(Math.PI - (135 * Math.PI / 180));
  double y = 20 * Math.sin(Math.PI - (135 * Math.PI / 180));

  boolean testImages(Tester t) {
    return t.checkExpect(l1.draw(), new CircleImage(5, OutlineMode.SOLID, Color.BLACK))
        && t.checkExpect(l2.draw(), new CircleImage(8, OutlineMode.SOLID, Color.PINK))
        && t.checkExpect(s2.draw(),
            new OverlayImage(new LineImage(new Posn((int) x, (int) y), Color.GRAY),
                new CircleImage(8, OutlineMode.SOLID, Color.PINK)))
        && t.checkExpect(b1.draw(), b1Check.draw());
  }

  boolean testDrawTree(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(s2.draw(), 250, 250)) && c.show();
  }

  boolean testIsDrooping(Tester t) {
    return t.checkExpect(this.l1.isDrooping(), false) 
        && t.checkExpect(this.s1.isDrooping(), false)
        && t.checkExpect(this.s5.isDrooping(), true)
        && t.checkExpect(this.tree1.isDrooping(), false)
        && t.checkExpect(this.tree3.isDrooping(), true)
        && t.checkExpect(this.s6.isDrooping(), true);
  }

  boolean testCombine(Tester t) {
    return t.checkExpect(this.tree1.combine(40, 50, 150, 30, tree2), b1Check)
        && t.checkExpect(this.l1.combine(10, 20, 120, 60, l2), cobl1)
        && t.checkExpect(this.l1.combine(30, 25, 100, 80, s4), cobl2)
        && t.checkExpect(this.l2.combine(15, 30, 135, 55, s2), cobl3)
        && t.checkExpect(this.s6.combine(10, 25, 110, 70, tree2), cobl4);
  }

  boolean testCombineHelp(Tester t) {
    return t.checkExpect(this.tree1.combinHelp(60), tree1v)
        && t.checkExpect(this.l1.combinHelp(30), l1) 
        && t.checkExpect(this.s2.combinHelp(55), news2)
        && t.checkExpect(this.s4.combinHelp(80), news4)
        && t.checkExpect(this.s6.combinHelp(20), news6);
  }

  boolean testGetWidth(Tester t) {
    return t.checkInexact(this.l1.getWidth(), 10.0, 0.1)
        && t.checkInexact(this.l2.getWidth(), 16.0, 0.1)
        && t.checkInexact(this.s1.getWidth(), 15.0, 0.1)
        && t.checkInexact(this.s3.getWidth(), 69.19, 0.1)
        && t.checkInexact(this.s5.getWidth(), 41.0, 0.1)
        && t.checkInexact(this.s7.getWidth(), 41.8, 0.01)
        && t.checkInexact(this.tree1.getWidth(), 69.19, 0.01)
        && t.checkInexact(this.tree2.getWidth(), 48.36, 0.01)
        && t.checkInexact(this.b1Check.getWidth(), 154.80, 0.01);// 82.17
  }

  boolean testGetLeftMax(Tester t) {
    return t.checkInexact(this.l1.getLeftMax(0, 0), -5.0, 0.1)
        && t.checkInexact(this.l2.getLeftMax(0, 0), -8.0, 0.1)
        && t.checkInexact(this.s1.getLeftMax(0, 0), 0.0, 0.1)
        && t.checkInexact(this.s3.getLeftMax(0, 0), -31.21, 0.1)
        && t.checkInexact(this.s5.getLeftMax(0, 0), -41.80, 0.1)
        && t.checkInexact(this.s7.getLeftMax(0, 0), -28.86, 0.01)
        && t.checkInexact(this.tree1.getLeftMax(0, 0), -31.21, 0.01)
        && t.checkInexact(this.tree2.getLeftMax(0, 0), -27.67, 0.01)
        && t.checkInexact(this.b1Check.getLeftMax(0, 0), -73.62, 0.01);
  }

  boolean testGetRightMax(Tester t) {
    return t.checkInexact(this.l1.getRightMax(0, 0), 5.0, 0.1)
        && t.checkInexact(this.l2.getRightMax(0, 0), 8.0, 0.1)
        && t.checkInexact(this.s1.getRightMax(0, 0), 15.0, 0.1)
        && t.checkInexact(this.s3.getRightMax(0, 0), 37.98, 0.1)
        && t.checkInexact(this.s7.getRightMax(0, 0), 12.94, 0.01)
        && t.checkInexact(this.tree1.getRightMax(0, 0), 37.98, 0.01)
        && t.checkInexact(this.tree2.getRightMax(0, 0), 20.67, 0.01)
        && t.checkInexact(this.b1Check.getRightMax(0, 0), 81.18, 0.01);
  }

}