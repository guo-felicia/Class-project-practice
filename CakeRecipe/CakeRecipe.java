import tester.Tester;

class CakeRecipe {

  /*
   * TEMPLATE
   * 
   * FIELDS: ... this.flour ... -- double ... this.sugar ... -- double ...
   * this.eggs ... -- double ... this.butter ... -- double ... this.milk ... --
   * double
   * 
   * METHODS: ... this.sameRecipe(CakeRecipe other) ... -- boolean
   * 
   * METHODS FOR FIELDS: None
   */

  String invalidFlour = "Weight of flour must equal sugar";
  String invalidEggs = "Weight of eggs must equal butter";
  String invalidMilkAndEggs = "Invalid egg and milk weight."
      + "The weight of the milk and eggs must add up to either the sugar/flour.";
  String negativeMsg = "Invalid: One or more values are negative.";

  // weight of flour = sugar
  // egg weight = butter weight
  // weight of eggs + milk = sugar / flour
  // weights in ounces (weights must be equal in oz, but not volume persay
  double flour;
  double sugar;
  double eggs;
  double butter;
  double milk;

  // main constructor
  CakeRecipe(double flour, double sugar, double eggs, double butter, double milk) {

    if (flour > 0 && sugar > 0 && eggs > 0 && butter > 0 && milk > 0) {

      this.flour = new Util().checkEquality(flour, sugar, invalidFlour);
      this.sugar = new Util().checkEquality(sugar, flour, invalidFlour);
      this.eggs = new Util().checkEquality(eggs, butter, invalidEggs);
      this.butter = new Util().checkEquality(butter, eggs, invalidEggs);
      this.milk = new Util().checkAddEquality(milk, eggs, flour, invalidMilkAndEggs);

    }

    else {
      throw new IllegalArgumentException(negativeMsg);
    }

  }

  // only requires flour, eggs, and milk
  CakeRecipe(double flour, double eggs, double milk) {

    // the values in this constructor follow the same rules as default constructors
    this(flour, flour, eggs, eggs, milk);

  }

  // takes in flour, eggs, and milk as VOLUMES
  CakeRecipe(double vFlour, double vEggs, double vMilk, boolean areVolumes) {

    // convert ingredient in volume to ingredient in oz
    double convertFlour = vFlour * 4.25;
    double convertEggs = vEggs * 1.75;
    double convertMilk = vMilk * 8;

    if (areVolumes) {

      // force flour and sugar to be equal
      this.flour = convertFlour;
      this.sugar = convertFlour;

      this.eggs = new Util().checkAddEquality(convertEggs, convertMilk, convertFlour,
          invalidMilkAndEggs);
      this.butter = convertEggs;
      this.milk = convertMilk;

    } else {
      this.flour = vFlour;
      this.sugar = vFlour;
      this.eggs = new Util().checkAddEquality(vEggs, vMilk, vFlour, invalidMilkAndEggs);
      this.milk = vMilk;
      this.butter = vEggs;

    }

  }

  boolean sameRecipe(CakeRecipe other) {
    // Template: Same as class
    // Parameters: CakeRecipe other
    // Methods: Math.abs(this - other)

    return Math.abs(this.flour - other.flour) < 0.001 && Math.abs(this.sugar - other.sugar) < 0.001
        && Math.abs(this.eggs - other.eggs) < 0.001 && Math.abs(this.butter - other.butter) < 0.001
        && Math.abs(this.milk - other.milk) < 0.001;
  }

}

//utility class to check data integrity
class Util {

  /*
   * TEMPLATE
   * 
   * FIELDS: None
   * 
   * METHODS: checkEquality(double value1, double value2, String msg) -- boolean
   * checkAddEquality(double val1, double val2, double result, String msg) --
   * boolean
   * 
   * METHODS FOR FIELDS: None
   */

  // checks that 2 values are equal when both values are in ounces
  double checkEquality(double value1, double value2, String msg) {
    // template: same as class
    if (Math.abs(value2 - value1) < 0.001) {
      return value1;
    } else {
      throw new IllegalArgumentException(msg);
    }
  }

  // checks if val1 and val2 add up to result
  double checkAddEquality(double val1, double val2, double result, String msg) {
    // template: same as class
    if (Math.abs(result - (val1 + val2)) < 0.001) {
      return val1;
    } else {
      throw new IllegalArgumentException(msg);
    }
  }

}

//Examples class
class ExamplesCakes {

  String cakeRecipe = "CakeRecipe";

  // Valid cakes
  
  CakeRecipe cake0 = new CakeRecipe(0.0, 0.0, 0.0, 0.0, 0.0);
  CakeRecipe cake0a = new CakeRecipe(0.0, 0.0, 0.0);
  CakeRecipe cake0b = new CakeRecipe(0.0, 0.0, 0.0, true);
  CakeRecipe cake0c = new CakeRecipe(0.0, 0.0, 0.0, false);
  
  // Cakes with 7 oz flour, 1.75 oz egg, 5.25 oz milk
  CakeRecipe cake1 = new CakeRecipe(7.0, 7.0, 1.75, 1.75, 5.25); // uses default constructor
  CakeRecipe cake1a = new CakeRecipe(7.0, 7.0, 1.75, 1.75, 5.25);// same as cake1
  CakeRecipe cake1b = new CakeRecipe(7.0, 1.75, 5.25); // same as cake 1 (in oz)
  CakeRecipe cake1c = new CakeRecipe(1.64705, 1.0, 0.65625, true); // cake 1 in volume

