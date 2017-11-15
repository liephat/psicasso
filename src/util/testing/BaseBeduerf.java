package util.testing;

/**
 * BaseBeduerf ist the core class for generating, storing and calculating
 * the needs of our virtual agents
 * @author  Marius Raab
 * @version 2.0, September 2006
 */

class BaseBeduerf {

  /**
   * the basic variables for our system of need
   * x and y: the current values for the level of the need and the urge to satisfy it
   * grundstimmung: the urge when x is zero
   * schwingungsbreite: the possible range of values for y (between -1 and 1 or less)
   * y-shift: shifts the curve up and down
   * lochImTank: used to modify the speed the "tank" loses content
   * name: to store a name for a given set of modificators
   */

  private double x, y;
  private double grundstimmung;
  private double schwingungsbreite;
  private double neurotizismus;
  private double y_shift;
  private double lochImTank;
  private String name;

  /**
  * the methods for setting and getting the values of the private variables
  */

  public void setLochImTank(double set) {
    this.lochImTank = set;
  }

  public double getLochImTank() {
    return this.lochImTank;
  }

  public void setGrundstimmung(double set) {
    this.grundstimmung = set;
  }

  public double getGrundstimmung() {
    return this.grundstimmung;
  }

  public void setName(String set) {
    this.name = set;
  }

  public String getName() {
    return this.name;
  }

  public void setSchwingungsbreite(double set) {
    this.schwingungsbreite = set;
  }

  public double getSchwingungsbreite() {
    return this.schwingungsbreite;
  }

  public void setNeurotizismus(double set) {
    this.neurotizismus = set;
  }

  public double getNeurotizismus() {
    return neurotizismus;
  }

  public void setY_shift(double set) {
    this.y_shift = set;
  }

  public double getY_shift() {
    return this.y_shift;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void raiseX(double i) {
    this.x += i;
  }

  public void lowerX(double i) {
    this.x -= i;
  }

  public double getX() {
    return this.x;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getY() {
    return this.y;
  }

 public void calculate() {

   this.y = calculate_y(this.x);

 }

 private double calculate_y(double x) {

    /** the heart of our system of needs
    * here, for the actual x-value (to speak in Doerner's words, for the actual fill level of the tank),
    * the y-value (the urge to staisfy the need) is calculated
    *
    * the tangens hyperbolicus is a fast and elegant way to do this
    * via four modificators nearly every plausible curve for the respective need may be generated,
    * thus allowing to create different "personalities"
    *
    * schwingungssbreite, a double between 0 and 1, defines the possible range of the y-values (between -1 and 1, or less)
    *
    * neurotizismus (double) defines how fast the urge to satisfy the need goes up (or down)
    *
    * grundstimmung (double) is used to set the basic urge when x equals zero
    * it actually shifts the curve to the left/ right
    *
    * the y-shift (double), as might be seen by the name, shifts the curve up and down
    */

    this.y = (schwingungsbreite * Math.tanh(neurotizismus * -this.x + grundstimmung) +
         y_shift);

    return this.y;
  }

  /**
   * default constructor
   */

  BaseBeduerf() {
    grundstimmung = 0;
    schwingungsbreite = 1;
    neurotizismus = 0.01;
    y_shift = 0;
    lochImTank = 1;
    setX(0);
    setY(0);
  }
}
