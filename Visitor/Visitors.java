import tester.Tester;
import java.util.function.BiFunction;
import java.util.function.Function;

// an arithmetic expression interface
interface IArith {
  // accepts a visitor of type R
  <R> R accept(IArithVisitor<R> visitor);
}

// representing a visitor that visits an IArith
interface IArithVisitor<R> {

  R apply(IArith obj);

  R visitConst(Const c);

  R visitUnaryFormula(UnaryFormula uf);

  R visitBinaryFormula(BinaryFormula bf);
}

// EvalVisitor class
class EvalVisitor implements IArithVisitor<Double> {

  /* Template:
   * methods:
   * this.apply(IArith) -- Double
   * this.visitConst(Const) -- Double
   * this.visitUnaryFormula(UnaryFormula) -- Double
   * this.visitBinaryFormula(BinaryFormula) -- Double
   */
  
  // apply the visitor
  public Double apply(IArith obj) {
    return obj.accept(this);
  }

  // returns the constant
  public Double visitConst(Const c) {
    return c.num;
  }

  // evaluates a UnaryFormula
  public Double visitUnaryFormula(UnaryFormula uf) {
    return uf.func.apply(uf.child.accept(new EvalVisitor()));
  }

  // evaluates a BinaryFormula
  public Double visitBinaryFormula(BinaryFormula bf) {
    return bf.func.apply(bf.left.accept(new EvalVisitor()), bf.right.accept(new EvalVisitor()));
  }
}

// PrintVisitor class
class PrintVisitor implements IArithVisitor<String> {

  /* Template:
   * methods:
   * this.apply(IArith) -- String
   * this.visitConst(Const) -- String
   * this.visitUnaryFormula(UnaryFormula) -- String
   * this.visitBinaryFormula(BinaryFormula) -- String
   */
  
  public String apply(IArith obj) {
    return obj.accept(this);
  }

  // produces a constant as string
  public String visitConst(Const c) {
    return Double.toString(c.num);
  }

  // produces a string in Racket-like profix notation
  public String visitUnaryFormula(UnaryFormula uf) {
    return "(" + uf.name + " " + uf.child.accept(new PrintVisitor()) + ")";
  }

  // produces a string in Racket-like profix notation
  public String visitBinaryFormula(BinaryFormula bf) {
    return "(" + bf.name + " " + bf.left.accept(new PrintVisitor()) + " "
        + bf.right.accept(new PrintVisitor()) + ")";
  }
}

// DoubleVisitor class
class DoublerVisitor implements IArithVisitor<IArith> {

  /* Template:
   * methods:
   * this.apply(IArith) -- IArith
   * this.visitConst(Const) -- IArith
   * this.visitUnaryFormula(UnaryFormula) -- IArith
   * this.visitBinaryFormula(BinaryFormula) -- IArith
   */
  
  public IArith apply(IArith obj) {
    return obj.accept(this);
  }

  // doubles const, produces IArith
  public IArith visitConst(Const c) {
    return new Const(2 * c.num);
  }

  // produces IArith: UnaryFormula with const doubled
  public IArith visitUnaryFormula(UnaryFormula uf) {
    return new UnaryFormula(uf.func, uf.name, uf.child.accept(new DoublerVisitor()));
  }

  // produces IArith: BinayFormula with const doubled
  public IArith visitBinaryFormula(BinaryFormula bf) {
    return new BinaryFormula(bf.func, bf.name, bf.left.accept(new DoublerVisitor()), 
        bf.right.accept(new DoublerVisitor()));
  }
}

// NoNegativeResults class
class NoNegativeResults implements IArithVisitor<Boolean> {

  /* Template:
   * methods:
   * this.apply(IArith) -- Boolean
   * this.visitConst(Const) -- Boolean
   * this.visitUnaryFormula(UnaryFormula) -- Boolean
   * this.visitBinaryFormula(BinaryFormula) -- Boolean
   */
  
  public Boolean apply(IArith obj) {
    return obj.accept(this);
  }

