package com.examples.swing.controller;

import java.util.ArrayList;
import java.util.List;

public class Ordre{
	private Etat commande;
	List<Observateur> mesObservateurs;
	
	public Ordre() {
		commande = Etat.AUCUNE;
		mesObservateurs = new ArrayList<>();
	}
	
	public Etat getCommande() {
		return commande;
	}
	public void setCommande(Etat commande) {
		this.commande = commande;
		for(Observateur ob:mesObservateurs){
			ob.notifier();
		}
	} 
	
	public void addObservateur(Observateur ob){
		mesObservateurs.add(ob);
	}
}
