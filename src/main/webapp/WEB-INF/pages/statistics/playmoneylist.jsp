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

        var dialogOptions = {
            onDestroy: function () {
                window.parent.reloadGrid();
                $("#addUserWindow").window('destroy');

            }
        };

        function doUpload() {
            $('#uploadExecelWindow').window('open');
            $("#uploadProgress").progressbar('setValue', 0);
            $("#uploadProgress").show();
            $("#uploadSuccess").hide();
            $("#fileId").val('');
            $("#file").textbox('setText', '');
            $("input[name='file']").val('');
            $("input[name='file']").AjaxFileUpload({
                action: "${path}/file/upload",
                progressBar: $("#uploadProgress"),
                onComplete: function (filename, response) {
                    //$(this)._alert("上传完成，请点击确定完成导入");
                    $("#fileId").val(response.id);
                    $("#uploadProgress").progressbar('setValue', 100);
                    $("#uploadSuccess").show();
                    $("#uploadProgress").hide();

                    $("input[name='file']").bind('change', function () {

                    });
                }
            });
        }

        function doImport() {
            if(!$("#date").val()){
                $(this)._alert("打款日期不能为空");
                return;
            }

            if(!$("#fileId").val()){
                $(this)._alert("请先上传excel");
                return;
            }
            $.ajax("${path}/playmoney/importPlayMoney", {
                data: {
                    fileId: $("#fileId").val(),
                    date : $("#date").val()
                },
                success: function (data) {
                    $(this)._alert(data);
                    $("#uploadExecelWindow").window('close');
                    window.parent.reloadGrid();
                }
            });
        }

        var xingzhi = $.loadEnum('xingzhi');
        var zhuangtai = $.loadEnum('zhuangtai');

    </script>
</head>
<body class="easyui-layout">
<div region="center" border="false">
    <table id="datagrid" class="easyui-datagrid"
           style="height: 97%"
           data-options="rownumbers:true,
           singleSelect:true,
           pagination:true,
           url:'${path}/statistics/playMoneyStatistics',
           method:'get',toolbar:'#tb'">
        <thead>
        <tr>
            <th data-options="field:'name',width:60">姓名</th>
            <th data-options="field:'sex',width:50">性别</th>
            <th data-options="field:'canji',width:70">残疾等级</th>
            <th data-options="field:'xingzhi',width:60,formatter: function (value, row, index) {return xingzhi[value];}">户口性质</th>
            <th data-options="field:'quyu',width:40">区域</th>
            <th data-options="field:'zhuangtai',width:40,formatter: function (value, row, index) {return zhuangtai[value];}">状态</th>
            <th data-options="field:'identity',width:150">身份证号</th>
            <th data-options="field:'deformity',width:160">残疾证号</th>
            <th data-options="field:'bankid',width:150">银行卡号</th>
            <th data-options="field:'bankname',width:80">银行打款人</th>
            <th data-options="field:'money',width:60,align:'right'">金额</th>
            <th data-options="field:'date',width:80">月份</th>
            <th data-options="field:'address',width:240">地址</th>
        </tr>
        </thead>
    </table>

    <div id="tb" style="padding:5px;height:auto">
        <div>
            <a href="#" class="easyui-linkbutton" onclick="doUpload()" iconCls="icon-add" data-options="handler:doUpload" plain="true">导入</a>
        </div>
    </div>

    <!--修改密码窗口-->
    <div id="uploadExecelWindow" class="easyui-window" title="上传excel" collapsible="false" minimizable="false" modal="true"
         closed="true" resizable="false"
         maximizable="false" icon="icon-save" style="width: 300px; height: 220px; padding: 5px;
        background: #fafafa">
        <input type="hidden" id="fileId" name="fileId">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <table width="90%">
                    <tr>
                        <td><input id="file" class="easyui-filebox" name="file" data-options="prompt:'请选择一个excel'"
                                   style="width:100%"></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            <div id="uploadProgress" class="easyui-progressbar" style="width:100%; padding: 0px"></div>
                            <div id="uploadSuccess" style="display: none">上传成功</div>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>打款日期：<input id="date" class="easyui-datebox" style="width: 73%" value=""></td>
                    </tr>
                </table>
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="doImport()">确定</a>
                <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"
                   onclick="$('#uploadExecelWindow').window('close')">取消</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>