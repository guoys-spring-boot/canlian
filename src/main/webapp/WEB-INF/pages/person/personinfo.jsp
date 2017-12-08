<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <jsp:include page="${pageContext.request.contextPath}/common/reference.jsp"/>
</head>
<body class="easyui-layout">

<div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
    <div class="datagrid-toolbar">
        <c:if test="${action eq 'add'}">
            <a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
        </c:if>
        <c:if test="${action eq 'edit'}">
            <a id="update" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
        </c:if>
    </div>
</div>
<div region="center" style="height:600px;overflow:auto;padding:5px;" border="false">
    <form:form id="useForm" method="post" commandName="person" action="/business/addPerson">
        <table class="table-edit" width="95%" align="center">
            <tr class="title">
                <td colspan="4">基本信息</td>
            </tr>
            <tr>
                <td>姓名:</td>
                <td>
                    <form:input path="name" disabled="${disabled}" type="text"
                                    class="easyui-validatebox"/>
                </td>
                <td>性别:</td>
                <td>
                    <form:input path="sex" disabled="${disabled}" type="text"
                                class="easyui-validatebox"/>
                </td>
            </tr>
            <tr>
                <td>身份证号:</td>
                <td><form:input path="identity" disabled="${disabled}" type="text" class="easyui-validatebox" required="true"/></td>
                <td>残疾证号:</td>
                <td><form:input path="deformity" disabled="${disabled}" type="text" class="easyui-validatebox" required="true"/></td>
            </tr>
            <tr>
                <td>手机号:</td>
                <td><form:input path="phonenumber" disabled="${disabled}" type="text"  class="easyui-validatebox" required="true"/></td>
                <td>银行卡号:</td>
                <td>
                    <form:input path="bankid" disabled="${disabled}" type="text"
                                    class="easyui-validatebox" required="true" style="width: 150px;"/>
                </td>
            </tr>
            <tr class="title">
                <td colspan="4">其他信息</td>
            </tr>
            <tr>
                <td>残疾：</td>
                <td>
                    <form:input path="canji" disabled="${disabled}" type="text"
                                class="easyui-validatebox" required="true" style="width: 150px;"/>
                </td>
                <td>性质:</td>
                <td>
                    <form:input path="xingzhi" disabled="${disabled}" type="text"
                                class="easyui-combobox" required="true" style="width: 150px;"/>
                </td>
            </tr>
            <tr>
                <td>类型：</td>
                <td>
                    <form:input path="leixing" disabled="${disabled}" type="text"
                                class="easyui-combobox" required="true" style="width: 150px;"/>
                </td>
                <td>状态:</td>
                <td>
                    <form:input path="zhuangtai" disabled="${disabled}" type="text"
                                class="easyui-combobox" required="true" style="width: 150px;"/>
                </td>
            </tr>
            <tr>
                <td>区域：</td>
                <td>
                    <form:input path="quyu" disabled="${disabled}" type="text"
                                class="easyui-validatebox" required="true" style="width: 150px;"/>
                </td>
            </tr>
            <tr>
                <td>备注:</td>
                <td colspan="3"><form:textarea path="beizhu" disabled="${disabled}" required="true" style="width:80%"/></td>
            </tr>
            <tr>
                <td>单位地址:</td>
                <td colspan="3"><form:textarea path="address" disabled="${disabled}" required="true" style="width:80%"/></td>
            </tr>
        </table>
        <form:hidden path="id"/>
    </form:form>
    <script type="text/javascript">

        $(function () {

            $("#save").click(function () {
                var userForm = $("#useForm");
                userForm.form('submit', {
                    success: function () {
                        $(window).closeWindow('addUserWindow');
                    }
                })
            });

            $("#update").click(function () {
                var userForm = $("#useForm");
                if (userForm.form('validate')) {
                    userForm.attr("action", "/business/updatePerson");
                    userForm.submit();
                }

            });


            $.enumCombobox('zhuangtai', 'zhuangtai');
            $.enumCombobox('leixing', 'leixing');
            $.enumCombobox('xingzhi', 'xingzhi');
        });

    </script>
</div>
</body>
</html>