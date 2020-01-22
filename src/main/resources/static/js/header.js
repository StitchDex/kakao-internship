$(document).ready(function() {
    $('.select2-tag').select2({
        'ajax' : {
            'url': '/guide/tag',
            'data': function (param) {
                return {'tag' : param.term}
            },
            'processResults': function (data) {
                Group(data); // grouping option

                var ret = $.map(data, function(obj){
                    obj.id = obj.id || obj.tag;
                    obj.text = obj.text || obj.tag;
                    return obj;
                })
                return {'results' : ret};
            }
        },
        'placeholder':"Search Tag"

    });
});

//tag select event
$('.select2-tag').on('select2:select', function(e){
    $.ajax
    ({
        'url':"/guide/menu",
        'data': {'doc_key' : e.params.data.doc_key},
        'success': function(){
            console.log("tag request succ");
        },
        'error' : function () {
            console.log("tag request fail");
        }
    })
});

function Group(data) {
    var optGroup = [{}];
    var opts = [];

    for(var i = 0; i < data.length; i ++)
    {

    }
}
