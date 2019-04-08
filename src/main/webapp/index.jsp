<%@ page language="java"  contentType="text/html; charset=UTF-8" %>
<html>
<body>
<h2>Hello World!</h2>
</body>
Springmvc 上传文件
<form name="form1" action="/manage/product/upload.do" METHOD="post" ENCTYPE="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="springmvc上传文件"/>
</form>
富文本图片上传
<form name="form1" action="/manage/product/richtext_img_upload.do" METHOD="post" ENCTYPE="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="富文本图片传文件"/>
</form>
</html>
