layui.use(['table','layer','form','upload','carousel'], function(){
    var $ = layui.jquery;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;


    var formIndex;
    var urlPrefix = parent.url_prefix;

    //数据表格
    var renderTable = table.render({
        elem: '#order',
        height: 'full',
        even: true,//隔行背景
        url: urlPrefix+'order/list.do', //数据接口
        // where: {},
        page: true, //开启分页
        cols: [[ //表头
            {field: 'id', title: 'ID', width:50, sort: true, fixed: 'left'},
            {field: 'userId', title: '会员ID'},
            {field: 'shippingId', title: '收货地址ID', sort: true},
            {field: 'payment', title: '付款金额'},
            {field: 'postage', title: '运费'},
            {field: 'status', title: '状态', sort: true},
            {field: 'payment_time', title: '支付时间', sort: true},
            {field: 'send_time', title: '发货时间', sort: true},
            {field: 'end_time', title: '交易完成时间', sort: true},
            {field: 'close_time', title: '交易关闭时间', sort: true},
            {field: 'create_time', title: '创建时间', sort: true},
            {field: 'updateTime', title: '更新时间', sort: true},
            {field: '', title: '操作', width:180, toolbar:'#bar'}
        ]]
    });


    /**
     * 按钮功能----------------------------------start--------------------------------------
     */

    //数据表格监听事件
    table.on('tool(order)',function (obj) {//注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        debugger;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象

        if(layEvent === 'detail'){ //查看
            layer.msg("待开发");
        } else if(layEvent === 'del'){ //删除
            layer.confirm('是否要删除', function(index){

                layer.close(index);
                //向服务端发送删除指令
                $.ajax({
                    method: 'delete',
                    url: urlPrefix + 'order/deleteOrder.do',
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

    /**
     * 按钮功能----------------------------------end--------------------------------------
     */

});