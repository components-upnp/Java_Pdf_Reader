package com.examples.pdf.convert;

import com.jmupdf.enums.ImageType;
import com.jmupdf.exceptions.DocException;
import com.jmupdf.exceptions.DocSecurityException;
import com.jmupdf.pdf.PdfDocument;

/**
 * Create a PNG file
 *
 */
public class Pdf_2_Png {

	public static void main(String[] args) {

		try {

			/*
			 * Open document
			 */
			//PdfDocument pdfDoc = new PdfDocument("c:\\tmp\\test1.pdf");
			PdfDocument pdfDoc = new PdfDocument("E:\\development\\indigo\\workspace_jmupdf-old\\pdf_docs\\itextinaction.pdf");

			/*
			 * Create JPeg file
			 */
			pdfDoc.saveAsPng(
					1,									// Page number
					"e:\\tmp\\L�sung.png",				// PNG file
					2f,									// Zoom	
					ImageType.IMAGE_TYPE_RGB);		// Color space. Only RGB, ARGB & Gray.

			/*
			 * Dispose
			 */
			pdfDoc.dispose();

		} catch (DocException e) {
			e.printStackTrace();
		} catch (DocSecurityException e) {
			e.printStackTrace();
		}
		
	}

}
