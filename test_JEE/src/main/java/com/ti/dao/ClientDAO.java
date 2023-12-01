package com.ti.dao;

import java.util.Map;

import com.ti.beans.Client;

public interface ClientDAO {

	void create(Client client) throws DAOException;

	Client find(long id) throws DAOException;

	void delete(long id) throws DAOException;

	Map<Long, Client> show() throws DAOException;
}
