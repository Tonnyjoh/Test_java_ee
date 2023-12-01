package com.ti.servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListerClient extends HttpServlet {

	public static final String VUE_LISTE = "/WEB-INF/listerClients.jsp";
	public static final String ATT_LISTECLIENTS = "listeClients";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(VUE_LISTE).forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		CreationClientForm form = new CreationClientForm();
//		Map<String, Client> listeClients = form.getListeClients();

		/* Stockage du formulaire et du bean dans l'objet request */
//		request.setAttribute(ATT_LISTECLIENTS, listeClients);

		this.getServletContext().getRequestDispatcher(VUE_LISTE).forward(request, response);
	}
}