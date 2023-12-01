package com.ti.servlets;

import java.io.IOException;

import com.ti.beans.Client;
import com.ti.dao.ClientDAO;
import com.ti.dao.DAOFactory;
import com.ti.forms.CreationClientForm;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreationClient extends HttpServlet {

	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String CHEMIN = "chemin";
	public static final String ATT_FORM = "form";
	public static final String ATT_CLIENT = "client";
	public static final String VUE_SUCCESS = "/listerClient";
	public static final String VUE_ECHEC = "/WEB-INF/creerClient.jsp";
	private ClientDAO clientDao;

	@Override
	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.clientDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getClientDao();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(VUE_ECHEC).forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chemin = this.getServletConfig().getInitParameter(CHEMIN);
		request.setAttribute("misySary", true);
		CreationClientForm form = new CreationClientForm(clientDao);
		Client client = form.createClient(request, chemin);
		form.getListeClients();

		/* Stockage du formulaire et du bean dans l'objet request */
		request.setAttribute(ATT_FORM, form);
		request.setAttribute(ATT_CLIENT, client);

		if (form.getErreurs().isEmpty()) {
			response.sendRedirect(request.getContextPath() + VUE_SUCCESS);
		} else {
			this.getServletContext().getRequestDispatcher(VUE_ECHEC).forward(request, response);
		}
	}
}