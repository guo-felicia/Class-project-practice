import tester.*;
import java.util.function.Predicate;

//Define the classes ANode<T>, Node<T>, Sentinel<T>, and Deque<T>.

//class represent a Deque
class Deque<T> {
  Sentinel<T> header;

  // constructor
  Deque() {
    this.header = new Sentinel<T>();
  }

  // convenience constructor
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // counts the number of nodes in a Deque list
  public int size() {
    return this.header.next.sizeHelp();
  }

  // consumes a value of type T and inserts it at the front of the list
  public void addAtHead(T t) {
    this.header.insertBetween(t, this.header, this.header.next);
  }

  // consumes a value of type T and inserts it at the end of the list
  public void addAtTail(T t) {
    this.header.insertBetween(t, this.header.prev, this.header);
  }

  // removes the first node from this Deque
  // return the item that’s been removed from the list.
  public T removeFromHead() {
    return this.header.next.removeHelp();
  }

  // removes the last node from this Deque
  // return the item that’s been removed from the list.
  public T removeFromTail() {
    return this.header.prev.removeHelp();
  }

  // returns the first node that satisfy the Predicate<T>
  public ANode<T> find(Predicate<T> pred) {
    return this.header.next.findHelp(pred);
  }

  // removes the given node from this Deque.
  public void removeNode(ANode<T> node) {
    if (node != this.header) {
      node.removeHelp();
    }
  }
}

//the abstract class represent the ANode<T>
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // constructor
  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  // calculate the size of the Deque
  public abstract int sizeHelp();

  // insert the node between the given two nodes
  public void insertBetween(T t, ANode<T> prev, ANode<T> next) {
    Node<T> temp = new Node<T>(t, next, prev);
    prev.next = temp;
    next.prev = temp;
  }

  // remove the given node from the Deque
  public abstract T removeHelp();

  // helper of find method the specific ANode
  public abstract ANode<T> findHelp(Predicate<T> whichOne);

}

//class represent the Node<T>
class Node<T> extends ANode<T> {
  T data;

  // constructor
  Node(T data) {
    super(null, null);
    this.data = data;
  }

  // convenience constructor
  Node(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    this.next = new Util().checkNullness(this.next, "this.next is null");
    this.prev = new Util().checkNullness(this.prev, "this.prev is null");
    this.data = data;
    // fix the links
    next.prev = this;
    prev.next = this;
  }

  // count the number of nodes
  public int sizeHelp() {
    return 1 + this.next.sizeHelp();
  }

  // return the first Node that satisfy the Predicate
  public ANode<T> findHelp(Predicate<T> whichOne) {
    if (whichOne.test(this.data)) {
      return this;
    }
    else {
      return this.next.findHelp(whichOne);
    }
  }

  //the helper for the remove method to removes node and fix all links
  public T removeHelp() {
    // remove the current node
    prev.next = this.next;
    // update this node links after removing
    next.prev = this.prev;
    return this.data;
  }
}

//class represents the Sentinel<T>
class Sentinel<T> extends ANode<T> {

  // convenience constructor
  Sentinel(ANode<T> next, ANode<T> prev) {
    super(next, prev);
  }

  // constructor
  Sentinel() {
    super(null, null);
    this.next = this;
    this.prev = this;
  }

  // count the number of nodes
  public int sizeHelp() {
    return 0;
  }

  // remove the first node from the list of ANode
  public T removeHelp() {
    throw new RuntimeException("Can't try to remove on a Sentinel!");
  }

  // return the first Node that satisfy the IPred
  public ANode<T> findHelp(Predicate<T> pred) {
    return this;
  }

}

//utility class to check data integrity
class Util {

  // check the nullness of ANode objects
  <T> ANode<T> checkNullness(ANode<T> n, String msg) {
    if (n == null) {
      throw new IllegalArgumentException(msg);
    }
    else {
      return n;
    }
  }
}

//class implement Predicate<T>
class FindNode implements Predicate<String> {
  String n1;

  FindNode(String n1) {
    this.n1 = n1;
  }

  // test whether two data are same
  public boolean test(String data) {
    return (data.equals(n1));
  }
}

//example of the Deque
class ExamplesDeque {

