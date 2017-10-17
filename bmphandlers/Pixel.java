package bmphandlers;

public class Pixel{
  private int red;
  private int green;
  private int blue;
  private int val;

  public Pixel(int red, int green, int blue){
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.val =  (red & 0xFF) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 16);
  }
  public int getRed(){
    return this.red;
  }
  public int getGreen(){
    return this.green;
  }
  public int getBlue(){
    return this.blue;
  }
  public void setRed(int red){
    this.red = red;
  }
  public void setGreen(int green){
    this.green = green;
  }
  public void setBlue(int blue){
    this.blue = blue;
  }

  public String toString(){
    return "RGB(" + red + "," + green + "," + blue + ") Val: " + val;
  }
}
