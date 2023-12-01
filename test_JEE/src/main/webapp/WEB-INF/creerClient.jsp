<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Création d'un client</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/inc/style.css"/>" />
</head>
<body>

	<div>
        <c:import url="/inc/menu.jsp" />
		<form method="post" action="creationClient" enctype="multipart/form-data">
			<c:import url="/inc/inc_client_form.jsp"></c:import>
			<input type="file" id="imageCslient" name="imageClient" value="<c:out value="${fichier.nom}"/>" />
            <span class="erreur">${form.erreurs['imageClient']}</span>
            <br />
			
			<input type="submit" value="Valider" /> 
			<input type="reset"	value="Remettre à zéro" /> <br />
		</form>
	</div>
</body>
</html>