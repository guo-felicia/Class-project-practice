import tester.*;

interface IEntertainment {
  // compute the total price of this Entertainment
  double totalPrice();

  // computes the minutes of entertainment of this IEntertainment
  int duration();

  // produce a String that shows the name and price of this IEntertainment
  String format();

  // is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);

  // check same for each classes
  boolean sameMag(Magazine that); 

  boolean sameTv(TVSeries that);

  boolean sameSer(Podcast that);
}

abstract class AEntertainment implements IEntertainment {
  String name;
  double price; // represents price per issue
  int installments;

  AEntertainment(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }
  
  /*  TEMPLATE 
  Fields:
  ... this.name ...                   -- String
  ... this.price ...                  -- double
  ... this.installments ...           -- String
  
  Methods:
  ... this.totalPrice() ...           -- double 
  ... this.duration() ...             -- int
  ... this.format() ...               -- String
  ... this.sameEntertainment(IEntertainment that) ...      -- boolean

  Methods on fields: None
  
  Fields of parameters: None
  
  Methods on parameters: this.sameEntertainment(IEntertainment that)
  
  */

  // compute the total price of this Entertainment
  public double totalPrice() {
    return this.price * this.installments;
  }

  // computes the minutes of entertainment of this IEntertainment
  public int duration() {
    return 50 * this.installments;
  }

  // produce a String that shows the name and price of this IEntertainment
  public String format() {
    return this.name + ", " + this.price + ".";
  }

  // is this IEntertainment the same as that one?
  public abstract boolean sameEntertainment(IEntertainment that);
  
  public boolean sameMag(Magazine that) {return false;}
  public boolean sameTv(TVSeries that) {return false;}
  public boolean sameSer(Podcast that) {return false;}

}

class Magazine extends AEntertainment {
  String genre;
  int pages;

  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }
  
  /*  TEMPLATE 
  Fields:
  ... this.name ...                   -- String
  ... this.price ...                  -- double
  ... this.genre ...                  -- String
  ... this.pages ...                  -- int
  ... this.installments ...           -- String
  
  Methods:
  ... this.totalPrice() ...           -- double 
  ... this.duration() ...             -- int
  ... this.format() ...               -- String
  ... this.sameEntertainment(IEntertainment that) ...      -- boolean
  ... this.sameMag(Magazine that) ...      -- boolean
  ... this.sameTv(TVSeries that) ...       -- boolean
  ... this.sameSer(Podcast that) ...       -- boolean
  
  Methods on fields:
  
  Fields of parameters:
  
  Methods on parameters:
  ... that.sameMag(Magazine this) ...      -- boolean
  ... that.sameTv(TVSeries this) ...       -- boolean
  ... that.sameSer(Podcast this) ...       -- boolean
  
  */

  // computes the minutes of entertainment of this Magazine, (includes all
  // installments)
  public int duration() {
    return 5 * this.pages * this.installments;
  }

  public boolean sameMag(Magazine that) {
    return this.name.equals(that.name) && this.price == that.price && this.genre.equals(that.genre)
        && this.pages == that.pages && this.installments == that.installments;
  }

  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMag(this);
  }
}

class TVSeries extends AEntertainment {
  String corporation;

  TVSeries(String name, double price, int installments, String corporation) {
    super(name, price, installments);
    this.corporation = corporation;
  }
  
  /*  TEMPLATE 
  Fields:
  ... this.name ...                   -- String
  ... this.price ...                  -- double
  ... this.installments ...           -- String
  ... this.corporation ...            -- String
  
  Methods:
  ... this.totalPrice() ...           -- double 
  ... this.duration() ...             -- int
  ... this.format() ...               -- String
  ... this.sameEntertainment(IEntertainment that) ...      -- boolean
  ... this.sameMag(Magazine that) ...      -- boolean
  ... this.sameTv(TVSeries that) ...       -- boolean
  ... this.sameSer(Podcast that) ...       -- boolean
  
  Methods on fields:
  
  Fields of parameters:
  
  Methods on parameters:
  ... that.sameMag(Magazine this) ...      -- boolean
  ... that.sameTv(TVSeries this) ...       -- boolean
  ... that.sameSer(Podcast this) ...       -- boolean
  
  */

  
  public boolean sameTv(TVSeries that) {
    return this.name.equals(that.name) && this.price == that.price
        && this.corporation.equals(that.corporation) && this.installments == that.installments;
  }

  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTv(this);
  }
}

class Podcast extends AEntertainment {

  Podcast(String name, double price, int installments) {
    super(name, price, installments);
  }
  