  // Cakes with 1 cup flour (4.25 oz), 4.25 oz sugar, 2 eggs (3.5oz), 3.5oz
  // butter, 0.75oz milk
  CakeRecipe cake2 = new CakeRecipe(4.25, 4.25, 3.5, 3.5, 0.75);
  CakeRecipe cake2a = new CakeRecipe(4.25, 4.25, 3.5, 3.5, 0.75); // Exact same
  CakeRecipe cake2b = new CakeRecipe(4.25, 3.5, 0.75); // same as cake2 but w 2nd constructor
  CakeRecipe cake2c = new CakeRecipe(1, 2, 0.09375, true); // Volume = true (same as cake2)
  CakeRecipe cake2d = new CakeRecipe(4.25, 3.5, 0.75, false); // Volume = false (same as cake2)

  // 4.25oz flour, 1.75 oz egg, 2.5oz milk
  CakeRecipe cake3 = new CakeRecipe(4.25, 1.75, 2.5);
  CakeRecipe cake3a = new CakeRecipe(1.0, 1.0, 0.3125, true); // in volume
  CakeRecipe cake3b = new CakeRecipe(4.25, 1.75, 2.5, false); // same as cake3 but in oz

  // Examples similar to cake 3 w/ some elements changed
  CakeRecipe cake4 = new CakeRecipe(5.0, 1.75, 3.25); // Same eggs, diff flour and milk
  CakeRecipe cake5 = new CakeRecipe(6, 3.5, 2.5); // Same milk, eggs and flour different
  CakeRecipe cake6 = new CakeRecipe(4.25, 1.0, 3.25); // Same eggs and flour, diff milk

  // checks that equality method is comparing 2 doubles properly
  boolean testCheckEquality(Tester t) {
    return t.checkExpect(new Util().checkEquality(1.45, 1.45, "Test"), 1.45)
        && t.checkExpect(new Util().checkEquality(1.0, 1.0001, "Test"), 1.0)
        && t.checkException(new IllegalArgumentException("Test"), new Util(), "checkEquality", 1.0,
            20.0, "Test")
        && t.checkException(new IllegalArgumentException("Test"), new Util(), "checkEquality", 1.0,
            1.01, "Test");
  }

  // checks that add equality method is comparing 2 doubles' sums properly
  boolean testAddEquality(Tester t) {
    return t.checkExpect(new Util().checkAddEquality(1.0, 2.0, 3.0, "Test"), 1.0)
        && t.checkExpect(new Util().checkAddEquality(1.001, 2.0, 3.0, "Test"), 1.001)
        && t.checkExpect(new Util().checkAddEquality(1.0001, 2.0001, 3.0, "Test"), 1.0001)
        && t.checkException(new IllegalArgumentException("Test"), new Util(), "checkAddEquality",
            1.0, 1.01, 2.0, "Test")
        && t.checkException(new IllegalArgumentException("Test"), new Util(), "checkAddEquality",
            3.0, 4.0, 7.01, "Test");
  }

  // Testing invalid cake illegal argument exceptions
  boolean testConstructors(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("Weight of flour must equal sugar"), cakeRecipe, 10.0, 11.0,
        5.0, 5.0, 5.0)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid egg and milk weight."
                + "The weight of the milk and eggs must add up to either the sugar/flour."),
            cakeRecipe, 4.25, 3.5, 0.75, true)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid: One or more values are negative."), cakeRecipe,
            -4.25, -1.75, -2.5)
        && t.checkConstructorException(
            new IllegalArgumentException("Weight of eggs must equal butter"), cakeRecipe, 7.0, 7.0,
            1.75, 1.0, 5.25);

  }

  // tests method sameRecipe
  boolean testSameRecipe(Tester t) {
    // Cake 0 in volume and oz (all ingredients are 0)
    return t.checkExpect(cake0.sameRecipe(cake0a), true)
        && t.checkExpect(cake0.sameRecipe(cake0b), true)
        && t.checkExpect(cake0.sameRecipe(cake0c), true)
        && t.checkExpect(cake0b.sameRecipe(cake0c), true) //volume in 0 = oz in 0
        // Cake 1, 1a, 1b
        && t.checkExpect(cake1.sameRecipe(cake1), true) // reflexivity (exact same object)
        && t.checkExpect(cake1.sameRecipe(cake1a), true) // same object, diff names
        && t.checkExpect(cake1.sameRecipe(cake1b), true) // same recipe, diff constructor
        && t.checkExpect(cake1b.sameRecipe(cake1), true) // symmetry
        && t.checkExpect(cake1a.sameRecipe(cake1b), true) // transitivity
        && t.checkExpect(cake1.sameRecipe(cake1c), true) // comparing vol to oz

        // Cake1 comparing to all other cakes (false)
        && t.checkExpect(cake1.sameRecipe(cake2), false)
        && t.checkExpect(cake1.sameRecipe(cake2a), false)
        && t.checkExpect(cake1.sameRecipe(cake2b), false)

        // Cake 2, 2a,2b
        && t.checkExpect(cake2.sameRecipe(cake2a), true)
        && t.checkExpect(cake2.sameRecipe(cake2b), true)
        && t.checkExpect(cake2.sameRecipe(cake2c), true)
        && t.checkExpect(cake2.sameRecipe(cake2d), true)

        // Cake 3
        && t.checkExpect(cake3.sameRecipe(cake3a), true)
        && t.checkExpect(cake3.sameRecipe(cake3b), true)

        && t.checkExpect(cake3.sameRecipe(cake4), false) // flour is the same, others aren't
        && t.checkExpect(cake3.sameRecipe(cake5), false) // eggs are the same but nothing else
        && t.checkExpect(cake3.sameRecipe(cake6), false) // milk is the same but nothing else
        && t.checkExpect(cake3a.sameRecipe(cake4), false); // cake 3a = cake 3b so cake 3a != cake4

  }

}
