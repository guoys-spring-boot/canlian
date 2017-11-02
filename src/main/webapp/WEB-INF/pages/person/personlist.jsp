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


        // 工具栏
        var toolbar = [{
            id: 'button-view',
            text: '修改',
            iconCls: 'icon-edit',
            handler: doEdit
        },{
            id: 'button-view',
            text: '银行卡号对比',
            iconCls: 'icon-edit',
            handler: doCompare
        }, {
            id: 'button-add',
            text: '新增',
            iconCls: 'icon-add',
            handler: doAdd
        }, {
            id: 'button-add',
            text: '新增导入',
            iconCls: 'icon-add',
            handler: doImport
        }, {
            id: 'button-delete',
            text: '删除',
            iconCls: 'icon-cancel',
            handler: doDelete
        }];


        var leixing = $.loadEnum('leixing');

        var xingzhi = $.loadEnum('xingzhi');

        var zhuangtai = $.loadEnum('zhuangtai')

        //定义冻结列
        var frozenColumns = [[{
            field: 'id',
            checkbox: true,
            rowspan: 1
        }, {
            field: 'name',
            title: '姓名',
            width: 70,
            rowspan: 1,
            sortable: false
        },
            {
                field: 'sex',
                title: '姓别',
                width: 30,
                rowspan: 1,
                sortable: false
            }]];


        // 定义标题栏
        var columns = [[{
            field: 'identity',
            title: '身份证号',
            width: 140,
            rowspan: 1
        }, {
            field: 'deformity',
            title: '残疾证号',
            width: 160,
            rowspan: 1
        }, {
            field: 'bankid',
            title: '银行卡号',
            width: 140,
            rowspan: 1,
            formatter : function(data, row, index) {
                return "<a href='#' onclick='toMoneyList(\""+data+"\")'>"+data+"</a>";
            }
        }, {
            field: 'quyu',
            title: '区域',
            width: 40
        },{
            field: 'address',
            title: '地址',
            width: 200
        }, {
            field: 'phonenumber',
            title: '手机号',
            width: 90
        }, {
            field: 'leixing',
            title: '类型',
            width: 86,
            formatter: function (value, row, index) {
                return leixing[value];
            }
        }, {
            field: 'canji',
            title: '残疾',
            width: 70
        }, {
            field: 'xingzhi',
            title: '性质',
            width: 50,
            formatter: function (value, row, index) {
                return xingzhi[value]
            }
        }, {
            field: 'zhuangtai',
            title: '状态',
            width: 86,
            formatter: function (value, row, index) {
                return zhuangtai[value]
            }
        }, {
            field: 'beizhu',
            title: '备注',
            width: 86
        }, {
            field: 'repeatFlag',
            title: '是否重复',
            width: 86
        }, {
            field: 'uploadDate',
            title: '上传日期',
            width: 86
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
                url: "/business/listperson",
                idField: 'id',
                frozenColumns: frozenColumns,
                columns: columns
            });


            $("body").css({visibility: "visible"});

            $("#addUserWindow").window('close');


        });

        function toMoneyList(data) {
            var url = '${pageContext.request.contextPath}/playmoney/toListPlayMoney?bankId=' + data;
            $(window).openWindow('addUserWindow', url, 650, 440, '打款记录', dialogOptions);
        }

        function doEdit() {
            var item = $('#grid').datagrid('getSelected');
            var url = '${pageContext.request.contextPath}/business/toUpdatePerson?personId=' + item.id;
            $(window).openWindow('addUserWindow', url, 650, 440, '人员管理', dialogOptions);
        }
        function doDelete() {
            var item = $('#grid').datagrid('getSelected');
            var url = '${pageContext.request.contextPath}/business/deletePerson?personId=' + item.id;
            $(this)._confirm("确定要删除么？", function () {
                $.ajax(url, {
                    success: function (data) {
                        $(this)._alert("成功");
                        window.parent.reloadGrid();
                    },
                    error:function (data) {
                        $(this)._alert(JSON.stringify(data, null, 4));
                    }
                });

            })

        }

        function doImport() {
            doOpenWindow('doImport');
        }

        function doAdd() {
            $(window).openWindow('addUserWindow',
                '${pageContext.request.contextPath}/business/toAddPerson',
                650, 440, '人员管理', dialogOptions);
        }

        function doCompare() {
            doOpenWindow('doCompare');
        }

        function doOpenWindow(c) {
            currentOps = c;
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


        var currentOps = 'doImport';
        function doUpload() {
            if (currentOps == 'doImport') {
                if(!$("#uploadDate").val()){
                    $(this)._alert("上传日期不能为空");
                    return;
                }
                $.ajax("${path}/business/importExcel", {
                    data: {
                        fileId: $("#fileId").val(),
                        uploadDate: $("#uploadDate").val()
                    },
                    success: function (data) {
                        $(this)._alert(data);
                        $("#uploadExecelWindow").window('close');
                        window.parent.reloadGrid();
                    },
                    error:function (data) {
                        $(this)._alert(JSON.stringify(data, null, 4));
                    }
                });
            } else {
                $.ajax("${path}/business/compareBankId", {
                    data: {
                        fileId: $("#fileId").val()
                    },
                    success: function (data) {
                        $("#uploadExecelWindow").window('close');
                        $("#bankIdWindow").window("open");
                        $("#bankid").datagrid({
                            data : data
                        })
                    },
                    error:function (data) {
                        $(this)._alert(JSON.stringify(data, null, 4));
                    }
                });
            }

        }
    </script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
<div region="center" border="false">
    <table id="grid"></table>
</div>

<!--修改密码窗口-->
<div id="uploadExecelWindow" class="easyui-window" title="上传excel" collapsible="false" minimizable="false" modal="true"
     closed="true" resizable="false"
     maximizable="false" icon="icon-save" style="width: 300px; height: 200px; padding: 5px;
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
                    <td>上传日期：<input id="uploadDate" class="easyui-datebox" style="width: 73%" value=""></td>
                </tr>
            </table>
        </div>
        <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
            <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="doUpload()">确定</a>
            <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"
               onclick="$('#uploadExecelWindow').window('close')">取消</a>
        </div>
    </div>
</div>



<!---->
<div id="bankIdWindow" class="easyui-window" title="银行卡号" collapsible="false" minimizable="false" modal="true"
     closed="true" resizable="false"
     maximizable="false" icon="icon-save" style="width: 300px; height: 400px; padding: 5px;
        background: #fafafa">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <table id="bankid" class="easyui-datagrid" style="width:250px;height:300px"
                   data-options="singleSelect:true,
                   collapsible:true">
                <thead>
                    <tr>
                        <th data-options="field:'bankid',width:160">银行卡号</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</body>
</html>