  // returns TRUE if a negative number is never encountered
  public Boolean visitConst(Const c) {
    return c.num >= 0;
  }

  // returns TRUE if a negative number is never encountered
  public Boolean visitUnaryFormula(UnaryFormula uf) {
    return uf.func.apply(uf.child.accept(new EvalVisitor())) >= 0
        && uf.child.accept(new NoNegativeResults());
  }

  // returns TRUE if a negative number is never encountered
  public Boolean visitBinaryFormula(BinaryFormula bf) {
    return bf.func.apply(bf.left.accept(new EvalVisitor()), 
        bf.right.accept(new EvalVisitor())) >= 0
        && bf.left.accept(new NoNegativeResults()) 
        && bf.right.accept(new NoNegativeResults());
  }
}

// Negation class
class Neg implements Function<Double, Double> {
  public Double apply(Double t) {
    return -1 * t;
  }
}

// Sqr Class
class Sqr implements Function<Double, Double> {
  public Double apply(Double t) {
    return t * t;
  }
}

// Plus class
class Plus implements BiFunction<Double, Double, Double> {
  public Double apply(Double t, Double r) {
    return t + r;
  }
}

// Minus class
class Sub implements BiFunction<Double, Double, Double> {
  public Double apply(Double t, Double r) {
    return t - r;
  }
}

// Mul class
class Mul implements BiFunction<Double, Double, Double> {
  public Double apply(Double t, Double r) {
    return t * r;
  }
}

// Div class
class Div implements BiFunction<Double, Double, Double> {
  public Double apply(Double t, Double r) {
    return t / r;
  }
}

// some constant value
class Const implements IArith {
  double num;

  Const(double num) {
    this.num = num;
  }

  /* Template:
   * fields:
   * this.num -- double
   * methods:
   * this.accept(IArithVisitor<R>) -- <R> R
   */

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

// UnaryFormula class
// neg & sqr
class UnaryFormula implements IArith {
  Function<Double, Double> func;
  String name;
  IArith child;

  UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }

  /* Template:
   * fields:
   * this.func -- Function<Double, Double>
   * this.name -- String
   * this.child -- IArith
   * methods:
   * this.accept(IArithVisitor<R>) -- <R> R
   */

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitUnaryFormula(this);
  }
}

// BinaryFormula class
// plus & minus & mul & div
class BinaryFormula implements IArith {
  BiFunction<Double, Double, Double> func;
  String name;
  IArith left;
  IArith right;

  BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  /* Template:
   * fields:
   * this.func -- BiFunction <Double, Double, Double>
   * this.name -- String
   * this.left -- IArith
   * this.right -- IArith
   * methods:
   * this.accept(IArithVisitor<R>) -- <R> R
   */

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitBinaryFormula(this);
  }
}

// examples and tests
class Examples {
  Examples() {}

  IArith c0 = new Const(0.0);
  IArith c1 = new Const(1.0);
  IArith c2 = new Const(2.0);
  IArith c3 = new Const(3.0);
  IArith c4 = new Const(4.0);
  IArith c5 = new Const(5.0);
  IArith f1 = new BinaryFormula(new Plus(), "plus", this.c1, this.c2);
  IArith f2 = new BinaryFormula(new Div(), "div", this.f1, this.c3);
  IArith f3 = new BinaryFormula(new Sub(), "minus", this.c4, this.f2);
  IArith f4 = new BinaryFormula(new Sub(), "minus", this.c5, this.c4);
  IArith f5 = new BinaryFormula(new Mul(), "mul", this.f3, this.f4);
  IArith negatec3 = new UnaryFormula(new Neg(), "neg", this.c3);
  IArith twoPlusThree = new BinaryFormula(new Plus(), "plus", this.c2, this.c4);
  IArith zeroResultNeverNegative = new BinaryFormula(
      new Sub(), "minus", this.c0, this.c0);

