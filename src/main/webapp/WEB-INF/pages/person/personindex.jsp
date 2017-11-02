<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 导入jquery核心类库 -->
	<jsp:include page="${pageContext.request.contextPath}/common/reference.jsp" />
    <script type="text/javascript">
	function reloadGrid() {
        var elWin = $("#list").get(0).contentWindow;
        elWin.$("#grid").datagrid('reload', {
			name:$("#name").textbox('getValue'),
            repeatFlag:$("#repeatFlag").attr('checked'),
			identity:$("#identity").textbox('getValue'),
			leixing:$("#leixing").textbox('getValue'),
            quyu:$("#quyu").textbox('getValue'),
            zhuangtai:$("#zhuangtai").textbox('getValue'),
            uploadDate : $("#uploadDate").textbox('getValue')
        });
    }
	$(function(){
		$("body").css({visibility:"visible"});
		// 注册按钮事件
		$('#reset').click(function() {
			$('#form').form("clear");
		});
		// 注册所有下拉控件
		$("select").combobox( {
			width : 155,
			listWidth : 180,
			editable : true
		});
		// 注册ajax查询
		$('#ajax').click(function() {
			reloadGrid();
		});
		//reloadGrid();

        $.enumCombobox('leixing', 'leixing');
        $.enumCombobox('zhuangtai', 'zhuangtai');
        $.enumComboboxFromUrl('quyu', '${path}/business/listAllArea');

        $("#export").click(function () {
            window.location = "${path}/business/exportperson?name=" + $("#name").textbox('getValue') + "&identity=" + $("#identity").textbox('getValue') + "&leixing=" + $("#leixing").textbox('getValue') + "&quyu=" + $("#quyu").textbox('getValue') + "&zhuangtai=" + $("#zhuangtai").textbox('getValue') + "&repeatFlag=" + $("#repeatFlag").attr('checked') + "&uploadDate=" + $("#uploadDate").textbox('getValue')
        });

	});
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
    <div region="east" title="查询条件" icon="icon-forward" style="width:180px;overflow:auto;" split="false" border="true" >
		<div class="datagrid-toolbar">	
			<a id="reset" href="#" class="easyui-linkbutton" plain="true" icon="icon-reload">重置</a>
		</div>
		
		<form id="form" method="post" >
			<table class="table-edit" width="100%" >
				<tr><td>
					<b>姓名</b><span class="operator"><a name="username-opt" opt="all"></a></span>
					<input type="text" class="easyui-textbox" id="name" name="name"/>
				</td></tr>
				<tr><td>
					<b>身份证号</b><span class="operator"><a name="gender-opt" opt="all"></a></span>
					<input id="identity" class="easyui-textbox" name="identity" value="">
				</td></tr>
				<tr><td>
					<b>类型</b><span class="operator"><a name="birthday-opt" opt="all"></a></span>
					<input id="leixing" class="easyui-textbox" name="leixing" value="">
				</td></tr>
				<tr><td>
					<b>区域</b><span class="operator"><a name="birthday-opt" opt="all"></a></span>
					<input id="quyu" class="easyui-textbox" name="leixing" value="">
				</td></tr>
				<tr><td>
					<b>状态</b><span class="operator"><a name="birthday-opt" opt="all"></a></span>
					<input id="zhuangtai" class="easyui-textbox" name="zhuangtai" value="">
				</td></tr>
				<tr><td>
					<b>查询重复</b><span class="operator"><a name="birthday-opt" opt="all"></a></span>
					<input id="repeatFlag" type="checkbox" name="repeatFlag">
				</td></tr>
                <tr><td>
                    <b>上传时间</b><span class="operator"><a name="username-opt" opt="all"></a></span>
                    <input class="easyui-datebox" id="uploadDate" name="uploadDate"/>
                </td></tr>
			</table>
		</form>
		<div class="datagrid-toolbar">	
			<a id="ajax" href="#" class="easyui-linkbutton" plain="true" icon="icon-search">查询</a>
			<a id="export" href="#" class="easyui-linkbutton" plain="true" icon="icon-search">导出</a>
		</div>
    </div>
    <div region="center" style="overflow:hidden;" border="false">
		<iframe id="list" src="${pageContext.request.contextPath }/page.do?module=person&resource=personlist" scrolling="no" style="width:100%;height:100%;border:0;"></iframe>
    </div>
</body>
</html>