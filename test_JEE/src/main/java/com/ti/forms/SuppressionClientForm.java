package com.ti.forms;

import java.util.Map;

import com.ti.beans.Client;
import com.ti.dao.ClientDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SuppressionClientForm {

	public static final String ATT_LISTE = "listeClient";
	private Map<Long, Client> listeClients;
	private ClientDAO clientDao;

	public SuppressionClientForm(ClientDAO clientDao) {
		this.clientDao = clientDao;
	}

	public Map<Long, Client> getListeClients() {
		return listeClients;
	}

	public void suppClient(HttpServletRequest request, String id) {
		long idLong = Long.parseLong(id);
		clientDao.delete(idLong);
		HttpSession session = request.getSession();
		listeClients = (Map<Long, Client>) session.getAttribute(ATT_LISTE);
//		System.out.println(listeClients.get(idLong));
		listeClients.remove(idLong);
		session.setAttribute(ATT_LISTE, listeClients);
	}
}
