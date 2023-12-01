package com.ti.servlets;

import java.io.IOException;

import com.ti.dao.ClientDAO;
import com.ti.dao.DAOFactory;
import com.ti.forms.SuppressionClientForm;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SuppressionClient extends HttpServlet {

	public static final String VUE_LISTE = "/listerClient";
	public static final String CHAMP_ID = "idClient";
	public static final String CONF_DAO_FACTORY = "daofactory";
	private ClientDAO clientDao;

	@Override
	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.clientDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getClientDao();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter(CHAMP_ID);
		SuppressionClientForm form = new SuppressionClientForm(clientDao);
		form.suppClient(request, id);
		response.sendRedirect(request.getContextPath() + VUE_LISTE);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(VUE_LISTE).forward(request, response);
	}
}