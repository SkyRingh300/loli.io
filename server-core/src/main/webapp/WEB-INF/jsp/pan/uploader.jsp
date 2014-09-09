<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.include file="../taglib.jsp" />
<div id="uploader">
  <div id="uploader-header">
    <div id="uploader-header-title"><label>上传文件</label></div>

    <div id="uploader-header-control">
      <span class="glyphicon glyphicon-minus uploader-header-min"></span><span class="glyphicon glyphicon-plus uploader-header-max"></span><span
        class="glyphicon glyphicon-remove uploader-header-close"></span>
    </div>
    <div style="clear: both;"></div>
  </div>
  <div id="uploader-content">
    <table class="table table-condensed table-bordered">
      <thead>
        <tr>
          <th class="upload-filelist-name"><div>文件名</div></th>
          <th class="upload-filelist-size"><div>大小</div></th>
          <th class="upload-filelist-folder"><div>目录</div></th>
          <th class="upload-filelist-progress"><div>进度</div></th>
          <th class="upload-filelist-button"><div>操作</div></th>
        </tr>
      </thead>
      <tbody>

      </tbody>
    </table>
  </div>



  <div id="uploader-footer"></div>
</div>