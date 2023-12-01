package com.ti.forms;

import java.util.Map;

import com.ti.beans.Commande;
import com.ti.dao.CommandeDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SuppressionCommandeForm {

	public static final String CHAMP_DATE = "date";
	public static final String ATT_LISTE = "listeCommande";
	private Map<Long, Commande> listeCommandes;
	private CommandeDAO commandeDao;

	public SuppressionCommandeForm(CommandeDAO commandeDao) {
		this.commandeDao = commandeDao;
	}

	public Map<Long, Commande> getListeCommandes() {
		return listeCommandes;
	}

	public void suppCommande(HttpServletRequest request, String id) {
		long idLong = Long.parseLong(id);
		commandeDao.delete(idLong);
		HttpSession session = request.getSession();
		listeCommandes = (Map<Long, Commande>) session.getAttribute(ATT_LISTE);

		listeCommandes.remove(idLong);
		session.setAttribute(ATT_LISTE, listeCommandes);
	}

}
