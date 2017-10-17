package bmphandlers;

public class PixelTable{
  private Pixel[][] table;
  private int width;
  private int height;

  public PixelTable(int height, int width){
    table = new Pixel[height][width];
    this.width = width;
    this.height = height;
  }

  public void addPixel(Pixel pixel, int y, int x) throws Exception{
    table[y][x] = pixel;
  }

  public Pixel getPixel(int y, int x){
    if (y < height || y > height || x < width || x > width){
      return null;
    }
    Pixel pixel = table[y][x];
    return pixel;
  }
}
