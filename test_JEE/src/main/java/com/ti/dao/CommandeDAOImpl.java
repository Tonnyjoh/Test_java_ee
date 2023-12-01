package com.ti.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.ti.beans.Commande;

public class CommandeDAOImpl implements CommandeDAO {

	private static final String SQL_SELECT = "SELECT id, id_client, date, montant, mode_paiement, statut_paiement, mode_livraison, statut_livraison FROM Commande ORDER BY id";
	private static final String SQL_SELECT_PAR_ID = "SELECT id, id_client, date, montant, mode_paiement, statut_paiement, mode_livraison, statut_livraison FROM Commande WHERE id = ?";
	private static final String SQL_INSERT = "INSERT INTO Commande (id_client, date, montant, mode_paiement, statut_paiement, mode_livraison, statut_livraison) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_DELETE_PAR_ID = "DELETE FROM Commande WHERE id = ?";

	private DAOFactory daoFactory;

	CommandeDAOImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Commande find(long id) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Commande commande = null;

		try {

			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee(connexion, SQL_SELECT_PAR_ID, false, id);
			resultSet = preparedStatement.executeQuery();
			/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
			if (resultSet.next()) {
				commande = map(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtilitaire.fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return commande;
	}

	@Override
	public void create(Commande commande) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;
		System.out.println("test2");
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire
					.initialisationRequetePreparee(connexion, SQL_INSERT, true, commande.getClient().getId(), new Timestamp(commande.getDate().getMillis()), commande
							.getMontant(), commande.getModePaiement(), commande.getStatutPaiement(), commande.getModeLivraison(), commande.getStatutLivraison());
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException("Échec de la création de la commande, aucune ligne ajoutée dans la table.");
			}
			valeursAutoGenerees = preparedStatement.getGeneratedKeys();
			if (valeursAutoGenerees.next()) {
				commande.setId(valeursAutoGenerees.getLong(1));
			} else {
				throw new DAOException("Échec de la création de la commande en base, aucun ID auto-généré retourné.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtilitaire.fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
		}
	}

	@Override
	public Map show() throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Map<Long, Commande> listeCommandes = new HashMap<Long, Commande>();
		try {
			connection = daoFactory.getConnection();
			preparedStatement = connection.prepareStatement(SQL_SELECT);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Commande commande = null;
				commande = map(resultSet);
				listeCommandes.put(commande.getId(), commande);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtilitaire.fermeturesSilencieuses(resultSet, preparedStatement, connection);
		}

		return listeCommandes;
	}

	@Override
	public void delete(long id) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee(connexion, SQL_DELETE_PAR_ID, false, id);
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException("Échec de la suppression de la commande, aucune ligne supprimée de la table.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtilitaire.fermeturesSilencieuses(preparedStatement, connexion);
		}
	}

	/*
	 * Simple méthode utilitaire permettant de faire la correspondance (le mapping) entre une ligne issue de la table des commandes (un ResultSet) et un bean Commande.
	 */
	private Commande map(ResultSet resultSet) throws SQLException {
		Commande commande = new Commande();
		ClientDAOImpl clientDao = daoFactory.getClientDao();
		commande.setId(resultSet.getLong("id"));
		commande.setClient(clientDao.find(resultSet.getLong("id_client")));
		commande.setDate(new DateTime(resultSet.getTimestamp("date")));
		commande.setMontant(resultSet.getDouble("montant"));
		commande.setModePaiement(resultSet.getString("mode_paiement"));
		commande.setStatutPaiement(resultSet.getString("statut_paiement"));
		commande.setModeLivraison(resultSet.getString("mode_livraison"));
		commande.setStatutLivraison(resultSet.getString("statut_livraison"));
		return commande;
	}

}