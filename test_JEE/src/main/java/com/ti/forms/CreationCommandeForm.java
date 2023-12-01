package com.ti.forms;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.ti.beans.Client;
import com.ti.beans.Commande;
import com.ti.dao.ClientDAO;
import com.ti.dao.CommandeDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class CreationCommandeForm {

	private static final String CHAMP_DATE = "dateCommande";
	private static final String CHAMP_MONTANT = "montantCommande";
	private static final String CHAMP_MODE_PAIEMENT = "modePaiementCommande";
	private static final String CHAMP_STATUT_PAIEMENT = "statutPaiementCommande";
	private static final String CHAMP_MODE_LIVRAISON = "modeLivraisonCommande";
	private static final String CHAMP_STATUT_LIVRAISON = "statutLivraisonCommande";
	public static final String ATT_LISTE = "listeCommande";
	public static final String ATT_LISTE_CLIENT = "listeClient";
	private static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";

	private String resultat;
	private Map<String, String> erreurs = new HashMap<String, String>();
	private Map<Long, Commande> listeCommandes;
	private CommandeDAO commandeDao;
	private ClientDAO clientDao;

	public CreationCommandeForm(CommandeDAO commandeDao, ClientDAO clientDao) {
		this.commandeDao = commandeDao;
		this.clientDao = clientDao;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public String getResultat() {
		return resultat;
	}

	public Commande creerCommande(HttpServletRequest request, boolean isNewClient, String chemin) {

		CreationClientForm clientForm = new CreationClientForm(clientDao);
		Client client = null;
		if (isNewClient) {
			client = clientForm.createClient(request, chemin);
		} else {
			HttpSession session = request.getSession();
			long id = Long.parseLong(request.getParameter("clientChoisi"));
			Map<Long, Client> listeClient = (Map<Long, Client>) session.getAttribute(ATT_LISTE_CLIENT);
			client = listeClient.get(id);
//			client=clientDao.find(id);
		}

		erreurs = clientForm.getErreurs();
//		System.out.println(erreurs);
		DateTime dt = new DateTime();
//		DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
		DateTime date = dt.toDateTime();

		String montant = getValeurChamp(request, CHAMP_MONTANT);
		String modePaiement = getValeurChamp(request, CHAMP_MODE_PAIEMENT);
		String statutPaiement = getValeurChamp(request, CHAMP_STATUT_PAIEMENT);
		String modeLivraison = getValeurChamp(request, CHAMP_MODE_LIVRAISON);
		String statutLivraison = getValeurChamp(request, CHAMP_STATUT_LIVRAISON);

		Commande commande = new Commande();

		commande.setClient(client);
		commande.setDate(date);

		double valeurMontant = -1;
		try {
			valeurMontant = validationMontant(montant);
		} catch (Exception e) {
			setErreur(CHAMP_MONTANT, e.getMessage());
		}
		commande.setMontant(valeurMontant);

		try {
			validationModePaiement(modePaiement);
		} catch (Exception e) {
			setErreur(CHAMP_MODE_PAIEMENT, e.getMessage());
		}
		commande.setModePaiement(modePaiement);

		try {
			validationStatutPaiement(statutPaiement);
		} catch (Exception e) {
			setErreur(CHAMP_STATUT_PAIEMENT, e.getMessage());
		}
		commande.setStatutPaiement(statutPaiement);

		try {
			validationModeLivraison(modeLivraison);
		} catch (Exception e) {
			setErreur(CHAMP_MODE_LIVRAISON, e.getMessage());
		}
		commande.setModeLivraison(modeLivraison);

		try {
			validationStatutLivraison(statutLivraison);
		} catch (Exception e) {
			setErreur(CHAMP_STATUT_LIVRAISON, e.getMessage());
		}
		commande.setStatutLivraison(statutLivraison);

		if (erreurs.isEmpty()) {
			resultat = "Succès de la création de la commande.";
			createListCommande(request, commande);
			commandeDao.create(commande);
		} else {
			resultat = "Échec de la création de la commande.";
		}
		return commande;
	}

	private double validationMontant(String montant) throws Exception {
		double temp;
		if (montant != null) {
			try {
				temp = Double.parseDouble(montant);
				if (temp < 0) {
					throw new Exception("Le montant doit être un nombre positif.");
				}
			} catch (NumberFormatException e) {
				temp = -1;
				throw new Exception("Le montant doit être un nombre.");
			}
		} else {
			temp = -1;
			throw new Exception("Merci d'entrer un montant.");
		}
		return temp;
	}

	private void validationModePaiement(String modePaiement) throws Exception {
		if (modePaiement != null) {
			if (modePaiement.length() < 2) {
				throw new Exception("Le mode de paiement doit contenir au moins 2 caractères.");
			}
		} else {
			throw new Exception("Merci d'entrer un mode de paiement.");
		}
	}

	private void validationStatutPaiement(String statutPaiement) throws Exception {
		if (statutPaiement != null && statutPaiement.length() < 2) {
			throw new Exception("Le statut de paiement doit contenir au moins 2 caractères.");
		}
	}

	private void validationModeLivraison(String modeLivraison) throws Exception {
		if (modeLivraison != null) {
			if (modeLivraison.length() < 2) {
				throw new Exception("Le mode de livraison doit contenir au moins 2 caractères.");
			}
		} else {
			throw new Exception("Merci d'entrer un mode de livraison.");
		}
	}

	private void validationStatutLivraison(String statutLivraison) throws Exception {
		if (statutLivraison != null && statutLivraison.length() < 2) {
			throw new Exception("Le statut de livraison doit contenir au moins 2 caractères.");
		}
	}

	/*
	 * Ajoute un message correspondant au champ spécifié à la map des erreurs.
	 */
	private void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	/*
	 * Méthode utilitaire qui retourne null si un champ est vide, et son contenu sinon.
	 */
	private static String getValeurChamp(HttpServletRequest request, String nomChamp) {
		String valeur = request.getParameter(nomChamp);
		if (valeur == null || valeur.trim().length() == 0) {
			return null;
		} else {
			return valeur;
		}
	}

	public Map getListeCommandes() {
		return listeCommandes;
	}

	private void createListCommande(HttpServletRequest request, Commande commande) {
		HttpSession session = request.getSession();
		listeCommandes = (Map<Long, Commande>) session.getAttribute(ATT_LISTE);
		if (listeCommandes == null)
			listeCommandes = new HashMap<Long, Commande>();
		listeCommandes.put(commande.getId(), commande);
		session.setAttribute(ATT_LISTE, listeCommandes);
	}
}