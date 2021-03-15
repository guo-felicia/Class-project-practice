import tester.Tester;

interface ILoString {

  // adds a single string to the list of string
  ILoString appendItem(String base);

  // creates a list of proteins by creating a list of codons
  // and creates a new list every time a stop codon is reached or there are no
  // more codons in the sequence
  ILoLoString makeProteinList(String codon, ILoString codonList);

  // calls up to makeProteinList and passes in RNA to translate
  ILoLoString translate();

  // reverses a list of codons, calls up to helper
  ILoString reverse();

  // reverses a list of codons
  ILoString reverseHelp(ILoString acc);

}

class MtLoString implements ILoString {
  MtLoString() {
  }

  /*
   * Template
   * 
   * Fields: None
   * 
   * Methods: ILoString appendItem(String base) ILoLoString makeProteinList(String
   * codon, ILoString codonList) ILoLoString translate() ILoString reverse()
   * ILoString reverseHelp(ILoString acc)
   * 
   * Methods of fields: None
   * 
   */

  // reverses an empty list, returns itself
  // Template: Same as class
  public ILoString reverse() {
    return this;
  }

  // returns the successfully reversed list after hitting empty case
  // template: same as class
  // parameter: acc
  public ILoString reverseHelp(ILoString acc) {
    return acc;
  }

  // appends a codon into a new list of strings
  // template: same as class
  // parameter: String base
  public ILoString appendItem(String base) {
    return new ConsLoString(base, this);
  }

  // creates a new list of proteins
  // template: same as class
  // parameters: String codon, ILoString codonList
  public ILoLoString makeProteinList(String codon, ILoString codonList) {
    return new ConsLoLoString(codonList, new MtLoLoString());
  }

  // returns an empty list of lists when an end condition is reached
  // template: same as class
  public ILoLoString translate() {
    return new MtLoLoString();
  }

}

class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }
  /*
   * Template
   * 
   * Fields: this.first this.rest
   * 
   * Methods: ILoString appendItem(String base) ILoLoString makeProteinList(String
   * codon, ILoString codonList) ILoLoString translate() ILoString reverse()
   * ILoString reverseHelp(ILoString acc)
   * 
   * Methods of fields: this.rest.reverseHelp() this.rest.translate()
   * 
   */

  // reverses a list
  // template: same as class
  public ILoString reverse() {
    return this.reverseHelp(new MtLoString());
  }

  // reverses a list
  // template: same as class
  // parameters: ILoString acc
  public ILoString reverseHelp(ILoString acc) {
    return this.rest.reverseHelp(new ConsLoString(this.first, acc));
  }

  // Adds new codon onto a list of existing codons previously made
  // Template: same as class
  // Parameters: String codon
  public ILoString appendItem(String codon) {
    return new ConsLoString(codon, this.reverse());
  }

  // Creates a list of proteins by adding onto a codon until length = 3
  // Template: same as class
  // Parameters: String codon, ILoString protein
  // Methods: .length(), .equals()
  //
  public ILoLoString makeProteinList(String codon, ILoString protein) {
    String codonSofar = codon + this.first;

    if (codonSofar.length() == 3) {

      if (codonSofar.equals("UAG") || codonSofar.equals("UAA") || codonSofar.equals("UGA")) {

        return new ConsLoLoString(protein, this.rest.translate());
      } else {
        return this.rest.makeProteinList("", protein.appendItem(codonSofar).reverse());
      }

    } else {

      return this.rest.makeProteinList(codon + this.first, protein.reverse());

    }
  }

  // passes in base arguments to makeProteinList helper
  // template: same as class
  public ILoLoString translate() {
    return this.makeProteinList("", new MtLoString());
  }

}

//represents a list of proteins
interface ILoLoString {
}

class MtLoLoString implements ILoLoString {
  MtLoLoString() {
  }
  /*
   * Template
   * 
   * Fields: None
   * 
   * Methods: None
   * 
   * Methods of fields: None
   * 
   */
}

class ConsLoLoString implements ILoLoString {
  ILoString first;
  ILoLoString rest;

  ConsLoLoString(ILoString first, ILoLoString rest) {
    this.first = first;
    this.rest = rest;
  }
  /*
   * Template
   * 
   * Fields: this.first this.rest
   * 
   * Methods: None
   * 
   * Methods of fields: None
   * 
   */
}

class ExamplesRNA {
  ExamplesRNA() {
  }

  ILoString emptylist = new MtLoString();

