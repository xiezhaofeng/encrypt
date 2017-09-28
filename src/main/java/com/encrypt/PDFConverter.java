package com.encrypt;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;


public class PDFConverter
{
	public static void converterByImage(){
		try {  
            String imagePath = "D:/df.jpg";
            String pdfPath = "D:/test.pdf";
            BufferedImage img = ImageIO.read(new File(imagePath));
            FileOutputStream fos = new FileOutputStream(pdfPath);
            Document doc = new Document(null, 0, 0, 0, 0);
            doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
            Image image = Image.getInstance(imagePath);
            PdfWriter.getInstance(doc, fos);
            doc.open();
            doc.add(image);
            doc.close();
        } catch (IOException e) {  
            e.printStackTrace();
        } catch (DocumentException e) {  
            e.printStackTrace();
        }  
	}
	public static String converterByImageToBase64(String content){
		try {  
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            Document doc = new Document(null, 0, 0, 0, 0);
            URL url = new URL(content);
            Image image = Image.getInstance(url);
            doc.setPageSize(new Rectangle(image.getWidth(), image.getHeight()));
            PdfWriter.getInstance(doc, byteOut);
            doc.open();
            doc.add(image);
            doc.close();
            return Base64.getEncoder().encodeToString(byteOut.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("converterByImageToBase64 occurred exception: " + e.getMessage());
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("converterByImageToBase64 occurred exception: " + e.getMessage());
        }  
	}
}
