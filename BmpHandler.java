/* CC2 - 2017 - PJ1 */
import bmphandlers.*;
import bmphandlers.bmpimagehandlercopy.*;
import bmphandlers.bmpimagehandlercolors.*;
import bmphandlers.bmphandlerrotators.*;
import bmphandlers.bmpkernelfilters.*;
import java.io.*;

public class BmpHandler {

	public static void runHandler(ImageHandler imgh) throws Exception {
		/** NO CAMBIE ESTE CODIGO **/
		System.out.println("--------------------------------------");
		System.out.println(imgh.getClass().getTypeName().toUpperCase() + ": ");
		System.out.println("\nLeyendo imagen : " + imgh.getFileName());
		try{
			imgh.readFile();
		}catch(FileNotFoundException f){
			System.out.println("Error: " + f.getMessage());
			System.exit(1);
		}
		System.out.println("Proceso de lectura de imagen terminado!");
		System.out.println("\nGenerando imagenes : ");
		imgh.generateFiles();
		System.out.println("Proceso de generacion de archivos terminado!");
		System.out.println("\n--------------------------------------");
		/** --------------------- **/
	}


	public static void main(String[] args) throws Exception {
		String runoption = "";
		String filename = "";
		String kernelfilename = "";

		if (args.length == 1){
			runoption = args[0];
		}else if (args.length == 2){
			runoption = args[0];
			filename = args[1];
		}else if (args.length == 3){
			runoption = args[0];
			kernelfilename = args[1];
			filename = args[2];
		}else{
			System.out.println("Error: Wrong number of Arguments");
			System.exit(1);
		}

		switch(runoption) {
			case "-copy": {
				BmpHandlerCopy bhc = new BmpHandlerCopy(filename);
				runHandler(bhc);
				break;
			}
			case "-colors": {
				BmpImageHandlerColors bhc = new BmpImageHandlerColors(filename);
				runHandler(bhc);
				break;
			}
			case "-rotate": {
				BmpImageHandlerRotator bhc = new BmpImageHandlerRotator(filename);
				runHandler(bhc);
				break;
			}
			case "-grayscale": {
				BmpImageToGrayscale bhc = new BmpImageToGrayscale(filename);
				runHandler(bhc);
				break;
			}
			case "-all": {
				runHandler(new BmpImageHandlerColors(filename));
				runHandler(new BmpImageHandlerRotator(filename));
				runHandler(new BmpImageToGrayscale(filename));
				break;
			}
			case "-kernel": {
				BMPKernelFilter bhc = new BMPKernelFilter(filename, kernelfilename);
				runHandler(bhc);
				break;
			}
			case "-help": {
				System.out.println("Usage: java BmpHandler -options [kernelfilename] imagename.bmp");
				System.out.println();
				System.out.println("options");
				System.out.println("-------");
				System.out.println("-colors:\twill generate 4 files corresponding to Red, Green, Blue and Sepia matrix of the image passed as argument");
				System.out.println("-rotate:\twill generate 2 files corresponding to a Vertical and Horizontal rotation of the image passed as argument");
				System.out.println("-grayscale:\twill generate a grayscale 8 bit image from the image passed as argument");
				System.out.println("-kernel:\twill generate a modified image using a \"kernelfilename\" matrix to the image passed as argument");
				System.exit(0);
			}
			default: {
				System.out.println("Opcion de ejecucion invalida");
			}
		}
	}
}