  /*  TEMPLATE 
  Fields:
  ... this.name ...                   -- String
  ... this.price ...                  -- double
  ... this.installments ...           -- String
  
  Methods:
  ... this.totalPrice() ...           -- double 
  ... this.duration() ...             -- int
  ... this.format() ...               -- String
  ... this.sameEntertainment(IEntertainment that) ...      -- boolean
  ... this.sameMag(Magazine that) ...      -- boolean
  ... this.sameTv(TVSeries that) ...       -- boolean
  ... this.sameSer(Podcast that) ...       -- boolean
  
  Methods on fields:
  
  Fields of parameters:
  
  Methods on parameters:
  ... that.sameMag(Magazine this) ...      -- boolean
  ... that.sameTv(TVSeries this) ...       -- boolean
  ... that.sameSer(Podcast this) ...       -- boolean
  
  */

  public boolean sameSer(Podcast that) {
    return this.name.equals(that.name) && this.price == that.price
        && this.installments == that.installments;
  }

  public boolean sameEntertainment(IEntertainment that) {
    return that.sameSer(this);
  }
}

class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment vogue = new Magazine("Vogue", 3.25, "Fashion", 30, 8);
  IEntertainment theNature = new Magazine("The Nature", 3.15, "Science", 25, 10);
  IEntertainment theNature2 = new Magazine("The Nature", 3.15, "Sea World", 25, 10);
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment blackMirror = new TVSeries("Black Mirror", 8.15, 5, "Sony");
  IEntertainment blackMirror2 = new TVSeries("Black Mirror", 8.15, 3, "Sony");
  IEntertainment westWorld = new TVSeries("West world", 6.50, 18, "Amazon");
  IEntertainment serial = new Podcast("Serial", 0.0, 8);
  IEntertainment voa = new Podcast("VOA", 0.0, 12);
  IEntertainment voa2 = new Podcast("VOA", 1.0, 48);
  IEntertainment frenchTalk = new Podcast("French Talk", 2.0, 15);

  // testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 30.6, .0001)
        && t.checkInexact(this.vogue.totalPrice(), 26.0, .0001)
        && t.checkInexact(this.houseOfCards.totalPrice(), 68.25, .0001)
        && t.checkInexact(this.blackMirror.totalPrice(), 40.75, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
        && t.checkInexact(this.frenchTalk.totalPrice(), 30.0, .0001);
  }

  // testing total price method
  boolean testDuration(Tester t) {
    return t.checkExpect(this.rollingStone.duration(), 3600)
        && t.checkExpect(this.vogue.duration(), 1200)
        && t.checkExpect(this.theNature.duration(), 1250)
        && t.checkExpect(this.houseOfCards.duration(), 650)
        && t.checkExpect(this.serial.duration(), 400) && t.checkExpect(this.voa.duration(), 600)
        && t.checkExpect(this.westWorld.duration(), 900)
        && t.checkExpect(this.blackMirror.duration(), 250);
  }

  // testing total price method
  boolean testFormat(Tester t) {
    return t.checkExpect(this.rollingStone.format(), "Rolling Stone, 2.55.")
        && t.checkExpect(this.houseOfCards.format(), "House of Cards, 5.25.")
        && t.checkExpect(this.serial.format(), "Serial, 0.0.")
        && t.checkExpect(this.vogue.format(), "Vogue, 3.25.")
        && t.checkExpect(this.frenchTalk.format(), "French Talk, 2.0.")
        && t.checkExpect(this.voa.format(), "VOA, 0.0.")
        && t.checkExpect(this.westWorld.format(), "West world, 6.5.");
  }

  // testing total price method
  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(this.rollingStone.sameEntertainment(rollingStone), true)
        && t.checkExpect(this.houseOfCards.sameEntertainment(rollingStone), false)
        && t.checkExpect(this.serial.sameEntertainment(frenchTalk), false)
        && t.checkExpect(this.vogue.sameEntertainment(vogue), true)
        && t.checkExpect(this.vogue.sameEntertainment(theNature2), false)
        && t.checkExpect(this.frenchTalk.sameEntertainment(blackMirror), false)
        && t.checkExpect(this.voa.sameEntertainment(frenchTalk), false)
        && t.checkExpect(this.westWorld.sameEntertainment(theNature), false)
        && t.checkExpect(this.westWorld.sameEntertainment(blackMirror), false)
        && t.checkExpect(this.theNature.sameEntertainment(theNature2), false)
        && t.checkExpect(this.blackMirror.sameEntertainment(blackMirror2), false)
        && t.checkExpect(this.blackMirror2.sameEntertainment(blackMirror2), true)
        && t.checkExpect(this.voa.sameEntertainment(voa2), false);
  }

}