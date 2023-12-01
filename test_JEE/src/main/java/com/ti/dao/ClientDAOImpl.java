package com.ti.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ti.beans.Client;

public class ClientDAOImpl implements ClientDAO {

	private static final String SQL_SELECT = "SELECT id, nom, prenom, adresse, telephone, email, image FROM Client ORDER BY id";
	private static final String SQL_SELECT_PAR_ID = "SELECT id, nom, prenom, adresse, telephone, email, image FROM Client WHERE id = ?";
	private static final String SQL_INSERT = "INSERT INTO Client (nom, prenom, adresse, telephone, email, image) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String SQL_DELETE_PAR_ID = "DELETE FROM Client WHERE id = ?";
	private DAOFactory daoFactory;

	ClientDAOImpl(DAOFactory daoFact) {
		this.daoFactory = daoFact;
	}

	@Override
	public void create(Client client) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee(connexion, SQL_INSERT, true, client.getNom(), client.getPrenom(), client.getAdresse(), client
					.getNumTel(), client.getEmail(), client.getImage());
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException("Échec de la création du client, aucune ligne ajoutée dans la table.");
			}
			valeursAutoGenerees = preparedStatement.getGeneratedKeys();
			if (valeursAutoGenerees.next()) {
				client.setId(valeursAutoGenerees.getLong(1));
			} else {
				throw new DAOException("Échec de la création du client en base, aucun ID auto-généré retourné.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtilitaire.fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
		}
	}

	@Override
	public Client find(long id) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Client client = null;

		try {
			/* Récupération d'une connexion depuis la Factory */
			// System.out.println("Hello1");
			connexion = daoFactory.getConnection();

			// System.out.println(connexion);
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee(connexion, SQL_SELECT_PAR_ID, false, id);
//			System.out.println(preparedStatement);
			resultSet = preparedStatement.executeQuery();
			/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
			if (resultSet.next()) {
				client = map(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtilitaire.fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return client;

	}

	@Override
	public void delete(long id) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee(connexion, SQL_DELETE_PAR_ID, false, id);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtilitaire.fermeturesSilencieuses(preparedStatement, connexion);
		}

	}

	@Override
	public Map show() throws DAOException {

		Map<Long, Client> listeClients = new HashMap<Long, Client>();
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			connexion = daoFactory.getConnection();

			preparedStatement = DAOUtilitaire.initialisationRequetePreparee(connexion, SQL_SELECT, false);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Client client = null;
				client = map(resultSet);
				listeClients.put(client.getId(), client);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtilitaire.fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return listeClients;
	}

	private static Client map(ResultSet resultSet) throws SQLException {
		Client utilisateur = new Client();
		utilisateur.setId(resultSet.getLong("id"));
		utilisateur.setEmail(resultSet.getString("email"));
		utilisateur.setPrenom(resultSet.getString("prenom"));
		utilisateur.setNom(resultSet.getString("nom"));
		utilisateur.setNumTel(resultSet.getString("telephone"));
		utilisateur.setImage(resultSet.getString("image"));
		return utilisateur;
	}
}
