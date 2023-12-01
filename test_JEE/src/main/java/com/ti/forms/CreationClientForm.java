package com.ti.forms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ti.beans.Client;
import com.ti.dao.ClientDAO;

import eu.medsea.mimeutil.MimeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public final class CreationClientForm {

	public static final String CHAMP_NOM = "nomClient";
	public static final String CHAMP_PRENOM = "prenomClient";
	public static final String CHAMP_ADRESSE = "adresseClient";
	public static final String CHAMP_TELEPHONE = "telephoneClient";
	public static final String CHAMP_EMAIL = "emailClient";
	public static final String ATT_LISTE = "listeClient";
	private static final String CHAMP_IMAGE = "imageClient";

	private static final int TAILLE_TAMPON = 10240; // 10 ko

	private String resultat;
	private Map<String, String> erreurs = new HashMap<String, String>();
	private Map<Long, Client> listeClients;
	private ClientDAO clientDao;

	public CreationClientForm(ClientDAO clientDao) {
		this.clientDao = clientDao;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public Map<Long, Client> getListeClients() {
		return this.listeClients;
	}

	public String getResultat() {
		return resultat;
	}

	public Client createClient(HttpServletRequest request, String chemin) {
		String nom = getValeurChamp(request, CHAMP_NOM);
		String prenom = getValeurChamp(request, CHAMP_PRENOM);
		String adresse = getValeurChamp(request, CHAMP_ADRESSE);
		String telephone = getValeurChamp(request, CHAMP_TELEPHONE);
		String email = getValeurChamp(request, CHAMP_EMAIL);
		String cheminFichier = null;
		Client client = new Client();

		try {
			validationEmail(email);
		} catch (Exception e) {
			setErreur(CHAMP_EMAIL, e.getMessage());
		}
		client.setEmail(email);
		try {
			validationNom(nom);
		} catch (Exception e) {
			setErreur(CHAMP_NOM, e.getMessage());
		}
		client.setNom(nom);
		try {
			validationPrenom(prenom);
		} catch (Exception e) {
			setErreur(CHAMP_PRENOM, e.getMessage());
		}
		client.setPrenom(prenom);
		try {
			validationAdresse(adresse);
		} catch (Exception e) {
			setErreur(CHAMP_ADRESSE, e.getMessage());
		}
		client.setAdresse(adresse);
		try {
			validationTelephone(telephone);
		} catch (Exception e) {
			setErreur(CHAMP_TELEPHONE, e.getMessage());
		}
		client.setNumTel(telephone);
		try {
			cheminFichier = enregistrerFichier(request, chemin);
		} catch (Exception e) {
			setErreur(CHAMP_IMAGE, e.getMessage());
		}
		client.setImage(cheminFichier);
		if (erreurs.isEmpty()) {
			resultat = "Succès de la creation du client.";
			System.out.println(clientDao);
			clientDao.create(client);
			System.out.println("test1");
			createListClient(request, client);
		} else {
			resultat = "Échec de la creation du client.";
		}

		return client;
	}

	private void validationEmail(String email) throws Exception {
		if (email != null && !email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
			throw new Exception("Merci de saisir une adresse mail valide.");
		}

	}

	private void validationNom(String nom) throws Exception {
		if (nom != null && nom.length() < 2) {
			throw new Exception("Le nom du client doit contenir au moins 2 caractères.");
		} else if (nom == null) {
			throw new Exception("Le champ est obligatoire");
		}
	}

	private void validationPrenom(String prenom) throws Exception {
		if (prenom != null && prenom.length() < 2) {
			throw new Exception("Le prenom du client doit contenir au moins 2 caractères.");
		}
	}

	private void validationAdresse(String adresse) throws Exception {
		if (adresse != null && adresse.length() < 10) {
			throw new Exception("L'adresse du client doit contenir au moins 10 caractères.");
		} else if (adresse == null) {
			throw new Exception("Le champ est obligatoire");
		}

	}

	private void validationTelephone(String telephone) throws Exception {
		if (telephone != null && telephone.length() < 4) {
			throw new Exception("Le numero de telephome du client doit contenir au moins 4 caractères.");
		} else if (telephone == null) {
			throw new Exception("Le champ est obligatoire");
		}
	}

	private void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	private static String getValeurChamp(HttpServletRequest request, String nomChamp) {
		String valeur = request.getParameter(nomChamp);
		if (valeur == null || valeur.trim().length() == 0) {
			return null;
		} else {
			return valeur.trim();
		}
	}

	private void createListClient(HttpServletRequest request, Client client) {
		HttpSession session = request.getSession();
		listeClients = (Map<Long, Client>) session.getAttribute(ATT_LISTE);
		if (listeClients == null) {
			listeClients = new HashMap<Long, Client>();
		}
		listeClients.put(client.getId(), client);
		session.setAttribute(ATT_LISTE, listeClients);
	}

	public String enregistrerFichier(HttpServletRequest request, String chemin) throws Exception {
		Boolean misySary = (Boolean) request.getAttribute("misySary");
		String nomFichier = null;
		InputStream contenuFichier = null;
		if (misySary) {
			try {
				Part part = request.getPart(CHAMP_IMAGE);

				nomFichier = getNomFichier(part);

				if (nomFichier != null && !nomFichier.isEmpty()) {
					nomFichier = nomFichier.substring(nomFichier.lastIndexOf('/') + 1).substring(nomFichier.lastIndexOf('\\') + 1);

					/* Récupération du contenu du fichier */
					contenuFichier = part.getInputStream();
					/* Extraction du type MIME du fichier depuis l'InputStream */
					MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
					Collection<?> mimeTypes = MimeUtil.getMimeTypes(contenuFichier);

					/*
					 * Si le fichier est bien une image, alors son en-tête MIME commence par la chaîne "image"
					 */
					if (mimeTypes.toString().startsWith("image")) {
						/* Ecriture du fichier sur le disque */
						ecrireFichier(contenuFichier, nomFichier, chemin);
					} else {
						throw new Exception("Le fichier envoyé doit être une image.");
					}

				}
			} catch (IllegalStateException e) {
//				e.printStackTrace();
				throw new Exception("Les données envoyées sont trop volumineuses.");
			} catch (IOException e) {
//				e.printStackTrace();
				throw new Exception("Erreur de configuration du serveur.");
			} catch (ServletException e) {
//				e.printStackTrace();
				throw new Exception("Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier.");
			}

		}
		return nomFichier;
	}

	private static String getNomFichier(Part part) {
		/* Boucle sur chacun des paramètres de l'en-tête "content-disposition". */
		for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
			/* Recherche de l'éventuelle présence du paramètre "filename". */
			if (contentDisposition.trim().startsWith("filename")) {
				/*
				 * Si "filename" est présent, alors renvoi de sa valeur, c'est-à-dire du nom de fichier sans guillemets.
				 */
				return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
			}
		}

		return null;
	}

	/*
	 * Méthode utilitaire qui a pour but d'écrire le fichier passé en paramètre sur le disque, dans le répertoire donné et avec le nom donné.
	 */
	private void ecrireFichier(InputStream contenu, String nomFichier, String chemin) throws Exception {
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		try {

			entree = new BufferedInputStream(contenu, TAILLE_TAMPON);
			sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON);

			byte[] tampon = new byte[TAILLE_TAMPON];
			int longueur = 0;
			while ((longueur = entree.read(tampon)) > 0) {
				sortie.write(tampon, 0, longueur);
			}
		} finally {
			try {
				sortie.close();
			} catch (IOException ignore) {
			}
			try {
				entree.close();
			} catch (IOException ignore) {
			}
		}
	}

}
