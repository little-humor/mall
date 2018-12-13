layui.use(['table','layer','form','upload','carousel','colorpicker'], function(){
    var $ = layui.jquery;
    var table = layui.table;
    var layer = layui.layer;


    var urlPrefix = parent.url_prefix;

    //数据表格
    var renderTable = table.render({
        elem: '#user',
        height: 'full',
        even: true,//隔行背景
        url: urlPrefix+'user/list.do', //数据接口
        // where: {},
        page: true, //开启分页
        cols: [[ //表头
            {field: 'id', title: 'ID', width:50, sort: true, fixed: 'left'},
            {field: 'username', title: '账户名称'},
            {field: 'password', title: '账户密码', sort: true},
            {field: 'email', title: '邮箱'},
            {field: 'phone', title: '联系方式'},
            {field: 'role', title: '角色', sort: true},
            {field: 'createTime', title: '创建时间', sort: true},
            {field: 'updateTime', title: '更新时间', sort: true},
            {field: '', title: '操作', width:180, toolbar:'#bar'}
        ]]
    });

    /**
     * 按钮功能----------------------------------start--------------------------------------
     */
    //数据表格监听事件
    table.on('tool(user)',function (obj) {//注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
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
                layer.msg("待开发");
            });
        } else if(layEvent === 'edit'){ //编辑
            layer.msg("待开发");
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