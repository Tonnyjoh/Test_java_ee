package com.ti.filters;

import java.io.IOException;
import java.util.Map;

import com.ti.beans.Client;
import com.ti.dao.ClientDAO;
import com.ti.dao.CommandeDAO;
import com.ti.dao.DAOFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ChargementFilter implements Filter {

	public static final String ACCES_PUBLIC = "/accesPublic.jsp";
	public static final String CONNEXION = "/connexion";
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String ATT_LISTE_CLIENT = "listeClient";
	public static final String ATT_LISTE_COMMANDE = "listeCommande";

	private ClientDAO clientDao;
	private CommandeDAO commandeDao;

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.clientDao = ((DAOFactory) config.getServletContext().getAttribute(CONF_DAO_FACTORY)).getClientDao();
		this.commandeDao = ((DAOFactory) config.getServletContext().getAttribute(CONF_DAO_FACTORY)).getCommandeDao();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		/* Récupération de la session depuis la requête */
		HttpSession session = request.getSession();

		if (session.getAttribute(ATT_LISTE_CLIENT) == null) {
			/*
			 * Récupération de la liste des clients existants, et enregistrement en session
			 */
			Map<Long, Client> mapClients = clientDao.show();
			session.setAttribute(ATT_LISTE_CLIENT, mapClients);
		}
		if (session.getAttribute(ATT_LISTE_COMMANDE) == null) {
			Map<Long, Client> mapCommandes = commandeDao.show();
			session.setAttribute(ATT_LISTE_COMMANDE, mapCommandes);
		}

		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
	}
}