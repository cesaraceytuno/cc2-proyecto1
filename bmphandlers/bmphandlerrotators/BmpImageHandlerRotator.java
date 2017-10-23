package bmphandlers.bmphandlerrotators;

import java.io.*;
import bmphandlers.*;

public class BmpImageHandlerRotator extends ImageHandler{
  protected byte[] filebytes;
  protected PixelTable pixeltable;
  protected String vfilename;
  protected String hfilename;
  private int height;
  private int width;


  public BmpImageHandlerRotator(String imagename){
    super(imagename);
    this.vfilename = "Vrotation-" + imagename;
    this.hfilename = "Hrotation-" + imagename;
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

    if (bits != 24){
      System.out.println();
      System.out.println("Error: Number of bits in image not allowed");
      System.exit(1);
    }

    createPixelTable(this.filebytes, this.width, this.height);

  }

  public void generateFiles() throws Exception{
//======================================================VERTICAL FILE==========================================================================
    FileOutputStream output = new FileOutputStream(this.vfilename);
    //printing header of vertical file
    for (int i = 0; i < 54; i++){
      output.write(filebytes[i]);
    }
    //printing pixel data for vertical file
    for (int j = height - 1; j >= 0; j--){
      for (int i = width - 1; i >= 0; i--){
        Pixel pixel = pixeltable.getPixel(j,i);
        output.write(pixel.getBlue());
        output.write(pixel.getGreen());
        output.write(pixel.getRed());
      }
    }
    output.close();

    //======================================================HORIZONTAL FILE==========================================================================
        output = new FileOutputStream(this.hfilename);
        //printing header of horizontal file
        for (int i = 0; i < 54; i++){
          output.write(filebytes[i]);
        }
        //printing pixel data for horizontal file
        for (int j = 0; j < height; j++){
          for (int i = 0; i < width; i++){
            Pixel pixel = pixeltable.getPixel(j,i);
            output.write(pixel.getBlue());
            output.write(pixel.getGreen());
            output.write(pixel.getRed());
          }
        }
        output.close();
    System.out.println("Imagenes generadas : " + this.vfilename + ", " + this.hfilename);
  }

  private void createPixelTable(byte[] filebytes, int width, int height) throws Exception{
    int x = 0;
    int y = height - 1;
    pixeltable = new PixelTable(height, width);
    for (int i = 54; i < filebytes.length - 1; i = i + 3){
      if (i%3 == 0){
        int red = filebytes[i+2] & 0xff;
        int green = filebytes[i+1] & 0xff;
        int blue = filebytes[i] & 0xff;
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