  // 3 lists: mt, los, unsorted los
  Deque<String> deque1;
  Deque<String> deque2;
  Deque<String> deque3;
  Deque<String> deque4;
  // sorted los: "abc" "bcd" "cde" "def"
  // Deque<String> deque2 = new Deque<String>();
  // unsorted los: "cde" "bcd" "def" "abc"
  // Deque<String> deque3 = new Deque<String>();

  // represent the header of the Deque
  ANode<String> header1;
  ANode<String> header2;
  ANode<String> header3;
  ANode<String> header4;
  
  //define local variable
  String nullPrev = "this.prev is null";
  String nullNext = "this.next is null";

  //initial data
  void initData() {
    // initial the deque with the Deque consttructor
    this.deque1 = new Deque<String>();
    this.deque2 = new Deque<String>();
    this.deque3 = new Deque<String>();
    this.deque4 = new Deque<String>();

    // DEQUE2 is sorted los in the order "abc" "bcd" "cde" "def"
    this.deque2.addAtHead("def");
    this.deque2.addAtHead("cde");
    this.deque2.addAtHead("bcd");
    this.deque2.addAtHead("abc");

    // DEQUE3 is unsorted los in the order "cde" "bcd" "def" "abc"
    this.deque3.addAtHead("abc");
    this.deque3.addAtHead("def");
    this.deque3.addAtHead("bcd");
    this.deque3.addAtHead("cde");

    // DEQUE4 is the los after deque3 remove the head node
    this.deque4.addAtHead("def");
    this.deque4.addAtHead("cde");
    this.deque4.addAtHead("bcd");

    // get the header of the Deque
    this.header1 = this.deque1.header;
    this.header2 = this.deque2.header;
    this.header3 = this.deque3.header;
    this.header4 = this.deque4.header;
  }
  
  // TEST FOR UTILL CLASS
  // checks that nullness of the constructor of the Node class
  boolean testCheckNullness(Tester t) {
    return t.checkExpect(new Util().checkNullness(this.header2.next, this.nullNext),
        this.header2.next)
        && t.checkExpect(new Util().checkNullness(this.header3.prev, this.nullPrev),
            this.header3.prev)
        && t.checkException(new IllegalArgumentException(this.nullPrev), new Util(),
            "checkNullness", new Node<String>("null").prev, this.nullPrev)
        && t.checkException(new IllegalArgumentException(this.nullNext), new Util(),
            "checkNullness", new Node<String>("null").next, this.nullNext);
  }

  // HELPERS TEST
  // test the sizeHelp mehod
  boolean testSizeHelp(Tester t) {
    // 1. start with the initial data
    initData();
    // 2. check the Expection of the method
    return t.checkExpect(this.header1.sizeHelp(), 0) // test for empty list
        && t.checkExpect(this.header2.next.sizeHelp(), 4) // non-empty deque check
        && t.checkExpect(this.header3.prev.sizeHelp(), 1)
        && t.checkExpect(this.header4.next.sizeHelp(), 3);
  }

  // test the insertBetween method
  boolean testInsertBetween(Tester t) {
    // 1. start with the initial data
    initData();
    // 2. modify the data
    // for deque1
    ANode<String> prev = this.header1;
    ANode<String> next = this.header1.next;
    this.header1.insertBetween("HEAD", prev, next);
    ANode<String> insertNode1 = new Node<String>("HEAD", next, prev);
    // for deque2
    ANode<String> prev2 = this.header2;
    ANode<String> next2 = this.header2.next;
    this.header2.insertBetween("HEAD", prev2, next2);
    ANode<String> insertNode2 = new Node<String>("HEAD", next2, prev2);
    // for deque3
    ANode<String> prev3 = this.header3.prev;
    ANode<String> next3 = this.header3;
    this.header3.insertBetween("HEAD", prev3, next3);
    ANode<String> insertNode3 = new Node<String>("TAIL", next3, prev3);
    // for deque4
    ANode<String> prev4 = this.header4.prev;
    ANode<String> next4 = this.header4;
    this.header2.insertBetween("HEAD", prev4, next4);
    ANode<String> insertNode4 = new Node<String>("TAIL", next4, prev4);

    // 3. check the Expection of the method
    // TEST FOR DEQUE1 -- insert node on empty deque
    return t.checkExpect(this.header1.next, insertNode1) // check the insertion
        && t.checkExpect(insertNode1.prev, prev) // check whether the links of new node fixes
        && t.checkExpect(insertNode1.next, next) && t.checkExpect(prev.next, insertNode1) 
        && t.checkExpect(next.prev, insertNode1)
        // TEST FOR DEQUE2 -- insert node on non-empty node
        && t.checkExpect(this.header2.next, insertNode2) && t.checkExpect(insertNode2.prev, prev2)
        && t.checkExpect(insertNode2.next, next2) && t.checkExpect(prev2.next, insertNode2)
        && t.checkExpect(next2.prev, insertNode2)
        // TEST FOR DEQUE3
        && t.checkExpect(this.header3.prev, insertNode3) && t.checkExpect(insertNode3.prev, prev3)
        && t.checkExpect(insertNode3.next, next3) && t.checkExpect(prev3.next, insertNode3)
        && t.checkExpect(next3.prev, insertNode3)
        // TEST FOR DEQUE4
        && t.checkExpect(this.header4.prev, insertNode4) && t.checkExpect(insertNode4.prev, prev4)
        && t.checkExpect(insertNode4.next, next4) && t.checkExpect(prev4.next, insertNode4)
        && t.checkExpect(next4.prev, insertNode4);
  }

