layui.use('element', function(){
    var $ = layui.jquery;
    var element = layui.element;


    var active = {
        tabAdd: function (title,url, id) {
            //新增一个Tab项
            element.tabAdd('mall', {
                title: title,
                content: '<iframe data-frameid="'+id+'" frameborder="0" name="content" scrolling="yes" width="100%" src="' + url + '"></iframe>',
                id: id
            });
            FrameWH();//计算框架高度
        },
        tabChange: function (id) {
            //切换到指定Tab项
            element.tabChange('mall', id);
            //切换后刷新框架
            $("iframe[data-frameid='"+id+"']").attr("src",$("iframe[data-frameid='"+id+"']").attr("src"))
        },
        tabDelete: function (id) {
            //删除
            element.tabDelete("mall", id);
        },
        tabDeleteAll: function (ids) {
            $.each(ids, function (i,item) {
                //删除全部
                element.tabDelete("mall", item);
            })
        }
    };

    //结合菜单展示内容
    $(".layui-side-scroll a").click(function () {
        var data = $(this);
        var title = data.html();
        var url = data.attr("data-url");
        var id = data.attr("data-id");
        if(url){
            if ($(".layui-tab-title li[lay-id]").length <= 0) {
                active.tabAdd(title,url,id);
            } else {
                var isData = false;
                $.each($(".layui-tab-title li[lay-id]"), function () {
                    if ($(this).attr("lay-id") == id) {
                        isData = true;
                    }
                })
                if (isData == false) {
                    active.tabAdd(title,url,id);
                }
            }
            active.tabChange(id);
        }
    });

    //计算iframe的高度，因为自动高度，会导致iframe挤到一起
    function FrameWH() {
        var h = $(window).height() -41- 10 - 60 -10-44 -10;
        $("iframe").css("height",h+"px");
    }
    $(window).resize(function () {
        FrameWH();
    });

});