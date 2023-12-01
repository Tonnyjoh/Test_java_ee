
<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Liste des commandes</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/inc/style.css"/>" />
</head>
<body>
	<div>
		<c:import url="/inc/menu.jsp"></c:import>
	</div>
	<div>
		<c:choose>
			<c:when test="${empty sessionScope.listeCommande}">
				<span class="">Aucune commande enregistrée.</span>
			</c:when>
			<c:otherwise>
				<table>
					<tr>
						<th>Id</th>
						<th>Client</th>
						<th>date</th>
						<th>Mode de paiement</th>
						<th>Statut du paiement</th>
						<th>Mode de livraison</th>
						<th>Statut de la livraison</th>
						<th>Montant</th>

						<th class="action">ACTION</th>
					</tr>

					<c:forEach items="${sessionScope.listeCommande}" var="commande"
						varStatus="status">
						<tr class="${ (status.count % 2==0)?'pair':'impair' }">
							<td><c:out value="${commande.value['id'] }"></c:out></td>
							<td><c:out value="${commande.value.client['nom']}" /> <c:out
									value="${commande.value.client['prenom']}" /></td>
							<td><c:out value="${commande.value['date']}" /></td>
							<td><c:out value="${commande.value['modePaiement']}" /></td>
							<td><c:out value="${commande.value['statutPaiement']}" /></td>
							<td><c:out value="${commande.value['modeLivraison']}" /></td>
							<td><c:out value="${commande.value['statutLivraison']}" /></td>
							<td><c:out value="${commande.value['montant']}" /></td>
							<td><a
								href="<c:url value="/supprimerCommande">
                                		<c:param name="idCommande" value="${commande.value['id']}"></c:param>
                                	</c:url>">
									X</a></td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>