  IArith const0 = new Const(0.0);
  IArith const1 = new Const(1.0);
  IArith const1_5 = new Const(1.5);
  IArith const2 = new Const(2.0);
  IArith neg_const1_5 = new Const(-1.5);

  IArith neg1 = new UnaryFormula(new Neg(), "neg", this.const1_5);
  IArith plus1 = new BinaryFormula(new Plus(), "plus", this.const1, this.const2);

  IArith example = new BinaryFormula(new Div(), "div", this.plus1, this.neg1);

  // EvalVisitor class tests
  boolean testEvalClass(Tester t) {
    return t.checkInexact(new EvalVisitor().apply(new Const(1.0)), 1.0, 0.01)
        && t.checkInexact(new EvalVisitor().apply(new Const(-1.0)), -1.0, 0.01)
        && t.checkInexact(
            new EvalVisitor().apply(new UnaryFormula(new Neg(), "neg", new Const(4.5))), 
            -4.5, 0.01)
        && t.checkInexact(
            new EvalVisitor().apply(new UnaryFormula(new Sqr(), "sqr", new Const(3.0))), 
            9.0, 0.01)
        && t.checkInexact(new EvalVisitor().apply(new BinaryFormula(new Plus(), "plus",
            new Const(3.0), new Const(1.5))), 4.5, 0.01);
  }

  // PrintVisitor class tests
  boolean testPrintClass(Tester t) {
    return t.checkExpect(new PrintVisitor().apply(this.example), 
        "(div (plus 1.0 2.0) (neg 1.5))")
        && t.checkExpect(new PrintVisitor().apply(new Const(0.0)), "0.0")
        && t.checkExpect(new PrintVisitor().apply(new UnaryFormula(new Neg(), 
            "neg", new Const(5.0))), "(neg 5.0)")
        && t.checkExpect(new PrintVisitor().apply(new BinaryFormula(new Sub(), "minus", 
            new BinaryFormula(new Mul(), "mul", 
                new Const(3.0), new UnaryFormula(new Neg(), "neg", 
                    new UnaryFormula(new Sqr(), "sqr", new Const(4.0)))), 
            new Const(1.0))), "(minus (mul 3.0 (neg (sqr 4.0))) 1.0)");
  }

  // DoublerVisitor class tests
  boolean testDoublerClass(Tester t) {
    return t.checkExpect(new DoublerVisitor().apply(this.example), 
        new BinaryFormula(new Div(), "div", 
            new BinaryFormula(new Plus(), "plus", new Const(2.0), new Const(4.0)), 
            new UnaryFormula(new Neg(), "neg", new Const(3.0))))
        && t.checkExpect(new DoublerVisitor().apply(new Const(3.5)), new Const(7.0))
        && t.checkExpect(new DoublerVisitor().apply(new BinaryFormula(new Sub(), "minus", 
            new BinaryFormula(new Mul(), "mul", 
                new Const(3.0), new UnaryFormula(new Neg(), "neg", 
                    new UnaryFormula(new Sqr(), "sqr", new Const(4.0)))), 
            new Const(1.0))), new BinaryFormula(new Sub(), "minus", 
                new BinaryFormula(new Mul(), "mul", 
                    new Const(6.0), new UnaryFormula(new Neg(), "neg", 
                        new UnaryFormula(new Sqr(), "sqr", new Const(8.0)))), 
                new Const(2.0)))
        && t.checkExpect(new DoublerVisitor().apply(this.neg1), 
            new UnaryFormula(new Neg(), "neg", new Const(3.0)));
  }

  // NoNegativeResults class tests
  boolean testNoNegativeResultsClass(Tester t) {
    return t.checkExpect(new NoNegativeResults().apply(this.neg1), false)
        && t.checkExpect(new NoNegativeResults().apply(new Const(-3.8)), false)
        && t.checkExpect(new NoNegativeResults().apply(new UnaryFormula(new Neg(), "neg", 
            new Const(3.8))), false)
        && t.checkExpect(new NoNegativeResults().apply(new BinaryFormula(new Div(), "div", 
                new BinaryFormula(new Plus(), 
                    "plus", new Const(2.0), new Const(4.0)), 
                new UnaryFormula(new Neg(), "neg", 
                    new Const(3.0)))), false);
  }