  // A
  // No actual codons
  ILoString rna0 = new ConsLoString("A", new MtLoString());

  // GCCA
  // One codon with extra A
  ILoString rna1 = new ConsLoString("G",
      new ConsLoString("C", new ConsLoString("C", new ConsLoString("A", emptylist))));

  // AUG AUC UCG UAA
  // Stop codon at end
  ILoString rna2 = new ConsLoString("A",
      new ConsLoString("U",
          new ConsLoString("G",
              new ConsLoString("A",
                  new ConsLoString("U",
                      new ConsLoString("C",
                          new ConsLoString("U",
                              new ConsLoString("C", new ConsLoString("G", new ConsLoString("U",
                                  new ConsLoString("A", new ConsLoString("A", emptylist))))))))))));
  // Two stop codons UAG UAA
  ILoString rna3 = new ConsLoString("U", new ConsLoString("A", new ConsLoString("G",
      new ConsLoString("U", new ConsLoString("A", new ConsLoString("A", emptylist))))));

  // "incomplete" ACA GAU AG
  ILoString rna4 = new ConsLoString("A",
      new ConsLoString("C", new ConsLoString("A", new ConsLoString("G", new ConsLoString("A",
          new ConsLoString("U", new ConsLoString("A", new ConsLoString("G", emptylist))))))));

  // ACA AAG UAG UUG
  // Stop codon in middle
  ILoString rna5 = new ConsLoString("A",
      new ConsLoString("C",
          new ConsLoString("A",
              new ConsLoString("A",
                  new ConsLoString("A",
                      new ConsLoString("G",
                          new ConsLoString("U",
                              new ConsLoString("A", new ConsLoString("G", new ConsLoString("U",
                                  new ConsLoString("U", new ConsLoString("G", emptylist))))))))))));

  // ACA
  // One codon
  ILoString rna6 = new ConsLoString("A",
      new ConsLoString("C", new ConsLoString("A", new MtLoString())));

  // longer rna with all 3 stop codons separated (UAA, UAG, UGA)

  // GCC ACC CCC UAA CAG UAG UUC UGA UUA
  ILoString rna7 = new ConsLoString("G", new ConsLoString("C", new ConsLoString("C",
      new ConsLoString("A", new ConsLoString("C", new ConsLoString("C", new ConsLoString("C",
          new ConsLoString("C", new ConsLoString("C", new ConsLoString("U", new ConsLoString("A",
              new ConsLoString("A", new ConsLoString("C", new ConsLoString("A",
                  new ConsLoString("G", new ConsLoString("U", new ConsLoString("A",
                      new ConsLoString("G", new ConsLoString("U", new ConsLoString("U",
                          new ConsLoString("C", new ConsLoString("U", new ConsLoString("G",
                              new ConsLoString("A", new ConsLoString("U", new ConsLoString("U",
                                  new ConsLoString("A", new MtLoString())))))))))))))))))))))))))));

  // One codon that's a stop codon UAA
  ILoString rna8 = new ConsLoString("U",
      new ConsLoString("A", new ConsLoString("A", new MtLoString())));

  // Lists of codons and their reversed versions (correspond to RNA sequences)

  ILoString codonList1 = new ConsLoString("GCC", emptylist);
  ILoString codonList2 = new ConsLoString("AUG",
      (new ConsLoString("AUC", new ConsLoString("UCG", emptylist))));
  ILoString codonList3 = new ConsLoString("AUG",
      (new ConsLoString("AUC", new ConsLoString("UCG", new ConsLoString("UAA", emptylist)))));
  ILoString codonList4 = new ConsLoString("ACA", new ConsLoString("GAU", emptylist));
  ILoString codonList5A = new ConsLoString("ACA", new ConsLoString("AAG", emptylist));
  ILoString codonList5B = new ConsLoString("UUG", emptylist);
  ILoString codonList6 = new ConsLoString("ACA", new MtLoString());
  // List 7A: GCC ACC CCC
  // List 7B: CAG
  // List 7C: UUC
  // List 7D: UUA
  ILoString codonList7A = new ConsLoString("GCC",
      new ConsLoString("ACC", new ConsLoString("CCC", new MtLoString())));
  ILoString codonList7B = new ConsLoString("CAG", new MtLoString());
  ILoString codonList7C = new ConsLoString("UUC", new MtLoString());
  ILoString codonList7D = new ConsLoString("UUA", new MtLoString());

