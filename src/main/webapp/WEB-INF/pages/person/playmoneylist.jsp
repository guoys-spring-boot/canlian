<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <!-- 导入jquery核心类库 -->
    <jsp:include page="${pageContext.request.contextPath}/common/reference.jsp"/>
    <script type="text/javascript">
        // 工具栏
        var toolbar = [];

        // 定义标题栏
        var columns = [[{
            field: 'id',
            checkbox: true,
            rowspan: 1
        },{
            field: 'bankid',
            title: '银行卡号',
            width: 140,
            rowspan: 1
        }, {
            field: 'bankname',
            title: '打款人',
            width: 80,
            rowspan: 1
        }, {
            field: 'money',
            title: '金额',
            width: 80,
            rowspan: 1
        }, {
            field: 'date',
            title: '日期',
            width: 200
        }
        ]];
        $(function () {
            // 初始化 datagrid
            // 创建grid
            $('#grid').datagrid({
                iconCls: 'icon-forward',
                pagination: true,
                fit: true,
                border: false,
                singleSelect: true,
                rownumbers: true,
                striped: true,
                toolbar: toolbar,
                url: "/playmoney/listPlayMoney?bankId=${requestScope.bankid}",
                idField: 'id',
                columns: columns
            });
            $("body").css({visibility: "visible"});
        });

    </script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
<div region="center" border="false">
    <table id="grid"></table>
</div>
</body>
</html>