package com.ti.dao;

import java.util.Map;

import com.ti.beans.Client;
import com.ti.beans.Commande;

public interface CommandeDAO {

	void create(Commande commande) throws DAOException;

	Commande find(long id) throws DAOException;

	void delete(long id) throws DAOException;

	Map<Long, Client> show() throws DAOException;
}
