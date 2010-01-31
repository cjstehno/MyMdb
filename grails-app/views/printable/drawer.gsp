<html>
	<head>
		<title>Printable Box Drawer Index</title>
		<style type="text/css">
			table {
				width: 5.75in;
				background-color: black;
			}
			
			td {
				background-color: white;
				font-family: Arial;
				font-size: 7pt;
			}	
		</style>
	</head>
	<body>
	
		<table>
			<col width="33%" />
			<col width="34%" />
			<col width="33%" />
			<g:each in="${(1..20)}" var="idx">
				<tr>
					<td>
						${idx} - 
						<g:each in="${slots[idx]}" var="m">
							${m.title}<g:if test="${slots[idx].size() > 1}">,</g:if>
						</g:each>
					</td>
					<td>
						${idx+20} - 
						<g:each in="${slots[idx+20]}" var="m">
							${m.title}<g:if test="${slots[idx+20].size() > 1}">,</g:if>
						</g:each>						
					</td>
					<td>
						${idx+40} - 
						<g:each in="${slots[idx+40]}" var="m">
							${m.title}<g:if test="${slots[idx+40].size() > 1}">,</g:if>
						</g:each>						
					</td>
				</tr>
				
			</g:each>
			<tr>
				<td colspan="3" align="center">
					Storage Box ${name}
				</td>
			</tr>
		</table>
		
		<br />
		
		<table>
			<col width="33%" />
			<col width="34%" />
			<col width="33%" />
			<g:each in="${(61..80)}" var="idx">
				<tr>
					<td>
						${idx} - 
						<g:each in="${slots[idx]}" var="m">
							${m.title}<g:if test="${slots[idx].size() > 1}">,</g:if>
						</g:each>
					</td>
					<td>
						${idx+20} - 
						<g:each in="${slots[idx+20]}" var="m">
							${m.title}<g:if test="${slots[idx+20].size() > 1}">,</g:if>
						</g:each>						
					</td>
					<td>
						${idx+40} - 
						<g:each in="${slots[idx+40]}" var="m">
							${m.title}<g:if test="${slots[idx+40].size() > 1}">,</g:if>
						</g:each>						
					</td>
				</tr>
				
			</g:each>
			<tr>
				<td colspan="3" align="center">
					Storage Box ${name}
				</td>
			</tr>
		</table>		
		
	</body>
</html>