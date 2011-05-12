<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Shorty - An Open Source URL Shortner in Struts2/Hibernate</title>
<link href="css/style.css" rel="stylesheet"/>
</head>
<body>
<h1 class="title">Shorty</h1>
<br>
<p>A Simple URL Shortner in Struts2/Hibernate/MySQL</p>
<br><br>
<div id="link-container">

	<s:form action="add" method="post">
		<s:actionerror/>
		<s:textfield name="url" cssClass="link"/>
		<s:submit value="Shorten"/>
	</s:form>
	<s:if test="link.shortCode != null">
			<h3><s:text name="shorty.base.url"/><s:property value="link.shortCode"/></h3>
	</s:if>
	
</div>
</body>
</html>
