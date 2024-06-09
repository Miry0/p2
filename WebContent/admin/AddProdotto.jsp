<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">

<title>Aggiungi prodotto</title>
</head>
<body>


	<%@ include file="../fragments/header.jsp" %>
	<%@ include file="../fragments/menu.jsp" %>
	
	
	<div id="main" class="clear">
	
		<h2>AGGIUNGI PRODOTTO</h2>

	<form action="../catalogo" method="post" id="myform">
		<input type="hidden" name="action" value="add">
		<input type="hidden" name="page" value="admin/GestioneCatalogo.jsp"><br><br>
		<div class="tableRow">
		 <p>Nome:</p>
            <p><input type="text" name="nome" value="" required pattern="[A-Za-z0-9\s]+" title="Solo lettere, numeri e spazi"></p>
        </div>
        <div class="tableRow">
            <p>Descrizione:</p>
            <p><input type="text" name="descrizione" value="" required pattern=".{10,}" title="Almeno 10 caratteri"></p>
        </div>
        <div class="tableRow">
            <p>Iva:</p>
            <p><input type="text" name="iva" value="" required pattern="^\d{1,2}(\.\d{1,2})?$" title="Numero con massimo 2 cifre decimali"></p>
        </div>
        <div class="tableRow">
            <p>Prezzo:</p>
            <p><input type="text" name="prezzo" value="" required pattern="^\d+(\.\d{1,2})?$" title="Numero con massimo 2 cifre decimali"></p>
        </div>
        <div class="tableRow">
            <p>Data:</p>
            <p><input type="text" name="dataUscita" value="" required pattern="^\d{4}-\d{2}-\d{2}$" title="Formato data YYYY-MM-DD"></p>
        </div>
        <div class="tableRow">
            <p>Quantità:</p>
            <p><input type="number" name="quantità" value="" required min="1" title="Solo numeri positivi"></p>
        </div>
        <div class="tableRow">
            <p>Immagine:</p>
            <p><input type="text" name="img" value="" required pattern="https?://.+" title="URL valido"></p>
        </div>
        <div class="tableRow">
            <p>Piattaforma:</p>
            <p><input type="text" name="piattaforma" value="" required pattern="[A-Za-z\s]+" title="Solo lettere e spazi"></p>
        </div>
        <div class="tableRow">
            <p>Genere:</p>
            <p><input type="text" name="genere" value="" required pattern="[A-Za-z\s]+" title="Solo lettere e spazi"></p>
        </div>
        <div class="tableRow">
            <p>Descrizione dettagliata:</p>
            <p><input type="text" name="descDett" value="" pattern=".{0,}" title="Testo libero"></p>
        </div>
        <div class="tableRow">
            <button type="button" onclick="validateForm()">Invia</button>
        </div>
    </form>

    <div id="errorMessages" class="error"></div>

    <script>
        function showError(message) {
            var errorMessages = document.getElementById("errorMessages");
            errorMessages.innerText = message;
        }

        function validateForm() {
            var nome = document.forms["myform"]["nome"].value;
            var descrizione = document.forms["myform"]["descrizione"].value;
            var iva = document.forms["myform"]["iva"].value;
            var prezzo = document.forms["myform"]["prezzo"].value;
            var dataUscita = document.forms["myform"]["dataUscita"].value;
            var quantità = document.forms["myform"]["quantità"].value;
            var img = document.forms["myform"]["img"].value;
            var piattaforma = document.forms["myform"]["piattaforma"].value;
            var genere = document.forms["myform"]["genere"].value;

            var nomePattern = /^[A-Za-z0-9\s]+$/;
            var descrizionePattern = /.{10,}/;
            var ivaPattern = /^\d{1,2}(\.\d{1,2})?$/;
            var prezzoPattern = /^\d+(\.\d{1,2})?$/;
            var dataPattern = /^\d{4}-\d{2}-\d{2}$/;
            var imgPattern = /^https?:\/\/.+/;
            var textPattern = /^[A-Za-z\s]+$/;

            var errorMessage = "";

            if (!nomePattern.test(nome)) {
                errorMessage += "Nome non valido: solo lettere, numeri e spazi sono ammessi.\n";
            }
            if (!descrizionePattern.test(descrizione)) {
                errorMessage += "Descrizione non valida: deve contenere almeno 10 caratteri.\n";
            }
            if (!ivaPattern.test(iva)) {
                errorMessage += "IVA non valida: deve essere un numero con massimo 2 cifre decimali.\n";
            }
            if (!prezzoPattern.test(prezzo)) {
                errorMessage += "Prezzo non valido: deve essere un numero con massimo 2 cifre decimali.\n";
            }
            if (!dataPattern.test(dataUscita)) {
                errorMessage += "Data non valida: il formato deve essere YYYY-MM-DD.\n";
            }
            if (quantità < 1) {
                errorMessage += "Quantità non valida: deve essere un numero positivo.\n";
            }
            if (!imgPattern.test(img)) {
                errorMessage += "URL immagine non valido.\n";
            }
            if (!textPattern.test(piattaforma)) {
                errorMessage += "Piattaforma non valida: solo lettere e spazi sono ammessi.\n";
            }
            if (!textPattern.test(genere)) {
                errorMessage += "Genere non valido: solo lettere e spazi sono ammessi.\n";
            }

            if (errorMessage) {
                showError(errorMessage);
                return false;
            }

            // Se non ci sono errori, invia il modulo
            document.getElementById("myForm").submit();
        }
    </script>
	
	</div>

	<%@ include file="../fragments/footer.jsp" %>

</body>
</html>