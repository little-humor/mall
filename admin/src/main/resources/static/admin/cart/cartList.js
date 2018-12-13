layui.use(['table','layer','form','upload','carousel','colorpicker'], function(){
    var $ = layui.jquery;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var upload = layui.upload;
    var carousel = layui.carousel;


    var formIndex;
    var urlPrefix = parent.url_prefix;

    //数据表格
    var renderTable = table.render({
        elem: '#product',
        height: 'full',
        even: true,//隔行背景
        url: urlPrefix+'product/list.do', //数据接口
        // where: {},
        page: true, //开启分页
        cols: [[ //表头
            {field: 'id', title: 'ID', width:50, sort: true, fixed: 'left'},
            {field: 'categoryId', title: '分类'},
            {field: 'name', title: '名称', sort: true},
            {field: 'subtitle', title: '副标题'},
            {field: 'mainImage', title: '主图'},
            {field: 'price', title: '价格', sort: true},
            {field: 'stock', title: '库存', sort: true},
            {field: 'status', title: '状态', sort: true},
            {field: 'createTime', title: '创建时间', sort: true},
            {field: 'updateTime', title: '更新时间', sort: true},
            {field: '', title: '操作', width:180, toolbar:'#bar'}
        ]]
    });

    //轮播图实例
    var carouselRender = carousel.render({
        elem: '#carousel',
        width: '100%', //设置容器宽度
        arrow: 'always', //始终显示箭头
        interval: 5000
    });

    //文件上传实例
    upload.render({
        elem: '#upload', //绑定元素
        url: urlPrefix + 'common/uploadImages.do', //上传接口
        accept: 'images',//指定允许上传时校验的文件类型
        acceptMime: 'image/jpg',//规定打开文件选择框时，筛选出的文件类型
        size: 0,//设置文件最大可允许上传的大小，单位 KB  0（即不限制）
        multiple: true,//是否允许多文件上传
        number: 0,//设置同时可上传的文件数量
        done: function(res,index,upload){//每个文件提交一次触发一次
            //上传完毕回调
            //do something （比如将res返回的图片链接保存到表单的隐藏域）
            if(res.code == 0){
                console.log("done",res.data);
                $('#subImages').val(res.data+",");
                var image = '<img src="'+res.data+'">';
                $('#productImages').append(image);
            }else{
                layer.alert(res.msg);
            }

        },
        allDone: function(obj){ //当文件全部被提交后，才触发
            console.log(obj.total); //得到总文件数
            console.log(obj.successful); //请求成功的文件数
            console.log(obj.aborted); //请求失败的文件数
            renderCarouselOfUpload();
            if(obj.aborted!=0){
                layer.msg(obj.aborted+'张图片上传失败');
            }else{
                layer.msg('上传成功');
            }
        },
        error: function(index,upload){
            //请求异常回调
            //当上传失败时，你可以生成一个“重新上传”的按钮，点击该按钮时，执行 upload() 方法即可实现重新上传
        }
    });

    //颜色选择器
    // colorpicker.render({
    //     elem: '#productForm', //绑定元素
    //     predefine: true,
    //     colors: ['#F00','#0F0','#00F','rgb(255, 69, 0)','rgba(255, 69, 0, 0.5)']
    // });

    /**
     * 按钮功能----------------------------------start--------------------------------------
     */
    //搜索
    search = function(){
        layer.alert('搜索');
    };

    //数据表格监听事件
    table.on('tool(product)',function (obj) {//注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        debugger;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象

        if(layEvent === 'detail'){ //查看
            $('#productForm').removeClass("layui-hide");

            //渲染轮播图
            var subImages = data.subImages;
            renderCarouselOfEdit(subImages);

            //表单赋值
            form.val("product", data);
            formIndex = layer.open({
                type: 1,
                anim : 5,
                title: '查看商品',
                area: '800px',
                content: $('#productForm')
            });
        } else if(layEvent === 'del'){ //删除
            layer.confirm('是否要删除', function(index){

                layer.close(index);
                //向服务端发送删除指令
                $.ajax({
                    method: 'delete',
                    url: urlPrefix + 'product/deleteProducts.do',
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
            $('#productForm').removeClass("layui-hide");

            //渲染轮播图
            var subImages = data.subImages;
            renderCarouselOfEdit(subImages);

            //表单赋值
            form.val("product", data);
            formIndex = layer.open({
                type: 1,
                anim : 5,
                title: '编辑商品',
                area: '800px',
                content: $('#productForm')
            });
        }
    });


    //查询表单 按钮 监听
    form.on('submit(search)',function (data) {
        //全部表单字段，名值对形式：{name: value}
        var fields = data.field;
        fields = getCategoryId(fields);
        renderTable.reload({
            where: fields,
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //查询表单 分类下拉选择 监听
    form.on('select(searchCategory)', function(data){
        //得到被选中的值
        var value = data.value;
        $.ajax({
            url: urlPrefix+'product/findChildCategory.do',
            data: {
                categoryId: value
            },
            success:function (result,status,xhr) {
                var data = result.data;
                var searchSelectCategoryTwo = $('#searchSelectCategoryTwo');
                $.each(data,function (index, item) {
                    searchSelectCategoryTwo.append('<option value='+item.id+'>'+item.name+'</option>');
                });
                //刷新select选择框渲染 支持select、checkbox、radio
                form.render('select','search');
            },
            error:function (xhr,status,error) {
                console.log(status,error);
                layer.msg('加载「二级分类」失败');
            }
        });
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
            url: urlPrefix + 'product/addOrUpdate.do',
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
    $.ajax({
        url: urlPrefix+'product/findParentCategory.do',
        success:function (result,status,xhr) {
            var data = result.data;
            var categorySelect = $('#categorySelect');
            var searchSelectCategory = $('#searchSelectCategory');
            $.each(data,function (index, item) {
                categorySelect.append('<option value='+item.id+'>'+item.name+'</option>');
                searchSelectCategory.append('<option value='+item.id+'>'+item.name+'</option>');
            });
            debugger;
            //刷新select选择框渲染 支持select、checkbox、radio
            form.render('select','product');
            form.render('select','search');
        },
        error:function (xhr,status,error) {
            console.log(status,error);
            layer.msg('加载「一级分类」失败');
        }
    });

    /**
     * 上传图片 渲染到轮播图
     * @param url
     */
    function renderCarouselOfUpload(){
        carouselRender.reload();
        //改变轮播图左右箭头按钮类型，禁止触发submit监听事件
        $('#carousel button').attr('type',"button");
    }

    /**
     * 打开form表单 渲染图片到轮播图
     * @param subImages
     */
    function renderCarouselOfEdit(subImages){
        if(subImages){
            var arr = subImages.split(",");
            $.each(arr,function(index,url){
                if(url){
                    var image = '<img src="'+url+'">';
                    $('#productImages').append(image);
                }
            });
            renderCarouselOfUpload();
        }

    }

    /**
     * 获取选中的商品分类（目前选中最细的分类）
     * @param fields
     * @returns {*}
     */
    function getCategoryId(fields){
        if(fields.one){
            fields.categoryId = fields.one;
        }
        if(fields.two){categoryId
            fields.categoryId = fields.two;
        }
        delete fields.one;
        delete fields.two;

        return fields;
    }

});