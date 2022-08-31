function fileupload(fileId, fileType){
	window.open(base+'/servlet/UploadServlet?method=uploadPage&fileId='+fileId+'&fileType='+fileType, "uploadW", "height=400, width=400, toolbar= yes, menubar=no, scrollbars=no, resizable=no, location=no, status=no,top=100,left=300")
}
function applypath(fileId, filePath){
	window.opener.document.getElementById(fileId).value = filePath;
}