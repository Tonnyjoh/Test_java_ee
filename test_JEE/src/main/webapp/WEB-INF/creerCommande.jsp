<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Création d'une commande</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/inc/style.css"/>" />
</head>
<body>
	<c:import url="/inc/menu.jsp" />
	<div>
		<form method="post" action="<c:url value="/creationCommande"/>"  enctype="multipart/form-data">
			<%-- Petite astuce ici : placer le client accessible via  
                 l'EL ${ commande.client } dans une variable "client" de 
                 portée request, pour que le code du fragment fonctionne
                 à la fois depuis le formulaire de création d'un client 
                 et depuis celui de création d'une commande. --%>
			<c:set var="client" value="${ commande.client }" scope="request" />
			
			<label for="memoire">Nouveau client?<span class="requis">*</span></label>
			<input type="radio" name="isNew" value="oui" checked>Oui <input
				type="radio" name="isNew" value="non">Non<br>
			<div id="bloc1">
				<c:if test="${ !empty sessionScope.listeCommande }">
				<select name="clientChoisi">
					<option value="">Choisissez un client</option>
					<c:forEach items="${sessionScope.listeCommande}" var="commande">
						<option value="${commande.value.client['id'] }">
							<c:out value="${commande.value.client['nom']}" />
							<c:out value="${commande.value.client['prenom']}" />
						</option>
					</c:forEach>
				</select>
				</c:if>
			</div>
			<div id="bloc2">
				<c:import url="/inc/inc_client_form.jsp" />
			</div>

			<fieldset>
				<legend>Informations commande</legend>

				<label for="dateCommande">Date <span class="requis">*</span></label>
				<input type="text" id="v" name="dateCommande"
					value="<c:out value="${commande.date}"/>" size="30" maxlength="30"
					disabled /> <span class="erreur">${form.erreurs['dateCommande']}</span>
				<br /> <label for="montantCommande">Montant <span
					class="requis">*</span></label> <input type="text" id="montantCommande"
					name="montantCommande" value="<c:out value="${commande.montant}"/>"
					size="30" maxlength="30" /> <span class="erreur">${form.erreurs['montantCommande']}</span>
				<br /> <label for="modePaiementCommande">Mode de paiement <span
					class="requis">*</span></label> <input type="text"
					id="modePaiementCommande" name="modePaiementCommande"
					value="<c:out value="${commande.modePaiement}"/>" size="30"
					maxlength="30" /> <span class="erreur">${form.erreurs['modePaiementCommande']}</span>
				<br /> <label for="statutPaiementCommande">Statut du
					paiement</label> <input type="text" id="statutPaiementCommande"
					name="statutPaiementCommande"
					value="<c:out value="${commande.statutPaiement}"/>" size="30"
					maxlength="30" /> <span class="erreur">${form.erreurs['statutPaiementCommande']}</span>
				<br /> <label for="modeLivraisonCommande">Mode de livraison
					<span class="requis">*</span>
				</label> <input type="text" id="modeLivraisonCommande"
					name="modeLivraisonCommande"
					value="<c:out value="${commande.modeLivraison}"/>" size="30"
					maxlength="30" /> <span class="erreur">${form.erreurs['modeLivraisonCommande']}</span>
				<br /> <label for="statutLivraisonCommande">Statut de la
					livraison</label> <input type="text" id="statutLivraisonCommande"
					name="statutLivraisonCommande"
					value="<c:out value="${commande.statutLivraison}"/>" size="30"
					maxlength="30" /> <span class="erreur">${form.erreurs['statutLivraisonCommande']}</span>
				<br />
			</fieldset>
			<p class="info">${ form.resultat }</p>
			<input type="submit" value="Valider" /> <input type="reset"
				value="Remettre à zéro" /> <br />
		</form>
	</div>
</body>
</html>