  ILoString codonList2Rev = new ConsLoString("UCG",
      new ConsLoString("AUC", new ConsLoString("AUG", emptylist)));
  ILoString codonList3Rev = new ConsLoString("UAA",
      new ConsLoString("UCG", new ConsLoString("AUC", new ConsLoString("AUG", emptylist))));
  ILoString codonList4Rev = new ConsLoString("GAU", new ConsLoString("ACA", emptylist));
  ILoString codonList5ARev = new ConsLoString("AAG", new ConsLoString("ACA", emptylist));
  ILoString codonList7ARev = new ConsLoString("CCC",
      new ConsLoString("ACC", new ConsLoString("GCC", new MtLoString())));

  // Lists of Protein
  ILoLoString emptylolist = new MtLoLoString();
  ILoLoString lop1 = new ConsLoLoString(codonList1, emptylolist);
  ILoLoString lop2 = new ConsLoLoString(codonList2, emptylolist);
  ILoLoString lop3 = new ConsLoLoString(codonList3, emptylolist);
  ILoLoString lop4 = new ConsLoLoString(codonList4, emptylolist);
  ILoLoString lop5 = new ConsLoLoString(codonList5A, new ConsLoLoString(codonList5B, emptylolist));
  ILoLoString lop6 = new ConsLoLoString(codonList6, emptylolist);
  ILoLoString lop7 = new ConsLoLoString(codonList7A, new ConsLoLoString(codonList7B,
      new ConsLoLoString(codonList7C, new ConsLoLoString(codonList7D, new MtLoLoString()))));

  // Tests that the reverse function can successfully reverse a list of codons
  boolean testReverse(Tester t) {
    return t.checkExpect(this.emptylist.reverse(), emptylist)
        && t.checkExpect(this.codonList1.reverse(), codonList1)
        && t.checkExpect(this.codonList2.reverse(), codonList2Rev)
        && t.checkExpect(this.codonList3.reverse(), codonList3Rev)
        && t.checkExpect(this.codonList4.reverse(), codonList4Rev)
        && t.checkExpect(this.codonList5A.reverse(), codonList5ARev)
        && t.checkExpect(this.codonList5B.reverse(), codonList5B)
        && t.checkExpect(this.codonList6, codonList6)
        && t.checkExpect(this.codonList7A.reverse(), codonList7ARev)
        && t.checkExpect(this.codonList7B.reverse(), codonList7B)
        && t.checkExpect(this.codonList7C.reverse(), codonList7C)
        && t.checkExpect(this.codonList7D.reverse(), codonList7D);

  }

  // tests that the helper can create a protein list from a list of codons
  boolean testMakeProList(Tester t) {
    return t.checkExpect(this.emptylist.makeProteinList("", new MtLoString()),
        new ConsLoLoString(new MtLoString(), new MtLoLoString()))
        && t.checkExpect(this.rna0.makeProteinList("", new MtLoString()),
            new ConsLoLoString(new MtLoString(), new MtLoLoString()))
        && t.checkExpect(this.rna1.makeProteinList("", new MtLoString()), lop1)
        && t.checkExpect(this.rna2.makeProteinList("", new MtLoString()), lop2)
        && t.checkExpect(this.rna3.makeProteinList("", new MtLoString()),
            new ConsLoLoString(emptylist, new ConsLoLoString(emptylist, emptylolist)))
        && t.checkExpect(this.rna4.makeProteinList("", new MtLoString()), lop4)
        && t.checkExpect(this.rna5.makeProteinList("", new MtLoString()), lop5)
        && t.checkExpect(this.rna6.makeProteinList("", new MtLoString()), lop6)
        && t.checkExpect(this.rna7.makeProteinList("", new MtLoString()), lop7);
  }

  // tests that the overall translate method works
  boolean testTranslate(Tester t) {
    return t.checkExpect(rna0.translate(), new ConsLoLoString(new MtLoString(), new MtLoLoString()))
        && t.checkExpect(rna1.translate(), lop1) && t.checkExpect(rna2.translate(), lop2)
        && t.checkExpect(rna3.translate(),
            new ConsLoLoString(new MtLoString(),
                new ConsLoLoString(new MtLoString(), new MtLoLoString())))
        && t.checkExpect(rna4.translate(), lop4) && t.checkExpect(rna5.translate(), lop5)
        && t.checkExpect(rna6.translate(), lop6) && t.checkExpect(rna7.translate(), lop7);
  }
}