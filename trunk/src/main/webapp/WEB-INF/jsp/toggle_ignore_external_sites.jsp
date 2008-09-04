<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title>Toggle Ignore External Sites</title></head>
<body>
<c:if test="${model.ignoreExternalSites == 'true'}">
    <p>Now ignoring external sites.</p>
</c:if>
<c:if test="${model.ignoreExternalSites != 'true'}">
    <p>Now accessing external sites.</p>
</c:if>
</body>
</html>