  // tests for eval visitor
  boolean testEvalVisitor(Tester t) {
    return t.checkInexact(this.const1.accept(new EvalVisitor()), 1.0, 0.01)
        && t.checkInexact(this.neg1.accept(new EvalVisitor()), -1.5, 0.01)
        && t.checkInexact(this.plus1.accept(new EvalVisitor()), 3.0, 0.01)
        && t.checkInexact(this.example.accept(new EvalVisitor()), -2.0, 0.01);
  }

  // tests for print visitor
  boolean testPrintVisitor(Tester t) {
    return t.checkExpect(this.const1.accept(new PrintVisitor()), "1.0")
        && t.checkExpect(this.neg1.accept(new PrintVisitor()), "(neg 1.5)")
        && t.checkExpect(this.plus1.accept(new PrintVisitor()), "(plus 1.0 2.0)")
        && t.checkExpect(this.example.accept(new PrintVisitor()), 
            "(div (plus 1.0 2.0) (neg 1.5))")
        && t.checkExpect(new BinaryFormula(new Plus(), "plus", 
            new BinaryFormula(new Sub(), "minus", 
                new Const(3.0), 
                new UnaryFormula(new Sqr(), "sqr", new Const(2.0))),
            new UnaryFormula(new Sqr(), "sqr", 
                new UnaryFormula(new Sqr(), "sqr", new Const(3.0)))).accept(new PrintVisitor()), 
            "(plus (minus 3.0 (sqr 2.0)) (sqr (sqr 3.0)))")
        && t.checkExpect(new BinaryFormula(new Sub(), "minus", 
            new BinaryFormula(new Mul(), "mul", 
                new Const(3.0), new UnaryFormula(new Neg(), "neg", 
                    new UnaryFormula(new Sqr(), "sqr", new Const(4.0)))), 
            new Const(1.0)).accept(new PrintVisitor()), "(minus (mul 3.0 (neg (sqr 4.0))) 1.0)");
  }

  // tests for double visitor
  boolean testDoublerVisitor(Tester t) {
    return t.checkExpect(this.const1.accept(new DoublerVisitor()), new Const(2.0))
        && t.checkExpect(this.neg1.accept(new DoublerVisitor()), 
            new UnaryFormula(new Neg(), "neg", new Const(3.0)))
        && t.checkExpect(this.example.accept(new DoublerVisitor()), 
            new BinaryFormula(new Div(), "div", 
                new BinaryFormula(new Plus(), 
                    "plus", new Const(2.0), new Const(4.0)), 
                new UnaryFormula(new Neg(), "neg", new Const(3.0))));
  }

  // tests for no negative results visitor
  boolean testNoNegativeResults(Tester t) {
    return t.checkExpect(new Const(-3.8).accept(new NoNegativeResults()), false)
        && t.checkExpect(new UnaryFormula(new Neg(), "neg", 
            new Const(3.8)).accept(new NoNegativeResults()), false)
        && t.checkExpect(new UnaryFormula(new Neg(), "neg", 
            new Const(3.8)).accept(new NoNegativeResults()), false)
        && t.checkExpect(new Const(0.0).accept(new NoNegativeResults()), true)
        && t.checkExpect(new UnaryFormula(new Neg(), "neg", 
            new UnaryFormula(new Neg(), "neg", 
                new Const(3.8))).accept(new NoNegativeResults()), false)
        && t.checkExpect(new BinaryFormula(new Div(), "div", 
                new BinaryFormula(new Plus(), 
                    "plus", new Const(2.0), new Const(4.0)), 
                new UnaryFormula(new Neg(), "neg", 
                    new Const(3.0))).accept(new NoNegativeResults()), false);
  }
}