  // test the removeHelp method
  boolean testRemovehelp(Tester t) {
    // 1. start with the initial data
    initData();
    // 2. modify the data
    // case1 : for deque2 (size 4)
    // remove from the head
    ANode<String> prev1 = this.header2;
    ANode<String> next1 = this.header2.next;
    this.header2.next.removeHelp();
    // case2 : for deque3 (size 4)
    // remove from the tail
    ANode<String> prev2 = this.header3;
    ANode<String> next2 = this.header3.next;
    this.header3.prev.removeHelp();
    // case3 : for deque4 (size 3)
    // remove from the head
    this.header3.next.removeHelp();
    // case3 : for deque4 (size 3)
    // remove from the head
    ANode<String> prev4 = this.header4;
    ANode<String> next4 = this.header4.next;
    this.header4.next.removeHelp();
    // 3. check the sentinel case
    t.checkException(new RuntimeException("Can't try to remove on a Sentinel!"), this.deque1,
        "removeFromHead");
    // 4. check the node case
    // check order : the node itself -> the links of neighbor -> size of deque
    return t.checkExpect(this.header2.next, next1.next) // case1
        && t.checkExpect(next1.prev, prev1) && t.checkExpect(prev1.next, next1.next)
        && t.checkExpect(this.deque2.size(), 3)
        // case2 & case3
        && t.checkExpect(this.header3.prev, prev2.prev) && t.checkExpect(next2.prev, prev2)
        && t.checkExpect(prev2.next, next2.next)
        // the size should be reduced twice since we call
        // the removeHelp twice
        && t.checkExpect(this.deque3.size(), 2)
        // case 4
        && t.checkExpect(this.header4.next, next4.next) // case1
        && t.checkExpect(next4.prev, prev4) && t.checkExpect(prev4.next, next4.next)
        && t.checkExpect(this.deque4.size(), 2);
  }

  // test the findHekp method
  boolean testFindHelp(Tester t) {
    // 1. start with the initial data
    initData();
    // LOCAL VARIABLE
    ANode<String> node = this.header4.next;
    // 2. test the method
    return // sentinel case -- should return itself
    t.checkExpect(this.header1.findHelp(new FindNode("null")), this.header1)
        // find the first node of Deque
        && t.checkExpect(this.header2.findHelp(new FindNode("abc")), this.header2.next)
        // find the node that is not existed in the Deque
        && t.checkExpect(this.header3.findHelp(new FindNode("cannot find")), this.header3)
        // find the node in the middle of the Deque
        && t.checkExpect(this.header4.findHelp(new FindNode("cde")), node.next);
  }

  // TEST ALL METHOD IN THE DEQUE CLASS
  // test the method size
  boolean testSize(Tester t) {
    // 1. start with the initial data
    initData();
    // 2. check the expect result
    return t.checkExpect(this.deque1.size(), 0) // check the empty case
        && t.checkExpect(this.deque2.size(), 4) // check the non-empy case
        && t.checkExpect(this.deque4.size(), 3); // check the different size of non-empty case
  }

