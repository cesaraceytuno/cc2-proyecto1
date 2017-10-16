package bmphandlers.bmpimagehandlercolors;

import java.io.*;
import java.util.*;
import bmphandlers.*;

public class BmpImageToGrayscale extends ImageHandler{
  protected byte[] filebytes;
  protected PixelTable pixeltable;
  protected String grayfilename;
  private int height;
  private int width;


  public BmpImageToGrayscale(String imagename){
    super(imagename);
    this.grayfilename = imagename.substring(0,imagename.length() - 4) + "-gray.bmp";
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
    int size =  (filebytes[5]<<24) & 0xff000000|
                (filebytes[4]<<16) & 0x00ff0000|
                (filebytes[3]<<8)  & 0x0000ff00|
                (filebytes[2]<<0)  & 0x000000ff;
    int sizeofbytes = (filebytes[37]<<24) & 0xff000000|
                  (filebytes[36]<<16) & 0x00ff0000|
                  (filebytes[35]<<8)  & 0x0000ff00|
                  (filebytes[34]<<0)  & 0x000000ff;

		input.close();
		System.out.println("Imagen leida: " + this.handledFileName);
    System.out.println("Width: " + this.width);
    System.out.println("Height: " + this.height);
    System.out.println("Pixel bits: " + bits);

    createPixelTable(this.filebytes, this.width, this.height);


  }

  public void generateFiles() throws Exception{
//======================================================GRAYSCALE FILE==========================================================================
    FileOutputStream output = new FileOutputStream(this.grayfilename);
    //printing header of grayscale file
    createHeader(output);

    //printing palettemap (values from 0 to 255)
    byte[] palette = new byte[1024];      //256 * 4(Red,Green,Blue,padding)
    for (int i = 0; i < 256; i++){
      palette[i * 4 + 0] = (byte)i;       //blue
      palette[i * 4 + 1] = (byte)i;       //green
      palette[i * 4 + 2] = (byte)i;       //red
      palette[i * 4 + 3] = (byte)0;       //padding
    }
    output.write(palette);


    //printing pixel data for grayscale file
    for (int j = height - 1; j >= 0; j--){
      for (int i = 0; i < width; i++){
        Pixel pixel = pixeltable.getPixel(j,i);
        int red = pixel.getRed();
        int green = pixel.getGreen();
        int blue = pixel.getBlue();
        int luminance = (int)(pixel.getRed()*0.3 + pixel.getGreen()*0.59 + pixel.getBlue()*0.11);
        output.write( luminance > 255 ? (byte)255 : (byte)luminance );
      }
    }
    output.close();

    System.out.println("Imagen generada: " + this.grayfilename);

  }

  private void createHeader(FileOutputStream output) throws Exception{
    int bfOffBits = 1078;
    int bfSize = 1077 + (width * height);
    int biClrUsed = 256;
    int biBitCount = 8;
    byte[] res;

    ArrayList<Byte> header = new ArrayList<Byte>();
    //fileheader
    header.add(filebytes[0]);         //adding bfType to header
    header.add(filebytes[1]);         //adding bfType to header
    //adding bfSize to header
    header.add( (byte)(bfSize) );
    header.add( (byte)(bfSize >> 8) );
    header.add( (byte)(bfSize >> 16) );
    header.add( (byte)(bfSize >> 24) );

    //adding bfReserved1, bfReserved2 to header
    header.add((byte)0);
    header.add((byte)0);
    header.add((byte)0);
    header.add((byte)0);

    //adding bfOffBits to header
    header.add( (byte)(bfOffBits) );
    header.add( (byte)(bfOffBits >> 8) );
    header.add( (byte)(bfOffBits >> 16) );
    header.add( (byte)(bfOffBits >> 24) );

    //imageheader
    for (int i = 14; i < 28; i++){
      header.add(filebytes[i]);       //adding biSize,biWidth,biHeight,biPlanes to header
    }
    //adding biBitCount to header
    header.add( (byte)(biBitCount & 0xff) );
    header.add( (byte)((biBitCount >> 8) & 0xff) );

    for (int i = 30; i < 46; i++){
      header.add(filebytes[i]);       //adding biCompression,biSizeImage,biXPelsPerMeter,biYPelsPerMeter to header
    }

    //adding biClrUsed to header
    header.add( (byte)(biClrUsed) );
    header.add( (byte)(biClrUsed >> 8) );
    header.add( (byte)(biClrUsed >> 16) );
    header.add( (byte)(biClrUsed >> 24) );

    //adding biClrImportant to header
    header.add((byte)0);
    header.add((byte)0);
    header.add((byte)0);
    header.add((byte)0);
    //adding to result array
    res = new byte[header.size()];
    for (int i = 0; i < header.size() - 1; i++){
      res[i] = header.get(i);
    }
    //writing to outputstream
    output.write(res);
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
