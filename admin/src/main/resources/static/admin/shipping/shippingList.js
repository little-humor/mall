layui.use(['table','layer','form','upload','carousel','colorpicker'], function(){
    var $ = layui.jquery;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;


    var formIndex;
    var urlPrefix = parent.url_prefix;

    //数据表格
    var renderTable = table.render({
        elem: '#shipping',
        height: 'full',
        even: true,//隔行背景
        url: urlPrefix+'shipping/list.do', //数据接口
        // where: {},
        page: true, //开启分页
        cols: [[ //表头
            {field: 'id', title: 'ID', width:50, sort: true, fixed: 'left'},
            {field: 'userId', title: '会员ID',fixed: 'left'},
            {field: 'receiverName', title: '姓名', sort: true,fixed: 'left'},
            {field: 'receiverPhone', title: '固定电话',width:100},
            {field: 'receiverMobile', title: '移动电话',width:100},
            {field: 'receiverProvince', title: '省份',width:100, sort: true},
            {field: 'receiverCity', title: '城市',width:100, sort: true},
            {field: 'receiverDistrict', title: '区/县',width:100, sort: true},
            {field: 'receiverAddress', title: '详细地址',width:200, sort: true},
            {field: 'receiverZip', title: '邮编',width:100, sort: true},
            {field: 'createTime', title: '创建时间',width:100, sort: true},
            {field: 'updateTime', title: '更新时间',width:100, sort: true},
            {field: '', title: '操作', width:180, toolbar:'#bar'}
        ]]
    });

    /**
     * 按钮功能----------------------------------start--------------------------------------
     */

    //数据表格监听事件
    table.on('tool(shipping)',function (obj) {//注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        debugger;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象

        if(layEvent === 'detail'){ //查看
            $('#shippingForm').removeClass("layui-hide");

            //表单赋值
            form.val("shipping", data);
            formIndex = layer.open({
                type: 1,
                anim : 5,
                title: '查看商品',
                area: '800px',
                content: $('#shippingForm')
            });
        } else if(layEvent === 'del'){ //删除
            layer.confirm('是否要删除', function(index){

                layer.close(index);
                //向服务端发送删除指令
                $.ajax({
                    method: 'delete',
                    url: urlPrefix + 'shipping/deleteProducts.do',
                    data: {
                        id: data.id
                    },
                    success: function (result, status, xhr) {
                        obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                        layer.msg(result.msg);
                    },
                    error: function (xhr, status, error) {
                        layer.msg('删除失败');
                    }
                })
            });
        } else if(layEvent === 'edit'){ //编辑
            $('#shippingForm').removeClass("layui-hide");

            //表单赋值
            form.val("shipping", data);
            formIndex = layer.open({
                type: 1,
                anim : 5,
                title: '编辑商品',
                area: '800px',
                content: $('#shippingForm')
            });
        }
    });


    //查询表单 按钮 监听
    form.on('submit(search)',function (data) {
        //全部表单字段，名值对形式：{name: value}
        var fields = data.field;
        renderTable.reload({
            where: fields,
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //提交表单 分类下拉选择 监听
    form.on('select(categorySelect)', function(data){
        //得到被选中的值
        var value = data.value;
        $.ajax({
            url: urlPrefix+'product/findChildCategory.do',
            data: {
                categoryId: value
            },
            success:function (result,status,xhr) {
                var data = result.data;
                var categorySelectTwo = $('#categorySelectTwo');
                $.each(data,function (index, item) {
                    categorySelectTwo.append('<option value='+item.id+'>'+item.name+'</option>');
                });
                //刷新select选择框渲染 支持select、checkbox、radio
                form.render('select','product');
            },
            error:function (xhr,status,error) {
                console.log(status,error);
                layer.msg('加载「二级分类」失败');
            }
        });
    });

    //表单 提交 监听
    form.on('submit(submit)',function (data) {
        //全部表单字段，名值对形式：{name: value}
        var fields = data.field;
        fields = getCategoryId(fields);
        $.ajax({
            method: 'POST',
            url: urlPrefix + 'shipping/addOrUpdate.do',
            data: fields,
            success: function (result,status,xhr) {
                layer.msg(result.msg);
                $('#productForm').addClass("layui-hide");
                layer.close(formIndex);
            },
            error: function (xhr, status, error) {
                layer.alert('提交失败');
            }
        });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        // return false;
    });

    /**
     * 按钮功能----------------------------------end--------------------------------------
     */

    //异步加载商品分类
    // $.ajax({
    //     url: urlPrefix+'product/findParentCategory.do',
    //     success:function (result,status,xhr) {
    //         var data = result.data;
    //         var categorySelect = $('#categorySelect');
    //         var searchSelectCategory = $('#searchSelectCategory');
    //         $.each(data,function (index, item) {
    //             categorySelect.append('<option value='+item.id+'>'+item.name+'</option>');
    //             searchSelectCategory.append('<option value='+item.id+'>'+item.name+'</option>');
    //         });
    //         debugger;
    //         //刷新select选择框渲染 支持select、checkbox、radio
    //         form.render('select','product');
    //         form.render('select','search');
    //     },
    //     error:function (xhr,status,error) {
    //         console.log(status,error);
    //         layer.msg('加载「一级分类」失败');
    //     }
    // });


});