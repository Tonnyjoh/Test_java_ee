package com.ti.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Image extends HttpServlet {

	public static final int TAILLE_TAMPON = 10240; // 10ko

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chemin = this.getServletConfig().getInitParameter("chemin");

		String fichierRequis = request.getPathInfo();

		if (fichierRequis == null || "/".equals(fichierRequis)) {
			/*
			 * Si non, alors on envoie une erreur 404, qui signifie que la ressource demandée n'existe pas
			 */
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		/*
		 * Décode le nom de fichier récupéré, susceptible de contenir des espaces et autres caractères spéciaux, et prépare l'objet File
		 */
		fichierRequis = URLDecoder.decode(fichierRequis, "UTF-8");
		File fichier = new File(chemin, fichierRequis);

		/* Vérifie que le fichier existe bien */
		if (!fichier.exists()) {
			/*
			 * Si non, alors on envoie une erreur 404, qui signifie que la ressource demandée n'existe pas
			 */
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		/* Récupère le type du fichier */
		String type = getServletContext().getMimeType(fichier.getName());

		/*
		 * Si le type de fichier est inconnu, alors on initialise un type par défaut
		 */
		if (type == null) {
			type = "application/octet-stream";
		}

		/* Initialise la réponse HTTP */
		response.reset();
		response.setBufferSize(TAILLE_TAMPON);
		response.setContentType(type);
		response.setHeader("Content-Length", String.valueOf(fichier.length()));
		response.setHeader("Content-Disposition", "inline; filename=\"" + fichier.getName() + "\"");

		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		try {
			entree = new BufferedInputStream(new FileInputStream(fichier), TAILLE_TAMPON);
			sortie = new BufferedOutputStream(response.getOutputStream(), TAILLE_TAMPON);

			/* Lit le fichier et écrit son contenu dans la réponse HTTP */
			byte[] tampon = new byte[TAILLE_TAMPON];
			int longueur;
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