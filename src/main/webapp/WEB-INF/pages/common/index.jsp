<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>恩施市文明单位创建动态管理系统</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link id="easyuiTheme" rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<!-- 导入ztree类库 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/js/ztree/zTreeStyle.css"
	type="text/css" />
<script
	src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>

    <script
            src="${pageContext.request.contextPath }/js/platform/common.js"
            type="text/javascript"></script>
<script type="text/javascript">
	if(window.top!=window){
		top.location.href = window.location.href;		
	}
	// 初始化ztree菜单
	$(function() {
		var setting = {
			data : {
				simpleData : { // 简单数据 
					enable : true,
					pIdKey: "_parentId"
				}
			},
			callback : {
				onClick : onClick
			}
		};
		
		// 基本功能菜单加载
		$.ajax({
			url : '${pageContext.request.contextPath}/function_menu.do',
			type : 'GET',
			dataType : 'text',
			success : function(data) {
				var zNodes = eval("(" + data + ")");
				if(zNodes && zNodes.rows){
                    $.fn.zTree.init($("#treeMenu"), setting, zNodes.rows);
				}

			},
			error : function(msg) {
				alert('菜单加载异常!');
			}
		});
		
		var adminsetting = {
				data : {
					simpleData : { // 简单数据 
						enable : true
					}
				},
				callback : {
					onClick : onClick
				}
		};

		// 页面加载后 右下角 弹出窗口
		/**************/
		window.setTimeout(function(){
			$.messager.show({
				title:"消息提示",
				msg:'欢迎登录，${user.username}',
				timeout:5000
			});
		},3000);
		/*************/

		$("#btnCancel").click(function(){
			$('#editPwdWindow').window('close');
		});
		
		// 点击修改密码窗口 确定按钮，执行点击事件按钮
		$("#btnEp").click(function(){
			var txtNewPass = $('#txtNewPass').val().trim();
			var txtRePass = $('#txtRePass').val().trim();
			
			if(txtNewPass == ""){
				$.messager.alert('警告','密码不能为空！','warning');
				return ;
			}
			
			if(txtNewPass != txtRePass){
				$.messager.alert('警告','两次密码输入不一致！','warning');
				return ;
			}
			
			// 使用 ajax发起请求，将新密码提交到服务器 完成修改
			$.post("${pageContext.request.contextPath}/business/updatePassword",{password : txtNewPass}, function(data){
				if(data.success){
					// 成功
					$.messager.alert('信息',data.msg,'info');
				}else{
					// 失败
					$.messager.alert('错误',data.msg,'error');
				}
				$('#editPwdWindow').window('close');
			});
		});
	});

	function onClick(event, treeId, treeNode, clickFlag) {
		// 判断树菜单节点是否含有 page属性
		if (treeNode.page!=undefined && treeNode.page!= "") {

		    $(this)._openTab('tabs', treeNode.page, treeNode.name);

//			if ($("#tabs").tabs('exists', treeNode.name)) {// 判断tab是否存在
//				$('#tabs').tabs('select', treeNode.name); // 切换tab
//			} else {
//				// 开启一个新的tab页面
//				var content = '<div style="width:100%;height:100%;overflow:hidden;">'
//						+ '<iframe src="'
//						+ treeNode.page
//						+ '" scrolling="auto" style="width:100%;height:100%;border:0;" ></iframe></div>';
//
//				$('#tabs').tabs('add', {
//					title : treeNode.name,
//					content : content,
//					closable : true
//				});
//			}
		}
	}

	/*******顶部特效 *******/
	/**
	 * 更换EasyUI主题的方法
	 * @param themeName
	 * 主题名称
	 */
	changeTheme = function(themeName) {
		var $easyuiTheme = $('#easyuiTheme');
		var url = $easyuiTheme.attr('href');
		var href = url.substring(0, url.indexOf('themes')) + 'themes/'
				+ themeName + '/easyui.css';
		$easyuiTheme.attr('href', href);
		var $iframe = $('iframe');
		if ($iframe.length > 0) {
			for ( var i = 0; i < $iframe.length; i++) {
				var ifr = $iframe[i];
				$(ifr).contents().find('#easyuiTheme').attr('href', href);
			}
		}
	};
	// 退出登录
	function logoutFun() {
		$.messager
		.confirm('系统提示','您确定要退出本次登录吗?',function(isConfirm) {
			if (isConfirm) {
				location.href = '${pageContext.request.contextPath }/logout.do';
			}
		});
	}
	// 修改密码
	function editPassword() {
		$('#editPwdWindow').window('open');
	}
	// 版权信息
	function showAbout(){
		$.messager.alert("恩施市残联 v1.0","设计: xxx<br/> 管理员邮箱: gys@xxx.cn <br/> QQ: xxxxxx");
	}
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false"
		style="height:60px;padding:10px;background:url('${pageContext.request.contextPath }/images/header_bg.png') no-repeat right;">
		<div style="height: 100%" >
            <p style="font-size: 30px;height: 100%">恩施市残联『两补一贴』系统</p>
		</div>
		<div id="sessionInfoDiv"
			style="position: absolute;right: 5px;top:10px;">
			[<strong>==${user.username }==</strong>]，欢迎你！
		</div>
		<div style="position: absolute; right: 5px; bottom: 10px; ">
			<a href="javascript:void(0);" class="easyui-menubutton"
				data-options="menu:'#layout_north_pfMenu',iconCls:'icon-ok'">更换皮肤</a>
			<a href="javascript:void(0);" class="easyui-menubutton"
				data-options="menu:'#layout_north_kzmbMenu',iconCls:'icon-help'">控制面板</a>
		</div>
		<div id="layout_north_pfMenu" style="width: 120px; display: none;">
			<div onclick="changeTheme('default');">default</div>
			<div onclick="changeTheme('gray');">gray</div>
			<div onclick="changeTheme('black');">black</div>
			<div onclick="changeTheme('bootstrap');">bootstrap</div>
			<div onclick="changeTheme('metro');">metro</div>
		</div>
		<div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
			<div onclick="editPassword();">修改密码</div>
			<div onclick="showAbout();">联系管理员</div>
			<div class="menu-sep"></div>
			<div onclick="logoutFun();">退出系统</div>
		</div>
	</div>
	<div data-options="region:'west',split:true,title:'菜单导航'"
		style="width:200px">
		<div class="easyui-accordion" fit="true" border="false">
			<div title="基本功能" data-options="iconCls:'icon-mini-add'" style="overflow:auto">
				<ul id="treeMenu" class="ztree"></ul>
			</div>
		</div>
	</div>
	<div data-options="region:'center'">
		<div id="tabs" fit="true" class="easyui-tabs" border="false">

		</div>
	</div>
	<div data-options="region:'south',border:false"
		style="height:30px;padding:10px;background:url('${pageContext.request.contextPath }/images/header_bg.png') no-repeat right;">
		<table style="width: 100%;">
			<tbody>
				<tr>

				</tr>
			</tbody>
		</table>
	</div>

	<!--修改密码窗口-->
    <div id="editPwdWindow" class="easyui-window" title="修改密码" collapsible="false" minimizable="false" modal="true" closed="true" resizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 160px; padding: 5px;
        background: #fafafa">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <table cellpadding=3>
                    <tr>
                        <td>新密码：</td>
                        <td><input id="txtNewPass" type="Password" class="txt01" /></td>
                    </tr>
                    <tr>
                        <td>确认密码：</td>
                        <td><input id="txtRePass" type="Password" class="txt01" /></td>
                    </tr>
                </table>
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" >确定</a> 
                <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">取消</a>
            </div>
        </div>
    </div>
</body>
</html>