layui.use(['table','layer','form'], function(){
    var table = layui.table;
    var form = layui.form;


    var urlPrefix = parent.url_prefix;

    //数据表格
    var renderTable = table.render({
        elem: '#cart',
        height: 'full',
        even: true,//隔行背景
        url: urlPrefix+'cart/list.do', //数据接口
        // where: {},
        page: true, //开启分页
        cols: [[ //表头
            {field: 'id', title: 'ID', width:50, sort: true, fixed: 'left'},
            {field: 'userId', title: '会员ID'},
            {field: 'productId', title: '商品ID', sort: true},
            {field: 'quantity', title: '数量'},
            {field: 'checked', title: '是否勾选'},
            {field: 'createTime', title: '创建时间', sort: true},
            {field: 'updateTime', title: '更新时间', sort: true},
            {field: '', title: '操作', width:180, toolbar:'#bar'}
        ]]
    });

    /**
     * 按钮功能----------------------------------start--------------------------------------
     */

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