package com.ti.servlets;

import java.io.IOException;

import com.ti.dao.CommandeDAO;
import com.ti.dao.DAOFactory;
import com.ti.forms.SuppressionCommandeForm;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SuppressionCommande extends HttpServlet {

	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String VUE_LISTE = "/listerCommande";
	public static final String CHAMP_DATE = "date";
	public static final String CHAMP_ID = "idCommande";
	private CommandeDAO commandeDao;

	@Override
	public void init() throws ServletException {

		this.commandeDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getCommandeDao();

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter(CHAMP_ID);
		SuppressionCommandeForm form = new SuppressionCommandeForm(commandeDao);
		form.suppCommande(request, id);
		response.sendRedirect(request.getContextPath() + VUE_LISTE);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(VUE_LISTE).forward(request, response);
	}
}