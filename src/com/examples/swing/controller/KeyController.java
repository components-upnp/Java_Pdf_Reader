package com.examples.swing.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.examples.swing.view.MainView;

import javax.swing.*;

public class KeyController implements KeyListener,MouseListener{
	
	private MainView view;
	private Ordre ordre;

	public KeyController(MainView view,Ordre order) {
		super();
		this.view = view;
		this.ordre = order;
		//this.mainController = ctrl;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Touche appuyee");
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			this.ordre.setCommande(Etat.DROITE);
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			this.ordre.setCommande(Etat.GAUCHE);
		}else if(e.getKeyCode() == KeyEvent.VK_UP){
			this.ordre.setCommande(Etat.HAUT);
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			this.ordre.setCommande(Etat.BAS);
		}else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.ordre.setCommande(Etat.CENTRE);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		this.view.getPageView().requestFocusInWindow();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
