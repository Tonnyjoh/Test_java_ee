package com.ti.servlets;

import java.io.IOException;
import java.util.Map;

import com.ti.beans.Commande;
import com.ti.dao.ClientDAO;
import com.ti.dao.CommandeDAO;
import com.ti.dao.DAOFactory;
import com.ti.forms.CreationCommandeForm;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CreationCommande extends HttpServlet {

	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String CHEMIN = "chemin";
	public static final String ATT_COMMANDE = "commande";
	public static final String ATT_FORM = "form";
	public static final String PARA_CLIENT = "isNew";

	public static final String VUE_SUCCES = "/WEB-INF/afficherCommande.jsp";
	public static final String VUE_FORM = "/WEB-INF/creerCommande.jsp";
	private CommandeDAO commandeDao;
	private ClientDAO clientDao;

	@Override
	public void init() throws ServletException {

		this.commandeDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getCommandeDao();
		this.clientDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getClientDao();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getServletContext().getRequestDispatcher(VUE_FORM).forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chemin = this.getServletConfig().getInitParameter(CHEMIN);
		request.setAttribute("misySary", false);
		CreationCommandeForm form = new CreationCommandeForm(commandeDao, clientDao);
		Commande commande = form.creerCommande(request, isNewClient(request), chemin);
		request.setAttribute(ATT_COMMANDE, commande);
		request.setAttribute(ATT_FORM, form);

		if (form.getErreurs().isEmpty()) {
			this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(request, response);
		} else {
			this.getServletContext().getRequestDispatcher(VUE_FORM).forward(request, response);
		}
	}

	private static boolean isNewClient(HttpServletRequest request) {
		String value = request.getParameter(PARA_CLIENT);
		if (value != null && value.equals("oui")) {
			return true;
		} else if (value.equals("non")) {
			HttpSession session = request.getSession();
			Map<Long, Commande> listeComm = (Map<Long, Commande>) session.getAttribute("listeCommande");

			if (listeComm == null) {
				return true;
			}
		}
		return false;
	}

}