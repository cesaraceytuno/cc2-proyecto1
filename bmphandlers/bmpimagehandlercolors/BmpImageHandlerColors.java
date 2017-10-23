package bmphandlers.bmpimagehandlercolors;

import java.io.*;
import bmphandlers.*;

public class BmpImageHandlerColors extends ImageHandler{
  protected byte[] filebytes;
  protected PixelTable pixeltable;
  protected String redfilename;
  protected String greenfilename;
  protected String bluefilename;
  protected String sepiafilename;
  private int height;
  private int width;


  public BmpImageHandlerColors(String imagename){
    super(imagename);
    this.redfilename = "Red-" + imagename;
    this.greenfilename = "Green-" + imagename;
    this.bluefilename = "Blue-" + imagename;
    this.sepiafilename = "Sepia-" + imagename;
  }

  public void readFile() throws Exception{
    FileInputStream input = new FileInputStream(this.handledFileName);
		this.filebytes = new byte[input.available()];
		input.read(filebytes);
    this.width =  (filebytes[21]<<24) & 0xff000000|
                  (filebytes[20]<<16) & 0x00ff0000|
                  (filebytes[19]<<8)  & 0x0000ff00|
                  (filebytes[18]<<0)  & 0x000000ff;
    this.height = (filebytes[25]<<24) & 0xff000000|
                  (filebytes[24]<<16) & 0x00ff0000|
                  (filebytes[23]<<8)  & 0x0000ff00|
                  (filebytes[22]<<0)  & 0x000000ff;
    int bits =  (filebytes[29]<<8)  & 0x0000ff00|
                (filebytes[28]<<0)  & 0x000000ff;
		input.close();
		System.out.println("Imagen leida: " + this.handledFileName);
    System.out.println("Width: " + this.width);
    System.out.println("Height: " + this.height);
    System.out.println("Pixel bits: " + bits);
    String[] fileformat = handledFileName.split("[.]");
    if (bits != 24){
      System.out.println();
      System.out.println("Error: Number of bits in image not allowed");
      System.exit(1);
    }else if (!fileformat[1].equals("bmp")){
      System.out.println();
      System.out.println("Error: File format " + fileformat[1] + " not allowed");
      System.exit(1);
    }

    createPixelTable(this.filebytes, this.width, this.height);

  }

  public void generateFiles() throws Exception{
//======================================================RED FILE==========================================================================
    FileOutputStream output = new FileOutputStream(this.redfilename);
    //printing header of red file
    for (int i = 0; i < 54; i++){
      output.write(filebytes[i]);
    }
    //printing pixel data for red file
    for (int j = height - 1; j >= 0; j--){
      for (int i = 0; i < width; i++){
        Pixel pixel = pixeltable.getPixel(j,i);
        output.write(0);
        output.write(0);
        output.write(pixel.getRed());
      }
    }
    output.close();

//======================================================GREEN FILE==========================================================================
    output = new FileOutputStream(this.greenfilename);
    //printing header of green file
    for (int i = 0; i < 54; i++){
      output.write(filebytes[i]);
    }
    //printing pixel data for green file
    for (int j = height - 1; j >= 0; j--){
      for (int i = 0; i < width; i++){
        Pixel pixel = pixeltable.getPixel(j,i);
        output.write(0);
        output.write(pixel.getGreen());
        output.write(0);
      }
    }
    output.close();

//======================================================BLUE FILE==========================================================================
    output = new FileOutputStream(this.bluefilename);
    //printing header of blue file
    for (int i = 0; i < 54; i++){
      output.write(filebytes[i]);
    }
    //printing pixel data for blue file
    for (int j = height - 1; j >= 0; j--){
      for (int i = 0; i < width; i++){
        Pixel pixel = pixeltable.getPixel(j,i);
        output.write(pixel.getBlue());
        output.write(0);
        output.write(0);
      }
    }
    output.close();

//======================================================SEPIA FILE==========================================================================
    output = new FileOutputStream(this.sepiafilename);
    //printing header of sepia file
    for (int i = 0; i < 54; i++){
      output.write(filebytes[i]);
    }
    //printing pixel data for sepia file
    for (int j = height - 1; j >= 0; j--){
      for (int i = 0; i < width; i++){
        Pixel pixel = pixeltable.getPixel(j,i);
        int red =   (int)(pixel.getRed()*.393 + pixel.getGreen()*.769 + pixel.getBlue()*.189);
        int green = (int)(pixel.getRed()*.349 + pixel.getGreen()*.686 + pixel.getBlue()*.168);
        int blue =  (int)(pixel.getRed()*.272 + pixel.getGreen()*.534 + pixel.getBlue()*.131);
        output.write(blue > 255 ? 255 : blue);
        output.write(green > 255 ? 255 : green);
        output.write(red > 255 ? 255 : red);
      }
    }
    output.close();
    System.out.println("Imagenes generadas : " + this.redfilename + ", " + this.greenfilename + ", " + this.bluefilename + ", " + this.sepiafilename);
  }


  private void createPixelTable(byte[] filebytes, int width, int height) throws Exception{
    int x = 0;
    int y = height - 1;
    pixeltable = new PixelTable(height, width);
    for (int i = 54; i < filebytes.length - 1; i = i + 3){
      if (i%3 == 0){
        int blue = filebytes[i] & 0xff;
        int green = filebytes[i+1] & 0xff;
        int red = filebytes[i+2] & 0xff;
        Pixel pixel = new Pixel(red,green,blue);
        pixeltable.addPixel(pixel,y,x);
        x++;
      }
      if ((x)%width == 0){
        x = 0;
        if (y!=0 && x == 0) { y = y-1; }
      }
    }
  }
}
