<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Liste des clients</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/inc/style.css"/>" />
</head>

<body>
	<div>
		<c:import url="/inc/menu.jsp"></c:import>
	</div>
	<div>
		<c:choose>
			<c:when test="${empty sessionScope.listeClient}">
				<span class="">Aucun client enregistré.</span>
			</c:when>
			<c:otherwise>
				<table>
					<tr>
						<th>ID</th>
						<th>NOM</th>
						<th>PRENOM</th>
						<th>ADRESSE</th>
						<th>TELEPHONE</th>
						<th>EMAIL</th>
						<th>IMAGE</th>
						<th class="action">ACTION</th>
					</tr>

					<c:forEach items="${sessionScope.listeClient}" var="client"
						varStatus="status">
						<tr class="${ (status.count % 2==0)?'pair':'impair' }">
							<td><c:out value="${client.value['id']}" /></td>
							<td><c:out value="${client.value['nom']}" /></td>
							<td><c:out value="${client.value['prenom']}" /></td>
							<td><c:out value="${client.value['adresse']}" /></td>
							<td><c:out value="${client.value['numTel']}" /></td>
							<td><c:out value="${client.value['email']}" /></td>
							<td>
								<%-- On ne construit et affiche un lien vers l'image que si elle existe. --%>
								<c:if test="${ !empty client.value.image }">
									<c:set var="image">
										<c:out value="${ client.value.image }" />
									</c:set>
									<a href="<c:url value="/image/${ image }"/>">Voir</a>
								</c:if>
							<td><a
								href="<c:url value="/supprimerClient">
                                		<c:param name="idClient" value="${client.value['id']}"></c:param>
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