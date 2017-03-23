<%-- 
    Document   : index
    Created on : 23-mar-2017, 16.19.54
    Author     : Marco
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <form action="findcategory" method="GET">
            <input type="text" name="genere" placeholder="Genere">
            <input type="submit" value="Ricerca">
        </form>
        <hr>
        <form action="findactor" method="GET">
            <input type="text" name="attore" placeholder="Cognome">
            <input type="submit" value="Ricerca">
        </form>
        <hr>
        
    </body>
</html>
