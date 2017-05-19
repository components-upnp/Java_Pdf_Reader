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

package com.examples.swing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;

import com.examples.swing.resources.Images;
import com.jmupdf.enums.ImageType;
import com.jmupdf.page.Page;

/**
 * Main View
 * 
 * @author Pedro J Rivera
 * 
 */
public class MainView {

	private JFrame mainFrame;
	private JSplitPane splitPane;
	private JScrollPane scrollPane;

    private JComboBox<String> comboZoom;
    private JSpinner pageNumber;
    private JLabel pageCount;
    private JButton open;
    private JButton prvPage;
    private JButton nxtPage;
    private JButton fstPage;
    private JButton lstPage;
    
    private PageView pageView;

    private JToolBar toolBar1;

    private final String[] zoomLevels = {"25", "50", "75", "100", "125", "150", "200", "250", "300", "350", "400", "450", "500"};
    private final int defaultZoomIndex = 3;

	/**
	 * Class Constructor
	 */
	public MainView() {
		createUI();
	}
	
	public PageView getPageView() {
		return pageView;
	}
	
	/**
	 * Create User Interface
	 */
	private void createUI() {
		setLookAndFeel();
		buildMainFrame();
		buildSplitPane();
		buildToolBar();
	}
	
	/**
	 * Set default look and feel  
	 */
	private void setLookAndFeel() {		
		try {
			String os = System.getProperty("os.name");
			if (os.equals("Linux")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());	
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());				
			} catch (Exception e2) {
				// Well, we did the best we could!
			}
		}
	}

	/**
	 * Build main window frame
	 */
	private void buildMainFrame() {
		this.mainFrame = new JFrame();
		this.mainFrame.setSize(800, 700);
		this.mainFrame.setLocationRelativeTo(null);
		this.mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Build split pane component
	 */
	private void buildSplitPane() {
		this.splitPane = new JSplitPane();
		this.scrollPane = new JScrollPane();
		this.splitPane.setRightComponent(this.scrollPane);		
		this.scrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
		
		this.splitPane.getLeftComponent().setVisible(false);
		this.splitPane.setDividerSize(0);
		
		//this.mainFrame.getContentPane().add(this.splitPane, BorderLayout.CENTER);
		this.mainFrame.getContentPane().add(this.splitPane);
	}

	/**
	 * Build main tool bar
	 */
	private void buildToolBar() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		toolBar1 = new JToolBar();
				
		Insets margins = new Insets(0, 0, 0, 0);
		
		/*
		 * Open button 
		 */
		this.open = new JButton(Images.getImageIcon("open.gif"));		
		this.open.setMargin(margins);
		toolBar1.add(open);

		/*
		 * Page navigation buttons |< << >> >|
		 */
		toolBar1.addSeparator();		
		this.fstPage = new JButton(Images.getImageIcon("first.gif"));
		this.prvPage = new JButton(Images.getImageIcon("prev.gif"));
		this.nxtPage = new JButton(Images.getImageIcon("next.gif"));		
		this.lstPage = new JButton(Images.getImageIcon("last.gif"));		
		this.fstPage.setMargin(margins);
		this.prvPage.setMargin(margins);
		this.nxtPage.setMargin(margins);		
		this.lstPage.setMargin(margins);
		toolBar1.add(fstPage);
		toolBar1.add(prvPage);
		toolBar1.add(nxtPage);		
		toolBar1.add(lstPage);

		/*
		 * Page Number Spinner
		 */
		toolBar1.addSeparator();
		this.pageCount = new JLabel();
		this.pageNumber = new JSpinner();
		Dimension d = new Dimension(this.pageNumber.getPreferredSize());
		d.setSize(60, d.getHeight());
		this.pageNumber.setMaximumSize(d);
		toolBar1.add(new JLabel("Page: "));		
		toolBar1.add(this.pageNumber);
		toolBar1.add(new JLabel(" of "));
		toolBar1.add(this.pageCount);
		
		/*
		 * Zoom level combo box
		 */
		toolBar1.addSeparator();		
		JLabel zoomText = new JLabel("Zoom:");
		this.comboZoom = new JComboBox<String>(zoomLevels);
		d = new Dimension(comboZoom.getPreferredSize());
		d.setSize(60, d.getHeight());
		this.comboZoom.setMaximumSize(d);
		toolBar1.add(zoomText);
		toolBar1.add(comboZoom);
		
		// Set alignment
		toolBar1.setAlignmentX(0);
				
		// Add toolbars to panel
		panel.add(toolBar1);
		
		// Add panel to frame
		this.mainFrame.getContentPane().add(panel, BorderLayout.NORTH);
		
	}

	// ----------------------------------------------------
	// Frame
	// ----------------------------------------------------
	
	
	/**
	 * Change window title
	 * @param title
	 */
	public void setTitle(String title) {
		if (title == null || title.trim().length() == 0) {
			mainFrame.setTitle("Visionneuse 1.0");
		} else {
			mainFrame.setTitle("Visionneuse 1.0 - " + title);
		}
	}

	/**
	 * Close window
	 */
	public void close() {
		Toolkit.getDefaultToolkit().getSystemEventQueue()
		       .postEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * Display window
	 */
	public void show() {
		this.mainFrame.setVisible(true);
	}	
	
	/**
	 * Get JFrame associated with main window
	 * @return
	 */
	public JFrame getMainFrame() {
		return this.mainFrame;
	}
	
	//----------------------------------------------------
	//  listener pour avancer avec les fleches de direction
	//----------------------------------------------------
	public void setKeyListener(EventListener kl){
		this.pageView.addMouseListener((MouseListener) kl);
		this.pageView.addKeyListener((KeyListener) kl);
	}
	
	public void removeKeyListener(EventListener kl){
		this.pageView.removeMouseListener((MouseListener) kl);
		this.pageView.removeKeyListener((KeyListener) kl);
	}
	/**
	 * setWindowListener()
	 * @param wl
	 */
	public void setWindowListener(WindowListener wl) {
		this.mainFrame.addWindowListener(wl);
	}
	
	/**
	 * setActionListeners()
	 * @param al
	 */
	public void setActionListeners(ActionListener al) {
		this.open.addActionListener(al);
		this.prvPage.addActionListener(al);
		this.nxtPage.addActionListener(al);
		this.fstPage.addActionListener(al);
		this.lstPage.addActionListener(al);
		this.comboZoom.addActionListener(al);
	}	

	/**
	 * setChangeListeners()
	 * @param cl
	 */
	public void setChangeListener(ChangeListener cl) {
		this.pageNumber.addChangeListener(cl);
	}
	
	/**
	 * Set Mouse listener(s) for canvas panning
	 * @param el
	 */
	public void setPanningListener(EventListener el) {
		this.scrollPane.getViewport().addMouseMotionListener((MouseMotionListener)el);
		this.scrollPane.getViewport().addMouseListener((MouseListener)el);
		this.scrollPane.getHorizontalScrollBar().addAdjustmentListener((AdjustmentListener)el);
		this.scrollPane.getVerticalScrollBar().addAdjustmentListener((AdjustmentListener)el);
	}
	
	/**
	 * Remove Mouse listener(s) from canvas
	 * @param el
	 */
	public void removePanningListener(EventListener el) {
		this.scrollPane.getViewport().removeMouseMotionListener((MouseMotionListener)el);
		this.scrollPane.getViewport().removeMouseListener((MouseListener)el);
		this.scrollPane.getHorizontalScrollBar().removeAdjustmentListener((AdjustmentListener)el);
		this.scrollPane.getVerticalScrollBar().removeAdjustmentListener((AdjustmentListener)el);
	}
	
	// ----------------------------------------------------
	// Page Canvas
	// ----------------------------------------------------
	
	
	/**
	 * Clear page canvas area
	 */
	public void clearPageCanvas() {
		if (this.scrollPane.getViewport() != null) {
			this.scrollPane.getViewport().removeAll();
			this.scrollPane.setViewport(null);
			if (this.pageView != null) {
				this.pageView.dispose();
				this.pageView = null;
			}
		}
		this.splitPane.setVisible(false);		
		this.comboZoom.setEnabled(false);
		this.open.setEnabled(true);
		this.prvPage.setEnabled(false);
		this.nxtPage.setEnabled(false);
		this.fstPage.setEnabled(false);
		this.lstPage.setEnabled(false);
		this.pageNumber.setEnabled(false);		
		this.pageNumber.setValue(Integer.valueOf("0"));
		this.pageCount.setText("0");
		this.comboZoom.setSelectedIndex(defaultZoomIndex);
		this.setTitle("");
		this.mainFrame.validate();
	}

	/**
	 * Set page canvas area
	 * @param page
	 */
	public void setPageCanvas(Page page, float zoom, int rotate, ImageType color, int antiAliasLevel, float gamma) {
		if (this.pageView == null) {
			this.pageView = new PageView();
			this.scrollPane.setViewportView(pageView);
		}
		this.pageView.setPage(page, zoom, rotate, color, antiAliasLevel, gamma);
		pageView.requestFocus();
		this.splitPane.setVisible(true);		
		this.comboZoom.setEnabled(true);
		this.open.setEnabled(true);
		this.prvPage.setEnabled(true);
		this.nxtPage.setEnabled(true);
		this.fstPage.setEnabled(true);
		this.lstPage.setEnabled(true);
		this.pageNumber.setEnabled(true);
		this.pageNumber.setValue(Integer.valueOf(page.getPageNumber()));
		this.pageCount.setText(""+page.getDocument().getPageCount());
		this.comboZoom.setSelectedIndex(defaultZoomIndex);
		this.setTitle(page.getDocument().getDocumentName());
		this.mainFrame.validate();
	}
	
	/**
	 * Get page canvas
	 * @return
	 */
	public PageView getPageCanvas() {
		return pageView;
	}

	
		
	/**
	 * Get Zoom Combo Box
	 * @return
	 */
	public JComboBox<String> getComboZoom() {
		return comboZoom;
	}
	
	/**
	 * Get Rotate Combo Box
	 * @return
	 */
	
	/**
	 * Get Open 
	 * @return
	 */
	public JButton getOpen() {
		return open;
	}
	
	/**
	 * Get Previous Page
	 * @return
	 */
	public JButton getPrvPage() {
		return prvPage;
	}
	
	/**
	 * Get Next Page
	 * @return
	 */
	public JButton getNxtPage() {
		return nxtPage;
	}

	/**
	 * Get First Page
	 * @return
	 */
	public JButton getFstPage() {
		return fstPage;
	}
	
	/**
	 * Get Last Page
	 * @return
	 */
	public JButton getLstPage() {
		return lstPage;
	}
	
	/**
	 * Get page number text field
	 * @return
	 */
	public JSpinner getPageNumber() {
		return pageNumber;
	}

	public void setFullScreen(boolean b) {
		mainFrame.dispose();
		toolBar1.setVisible(!b);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setUndecorated(b);
		mainFrame.setVisible(true);
		pageView.requestFocus();
}
	
}
