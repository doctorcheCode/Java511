<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Пекарня</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous" />
        <link href="resources/styles/default.css" rel="stylesheet" />
    </head>
    <body>
        <jsp:include page="../components/navbar.jsp" />
        <jsp:include page="../components/header.jsp" />
        <jsp:include page="../components/about.jsp" />
        <jsp:include page="../components/menu.jsp" />
        <jsp:include page="../components/contact.jsp" />
        <jsp:include page="../components/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
    </body>
</html>