  // test addAtHead for empty case
  void testAddAtHead(Tester t) {
    // 1. start with the initial data
    initData();
    // 2. modify the data
    this.deque1.addAtHead("abc");
    // test addAtHead for empty deque1
    // check the node
    t.checkExpect(this.deque1.header.next,
        new Node<String>("abc", this.deque1.header, this.deque1.header));
    // check all links
    t.checkExpect(this.deque1.header.prev, this.deque1.header.next);
    // check the size of the deque to make sure the insertion happen
    t.checkExpect(this.deque1.size(), 1);
  }

  // test addAtHead for nonempty deque
  void testAddAtHead2(Tester t) {
    // 1. start with initial data
    initData();
    // modify the data
    this.deque1.addAtHead("bcd");
    this.deque1.addAtHead("abc");
    // check the node
    t.checkExpect(this.deque1.header.next, new Node<String>("abc",
        new Node<String>("bcd", this.deque1.header, this.deque1.header.next), this.deque1.header));
    // check the link -- can see it from insertBetween checkExpect
    // check the size
    t.checkExpect(this.deque1.size(), 2);
  }

  // test addAtTail for empty deque
  void testAddAtTail(Tester t) {
    initData();
    this.deque1.addAtTail("abc");
    t.checkExpect(this.deque1.header.next,
        new Node<String>("abc", this.deque1.header, this.deque1.header));
    t.checkExpect(this.deque1.header.prev, this.deque1.header.next);
    t.checkExpect(this.deque1.size(), 1);
  }

  // test addAtTail for non-empty deque
  void testAddAtTai2(Tester t) {
    initData();
    // save the local vriable
    ANode<String> prev = this.deque2.header.prev;
    // check the node
    this.deque2.addAtTail("tail");
    t.checkExpect(this.deque2.header.prev, 
        new Node<String>("tail", this.deque2.header, prev));
    // check the size
    t.checkExpect(this.deque2.size(), 5);
  }

  // test removeFromHead for empty Deque
  public boolean testRemoveFromHead(Tester t) {
    initData();
    return t.checkException(
        new RuntimeException("Can't try to remove on a Sentinel!"), 
        this.deque1,
        "removeFromHead");
  }

  // test removeFromHead for nonempty Deque
  public void testRemoveFromHead2(Tester t) {
    // initialize the data
    initData();
    // test Deque2
    this.deque2.removeFromHead();
    // the expect result
    t.checkExpect(this.deque2.header.next, this.deque4.header.next);
    // check the deque1
    // first enroll one node to deque1
    this.deque1.addAtHead("abc");
    t.checkExpect(this.deque1.header.next,
        new Node<String>("abc", this.deque1.header, this.deque1.header));
    // then remove that new node
    this.deque1.removeFromHead();
    // we have the same deque before and after the method call
    t.checkExpect(this.deque1.header.next, new Sentinel<String>());
  }

  // test find Method
  void testFind(Tester t) {
    initData();
    t.checkExpect(this.deque1.find(new FindNode("abc")), this.deque1.header);
    t.checkExpect(this.deque2.find(new FindNode("abc")), this.deque2.header.next);
  }

  // test for removeNode
  void tesRemoveNode(Tester t) {
    // initialize the data
    initData();
    // define the local variable
    ANode<String> mnnext = this.header2.next.next; // middlenode next
    ANode<String> fnnext = this.header3.next.next; // firstNode next
    ANode<String> lnprev = this.header4.prev.prev; // lastNode prev
    ANode<String> newNode = new Node<String>("tail", this.header1, this.header1);
    ANode<String> middleNode = mnnext; // with data "bcd"
    ANode<String> firstNode = this.header3.next; // with data "cde"
    ANode<String> lastNode = this.header4.prev; // with data"def"this.header1
    // modofy the data
    this.deque1.removeNode(newNode);
    this.deque2.removeNode(middleNode);
    this.deque3.removeNode(firstNode);
    this.deque4.removeNode(lastNode);
    // test the result of the data
    t.checkExpect(this.deque1, this.deque1);
    t.checkExpect(mnnext.prev, this.header2.next);
    t.checkExpect(this.header3.next, fnnext);
    t.checkExpect(this.header4.prev, lnprev);
  }

}