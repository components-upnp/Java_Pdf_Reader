/*
 * Copyright (C) 2010-2011 Pedro J Rivera
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.examples.swing.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fourthline.cling.model.meta.LocalService;

import com.examples.swing.util.FileChooser;
import com.examples.swing.view.MainView;
import com.examples.upnp.VisionneuseService;
import com.jmupdf.cbz.CbzDocument;
import com.jmupdf.document.Document;
import com.jmupdf.enums.ImageType;
import com.jmupdf.exceptions.DocException;
import com.jmupdf.exceptions.DocSecurityException;
import com.jmupdf.page.Page;
import com.jmupdf.pdf.PdfDocument;
import com.jmupdf.print.PrintServices;
import com.jmupdf.xps.XpsDocument;

/**
 * MainController Class
 * 
 * @author Pedro J Rivera
 *
 */
public class MainController implements ActionListener, ChangeListener, WindowListener,Observateur {
	private MainView view;
	private MousePanController mousePanController;
	private KeyController keyController;
	private Document document;
	private Page page;
	private PrintServices printService;
	private float zoom = 1f;
	private int antiAliasLevel = 8;
	private float gammaLevel = 1f;
	private ImageType color = ImageType.IMAGE_TYPE_RGB;
	private boolean isOpened = false;
	private boolean isZooming = false;
	private int maxStore = 60; 
	private Ordre ordre;
	private LocalService<VisionneuseService>service;
	/**
	 * Main controller
	 * @param view
	 */
	public MainController(MainView view,LocalService<VisionneuseService> service) {
		this.ordre = new Ordre();
		this.service = service;
		this.ordre.addObservateur(this);
		this.view = view;		
		view.setActionListeners(this);
		view.setWindowListener(this);		
		view.setChangeListener(this);
		view.setTitle(null);
		view.clearPageCanvas();
		view.show();
		this.ordre.setCommande(Etat.AUCUNE);
        this.service.getManager().getImplementation().getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("status")) {
                    Etat etat = (Etat) evt.getNewValue();
                    ordre.setCommande(etat);
                }            
            }
            
});
	}
	
	
	//methode pour gerer le changement d'etat de la variable ordre
	@Override
	public void notifier() {
		Etat etat = ordre.getCommande();
		if(etat == Etat.DROITE)
			avancer();
		else if(etat == Etat.GAUCHE)
			reculer();
		else if(etat == Etat.BAS){
			diminuerZoom();
			zoomer();
		}
		else if(etat == Etat.HAUT){
			augmenterZoom();
			zoomer();
		}
	}
	
	public Ordre getOrdre(){
		return ordre;
	}
	
	// ----------------------------------------------------
	// WindowListener Interface
	// ----------------------------------------------------

	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		if (printService != null && !printService.isPrintJobDone()) { 
			printService.cancel();
			printService.waitForPrintJobDone();
		}
		System.exit(0);
	}

	// ----------------------------------------------------
	// ChangedListener Interface
	// ----------------------------------------------------

	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if (source == null || !isOpened) {
        	return;
        }
	
		if (source.equals(view.getPageNumber())) {
			int p = getPageNumber();
			if ( p < 1 ) {
				view.getPageNumber().setValue(Integer.valueOf("1"));
			} 
			else if ( p > document.getPageCount() ) {
				view.getPageNumber().setValue(Integer.valueOf(document.getPageCount()));
			} else {
				if (p != page.getPageNumber()) {
					if (!view.getPageCanvas().isPageRendering()) {
						setPage();
					} else {
						view.getPageNumber().setValue(Integer.valueOf(page.getPageNumber()));
					}
				}
			}
		}		
	}

	// ----------------------------------------------------
	// ActionListener Interface
	// ----------------------------------------------------
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
   
		if (source == null) {
        	return;
        }
		
		if(source.equals(view.getOpen())){
			openDoc();
		}
		else if (source.equals(view.getFstPage())) {
			view.getPageNumber().setValue(Integer.valueOf(1));
		}
		
		else if (source.equals(view.getLstPage())) {
			view.getPageNumber().setValue(Integer.valueOf(document.getPageCount()));
		}

		else if (source.equals(view.getNxtPage())) {
			ordre.setCommande(Etat.DROITE);
		}

		else if (source.equals(view.getPrvPage())) {
			ordre.setCommande(Etat.GAUCHE);
		}

		else if (source.equals(view.getComboZoom())) {
			zoomer();
		}
	}
	
	// ----------------------------------------------------
	// ------------------
	// ----------------------------------------------------
	
	
	/**
	 * Set page view
	 */
	private void setPage() {
		if (isOpened) {			
			page = null;
			page = document.getPage(getPageNumber());
			if (page != null) {
				try {
					if (isZooming) {
						view.getPageCanvas().scaleImage(zoom);
					}
					view.getPageCanvas().setPage(page, zoom,Page.PAGE_ROTATE_AUTO, color, antiAliasLevel, gammaLevel);
				} catch (OutOfMemoryError e) {
					view.getPageCanvas().getRenderer().dispose();				
					view.getPageCanvas().repaint();
					System.gc();
				} catch (Exception e) {
				}
			}
		}
		isZooming = false;
	}
	
	/**
	 * Open Document
	 */
	private void openDoc() {
		File file = FileChooser.getPdfDocument(view.getMainFrame());
		if (file != null) {
			closeDoc();
			String pass = "";
			while (true) {
				try {
					if (FileChooser.getExtension(file).equals("pdf")) {
						document = new PdfDocument(file.toString(), pass, maxStore);	
					} else if (FileChooser.getExtension(file).equals("xps")) {
						document = new XpsDocument(file.toString(), maxStore);
					} else if (FileChooser.getExtension(file).equals("cbz")) {
						document = new CbzDocument(file.toString(), maxStore);
					}
					if (document != null && document.getHandle() > 0) {
						zoom = 0.75f;
						antiAliasLevel = 8;
						page = document.getPage(1);								
						view.setPageCanvas(page, zoom, Page.PAGE_ROTATE_AUTO, color, antiAliasLevel, gammaLevel);
						mousePanController = new MousePanController(view);
						view.setPanningListener(mousePanController);
						keyController = new KeyController(view,this.ordre);
						view.setKeyListener(keyController);
						view.getPageNumber().setValue(Integer.valueOf(1));
						isOpened = true;
						break;
					}
				} catch (DocException e1) {
					e1.printStackTrace();
					break;
				} catch (DocSecurityException e1) {
					pass = JOptionPane.showInputDialog(null, "Document requires authentication:");
					if (pass == null) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Close Document
	 */
	private void closeDoc() {
		isOpened = false;
		view.getPageNumber().requestFocusInWindow();		
		if (document != null) {
			document.close();
			if (mousePanController != null) {
				view.removePanningListener(mousePanController);
				mousePanController = null;
			}
			if (keyController != null) {
				view.removeKeyListener(keyController);
				keyController = null;
			}
			view.clearPageCanvas();
			document = null;			
		}		
	}
	
	/**
	 * Get page number from JSpinner
	 * @return
	 */
	private int getPageNumber() {
		return ((Integer)view.getPageNumber().getValue()).intValue();
	}

	/**
	 * Get zoom level as float value
	 * @return
	 */
	private float getZoomLevel() {
		String s = (String)view.getComboZoom().getSelectedItem();
		s = s.replace("%", "");
		float f = Float.valueOf(s.trim());
		f = f / 100;
		isZooming = true;
		return f;
	}
	
	
    /**
     * Print test messages
     * @param text
     */
    protected void log(String text) {
    	System.out.println(text);
    }

	public Document getDocument() {
		return document;
	}

	public Page getPage() {
		return page;
	}

	//methode pour next page
    private void avancer(){
    	if (page.getPageNumber() < document.getPageCount()) {
			int p = page.getPageNumber() + 1;
			view.getPageNumber().setValue(Integer.valueOf(p));
		}
    }
    
    //methode pour reculer
    private void reculer(){
    	if (page.getPageNumber() != 1) {
			int p = page.getPageNumber() - 1;
			view.getPageNumber().setValue(Integer.valueOf(p));
		}
    }
    
    //methode pour actualiser le zoom
    private void zoomer(){
    	zoom = getZoomLevel();
		setPage();
    }
    
    //methode pour diminuer la taille du zoom
    private void diminuerZoom(){
    	int index = view.getComboZoom().getSelectedIndex();
    	if(index != 0){
    		view.getComboZoom().setSelectedIndex(index-1);
    	}
    }
    
  //methode pour augmenter la taille du zoom
    private void augmenterZoom(){
    	int index = view.getComboZoom().getSelectedIndex();
    	if(index < view.getComboZoom().getModel().getSize() - 1){
    		view.getComboZoom().setSelectedIndex(index+1);
    	}
    }
}
	